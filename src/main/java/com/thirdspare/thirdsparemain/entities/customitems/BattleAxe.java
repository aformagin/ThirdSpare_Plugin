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
// TODO - Clean this up
public class BattleAxe extends CustomItemTemplate{

    public BattleAxe(ThirdSpareMain plugin) {
        this.plugin = plugin;
        this.item = new ItemStack(Material.IRON_AXE);
        this.im = item.getItemMeta();

        im.setCustomModelData(1337);

        var name = String.format("%sBattle Axe", ChatColor.GREEN);
        im.displayName(Component.text(name));
        String msg = String.format("%sA more durable axe, more suited for dispatching",
                ChatColor.WHITE);
        String msg2 = String.format("%senemies %sheads than tree branches.", ChatColor.RED, ChatColor.WHITE);
        var lore = Component.text(msg);
        var lore2 = Component.text(msg2);
        double dmgAmount = 10;
        double spdAmount = -2.0;
        double newDmg = baseDmg + dmgAmount;
        double newSpeed = baseSpeed + spdAmount;

        String loreMsg2 = String.format("%sAttack Damage: %.2f", ChatColor.BLUE, newDmg); // Attack Damage
        String loreMsg3 = String.format("%sAttack Speed: %.2f", ChatColor.BLUE, newSpeed); // Attack Speed
        var lore3 = Component.text(loreMsg3);
        var lore4 = Component.text(loreMsg2);
        ArrayList<Component> loreList = new ArrayList<>();
        loreList.add(lore);
        loreList.add(lore2);
        loreList.add(lore3);
        loreList.add(lore4);
        im.lore(loreList);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", dmgAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, modifier);


        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", spdAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, modifier2);
        item.setItemMeta(im);


        NamespacedKey key = new NamespacedKey(plugin, "custom_axe");
        recipe = new ShapedRecipe(key, item);
        recipe.shape("iBn", " S ", " S ");
        recipe.setIngredient('i', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.IRON_BLOCK);
        recipe.setIngredient('n', Material.GOLD_NUGGET);
        recipe.setIngredient('S', Material.STICK);

    }

}
