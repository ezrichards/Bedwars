package me.erichards.bedwars.game.team;

import org.bukkit.ChatColor;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public enum TeamType {

    RED("Red", ChatColor.RED),
    GREEN("Green", ChatColor.GREEN),
    BLUE("Blue", ChatColor.BLUE),
    YELLOW("Yellow", ChatColor.YELLOW);

    private String name;
    private ChatColor color;

    TeamType(String name, ChatColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }
}
