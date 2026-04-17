package com.thirdspare.thirdsparemain.commands.econcommands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Balance implements CommandExecutor {
    ThirdSpareMain plugin;

    public Balance(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (commandSender instanceof Player && command.getName().equalsIgnoreCase("balance")) {
            if (!commandSender.hasPermission("tsm.econ.bal")) {
                commandSender.sendMessage(Component.text("You do not have permission to use this command.")
                        .color(NamedTextColor.RED));
                return true;
            }
            var player = ((Player) commandSender).getPlayer();
            assert player != null;
            var bal = plugin.getTSMEconomy().getPlayerBalance(player);
            var msg = String.format("&6&l[ ! ] &a%.2f is your balance.", bal);
            player.sendMessage(Utils.applyColour(msg));
            return true;
        }
        return false;
    }
}
