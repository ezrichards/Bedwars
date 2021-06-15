package me.etheus.bedwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Made by Ethan Richards
 * September 16, 2020
 */
public class WorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Console cannot use world commands!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            if(command.getName().equalsIgnoreCase("world")) {
                player.sendMessage("/world <world> - Teleports to world");
                player.sendMessage("/world list - Lists loaded worlds");
            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {
                player.sendMessage("Loaded Worlds:");
                for(World world : Bukkit.getServer().getWorlds()) {
                    player.sendMessage(world.getName());
                }
            }
            else {
                World world = Bukkit.getWorld(args[0]);

                if(world == null) {
                    Bukkit.broadcastMessage("Null!");
                    return true;
                }

                player.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
                player.sendMessage("Teleported!");
            }
        }

        return true;
    }
}
