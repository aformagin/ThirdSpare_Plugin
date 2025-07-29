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

    /**
     * isConfigDirCreated - Checks to see if the config directory has been created yet
     * @return Returns the results of !mkdirs on the config directory
     */
    public boolean isConfigDirCreated() {
        File configPath = new File("plugins" + File.separator + "TSM" + File.separator +  "configs" + File.separator);
        return !configPath.mkdirs();
    }

    /**
     * isDataDirCreated - Checks to see if the data directory has been created yet
     * @return Returns the result of !mkdirs() for the data folder location
     */
    public boolean isDataDirCreated() {
        File dataPath = new File("plugins" + File.separator + "TSM" + File.separator + "data" + File.separator);
        return !dataPath.mkdirs();
    }

    /**
     * createJSONConfig - Creates the Config file in a JSON format using Gson
     * @return Returns FALSE if the method has to create the file
     */
    public boolean createJSONConfig() {
        // Config file path -- Hardcoded for now.
        File configFile = new File(Utils.CONFIG_FILE);

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
                plugin.getLogger().info("TSM -- JSON CONFIG CREATED");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * createJSONPlayerData - Creates the PlayerData file in a JSON format using Gson
     * @return Returns FALSE of the method has to create the file
     */
    public boolean createJSONPlayerData() {
        File playerData = new File(Utils.PLAYERS_FILE);
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
                plugin.getLogger().info("TSM -- Player Data JSON created");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * createJSONChannelData - Creates the channel data file if it has not been created before using Gson
     * @return Returns FALSE if the file has to be created by the method
     */
    public boolean createJSONChannelData() {
        File channelData = new File(Utils.CHANNELS_FILE);
        if (channelData.exists())
            return true;
        else {
            try {
                if (!channelData.createNewFile())
                    return false;
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Creating the default channel data using POJOs
            ChannelData defaultChannel = new ChannelData("GLOBAL", "G", "G");
            
            List<ChannelData> channelList = new ArrayList<>();
            channelList.add(defaultChannel);
            
            ChannelListData channelListData = new ChannelListData(channelList);

            try {
                Utils.writeObjectToFile(channelListData, channelData);
                plugin.getLogger().info("TSM -- Channel Data JSON created");
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
