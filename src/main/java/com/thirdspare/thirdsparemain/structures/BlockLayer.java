package com.thirdspare.thirdsparemain.structures;

import org.bukkit.Material;

import java.util.HashMap;

public class BlockLayer {
    char[] layer;
    HashMap<Character, Material> materialMap;
    int maxDepth;

    public BlockLayer(char[] layer, HashMap<Character, Material> materialMap, int maxDepth) {
        this.layer = layer;
        this.materialMap = materialMap;
        this.maxDepth = maxDepth;
    }

    public char[] getLayer() {
        return layer;
    }

    public void setLayer(char[] layer) {
        this.layer = layer;
    }

    public HashMap<Character, Material> getMaterialMap() {
        return materialMap;
    }

    public void setMaterialMap(HashMap<Character, Material> materialMap) {
        this.materialMap = materialMap;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}
