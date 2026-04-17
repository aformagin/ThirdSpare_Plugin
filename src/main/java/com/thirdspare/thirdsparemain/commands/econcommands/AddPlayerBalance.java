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

public class AddPlayerBalance implements CommandExecutor {
    ThirdSpareMain plugin;

    public AddPlayerBalance(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }
    //TODO Player permissions
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (!commandSender.hasPermission("tsm.econ.bal_add")) {
                commandSender.sendMessage(Component.text("You do not have permission to use this command.")
                        .color(NamedTextColor.RED));
                return true;
            }
            var player = ((Player) commandSender).getPlayer();
            assert player != null;
            if (command.getName().equalsIgnoreCase("addbalance")) {
                if (strings.length < 2) {
                    return false;
                }
                if (strings.length == 2) {
                    Player target = plugin.getServer().getPlayer(strings[0]);
                    assert target != null;
                    if (!target.isOnline())
                        return false;
                    double amount = Double.parseDouble(strings[1]);
                    if (plugin.getTSMEconomy().addPlayerCredits(target, amount)) {
                        var msg = String.format("&6&l[ ! ] &c%.2f added to %s's account.",
                                amount, target.getName());

                        player.sendMessage(Utils.applyColour(msg));
                        return true;
                    } else
                        return false;
                }
            }
        }
        return false;
    }
}
