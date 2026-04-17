package com.thirdspare.thirdsparemain.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!commandSender.hasPermission("tsm.listp")) {
            commandSender.sendMessage(Component.text("You do not have permission to use this command.")
                    .color(NamedTextColor.RED));
            return true;
        }
        var players = commandSender.getServer().getOnlinePlayers();
        int online = players.size();
        int max = commandSender.getServer().getMaxPlayers();
        var onlineResponse = Component.text("Currently Online [")
                .append(Component.text(online).color(NamedTextColor.GREEN))
                .append(Component.text(" / "))
                .append(Component.text(max).color(NamedTextColor.BLUE))
                .append(Component.text("]"));
        commandSender.sendMessage(onlineResponse);
        for (Object player : players) {
            Player p = (Player) player;
            commandSender.sendMessage(String.format("- %s", p.getName()));
        }
        return true;
    }
}
