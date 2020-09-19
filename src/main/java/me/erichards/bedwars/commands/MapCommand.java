package me.erichards.bedwars.commands;

import me.erichards.bedwars.game.map.GameMap;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Made by Ethan Richards
 * May 28, 2020
 */
public class MapCommand implements CommandExecutor {

    private Map<UUID, GameMap> selectedMap;

    public MapCommand() {
        this.selectedMap = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Console cannot use Map commands!");
            return true;
        }

        Player player = (Player) sender;

        if(args.length == 0) {
            if(cmd.getName().equalsIgnoreCase("map")) {
                player.sendMessage("/map create");
                player.sendMessage("/map info <map>");
                player.sendMessage("/map select <map>");
                player.sendMessage("/map list");
                player.sendMessage("/map add itemshop");
                player.sendMessage("/map add upgradeshop");
                player.sendMessage("/map spawnpoint <team>");

            }
        }

        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("create")) {

            }
            else if(args[0].equalsIgnoreCase("info")) {

            }
            else if(args[0].equalsIgnoreCase("add")) {
//                player.getWorld().spawnEntity(player.getLocation(), NPC)

                Location location = player.getLocation();

                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Land1401");
                npc.spawn(player.getLocation());
            }
        }

        if(args.length == 2) {

        }
        return true;
    }
}
