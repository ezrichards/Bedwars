package me.etheus.bedwars.listeners;

import me.etheus.bedwars.Bedwars;
import me.etheus.bedwars.game.Game;
import me.etheus.bedwars.game.GameState;
import me.etheus.bedwars.game.player.GamePlayer;
import me.etheus.bedwars.game.team.Team;
import me.etheus.bedwars.game.team.TeamManager;
import me.etheus.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class PlayerListener implements Listener {

    private List<Team> availableTeams;

    public PlayerListener() {
        availableTeams = new ArrayList<>(Bedwars.getInstance().getTeamManager().getTeams());
    }

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

            GamePlayer gamePlayer = new GamePlayer(player.getUniqueId(), team, 0, 0, 0);
            Game.getInstance().addPlayer(gamePlayer);

            player.setPlayerListName(team.getColor() + "" + ChatColor.BOLD + team.getName().charAt(0) + " " + ChatColor.RESET + team.getColor() + player.getName());
        }
        else {
            player.sendMessage(ChatColor.RED + "There was no more room for you to join a team!");
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if(Game.getInstance().getPlayerByUUID(player.getUniqueId()) != null) {
            GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

            if(gamePlayer.getTeam().isBedBroken()) { // TODO teleport player back to map
                player.setGameMode(GameMode.SPECTATOR);
                player.sendMessage(ChatColor.GRAY + "You died while your bed was broken, so you are now spectating!");;
            }
            else {
                event.setRespawnLocation(gamePlayer.getTeam().getSpawnLocation());
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setShouldDropExperience(false);
        event.setDeathMessage("");

        Player player = event.getEntity();

        if(Game.getInstance().getPlayerByUUID(player.getUniqueId()).getTeam().isBedBroken()) { // TODO may not be necessary..also TODO respawn timer system
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage(ChatColor.GRAY + "You died while your bed was broken, so you are now spectating!");
        }

        if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            if(player.getKiller() != null) {
                if(Game.getInstance().getPlayerByUUID(player.getUniqueId()).getTeam().isBedBroken()) {
                    GamePlayer killer = Game.getInstance().getPlayerByUUID(player.getKiller().getUniqueId());
                    killer.addFinalKill();
                    Utils.broadcastMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "FINAL KILL! " + Game.getInstance().getPlayerByUUID(player.getUniqueId()).getTeam().getColor() + player.getName() + ChatColor.GRAY +  " was killed by " + killer.getTeam().getColor() + player.getKiller().getName() + ChatColor.GRAY + "!");
                }
                else {
                    Game.getInstance().getPlayerByUUID(player.getKiller().getUniqueId()).addKill();
                    Utils.broadcastMessage(player.getName() + " was killed by " + player.getKiller().getName());
                }
            }
        }
        else if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            Utils.broadcastMessage(player.getName() + " took too big of a fall!");
        }
        else if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if(player.getKiller() != null) {
                if(Game.getInstance().getPlayerByUUID(player.getUniqueId()).getTeam().isBedBroken()) {
                    Game.getInstance().getPlayerByUUID(player.getKiller().getUniqueId()).addFinalKill();
                    Utils.broadcastMessage("FINAL KILL! " + player.getName() + " was shot by " + player.getKiller().getName());
                }
                else {
                    Game.getInstance().getPlayerByUUID(player.getKiller().getUniqueId()).addKill();
                    Utils.broadcastMessage(player.getName() + " was shot by " + player.getKiller().getName());
                }
            }
            else {
                Utils.broadcastMessage(player.getName() + " got shot with a projectile!");
            }
        }
        else if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) {
            Utils.broadcastMessage(player.getName() + " has fallen into the void!");
        }
        else {
            Utils.broadcastMessage(player.getName() + " has perished!");
        }

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
