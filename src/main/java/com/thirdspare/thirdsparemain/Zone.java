package com.thirdspare.thirdsparemain;

import org.bukkit.Location;

import java.util.ArrayList;

public class Zone {
    Location a, b;
    ArrayList<String> playersInZone; //This is the members included in the zone
    String ownerUUID;

    public Zone(Location a, Location b, String ownerUUID) {
        this.a = a;
        this.b = b;
        this.ownerUUID = ownerUUID;

        playersInZone = new ArrayList<>();
        playersInZone.add(ownerUUID);
    }

    public boolean blockInZone(Location blockLocation){
        int lowerX, upperX, lowerY, upperY, lowerZ, upperZ;
        if(a.getBlockX() <= b.getBlockX()){
            lowerX = a.getBlockX();
            upperX = b.getBlockX();
        }else{
            lowerX = b.getBlockX();
            upperX = a.getBlockX();
        }

        if(a.getBlockY() <= b.getBlockY()){
            lowerY = a.getBlockY();
            upperY = b.getBlockY();
        }else{
            lowerY = b.getBlockY();
            upperY = a.getBlockY();
        }
        if(a.getBlockZ() <= b.getBlockZ()){
            lowerZ = a.getBlockZ();
            upperZ = b.getBlockZ();
        }else{
            lowerZ = b.getBlockZ();
            upperZ = a.getBlockZ();
        }


        return true;
    }
}
