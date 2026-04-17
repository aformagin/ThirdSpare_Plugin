package com.thirdspare.thirdsparemain.econ;

import com.google.gson.reflect.TypeToken;
import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.entities.data.PlayerData;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TSMVaultEconomy implements Economy {

    private static final int DEFAULT_BACKPACK_SIZE = 9;
    private static final String CURRENCY_SINGULAR = "credit";
    private static final String CURRENCY_PLURAL = "credits";

    private final ThirdSpareMain plugin;
    private final Type playerMapType = new TypeToken<Map<String, PlayerData>>() {}.getType();

    public TSMVaultEconomy(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean isEnabled() {
        return plugin.isEnabled();
    }

    @Override
    public String getName() {
        return "ThirdSpareEconomy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return 2;
    }

    @Override
    public String format(double amount) {
        return String.format("%.2f %s", amount, amount == 1.0 ? CURRENCY_SINGULAR : CURRENCY_PLURAL);
    }

    @Override
    public String currencyNamePlural() {
        return CURRENCY_PLURAL;
    }

    @Override
    public String currencyNameSingular() {
        return CURRENCY_SINGULAR;
    }

    @Override
    public boolean hasAccount(String playerName) {
        var player = resolvePlayer(playerName);
        return player != null && hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return getStoredPlayerData(player.getUniqueId()) != null;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return hasAccount(playerName);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public double getBalance(String playerName) {
        var player = resolvePlayer(playerName);
        if (player == null) {
            return 0.0;
        }
        return getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        var online = player.getPlayer();
        if (online != null) {
            User user = plugin.getOnlinePlayers().get(online.getUniqueId());
            if (user != null) {
                return user.getBalance();
            }
        }

        PlayerData data = getStoredPlayerData(player.getUniqueId());
        if (data == null) {
            return 0.0;
        }
        return data.getBalance();
    }

    @Override
    public double getBalance(String playerName, String world) {
        return getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        var player = resolvePlayer(playerName);
        return player != null && has(player, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return getBalance(player) >= amount;
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        var player = resolvePlayer(playerName);
        if (player == null) {
            return failureResponse(0.0, amount, "Unknown player");
        }
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount < 0.0) {
            return failureResponse(getBalance(player), amount, "Cannot withdraw negative amount");
        }
        if (!Double.isFinite(amount)) {
            return failureResponse(getBalance(player), amount, "Amount is not finite");
        }
        if (!hasAccount(player)) {
            return failureResponse(0.0, amount, "Account does not exist");
        }

        double currentBalance = getBalance(player);
        if (currentBalance < amount) {
            return failureResponse(currentBalance, amount, "Insufficient funds");
        }

        double newBalance = currentBalance - amount;
        if (!setBalance(player, newBalance)) {
            return failureResponse(currentBalance, amount, "Failed to persist balance change");
        }
        return successResponse(newBalance, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        var player = resolvePlayer(playerName);
        if (player == null) {
            return failureResponse(0.0, amount, "Unknown player");
        }
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount < 0.0) {
            return failureResponse(getBalance(player), amount, "Cannot deposit negative amount");
        }
        if (!Double.isFinite(amount)) {
            return failureResponse(getBalance(player), amount, "Amount is not finite");
        }

        if (!hasAccount(player) && !createPlayerAccount(player)) {
            return failureResponse(0.0, amount, "Failed to create account");
        }

        double currentBalance = getBalance(player);
        double newBalance = currentBalance + amount;
        if (!setBalance(player, newBalance)) {
            return failureResponse(currentBalance, amount, "Failed to persist balance change");
        }
        return successResponse(newBalance, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return notImplementedResponse("Bank accounts are not supported");
    }

    @Override
    public java.util.List<String> getBanks() {
        return Collections.emptyList();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        var player = resolvePlayer(playerName);
        return player != null && createPlayerAccount(player);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        if (hasAccount(player)) {
            return true;
        }

        String name = player.getName() != null ? player.getName() : player.getUniqueId().toString();
        PlayerData newData = new PlayerData();
        newData.setName(name);
        newData.setLastJoined(LocalDateTime.now());
        newData.setLastIpAddress("");
        newData.setBalance(0.0);
        newData.setBpSize(DEFAULT_BACKPACK_SIZE);
        newData.setHome(null);
        newData.setBackpack(new ArrayList<>());

        Map<String, PlayerData> playersMap = readPlayersMap();
        playersMap.put(player.getUniqueId().toString(), newData);
        return writePlayersMap(playersMap);
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return createPlayerAccount(playerName);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }

    private synchronized Map<String, PlayerData> readPlayersMap() {
        try {
            Map<String, PlayerData> playersMap = Utils.readObjectFromFile(Utils.getPlayersFile(plugin), playerMapType);
            if (playersMap == null) {
                return new HashMap<>();
            }
            return playersMap;
        } catch (IOException e) {
            plugin.getLogger().warning("TSM -- Vault bridge failed to read players.json: " + e.getMessage());
            return new HashMap<>();
        }
    }

    private synchronized boolean writePlayersMap(Map<String, PlayerData> playersMap) {
        try {
            Utils.writeObjectToFile(playersMap, Utils.getPlayersFile(plugin));
            return true;
        } catch (IOException e) {
            plugin.getLogger().severe("TSM -- Vault bridge failed to write players.json: " + e.getMessage());
            return false;
        }
    }

    private PlayerData getStoredPlayerData(UUID playerUuid) {
        return readPlayersMap().get(playerUuid.toString());
    }

    private boolean setBalance(OfflinePlayer player, double newBalance) {
        if (player == null) {
            return false;
        }

        Player online = player.getPlayer();
        if (online != null) {
            User user = plugin.getOnlinePlayers().get(online.getUniqueId());
            if (user != null) {
                user.setBalance(newBalance);
            }
        }

        Map<String, PlayerData> playersMap = readPlayersMap();
        String key = player.getUniqueId().toString();
        PlayerData data = playersMap.get(key);

        if (data == null) {
            if (!createPlayerAccount(player)) {
                return false;
            }
            playersMap = readPlayersMap();
            data = playersMap.get(key);
            if (data == null) {
                return false;
            }
        }

        data.setBalance(newBalance);
        playersMap.put(key, data);
        return writePlayersMap(playersMap);
    }

    private OfflinePlayer resolvePlayer(String playerName) {
        if (playerName == null || playerName.isBlank()) {
            return null;
        }

        Player online = Bukkit.getPlayerExact(playerName);
        if (online != null) {
            return online;
        }

        OfflinePlayer cached = Bukkit.getOfflinePlayerIfCached(playerName);
        if (cached != null) {
            return cached;
        }

        UUID uuidFromStore = findUuidByNameFromStore(playerName);
        if (uuidFromStore != null) {
            return Bukkit.getOfflinePlayer(uuidFromStore);
        }

        return null;
    }

    private UUID findUuidByNameFromStore(String playerName) {
        Map<String, PlayerData> playersMap = readPlayersMap();
        for (Map.Entry<String, PlayerData> entry : playersMap.entrySet()) {
            PlayerData data = entry.getValue();
            if (data == null || data.getName() == null) {
                continue;
            }
            if (!data.getName().equalsIgnoreCase(playerName)) {
                continue;
            }
            try {
                return UUID.fromString(entry.getKey());
            } catch (IllegalArgumentException ignored) {
                plugin.getLogger().warning("TSM -- Vault bridge found invalid UUID key in players.json: " + entry.getKey());
                return null;
            }
        }
        return null;
    }

    private EconomyResponse successResponse(double balance, double amount) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.SUCCESS, null);
    }

    private EconomyResponse failureResponse(double balance, double amount, String errorMessage) {
        return new EconomyResponse(amount, balance, EconomyResponse.ResponseType.FAILURE, errorMessage);
    }

    private EconomyResponse notImplementedResponse(String errorMessage) {
        return new EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, errorMessage);
    }
}
