package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

/**
 * This listener is currently not implemented and is disabled in the main plugin class.
 * It is intended to handle sign-based interactions, such as creating signs that can execute commands
 * or display dynamic information. Future implementation could include features like:
 * - [Warp] signs for teleportation.
 * - [Trade] signs for player-to-player item trading.
 * - [Info] signs that display server information or player stats.
 * - Command signs that execute a command when right-clicked.
 */
public class SignListener implements Listener {
    ThirdSpareMain plugin;

    public SignListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        // The event is cancelled because the feature is not implemented.
        event.setCancelled(true);
    }
}
