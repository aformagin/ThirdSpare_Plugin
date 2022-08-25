package com.thirdspare.thirdsparemain.structures;

import org.bukkit.Material;

import java.util.HashMap;

public class WarpStone extends Structure {
    //TODO add a block break event listener to ensure that no one can break the warp stone
    public WarpStone() {
        //Each character array represents the layer in characters
        char[] l1 = {'c', 'c', 'c',
                'c', 'g', 'c',
                'c', 'c', 'c'};
        char[] l2 = {'-', '-', '-',
                '-', 'l', '-',
                '-', '-', '-'};

        char[] l3 = {'-', '-', '-',
                '-', 's', '-',
                '-', '-', '-'};

        char[] l4 = {'-', '-', '-',
                '-', 'l', '-',
                '-', '-', '-'};

        HashMap<Character, Material> map = new HashMap<>();
        map.put('c', Material.COBBLESTONE);
        map.put('g', Material.GLOWSTONE);
        map.put('l', Material.LAPIS_BLOCK);
        map.put('s', Material.SEA_LANTERN);

        //Block layer init
        BlockLayer blockLayer = new BlockLayer(l1, map, 3);
        BlockLayer blockLayer1 = new BlockLayer(l2, map, 3);
        BlockLayer blockLayer2 = new BlockLayer(l3, map, 3);
        BlockLayer blockLayer3 = new BlockLayer(l4, map, 3);

        //Adding block layers to the structure layer list
        getLayers().add(blockLayer);
        getLayers().add(blockLayer1);
        getLayers().add(blockLayer2);
        getLayers().add(blockLayer3);
    }
}
