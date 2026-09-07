package com.thirdspare.thirdsparemain.commands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChannelCommands implements CommandExecutor {
    ThirdSpareMain plugin;

    public ChannelCommands(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player player) {
            if (command.getName().equalsIgnoreCase("chat") && strings.length >= 1) {
                if (!player.hasPermission("tsm.chat")) {
                    player.sendMessage(Component.text("You do not have permission to use this command.")
                            .color(NamedTextColor.RED));
                    return true;
                }
                var list = plugin.chatManager.getChannelsList();
                switch (strings[0]) {
                    case "join" -> {
                        if (list.containsKey(strings[1].toUpperCase())) {
                            var user = plugin.getOnlinePlayers().get(player.getUniqueId());
                            plugin.chatManager.joinChannel(user, strings[1].toUpperCase());
                        }
                        return true;
                    }
                    case "leave" -> {
                        if (list.containsKey(strings[1].toUpperCase())) {
                            var user = plugin.getOnlinePlayers().get(player.getUniqueId());
                            plugin.chatManager.leaveChannel(user, strings[1]);
                        }
                        return true;
                    }
                    case "list" -> {
                        list.forEach((s1, chatChannel) ->
                                player.sendMessage(" > " + chatChannel.getChannelColor() + chatChannel.getChannelName()));
                        return true;
                    }
                    default -> {
                        player.sendMessage("Please input a valid parameter.");
                        return false;
                    }
                }
            }
            return false;
        }
        return false;
    }
}
