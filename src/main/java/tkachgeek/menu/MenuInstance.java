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
import tkachgeek.tkachutils.items.ItemFactory;
import tkachgeek.tkachutils.messages.Message;
import tkachgeek.tkachutils.messages.MessagesUtils;
import tkachgeek.tkachutils.server.ServerUtils;

import java.util.Arrays;
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
  public MenuManager manager;
  
  @Override
  public void ownInventoryClick(InventoryClickEvent event) {
    event.setCancelled(true);
  }
  
  protected MenuInstance(Component title, InventoryType type) {
    if (ServerUtils.isVersionBefore1_16_5()) {
      String old_title = Message.getInstance(title).toString();
      inventory = Bukkit.createInventory(null, type, old_title);
    } else {
      inventory = Bukkit.createInventory(null, type, title);
    }
  }
  
  protected MenuInstance() {
  }
  
  protected MenuInstance(Component title, int size) {
    if (ServerUtils.isVersionBefore1_16_5()) {
      String old_title = Message.getInstance(title).toString();
      inventory = Bukkit.createInventory(null, size, old_title);
    } else {
      inventory = Bukkit.createInventory(null, size, title);
    }
  }
  
  public void setValue(int index, String value) {
    ItemStack item = inventory.getItem(index);
    if (item == null) {
      return;
    }
    
    List<Component> lore = List.of(Component.text(value));
    item.setItemMeta(ItemFactory.of(item).description(lore).build().getItemMeta());
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
  
  public void getValueDouble() {
    TextComponent message = Component.text("| ");
    for (int i = 0; i < 10; i++) {
      message = appendClickableValue(message, "0." + i);
    }
    message = appendClickableValue(message, "1");
    MessagesUtils.send(player, message);
    getValue();
  }
  
  public TextComponent appendClickableValue(TextComponent message, String text) {
    return message.append(Component.text(text))
                  .clickEvent(ClickEvent.runCommand(text))
                  .hoverEvent(HoverEvent.showText(Component.text(text)))
                  .append(Component.text(" | "));
  }
  
  public void getValueInt() {
    TextComponent message = Component.text("| ");
    for (int i = 1; i < 11; i++) {
      message = appendClickableValue(message, String.valueOf(i));
    }
    MessagesUtils.send(player, message);
    getValue();
  }
  
  public void getValue() {
    player.sendTitle("Введите новое значение в чат", "", 10, 70, 20);
    player.closeInventory();
    
    manager.listener.getNextMessage(player, this);
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
      Bukkit.getScheduler().scheduleSyncDelayedTask(manager.plugin, () -> show(player), 1);
    } else if (removeAfterClose) {
      manager.remove(this);
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