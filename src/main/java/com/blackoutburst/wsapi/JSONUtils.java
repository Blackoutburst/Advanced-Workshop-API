package com.blackoutburst.wsapi;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static String generatePlayerListJSON() {
        StringBuilder players = new StringBuilder();

        File folder = new File("./plugins/Workshop/playerData");
        File[] files = folder.listFiles();
        if (files == null) return "{}";

        for (int i = 0; i < files.length; i++) {
            if (!files[i].isFile()) continue;

            String[] fileName = files[i].getName().split("\\.");

            if (fileName.length == 1) continue;
            if (!fileName[1].equals("yml")) continue;

            String uuid = fileName[0];
            YamlConfiguration playerData = YamlConfiguration.loadConfiguration(files[i].getAbsoluteFile());
            String name = playerData.getString("name");

            players.append("{")
                    .append("\"uuid\": \"").append(uuid).append("\",")
                    .append("\"name\": \"").append(name).append("\"")
                    .append("}");
            if (i != files.length -1) {
                players.append(",");
            }
        }

        return "{" +
                    "\"players\": [" +
                        players +
                    "]" +
                "}";
    }

    public static String generatePlayerJSON(File file) {
        final YamlConfiguration playerData = YamlConfiguration.loadConfiguration(file);
        String name = playerData.getString("name", "unknown");
        int gameCount = playerData.getInt("gameCount", 0);
        int roundCount = playerData.getInt("roundCount", 0);
        List<String> maps = new ArrayList<>(playerData.getKeys(false));

        int mapSize = maps.size();
        for (int i = 0; i < mapSize; i++) {
            String map = maps.get(i);
            if (map.equals("name") || map.equals("gameCount") || map.equals("roundCount")) {
                maps.remove(map);
                mapSize--;
                i--;
            }
        }

        List<String> mapGameCount = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".gameCount", 0);
            mapGameCount.add("\"gameCount\": " + value);
        }

        List<String> mapRoundCount = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".roundCount", 0);
            mapRoundCount.add("\"roundCount\": " + value);
        }

        List<String> mapTime = new ArrayList<>();
        for (String map : maps) {
            double value = playerData.getDouble(map + ".time", 0);
            mapTime.add("\"time\": " + value);
        }

        StringBuilder mapData = new StringBuilder();

        for (int i = 0; i < maps.size(); i++) {
            String mgc = mapGameCount.get(i);
            String mrc = mapRoundCount.get(i);
            String mt = mapTime.get(i);
            String mn = maps.get(i);

            StringBuilder craftData = new StringBuilder();

            List<String> crafts = new ArrayList<>(playerData.getConfigurationSection(mn+".crafts").getKeys(false));

            craftData.append("\"crafts\": [");

            for (int j = 0; j < crafts.size(); j++) {
                String craft = crafts.get(j);
                double time = playerData.getDouble(mn+".crafts."+craft);
                craftData.append("{")
                        .append("\"name\": \"").append(craft).append("\",")
                        .append("\"time\": ").append(time)
                        .append("}");

                if (j != crafts.size() -1) {
                    craftData.append(",");
                }
            }
            craftData.append("]");

            mapData.append("{")
                    .append("\"name\": \"").append(mn).append("\",")
                    .append(mgc).append(",")
                    .append(mrc).append(",")
                    .append(mt).append(",")
                    .append(craftData)
                    .append("}");

            if (i != maps.size() -1) {
                mapData.append(",");
            }
        }

        return "{" +
                    "\"name\": \"" + name + "\"" + "," +
                    "\"gameCount\":" + gameCount + "," +
                    "\"roundCount\":" + roundCount + "," +
                    "\"maps\": [" +
                        mapData +
                    "]" +
                "}";
    }
}
