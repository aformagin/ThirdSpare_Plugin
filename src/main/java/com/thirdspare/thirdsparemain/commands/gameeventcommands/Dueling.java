package com.thirdspare.thirdsparemain.commands.gameeventcommands;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.gameevents.Duel;
import com.thirdspare.thirdsparemain.gameevents.DuelRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class Dueling implements CommandExecutor {
    private static final long REQUEST_TIMEOUT_MS = 3L * 60L * 1000L;
    private static final long REQUEST_TIMEOUT_TICKS = 3L * 60L * 20L;
    private static final double MAX_REQUEST_DISTANCE_SQUARED = 50D * 50D;

    private final ThirdSpareMain plugin;

    public Dueling(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s,
                             @NotNull String[] args) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }
        if (!command.getName().equalsIgnoreCase("duel")) {
            return false;
        }
        if (!player.hasPermission("tsm.duel")) {
            player.sendMessage(Component.text("You do not have permission to use this command.")
                    .color(NamedTextColor.RED));
            return true;
        }

        HashMap<java.util.UUID, User> onlinePlayers = plugin.getOnlinePlayers();
        User playerUser = onlinePlayers.get(player.getUniqueId());
        if (playerUser == null) {
            player.sendMessage(Component.text("Your player data is not loaded yet.")
                    .color(NamedTextColor.RED));
            return true;
        }

        pruneExpiredRequest(playerUser.getIncomingDuelRequest(), true);
        pruneExpiredRequest(playerUser.getOutgoingDuelRequest(), true);

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("accept")) {
                return acceptRequest(player, playerUser);
            }
            if (args[0].equalsIgnoreCase("deny")) {
                return denyRequest(player, playerUser);
            }
            return sendRequest(player, playerUser, args[0]);
        }

        return false;
    }

    private boolean sendRequest(Player requester, User requesterUser, String targetName) {
        Player target = plugin.getServer().getPlayerExact(targetName);
        if (target == null || !target.isOnline()) {
            requester.sendMessage(Component.text("That player is not online.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (requester.getUniqueId().equals(target.getUniqueId())) {
            requester.sendMessage(Component.text("You cannot duel yourself.")
                    .color(NamedTextColor.RED));
            return true;
        }

        User targetUser = plugin.getOnlinePlayers().get(target.getUniqueId());
        if (targetUser == null) {
            requester.sendMessage(Component.text("That player's data is not loaded.")
                    .color(NamedTextColor.RED));
            return true;
        }

        pruneExpiredRequest(targetUser.getIncomingDuelRequest(), true);
        pruneExpiredRequest(targetUser.getOutgoingDuelRequest(), true);

        if (requesterUser.isInDuel()) {
            requester.sendMessage(Component.text("You are already in a duel.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (targetUser.isInDuel()) {
            requester.sendMessage(Component.text(target.getName() + " is already in a duel.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (requesterUser.hasPendingDuelRequest()) {
            requester.sendMessage(Component.text("You already have a pending duel request.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (targetUser.hasPendingDuelRequest()) {
            requester.sendMessage(Component.text(target.getName() + " already has a pending duel request.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (!requester.getWorld().equals(target.getWorld())
                || requester.getLocation().distanceSquared(target.getLocation()) > MAX_REQUEST_DISTANCE_SQUARED) {
            requester.sendMessage(Component.text(target.getName() + " is not in range for a duel request.")
                    .color(NamedTextColor.RED));
            return true;
        }

        DuelRequest request = new DuelRequest(requester, target, System.currentTimeMillis());
        requesterUser.setOutgoingDuelRequest(request);
        targetUser.setIncomingDuelRequest(request);

        request.setExpirationTask(new BukkitRunnable() {
            @Override
            public void run() {
                if (requesterUser.getOutgoingDuelRequest() != request || targetUser.getIncomingDuelRequest() != request) {
                    return;
                }

                clearRequest(request);
                requester.sendMessage(Component.text("Your duel request to " + target.getName() + " expired.")
                        .color(NamedTextColor.YELLOW));
                if (target.isOnline()) {
                    target.sendMessage(Component.text("The duel request from " + requester.getName() + " expired.")
                            .color(NamedTextColor.YELLOW));
                }
            }
        }.runTaskLater(plugin, REQUEST_TIMEOUT_TICKS));

        requester.sendMessage(Component.text("Duel request sent to " + target.getName() + ".")
                .color(NamedTextColor.GREEN));
        target.sendMessage(Component.text(requester.getName() + " has challenged you to a duel.")
                .color(NamedTextColor.GOLD));
        target.sendMessage(Component.text("Use /duel accept or /duel deny within 3 minutes.")
                .color(NamedTextColor.YELLOW));
        return true;
    }

    private boolean acceptRequest(Player target, User targetUser) {
        DuelRequest request = targetUser.getIncomingDuelRequest();
        if (request == null) {
            target.sendMessage(Component.text("You do not have a pending duel request.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (pruneExpiredRequest(request, false)) {
            target.sendMessage(Component.text("That duel request has expired.")
                    .color(NamedTextColor.RED));
            return true;
        }

        Player requester = request.getRequester();
        if (!requester.isOnline()) {
            clearRequest(request);
            target.sendMessage(Component.text("The requesting player is no longer online.")
                    .color(NamedTextColor.RED));
            return true;
        }

        User requesterUser = plugin.getOnlinePlayers().get(requester.getUniqueId());
        if (requesterUser == null) {
            clearRequest(request);
            target.sendMessage(Component.text("The requesting player's data is not loaded.")
                    .color(NamedTextColor.RED));
            return true;
        }

        if (requesterUser.isInDuel() || targetUser.isInDuel()) {
            clearRequest(request);
            target.sendMessage(Component.text("This duel request is no longer valid.")
                    .color(NamedTextColor.RED));
            return true;
        }
        if (!requester.getWorld().equals(target.getWorld())
                || requester.getLocation().distanceSquared(target.getLocation()) > MAX_REQUEST_DISTANCE_SQUARED) {
            clearRequest(request);
            target.sendMessage(Component.text(requester.getName() + " is no longer in range.")
                    .color(NamedTextColor.RED));
            requester.sendMessage(Component.text(target.getName() + " could not accept because you are out of range.")
                    .color(NamedTextColor.RED));
            return true;
        }

        clearRequest(request);

        Duel duel = new Duel(requesterUser, targetUser, 10, plugin);
        duel.startCountdown();

        requester.sendMessage(Component.text(target.getName() + " accepted your duel request.")
                .color(NamedTextColor.GREEN));
        target.sendMessage(Component.text("You accepted " + requester.getName() + "'s duel request.")
                .color(NamedTextColor.GREEN));
        return true;
    }

    private boolean denyRequest(Player target, User targetUser) {
        DuelRequest request = targetUser.getIncomingDuelRequest();
        if (request == null) {
            target.sendMessage(Component.text("You do not have a pending duel request.")
                    .color(NamedTextColor.RED));
            return true;
        }

        Player requester = request.getRequester();
        clearRequest(request);

        target.sendMessage(Component.text("You denied the duel request.")
                .color(NamedTextColor.YELLOW));
        if (requester.isOnline()) {
            requester.sendMessage(Component.text(target.getName() + " denied your duel request.")
                    .color(NamedTextColor.RED));
        }
        return true;
    }

    private boolean pruneExpiredRequest(DuelRequest request, boolean notifyPlayers) {
        if (request == null || !request.isExpired(REQUEST_TIMEOUT_MS)) {
            return false;
        }

        Player requester = request.getRequester();
        Player target = request.getTarget();
        clearRequest(request);

        if (notifyPlayers) {
            if (requester.isOnline()) {
                requester.sendMessage(Component.text("Your duel request to " + target.getName() + " expired.")
                        .color(NamedTextColor.YELLOW));
            }
            if (target.isOnline()) {
                target.sendMessage(Component.text("The duel request from " + requester.getName() + " expired.")
                        .color(NamedTextColor.YELLOW));
            }
        }
        return true;
    }

    private void clearRequest(DuelRequest request) {
        request.cancelExpirationTask();

        User requesterUser = plugin.getOnlinePlayers().get(request.getRequester().getUniqueId());
        if (requesterUser != null && requesterUser.getOutgoingDuelRequest() == request) {
            requesterUser.clearOutgoingDuelRequest();
        }

        User targetUser = plugin.getOnlinePlayers().get(request.getTarget().getUniqueId());
        if (targetUser != null && targetUser.getIncomingDuelRequest() == request) {
            targetUser.clearIncomingDuelRequest();
        }
    }
}
