package me.ezrichards.bedwars.game.scoreboard;

import me.ezrichards.bedwars.Bedwars;
import me.ezrichards.bedwars.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.concurrent.TimeUnit;

/**
 * Made by Ethan Richards
 * June 18, 2020
 */
public class GameScoreboard {

    public static void setScoreboard(Player player, int bedsBroken, int finalKills, int kills, int countdown) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("Map", "dummy", "Map");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS");
        objective.getScore(ChatColor.GRAY + "Map: Beacon").setScore(1);
        objective.getScore("").setScore(2);
        objective.getScore(ChatColor.WHITE + "Beds Broken: " + ChatColor.GOLD + bedsBroken).setScore(3);
        objective.getScore(ChatColor.WHITE + "Final Kills: " + ChatColor.GOLD + finalKills).setScore(4);
        objective.getScore(ChatColor.WHITE + "Kills: " + ChatColor.GOLD + kills).setScore(5);
        objective.getScore(ChatColor.GOLD + "").setScore(6);
        objective.getScore(ChatColor.YELLOW + "Y" + ChatColor.WHITE + " Yellow: " + getBedIcon(Bedwars.getInstance().getTeamManager().getTeamByName("Yellow"))).setScore(7);
        objective.getScore(ChatColor.AQUA + "B" + ChatColor.WHITE + " Blue: " + getBedIcon(Bedwars.getInstance().getTeamManager().getTeamByName("Blue"))).setScore(8);
        objective.getScore(ChatColor.GREEN + "G" + ChatColor.WHITE + " Green: " + getBedIcon(Bedwars.getInstance().getTeamManager().getTeamByName("Green"))).setScore(9);
        objective.getScore(ChatColor.RED + "R" + ChatColor.WHITE + " Red: " + getBedIcon(Bedwars.getInstance().getTeamManager().getTeamByName("Red"))).setScore(10);
        objective.getScore(ChatColor.YELLOW + "").setScore(11);
        objective.getScore(ChatColor.WHITE + "Diamond II in " + ChatColor.GREEN + getTime(countdown)).setScore(12); // TODO make this more dynamic (w/ emeralds)
        objective.getScore(ChatColor.LIGHT_PURPLE + "").setScore(13);

        player.setScoreboard(scoreboard);
    }

    private static String getBedIcon(Team team) {
        return !team.isBedBroken() ? ChatColor.GREEN + "✔" : ChatColor.RED + "✘";
    }

    private static String getTime(int seconds) {
        if((TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60) < 10) {
            return TimeUnit.SECONDS.toMinutes(seconds) + ":0" + (TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60);
        }
        return TimeUnit.SECONDS.toMinutes(seconds) + ":" + (TimeUnit.SECONDS.toSeconds(seconds) - TimeUnit.SECONDS.toMinutes(seconds) * 60);
    }
}
