package com.thirdspare.thirdsparemain.chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class ChatChannel {
    private final String channelName;
    private final char prefix;
    private final ArrayList<Player> PLAYER_LIST;
    private ChatColor channelColor;

    public ChatChannel(String channelName, char prefix) {
        this.channelName = channelName;
        this.prefix = prefix;
        //Initialize the list of players in the channel
        PLAYER_LIST = new ArrayList<>();
    }

    public ChatChannel(String channelName, char prefix, char channelColor) {
        //Setting channel name, prefix and color
        this.channelName = channelName;
        this.prefix = prefix;
        setChannelColor(channelColor);
        //Initialize the list of players in the channel
        PLAYER_LIST = new ArrayList<>();
    }

    public ChatColor getChannelColor() {
        return channelColor;
    }

    public void setChannelColor(ChatColor channelColor) {
        this.channelColor = channelColor;
    }

    /**
     * setChannelColor - sets the channel color based on a passed color code
     *
     * @param colorCode - A color code represented by one of the following characters
     *                  G - Green
     *                  C - Aqua/Cyan
     *                  B - Blue
     *                  Y - Yellow
     *                  R - Red
     *                  M - Light Purple/Magenta
     */
    public void setChannelColor(char colorCode) {
        switch (colorCode) {
            case 'G':
                this.channelColor = ChatColor.GREEN;
                break;
            case 'C':
                this.channelColor = ChatColor.AQUA;
                break;
            case 'B':
                this.channelColor = ChatColor.BLUE;
                break;
            case 'Y':
                this.channelColor = ChatColor.YELLOW;
                break;
            case 'R':
                this.channelColor = ChatColor.RED;
                break;
            case 'M':
                this.channelColor = ChatColor.LIGHT_PURPLE;
                break;
            default:
                this.channelColor = ChatColor.WHITE;
                break;
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
