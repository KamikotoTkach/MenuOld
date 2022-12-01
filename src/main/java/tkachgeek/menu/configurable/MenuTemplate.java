package tkachgeek.menu.configurable;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tkachgeek.yaml.YmlConfig;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuTemplate extends YmlConfig {
  static List<String> possibleSizes = Arrays.asList("1x9", "2x9", "3x9", "4x9", "5x9", "6x9", "3x3", "1x5");
  String[] shape = {"abc", "def", "efg"};
  HashMap<Character, Button> items = new HashMap<>();
  
  public void shape(String[] shape) {
    this.shape = shape;
  }
  
  public void items(HashMap<Character, ItemStack> items) {
    for (Map.Entry<Character, ItemStack> item : items.entrySet()) {
      this.items.put(item.getKey(), new Button(item.getValue()));
    }
  }
  
  public void setItem(char character, ItemStack item) {
    this.items.put(character, new Button(item));
  }
  
  public void moveContents(List<ItemStack> direction) {
    int rows = shape.length;
    int columns = 0;
    
    for (String s : shape) {
      if (columns == 0) {
        columns = s.length();
        continue;
      }
      if (columns != s.length()) {
        columns = -1;
        break;
      }
    }
    if (columns == -1 || !possibleSizes.contains(rows + "x" + columns)) {
      throw new RuntimeException("Меню не может быть такого размера");
    }
    
    for (String s : shape) {
      for (int col = 0; col < columns; col++) {
        if (items.containsKey(s.charAt(col))) {
          direction.add(items.get(s.charAt(col)).createItem());
        } else {
          direction.add(new ItemStack(Material.AIR));
        }
      }
    }
  }
  
  public char characterAt(int slot) {
    return String.join("", shape).charAt(slot);
  }
}
