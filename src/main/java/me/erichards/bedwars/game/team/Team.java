package me.erichards.bedwars.game.team;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Made by Ethan Richards
 * June 01, 2020
 */
public class Team {

    private String name;
    private ChatColor color;
    private boolean bedBroken;
    private List<Player> members;

    public Team(String name, ChatColor color, boolean bedBroken, List<Player> members) {
        this.name = name;
        this.color = color;
        this.bedBroken = bedBroken;
        this.members = members;
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
