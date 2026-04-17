package com.thirdspare.thirdsparemain.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player player){

            if(command.getName().equalsIgnoreCase("roll")){
                if (!player.hasPermission("tsm.roll")) {
                    player.sendMessage(Component.text("You do not have permission to use this command.")
                            .color(NamedTextColor.RED));
                    return true;
                }
                player.getServer().getLogger().info("Roll command called by " + player.displayName());
                int roll;
                if(strings.length == 1){
                    if(Integer.parseInt(strings[0]) > 0 && Integer.parseInt(strings[0]) < 100){
                        int max = Integer.parseInt(strings[0]);
                        roll = (int) ((Math.random() * ( max- 1 + 1)) + 1);
                        player.getServer().getLogger().info("Roll is: "+ roll);
                        player.sendMessage(Component.text("[TS Dice Roll] " + roll + "/" + max)
                                .color(NamedTextColor.GREEN)
                                .decorate(TextDecoration.BOLD));
                    }
                }

                else if(strings.length == 0){
                    roll = (int) ((Math.random() * (20 - 1 + 1)) + 1);
                    player.sendMessage(Component.text("[TS Dice Roll] " + roll + "/20")
                            .color(NamedTextColor.GREEN)
                            .decorate(TextDecoration.BOLD));
                    return true;
                }
                else return false;
            }
            else return false; //End of Roll command
        }
        else return false;//End of checking if player
        return true;
    }
}
