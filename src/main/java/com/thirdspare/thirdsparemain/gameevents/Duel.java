package com.thirdspare.thirdsparemain.gameevents;

import com.thirdspare.thirdsparemain.ThirdSpareMain;
import com.thirdspare.thirdsparemain.entities.User;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Duel {

    protected User playerOne, playerTwo;
    protected BukkitTask countDown;
    protected int time;
    protected final ThirdSpareMain plugin;

    public Duel(User playerOne, User playerTwo, int time, ThirdSpareMain plugin) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.time = time;
        this.plugin = plugin;
    }

    //Starts the duel count down
    public final void startCountdown() {
        //Once the countdown starts, set both users into a duel state
        playerOne.setInDuel(true);
        playerTwo.setInDuel(true);
        countDown = new BukkitRunnable() {
            @Override
            public void run() {
                String msg = String.format("%d remaining before duel starts...", time);
                if (time-- <= 0) cancel();
                playerOne.getPlayer().sendMessage(msg);
                playerTwo.getPlayer().sendMessage(msg);
            }
        }.runTaskTimer(plugin, 0L, 20L);
    }
}
