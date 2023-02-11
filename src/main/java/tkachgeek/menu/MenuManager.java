package tkachgeek.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import tkachgeek.config.yaml.YmlConfigManager;

import java.util.HashMap;

import static org.bukkit.Bukkit.getServer;

public class MenuManager {
  final MenuListener listener;
  public JavaPlugin plugin;
  public YmlConfigManager yml;
  static HashMap<Inventory, Menu> initializedInventories = new HashMap<>();
  
  public MenuManager(JavaPlugin plugin, YmlConfigManager manager) {
    this.yml = manager;
    this.plugin = plugin;
    this.listener = new MenuListener(this);
    getServer().getPluginManager().registerEvents(listener, plugin);
  }
  
  public boolean isInitialized(Inventory inventory) {
    return initializedInventories.containsKey(inventory);
  }
  
  public void handleInventoryClick(InventoryClickEvent event) {
    initializedInventories.get(event.getInventory()).inventoryClick(event);
  }
  
  public void handleOwnInventoryClick(InventoryClickEvent event) {
    initializedInventories.get(event.getInventory()).ownInventoryClick(event);
  }
  
  public void handleInventoryDrag(InventoryDragEvent event) {
    initializedInventories.get(event.getInventory()).inventoryDrag(event);
  }
  
  public void handleInventoryClose(InventoryCloseEvent event) {
    initializedInventories.get(event.getInventory()).inventoryClose(event);
  }
  
  public void show(Player player, Menu menu) {
    initializedInventories.put(menu.getInventory(), menu);
    menu.show(player);
  }
  
  public void remove(Menu menu) {
    initializedInventories.remove(menu.getInventory());
  }
  
  public int activeMenus() {
    return initializedInventories.size();
  }
}
