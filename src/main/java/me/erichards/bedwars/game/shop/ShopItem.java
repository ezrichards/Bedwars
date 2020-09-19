package me.erichards.bedwars.game.shop;

import org.bukkit.inventory.ItemStack;

/**
 * Made by Ethan Richards
 * September 07, 2020
 */
public class ShopItem {

    private ItemStack display;
    private ItemStack item;
    private int cost;
    private PayType type;

    public ShopItem(ItemStack display, ItemStack item, int cost, PayType type) {
        this.display = display;
        this.item = item;
        this.cost = cost;
        this.type = type;
    }

    public ItemStack getDisplay() {
        return display;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public PayType getType() {
        return type;
    }
}
