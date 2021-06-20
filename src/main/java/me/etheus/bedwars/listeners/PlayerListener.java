package me.etheus.bedwars.listeners;

import me.etheus.bedwars.Bedwars;
import me.etheus.bedwars.game.Game;
import me.etheus.bedwars.game.GameState;
import me.etheus.bedwars.game.player.GamePlayer;
import me.etheus.bedwars.game.team.Team;
import me.etheus.bedwars.utils.Utils;
import me.etheus.bedwars.utils.item.ItemBuilder;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class PlayerListener implements Listener {

    private final List<Team> availableTeams = new ArrayList<>(Bedwars.getInstance().getTeamManager().getTeams());

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.setPlayerListHeader(ChatColor.YELLOW + "" + ChatColor.BOLD + "BEDWARS");
        player.teleport(new Location(Bukkit.getWorld("Lobby"), 0, 64, 0));

        if(Game.getInstance().getState() == GameState.ACTIVE) {
            player.sendMessage(ChatColor.RED + "You joined while the game is active, so you are now in spectator mode.");
            player.setGameMode(GameMode.SPECTATOR);
            return;
        }

        if(Bedwars.getInstance().getTeamManager().getTeams().size() > 0) {
            Team team = availableTeams.get(new Random().nextInt(availableTeams.size()));
            availableTeams.remove(team);

            Game.getInstance().addPlayer(new GamePlayer(player.getUniqueId(), team));

            player.setPlayerListName(team.getColor() + "" + ChatColor.BOLD + team.getName().charAt(0) + " " + ChatColor.RESET + team.getColor() + player.getName());
        }
        else {
            player.sendMessage(ChatColor.RED + "There was no more room for you to join a team!");
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if(event.getPlayer().getLocation().getY() < 0) {
            GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

            player.teleport(gamePlayer.getTeam().getSpawnLocation());
            player.setHealth(20);
            player.setGameMode(GameMode.SPECTATOR);

            sendDeathMessage(gamePlayer, null, EntityDamageEvent.DamageCause.VOID);
            respawnPlayer(player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(Game.getInstance().getState() == GameState.LOBBY || Game.getInstance().getState() == GameState.ENDED) {
            event.setCancelled(true);
        }

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player player = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();

            if(event.getDamage() > player.getHealth()) {
                GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());
                GamePlayer killer = Game.getInstance().getPlayerByUUID(damager.getUniqueId());

                player.setHealth(20);
                player.setGameMode(GameMode.SPECTATOR);

                sendDeathMessage(gamePlayer, killer, event.getCause());

                if(gamePlayer.getTeam().isBedBroken()) {
                    player.sendMessage(ChatColor.GRAY + "You died while your bed was broken, so you are now spectating!");
                    killer.addFinalKill();
                    checkForWin();
                }
                else {
                    respawnPlayer(player);
                    killer.addKill();
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            Player player = (Player) event.getEntity();
            GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

            if (event.getDamage() > player.getHealth()) {
                sendDeathMessage(gamePlayer, null, event.getCause());

                player.teleport(gamePlayer.getTeam().getSpawnLocation());
                player.setHealth(20);
                player.setGameMode(GameMode.SPECTATOR);

                if (gamePlayer.getTeam().isBedBroken()) {
                    player.sendMessage(ChatColor.GRAY + "You died while your bed was broken, so you are now spectating!");
                    checkForWin();
                } else {
                    respawnPlayer(player);
                }
            }
        }
    }

    private void sendDeathMessage(GamePlayer player, GamePlayer killer, EntityDamageEvent.DamageCause damageCause) {
        String deathMessage = "";
        if(player.getTeam().isBedBroken()) {
            deathMessage += ChatColor.AQUA + "" + ChatColor.BOLD + "FINAL KILL! ";
        }

        if(damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            deathMessage += player.getTeam().getColor() + player.getSpigotPlayer().getName() + ChatColor.GRAY + " was killed by " + killer.getTeam().getColor() + killer.getSpigotPlayer().getName() + ChatColor.GRAY + "!";
        }
        else if(damageCause == EntityDamageEvent.DamageCause.PROJECTILE) {
            deathMessage += player.getTeam().getColor() + player.getSpigotPlayer().getName() + ChatColor.GRAY + " was shot by " + killer.getTeam().getColor() + killer.getSpigotPlayer().getName() + ChatColor.GRAY + "!";
        }
        else if(damageCause == EntityDamageEvent.DamageCause.FALL) {
            deathMessage += player.getTeam().getColor() + player.getSpigotPlayer().getName() + ChatColor.GRAY + " took too big of a fall!";
        }
        else if(damageCause == EntityDamageEvent.DamageCause.VOID) {
            deathMessage += player.getTeam().getColor() + player.getSpigotPlayer().getName() + ChatColor.GRAY + " has fallen into the void!";
        }
        else {
            deathMessage += player.getTeam().getColor() + player.getSpigotPlayer().getName() + ChatColor.GRAY + " has perished!";
        }

        Utils.broadcastMessage(deathMessage);
    }

    private void respawnPlayer(Player player) {
        GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

        Bukkit.getScheduler().runTaskLater(Bedwars.getInstance(), () -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.teleport(gamePlayer.getTeam().getSpawnLocation());
            player.setHealth(20);
            player.getInventory().setHelmet(new ItemBuilder(Material.LEATHER_HELMET).build()); // TODO dye this armor or check for upgrades
            player.getInventory().setChestplate(new ItemBuilder(Material.LEATHER_CHESTPLATE).build());
            player.getInventory().setLeggings(new ItemBuilder(Material.LEATHER_LEGGINGS).build());
            player.getInventory().setBoots(new ItemBuilder(Material.LEATHER_BOOTS).build());
        }, 5 * 20);
    }

    private void checkForWin() {
        for(Team team : Bedwars.getInstance().getTeamManager().getTeams()) {
            if(team.isBedBroken()) {
                int deadCount = 0;
                for(Player teamPlayer : team.getMembers()) {
                    if(teamPlayer.isDead()) {
                        deadCount++;
                    }
                }

                if(deadCount == team.getMembers().size()) {
                    Utils.broadcastMessage(team.getColor() + team.getName() + " Team " + ChatColor.GRAY + "has been eliminated!");
                    Game.getInstance().endGame();
                }
            }
        }
    }
}
