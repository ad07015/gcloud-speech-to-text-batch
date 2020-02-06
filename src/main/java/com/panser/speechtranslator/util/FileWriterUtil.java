package com.panser.speechtranslator.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;

public class FileWriterUtil {
    public static void writeToFile(Map<String, String> map, String targetFilePath) {
        try {
            int mapSize = map.size();
            BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(targetFilePath));
            int i = 1;
            for (Map.Entry<String, String> entry: map.entrySet()) {
                writeEntryToFile(writer, entry);
                System.out.println("Writing to file \"" + targetFilePath + "\": " + i + "/" + mapSize);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeEntryToFile(BufferedWriter writer, Map.Entry<String, String> entry) {
        try {
            writer.write(entry.getKey() + "=" + entry.getValue());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
