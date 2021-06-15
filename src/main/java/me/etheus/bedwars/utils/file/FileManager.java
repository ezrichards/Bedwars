package me.etheus.bedwars.utils.file;

import me.etheus.bedwars.Bedwars;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Made by Ethan Richards
 * September 09, 2020
 */
public class FileManager {

    public static YamlConfiguration getConfiguration(String fileName) {
        File file = new File(Bedwars.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            loadFile(fileName);
        }
        return YamlConfiguration.loadConfiguration(file);
    }

    public static void loadFile(String fileName) {
        File file = new File(Bedwars.getInstance().getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
                Bedwars.getInstance().saveResource(fileName, true);
                Bedwars.getInstance().getLogger().info("Generated new " + fileName + "!");
            } catch (IOException e) {
                Bedwars.getInstance().getLogger().severe("Could not create " + fileName + "!");
            }
        }
        YamlConfiguration.loadConfiguration(file);
    }

    public static void saveFile(String fileName) {
        try {
            getConfiguration(fileName).save(new File(Bedwars.getInstance().getDataFolder(), fileName));
        } catch (IOException e) {
            Bedwars.getInstance().getLogger().severe("Could not save " + fileName + ".yml!");
        }
    }
}
