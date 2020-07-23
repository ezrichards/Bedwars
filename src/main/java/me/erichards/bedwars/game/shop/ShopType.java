package me.erichards.bedwars.game.shop;

/**
 * Made by Ethan Richards
 * May 29, 2020
 */
public enum ShopType {

    SHOP("Item Shop"), UPGRADES("Team Upgrades");

    private String name;

    ShopType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
