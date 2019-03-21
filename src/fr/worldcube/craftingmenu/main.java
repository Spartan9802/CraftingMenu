package fr.worldcube.craftingmenu;

import com.avaje.ebeaninternal.server.cluster.Packet;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import fr.worldcube.craftingmenu.listeners.PlayerInventoryClick;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public class main extends JavaPlugin {

    private ProtocolManager protocolManager;
    public static main plugin;
    public static FileConfiguration config;

    @Override
    public void onEnable() {
        plugin = this;
        config = this.getConfig();

        config.options().copyDefaults(true);
        saveConfig();

        protocolManager = ProtocolLibrary.getProtocolManager();
        protocolManager.addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.CLIENT_COMMAND, PacketType.Play.Client.WINDOW_CLICK)
        {

            @Override
            public void onPacketReceiving(final PacketEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.CLIENT_COMMAND) {
                    if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {

                        Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, new Runnable() {
                            @Override
                            public void run() {
                                Utils.updateCraftingMenu(event.getPlayer());
                            }
                        }, 1L);
                    }

                    else if (event.getPacketType() == PacketType.Play.Client.WINDOW_CLICK) {
                        if (event.getPlayer().getGameMode() == GameMode.SURVIVAL) {
                            if(event.getPlayer().getOpenInventory().getType() == InventoryType.CRAFTING) {
                                event.setCancelled(true);



                                Bukkit.getScheduler().scheduleSyncDelayedTask(main.plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        Utils.updateCraftingMenu(event.getPlayer());
                                    }
                                }, 1L);
                            }
                        }
                    }
                }
            }

        });

        getServer().getPluginManager().registerEvents(new PlayerInventoryClick(), this);

    }

    @Override
    public void onDisable() {
        protocolManager.removePacketListeners(this);
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }


}
