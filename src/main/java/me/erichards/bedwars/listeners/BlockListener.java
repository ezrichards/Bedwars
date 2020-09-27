package me.erichards.bedwars.listeners;

import me.erichards.bedwars.Bedwars;
import me.erichards.bedwars.utils.Utils;
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
        this.placedBlocks = new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        Player player = event.getPlayer();

        // get gameplayer and set their color for name below
        // no breaking own bed

        switch(block.getType()) { // remove bed drop
            case YELLOW_BED:
                Utils.broadcastMessage(player.getName() + " broke Yellow Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Yellow").setBedBroken(true);
                break;
            case GREEN_BED:
                Utils.broadcastMessage(player.getName() + " broke Green Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Green").setBedBroken(true);
                break;
            case RED_BED:
                Utils.broadcastMessage(player.getName() + " broke Red Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Red").setBedBroken(true);
                break;
            case LIGHT_BLUE_BED:
                Utils.broadcastMessage(player.getName() + " broke Blue Team's bed!");
                Bedwars.getInstance().getTeamManager().getTeamByName("Blue").setBedBroken(true);
                break;
        }

        // if last bed == 1, end game

//        if(!placedBlocks.contains(block)) {
//            event.setCancelled(true);
//        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlocks.add(event.getBlockPlaced());

        // cant place near generators

        if(event.getBlockPlaced().getType() == Material.TNT) {
            // TODO auto light TNT, can't blow up map blocks or bed, only player placed blocks + knockback players w/ damage
        }
    }
}
