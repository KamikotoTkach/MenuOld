package tkachgeek.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public abstract class Menus {
  public static JavaPlugin plugin;
  static HashMap<Inventory, Menu> initializedInventories = new HashMap<>();
  
  public static void load(JavaPlugin plugin) {
    Menus.plugin = plugin;
    getServer().getPluginManager().registerEvents(new Listener(), plugin);
  }
  
  static public boolean isInitialized(Inventory inventory) {
    return initializedInventories.containsKey(inventory);
  }
  
  static public void handleInventoryClick(InventoryClickEvent event) {
    initializedInventories.get(event.getInventory()).inventoryClick(event);
  }
  
  static public void handleOwnInventoryClick(InventoryClickEvent event) {
    initializedInventories.get(event.getInventory()).ownInventoryClick(event);
  }
  
  static public void handleInventoryDrag(InventoryDragEvent event) {
    initializedInventories.get(event.getInventory()).inventoryDrag(event);
  }
  
  static public void handleInventoryClose(InventoryCloseEvent event) {
    initializedInventories.get(event.getInventory()).inventoryClose(event);
  }
  
  static public void show(Player player, Menu menu) {
    initializedInventories.put(menu.getInventory(), menu);
    menu.show(player);
  }
  
  static public void remove(Menu menu) {
    initializedInventories.remove(menu.getInventory());
  }
  
  static public int activeMenus() {
    return initializedInventories.size();
  }
}
