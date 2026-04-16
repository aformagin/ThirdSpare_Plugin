package com.thirdspare.thirdsparemain.entities.data;

import java.util.List;

public class WorldData {
    private List<Double> spawnLocation;

    public WorldData() {
    }

    public WorldData(List<Double> spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public List<Double> getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(List<Double> spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}