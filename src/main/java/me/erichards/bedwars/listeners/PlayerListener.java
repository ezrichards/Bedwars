package me.erichards.bedwars.listeners;

import me.erichards.bedwars.game.GameScoreboard;
import me.erichards.bedwars.utils.ItemBuilder;
import me.erichards.bedwars.utils.Utils;
import me.erichards.bedwars.utils.gui.GUI;
import me.erichards.bedwars.utils.gui.GUIItem;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.*;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        GameScoreboard.setScoreboard(player);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        event.setKeepInventory(true);
        event.setShouldDropExperience(false);
        event.setDeathMessage("");

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

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();

        if(event.getNPC().getName().contains("ITEM SHOP")) {
            GUI itemShop = new GUI(ChatColor.YELLOW + "" + ChatColor.BOLD + "Item Shop", 6);

            GUIItem item = new GUIItem(0, new ItemBuilder(Material.WHITE_WOOL, 16).setDisplayName(ChatColor.GREEN + "Wool").build()) {
                @Override
                public void onClick(Player player, ClickType clickType) {
                    player.closeInventory();
                    player.sendMessage("Yep!");
                }
            };

            itemShop.addItem(item);
            itemShop.open(player);
        }
        else if(event.getNPC().getName().contains("TEAM UPGRADES")) {
            GUI teamUpgrades = new GUI(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Upgrades", 6);

            GUIItem item = new GUIItem(3, new ItemStack(Material.EMERALD)) {
                @Override
                public void onClick(Player player, ClickType clickType) {
                    player.closeInventory();
                    player.sendMessage("Yep!");
                }
            };

            teamUpgrades.addItem(item);
            teamUpgrades.open(player);
        }
    }
}
