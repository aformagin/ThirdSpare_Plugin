package com.thirdspare.thirdsparemain.commands.teleportcommands;

import com.thirdspare.thirdsparemain.utilities.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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

                Gson gson = new Gson();
                JsonObject config;
                try {
                    config = Utils.readObjectFromFile(configJSON, JsonObject.class);
                    if (config == null) {
                        config = new JsonObject();
                    }
                } catch (IOException e) {
                    config = new JsonObject();
                }
                JsonObject worldsObject = config.getAsJsonObject("worlds");
                if (worldsObject == null) {
                    worldsObject = new JsonObject();
                }
                //player.getServer().getLogger().info(gson.toJson(worldsObject));
                JsonArray spawnLocationArray = new JsonArray();
                spawnLocationArray.add(world.getSpawnLocation().getX());
                spawnLocationArray.add(world.getSpawnLocation().getY());
                spawnLocationArray.add(world.getSpawnLocation().getZ());
                spawnLocationArray.add(world.getSpawnLocation().getPitch());
                spawnLocationArray.add(world.getSpawnLocation().getYaw());
                //player.getServer().getLogger().info(gson.toJson(spawnLocationArray));
                JsonObject worldJSONObject = new JsonObject();
                worldJSONObject.add("spawnLocation", spawnLocationArray);
                worldsObject.add(world.getName(), worldJSONObject);
                config.add("worlds", worldsObject);
                try {
                    Utils.writeObjectToFile(config, configJSON);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }
}
