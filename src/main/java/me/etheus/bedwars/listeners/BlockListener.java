package me.etheus.bedwars.listeners;

import me.etheus.bedwars.Bedwars;
import me.etheus.bedwars.game.Game;
import me.etheus.bedwars.game.generator.Generator;
import me.etheus.bedwars.game.player.GamePlayer;
import me.etheus.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class BlockListener implements Listener {

    private final List<Block> placedBlocks = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        GamePlayer gamePlayer = Game.getInstance().getPlayerByUUID(player.getUniqueId());

        if(gamePlayer.getTeam().getColor() == ChatColor.RED && block.getType() == Material.RED_BED
            || gamePlayer.getTeam().getColor() == ChatColor.YELLOW && block.getType() == Material.YELLOW_BED
            || gamePlayer.getTeam().getColor() == ChatColor.GREEN && block.getType() == Material.LIME_BED
            || gamePlayer.getTeam().getColor() == ChatColor.AQUA && block.getType() == Material.LIGHT_BLUE_BED) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.RED + "You cannot break your own bed!");
        }
        else {
            if(block.getType() == Material.YELLOW_BED) {
                Utils.broadcastMessage(gamePlayer.getTeam().getColor() + player.getName() + ChatColor.GRAY + " broke Yellow Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Yellow").setBedBroken(true);
            }
            else if(block.getType() == Material.LIME_BED) {
                Utils.broadcastMessage(gamePlayer.getTeam().getColor() + player.getName() + ChatColor.GRAY + " broke Green Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Green").setBedBroken(true);
            }
            else if(block.getType() == Material.RED_BED) {
                Utils.broadcastMessage(gamePlayer.getTeam().getColor() + player.getName() + ChatColor.GRAY + " broke Red Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Red").setBedBroken(true);
            }
            else if(block.getType() == Material.LIGHT_BLUE_BED) {
                Utils.broadcastMessage(gamePlayer.getTeam().getColor() + player.getName() + ChatColor.GRAY + " broke Blue Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Blue").setBedBroken(true);
            }
            event.setDropItems(false);
            gamePlayer.addBedBroken();
        }

        if(!placedBlocks.contains(block) && !block.getType().name().toLowerCase().endsWith("bed")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlocks.add(event.getBlockPlaced());

        if(event.getBlockPlaced().getType() == Material.TNT) {
            event.getBlockPlaced().setType(Material.AIR);
            event.getBlockPlaced().getLocation().getWorld().spawn(event.getBlockPlaced().getLocation().add(0.5, 0.5, 0.5), TNTPrimed.class);
        }
    }

    @EventHandler // TODO work on TNT explode, this solution isn't good
    public void onEntityExplode(EntityExplodeEvent event) {
        for(Block explodedBlock : event.blockList()) {
            for(Block placedBlock : placedBlocks) {
                if(explodedBlock != placedBlock) {
                    Bukkit.broadcastMessage("yes!");
                    event.setCancelled(true);
                }
            }
        }
    }
}
