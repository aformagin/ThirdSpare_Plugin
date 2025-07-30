package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class RespawnListener implements Listener {
    ThirdSpareMain plugin;

    public RespawnListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        var world = event.getPlayer().getWorld();
        var player = event.getPlayer();
        
        if (player.getRespawnLocation() != null) {
            event.setRespawnLocation(player.getRespawnLocation());
        } else {
            event.setRespawnLocation(world.getSpawnLocation());
        }
    }
}
