package me.etheus.bedwars.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        if(!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command!");
            return true;
        }

        if(args.length == 0) {
            if(command.getName().equalsIgnoreCase("world")) {
                player.sendMessage("");
                player.sendMessage(ChatColor.AQUA + "Worlds Utility");
                player.sendMessage(ChatColor.GRAY + "/world <world> - teleports to a specified world.");
                player.sendMessage(ChatColor.GRAY + "/world list - lists all loaded worlds.");
                player.sendMessage("");
            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("list")) {
                player.sendMessage("");
                player.sendMessage(ChatColor.AQUA + "Loaded Worlds:");
                for(World world : Bukkit.getServer().getWorlds()) {
                    player.sendMessage(ChatColor.GRAY + world.getName());
                }
                player.sendMessage("");
            }
            else {
                World world = Bukkit.getWorld(args[0]);

                if(world == null) {
                    player.sendMessage(ChatColor.RED + "The specified world does not exist!!");
                    return true;
                }

                player.teleport(new Location(world, world.getSpawnLocation().getX(), world.getSpawnLocation().getY(), world.getSpawnLocation().getZ()));
                player.sendMessage(ChatColor.GRAY + "Teleported to " + ChatColor.GREEN + world.getName() + ChatColor.GRAY + "!");
            }
        }

        return true;
    }
}
