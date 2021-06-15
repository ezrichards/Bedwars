package me.etheus.bedwars.game.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * June 01, 2020
 */
public class Team {

    private String name;
    private ChatColor color;
    private boolean bedBroken;
    private Location spawnLocation;
    private List<Player> members;

    public Team(String name, ChatColor color) {
        this.name = name;
        this.color = color;
        this.bedBroken = false;
        this.spawnLocation = null;
        this.members = new ArrayList<>();
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

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public List<Player> getMembers() {
        return members;
    }

     public void addMember(Player member) {
        members.add(member);
     }

     public void removeMember(Player member) {
        members.remove(member);
     }
}
