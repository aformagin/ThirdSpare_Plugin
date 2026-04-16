package com.thirdspare.thirdsparemain.entities.data;

import java.util.Map;

public class ConfigData {
    private Map<String, WorldData> worlds;

    public ConfigData() {
    }

    public ConfigData(Map<String, WorldData> worlds) {
        this.worlds = worlds;
    }

    public Map<String, WorldData> getWorlds() {
        return worlds;
    }

    public void setWorlds(Map<String, WorldData> worlds) {
        this.worlds = worlds;
    }
}