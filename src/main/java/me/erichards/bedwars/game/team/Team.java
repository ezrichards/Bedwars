package me.erichards.bedwars.game.team;

import org.bukkit.ChatColor;

/**
 * Made by Ethan Richards
 * June 01, 2020
 */
public class Team {

    private String name;
    private ChatColor color;
    private boolean bedBroken;

    public Team(String name, ChatColor color, boolean bedBroken) {
        this.name = name;
        this.color = color;
        this.bedBroken = bedBroken;
    }

    public String getName() {
        return name;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isBedBroken() {
        return bedBroken;
    }

    public void setBedBroken(boolean bedBroken) {
        this.bedBroken = bedBroken;
    }
}
