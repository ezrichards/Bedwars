package me.erichards.bedwars.utils.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.*;

/**
 * Made by Ethan Richards
 * June 03, 2020
 */
public class GUI {

//    TODO add page support, use abstract or interface for GUIItem click, remove maps, maybe separate class to initialize instead of initializing on interact..inherits GUI
    public static Map<UUID, GUI> activeGUIs = new HashMap<>();

    private String title;
    private int rows;
    private List<GUIItem> items;

    public GUI(String title, int rows) {
        this.title = title;
        this.rows = rows;

        this.items = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public int getRows() {
        return rows;
    }

    public List<GUIItem> getItems() {
        return items;
    }

    public void addItem(GUIItem item) {
        items.add(item);
    }

    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(null, rows * 9, title);

        for(GUIItem item : items) {
            inventory.setItem(item.getSlot(), item.getItemStack());
        }

        activeGUIs.put(player.getUniqueId(), this);
        player.openInventory(inventory);
    }
}
