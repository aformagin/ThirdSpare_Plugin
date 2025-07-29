package com.thirdspare.thirdsparemain.utilities;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationTypeAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext context) {
        if (location == null) {
            return JsonNull.INSTANCE;
        }
        
        JsonObject obj = new JsonObject();
        obj.addProperty("world", location.getWorld() != null ? location.getWorld().getName() : null);
        obj.addProperty("x", location.getX());
        obj.addProperty("y", location.getY());
        obj.addProperty("z", location.getZ());
        obj.addProperty("pitch", location.getPitch());
        obj.addProperty("yaw", location.getYaw());
        return obj;
    }

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) 
            throws JsonParseException {
        if (jsonElement.isJsonNull()) {
            return null;
        }
        
        JsonObject obj = jsonElement.getAsJsonObject();
        String worldName = obj.get("world").getAsString();
        World world = Bukkit.getWorld(worldName);
        
        if (world == null) {
            throw new JsonParseException("World '" + worldName + "' not found");
        }
        
        double x = obj.get("x").getAsDouble();
        double y = obj.get("y").getAsDouble();
        double z = obj.get("z").getAsDouble();
        float pitch = obj.get("pitch").getAsFloat();
        float yaw = obj.get("yaw").getAsFloat();
        
        return new Location(world, x, y, z, yaw, pitch);
    }
}