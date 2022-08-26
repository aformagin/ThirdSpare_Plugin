package com.thirdspare.thirdsparemain.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatChannel {
    private final String channelName;
    private final char prefix;
    private final ArrayList<Player> PLAYER_LIST;
    private ChatColor channelColor;

    /* Default ChatChannel constructor - No colour set */
    public ChatChannel(String channelName, char prefix) {
        //Initialization of name, prefix and players in list
        this.channelName = channelName;
        this.prefix = prefix;
        PLAYER_LIST = new ArrayList<>();
    }

    /* Secondary ChatChannel constructor - Sets the colour of the chat channel */
    public ChatChannel(String channelName, char prefix, char channelColor) {
        this.channelName = channelName;
        this.prefix = prefix;
        PLAYER_LIST = new ArrayList<>();
        //Initializes the colour of the channel
        setChannelColor(channelColor);
    }

    public ChatColor getChannelColor() {
        return channelColor;
    }

    public void setChannelColor(ChatColor channelColor) {
        this.channelColor = channelColor;
    }

    /**
     * setChannelColor - sets the channel color based on a passed color code
     * @param colorCode - A color code represented by one of the following characters [G,C,B,Y,R,M]
     */
    public void setChannelColor(char colorCode) {
        switch (colorCode) {
            case 'G' -> this.channelColor = ChatColor.GREEN;
            case 'C' -> this.channelColor = ChatColor.AQUA;
            case 'B' -> this.channelColor = ChatColor.BLUE;
            case 'Y' -> this.channelColor = ChatColor.YELLOW;
            case 'R' -> this.channelColor = ChatColor.RED;
            case 'M' -> this.channelColor = ChatColor.LIGHT_PURPLE;
            default -> this.channelColor = ChatColor.WHITE;
        }
    }

    public String getChannelName() {
        return channelName;
    }

    public ArrayList<Player> getPlayersInChannel() {
        return PLAYER_LIST;
    }

    public void addPlayer(Player player) {
        PLAYER_LIST.add(player);
    }

    public void removePlayer(Player player) {
        PLAYER_LIST.remove(player);
    }

    public char getPrefix() {
        return prefix;
    }

    @Override
    public String toString() {
        return "ChatChannel{" +
                "channelName='" + channelName + '\'' +
                ", prefix=" + prefix +
                ", PLAYER_LIST=" + PLAYER_LIST +
                ", channelColor=" + channelColor +
                '}';
    }
}
