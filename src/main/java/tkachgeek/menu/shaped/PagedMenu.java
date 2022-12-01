package tkachgeek.menu.shaped;

import org.bukkit.event.inventory.InventoryClickEvent;
import tkachgeek.menu.MenuInstance;

public class PagedMenu extends MenuInstance {
  public PagedMenuBuilder builder = new PagedMenuBuilder(this);
  
  protected PagedMenu() {
  }
  
  @Override
  public void inventoryClick(InventoryClickEvent event) {
    builder.onClick(event.getSlot(), event.getClick());
    super.inventoryClick(event);
  }
}
