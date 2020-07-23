package me.erichards.bedwars.listeners;

import me.erichards.bedwars.Bedwars;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class BlockListener implements Listener {

    private List<Block> placedBlocks;

    public BlockListener() {
        placedBlocks = new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        if(block.getType() == Material.YELLOW_BED || block.getType() == Material.GREEN_BED || block.getType() == Material.RED_BED || block.getType() == Material.LIGHT_BLUE_BED) {
            Bukkit.broadcastMessage(""); // teamcolor + name
            Bukkit.broadcastMessage(player.getName() + " broke " + block.getType().name() + "'s bed!");
            Bukkit.broadcastMessage("");
        }
        else {
//            if(placedBlocks.contains(block)) {
//                event.setCancelled(true);
//            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlocks.add(event.getBlockPlaced());
    }
}
