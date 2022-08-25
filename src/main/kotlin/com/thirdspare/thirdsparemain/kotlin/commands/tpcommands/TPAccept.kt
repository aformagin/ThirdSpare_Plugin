package com.thirdspare.thirdsparemain.kotlin.commands.tpcommands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TPAccept(val instance: ThirdSpareMain) : CommandExecutor {
    private val TIMEOUT = 120
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (command.name != "tpaccept") return false

        val user = instance.onlinePlayers[sender.uniqueId]
        //Check that request isn't null

        if (user?.teleportRequest != null) {
            if (user.teleportRequest.getDeltaTime() >= (TIMEOUT * 1000)) {
                //Tell user that the request has timed out
                sender.sendMessage(Component.text("Request Timeout."))
                return true
            }
        } else {
            sender.sendMessage(Component.text("Request not found."))
            return true
        }
        val target = user.teleportRequest?.target
        if (!instance.server.onlinePlayers.contains(target)) {
            //The target player is not online
            val tarName = target?.name
            val msg =
                Component.text("Target player (%s) is not online. Teleport request failed!".format(tarName))
            sender.sendMessage(msg)
            return true
        }

        //Teleport the requester to the user (target)
        val msg = Component.text(user.teleportRequest.teleportReqToTarget())
        sender.sendMessage(msg)
        target?.sendMessage(msg)
        return true
    }
}