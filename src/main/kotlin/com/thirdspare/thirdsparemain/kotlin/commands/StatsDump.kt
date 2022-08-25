package com.thirdspare.thirdsparemain.kotlin.commands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StatsDump (private val instance : ThirdSpareMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        instance.server.logger.info("Online Player List : %s".format(instance.onlinePlayers.toString()))
        return true;
    }
}