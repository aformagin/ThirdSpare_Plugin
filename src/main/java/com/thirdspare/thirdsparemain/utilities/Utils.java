package com.thirdspare.thirdsparemain.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.time.LocalDateTime;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.EOFException;
import java.lang.reflect.Type;
import java.util.Scanner;

public class Utils {
    /* Public "Global" variables to make life a little easier */

    //Location of files
    public static final String PLAYERS_FILE = "plugins" + File.separator + "TSM"+ File.separator + "data"+ File.separator + "players.json";
    public static final String CONFIG_FILE = "plugins" + File.separator + "TSM"+ File.separator + "configs"+ File.separator + "config.json";
    public static final String CHANNELS_FILE = "plugins" + File.separator + "TSM" + File.separator + "data"+ File.separator + "channels.json";

    //Link to resource pack
    public static final String RPACK_LINK = "https://drive.google.com/uc?export=download&id=1COUZQuKN0g_rmiZQjJ2DzX8B_RbIXbPl"; //TODO Update resource pack
    public static final String RPACK_HASH = "70579456E30093F14DD5589A89DE4BF3AF047A9A";

    // Gson instance with custom type adapters
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Location.class, new LocationTypeAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
            .create();

    /**
     * Get the configured Gson instance
     * @return Gson instance with custom type adapters
     */
    public static Gson getGson() {
        return gson;
    }

    /**
     * Writes an object to a JSON file using Gson
     * @param object The object to serialize
     * @param fileToWrite The file to write to
     * @throws IOException if writing fails
     */
    public static void writeObjectToFile(Object object, File fileToWrite) throws IOException {
        try (FileWriter fw = new FileWriter(fileToWrite)) {
            gson.toJson(object, fw);
        }
    }

    /**
     * Enhanced JSON file reader with validation and error recovery
     * Handles empty files, corrupted JSON, and provides default fallbacks
     * @param fileToRead The file to read from
     * @param clazz The class type to deserialize to
     * @param <T> The type parameter
     * @return The deserialized object or null if file is empty/corrupted
     * @throws IOException if reading fails critically
     */
    public static <T> T readObjectFromFile(File fileToRead, Class<T> clazz) throws IOException {
        // Validate file exists and is readable
        if (!fileToRead.exists()) {
            throw new FileNotFoundException("File does not exist: " + fileToRead.getAbsolutePath());
        }
        
        if (!fileToRead.canRead()) {
            throw new IOException("Cannot read file: " + fileToRead.getAbsolutePath());
        }
        
        // Check if file is empty or too small to contain valid JSON
        if (fileToRead.length() == 0) {
            Bukkit.getLogger().warning("TSM -- Empty JSON file detected: " + fileToRead.getAbsolutePath());
            return null; // Return null for empty files instead of throwing exception
        }
        
        if (fileToRead.length() < 2) {
            Bukkit.getLogger().warning("TSM -- JSON file too small to be valid: " + fileToRead.getAbsolutePath());
            return null; // Return null for files that can't contain valid JSON
        }
        
        try (FileReader fr = new FileReader(fileToRead)) {
            T result = gson.fromJson(fr, clazz);
            
            // Additional validation: check if gson returned null due to malformed JSON
            if (result == null) {
                Bukkit.getLogger().warning("TSM -- JSON deserialization returned null for file: " + fileToRead.getAbsolutePath());
            }
            
            return result;
        } catch (JsonSyntaxException e) {
            // Handle malformed JSON gracefully
            if (e.getCause() instanceof EOFException) {
                Bukkit.getLogger().severe("TSM -- JSON EOF Exception - file may be empty or corrupted: " + fileToRead.getAbsolutePath());
                Bukkit.getLogger().severe("TSM -- File size: " + fileToRead.length() + " bytes");
                return null; // Return null instead of crashing
            } else {
                Bukkit.getLogger().severe("TSM -- Malformed JSON in file: " + fileToRead.getAbsolutePath());
                Bukkit.getLogger().severe("TSM -- JSON Error: " + e.getMessage());
                throw new IOException("Malformed JSON file: " + fileToRead.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Enhanced JSON file reader with validation and error recovery (Type-based)
     * Handles empty files, corrupted JSON, and provides default fallbacks
     * @param fileToRead The file to read from
     * @param type The Type to deserialize to
     * @param <T> The type parameter
     * @return The deserialized object or null if file is empty/corrupted
     * @throws IOException if reading fails critically
     */
    public static <T> T readObjectFromFile(File fileToRead, Type type) throws IOException {
        // Validate file exists and is readable
        if (!fileToRead.exists()) {
            throw new FileNotFoundException("File does not exist: " + fileToRead.getAbsolutePath());
        }
        
        if (!fileToRead.canRead()) {
            throw new IOException("Cannot read file: " + fileToRead.getAbsolutePath());
        }
        
        // Check if file is empty or too small to contain valid JSON
        if (fileToRead.length() == 0) {
            Bukkit.getLogger().warning("TSM -- Empty JSON file detected: " + fileToRead.getAbsolutePath());
            return null; // Return null for empty files instead of throwing exception
        }
        
        if (fileToRead.length() < 2) {
            Bukkit.getLogger().warning("TSM -- JSON file too small to be valid: " + fileToRead.getAbsolutePath());
            return null; // Return null for files that can't contain valid JSON
        }
        
        try (FileReader fr = new FileReader(fileToRead)) {
            T result = gson.fromJson(fr, type);
            
            // Additional validation: check if gson returned null due to malformed JSON
            if (result == null) {
                Bukkit.getLogger().warning("TSM -- JSON deserialization returned null for file: " + fileToRead.getAbsolutePath());
            }
            
            return result;
        } catch (JsonSyntaxException e) {
            // Handle malformed JSON gracefully
            if (e.getCause() instanceof EOFException) {
                Bukkit.getLogger().severe("TSM -- JSON EOF Exception - file may be empty or corrupted: " + fileToRead.getAbsolutePath());
                Bukkit.getLogger().severe("TSM -- File size: " + fileToRead.length() + " bytes");
                return null; // Return null instead of crashing
            } else {
                Bukkit.getLogger().severe("TSM -- Malformed JSON in file: " + fileToRead.getAbsolutePath());
                Bukkit.getLogger().severe("TSM -- JSON Error: " + e.getMessage());
                throw new IOException("Malformed JSON file: " + fileToRead.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Legacy method for backward compatibility - converts JSON string to file
     * @deprecated Use writeObjectToFile instead
     */
    @Deprecated
    public static void JsonToFile(String json, File fileToWrite) throws IOException {
        try (FileWriter fw = new FileWriter(fileToWrite)) {
            fw.write(json);
        }
    }

    /**
     * Legacy method for backward compatibility - reads file to JSON string
     * @deprecated Use readObjectFromFile instead
     */
    @Deprecated
    public static String FileToJSONString(File fileToRead){
        StringBuilder jsonResponse = new StringBuilder();
        try {
            Scanner scanner = new Scanner(fileToRead);
            while (scanner.hasNextLine()){
                jsonResponse.append(scanner.nextLine().trim());
            }
            return jsonResponse.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return jsonResponse.toString();
    }

    /**
     * Applies chat colours based on the alternate colour codes using Adventure API
     * @param input The String that needs to be "colourised"
     * @return The Component with applied colors
     */
    public static Component applyColour(String input){
        return LegacyComponentSerializer.legacyAmpersand().deserialize(input);
    }

    /**
     * Legacy method for backward compatibility - applies chat colors as string
     * @param input The String that needs to be "colourised"
     * @return The "colourised" string
     * @deprecated Use applyColour that returns Component instead
     */
    @Deprecated
    public static String applyColourLegacy(String input){
        return LegacyComponentSerializer.legacyAmpersand().serialize(
            LegacyComponentSerializer.legacyAmpersand().deserialize(input)
        );
    }
}
