package tkachgeek.menu.shaped;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import tkachgeek.menu.MenuInstance;
import tkachgeek.menu.SlotClick;
import tkachgeek.tkachutils.items.ItemFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PagedMenuBuilder {
  String title = "";
  String shape;
  String[] shapeArr;
  ItemStack[] cached = null;
  Character dynamic = '#';
  
  HashMap<Character, ItemStack> staticItems = new HashMap<>();
  HashMap<Character, SlotClick> runnable = new HashMap<>();
  List<ItemStack> dynamicItems = new ArrayList<>();
  
  ItemStack nextItem = new ItemStack(Material.AIR);
  ItemStack previousItem = new ItemStack(Material.AIR);
  
  int page = 0;
  int col;
  int row;
  
  SlotClick nothing = (click, item) -> {
    Bukkit.broadcast(Component.text(click.name() + " " + item.getType()));
  };
  
  SlotClick next = (click, item) -> {
    page = Math.min(getMaxPage() - 1, page + 1);
    draw();
  };
  
  SlotClick previous = (click, item) -> {
    page = Math.max(0, page - 1);
    draw();
  };
  
  protected void updateControls() {
    ItemFactory.of(previousItem).name(Component.text("Страница " + (page + 1) + "/" + getMaxPage())).build();
    ItemFactory.of(nextItem).name(Component.text("Страница " + (page + 1) + "/" + getMaxPage())).build();
  }
  
  MenuInstance menuInstance;
  
  public PagedMenuBuilder(MenuInstance menuInstance) {
    this.menuInstance = menuInstance;
  }
  
  public int getMaxPage() {
    return dynamicItems.size() / calcEntries(shape, dynamic);
  }
  
  protected int calcEntries(String str, char character) {
    int counter = 0;
    for (char ch : str.toCharArray()) {
      if (character == ch) counter++;
    }
    return counter;
  }
  
  public PagedMenuBuilder shape(String shape) {
    this.shape = shape;
    shapeArr = shape.split("\n");
    col = -1;
    row = shapeArr.length;
    
    if (row < 1 || row > 6) {
      Bukkit.getLogger().warning("Невозможно сгенерировать меню " + col + "*" + row);
      return this;
    }
    
    for (String string : shapeArr) {
      int localCol = string.length();
      if (localCol != 3 && localCol != 9) {
        Bukkit.getLogger().warning("Невозможно сгенерировать меню " + col + "*" + row);
        return this;
      }
      
      if (col == -1) {
        col = localCol;
      } else if (localCol != col) {
        Bukkit.getLogger().warning("Невозможно сгенерировать меню " + col + "*" + row);
        return this;
      }
    }
    return this;
  }
  
  public PagedMenuBuilder ingredient(char character, ItemStack item) {
    staticItems.put(character, item);
    return this;
  }
  
  public PagedMenuBuilder nextPage(char character, ItemStack item) {
    staticItems.put(character, item);
    this.runnable.put(character, next);
    nextItem = item;
    return this;
  }
  
  public PagedMenuBuilder previousPage(char character, ItemStack item) {
    staticItems.put(character, item);
    this.runnable.put(character, previous);
    previousItem = item;
    return this;
  }
  
  public PagedMenuBuilder ingredient(char character, ItemStack item, SlotClick runnable) {
    staticItems.put(character, item);
    this.runnable.put(character, runnable);
    return this;
  }
  
  public PagedMenuBuilder setDynamicIngredient(char character, List<ItemStack> items) {
    dynamicItems = items;
    return this;
  }
  
  public PagedMenuBuilder setDynamicIngredient(char character, List<ItemStack> items, SlotClick runnable) {
    dynamicItems = items;
    this.dynamic = character;
    this.runnable.put(character, runnable);
    
    return this;
  }
  
  protected void generateContent() {
    int dynamicPointer = calcEntries(shape, dynamic) * page;
    while (page > 0 && !dynamicItems.isEmpty() && dynamicItems.size() < dynamicPointer) {
      page--;
      dynamicPointer = calcEntries(shape, dynamic) * page;
    }
    cached = new ItemStack[col * row];
    int slot = 0;
    for (String line : shapeArr) {
      for (char character : line.toCharArray()) {
        if (staticItems.containsKey(character)) {
          cached[slot] = staticItems.get(character);
        } else if (dynamic == character) {
          if (dynamicItems.size() <= dynamicPointer) continue;
          cached[slot] = dynamicItems.get(dynamicPointer++);
        }
        slot++;
      }
    }
  }
  
  public void build() {
    menuInstance.inventory = Bukkit.createInventory(null, col * row);
    draw();
  }
  
  public void build(InventoryType inventoryType) {
    if (inventoryType.getDefaultSize() != col * row) {
      Bukkit.getLogger().warning("Размеры инвентаря " + inventoryType.getDefaultTitle() + " не " + col + "*" + row);
    }
    menuInstance.inventory = Bukkit.createInventory(null, inventoryType);
    draw();
  }
  
  protected void draw() {
    generateContent();
    updateControls();
    menuInstance.inventory.setContents(cached);
  }
  
  void onClick(int slot, ClickType click) {
    runnable.getOrDefault(shapeArr[slot / col].charAt(slot % col), nothing).handle(click, cached[slot]);
  }
}
