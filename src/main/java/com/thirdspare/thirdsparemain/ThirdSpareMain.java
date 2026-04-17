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
import com.thirdspare.thirdsparemain.commands.gameeventcommands.Dueling;
import com.thirdspare.thirdsparemain.commands.econcommands.AddPlayerBalance;
import com.thirdspare.thirdsparemain.commands.econcommands.Balance;
import com.thirdspare.thirdsparemain.commands.econcommands.SetPlayerBalance;
import com.thirdspare.thirdsparemain.commands.teleportcommands.SetSpawnCommand;
import com.thirdspare.thirdsparemain.econ.TSMEconomy;
import com.thirdspare.thirdsparemain.econ.TSMVaultEconomy;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.entities.customitems.BattleAxe;
import com.thirdspare.thirdsparemain.inventories.Backpack;
import com.thirdspare.thirdsparemain.kotlin.commands.Countdown;
import com.thirdspare.thirdsparemain.kotlin.commands.OpenBackpack;
import com.thirdspare.thirdsparemain.kotlin.commands.StatsDump;
import com.thirdspare.thirdsparemain.kotlin.commands.econcommands.Pay;
import com.thirdspare.thirdsparemain.kotlin.commands.tpcommands.TPA;
import com.thirdspare.thirdsparemain.kotlin.commands.tpcommands.TPAccept;
import com.thirdspare.thirdsparemain.listeners.*;
import com.thirdspare.thirdsparemain.utilities.ConfigSetup;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;


public class ThirdSpareMain extends JavaPlugin {
    private ConfigSetup config;
    private TSMEconomy econ;
    public ChatManager chatManager;
    private boolean vaultIntegrationEnabled = false;
    private Economy vaultEconomyProvider;

    /* This HashMap keeps track of our online players based on their UUID, so we can easily grab their User
     * content for checking duels, requested teleports, etc. */
    private final HashMap<UUID, User> onlinePlayers = new HashMap<>();

    @Override
    public void onLoad() {
        super.onLoad();
        var vaultPlugin = getServer().getPluginManager().getPlugin("Vault");
        vaultIntegrationEnabled = vaultPlugin != null;
    }

    @Override
    public void onEnable() {

        var server = getServer();
        var logger = server.getLogger();
        super.onEnable();
        logger.info("ThirdSpareMain loading...");
        logger.info(vaultIntegrationEnabled
                ? "-- Vault detected: Vault integration enabled"
                : "-- Vault not detected: continuing without Vault integration");

        /* Plugin Configuration Setup using Paper standards
         * - Uses getDataFolder() for plugin data directory
         * - Uses saveResource() to copy default files from resources
         * - Creates necessary data files in plugin data folder */

        // Ensure plugin data folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
            logger.info("-- Plugin data folder created at: " + getDataFolder().getAbsolutePath());
        }

        // Save default configuration files using Paper's resource system
        saveResource("channels.json", false); // false = don't replace if exists
        logger.info("-- Default configuration files ensured");

        /* Variable Initialization */
        config = new ConfigSetup(this); //Configuration File Setup - updated to use Paper standards

        /* Initialize default data files if they don't exist */
        config.createJSONPlayerDataIfMissing();
        config.createJSONConfigIfMissing();

        /* Initialize & Register Custom Recipes */
        BattleAxe ba = new BattleAxe(this);
//        Bukkit.addRecipe(ba.getRecipe());

        econ = new TSMEconomy(this); //Base Economy Class
        chatManager = new ChatManager(this); //Base ChatManager Class with Paper data folder support
        initializeVaultEconomyBridge();

        /* Registering all EventListeners */
        logger.info("-- Registering EventListeners..."); //Output to console log that events are registering
        server.getPluginManager().registerEvents(new JoinListener(this), this);
        server.getPluginManager().registerEvents(new ItemPickUpListener(this), this);
        server.getPluginManager().registerEvents(new DeathListener(this), this);
        server.getPluginManager().registerEvents(new RespawnListener(this), this);
        server.getPluginManager().registerEvents(new ChatChannelListener(this), this);
        server.getPluginManager().registerEvents(new Backpack(this), this);
        server.getPluginManager().registerEvents(new DuelListener(this), this);
//        server.getPluginManager().registerEvents(new SignListener(this), this);


        /* Setting CommandExecutors */
        /* TODO Commands
         *   - Request/Accept duel command
         *   - Give special items command - based off of configurable items
         * */

        getServer().getLogger().info("-- Loading commands...");

        //Player based commands
        this.getCommand("roll").setExecutor(new RollCommand());
        this.getCommand("listp").setExecutor(new ListCommand());
        this.getCommand("inv").setExecutor(new OpenBackpack(this));
        this.getCommand("duel").setExecutor(new Dueling(this));
        //Teleport commands
        this.getCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        this.getCommand("tpa").setExecutor(new TPA(this));
        this.getCommand("tpaccept").setExecutor(new TPAccept(this));
        //Econ commands
        this.getCommand("balance").setExecutor(new Balance(this));
        this.getCommand("addbalance").setExecutor(new AddPlayerBalance(this));
        this.getCommand("setbalance").setExecutor(new SetPlayerBalance(this));
        this.getCommand("pay").setExecutor(new Pay(this));
        //Chat commands
        this.getCommand("chat").setExecutor(new ChannelCommands(this));
        this.getCommand("countdown").setExecutor(new Countdown(this));

        final boolean DEVELOPMENT_MODE = true;
        if (DEVELOPMENT_MODE) {
            /* Register TEST commands here */
            this.getCommand("test").setExecutor(new StructCommand());
            this.getCommand("dump").setExecutor(new StatsDump(this));
        }

    }

    @Override
    public void onDisable() {
        if (vaultEconomyProvider != null) {
            getServer().getServicesManager().unregister(Economy.class, vaultEconomyProvider);
            getLogger().info("-- Vault economy bridge unregistered");
        }
        super.onDisable();
    }

    public TSMEconomy getTSMEconomy() {
        return this.econ;
    }

    public boolean isVaultIntegrationEnabled() {
        return vaultIntegrationEnabled;
    }

    private void initializeVaultEconomyBridge() {
        if (!vaultIntegrationEnabled) {
            return;
        }

        var existingEconomy = getServer().getServicesManager().getRegistration(Economy.class);
        if (existingEconomy != null) {
            getLogger().warning("-- Existing Vault economy provider detected (" + existingEconomy.getProvider().getName()
                    + "). Skipping ThirdSpare Vault economy bridge registration.");
            return;
        }

        vaultEconomyProvider = new TSMVaultEconomy(this);
        getServer().getServicesManager().register(Economy.class, vaultEconomyProvider, this, ServicePriority.Normal);
        getLogger().info("-- ThirdSpare Vault economy bridge registered");
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
