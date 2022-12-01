package tkachgeek.menu.configurable;

import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import tkachgeek.menu.MenuInstance;
import tkachgeek.menu.SlotClick;
import tkachgeek.yaml.YmlConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigurableMenu extends MenuInstance {
  HashMap<Character, SlotClick> clicks = new HashMap<>();
  MenuTemplate template = null;
  
  public ConfigurableMenu() {
    super(Component.text("title"), 27);
    loadFromFile("menus/container");
  }
  public ConfigurableMenu(Component title, int size, String path) {
    super(title, size);
    loadFromFile(path);
  }
  
  public void registerButton(char character, SlotClick onClick) {
    clicks.put(character, onClick);
  }
  
  protected void loadFromFile(String path) {
    template = YmlConfigManager.load(path, MenuTemplate.class);
    List<ItemStack> content = new ArrayList<>();
    template.moveContents(content);
    if (content.isEmpty()) return;
    inventory.setContents(content.toArray(new ItemStack[0]));
  }
  
  protected void saveToFile(String path) {
    MenuTemplate template = new MenuTemplate();
    byte c = 0;
    for (ItemStack item : inventory.getContents()) {
      if (item == null) continue;
      template.setItem((char) ('a' + c++), item);
    }
    template.store(path);
  }
  
  @Override
  public void inventoryClick(InventoryClickEvent event) {
    super.inventoryClick(event);
    if (template != null) {
      char clickedChar = template.characterAt(clickedItem);
      if (clicks.containsKey(clickedChar)) {
        clicks.get(clickedChar).handle(event.getClick(), event.getCurrentItem());
      }
    }
  }
  
  @Override
  public void inventoryClose(InventoryCloseEvent event) {
    //saveToFile("menus/container");
  }
}
