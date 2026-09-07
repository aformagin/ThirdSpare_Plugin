package com.thirdspare.thirdsparemain.kotlin.commands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
/*
 * As a note, this "StatsDump" command does not need any permission nodes, it exists only for debugging purposes.
 * If the constant "DEVELOPMENT_MODE" is set to false, which it should be for all release builds,
 * this command never gets registered. As additional protection, it is only available to OPs by default.
 *
 * Not that this command will be harmful, it is just to prevent bots from iterating on commands and getting this data.
 * */
class StatsDump (private val instance : ThirdSpareMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        instance.server.logger.info("Online Player List : %s".format(instance.onlinePlayers.toString()))
        return true;
    }
}