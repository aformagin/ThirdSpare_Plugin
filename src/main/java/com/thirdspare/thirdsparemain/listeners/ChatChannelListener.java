package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.chat.ChatChannel;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.utilities.Utils;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;


//TODO Split up ChatComposer from the listener
public class ChatChannelListener implements Listener, ChatRenderer {
    ThirdSpareMain plugin;

    public ChatChannelListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player eventPlayer = event.getPlayer();
        //Get the user via UUID
        User user = plugin.getOnlinePlayers().get(eventPlayer.getUniqueId());
        event.viewers().clear();
        event.renderer(this);

        ChatChannel channel = user.getChannelTalkingIn();
        String cName = channel.getChannelName();

        plugin.chatManager.getChannel(cName).stream().forEach(players -> event.viewers().add(players));

    }


    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return generateMessage(player, message);
    }

    /**
     * Generates the format for player chats based on the chat channel
     * @param player The player that is speaking in the chat channel
     * @param message The message that the player is sending to the chat
     * @return The chat component to be sent to chat
     */
    @NotNull
    private Component generateMessage(@NotNull Player player, @NotNull Component message) {
        User u = plugin.getOnlinePlayers().get(player.getUniqueId());
        char prefix = u.getChannelTalkingIn().getPrefix();
        ChatColor c = u.getChannelTalkingIn().getChannelColor();
        var msg = String.format("&6&l[TSM] &r- %s[%s] &r%s", c,
                prefix, player.getName());
        return Component.text( Utils.applyColour(msg) + " > ").append(message);
    }

}
