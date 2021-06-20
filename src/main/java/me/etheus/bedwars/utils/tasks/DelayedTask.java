package me.etheus.bedwars.utils.tasks;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Made by Ethan Richards
 * June 20, 2021
 */
public abstract class DelayedTask implements Runnable {

    private int taskId;

    public DelayedTask(JavaPlugin plugin, int time) {
        taskId = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, time);
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}
