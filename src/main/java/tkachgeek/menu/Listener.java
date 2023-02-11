package tkachgeek.menu;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.WeakHashMap;

public class Listener implements org.bukkit.event.Listener {
   static WeakHashMap<Player, Menu> toTakeValue = new WeakHashMap<>();

   public static void getNextMessage(Player player, Menu menuInterface) {
      toTakeValue.put(player, menuInterface);
   }

   @EventHandler
   void onInventoryClick(InventoryClickEvent event) {
      if (Menus.isInitialized(event.getInventory())) {
         if (event.getClickedInventory() != null && !event.getClickedInventory().equals(event.getWhoClicked().getInventory())) {
            Menus.handleInventoryClick(event);
         } else {
            Menus.handleOwnInventoryClick(event);
         }
      }
   }

   @EventHandler
   void onInventoryClose(InventoryCloseEvent event) {
      if (Menus.isInitialized(event.getInventory())) {
         Menus.handleInventoryClose(event);
      }
   }

   @EventHandler
   void onInventoryDrag(InventoryDragEvent event) {
      if (Menus.isInitialized(event.getInventory())) {
         Menus.handleInventoryDrag(event);
      }
   }

   @EventHandler
   void onChatMessage(AsyncPlayerChatEvent event) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(Menus.plugin, () -> {
         if (toTakeValue.containsKey(event.getPlayer())) {
            toTakeValue.get(event.getPlayer()).returnValue(event.getMessage()); //todo:мб тут багуля, поменял эвент и метод, но не чекал
            toTakeValue.remove(event.getPlayer());
         }
      }, 0);
      if (toTakeValue.containsKey(event.getPlayer())) event.setCancelled(true);

   }
}
