package me.erichards.bedwars.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.erichards.bedwars.Bedwars;
import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.generator.GeneratorType;
import me.erichards.bedwars.game.player.GamePlayer;
import me.erichards.bedwars.game.scoreboard.GameScoreboard;
import me.erichards.bedwars.game.scoreboard.LobbyScoreboard;
import me.erichards.bedwars.game.shop.Shop;
import me.erichards.bedwars.game.team.Team;
import me.erichards.bedwars.game.team.TeamManager;
import me.erichards.bedwars.utils.item.ItemBuilder;
import me.erichards.bedwars.utils.world.WorldUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Game {

    private static final Game instance = new Game();
    private GameState state;
    private List<NPC> npcs;
    private List<GamePlayer> players;

    public Game() {
        this.state = GameState.LOBBY;
        this.npcs = new ArrayList<>();
        this.players = new ArrayList<>();
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

    public List<GamePlayer> getPlayers() {
        return players;
    }

    public void addPlayer(GamePlayer player) {
        players.add(player);
    }

    public void removePlayer(GamePlayer player) {
        players.remove(player);
    }

    public void startLobby() {
        WorldUtils.generateWorld("Lobby", World.Environment.NORMAL);
        WorldUtils.cloneWorld("BeaconOriginal", "Beacon");

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
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match starting in " + ChatColor.RED + time + ChatColor.YELLOW + "..");
                    }
                    Bukkit.getOnlinePlayers().forEach(player -> LobbyScoreboard.setScoreboard(player, time));
                } else {
                    Bukkit.broadcastMessage(ChatColor.YELLOW + "Let the games begin!");
                    setState(GameState.ACTIVE);
                    startGame();

                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.setFoodLevel(20);
                        player.setHealth(20);
                        getPlayers().forEach(gamePlayer -> {
                            for(Map.Entry<Team, Location> spawnLocations : Bedwars.getInstance().getMapManager().getMapByName("Beacon").getSpawnLocations().entrySet()) { // Better system in the future!
                                if(gamePlayer.getTeam().equals(spawnLocations.getKey())) {
                                    gamePlayer.getSpigotPlayer().teleport(spawnLocations.getValue());
                                }
                            }
                        });
                    });
                }
            }, (waitTime - i) * 20);
        }
    }

    // UNUSED FOR NOW - WILL USE RANDOM MAPS & TEAMS
    public void startVoting() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR, 1).setDisplayName(ChatColor.GOLD + "Vote for Map" + ChatColor.GRAY + " (Right-click)").setLore(ChatColor.GRAY + "Vote for the upcoming game's map.").build());
        });
    }

    public void startGame() {
        startGameCountdown();

        for(Shop shop : Bedwars.getInstance().getMapManager().getMapByName("Beacon").getItemShops()) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" + ChatColor.BOLD + "ITEM SHOP");
            LookClose lookClose = new LookClose();
            lookClose.setRange(32);
            lookClose.setRealisticLooking(true);
            lookClose.linkToNPC(npc);
            lookClose.toggle();
            npc.addTrait(lookClose);
            npc.setProtected(true);
            npc.spawn(shop.getLocation());
            npcs.add(npc);
        }

        for(Shop shop : Bedwars.getInstance().getMapManager().getMapByName("Beacon").getTeamUpgrades()) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" + ChatColor.BOLD + "TEAM UPGRADES");
            LookClose lookClose = new LookClose();
            lookClose.setRange(32);
            lookClose.setRealisticLooking(true);
            lookClose.linkToNPC(npc);
            lookClose.toggle();
            npc.addTrait(lookClose);
            npc.setProtected(true);
            npc.spawn(shop.getLocation());
            npcs.add(npc);
        }

        int ironTime = 2 * 20;
        int goldTime = 6 * 20;
        for(Generator ironGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(Bedwars.getInstance().getMapManager().getMapByName("Beacon"), GeneratorType.IRON)) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> ironGenerator.getLocation().getWorld().dropItem(ironGenerator.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.IRON_INGOT, 1)).setVelocity(new Vector(0, 0, 0)), 0, ironTime);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> ironGenerator.getLocation().getWorld().dropItem(ironGenerator.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLD_INGOT, 1)).setVelocity(new Vector(0, 0, 0)), 0, goldTime);
        }

        int diamond = 30 * 20;
        for(Generator diamondGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(Bedwars.getInstance().getMapManager().getMapByName("Beacon"), GeneratorType.DIAMOND)) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), diamondGenerator.getLocation());
            hologram.appendTextLine(ChatColor.DARK_AQUA + "Tier I");
            //    Tier 2 every 24 seconds
            //    Tier 3 every 12 seconds
            hologram.appendTextLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Diamond Generator");
            TextLine line = hologram.appendTextLine("");
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
                diamondGenerator.getLocation().getWorld().dropItem(diamondGenerator.getLocation().getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.DIAMOND, 1)).setVelocity(new Vector(0, 0, 0));
            }, 0, diamond);
        }

        int emerald = 55 * 20;
        for(Generator emeraldGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(Bedwars.getInstance().getMapManager().getMapByName("Beacon"), GeneratorType.EMERALD)) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), emeraldGenerator.getLocation());
            hologram.appendTextLine(ChatColor.DARK_GREEN + "Tier I");
            hologram.appendTextLine(ChatColor.GREEN + "" + ChatColor.BOLD + "Emerald Generator");
            TextLine line = hologram.appendTextLine("");
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
                emeraldGenerator.getLocation().getWorld().dropItem(emeraldGenerator.getLocation().getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.EMERALD, 1)).setVelocity(new Vector(0, 0, 0));
            }, 0, emerald);
        }
    }

    public void endGame() {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("Bedwars");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage("Winner");
        Bukkit.broadcastMessage("Top Kills");
        Bukkit.broadcastMessage("Most Beds Broken");
        Bukkit.broadcastMessage("");

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.GRAY + "You are now being sent back to the lobby.");
            player.teleport(new Location(Bukkit.getWorld("Lobby"), 0, 64, 0));
        });

        CitizensAPI.getNPCRegistry().deregisterAll();
        WorldUtils.deleteWorld("Beacon");
//        startLobby();
    }

    /**
     * Starts the game countdown
     * Tier 2 Diamond Generators in 6 minutes
     * Tier 2 Emerald Generators in 6 minutes (after diamond)
     */
    private void startGameCountdown() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> {
            int time = 360;

            for (int i = time; i >= 0; --i) {
                final int countdown = i;

                Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                    if (countdown > 0) {
                        Bukkit.getOnlinePlayers().forEach(player -> GameScoreboard.setScoreboard(player, 0, 0, 0, countdown));
                    }
                    else {
                        Bukkit.broadcastMessage("Diamond generators are now Tier II!");

                        Bukkit.getScheduler().scheduleSyncRepeatingTask(Bedwars.getInstance(), () -> {
                            int emeraldTime = 360;

                            for (int emeraldI = emeraldTime; emeraldI >= 0; --emeraldI) {
                                final int emeraldCountdown = emeraldI;

                                Bukkit.getScheduler().scheduleSyncDelayedTask(Bedwars.getInstance(), () -> {
                                    if (emeraldCountdown > 0) {
                                        Bukkit.getOnlinePlayers().forEach(player -> GameScoreboard.setScoreboard(player, 0, 0, 0, emeraldCountdown));
                                    }
                                    else {
                                        Bukkit.broadcastMessage("Emerald generators are now Tier II!");
                                    }
                                }, (emeraldTime - emeraldI) * 20);
                            }
                        }, 0, 360 * 20);
                    }
                }, (time - i) * 20);
            }
        }, 0, 360 * 20);
    }
}
