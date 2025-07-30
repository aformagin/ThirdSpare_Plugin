package com.thirdspare.thirdsparemain.entities.customitems;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;

public class NytherionHammer extends CustomItemTemplate {
    // The model ID attached in the custom resource pack
    protected final static int MODEL_ID = 1338;
    // Constructor for the hammer
    public NytherionHammer(ThirdSpareMain plugin) {

        //Assigning values to inherited variables
        this.plugin = plugin;
        this.item = new ItemStack(Material.NETHERITE_AXE);
        this.im = item.getItemMeta();

        // Damage amounts
        double dmgAmount = 10;
        double spdAmount = -2.0;
        double newDmg = baseDmg + dmgAmount;
        double newSpeed = baseSpeed + spdAmount;

        // Assigning model id to the item
        im.setCustomModelData(MODEL_ID);

        // Creating the display name using Adventure API
        im.displayName(Component.text("Nytherion Hammer").color(NamedTextColor.GREEN));

        // Item Lore using Adventure API
        // TODO Flavour text
        var lore3 = Component.text(String.format("Attack Speed: %.2f", newSpeed)).color(NamedTextColor.BLUE);
        var lore4 = Component.text(String.format("Attack Damage: %.2f", newDmg)).color(NamedTextColor.BLUE);
        ArrayList<Component> loreList = new ArrayList<>();
        // Adding lore to loreList
        loreList.add(lore3);
        loreList.add(lore4);
        // Assigning lore to item
        im.lore(loreList);

        // Create AttributeModifiers using new NamespacedKey-based API
        NamespacedKey attackDamageKey = new NamespacedKey(plugin, "nytherion_hammer_attack_damage");
        AttributeModifier modifier = new AttributeModifier(attackDamageKey, dmgAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
        im.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);

        NamespacedKey attackSpeedKey = new NamespacedKey(plugin, "nytherion_hammer_attack_speed");
        AttributeModifier modifier2 = new AttributeModifier(attackSpeedKey, spdAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlotGroup.HAND);
        im.addAttributeModifier(Attribute.ATTACK_SPEED, modifier2);
        // Setting meta of item
        item.setItemMeta(im);

        //TODO - Balance this crafting recipe
        NamespacedKey key = new NamespacedKey(plugin, "nytherion_hammer");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("BBB", " S ", " S ");
        recipe.setIngredient('B', Material.NETHERITE_BLOCK);
        recipe.setIngredient('S', Material.STICK);
    }
}
