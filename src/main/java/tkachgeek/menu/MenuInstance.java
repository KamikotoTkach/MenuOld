package tkachgeek.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class MenuInstance implements Menu {
  public Inventory inventory;
  public Player player = null;
  public int clickedItem = -1;
  public boolean canClose = true;
  public boolean removeAfterClose = true;
  public HashMap<Integer, Runnable> buttons = new HashMap<>();
  public HashMap<Integer, Object> values = new HashMap<>();

  @Override
  public void ownInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }

  protected MenuInstance(Component title, InventoryType type) {
    inventory = Bukkit.createInventory(null, type, title);
  }
  protected MenuInstance() {
  }

  protected MenuInstance(Component title, int size) {
    inventory = Bukkit.createInventory(null, size, title);
  }

  public void setValue(int index, String value) {
    ItemStack item = inventory.getItem(index);
    ItemMeta meta = item.getItemMeta();
    List<Component> lore = new ArrayList<>();
    lore.add(Component.text(value));
    meta.lore(lore);
    inventory.getItem(index).setItemMeta(meta);
    values.put(index, value);
  }

  public void registerButton(int index, Material material, int amount, String name, List<Component> description, Runnable onCLick) {
    ItemStack item = Utils.getItem(material, amount, name, description);
    item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    inventory.setItem(index, item);
    buttons.put(index, onCLick);
  }
  public void registerButton(int index, ItemStack item, Runnable onCLick) {
    item.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
    inventory.setItem(index, item);
    buttons.put(index, onCLick);
  }

  public void getValue_double() {
    TextComponent message = Component.text("| ");
    for (int i = 0; i < 10; i++) {
      message = appendClickableValue(message, "0." + i);
    }
    message = appendClickableValue(message, "1");
    player.sendMessage(message);
    getValue();
  }

  public TextComponent appendClickableValue(TextComponent message, String text) {
    return message.append(Component.text(text))
            .clickEvent(ClickEvent.runCommand(text))
            .hoverEvent(HoverEvent.showText(Component.text(text)))
            .append(Component.text(" | "));

  }

  public void getValue_int() {
    TextComponent message = Component.text("| ");
    for (int i = 1; i < 11; i++) {
      message = appendClickableValue(message, String.valueOf(i));
    }
    player.sendMessage(message);
    getValue();
  }

  public void getValue() {
    player.sendTitle("Введите новое значение в чат", "", 10, 70, 20);
    player.closeInventory();

    Listener.getNextMessage(player, this);
  }


  @Override
  public Inventory getInventory() {
    return inventory;
  }

  @Override
  public void inventoryClick(InventoryClickEvent event) {
    clickedItem = event.getSlot();
    if (buttons.containsKey(clickedItem)) {
      buttons.get(clickedItem).run();
    }
    event.setCancelled(true);
  }

  @Override
  public void inventoryDrag(InventoryDragEvent event) {
    event.setCancelled(true);
  }
  
  @Override
  public void inventoryClose(InventoryCloseEvent event) {

    if (!canClose && event.getReason().equals(InventoryCloseEvent.Reason.PLAYER)) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Menus.plugin, () -> show(player), 1);
    } else if (removeAfterClose) {
      Menus.remove(this);
    }
  }

  public void setCanClose(boolean bool) {
    canClose = bool;
  }

  public void setRemoveAfterClose(boolean bool) {
    removeAfterClose = bool;
  }

  @Override
  public void show(Player player) {
    this.player = player;
    player.openInventory(inventory);
  }

  @Override
  public void returnValue(String value) {
    show(player);
    setValue(clickedItem, value);
  }
}