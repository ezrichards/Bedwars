package me.etheus.bedwars.game.player;

import me.etheus.bedwars.game.team.Team;
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

    public GamePlayer(UUID player, Team team) {
        this.player = player;
        this.team = team;
        this.kills = 0;
        this.finalKills = 0;
        this.bedsBroken = 0;
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

    public void addKill() {
        this.kills++;
    }

    public int getFinalKills() {
        return finalKills;
    }

    public void setFinalKills(int finalKills) {
        this.finalKills = finalKills;
    }

    public void addFinalKill() {
        this.finalKills++;
    }

    public int getBedsBroken() {
        return bedsBroken;
    }

    public void setBedsBroken(int bedsBroken) {
        this.bedsBroken = bedsBroken;
    }

    public void addBedBroken() {
        this.bedsBroken++;
    }
}
