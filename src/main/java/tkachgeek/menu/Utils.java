package tkachgeek.menu;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class Utils {
  
  public static int calc(Player p, ItemStack s) {
    int count = 0;
    for (int i = 0; i < p.getInventory().getSize(); i++) {
      ItemStack stack = p.getInventory().getItem(i);
      if (stack == null)
        continue;
      if (s.isSimilar(stack)) {
        count += stack.getAmount();
      }
    }
    return count;
  }
  
  public static void clear(Player p, ItemStack s, int c) {
    for (int i = 0; i < p.getInventory().getSize(); i++) {
      ItemStack stack = p.getInventory().getItem(i);
      if (stack == null)
        continue;
      if (s.isSimilar(stack)) {
        if (stack.getAmount() == 0)
          break;
        if (stack.getAmount() <= c) {
          c = c - stack.getAmount();
          stack.setAmount(-1);
        }
        if (stack.getAmount() > c) {
          stack.setAmount(stack.getAmount() - c);
          c = 0;
        }
      }
    }
  }
  
  public static ItemStack getItem(Material material, int amount, String name, List<Component> description) {
    return getItem(material, amount, Component.text(name), description);
  }
  
  public static ItemStack getItem(Material material, int amount, TextComponent name) {
    ItemStack item = new ItemStack(material, amount);
    ItemMeta meta = item.getItemMeta();
    meta.displayName(name);
    item.setItemMeta(meta);
    return item;
  }
  
  public static ItemStack getItem(Material material, int amount, TextComponent name, List<Component> description) {
    ItemStack item = new ItemStack(material, amount);
    ItemMeta meta = item.getItemMeta();
    meta.displayName(name);
    meta.lore(description);
    item.setItemMeta(meta);
    return item;
  }
}
