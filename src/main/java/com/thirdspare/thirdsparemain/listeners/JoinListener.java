package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {

    JSONObject playersJSON;
    private final File playerFile = new File(Utils.PLAYERS_FILE);
    ThirdSpareMain plugin;

    public JoinListener(ThirdSpareMain instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        /* Updated to PaperMC 1.16.5 #473 formatting */
        var msg = String.format("%s%s%s joined.", ChatColor.GREEN, ChatColor.BOLD, event.getPlayer().getName());
        var joinMessage = Component.text(msg);
        event.joinMessage(joinMessage);

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        //Player's UUID
        String UUID = event.getPlayer().getUniqueId().toString();

        playersJSON = new JSONObject(Utils.FileToJSONString(playerFile));

        //If the player is not in the JSON object, create them in the file (New player creation)
        if (!playersJSON.has(UUID)) {
            plugin.getLogger().warning("TSM -- FIRST TIME JOIN, ADDED TO DATA");
            JSONObject PLAYER = new JSONObject();
            PLAYER.put("name", event.getPlayer().getName());
            PLAYER.put("last_joined", java.time.LocalDateTime.now());
            PLAYER.put("last_ip_address", event.getPlayer().getAddress().toString());
            PLAYER.put("balance", 100.00);
            PLAYER.put("bp_size", 9); // Default size for backpack, TODO - this should be read from a config file
            PLAYER.put("home", "FILLER");
            JSONArray items = new JSONArray();
            PLAYER.put("backpack", items);
            playersJSON.put(UUID, PLAYER);
            try {
                Utils.JsonToFile(playersJSON.toString(2), playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            //If the player has joined before, update this information
            playersJSON.getJSONObject(UUID).put("name", event.getPlayer().getName());
            playersJSON.getJSONObject(UUID).put("last_joined", java.time.LocalDateTime.now());
            playersJSON.getJSONObject(UUID).put("last_ip_address", event.getPlayer().getAddress().toString());
            try {
                Utils.JsonToFile(playersJSON.toString(2), playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Retrieving player data from player file to load into user Object
        var bal = playersJSON.getJSONObject(UUID).getDouble("balance");
        var backpackInv = playersJSON.getJSONObject(UUID).getJSONArray("backpack");
        var bp_size = playersJSON.getJSONObject(UUID).getInt("bp_size");
        var homeSerialized = playersJSON.getJSONObject(UUID).get("home");


        User user = new User(player);

        //Make sure it does not have the filler text
        if (!homeSerialized.equals("FILLER")) {
            var deserializedHome = Location.deserialize((Map<String, Object>) homeSerialized);
            user.setHome(deserializedHome);
        }
        //Initializing the user's backpack
        user.initBackpack(bp_size);
        //Setting the user's balance
        user.setBalance(bal);
        plugin.getServer().getLogger().info("Player Balance assigned as: " + bal);

        if (!backpackInv.isEmpty()) {
            //Where i is the inventory slot, and item is the item stack placed in slot 'i'
            for (int i = 0; i < backpackInv.length(); i++) {
                var encodedString = String.valueOf(backpackInv.get(i));
                if (!encodedString.equals("")) {
                    ItemStack item = user.getBackpack().deserializeItem(encodedString);
                    //TODO figure out how to get them in order -- Is this done?
                    user.getBackpack().getInv().setItem(i, item);
                }
            }
        }

        plugin.insertOnlinePlayer(player.getUniqueId(), user);
        plugin.chatManager.joinChannel(user, "global"); //Should only do this if user has no channel selected
        //TODO Read last set channel in from file and set it

        //Health under names
        // TODO find non-deprecated alternative
//        Objective objective = board.registerNewObjective("showhealth", "health", "/ 20", RenderType.INTEGER);
//        objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
//        objective.setDisplayName("/ 20");

//        player.setResourcePack(Utils.RPACK_LINK, Utils.RPACK_HASH);


        plugin.getServer().getLogger().info(plugin.getOnlinePlayers().toString());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        //Getting the player that is quitting
        var player = event.getPlayer();
        HashMap<UUID, User> tempMap = plugin.getOnlinePlayers();
        UUID playerUUID = player.getUniqueId();
        //The User that is leaving the server
        var user = tempMap.get(playerUUID);
        //Removes player quit message for now
        event.quitMessage(null);

        //Getting the playersJSON file to retrieve player
        playersJSON = new JSONObject(Utils.FileToJSONString(playerFile));
        var bal = plugin.getOnlinePlayers().get(player.getUniqueId()).getBalance();
        var leavingPlayerJson = playersJSON.getJSONObject(player.getUniqueId().toString());
        leavingPlayerJson.put("balance", bal);

        var items = new JSONArray();
        //When player leaves, get their backpack content and write it to the file
        var backpackItems = user.getBackpack().getInv().getContents();
        leavingPlayerJson.remove("backpack"); // Removes backpack before rebuilding it

        for (ItemStack backpackItem : backpackItems) {
            if (backpackItem != null) {
                //Serializing the items to Base64
                items.put(user.getBackpack().serializeItem(backpackItem));
            }
            //If there is no item in the space, place an empty string
            items.put("");
        }

        leavingPlayerJson.put("backpack", items);

        //Serializes the players home location to a Map<String, Object>
        Map<String, Object> home = null;

        if (user.getHome() != null)
            home = user.getHome().serialize();

        //TODO Test writing home to file
        if(home != null)
            leavingPlayerJson.put("home", home);
        else
            leavingPlayerJson.put("home", "FILLER");


        //Writing data to JSON
        playersJSON.put(String.valueOf(playerUUID), leavingPlayerJson);

        //Writing the changes to the data file
        try {
            Utils.JsonToFile(playersJSON.toString(2), playerFile);
            plugin.getLogger().info("Player written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Remove player from the hashmap
        plugin.removeOnlinePlayer(playerUUID, tempMap.get(playerUUID));
    }
}
