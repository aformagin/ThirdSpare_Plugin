package com.thirdspare.thirdsparemain.chat;

import com.google.gson.Gson;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.entities.data.ChannelData;
import com.thirdspare.thirdsparemain.entities.data.ChannelListData;
import com.thirdspare.thirdsparemain.utilities.Utils;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/* TODO - Have ability to be in more than one channel at once. (Needs testing) */
public class ChatManager {
    private final HashMap<Player, String> playerChannel = new HashMap<>();  //List of players and their current channel
    private final HashMap<String, ChatChannel> channelsList; //holds the list of channels loaded from the configuration file

    public ChatManager() {
        //Initializing the list of Channels loaded from config
        this.channelsList = new HashMap<>();
        //Loading the channel list from a JSON file
        loadChannelsFromFile();
    }

    public void joinChannel(User user, String channelName) {
        //Player/User that is joining the channel
        Player player = user.getPlayer();
        //Using a temporary variable to set the name of the channel to uppercase
        String channel = channelName.toUpperCase();

        if (channelsList.containsKey(channel)) {
            ChatChannel cc = channelsList.get(channel); // This is used only for ease of getting name and prefix of the channel
            channelsList.get(channel).addPlayer(player);
            user.setChannelTalkingIn(channelsList.get(channel));

            player.sendMessage(Component.text(String.format("You have joined %s [%s] channel.", cc.getChannelName(), cc.getPrefix())));
        }
    }

    public void leaveChannel(User user, String channelName) {

        Player player = user.getPlayer();
        String channel = channelName.toUpperCase();

        if (channelsList.containsKey(channel)) {
            ChatChannel cc = channelsList.get(channel); // This is used only for ease of getting name and prefix of the channel
            if (cc.getChannelName().equalsIgnoreCase("global")) {
                player.sendMessage(Component.text(String.format("Cannot leave %s Channel.... Yet..", cc.getChannelName())));
                return;
            }

            channelsList.get(channel).removePlayer(player);

            channelsList.forEach((CHANNEL_NAME, CHANNEL_OBJECT) -> {
                if (CHANNEL_OBJECT.getPlayersInChannel().contains(player)) {
                    user.setChannelTalkingIn(CHANNEL_OBJECT);

                    player.sendMessage(Component.text(String.format("You have left %s %s[%s] channel.",
                            cc.getChannelName(), cc.getChannelColor(), cc.getPrefix())));
                    player.sendMessage(Component.text(String.format("Now talking in: %s", CHANNEL_OBJECT.getChannelName())));
                }
            });
        }

    }

    public ArrayList<Player> getChannel(String chatChannelName) {

        var channel = chatChannelName.toUpperCase();

        if (channelsList.containsKey(channel))
            return channelsList.get(channel).getPlayersInChannel();
        else
            return null;
    }


    public HashMap<String, ChatChannel> getChannelsList() {
        return channelsList;
    }

    public void addChannelToList(ChatChannel newChatChannel) {
        this.channelsList.put(newChatChannel.getChannelName(), newChatChannel);
    }

    public String getPlayerChannelName(Player p) {
        return playerChannel.get(p);
    }

    public void loadChannelsFromFile() {
        try {
            ChannelListData channelListData = Utils.readObjectFromFile(new File(Utils.CHANNELS_FILE), ChannelListData.class);
            
            if (channelListData != null && channelListData.getChannelList() != null) {
                for (ChannelData channelData : channelListData.getChannelList()) {
                    String channelName = channelData.getName();
                    String channelPrefix = channelData.getPrefix();
                    String channelColor = channelData.getColor();

                    ChatChannel chatChannel = new ChatChannel(channelName, channelPrefix.charAt(0), channelColor.charAt(0));
                    channelsList.put(channelName, chatChannel);
                }
            }
        } catch (IOException e) {
            // Handle error or create default channel
            ChatChannel defaultChannel = new ChatChannel("GLOBAL", 'G', 'G');
            channelsList.put("GLOBAL", defaultChannel);
        }
    }

}
