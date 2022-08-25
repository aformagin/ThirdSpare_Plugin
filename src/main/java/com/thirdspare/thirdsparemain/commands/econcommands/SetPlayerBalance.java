package com.thirdspare.thirdsparemain.commands.econcommands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SetPlayerBalance implements CommandExecutor {
    ThirdSpareMain plugin;

    public SetPlayerBalance(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            var player = ((Player) commandSender).getPlayer();

            if (command.getName().equalsIgnoreCase("setbalance")) {
                if (strings.length < 2) {
                    return false;
                }
                if (strings.length == 2) {
                    Player target = plugin.getServer().getPlayer(strings[0]);
                    assert target != null;
                    player.sendMessage("Debug 1");
                    if(!target.isOnline())
                        return false;
                    double amount = Double.parseDouble(strings[1]);
                    if(plugin.getTSMEconomy().setPlayerBalance(target, amount)){
                        var msg = String.format("%s%s[ ! ] %s%.2f -- %s's New Account Value.",
                                ChatColor.GOLD, ChatColor.BOLD, ChatColor.GREEN, amount, target.getName());
                        assert player != null;
                        player.sendMessage(msg);
                        return true;
                    }
                    else
                        return false;
                }
            }
        }
        return false;
    }
}
