package com.thirdspare.thirdsparemain.listeners;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.entities.data.PlayerData;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class JoinListener implements Listener {

    private final Gson gson;
    ThirdSpareMain plugin;

    public JoinListener(ThirdSpareMain instance) {
        plugin = instance;
        this.gson = Utils.getGson();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        //Declare local variables
        var player = event.getPlayer();
        User user;
        double playerBalance;
        /* Updated to PaperMC 1.16.5 #473 formatting */
        var joinMessage = Component.text(event.getPlayer().getName() + " joined.")
                .color(NamedTextColor.GREEN)
                .decorate(TextDecoration.BOLD);
        event.joinMessage(joinMessage);

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        Scoreboard board = scoreboardManager.getNewScoreboard();
        //Player's UUID
        String UUID = event.getPlayer().getUniqueId().toString();

        // Enhanced JSON loading with comprehensive error recovery for player join
        // Handles empty files, corrupted JSON, and I/O errors gracefully
        // Now uses Paper's standard data folder
        File playerFile = Utils.getPlayersFile(plugin);
        Type playerMapType = new TypeToken<Map<String, PlayerData>>(){}.getType();
        Map<String, PlayerData> playersMap;
        
        try {
            playersMap = Utils.readObjectFromFile(playerFile, playerMapType);
            if (playersMap == null) {
                plugin.getLogger().warning("TSM -- Player data file empty or corrupted on join, creating new data structure");
                playersMap = new HashMap<>();
            }
        } catch (IOException e) {
            plugin.getLogger().severe("TSM -- Failed to read player data file on join: " + e.getMessage());
            plugin.getLogger().info("TSM -- Player file location: " + playerFile.getAbsolutePath());
            plugin.getLogger().warning("TSM -- Using empty player data structure for this session");
            playersMap = new HashMap<>();
        }

        //If the player is not in the map, create them (New player creation)
        if (!playersMap.containsKey(UUID)) {
            plugin.getLogger().warning("TSM -- FIRST TIME JOIN, PLAYER ADDED TO DATA");
            
            PlayerData newPlayer = new PlayerData();
            newPlayer.setName(event.getPlayer().getName());
            newPlayer.setLastJoined(LocalDateTime.now());
            newPlayer.setLastIpAddress(event.getPlayer().getAddress().toString());
            newPlayer.setBalance(100.00);
            newPlayer.setBpSize(9); // Default size for backpack, TODO - this should be read from a config file
            newPlayer.setHome(null); // No home set initially
            newPlayer.setBackpack(new ArrayList<>()); // Empty backpack initially

            playersMap.put(UUID, newPlayer);

            try {
                Utils.writeObjectToFile(playersMap, playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            //If the player has joined before, update this information
            PlayerData existingPlayer = playersMap.get(UUID);
            existingPlayer.setName(event.getPlayer().getName());
            existingPlayer.setLastJoined(LocalDateTime.now());
            existingPlayer.setLastIpAddress(event.getPlayer().getAddress().toString());
            
            try {
                Utils.writeObjectToFile(playersMap, playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Retrieving player data from player file to load into user Object
        PlayerData currentPlayerData = playersMap.get(UUID);
        playerBalance = currentPlayerData.getBalance();
        List<String> backpackInv = currentPlayerData.getBackpack();
        int bp_size = currentPlayerData.getBpSize();
        Map<String, Object> homeSerialized = currentPlayerData.getHome();


        user = new User(player);

        //Make sure home location exists
        if (homeSerialized != null) {
            var deserializedHome = Location.deserialize(homeSerialized);
            user.setHome(deserializedHome);
        }

        user.initBackpack(bp_size); //Initializing the user's backpack
        user.setBalance(playerBalance); //Setting the user's balance
        plugin.getServer().getLogger().info("Player Balance assigned as: " + playerBalance);

        if (backpackInv != null && !backpackInv.isEmpty()) {
            //Where i is the inventory slot, and item is the item stack placed in slot 'i'
            for (int i = 0; i < backpackInv.size(); i++) {
                String encodedString = backpackInv.get(i);
                if (encodedString != null && !encodedString.equals("")) {
                    ItemStack item = user.getBackpack().deserializeItem(encodedString);
                    user.getBackpack().getInv().setItem(i, item);
                }
            }
        }

        // Enhanced User validation and storage with error recovery
        // Ensures User object is properly created and stored in online players map

        try {
            plugin.insertOnlinePlayer(player.getUniqueId(), user);
            
            // Verify the user was actually stored correctly
            User verifyUser = plugin.getOnlinePlayers().get(player.getUniqueId());
            if (verifyUser == null) {
                plugin.getLogger().severe("TSM -- CRITICAL ERROR: Failed to store User object for player " + player.getName());
                player.kick(Component.text("Failed to save player data. Please contact an administrator."));
                return;
            }
            
            plugin.chatManager.joinChannel(user, "global"); //Should only do this if user has no channel selected
            //TODO Read last set channel in from file and set it
            
            plugin.getLogger().info("TSM -- Successfully loaded and stored User for player: " + player.getName());
            
        } catch (Exception e) {
            plugin.getLogger().severe("TSM -- Exception while storing User object for player " + player.getName() + ": " + e.getMessage());
            e.printStackTrace();
            player.kick(Component.text("Error during login process. Please contact an administrator."));
        }

        // The resource pack feature is currently disabled.
        // To enable it, uncomment the following line and replace the placeholder URL with a direct download link to the resource pack.
        // The resource pack hash (SHA-1) should also be provided for security.
        // It is recommended to make the URL and hash configurable in the plugin's config.yml.
        // The resource pack itself needs to be created and hosted on a web server or file hosting service.
//        player.setResourcePack(Utils.RPACK_LINK, Utils.RPACK_HASH);

        plugin.getServer().getLogger().info(plugin.getOnlinePlayers().toString());
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        //Getting the player that is quitting
        var player = event.getPlayer();
        HashMap<UUID, User> tempMap = plugin.getOnlinePlayers();
        UUID playerUUID = player.getUniqueId();
        // Enhanced User validation for player quit event
        // Ensures User object exists before attempting to save data
        var user = tempMap.get(playerUUID);
        if (user == null) {
            plugin.getLogger().warning("TSM -- User object is null for leaving player " + player.getName() + ". Data cannot be saved.");
            //Still remove from quit message to prevent spam
            event.quitMessage(null);
            return;
        }
        //Removes player quit message for now
        event.quitMessage(null);

        // Enhanced JSON loading with comprehensive error recovery for player quit
        // Handles empty files, corrupted JSON, and I/O errors gracefully
        // Now uses Paper's standard data folder
        File playerFile = Utils.getPlayersFile(plugin);
        Type playerMapType = new TypeToken<Map<String, PlayerData>>(){}.getType();
        Map<String, PlayerData> playersMap;
        
        try {
            playersMap = Utils.readObjectFromFile(playerFile, playerMapType);
            if (playersMap == null) {
                plugin.getLogger().warning("TSM -- Player data file empty or corrupted on quit, creating new data structure");
                playersMap = new HashMap<>();
            }
        } catch (IOException e) {
            plugin.getLogger().severe("TSM -- Failed to read player data file on quit: " + e.getMessage());
            plugin.getLogger().info("TSM -- Player file location: " + playerFile.getAbsolutePath());
            plugin.getLogger().warning("TSM -- Player data may not be saved properly this session");
            playersMap = new HashMap<>();
        }
        
        double bal = plugin.getOnlinePlayers().get(player.getUniqueId()).getBalance();
        PlayerData leavingPlayerData = playersMap.get(player.getUniqueId().toString());
        List<String> items = new ArrayList<>();
        //When player leaves, get their backpack content and write it to the file
        ItemStack[] backpackItems = user.getBackpack().getInv().getContents();

        /* Rebuilding and serializing backpack for writing to file */
        for (ItemStack backpackItem : backpackItems) {
            if (backpackItem != null) {
                //Serializing the items to Base64
                items.add(user.getBackpack().serializeItem(backpackItem));
            } else {
                //If there is no item in the space, place an empty string
                items.add("");
            }
        }


        //Serializes the players home location to a Map<String, Object>
        Map<String, Object> home = null;

        if (user.getHome() != null)
            home = user.getHome().serialize();

        //Update player data
        leavingPlayerData.setHome(home); //Will write as null if location is not set by player
        leavingPlayerData.setBackpack(items);
        leavingPlayerData.setBalance(bal);

        //Writing data using Gson
        playersMap.put(String.valueOf(playerUUID), leavingPlayerData);

        //Writing the changes to the data file
        try {
            Utils.writeObjectToFile(playersMap, playerFile);
            plugin.getLogger().info("Player written to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Remove player from the hashmap
        plugin.removeOnlinePlayer(playerUUID, tempMap.get(playerUUID));
    }
}
