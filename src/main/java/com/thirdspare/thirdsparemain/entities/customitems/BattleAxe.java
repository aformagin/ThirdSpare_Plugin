package com.thirdspare.thirdsparemain.entities.customitems;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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

        im.displayName(Component.text("Battle Axe").color(NamedTextColor.GREEN));
        var lore = Component.text("A more durable axe, more suited for dispatching")
                .color(NamedTextColor.WHITE);
        var lore2 = Component.text("enemies ").color(NamedTextColor.RED)
                .append(Component.text("heads than tree branches.").color(NamedTextColor.WHITE));
        double dmgAmount = 10;
        double spdAmount = -2.0;
        double newDmg = baseDmg + dmgAmount;
        double newSpeed = baseSpeed + spdAmount;

        var loreMsg2 = Component.text(String.format("Attack Damage: %.2f", newDmg))
                .color(NamedTextColor.BLUE); // Attack Damage
        var loreMsg3 = Component.text(String.format("Attack Speed: %.2f", newSpeed))
                .color(NamedTextColor.BLUE); // Attack Speed
        ArrayList<Component> loreList = new ArrayList<>();
        loreList.add(lore);
        loreList.add(lore2);
        loreList.add(loreMsg3);
        loreList.add(loreMsg2);
        im.lore(loreList);


        AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage", dmgAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.ATTACK_DAMAGE, modifier);


        AttributeModifier modifier2 = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed", spdAmount,
                AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        im.addAttributeModifier(Attribute.ATTACK_SPEED, modifier2);
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
