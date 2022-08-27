package com.thirdspare.thirdsparemain.entities;

import com.thirdspare.thirdsparemain.chat.ChatChannel;
import com.thirdspare.thirdsparemain.groups.Guild;
import com.thirdspare.thirdsparemain.inventories.Backpack;
import com.thirdspare.thirdsparemain.kotlin.TeleportRequest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * User -
 */
public class User {

    protected Player player; // Represents the given player/user
    private Backpack backpack;

    //Duels
    protected Player against;
    protected boolean inDuel;


    private ChatChannel channelTalkingIn; // Keeps track of the current channel the user is chatting in

    private Location home; //TODO Write player home to file on Disconnect, load into memory on join

    //Teleport requesting
    protected TeleportRequest tpRequest;

    protected double balance;

    //Groups & Guilds
    protected Guild guild; //To be used later

    public User(Player player) {
        this.player = player;
    }

    public void initBackpack(int size) {
        this.backpack = new Backpack(size, "Backpack");
    }

    public Player getPlayer() {
        return player;
    }

    //Teleport commands
    public TeleportRequest getTeleportRequest() {
        return tpRequest;
    }

    public void setTeleportRequest(TeleportRequest requestedTarget) {
        this.tpRequest = requestedTarget;
    }


    //Dueling commands
    public void setInDuel(boolean inDuel) {
        this.inDuel = inDuel;
    }

    public boolean isInDuel() {
        return inDuel;
    }

    public Player getAgainst() {
        return against;
    }

    public void setAgainst(Player against) {
        this.against = against;
    }


    // Chat channel based commands
    public ChatChannel getChannelTalkingIn() {
        return channelTalkingIn;
    }

    public void setChannelTalkingIn(ChatChannel channelTalkingIn) {
        this.channelTalkingIn = channelTalkingIn;
    }

    public Location getHome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    // Guild related section
    public Guild getGuild() {
        return guild;
    }

    public void setGuild(Guild guild) {
        this.guild = guild;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Backpack getBackpack() {
        return backpack;
    }


    @Override
    public String toString() {
        return "User{" +
                "player=" + player +
                ", backpack=" + backpack +
                ", against=" + against +
                ", inDuel=" + inDuel +
                ", channelTalkingIn=" + channelTalkingIn +
                ", requestedTarget=" + tpRequest +
                ", balance=" + balance +
                ", guild=" + guild +
                '}';
    }
}

