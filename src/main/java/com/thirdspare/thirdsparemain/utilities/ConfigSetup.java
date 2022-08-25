package com.thirdspare.thirdsparemain.utilities;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import org.bukkit.World;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ConfigSetup {

    ThirdSpareMain plugin;

    public ConfigSetup(ThirdSpareMain plugin) {
        this.plugin = plugin;
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
     * createJSONConfig - Creates the Config file in a JSON format
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


            //Creating the world JSON object and everything attached
            JSONObject configJSONObj = new JSONObject();
            JSONObject worldsJSONObj = new JSONObject();
            List<World> worlds = plugin.getServer().getWorlds();

            /*
            Loops through each world, creating JSONObjects as well as
            grabbing the world's x, y, z, pitch, yaw of each spawn point
            */
            for (World world : worlds) {
                JSONArray spawnLocationArray = new JSONArray();
                //Creating a new object for each world in case we want to store more data on it later
                JSONObject tempWorld = new JSONObject();
                spawnLocationArray.put(world.getSpawnLocation().getX());
                spawnLocationArray.put(world.getSpawnLocation().getY());
                spawnLocationArray.put(world.getSpawnLocation().getZ());
                spawnLocationArray.put(world.getSpawnLocation().getPitch());
                spawnLocationArray.put(world.getSpawnLocation().getYaw());
                tempWorld.put("spawnLocation", spawnLocationArray);
                worldsJSONObj.put(world.getName(), tempWorld);
            }
            configJSONObj.put("worlds", worldsJSONObj);
            // Writing file to disk
            try {
                Utils.JsonToFile(configJSONObj.toString(2), configFile);
                plugin.getLogger().info("TSM -- JSON CONFIG CREATED");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * createJSONPlayerData - Creates the PlayerData file in a JSON format
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


            JSONObject playerDataJSON = new JSONObject();
            JSONObject defaultData = new JSONObject();
            defaultData.put("name", "test_player");

            playerDataJSON.put("FAKEUUID", defaultData);

            try {
                Utils.JsonToFile(playerDataJSON.toString(4), playerData);
                plugin.getLogger().info("TSM -- Player Data JSON created");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * createJSONChannelData - Creates the channel data file if it has not been created before
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

            //Creating the default JSON Objects to be inserted into the file
            JSONObject channelDataObject = new JSONObject();

            //Channel Object
            JSONObject defaultChannel = new JSONObject();
            defaultChannel.put("name", "GLOBAL");
            defaultChannel.put("prefix", "G");
            defaultChannel.put("color", "G");
            //The Json array that holds the list of channel objects
            JSONArray channelArray = new JSONArray();
            channelArray.put(defaultChannel);
            channelDataObject.put("channel_list", channelArray);

            try {
                Utils.JsonToFile(channelDataObject.toString(4), channelData);
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
