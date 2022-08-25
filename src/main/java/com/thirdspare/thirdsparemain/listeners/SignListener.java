package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.utilities.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class SignListener implements Listener {
    ThirdSpareMain plugin;

    public SignListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignText(SignChangeEvent event){
//        event.lines().forEach(line -> line);

        plugin.getLogger().info(event.lines().toString());
    }
}
