package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class DeathListener implements Listener {
    ThirdSpareMain plugin;
    private final ItemStack currencyItem = new ItemStack(Material.EMERALD);

    public DeathListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
        var lore = Component.text("[!]CurrencyItem");
        ArrayList<Component> loreList = new ArrayList<>();
        loreList.add(lore);
        currencyItem.lore(loreList);
    }

    //TODO Generate a loot table
    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event) {
        var entity = event.getEntity(); //The entity
        var loc = entity.getLocation(); //Location of the entity
        int rand = (int) (Math.random() * (100 + 1) + 0); //Math.random() * (max - min + 1) + min
        if (entity instanceof Zombie) {
            if (rand <= 25) {
//                loc.getWorld().dropItem(loc, currencyItem);
            }
        }
    }
}
