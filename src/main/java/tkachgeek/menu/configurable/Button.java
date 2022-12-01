package tkachgeek.menu.configurable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.tkachutils.items.ItemFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Button {
  String name;
  String material;
  List<String> description;
  int amount;
  
  public Button() {
  }
  
  Button(ItemStack item) {
    name = MiniMessage.get().serialize(item.displayName());
    description = new ArrayList<>();
    amount = item.getAmount();
    material = item.getType().toString();
    
    if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
      for (Component component : item.getItemMeta().lore()) {
        description.add(MiniMessage.get().serialize(component));
      }
    }
  }
  
  @JsonIgnore
  ItemStack createItem() {
    assert name != null;
    return ItemFactory.of(Material.valueOf(material))
       .name(MiniMessage.get().parse(name).decoration(TextDecoration.ITALIC, false))
       .description(description.stream().map(x -> MiniMessage.get().parse(x).decoration(TextDecoration.ITALIC, false)).collect(Collectors.toList()))
       .amount(amount)
       .build();
  }
}
