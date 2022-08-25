package com.thirdspare.thirdsparemain.entities.customitems;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.UUID;

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

        // Creating the name string and setting the display name
        var name = String.format("%sNytherion Hammer", ChatColor.GREEN);
        im.displayName(Component.text(name));


        // Item Lore to be assigned
        // TODO Flavour text

        String loreMsg2 = String.format("%sAttack Damage: %.2f", ChatColor.BLUE, newDmg); // Attack Damage
        String loreMsg3 = String.format("%sAttack Speed: %.2f", ChatColor.BLUE, newSpeed); // Attack Speed
        var lore3 = Component.text(loreMsg3);
        var lore4 = Component.text(loreMsg2);
        ArrayList<Component> loreList = new ArrayList<>();
        // Adding lore to loreList
        loreList.add(lore3);
        loreList.add(lore4);
        // Assigning lore to item
        im.lore(loreList);

        // Setting item attack damage and speed
        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", dmgAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);


        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", spdAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);

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
