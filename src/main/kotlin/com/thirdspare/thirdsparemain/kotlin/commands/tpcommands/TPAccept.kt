package com.thirdspare.thirdsparemain.kotlin.commands.tpcommands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TPAccept(val instance: ThirdSpareMain) : CommandExecutor {
    private val TIMEOUT = 120
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (command.name != "tpaccept") return false

        if (!sender.hasPermission("tsm.tpa")) {
            sender.sendMessage(Component.text("You do not have permission to use this command.")
                .color(NamedTextColor.RED))
            return true
        }

        val user = instance.onlinePlayers[sender.uniqueId]
        //Check that request isn't null

        if (user?.teleportRequest != null) {
            if (user.teleportRequest.getDeltaTime() >= (TIMEOUT * 1000)) {
                //Tell user that the request has timed out
                sender.sendMessage(Component.text("Request Timeout."))
                user.teleportRequest = null // Clear expired request
                return true
            }
        } else {
            sender.sendMessage(Component.text("Request not found."))
            return true
        }

        val teleportRequest = user.teleportRequest!!
        val requester = teleportRequest.requester // Need to access requester field (may need to make it public or add a getter)
        
        // Check if requester is still online
        if (!instance.server.onlinePlayers.contains(requester)) {
            sender.sendMessage(Component.text("The requester is no longer online."))
            user.teleportRequest = null
            return true
        }

        //Teleport the requester to the user (sender)
        val msg = Component.text(teleportRequest.teleportReqToTarget())
        sender.sendMessage(msg)
        requester.sendMessage(msg)
        
        // Clear the request after successful teleport
        user.teleportRequest = null
        return true
    }
}
