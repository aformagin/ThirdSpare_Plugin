package com.thirdspare.thirdsparemain;
/* ThirdSpare Main Plugin -- Abbreviated TSM
 * Author: auti117
 * Lets do this plugin right this time */
/* TODO Set up plugin permissions globally and hook vault into TSMEconomy */

import com.thirdspare.thirdsparemain.chat.ChatManager;
import com.thirdspare.thirdsparemain.commands.ChannelCommands;
import com.thirdspare.thirdsparemain.commands.ListCommand;
import com.thirdspare.thirdsparemain.commands.RollCommand;
import com.thirdspare.thirdsparemain.commands.StructCommand;
import com.thirdspare.thirdsparemain.commands.econcommands.AddPlayerBalance;
import com.thirdspare.thirdsparemain.commands.econcommands.Balance;
import com.thirdspare.thirdsparemain.commands.econcommands.SetPlayerBalance;
import com.thirdspare.thirdsparemain.commands.teleportcommands.SetSpawnCommand;
import com.thirdspare.thirdsparemain.econ.TSMEconomy;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.entities.customitems.BattleAxe;
import com.thirdspare.thirdsparemain.inventories.Backpack;
import com.thirdspare.thirdsparemain.kotlin.commands.Countdown;
import com.thirdspare.thirdsparemain.kotlin.commands.StatsDump;
import com.thirdspare.thirdsparemain.kotlin.commands.OpenBackpack;
import com.thirdspare.thirdsparemain.kotlin.commands.econcommands.Pay;
import com.thirdspare.thirdsparemain.kotlin.commands.tpcommands.TPA;
import com.thirdspare.thirdsparemain.listeners.*;
import com.thirdspare.thirdsparemain.utilities.ConfigSetup;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

// FOR DUELING LOOK INTO ENTITY DAMAGE BY ENTITY EVENT to prevent other players from attacking
public class ThirdSpareMain extends JavaPlugin {
    private ConfigSetup config;
    private TSMEconomy econ;
    public ChatManager chatManager;

    /* This HashMap keeps track of our online players based on their UUID, so we can easily grab their User
     * content for checking duels, requested teleports, etc. */
    private final HashMap<UUID, User> onlinePlayers = new HashMap<>();

    @Override
    public void onEnable() {

        var server = getServer();
        var logger = server.getLogger();
        super.onEnable();
        logger.info("ThirdSpareMain loading...");

        config = new ConfigSetup(this);


        BattleAxe ba = new BattleAxe(this);

//        Bukkit.addRecipe(ba.getRecipe());

        // Checking if required directories have been created
        //Config Directory
        if (config.isConfigDirCreated())
            logger.info("-- Config Directory Exists...");
        else
            logger.info("-- Config Directory Now Created...");
        //Data Directory
        if (config.isDataDirCreated())
            logger.info("-- Data Directory Exists...");
        else
            logger.info("-- Data Directory Now Created...");
        //Creating config file
        if (!config.createJSONConfig())
            logger.warning("-- Config file exists or was not created\n" +
                    "-- Most likely nothing to worry about.");
        //Creating player file
        if (!config.createJSONPlayerData())
            logger.warning("-- Data file exists or was not created\n" +
                    "-- Most likely nothing to worry about.");
        if (!config.createJSONChannelData())
            logger.warning("-- Chat Channel Data file exists or was not created\n" +
                    "-- Most likely nothing to worry about.");


        econ = new TSMEconomy(this);
        chatManager = new ChatManager();
        //Load required listeners
        logger.info("-- Loading listeners...");
        server.getPluginManager().registerEvents(new JoinListener(this), this);
        server.getPluginManager().registerEvents(new ItemPickUpListener(this), this);
        server.getPluginManager().registerEvents(new DeathListener(this), this);
        server.getPluginManager().registerEvents(new RespawnListener(this), this);
        server.getPluginManager().registerEvents(new ChatChannelListener(this), this);
        server.getPluginManager().registerEvents(new Backpack(this), this);
//        server.getPluginManager().registerEvents(new SignListener(this), this);
        //load commands
        getServer().getLogger().info("-- Loading commands...");
        /* TODO Commands
         *   - Request/Accept duel command
         *   - Give special items command - based off of configurable items */
        //noinspection ConstantConditions
        this.getCommand("roll").setExecutor(new RollCommand());
        //noinspection ConstantConditions
        this.getCommand("listp").setExecutor(new ListCommand());
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand());
        //Econ commands
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("addbalance").setExecutor(new AddPlayerBalance(this));
        this.getCommand("setbalance").setExecutor(new SetPlayerBalance(this));
        this.getCommand("pay").setExecutor(new Pay(this));

        this.getCommand("chat").setExecutor(new ChannelCommands(this));
        this.getCommand("countdown").setExecutor(new Countdown(this));
        this.getCommand("inv").setExecutor(new OpenBackpack(this));

        //For testing purposes only
//        this.getCommand("test").setExecutor(new StructCommand());
        this.getCommand("tpa").setExecutor(new TPA(this));
//        this.getCommand("dump").setExecutor(new StatsDump(this));
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    public TSMEconomy getTSMEconomy() {
        return this.econ;
    }

    /**
     * insertOnlinePlayer - Inserts a UUID and User representing a player into the onlinePlayers HashMap
     *
     * @param uuid - The unique ID of the player (obtained from Player::getUniqueID)
     * @param user - The user object that is being passed, hold their player info and server specific info
     */
    public void insertOnlinePlayer(UUID uuid, User user) {
        onlinePlayers.put(uuid, user);
    }

    /**
     * removeOnlinePlayer - Removes the selected player from the onlinePlayers HashMap
     *
     * @param uuid - The unique ID of the player (obtained from Player::getUniqueID)
     * @param user - The user object that holds the players info to be removed.
     */
    public void removeOnlinePlayer(UUID uuid, User user) {
        onlinePlayers.remove(uuid, user);
    }

    public void updateOnlinePlayer(UUID uuid, User user) {
        onlinePlayers.replace(uuid, user);
    }

    public HashMap<UUID, User> getOnlinePlayers() {
        return onlinePlayers;
    }
}
