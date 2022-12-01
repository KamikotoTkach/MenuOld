package tkachgeek.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public interface Menu {
  Inventory getInventory();

  void inventoryClick(InventoryClickEvent event);

  void ownInventoryClick(InventoryClickEvent event);

  void inventoryDrag(InventoryDragEvent event);

  void inventoryClose(InventoryCloseEvent event);

  void show(Player player);

  void returnValue(String value);
}
