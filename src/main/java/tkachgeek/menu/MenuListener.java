package tkachgeek.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.WeakHashMap;

public class MenuListener implements org.bukkit.event.Listener {
  private final MenuManager menuManager;
  WeakHashMap<Player, Menu> toTakeValue = new WeakHashMap<>();
  
  public MenuListener(MenuManager menuManager) {
    this.menuManager = menuManager;
  }
  
  public void getNextMessage(Player player, Menu menuInterface) {
    toTakeValue.put(player, menuInterface);
  }
  
  @EventHandler
  void onInventoryClick(InventoryClickEvent event) {
    if (menuManager.isInitialized(event.getInventory())) {
      if (event.getClickedInventory() != null && !event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
        menuManager.handleInventoryClick(event);
      } else {
        menuManager.handleOwnInventoryClick(event);
      }
    }
  }
  
  @EventHandler
  void onInventoryClose(InventoryCloseEvent event) {
    if (menuManager.isInitialized(event.getInventory())) {
      menuManager.handleInventoryClose(event);
    }
  }
  
  @EventHandler
  void onInventoryDrag(InventoryDragEvent event) {
    if (menuManager.isInitialized(event.getInventory())) {
      menuManager.handleInventoryDrag(event);
    }
  }
  
  @EventHandler
  void onChatMessage(AsyncPlayerChatEvent event) {
    Bukkit.getScheduler().scheduleSyncDelayedTask(menuManager.plugin, () -> {
      if (toTakeValue.containsKey(event.getPlayer())) {
        toTakeValue.get(event.getPlayer()).returnValue(event.getMessage()); //todo:мб тут багуля, поменял эвент и метод, но не чекал
        toTakeValue.remove(event.getPlayer());
      }
    }, 0);
    if (toTakeValue.containsKey(event.getPlayer())) event.setCancelled(true);
  }
}
