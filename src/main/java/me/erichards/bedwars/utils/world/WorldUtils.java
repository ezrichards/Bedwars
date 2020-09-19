package me.erichards.bedwars.utils.world;

import me.erichards.bedwars.Bedwars;
import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.io.File;
import java.util.Map;

/**
 * Made by Ethan Richards
 * September 17, 2020
 */
public class WorldUtils {

    /**
     * Generates the given world if it doesn't exist.
     * If it does exist, it will be loaded.
     *
     * If called while world was already generated
     * or loaded, nothing will happen.
     *
     * @param name Name of world to be generated
     * @param environment Environment to generate with
     * @param gameRules Game rules to be set
     */
    public static void generateWorld(String name, World.Environment environment, Map<GameRule<Object>, Object> gameRules) {
        World world = Bukkit.getWorld(name);

        if(world == null) {
            WorldCreator creator = new WorldCreator(name);
            creator.environment(environment);
            creator.generateStructures(false);

            world = creator.createWorld();
            world.setAutoSave(false);
            world.setTime(0);

            if(gameRules == null) return;

            World finalWorld = world;
            gameRules.forEach(finalWorld::setGameRule);
        }
    }

    /**
     * Generates the given world if it doesn't exist.
     * If it does exist, it will be loaded.
     *
     * If called while world was already generated
     * or loaded, nothing will happen.
     *
     * @param name Name of world to be generated
     * @param environment Environment to generate with
     */
    public static void generateWorld(String name, World.Environment environment) {
        generateWorld(name, environment, null);
    }

    /**
     * Clones an existing world and then loads it
     *
     * @param original Name of original world to be copied
     * @param target Name of target world that is copied
     */
    public static void cloneWorld(String original, String target) {
        try {
            File originalWorld = new File(Bedwars.getInstance().getServer().getWorldContainer().getCanonicalPath() + File.separator + original);
            File targetWorld = new File(Bedwars.getInstance().getServer().getWorldContainer().getCanonicalPath() + File.separator + target);

            File uid = new File(originalWorld.getCanonicalPath() + File.separator + "uid.dat");
            if(uid.exists()) uid.delete();

            File session = new File(originalWorld.getCanonicalPath() + File.separator + "session.lock");
            if(session.exists()) session.delete();

            FileUtils.copyDirectory(originalWorld, targetWorld);

            generateWorld(target, World.Environment.NORMAL);
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Deletes the target world
     *
     * @param target Name of world to delete
     */
    public static void deleteWorld(String target) {
        try {
            Bukkit.unloadWorld(target, false);

            File targetWorld = new File(Bedwars.getInstance().getServer().getWorldContainer().getCanonicalPath() + File.separator + target);
            targetWorld.delete();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }
}
