package me.ezrichards.bedwars.listeners;

import me.ezrichards.bedwars.game.shop.PayType;
import me.ezrichards.bedwars.game.shop.ShopItem;
import me.ezrichards.bedwars.game.team.Team;
import me.ezrichards.bedwars.utils.gui.GUIItem;
import me.ezrichards.bedwars.utils.item.ItemBuilder;
import me.ezrichards.bedwars.utils.file.FileManager;
import me.ezrichards.bedwars.utils.gui.GUI;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Made by Ethan Richards
 * September 14, 2020
 */
public class NPCListener implements Listener {

    @EventHandler
    public void onNPCRightClick(NPCRightClickEvent event) {
        Player player = event.getClicker();

        if(event.getNPC().getName().contains("ITEM SHOP")) {
            GUI itemShop = new GUI(ChatColor.YELLOW + "" + ChatColor.BOLD + "Item Shop", 6);
            List<ShopItem> shopItems = new ArrayList<>();
            FileConfiguration shopConfig = FileManager.getConfiguration("shop.yml");

            shopConfig.getConfigurationSection("items").getKeys(false).forEach(item -> {
                ItemStack displayItem = new ItemBuilder(Material.valueOf(shopConfig.getString("items." + item + ".material")), shopConfig.getInt("items." + item + ".amount"))
                        .setLore(ChatColor.GRAY + "Cost: " + ChatColor.GREEN + shopConfig.getInt("items." + item + ".cost") + " " + PayType.valueOf(shopConfig.getString("items." + item + ".type")).getName()).build();

                shopItems.add(new ShopItem(displayItem, new ItemBuilder(displayItem.getType(), shopConfig.getInt("items." + item + ".amount")).build(), shopConfig.getInt("items." + item + ".cost"), PayType.valueOf(shopConfig.getString("items." + item + ".type"))));
            });

            int slot = 0;
            for(ShopItem shopItem : shopItems) {
                GUIItem item = new GUIItem(slot, shopItem.getDisplay()) {
                    @Override
                    public void onClick(Player player, ClickType clickType) {
                        buyItem(player, shopItem);
                    }
                };
                itemShop.addItem(item);
                slot++;
            }
            itemShop.open(player);
        }
        else if(event.getNPC().getName().contains("TEAM UPGRADES")) {
            GUI teamUpgrades = new GUI(ChatColor.GREEN + "" + ChatColor.BOLD + "Team Upgrades", 6);

            List<ShopItem> upgrades = new ArrayList<>();

            int slot = 0;
            for(ShopItem upgrade : upgrades) {
                GUIItem item = new GUIItem(slot, upgrade.getItem()) {
                    @Override
                    public void onClick(Player player, ClickType clickType) {

                    }
                };
                teamUpgrades.addItem(item);
                slot++;
            }
            teamUpgrades.open(player);
        }
    }

    private void buyItem(Player player, ShopItem shopItem) {
        if(player.getInventory().firstEmpty() == -1) {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "Your inventory is full!");
            return;
        }

        if(player.getInventory().contains(shopItem.getType().getMaterial())) {
            if(player.getInventory().containsAtLeast(new ItemStack(shopItem.getType().getMaterial()), shopItem.getCost())) {
                player.sendMessage(ChatColor.GREEN + "Purchased " + ChatColor.GRAY + shopItem.getDisplay().getAmount() + "x " + ChatColor.GREEN + shopItem.getItem().getType().name() + ".");
                player.getInventory().removeItemAnySlot(new ItemStack(shopItem.getType().getMaterial(), shopItem.getCost()));
                player.getInventory().addItem(shopItem.getItem());
            }
            else {
                player.closeInventory();
                player.sendMessage(ChatColor.RED + "You do not have enough " + shopItem.getType().getName() + ChatColor.RED + " to buy this!");
            }
        }
        else {
            player.closeInventory();
            player.sendMessage(ChatColor.RED + "You do not have enough " + shopItem.getType().getName() + ChatColor.RED + " to buy this!");
        }
    }

    private void buyUpgrade(Team team, ShopItem shopItem) {
        // make Upgrade class, Team.hasUpgrade?
    }
}
