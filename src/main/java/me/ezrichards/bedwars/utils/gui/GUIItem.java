package me.ezrichards.bedwars.utils.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

/**
 * Made by Ethan Richards
 * June 03, 2020
 */
public class GUIItem {

    private int slot;
    private ItemStack itemStack;

    public GUIItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void onClick(Player player, ClickType clickType) {}
}
