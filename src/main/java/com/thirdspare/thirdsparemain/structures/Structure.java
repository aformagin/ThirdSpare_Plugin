package com.thirdspare.thirdsparemain.structures;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Structure {
    Location a, b;
    private Location structureLoc;
    private ArrayList<BlockLayer> layers;

    public Structure() {
        layers = new ArrayList<>(); //Init the layers list
    }

    public Location getStructureLoc() {
        return structureLoc;
    }

    public void setStructureLoc(Location structureLoc) {
        this.structureLoc = structureLoc;
    }

    public ArrayList<BlockLayer> getLayers() {
        return layers;
    }

    public void setLayers(ArrayList<BlockLayer> layers) {
        this.layers = layers;
    }

    public void generateOnPlayer(Player player) {

        //TODO Allow player to put in left offset
        var facing = player.getFacing().getDirection();
        var bFacing = player.getFacing();
        //Gets the vector of the direction they are facing
        var playerLocForward = player.getLocation().add(facing);

        /*
         * Facing south +X is left, -X right , +Z forward, -Z backwards
         * Facing west -Z moves left, +Z moves right, -X is forward, +X is backwards (FACING -X)
         * Facing north -X is left, +X right, -Z forward, +Z backwards (FACING -Z)
         * Facing east +Z moves left, -Z moves right, +X is forward, -X is backwards
         */

        Location startingLocation;
        Location currentLocation;

        int rightX, rightZ, leftX, leftZ;
        switch (bFacing) {
            case NORTH:
                rightX = 1;
                rightZ = 0;
                leftX = -rightX;
                leftZ = rightZ;
                break;
            case SOUTH:
                rightX = -1;
                rightZ = 0;
                leftX = -rightX;
                leftZ = rightZ;
                break;
            case EAST:
                rightX = 0;
                rightZ = 1;
                leftX = rightX;
                leftZ = -rightZ;
                break;
            default: //Default direction is assumed to be WEST
                rightX = 0;
                rightZ = -1;
                leftX = rightX;
                leftZ = -rightZ;
                break;
        }

        var layersList = getLayers();
        int layerY = 0;
        a = playerLocForward.getBlock().getRelative(BlockFace.DOWN).getRelative(leftX, layerY, leftZ).getLocation();
        // For each layer in the list, build the layer in front of the player
        for (BlockLayer l : layersList) {
            startingLocation = playerLocForward.getBlock().getRelative(BlockFace.DOWN).getRelative(leftX, layerY, leftZ).getLocation();
            currentLocation = startingLocation.clone();
            //Horizontal layering(hl) for loop
            int rowNumber = 1;
            for (int i = 0; i < l.getLayer().length; i++) {
                //Checking to see if letter is in map
                //Place block of matching letter
                currentLocation.getBlock().setType(l.getMaterialMap().getOrDefault(l.getLayer()[i], Material.AIR));
                /*if (l.getMaterialMap().containsKey(l.getLayer()[i]))
                    //Place block of matching letter
                    currentLocation.getBlock().setType(l.getMaterialMap().get(l.getLayer()[i]));
                else currentLocation.getBlock().setType(Material.AIR);*/
                //Checks to see if it is at the end of the row
                if (i == ((l.getMaxDepth() * rowNumber) - 1)) {
                    rowNumber = ++rowNumber;
                    //Move the location relative right
                    startingLocation = startingLocation.getBlock().getRelative(rightX, 0, rightZ).getLocation();
                    currentLocation = startingLocation.clone(); //Set current location to the starting location
                } else {
                    //Move current location forward
                    currentLocation = currentLocation.getBlock().getRelative(bFacing).getLocation();
                }
            }// End of hl for loop
            layerY = ++layerY;
            b = currentLocation;
        }
    }
}
