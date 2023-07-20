package me.ezrichards.bedwars.game;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import me.ezrichards.bedwars.Bedwars;
import me.ezrichards.bedwars.game.generator.Generator;
import me.ezrichards.bedwars.game.generator.GeneratorType;
import me.ezrichards.bedwars.game.map.GameMap;
import me.ezrichards.bedwars.game.player.GamePlayer;
import me.ezrichards.bedwars.game.scoreboard.GameScoreboard;
import me.ezrichards.bedwars.game.scoreboard.LobbyScoreboard;
import me.ezrichards.bedwars.game.team.Team;
import me.ezrichards.bedwars.utils.Utils;
import me.ezrichards.bedwars.utils.item.ItemBuilder;
import me.ezrichards.bedwars.utils.tasks.DelayedTask;
import me.ezrichards.bedwars.utils.tasks.RepeatingTask;
import me.ezrichards.bedwars.utils.world.WorldUtils;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.LookClose;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Made by Ethan Richards
 * May 26, 2020
 */
public class Game {

    private static final Game instance = new Game();
    private GameState state;
    private GameMap map;
    private List<GamePlayer> players;
    private List<DelayedTask> delayedTasks;
    private List<RepeatingTask> repeatingTasks;

    public Game() {
        this.state = GameState.LOBBY;
        this.map = Bedwars.getInstance().getMapManager().getMapByName("Beacon");
        this.players = new ArrayList<>();
        this.delayedTasks = new ArrayList<>();
        this.repeatingTasks = new ArrayList<>();
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

    /**
     * Find the specified GamePlayer by their UUID.
     * @param uuid UUID of player to find
     * @return GamePlayer of player found or null if not found.
     */
    public GamePlayer getPlayerByUUID(UUID uuid) {
        for(GamePlayer gamePlayer : getPlayers()) {
            if(gamePlayer.getSpigotPlayer().getUniqueId().equals(uuid)) {
                return gamePlayer;
            }
        }
        return null;
    }

    public GamePlayer getMostKills() {
        GamePlayer topPlayer = null;
        int top = 0;

        for(GamePlayer gamePlayer : getPlayers()) {
            if(gamePlayer.getFinalKills() > top) {
                top = gamePlayer.getFinalKills();
                topPlayer = gamePlayer;
            }
        }
        return topPlayer;
    }

    public GamePlayer getMostBedsBroken() {
        GamePlayer topPlayer = null;
        int top = 0;

        for(GamePlayer gamePlayer : getPlayers()) {
            if(gamePlayer.getBedsBroken() > top) {
                top = gamePlayer.getBedsBroken();
                topPlayer = gamePlayer;
            }
        }
        return topPlayer;
    }

    public Team getWinner() {
        for(Team team : Bedwars.getInstance().getTeamManager().getTeams()) { // TODO teams to be stored in Game? TODO: Plus, clear teams/refresh upon finish
            // TODO remove players or signify they're dead from team once final killed?
        }
        return null;
    }

    /**
     * Respawn the specified player.
     * @param player Player to respawn
     * @param auto Whether or not to do it automatically
     *             (useful if spawning for the first time)
     */
    public void respawnPlayer(Player player, boolean auto) {
        GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

        int time = 5 * 20;
        if(auto) {
            time = 0;
        }

        Bukkit.getScheduler().runTaskLater(Bedwars.getInstance(), () -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(gamePlayer.getTeam().getSpawnLocation());
            player.setHealth(20);
            player.setFoodLevel(20);
            player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).build()); // TODO dye this armor or check for upgrades
            player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).build());
            player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).build());
            player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).build());
        }, time);
    }

    public void startLobby() {
        WorldUtils.generateWorld("Lobby", World.Environment.NORMAL);
        WorldUtils.cloneWorld("BeaconOriginal", "Beacon");

        int waitTime = 61; // normally 120 (2 mins)
        for (int i = waitTime; i >= 0; --i) {
            final int time = i;

            DelayedTask delayedTask = new DelayedTask(Bedwars.getInstance(),(waitTime - i) * 20) {
                @Override
                public void run() {
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
                        Bukkit.getOnlinePlayers().forEach(player -> LobbyScoreboard.setScoreboard(player, time, map));
                    } else {
                        if(Bukkit.getOnlinePlayers().size() > 0) {
                            Bukkit.broadcastMessage(ChatColor.YELLOW + "Let the games begin!");
                            setState(GameState.ACTIVE);
                            startGame();

                            players.forEach(player -> {
                                if (player.getTeam() != null) {
                                    respawnPlayer(player.getSpigotPlayer(), true);
                                }
                            });
                        }
                        else {
                            Utils.broadcastMessage(ChatColor.RED + "There were not enough players to start a game!");
                            cancel();
                        }
                    }
                }
            };
            delayedTasks.add(delayedTask);
        }
    }

    // UNUSED FOR NOW - WILL USE RANDOM MAPS & TEAMS
    public void startVoting() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR, 1).setDisplayName(ChatColor.GOLD + "Vote for Map" + ChatColor.GRAY + " (Right-click)").setLore(ChatColor.GRAY + "Vote for the upcoming game's map.").build());
        });
    }

    public void startGame() {
        for(Location location : map.getItemShops()) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" + ChatColor.BOLD + "ITEM SHOP");
            LookClose lookClose = new LookClose();
            lookClose.setRange(32);
            lookClose.setRealisticLooking(true);
            lookClose.linkToNPC(npc);
            lookClose.toggle();
            npc.addTrait(lookClose);
            npc.setProtected(true);
            npc.spawn(location);
        }

        for(Location location : map.getTeamUpgrades()) {
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, ChatColor.YELLOW + "" + ChatColor.BOLD + "TEAM UPGRADES");
            LookClose lookClose = new LookClose();
            lookClose.setRange(32);
            lookClose.setRealisticLooking(true);
            lookClose.linkToNPC(npc);
            lookClose.toggle();
            npc.addTrait(lookClose);
            npc.setProtected(true);
            npc.spawn(location);
        }

        startDiamondCountdown();
        startGenerators();
    }

    public void endGame() {
        GamePlayer topKiller = getMostKills();
        GamePlayer topBreaker = getMostBedsBroken();

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "Bedwars");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Winner: ");
        Bukkit.broadcastMessage(ChatColor.GRAY + "Most Kills: " + topKiller.getTeam().getColor() + topKiller.getSpigotPlayer().getName() + ChatColor.GRAY +  " - " + ChatColor.GOLD + topKiller.getKills());
        Bukkit.broadcastMessage(ChatColor.GRAY + "Most Beds Broken: " + topBreaker.getTeam().getColor() + topBreaker.getSpigotPlayer().getName() + ChatColor.GRAY + " - " + ChatColor.GOLD + topBreaker.getBedsBroken());
        Bukkit.broadcastMessage("");

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.GRAY + "You are now being sent back to the lobby.");
            player.teleport(new Location(Bukkit.getWorld("Lobby"), 0, 64, 0));
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
        });

        delayedTasks.forEach(DelayedTask::cancel);
        repeatingTasks.forEach(RepeatingTask::cancel);

        players.forEach(player -> {
            player.setBedsBroken(0);
            player.setFinalKills(0);
            player.setKills(0);
        });

        CitizensAPI.getNPCRegistry().deregisterAll();
        WorldUtils.deleteWorld("Beacon");
        startLobby();
    }

    private void startGenerators() {
        int ironTime = 2 * 20;
        int goldTime = 6 * 20;
        for(Generator ironGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(map, GeneratorType.IRON)) {
            RepeatingTask iron = new RepeatingTask(Bedwars.getInstance(), 0, ironTime) {
                @Override
                public void run() {
                    ironGenerator.getLocation().getWorld().dropItem(ironGenerator.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.IRON_INGOT, 1)).setVelocity(new Vector(0, 0, 0));
                }
            };

            RepeatingTask gold = new RepeatingTask(Bedwars.getInstance(), 0, goldTime) {
                @Override
                public void run() {
                    ironGenerator.getLocation().getWorld().dropItem(ironGenerator.getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5), new ItemStack(Material.GOLD_INGOT, 1)).setVelocity(new Vector(0, 0, 0));
                }
            };

            repeatingTasks.add(iron);
            repeatingTasks.add(gold);
        }

        int diamond = 30 * 20;
        for(Generator diamondGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(map, GeneratorType.DIAMOND)) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), diamondGenerator.getLocation());
            hologram.appendTextLine(ChatColor.DARK_AQUA + "Tier I");
            //    Tier 2 every 24 seconds
            //    Tier 3 every 12 seconds
            hologram.appendTextLine(ChatColor.AQUA + "" + ChatColor.BOLD + "Diamond Generator");
            TextLine line = hologram.appendTextLine("");
            hologram.appendItemLine(new ItemStack(Material.DIAMOND_BLOCK));

            RepeatingTask repeatingTask = new RepeatingTask(Bedwars.getInstance(), 0, diamond) {
                @Override
                public void run() {
                    int diamondTime = 30;

                    for (int i = diamondTime; i >= 0; --i) {
                        final int diamondCountdown = i;

                        DelayedTask delayedTask = new DelayedTask(Bedwars.getInstance(), (diamondTime - i) * 20) {
                            @Override
                            public void run() {
                                if (diamondCountdown > 0) {
                                    line.setText(ChatColor.YELLOW + "Next diamond in.. " + ChatColor.GOLD + diamondCountdown);
                                }
                            }
                        };
                        delayedTasks.add(delayedTask);
                    }
                    diamondGenerator.getLocation().getWorld().dropItem(diamondGenerator.getLocation().getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.DIAMOND, 1)).setVelocity(new Vector(0, 0, 0));
                }
            };
            repeatingTasks.add(repeatingTask);
        }

        int emerald = 55 * 20;
        for(Generator emeraldGenerator : Bedwars.getInstance().getMapManager().getGeneratorsByType(map, GeneratorType.EMERALD)) {
            Hologram hologram = HologramsAPI.createHologram(Bedwars.getInstance(), emeraldGenerator.getLocation());
            hologram.appendTextLine(ChatColor.DARK_GREEN + "Tier I");
            hologram.appendTextLine(ChatColor.GREEN + "" + ChatColor.BOLD + "Emerald Generator");
            TextLine line = hologram.appendTextLine("");
            hologram.appendItemLine(new ItemStack(Material.EMERALD_BLOCK));

            RepeatingTask repeatingTask = new RepeatingTask(Bedwars.getInstance(), 0, emerald) {
                @Override
                public void run() {
                    int emeraldTime = 55;

                    for (int i = emeraldTime; i >= 0; --i) {
                        final int emeraldCountdown = i;

                        DelayedTask delayedTask = new DelayedTask(Bedwars.getInstance(), (emeraldTime - i) * 20) {
                            @Override
                            public void run() {
                                if (emeraldCountdown > 0) {
                                    line.setText(ChatColor.YELLOW + "Next emerald in.. " + ChatColor.GOLD + emeraldCountdown);
                                }
                            }
                        };
                        delayedTasks.add(delayedTask);
                    }
                    emeraldGenerator.getLocation().getWorld().dropItem(emeraldGenerator.getLocation().getBlock().getLocation().add(0.5, 1, 0.5), new ItemStack(Material.EMERALD, 1)).setVelocity(new Vector(0, 0, 0));
                }
            };
            repeatingTasks.add(repeatingTask);
        }
    }

    /**
     * Starts the diamond countdown (which starts the emerald countdown)
     * and keeps each player scoreboard ticking.
     *
     * Tier 2 Diamond Generators in 6 minutes
     * Tier 2 Emerald Generators in 6 minutes (after diamond)
     */
    private void startDiamondCountdown() {
        RepeatingTask repeatingTask = new RepeatingTask(Bedwars.getInstance(), 0, 360 * 20) {
            @Override
            public void run() {
                int time = 360;

                for (int i = time; i >= 0; --i) {
                    final int countdown = i;

                    DelayedTask delayedTask = new DelayedTask(Bedwars.getInstance(), (time - i) * 20) {
                        @Override
                        public void run() {
                            if (countdown > 0) {
                                players.forEach(player -> GameScoreboard.setScoreboard(player.getSpigotPlayer(), player.getBedsBroken(), player.getFinalKills(), player.getKills(), countdown));
                            }
                            else {
                                Utils.broadcastMessage(ChatColor.AQUA + "Diamond generators are now Tier II!");
                                startEmeraldCountdown();
                            }
                        }
                    };
                    delayedTasks.add(delayedTask);
                }
            }
        };
        repeatingTasks.add(repeatingTask);
    }

    /**
     * Starts the emerald upgrade countdown and
     * keeps the player scoreboard ticking.
     */
    private void startEmeraldCountdown() {
        RepeatingTask emeraldGenerator = new RepeatingTask(Bedwars.getInstance(), 0, 360 * 20) {
            @Override
            public void run() {
                int emeraldTime = 360;

                for (int emeraldI = emeraldTime; emeraldI >= 0; --emeraldI) {
                    final int emeraldCountdown = emeraldI;

                    DelayedTask emerald = new DelayedTask(Bedwars.getInstance(), (emeraldTime - emeraldI) * 20) {
                        @Override
                        public void run() {
                            if (emeraldCountdown > 0) {
                                players.forEach(player -> GameScoreboard.setScoreboard(player.getSpigotPlayer(), player.getBedsBroken(), player.getFinalKills(), player.getKills(), emeraldCountdown));
                            }
                            else {
                                Utils.broadcastMessage(ChatColor.GREEN + "Emerald generators are now Tier II!");
                            }
                        }
                    };
                    delayedTasks.add(emerald);
                }
            }
        };
        repeatingTasks.add(emeraldGenerator);
    }
}
