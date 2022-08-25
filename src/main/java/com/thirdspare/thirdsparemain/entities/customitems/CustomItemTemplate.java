package com.thirdspare.thirdsparemain.entities.customitems;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class CustomItemTemplate {
    protected ItemStack item;
    protected ItemMeta im;
    protected ShapedRecipe recipe;
    protected ThirdSpareMain plugin;

    // Minecraft's default values
    protected double baseDmg = 2;
    protected double baseSpeed = 4;


    public ItemStack getItem() {
        return item;
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }
}
