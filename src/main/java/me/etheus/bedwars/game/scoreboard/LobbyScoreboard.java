package me.etheus.bedwars.game.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Made by Ethan Richards
 * September 14, 2020
 */
public class LobbyScoreboard {

    public static void setScoreboard(Player player, int time) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Map", "dummy", "Map");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS");
        objective.getScore("").setScore(1);
        if(Bukkit.getOnlinePlayers().size() >= 1) {
            objective.getScore(ChatColor.WHITE + "Starting in " + ChatColor.GREEN + time + "s").setScore(2);
        }
        else {
            objective.getScore(ChatColor.WHITE + "Waiting for players.").setScore(2);
        }
        objective.getScore(ChatColor.YELLOW + "").setScore(3);
        objective.getScore(ChatColor.WHITE + "Players: " + ChatColor.GREEN + Bukkit.getOnlinePlayers().size()).setScore(4);
        objective.getScore(ChatColor.WHITE + "Mode: " + ChatColor.GREEN + "1v1v1v1").setScore(5);
        objective.getScore(ChatColor.WHITE + "Map: " + ChatColor.GREEN + "VOTING").setScore(6);
        objective.getScore(ChatColor.RED + "").setScore(7);

        player.setScoreboard(scoreboard);
    }
}
