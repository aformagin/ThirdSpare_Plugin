package com.thirdspare.thirdsparemain.entities.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class PlayerData {
    private String name;
    private LocalDateTime lastJoined;
    private String lastIpAddress;
    private double balance;
    private int bpSize;
    private Map<String, Object> home;
    private List<String> backpack;

    public PlayerData() {
    }

    public PlayerData(String name, LocalDateTime lastJoined, String lastIpAddress, 
                     double balance, int bpSize, Map<String, Object> home, List<String> backpack) {
        this.name = name;
        this.lastJoined = lastJoined;
        this.lastIpAddress = lastIpAddress;
        this.balance = balance;
        this.bpSize = bpSize;
        this.home = home;
        this.backpack = backpack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getLastJoined() {
        return lastJoined;
    }

    public void setLastJoined(LocalDateTime lastJoined) {
        this.lastJoined = lastJoined;
    }

    public String getLastIpAddress() {
        return lastIpAddress;
    }

    public void setLastIpAddress(String lastIpAddress) {
        this.lastIpAddress = lastIpAddress;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public int getBpSize() {
        return bpSize;
    }

    public void setBpSize(int bpSize) {
        this.bpSize = bpSize;
    }

    public Map<String, Object> getHome() {
        return home;
    }

    public void setHome(Map<String, Object> home) {
        this.home = home;
    }

    public List<String> getBackpack() {
        return backpack;
    }

    public void setBackpack(List<String> backpack) {
        this.backpack = backpack;
    }
}