package fr.worldcube.craftingmenu;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.InvocationTargetException;

public class Utils{

    public static void updateCraftingMenu(Player p){
        ConfigurationSection craftingMenu = main.plugin.getConfig().getConfigurationSection("CraftingMenu");

        if (p.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        for (String key : craftingMenu.getKeys(false)) {

            if(Material.getMaterial(craftingMenu.get(key + ".Material").toString().toUpperCase()) != null) {
                ItemStack item = new ItemStack(Material.getMaterial(craftingMenu.get(key + ".Material").toString().toUpperCase()));
                ItemMeta meta = item.getItemMeta();

                if (craftingMenu.get(key + ".Name") != null) {
                    meta.setDisplayName(craftingMenu.get(key + ".Name").toString());
                }

                if (!craftingMenu.getStringList(key + ".Lore").isEmpty()) {
                    meta.setLore(craftingMenu.getStringList(key + ".Lore"));
                }

                item.setItemMeta(meta);



                try {

                    PacketContainer fakeItem = new PacketContainer(PacketType.Play.Server.SET_SLOT);
                    fakeItem.getIntegers().write(0,0);
                    fakeItem.getIntegers().write(1,Integer.parseInt(key));
                    fakeItem.getItemModifier().write(0,item);
                    main.plugin.getProtocolManager().sendServerPacket(p, fakeItem);

                } catch (InvocationTargetException e) {
                    throw new RuntimeException(
                            "Cannot send packet ", e);
                }

            }




        }
    }

}
