package me.etheus.bedwars.listeners;

import me.etheus.bedwars.Bedwars;
import me.etheus.bedwars.game.Game;
import me.etheus.bedwars.game.generator.Generator;
import me.etheus.bedwars.game.player.GamePlayer;
import me.etheus.bedwars.game.team.Team;
import me.etheus.bedwars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        for(GamePlayer gamePlayer : Game.getInstance().getPlayers()) {
            if(gamePlayer.getSpigotPlayer().equals(player)) {
                if(gamePlayer.getTeam().getColor() == ChatColor.RED && block.getType() == Material.RED_BED
                || gamePlayer.getTeam().getColor() == ChatColor.YELLOW && block.getType() == Material.YELLOW_BED
                || gamePlayer.getTeam().getColor() == ChatColor.GREEN && block.getType() == Material.GREEN_BED
                || gamePlayer.getTeam().getColor() == ChatColor.AQUA && block.getType() == Material.LIGHT_BLUE_BED) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You cannot break your own bed!");
                }
                else {
                    switch(block.getType()) {
                        case YELLOW_BED:
                            Utils.broadcastMessage(player.getName() + " broke Yellow Team's bed!");
                            Bedwars.getInstance().getTeamManager().getTeamByName("Yellow").setBedBroken(true);
                            event.setDropItems(false);
                            break;
                        case GREEN_BED:
                            Utils.broadcastMessage(player.getName() + " broke Green Team's bed!");
                            Bedwars.getInstance().getTeamManager().getTeamByName("Green").setBedBroken(true);
                            event.setDropItems(false);
                            break;
                        case RED_BED:
                            Utils.broadcastMessage(player.getName() + " broke Red Team's bed!");
                            Bedwars.getInstance().getTeamManager().getTeamByName("Red").setBedBroken(true);
                            event.setDropItems(false);
                            break;
                        case LIGHT_BLUE_BED:
                            Utils.broadcastMessage(player.getName() + " broke Blue Team's bed!");
                            Bedwars.getInstance().getTeamManager().getTeamByName("Blue").setBedBroken(true);
                            event.setDropItems(false);
                            break;
                    }

                    List<Team> bedsBroken = new ArrayList<>();
                    for(Team team : Bedwars.getInstance().getTeamManager().getTeams()) {
                        if(team.isBedBroken()) {
                            bedsBroken.add(team);
                        }
                    }

                    if(bedsBroken.size() == 3) {
                        // TODO check if all players from team are dead
                        Game.getInstance().endGame();
                    }
                }
            }
        }

//        if(!placedBlocks.contains(block)) {
//            event.setCancelled(true);
//        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        placedBlocks.add(event.getBlockPlaced());

        for(Generator generator : Bedwars.getInstance().getMapManager().getMapByName("Beacon").getGenerators()) {
            // cant place near generators
        }

        if(event.getBlockPlaced().getType() == Material.TNT) {
            // TODO auto light TNT, can't blow up map blocks or bed, only player placed blocks + knockback players w/ damage
        }
    }
}
