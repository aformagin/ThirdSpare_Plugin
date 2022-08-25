package com.thirdspare.thirdsparemain.kotlin.commands.econcommands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import com.thirdspare.thirdsparemain.utilities.Utils
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class Pay(val plugin: ThirdSpareMain) : CommandExecutor {
    override fun onCommand(commandSender: CommandSender, command: Command, s: String, strings: Array<String>): Boolean {
        when (commandSender) {
            !is Player -> return false
            else -> {
                val player = commandSender.player
                if (command.name.equals("pay", ignoreCase = true)) {
                    // Checking the number of arguments
                    if (strings.size < 2) return false
                    val target = plugin.server.getPlayer(strings[0])
                    // Checking if the player and the target on the same
                    if (player == target) {
                        val failMsg = "Can not transfer funds between the same accounts!"
                        plugin.logger.warning("Failed to transfer fund! %s is same as target!".format(player?.uniqueId))
                        player?.sendMessage(failMsg)
                        return false
                    }
                    val amount = strings[1].toDouble()
                    val successMsg = Utils.applyColour("&6&l[ ! ] %.2f sent to %s".format(amount, target?.name))
                    player?.sendMessage(successMsg)
                    plugin.logger.info("Transferred funds from %s to %s".format(player?.uniqueId, target?.uniqueId))
                    return plugin.tsmEconomy.transferPlayerCredits(player, target, amount)
                }
            }
        }
        return false
    }
}

