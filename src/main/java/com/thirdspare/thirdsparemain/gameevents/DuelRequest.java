package com.thirdspare.thirdsparemain.gameevents;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DuelRequest {
    private final Player requester;
    private final Player target;
    private final long createdAtMillis;
    private BukkitTask expirationTask;

    public DuelRequest(Player requester, Player target, long createdAtMillis) {
        this.requester = requester;
        this.target = target;
        this.createdAtMillis = createdAtMillis;
    }

    public Player getRequester() {
        return requester;
    }

    public Player getTarget() {
        return target;
    }

    public long getCreatedAtMillis() {
        return createdAtMillis;
    }

    public boolean isExpired(long timeoutMillis) {
        return System.currentTimeMillis() - createdAtMillis >= timeoutMillis;
    }

    public BukkitTask getExpirationTask() {
        return expirationTask;
    }

    public void setExpirationTask(BukkitTask expirationTask) {
        this.expirationTask = expirationTask;
    }

    public void cancelExpirationTask() {
        if (expirationTask != null) {
            expirationTask.cancel();
            expirationTask = null;
        }
    }

    @Override
    public String toString() {
        return "DuelRequest{" +
                "requester=" + requester.getName() +
                ", target=" + target.getName() +
                ", createdAtMillis=" + createdAtMillis +
                '}';
    }
}
