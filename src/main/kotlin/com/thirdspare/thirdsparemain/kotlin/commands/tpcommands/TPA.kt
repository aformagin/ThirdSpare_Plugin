package com.thirdspare.thirdsparemain.kotlin.commands.tpcommands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import com.thirdspare.thirdsparemain.kotlin.TeleportRequest
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TPA(private val instance: ThirdSpareMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {

        //Check for command name and the type of command sender
        if (sender !is Player) return false
        if (command.name != "tpa") return false


        //Get the target and requester
        val target = instance.server.getPlayer(args[0])

        //Check if target = sender
        if (sender == target) {
            sender.sendMessage(Component.text("Can not send to yourself."))
            return false
        }

        //Get the user from the onlinePlayers hashmap
        val user = instance.onlinePlayers[target?.uniqueId]
        //Create and Set the pending request for the player
        user?.teleportRequest = TeleportRequest(sender, target!!, System.currentTimeMillis())

        //Insert the player back into the hashmap
        instance.onlinePlayers.replace(target.uniqueId, user) //Hopefully this works how I think? -- Update. It does.

        //Send message to player and target
        target.sendMessage(Component.text("%s has requested to teleport to you".format(sender.name)))

        //Command is successful
        return true
    }
}