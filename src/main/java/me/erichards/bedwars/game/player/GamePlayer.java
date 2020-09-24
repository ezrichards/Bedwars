package me.erichards.bedwars.game.player;

import me.erichards.bedwars.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Made by Ethan Richards
 * May 30, 2020
 */
public class GamePlayer {

    private UUID player;
    private Team team;
    private int kills;
    private int finalKills;
    private int bedsBroken;

    public GamePlayer(UUID player, Team team, int kills, int finalKills, int bedsBroken) {
        this.player = player;
        this.team = team;
        this.kills = kills;
        this.finalKills = finalKills;
        this.bedsBroken = bedsBroken;
    }

    public UUID getPlayer() {
        return player;
    }

    public Player getSpigotPlayer() {
        return Bukkit.getPlayer(player);
    }

    public Team getTeam() {
        return team;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void setBedsBroken(int bedsBroken) {
        this.bedsBroken = bedsBroken;
    }
}