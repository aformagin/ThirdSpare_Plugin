package com.thirdspare.thirdsparemain.econ;

import com.google.gson.Gson;
import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.data.ConfigData;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class TSMEconomy {
    ThirdSpareMain plugin;
    File econDataLocation;

    public TSMEconomy(ThirdSpareMain plugin) {
        this.plugin = plugin;
        this.econDataLocation = new File(Utils.CONFIG_FILE);
    }

    /**
     * Enhanced balance setter with comprehensive User validation
     * Sets the players balance to the desired amount with null safety
     *
     * @param player     The player object, used to get player UUID from OnlinePlayersList
     * @param newBalance The new balance for the targeted account
     * @return Returns true if the action was successful
     */
    public boolean setPlayerBalance(Player player, double newBalance) {
        // Enhanced validation: check both map containment and User object existence
        if (!plugin.getOnlinePlayers().containsKey(player.getUniqueId())) {
            var log = String.format("TSM -- ECONOMY ERROR: Player UUID not found in online players map -- %s", player.getName());
            plugin.getLogger().warning(log);
            player.sendMessage(Component.text("Economy error: Player data not found. Please rejoin the server.").color(NamedTextColor.RED));
            return false;
        }
        
        var user = plugin.getOnlinePlayers().get(player.getUniqueId());
        if (user == null) {
            var log = String.format("TSM -- ECONOMY ERROR: User object is null for player %s when setting balance", player.getName());
            plugin.getLogger().severe(log);
            player.sendMessage(Component.text("Economy error: Player data corrupted. Please rejoin the server.").color(NamedTextColor.RED));
            return false;
        }
        
        user.setBalance(newBalance);
        return true;
    }

    /**
     * Enhanced balance retrieval with comprehensive null safety
     * Retrieves the balance from the ONLINE users account with User validation
     *
     * @param player The player that you want the balance of.
     * @return Returns the amount in the players balance as a double, or 0.0 if User is null.
     */
    public double getPlayerBalance(Player player) {
        var log = String.format("Accessing balance of: %s", player.getName());
        plugin.getLogger().info(log);

        // Critical null safety check to prevent NullPointerException
        var user = plugin.getOnlinePlayers().get(player.getUniqueId());
        if (user == null) {
            plugin.getLogger().severe("TSM -- ECONOMY ERROR: User object is null for player " + player.getName() + " when accessing balance");
            player.sendMessage(Component.text("Economy error: Player data not loaded. Please rejoin the server.").color(NamedTextColor.RED));
            return 0.0; // Return safe default value instead of crashing
        }

        return user.getBalance();
    }

    /**
     * Enhanced credit addition with comprehensive User validation
     * Adds funds to the player's balance with null safety checks
     *
     * @param player  The player that you wish to add credits to.
     * @param credits The amount of credits you'd like to add
     * @return Returns true if the actions was successful
     */
    public boolean addPlayerCredits(Player player, double credits) {
        // Enhanced validation: check both map containment and User object existence
        if (!plugin.getOnlinePlayers().containsKey(player.getUniqueId())) {
            var log = String.format("TSM -- ECONOMY ERROR: Funds could not be added to account for %s - PLAYER UUID NOT FOUND", player.getName());
            plugin.getLogger().warning(log);
            player.sendMessage(Component.text("Economy error: Player data not found. Please rejoin the server.").color(NamedTextColor.RED));
            return false;
        }
        
        var user = plugin.getOnlinePlayers().get(player.getUniqueId());
        if (user == null) {
            var log = String.format("TSM -- ECONOMY ERROR: User object is null for player %s when adding credits", player.getName());
            plugin.getLogger().severe(log);
            player.sendMessage(Component.text("Economy error: Player data corrupted. Please rejoin the server.").color(NamedTextColor.RED));
            return false;
        }
        
        var currentBalance = user.getBalance();
        var newBalance = currentBalance + credits;
        user.setBalance(newBalance);
        
        //Output is [FUNDS ADDED] Funds added to account for <player name> - <UUID>
        var log = String.format("[%f] Funds added to account for %s - %s",
                credits,
                player.getName(),
                player.getUniqueId());
        plugin.getLogger().info(log);
        return true;
    }

    /**
     * transferPlayerCredits - Move credits from one player account to another
     *
     * @param player The first player - The funds will be removed from this balance
     * @param target The second player - The funds will be added to this balance
     * @param amount The amount to transfer between players
     * @return Returns true if the action was successful
     */
    public boolean transferPlayerCredits(Player player, Player target, double amount) {
        var playerBal = getPlayerBalance(player);
        var targetBal = getPlayerBalance(target);

        // Checking to see if the command sender has enough funds for transaction
        if (playerBal - amount < 0) {
            player.sendMessage(Component.text("Not enough funds in account.")
                    .color(NamedTextColor.RED));
            return false;
        }
        // Calculate new balances for each player
        var newPlayerBalance = playerBal - amount;
        var newTargetBalance = targetBal + amount;
        setPlayerBalance(player, newPlayerBalance); // Sets the command senders new balance
        setPlayerBalance(target, newTargetBalance); // Sets the targets new balance
        var log = String.format("Transferred %f funds from %s to %s", amount, player.getName(), target.getName());
        plugin.getLogger().info(log);
        return true;
    }

    public ConfigData readDataFile() {
        try {
            return Utils.readObjectFromFile(econDataLocation, ConfigData.class);
        } catch (IOException e) {
            plugin.getLogger().warning("Could not read economy data file: " + e.getMessage());
            return null;
        }
    }


}
