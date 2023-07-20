package me.ezrichards.bedwars.utils.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

/**
 * Made by Ethan Richards
 * June 03, 2020
 */
public class GUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR) return;

        Player player = (Player) event.getWhoClicked();

        if(GUI.activeGUIs.containsKey(player.getUniqueId())) {
            GUI gui = GUI.activeGUIs.get(player.getUniqueId());

            for(GUIItem item : gui.getItems()) {
                if(item.getItemStack().equals(event.getCurrentItem())) {
                    item.onClick(player, event.getClick());
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(GUI.activeGUIs.containsKey(event.getPlayer().getUniqueId())) GUI.activeGUIs.remove(event.getPlayer().getUniqueId());
    }
}
