package me.etheus.bedwars.utils.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Made by Ethan Richards
 * June 20, 2021
 */
public abstract class RepeatingTask implements Runnable {

    private int taskId;

    public RepeatingTask(JavaPlugin plugin, int delay, int time) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, delay, time);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
