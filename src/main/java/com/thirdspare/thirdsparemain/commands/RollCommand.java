package com.thirdspare.thirdsparemain.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RollCommand implements CommandExecutor {

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player player){

            if(command.getName().equalsIgnoreCase("roll")){
                player.getServer().getLogger().info("Roll command called by " + player.displayName());
                int roll;
                if(strings.length == 1){
                    if(Integer.parseInt(strings[0]) > 0 && Integer.parseInt(strings[0]) < 100){
                        int max = Integer.parseInt(strings[0]);
                        roll = (int) ((Math.random() * ( max- 1 + 1)) + 1);
                        player.getServer().getLogger().info("Roll is: "+ roll);
                        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[TS Dice Roll] " + roll +
                                "/" + max);
                    }
                }

                else if(strings.length == 0){
                    roll = (int) ((Math.random() * (20 - 1 + 1)) + 1);
                    player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[TS Dice Roll] " + roll +
                            "/20");
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
