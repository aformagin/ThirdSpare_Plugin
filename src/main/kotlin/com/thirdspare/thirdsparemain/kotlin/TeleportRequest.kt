package com.thirdspare.thirdsparemain.kotlin

import org.bukkit.entity.Player

class TeleportRequest(private val requester: Player, val target: Player, private val requestTime: Long) {
    /**
     * Returns the difference in time between the requested time and current system time
     */
    fun getDeltaTime(): Long {
        return System.currentTimeMillis() - requestTime
    }

    /**
     * Teleports the requester to the target player
     */
    fun teleportReqToTarget(): String {
        //TODO add effects later
        requester.teleport(target.location); //Teleports target player to the requester location

        //String output for the log
        return "%s accepted a teleport request from %s".format(target.name, requester.name)
    }

    override fun toString(): String {
        return "TeleportRequest(requester=$requester, target=$target, requestTime=$requestTime)"
    }


}