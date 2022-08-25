package com.thirdspare.thirdsparemain.kotlin.commands

import com.thirdspare.thirdsparemain.ThirdSpareMain
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class OpenBackpack(val plugin: ThirdSpareMain) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        if (command.name != "inv") return false
        val bp = plugin.onlinePlayers[sender.uniqueId]?.backpack
        bp?.openInventory(sender)
//        val pattern = "i___i___i"
//        val itemMap = HashMap<Char, ItemStack>()
//        val customIngot = ItemStack(Material.IRON_INGOT)
//        val meta = customIngot.itemMeta
//        meta.displayName(Component.text("Custom Ingot"))
//        customIngot.itemMeta = meta //Just some test meta to see if this works
//        itemMap['i'] = customIngot
//        val testInv = InvGui(9, itemMap, pattern, "Testing!")
//        testInv.openInventory(sender)
        return true
    }
}