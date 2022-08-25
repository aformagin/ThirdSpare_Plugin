package com.thirdspare.thirdsparemain.chat;

import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.utilities.Utils;
import org.bukkit.entity.Player;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/* TODO - Have ability to be in more than one channel at once. (Needs testing) */
public class ChatManager {
    //List of players and their current channel
    private HashMap<Player, String> playerChannel = new HashMap<>();

    //Each channel has a list of players
    private HashMap<String, ChatChannel> channelsList;

    public ChatManager() {
        this.channelsList = new HashMap<>();
        //Loading the channel list from a JSON file
        loadChannelsFromFile();
    }

    public void joinChannel(User user, String channelName) {
        //Player/User that is joining the channel
        Player player = user.getPlayer();
        //Using a temporary variable to set the name of the channel to uppercase
        String channel = channelName.toUpperCase();
        //Made this more modular so that I can add more channels via config
        if (channelsList.containsKey(channel)) {
            ChatChannel cc = channelsList.get(channel); // This is used only for ease of getting name and prefix of the channel
            channelsList.get(channel).addPlayer(player);
            user.setChannelTalkingIn(channelsList.get(channel));
            var msg = String.format("You have joined %s [%s] channel.", cc.getChannelName(), cc.getPrefix());
            player.sendMessage(msg);
        }
    }


    // TODO - Rework this method so it is compatible with current way of joining/leaving
    public void leaveChannel(User user, String channelName) {
        Player player = user.getPlayer();
        String channel = channelName.toUpperCase();
        if (channelsList.containsKey(channel)) {
            ChatChannel cc = channelsList.get(channel); // This is used only for ease of getting name and prefix of the channel
            if (cc.getChannelName().equalsIgnoreCase("global")) {
                var msg = String.format("Cannot leave %s Channel.... Yet..", cc.getChannelName());
                player.sendMessage(msg);
                return;
            }
            channelsList.get(channel).removePlayer(player);
            channelsList.forEach((CHANNEL_NAME, CHANNEL_OBJECT) -> {
                if (CHANNEL_OBJECT.getPlayersInChannel().contains(player)) {
                    user.setChannelTalkingIn(CHANNEL_OBJECT);
                    var msg = String.format("You have left %s %s[%s] channel.",
                            cc.getChannelName(), cc.getChannelColor(), cc.getPrefix());
                    var msg2 = String.format("Now talking in: %s", CHANNEL_OBJECT.getChannelName());
                    player.sendMessage(msg);
                    player.sendMessage(msg2);
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
        String channelsString = Utils.FileToJSONString(new File(Utils.CHANNELS_FILE));
        JSONObject channelListObject = new JSONObject(channelsString);
        JSONArray channelArray = channelListObject.getJSONArray("channel_list");

        var channelArrayLen = channelArray.length();
        for (int i = 0; i < channelArrayLen; i++) {
            JSONObject channel = channelArray.getJSONObject(i);
            String channelName = (String) channel.get("name");
            String channelPrefix = (String) channel.get("prefix");
            String channelColor = (String) channel.get("color");

            ChatChannel chatChannel = new ChatChannel(channelName, channelPrefix.charAt(0), channelColor.charAt(0));
            channelsList.put(channelName, chatChannel);
        }

    }

}
