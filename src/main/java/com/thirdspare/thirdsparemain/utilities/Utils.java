package com.thirdspare.thirdsparemain.utilities;

import org.bukkit.ChatColor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    /* My Rewritten JSON writter to work with Java 8 */
    public static void JsonToFile(String json, File fileToWrite) throws IOException {
        FileWriter fw;
        try {
            fw = new FileWriter(fileToWrite);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
     * Applies chat colours based on the alternate colour codes
     * @param input The String that needs to be "colourised"
     * @return The "colourised" string
     */
    public static String applyColour(String input){
        return ChatColor.translateAlternateColorCodes('&', input);
    }
}
