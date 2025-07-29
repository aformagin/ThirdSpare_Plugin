package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.chat.ChatChannel;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.utilities.Utils;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
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
        
        // Enhanced User validation to prevent NullPointerExceptions in chat
        // Handles missing User objects gracefully with appropriate error messages
        User user = plugin.getOnlinePlayers().get(eventPlayer.getUniqueId());
        if (user == null) {
            plugin.getLogger().warning("TSM -- User object is null for chatting player " + eventPlayer.getName());
            eventPlayer.sendMessage(Component.text("Chat error: Please rejoin the server.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        
        ChatChannel channel = user.getChannelTalkingIn();
        if (channel == null) {
            plugin.getLogger().warning("TSM -- ChatChannel is null for player " + eventPlayer.getName());
            eventPlayer.sendMessage(Component.text("Chat error: No active channel. Please rejoin the server.").color(NamedTextColor.RED));
            event.setCancelled(true);
            return;
        }
        
        // Enhanced chat logging: Log all chat messages to console in specified format
        // Format: [Channel-Name]<Player-Name> "Message"
        String channelName = channel.getChannelName();
        String playerName = eventPlayer.getName();
        String messageText = PlainTextComponentSerializer.plainText().serialize(event.message());
        String logMessage = String.format("[%s]<%s> \"%s\"", channelName, playerName, messageText);
        plugin.getLogger().info("CHAT: " + logMessage);
        
        event.viewers().clear();
        event.renderer(this);

        String cName = channel.getChannelName();
        plugin.chatManager.getChannel(cName).stream().forEach(players -> event.viewers().add(players));

    }

    @Override
    public @NotNull Component render(@NotNull Player player, @NotNull Component sourceDisplayName, @NotNull Component message, @NotNull Audience viewer) {
        return generateMessage(player, message);
    }

    /**
     * Enhanced message formatter with null safety for User and ChatChannel objects
     * Generates the format for player chats based on the chat channel
     * @param player The player that is speaking in the chat channel
     * @param message The message that the player is sending to the chat
     * @return The chat component to be sent to chat
     */
    @NotNull
    private Component generateMessage(@NotNull Player player, @NotNull Component message) {
        User u = plugin.getOnlinePlayers().get(player.getUniqueId());
        
        // Safety check: if User is null, return a basic fallback message format
        if (u == null) {
            plugin.getLogger().warning("TSM -- User object is null in generateMessage for player " + player.getName());
            // Fixed fallback formatting using proper Component construction
            Component fallbackPrefix = Utils.applyColour("&6&l[TSM] &r- ");
            Component unknownChannel = Component.text("[?] ").color(NamedTextColor.GRAY);
            Component playerName = Component.text(player.getName());
            Component arrow = Component.text(" > ");
            return fallbackPrefix.append(unknownChannel).append(playerName).append(arrow).append(message);
        }
        
        ChatChannel channel = u.getChannelTalkingIn();
        // Safety check: if ChatChannel is null, return a basic fallback message format
        if (channel == null) {
            plugin.getLogger().warning("TSM -- ChatChannel is null in generateMessage for player " + player.getName());
            // Fixed fallback formatting using proper Component construction  
            Component fallbackPrefix = Utils.applyColour("&6&l[TSM] &r- ");
            Component unknownChannel = Component.text("[?] ").color(NamedTextColor.GRAY);
            Component playerName = Component.text(player.getName());
            Component arrow = Component.text(" > ");
            return fallbackPrefix.append(unknownChannel).append(playerName).append(arrow).append(message);
        }
        
        // Fixed chat formatting: properly construct Component instead of string interpolation
        // This prevents NamedTextColor objects from displaying as JSON in chat
        char prefix = channel.getPrefix();
        NamedTextColor channelColor = channel.getChannelColor();
        
        // Build the chat message component properly using Adventure API
        Component chatPrefix = Utils.applyColour("&6&l[TSM] &r- ");
        Component channelBracket = Component.text("[").color(channelColor);
        Component channelPrefix = Component.text(prefix).color(channelColor);
        Component closingBracket = Component.text("] ");
        Component playerName = Component.text(player.getName());
        Component arrow = Component.text(" > ");
        
        return chatPrefix
                .append(channelBracket)
                .append(channelPrefix)
                .append(closingBracket)
                .append(playerName)
                .append(arrow)
                .append(message);
    }

}
