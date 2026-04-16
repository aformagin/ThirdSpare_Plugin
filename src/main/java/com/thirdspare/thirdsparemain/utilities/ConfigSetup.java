package com.thirdspare.thirdsparemain.utilities;

import com.google.gson.Gson;
import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.data.*;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigSetup {

    ThirdSpareMain plugin;
    private final Gson gson;

    public ConfigSetup(ThirdSpareMain plugin) {
        this.plugin = plugin;
        this.gson = Utils.getGson();
    }

    // Legacy methods removed - now using Paper standard data folder methods only

    /**
     * createJSONPlayerDataIfMissing - Creates player data file using Paper's data folder if it doesn't exist
     * @return Returns false if the file had to be created
     */
    public boolean createJSONPlayerDataIfMissing() {
        File playerData = new File(plugin.getDataFolder(), "players.json");
        if (playerData.exists())
            return true;
        else {
            try {
                if (!playerData.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Creating default player data using POJOs
            Map<String, PlayerData> playersMap = new HashMap<>();
            
            PlayerData defaultPlayerData = new PlayerData();
            defaultPlayerData.setName("test_player");
            
            playersMap.put("FAKEUUID", defaultPlayerData);

            try {
                Utils.writeObjectToFile(playersMap, playerData);
                plugin.getLogger().info("TSM -- Player Data JSON created in plugin data folder");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * createJSONConfigIfMissing - Creates config file using Paper's data folder if it doesn't exist
     * @return Returns false if the file had to be created
     */
    public boolean createJSONConfigIfMissing() {
        // Config file path using Paper's data folder
        File configFile = new File(plugin.getDataFolder(), "config.json");

        if (configFile.exists())
            return true;
        else {
            try {
                if (!configFile.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Creating the world configuration data using POJOs
            Map<String, WorldData> worldsMap = new HashMap<>();
            List<World> worlds = plugin.getServer().getWorlds();

            /*
            Loops through each world, creating WorldData objects with
            the world's x, y, z, pitch, yaw of each spawn point
            */
            for (World world : worlds) {
                List<Double> spawnLocationArray = new ArrayList<>();
                spawnLocationArray.add(world.getSpawnLocation().getX());
                spawnLocationArray.add(world.getSpawnLocation().getY());
                spawnLocationArray.add(world.getSpawnLocation().getZ());
                spawnLocationArray.add((double) world.getSpawnLocation().getPitch());
                spawnLocationArray.add((double) world.getSpawnLocation().getYaw());
                
                WorldData worldData = new WorldData(spawnLocationArray);
                worldsMap.put(world.getName(), worldData);
            }
            
            ConfigData configData = new ConfigData(worldsMap);
            
            // Writing file to disk using Gson
            try {
                Utils.writeObjectToFile(configData, configFile);
                plugin.getLogger().info("TSM -- JSON CONFIG CREATED in plugin data folder");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    //Load JSON to Config (Not sure if I will need this yet)
    public boolean loadJSONToConfig() {
        return false;
    }
}
