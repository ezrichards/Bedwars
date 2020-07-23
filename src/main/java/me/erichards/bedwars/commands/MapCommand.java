package me.erichards.bedwars.commands;

import me.erichards.bedwars.Bedwars;
import me.erichards.bedwars.game.GameMap;
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

    private Bedwars plugin;
    private Map<UUID, GameMap> selectedMap;

    public MapCommand(Bedwars instance) {
        this.plugin = instance;

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

                plugin.getConfig().set("maps.Beacon.itemShops.1.x", player.getLocation().getX());

//                plugin.getConfig().getConfigurationSection("maps.Beacon.itemShops").createSection("1");
                plugin.saveConfig();
                player.sendMessage("Set successfully");

//                player.getWorld().spawnEntity(player.getLocation(), NPC)

                Location location = player.getLocation();

                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "Land1401");
                npc.spawn(player.getLocation());

                // save loc as ()x|()y|()z|()yaw|()pitch - access as [0] from .split("x
            }
        }

        if(args.length == 2) {

        }
        return true;
    }
}

//    public void createNPC(Player player, String npcName) {
//        Location location = player.getLocation();
//        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
//        WorldServer nmsWorld = ((CraftWorld) player.getWorld()).getHandle();
//        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), npcName);
//
//        String[] skin = getFromName("Bleach__");
//        gameProfile.getProperties().put("textures", new Property("textures", skin[0], skin[1]));
////        gameProfile.getProperties().put("textures", new Property("textures", value, signature));
//
//        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
//        Player npcPlayer = npc.getBukkitEntity().getPlayer();
//        npcPlayer.setPlayerListName("");
//
//        npc.setLocation(location.getX(), location.getY(), location.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
//
//        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
//        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
//        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
//        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc));
//    }
//

