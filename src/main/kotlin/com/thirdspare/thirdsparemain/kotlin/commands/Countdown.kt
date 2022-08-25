package com.thirdspare.thirdsparemain.kotlin.commands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.scheduler.BukkitRunnable

class Countdown(val plugin : ThirdSpareMain) : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (command.name.equals("countdown", ignoreCase = true)) {
            if (args.size < 1) return false
            var cd = object : BukkitRunnable() {
                var time = args[0].toInt()
                override fun run() {
                    val msg = "[COUNTDOWN] %d remaining before end of countdown...".format(time)
                    if (time-- <= 0) cancel()
                    sender.sendMessage(msg)
                }
            }.runTaskTimer(plugin, 0L, 20L)
            return true
        }
        return false
    }
}