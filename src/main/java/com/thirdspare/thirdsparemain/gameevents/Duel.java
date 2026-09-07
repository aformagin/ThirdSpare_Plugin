package com.thirdspare.thirdsparemain.gameevents;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;

public class Duel {

    private final User playerOne;
    private final User playerTwo;
    private final ThirdSpareMain plugin;
    private BukkitTask countDown;
    private int time;
    private boolean combatEnabled;
    private boolean finished;

    public Duel(User playerOne, User playerTwo, int time, ThirdSpareMain plugin) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.time = time;
        this.plugin = plugin;
    }

    public void startCountdown() {
        playerOne.setInDuel(true);
        playerTwo.setInDuel(true);
        playerOne.setActiveDuel(this);
        playerTwo.setActiveDuel(this);
        playerOne.setAgainst(playerTwo.getPlayer());
        playerTwo.setAgainst(playerOne.getPlayer());

        countDown = new BukkitRunnable() {
            @Override
            public void run() {
                Player one = playerOne.getPlayer();
                Player two = playerTwo.getPlayer();
                if (!one.isOnline() || !two.isOnline()) {
                    finishDueToDisconnect();
                    cancel();
                    return;
                }

                if (time <= 0) {
                    combatEnabled = true;
                    var startMessage = Component.text("Fight!")
                            .color(NamedTextColor.GREEN);
                    one.sendMessage(startMessage);
                    two.sendMessage(startMessage);
                    cancel();
                    return;
                }

                Component countdownMessage = Component.text(time + "...")
                        .color(NamedTextColor.GOLD);
                one.sendMessage(countdownMessage);
                two.sendMessage(countdownMessage);
                time--;
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }

    public boolean isCombatEnabled() {
        return combatEnabled;
    }

    public boolean isParticipant(Player player) {
        return playerOne.getPlayer().getUniqueId().equals(player.getUniqueId())
                || playerTwo.getPlayer().getUniqueId().equals(player.getUniqueId());
    }

    public Player getOpponent(Player player) {
        if (playerOne.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            return playerTwo.getPlayer();
        }
        if (playerTwo.getPlayer().getUniqueId().equals(player.getUniqueId())) {
            return playerOne.getPlayer();
        }
        return null;
    }

    public boolean isOpponent(Player attacker, Player damaged) {
        Player expectedOpponent = getOpponent(damaged);
        return expectedOpponent != null && expectedOpponent.getUniqueId().equals(attacker.getUniqueId());
    }

    public void finishWithLoser(Player loser) {
        if (finished) {
            return;
        }

        Player winner = getOpponent(loser);
        if (winner == null) {
            return;
        }

        finished = true;
        cleanup();

        loser.setHealth(Math.min(2.0D, loser.getAttribute(org.bukkit.attribute.Attribute.MAX_HEALTH).getValue()));

        winner.showTitle(Title.title(
                Component.text("Victory", NamedTextColor.GREEN),
                Component.text("You defeated " + loser.getName(), NamedTextColor.YELLOW),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));
        loser.showTitle(Title.title(
                Component.text("Defeat", NamedTextColor.RED),
                Component.text("You lost to " + winner.getName(), NamedTextColor.YELLOW),
                Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofSeconds(1))
        ));
    }

    public void finishDueToDisconnect() {
        if (finished) {
            return;
        }

        Player one = playerOne.getPlayer();
        Player two = playerTwo.getPlayer();
        Player winner = one.isOnline() ? one : two;
        Player loser = one.isOnline() ? two : one;

        finished = true;
        cleanup();

        if (winner.isOnline()) {
            winner.showTitle(Title.title(
                    Component.text("Victory", NamedTextColor.GREEN),
                    Component.text(loser.getName() + " left the duel", NamedTextColor.YELLOW),
                    Title.Times.times(Duration.ZERO, Duration.ofSeconds(3), Duration.ofSeconds(1))
            ));
        }
    }

    public void cancelBeforeStart(Component reason) {
        if (finished) {
            return;
        }

        finished = true;
        cleanup();
        playerOne.getPlayer().sendMessage(reason);
        playerTwo.getPlayer().sendMessage(reason);
    }

    private void cleanup() {
        if (countDown != null) {
            countDown.cancel();
            countDown = null;
        }

        resetUserState(playerOne);
        resetUserState(playerTwo);
    }

    private void resetUserState(User user) {
        user.setInDuel(false);
        user.setActiveDuel(null);
        user.setAgainst(null);
    }

    @Override
    public String toString() {
        return "Duel{" +
                "playerOne=" + playerOne.getPlayer().getName() +
                ", playerTwo=" + playerTwo.getPlayer().getName() +
                ", combatEnabled=" + combatEnabled +
                ", finished=" + finished +
                '}';
    }
}
