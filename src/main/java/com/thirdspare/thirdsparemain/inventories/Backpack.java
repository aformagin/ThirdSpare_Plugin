package com.thirdspare.thirdsparemain.inventories;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Base64;

public class Backpack extends InvGui {

    public Backpack(ThirdSpareMain plugin) {
        plugin.getLogger().info("TSM Backpack listener running...");
    }

    public Backpack(int size, String title) {
        super(size, title);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        setInv(e.getInventory());
    }

    /**
     * serializeItem - Takes an ItemStack and serializes it to bytes and then encodes the bytes to a base64 string
     *
     * @param backpackItem The ItemStack that is being serialized
     * @return The item in bytes, as a base 64 string
     */
    public String serializeItem(ItemStack backpackItem) {
        var bytes = backpackItem.serializeAsBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * deserializeItem - Takes a base64 string and decodes it to an ItemStack
     *
     * @param b64SerializedItem The base64 string to decode
     * @return The decoded ItemStack
     */
    public ItemStack deserializeItem(String b64SerializedItem) {
        byte[] b64 = Base64.getDecoder().decode(b64SerializedItem);
        return ItemStack.deserializeBytes(b64);
    }

}
