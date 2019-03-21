package fr.worldcube.craftingmenu.listeners;

import fr.worldcube.craftingmenu.Utils;
import fr.worldcube.craftingmenu.main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryClick implements Listener{

    FileConfiguration config = main.config;

    @EventHandler (priority = EventPriority.NORMAL)
    public void onPlayerInventoryCraftingClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();

        if(p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        if (e.getSlot() == -999){
            return;
        }

        if (e.getClickedInventory().getType() == InventoryType.CRAFTING || e.getClickedInventory().getType() == InventoryType.PLAYER) {
            if(e.getClickedInventory().getType() == InventoryType.CRAFTING) {
                e.setCancelled(true);
                p.updateInventory();
                ConfigurationSection craftingMenu = config.getConfigurationSection("CraftingMenu");

                if(p.getItemOnCursor().getType() != Material.AIR) {
                    ItemStack item = p.getItemOnCursor().clone();
                    p.setItemOnCursor(new ItemStack(Material.AIR));
                    p.getInventory().addItem(item);

                }


                if (craftingMenu.get(e.getSlot() + ".Sond") != null) {
                    Sound sound = Sound.valueOf(craftingMenu.get(e.getSlot() + ".Sond").toString());
                    if (sound != null) {
                        p.playSound(p.getLocation(), sound, 10f, 10f);
                    }
                }

                if (craftingMenu.get(e.getSlot() + ".Command") != null) {
                    String cmd = craftingMenu.get(e.getSlot() + ".Command").toString();
                    p.closeInventory();
                    p.chat(cmd);
                }


                else if(craftingMenu.get(e.getSlot() + ".Open") != null) {

                    p.closeInventory();

                    switch (craftingMenu.get(e.getSlot() + ".Open").toString().toUpperCase()) {
                        case "WORKBENCH":
                            p.openWorkbench(null, true);
                            break;
                        case "ENCHANT":
                            p.openEnchanting(null, true);
                            break;
                        case "ENDERCHEST":
                            p.openInventory(p.getEnderChest());
                            break;
                        default:
                            p.openWorkbench(null, true);
                    }
                }
            }
        }
    }

}
