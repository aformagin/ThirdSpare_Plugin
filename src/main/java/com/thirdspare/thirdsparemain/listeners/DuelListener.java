package com.thirdspare.thirdsparemain.listeners;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import com.thirdspare.thirdsparemain.gameevents.Duel;
import com.thirdspare.thirdsparemain.gameevents.DuelRequest;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.projectiles.ProjectileSource;

public class DuelListener implements Listener {
    private final ThirdSpareMain plugin;

    public DuelListener(ThirdSpareMain plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player damagedPlayer) || event.isCancelled()) {
            return;
        }

        User damagedUser = plugin.getOnlinePlayers().get(damagedPlayer.getUniqueId());
        if (damagedUser == null || !damagedUser.isInDuel()) {
            return;
        }

        Duel duel = damagedUser.getActiveDuel();
        if (duel == null) {
            return;
        }

        Player attacker = null;
        if (event instanceof EntityDamageByEntityEvent byEntityEvent) {
            attacker = resolveAttacker(byEntityEvent.getDamager());
        }

        if (attacker == null || !duel.isOpponent(attacker, damagedPlayer)) {
            event.setCancelled(true);
            return;
        }

        if (!duel.isCombatEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (damagedPlayer.getHealth() - event.getFinalDamage() <= 2.0D) {
            event.setCancelled(true);
            duel.finishWithLoser(damagedPlayer);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = plugin.getOnlinePlayers().get(player.getUniqueId());
        if (user == null) {
            return;
        }

        Duel duel = user.getActiveDuel();
        if (duel != null) {
            duel.finishDueToDisconnect();
        }

        clearPendingRequest(user.getIncomingDuelRequest(), true);
        clearPendingRequest(user.getOutgoingDuelRequest(), false);
    }

    private void clearPendingRequest(DuelRequest request, boolean playerWasTarget) {
        if (request == null) {
            return;
        }

        request.cancelExpirationTask();

        User requesterUser = plugin.getOnlinePlayers().get(request.getRequester().getUniqueId());
        User targetUser = plugin.getOnlinePlayers().get(request.getTarget().getUniqueId());

        if (requesterUser != null && requesterUser.getOutgoingDuelRequest() == request) {
            requesterUser.clearOutgoingDuelRequest();
        }
        if (targetUser != null && targetUser.getIncomingDuelRequest() == request) {
            targetUser.clearIncomingDuelRequest();
        }

        Player otherPlayer = playerWasTarget ? request.getRequester() : request.getTarget();
        if (otherPlayer.isOnline()) {
            otherPlayer.sendMessage(Component.text("The pending duel request was cleared because the other player left.")
                    .color(NamedTextColor.YELLOW));
        }
    }

    private Player resolveAttacker(Entity damager) {
        if (damager instanceof Player player) {
            return player;
        }

        if (damager instanceof Projectile projectile) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player player) {
                return player;
            }
        }

        return null;
    }
}
