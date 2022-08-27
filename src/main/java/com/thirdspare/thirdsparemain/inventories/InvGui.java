/*
 * InvGui.java - A wrapper class I made to generate Inventory based UIs quickly and easily
 *  each child class will still need to implement
 * Author: Austin Formagin
 * Date: June 18th, 2021
 * */

package com.thirdspare.thirdsparemain.inventories;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class InvGui implements Listener {
    private final int MAX_SIZE = 54;
    private final int MIN_SIZE = 9;
    private Inventory inv;
    private int size;

    private final String title;

    public InvGui() {
        this.size = validateSize(9);
        this.title = "InvGUI";
        inv = Bukkit.createInventory(null, size, Component.text(this.title));

    }

    public InvGui(int size, String title) {
        this.size = validateSize(size);
        this.title = title;
        inv = Bukkit.createInventory(null, size, Component.text(this.title));
    }

    //This can be used for things like backpacks, etc. by saving the inv to the User class
    public InvGui(Inventory inv, int size, String title) {
        this.inv = inv;
        this.size = validateSize(size);
        this.title = title;
    }

    public InvGui(int size, HashMap<Character, ItemStack> itemMap, String pattern, String title) {
        this.size = validateSize(size);
        this.title = title;
        inv = Bukkit.createInventory(null, size, Component.text(this.title));
        generatePatternedInventory(itemMap, pattern);
    }

    /**
     * openInventory - Opens the inventory to the selected player
     *
     * @param entity The human entity that you want to display the inventory to
     */
    public void openInventory(HumanEntity entity) {
        entity.openInventory(inv);
    }

    /**
     * generatePatternedInventory - Generates the inventory in a passed pattern
     *
     * @param itemMap - Hashmap that represents the characters in the pattern and the item they represent
     * @param pattern - The pattern as a string, should be at least 9 characters long and length a multiple of 9
     */
    public void generatePatternedInventory(HashMap<Character, ItemStack> itemMap, String pattern) {
        if (pattern.length() > size)
            pattern = pattern.substring(0, size - 1); //If the pattern is larger than the inventory, chop the rest off

        var patternArray = pattern.toCharArray();
        for (int i = 0; i < size; i++) {
            if (itemMap.containsKey(patternArray[i])) {
                //Set item takes the inventory index slot and the item you want to add
                inv.setItem(i, itemMap.get(patternArray[i]));
            } else {
                inv.setItem(i, new ItemStack(Material.AIR));
            }
        }
    }

    /**
     * validateSize - DEFAULT SIZE = 18, Checks to see if the size is between 9 and 54, if not DEFAULT.
     * Checks to see if the size is a multiple of 9, if not DEFAULT.
     *
     * @param size A multiple of 9, between 9 and 54.
     * @return Either the defined size or the DEFAULT size
     */
    public int validateSize(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE) return this.size = 18;
        if (size % 9 != 0) return this.size = 18;
        else return this.size = size;
    }

    public Inventory getInv() {return inv;}

    public void setInv(Inventory inv) {
        this.inv = inv;
    }
}
