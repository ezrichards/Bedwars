package me.erichards.bedwars.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.erichards.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Game {

    private static final Game instance = new Game();
    private GameState state;
    private GameMap map;

    public Game() {
        this.state = GameState.LOBBY;
    }

    public static Game getInstance() {
        return instance;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public void startLobby() {
        int waitTime = 61; // 120

        for (int i = waitTime; i >= 0; --i) {
            final int time = i;

            Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                if (time > 0) {
                    if(time == 60) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GREEN + "1" + ChatColor.YELLOW + " minute..");
                    }
                    else if(time == 30) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GREEN + "30" + ChatColor.YELLOW + " seconds..");
                    }
                    else if(time == 15) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.GOLD + "15" + ChatColor.YELLOW + " seconds..");
                    }
                    else if(time <= 5) {
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match starting in.. " + ChatColor.RED + time);
                    }
                } else {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Let the games begin!");
                    setState(GameState.INGAME);
                    startGame();
                }
            }, (waitTime - i) * 20);
        }
    }

    // UNUSED FOR NOW - WILL USE RANDOM MAPS
    public void startVoting() {}

    public void startGame() {
        Location[] ironGenerators = {
            new Location(Bukkit.getWorld("Bedwars1"), 61.5, 65.5, 0.5),
            new Location(Bukkit.getWorld("Bedwars1"), 0.5, 65.5, -60.5),
            new Location(Bukkit.getWorld("Bedwars1"), -60.5, 65.5, 0.5),
            new Location(Bukkit.getWorld("Bedwars1"), 0.5, 65.5, 61.5)
        };

        Location[] diamondGenerators = {
            new Location(Bukkit.getWorld("Bedwars1"), 42.5, 67, 42.5),
            new Location(Bukkit.getWorld("Bedwars1"), -41.5, 67, 42.5),
            new Location(Bukkit.getWorld("Bedwars1"), 42.5, 67, -41.5),
            new Location(Bukkit.getWorld("Bedwars1"), -41.5, 67, -41.5)
        };

        Location[] emeraldGenerators = {
            new Location(Bukkit.getWorld("Bedwars1"), -8.5, 70, 0.5),
            new Location(Bukkit.getWorld("Bedwars1"), 0.5, 70, -8.5),
            new Location(Bukkit.getWorld("Bedwars1"), 9.5, 70, 0.5),
            new Location(Bukkit.getWorld("Bedwars1"), 0.5, 70, 9.5)
        };

        for(Location ironGenerator : ironGenerators) {
            int ironTime = 2 * 20;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> ironGenerator.getWorld().dropItem(ironGenerator.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.IRON_INGOT, 1)).setVelocity(new Vector(0, 0, 0)), 0, ironTime);

            int goldTime = 6 * 20;
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> ironGenerator.getWorld().dropItem(ironGenerator.getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLD_INGOT, 1)).setVelocity(new Vector(0, 0, 0)), 0, goldTime);
        }

        for(Location diamondGenerator : diamondGenerators) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), diamondGenerator);
            TextLine generator = hologram.appendTextLine(ChatColor.DARK_AQUA + "Tier I");
            //    Tier 2 every 24 seconds
            //    Tier 3 every 12 seconds
            hologram.appendTextLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Diamond Generator");
            TextLine line = hologram.appendTextLine("30");
            hologram.appendItemLine(new ItemStack(Material.DIAMOND_BLOCK));

            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> {
                int diamondTime = 30;

                for (int i = diamondTime; i >= 0; --i) {
                    final int diamondCountdown = i;

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                        if (diamondCountdown > 0) {
                            line.setText(ChatColor.YELLOW + "Next diamond in.. " + ChatColor.GOLD + diamondCountdown);
                        }
                    }, (diamondTime - i) * 20);
                }
                diamondGenerator.getWorld().dropItem(diamondGenerator.getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.DIAMOND, 1)).setVelocity(new Vector(0, 0, 0));
            }, 0, 30 * 20); // delay not working..
        }

        for(Location emeraldGenerator : emeraldGenerators) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), emeraldGenerator);
            hologram.appendTextLine(ChatColor.GREEN + "" + ChatColor.BOLD + "Emerald Generator");
            TextLine line = hologram.appendTextLine("55");
            hologram.appendItemLine(new ItemStack(Material.EMERALD_BLOCK));

            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> {
                int emeraldTime = 55;

                for (int i = emeraldTime; i >= 0; --i) {
                    final int emeraldCountdown = i;

                    Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                        if (emeraldCountdown > 0) {
                            line.setText(ChatColor.YELLOW + "Next emerald in.. " + ChatColor.GOLD + emeraldCountdown);
                        }
                    }, (emeraldTime - i) * 20);
                }
                emeraldGenerator.getWorld().dropItem(emeraldGenerator.getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.EMERALD, 1)).setVelocity(new Vector(0, 0, 0));
            }, 0, 55 * 20);
        }
    }

    public void endGame() {

    }
}
