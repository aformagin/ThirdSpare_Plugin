package com.thirdspare.thirdsparemain.commands.econcommands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
                        assert player != null;
                        player.sendMessage(Component.text("[ ! ] ")
                                .color(NamedTextColor.GOLD)
                                .decorate(TextDecoration.BOLD)
                                .append(Component.text(String.format("%.2f -- %s's New Account Value.", amount, target.getName()))
                                        .color(NamedTextColor.GREEN)));
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
