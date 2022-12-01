package tkachgeek.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface SlotClick {
  void handle(ClickType clickType, ItemStack item);
}
