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

            var joinChannelMsg = String.format("You have joined %s [%s] channel.", cc.getChannelName(), cc.getPrefix());
            player.sendMessage(joinChannelMsg);
        }
    }

    // TODO - Check if the below TODO is still valid and then clean up related code
    // TODO - Rework this method so it is compatible with current way of joining/leaving
    public void leaveChannel(User user, String channelName) {

        Player player = user.getPlayer();
        String channel = channelName.toUpperCase();

        if (channelsList.containsKey(channel)) {
            ChatChannel cc = channelsList.get(channel); // This is used only for ease of getting name and prefix of the channel
            if (cc.getChannelName().equalsIgnoreCase("global")) {
                var unableToLeaveMsg = String.format("Cannot leave %s Channel.... Yet..", cc.getChannelName());
                player.sendMessage(unableToLeaveMsg);
                return;
            }

            channelsList.get(channel).removePlayer(player);

            channelsList.forEach((CHANNEL_NAME, CHANNEL_OBJECT) -> {
                if (CHANNEL_OBJECT.getPlayersInChannel().contains(player)) {
                    user.setChannelTalkingIn(CHANNEL_OBJECT);

                    var leaveChannelMsg = String.format("You have left %s %s[%s] channel.",
                            cc.getChannelName(), cc.getChannelColor(), cc.getPrefix());

                    var nowTalkingInMsg = String.format("Now talking in: %s", CHANNEL_OBJECT.getChannelName());

                    player.sendMessage(leaveChannelMsg);
                    player.sendMessage(nowTalkingInMsg);
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
