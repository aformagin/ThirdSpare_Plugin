package com.thirdspare.thirdsparemain.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        var players = commandSender.getServer().getOnlinePlayers();
        int online = players.size();
        int max = commandSender.getServer().getMaxPlayers();
        var onlineResponse = String.format("Currently Online [%s%d %s/ %s%d%s]", ChatColor.GREEN, online,
                ChatColor.RESET, ChatColor.BLUE, max, ChatColor.RESET);
        commandSender.sendMessage(onlineResponse);
        for (Object player : players) {
            Player p = (Player) player;
            commandSender.sendMessage(String.format("- %s", p.getName()));
        }
        return true;
    }
}
