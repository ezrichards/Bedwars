package me.erichards.bedwars.listeners;

import me.erichards.bedwars.game.Game;
import me.erichards.bedwars.game.GameState;
import me.erichards.bedwars.utils.Utils;
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

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class PlayerListener implements Listener {

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
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setShouldDropExperience(false);
        event.setDeathMessage("");

        Player player = event.getEntity();

        if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            Utils.broadcastMessage(player.getName() + " was killed by " + player.getKiller().getName());
        }
        else if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL) {
            Utils.broadcastMessage(player.getName() + " took too big of a fall!");
        }
        else if(player.getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if(player.getKiller() != null) {
                Utils.broadcastMessage(player.getName() + " was shot by " + player.getKiller().getName());
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
    }
}
