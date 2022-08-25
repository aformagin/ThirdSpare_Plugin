package com.thirdspare.thirdsparemain.commands.teleportcommands;

import com.thirdspare.thirdsparemain.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class SetSpawnCommand implements CommandExecutor {
    private final File configJSON = new File("plugins/TSM/configs/config.json");

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player player){
            var world = player.getWorld();
            if(command.getName().equalsIgnoreCase("setspawn")){
                var loc = player.getLocation();
                var msg = String.format("Spawn set in %s by %s", world.getName(), player.getName());
                player.sendMessage(msg);
                player.getServer().getLogger().info(msg);
                world.setSpawnLocation(loc);

                var jsonResponse = Utils.FileToJSONString(configJSON);
                JSONObject config = new JSONObject(jsonResponse);
                JSONObject worldsObject = config.getJSONObject("worlds");
                //player.getServer().getLogger().info(worldsObject.toString(2));
                JSONArray spawnLocationArray = new JSONArray();
                spawnLocationArray.put(world.getSpawnLocation().getX());
                spawnLocationArray.put(world.getSpawnLocation().getY());
                spawnLocationArray.put(world.getSpawnLocation().getZ());
                spawnLocationArray.put(world.getSpawnLocation().getPitch());
                spawnLocationArray.put(world.getSpawnLocation().getYaw());
                //player.getServer().getLogger().info(spawnLocationArray.toString(2));
                JSONObject worldJSONObject = new JSONObject();
                worldJSONObject.put("spawnLocation", spawnLocationArray);
                worldsObject.put(world.getName(), worldJSONObject);
                config.put("worlds", worldsObject);
                try {
                    Utils.JsonToFile(config.toString(4), configJSON);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
