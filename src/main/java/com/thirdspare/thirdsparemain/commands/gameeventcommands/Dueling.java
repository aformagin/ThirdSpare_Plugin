package com.thirdspare.thirdsparemain.commands.gameeventcommands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class Dueling implements CommandExecutor {
    ThirdSpareMain plugin;

    public Dueling(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] strings) {

        if(commandSender instanceof Player){
            if(command.getName().equalsIgnoreCase("duel")){
                Player player = (Player) commandSender;
                int len = strings.length;
                /* Len of 1 should be the following commands with "duel" assumed in front
                * - accept
                * - deny
                * Len of 2 should be the following commands with "duel" assumed in front
                * - request <target> OR send <target>
                */
                switch (len){
                    case 1:
                        if(strings[0].equalsIgnoreCase("accept")){
                            return true;
                        }
                        else if(strings[0].equalsIgnoreCase("deny")){
                            return true;
                        }
                        break;
                    case 2:
                        if(strings[0].equalsIgnoreCase("send") ||
                                strings[0].equalsIgnoreCase("request")){
                            if(strings[1] != null){
                                Player target = player.getServer().getPlayer(strings[1]);
                                HashMap<UUID, User> temp = plugin.getOnlinePlayers();
                                assert target != null;
                                var targetUser = temp.get(target.getUniqueId());
                                var playerUser = temp.get(player.getUniqueId());

                                // TODO Clear target after X amount of time
                                playerUser.setAgainst(target);
                                targetUser.setAgainst(player);
                                return true;
                            }
                            return false;
                        }
                        break;
                    default:
                        break;
                }
                return true;
            } //End of duel name check
        } //End of checking for instance of Player
        return false;
    }
}
