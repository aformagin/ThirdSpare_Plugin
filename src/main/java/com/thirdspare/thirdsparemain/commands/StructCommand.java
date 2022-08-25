package com.thirdspare.thirdsparemain.commands;

import com.thirdspare.thirdsparemain.structures.WarpStone;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class StructCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            var player =(Player) commandSender;
            WarpStone ws = new WarpStone();
            ws.generateOnPlayer(player);
        } else
            return false;
        return false;
    }
}
