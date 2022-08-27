package com.thirdspare.thirdsparemain.econ;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.utilities.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.json.JSONObject;

import java.io.File;

public class TSMEconomy {
    ThirdSpareMain plugin;
    File econDataLocation;

    public TSMEconomy(ThirdSpareMain plugin) {
        this.plugin = plugin;
        this.econDataLocation = new File(Utils.CONFIG_FILE);
    }

    /**
     * setPlayerBalance - Sets the players balance to the desired amount
     *
     * @param player     The player object, used to get player UUID from OnlinePlayersList
     * @param newBalance The new balance for the targeted account
     * @return Returns true if the action was successful
     */
    public boolean setPlayerBalance(Player player, double newBalance) {
        if (!plugin.getOnlinePlayers().containsKey(player.getUniqueId())) {
            var log = String.format("Player data is not in file -- %s", player.getName());
            plugin.getLogger().warning(log);
            return false;
        } else {
            plugin.getOnlinePlayers().get(player.getUniqueId()).setBalance(newBalance);
            return true;
        }

    }

    /**
     * getPlayerBalance - Retrieves the balance from the ONLINE users account
     *
     * @param player The player that you want the balance of.
     * @return Returns the amount in the players balance as a double.
     */
    public double getPlayerBalance(Player player) {
        var log = String.format("Accessing balance of: %s", player.getName());
        plugin.getLogger().info(log);

        return plugin.getOnlinePlayers().get(player.getUniqueId()).getBalance();
    }

    /**
     * addPlayerCredits - Adds funds to the player's balance
     *
     * @param player  The player that you wish to add credits to.
     * @param credits The amount of credits you'd like to add
     * @return Returns true if the actions was successful
     */
    public boolean addPlayerCredits(Player player, double credits) {
        if (plugin.getOnlinePlayers().containsKey(player.getUniqueId())) {
            var currentBalance = plugin.getOnlinePlayers().get(player.getUniqueId()).getBalance();
            var newBalance = currentBalance + credits;
            plugin.getOnlinePlayers().get(player.getUniqueId()).setBalance(newBalance);
            //Output is [FUNDS ADDED] Funds added to account for <player name> - <UUID>
            var log = String.format("[%f] Funds added to account for %s - %s",
                    credits,
                    player.getName(),
                    player.getUniqueId());
            plugin.getLogger().info(log);
            return true;
        } else {
            var log = String.format("Funds could not be added to account for %s - PLAYER NOT FOUND",
                    player.getName());
            plugin.getLogger().warning(log);
            return false;
        }
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
            var msg = String.format("%sNot enough funds in account.", ChatColor.GREEN);
            player.sendMessage(msg);
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

    public JSONObject readDataFile() {
        String jsonResponse = Utils.FileToJSONString(econDataLocation);
        return new JSONObject(jsonResponse);
    }


}
