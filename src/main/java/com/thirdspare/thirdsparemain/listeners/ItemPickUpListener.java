package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class ItemPickUpListener implements Listener {
    ThirdSpareMain plugin;
    private final ItemStack currencyItem = new ItemStack(Material.EMERALD);

    public ItemPickUpListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
        var lore = Component.text("[!]CurrencyItem");
        ArrayList<Component> loreList = new ArrayList<>();
        loreList.add(lore);
        currencyItem.lore(loreList);
    }

    @EventHandler
    public void onItemPickUp(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player) {
            var item = event.getItem().getItemStack();
            var player = ((Player) event.getEntity()).getPlayer();
            assert player != null;
            /* Checking to see if the player is walking over a currency item */
            currencyItem.setAmount(item.getAmount());
            if (item.equals(currencyItem)) { //Check for proper meta data
                event.getItem().remove();
                // Create message using Adventure API
                Component msg = Component.text("[ ! ] ", NamedTextColor.GOLD, TextDecoration.BOLD)
                        .append(Component.text("Balance has been added. x" + item.getAmount(), NamedTextColor.GREEN));
                player.sendMessage(msg);
                /* TODO Add new balance to data file for this player */
                event.setCancelled(true);
            }
        }
    }
}
