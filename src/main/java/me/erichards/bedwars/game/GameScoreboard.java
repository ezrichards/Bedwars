package me.erichards.bedwars.game;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Made by Ethan Richards
 * June 18, 2020
 */
public class GameScoreboard {

    public static void setScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Map", "dummy", "Map");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS " + ChatColor.GOLD + ChatColor.UNDERLINE + "BETA");
        objective.getScore(ChatColor.GRAY + "Map: Beacon").setScore(1);
        objective.getScore("").setScore(2);
        objective.getScore(ChatColor.WHITE + "Beds Broken:").setScore(3);
        objective.getScore(ChatColor.WHITE + "Final Kills:").setScore(4);
        objective.getScore(ChatColor.WHITE + "Kills:").setScore(5); // add assists?
        objective.getScore(ChatColor.GOLD + "").setScore(6);
        objective.getScore(ChatColor.YELLOW + "Y" + ChatColor.WHITE + " Yellow: ").setScore(7);
        objective.getScore(ChatColor.AQUA + "B" + ChatColor.WHITE + " Blue: ").setScore(8);
        objective.getScore(ChatColor.GREEN + "G" + ChatColor.WHITE + " Green: ").setScore(9);
        objective.getScore(ChatColor.RED + "R" + ChatColor.WHITE + " Red: ").setScore(10);
        objective.getScore(ChatColor.YELLOW + "").setScore(11);

        player.setScoreboard(scoreboard);
    }
}
