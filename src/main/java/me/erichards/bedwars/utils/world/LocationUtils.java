package me.erichards.bedwars.utils.world;

import me.erichards.bedwars.Bedwars;
import me.erichards.bedwars.game.generator.Generator;
import me.erichards.bedwars.game.generator.GeneratorType;
import me.erichards.bedwars.game.team.Team;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

/**
 * Made by Ethan Richards
 * September 23, 2020
 */
public class LocationUtils {

    public static Map<Team, Location> parseSpawnpoint(String location) {
        String[] data = location.split(";");
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);
        WorldUtils.generateWorld("Beacon", World.Environment.NORMAL);

        Map<Team, Location> spawnpoint = new HashMap<>();
        spawnpoint.put(Bedwars.getInstance().getTeamManager().getTeamByName(data[0]), new Location(Bukkit.getWorld("Beacon"), x, y, z));

        return spawnpoint;
    }

    public static Generator parseGenerator(String location) {
        String[] data = location.split(";");
        double x = Double.parseDouble(data[1]);
        double y = Double.parseDouble(data[2]);
        double z = Double.parseDouble(data[3]);
        WorldUtils.generateWorld("Beacon", World.Environment.NORMAL);

        return new Generator(GeneratorType.valueOf(data[0].toUpperCase()), new Location(Bukkit.getWorld("Beacon"), x, y, z));
    }

    public static Location parseLocation(String location) {
        String[] data = location.split(";");
        double x = Double.parseDouble(data[0]);
        double y = Double.parseDouble(data[1]);
        double z = Double.parseDouble(data[2]);
        WorldUtils.generateWorld("Beacon", World.Environment.NORMAL);

        return new Location(Bukkit.getWorld("Beacon"), x, y, z);
    }
}
