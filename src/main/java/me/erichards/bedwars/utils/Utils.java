package me.erichards.bedwars.utils;

import org.bukkit.Bukkit;

/**
 * Made by Ethan Richards
 * June 03, 2020
 */
public class Utils {

    public static void broadcastMessage(String message) {
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(message);
        Bukkit.broadcastMessage("");
    }
}
