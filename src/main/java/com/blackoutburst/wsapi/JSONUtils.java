package com.blackoutburst.wsapi;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static String generateLeaderboardsList() {
        StringBuilder leaderboards = new StringBuilder();

        File folder = new File("./plugins/Workshop/");
        File[] files = folder.listFiles();
        if (files == null) return "{}";

        for (int i = 0; i < files.length; i++) {
            if (!files[i].isFile()) continue;

            String[] fileName = files[i].getName().split("\\.");

            if (fileName.length == 1) continue;
            if (!fileName[1].equals("craft")) continue;

            String map = fileName[0];

            leaderboards.append("\"").append(map).append(".gameCount").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".roundCount").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".time").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".1mCrafts").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".90sCrafts").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".2mCrafts").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".5mCrafts").append("\"").append(",");
            leaderboards.append("\"").append(map).append(".timeAll").append("\"").append(",");

            try {
                List<String> crafts = Files.readAllLines(Paths.get("./plugins/Workshop/" + map + ".craft"));

                for (int j = 0; j < crafts.size(); j++) {
                    String craft = crafts.get(j).split(", ")[0];
                    leaderboards.append("\"").append(map).append(".crafts.").append(craft).append("\"").append(",");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "{" +
                "\"leaderboards\": [" +
                        leaderboards +
                    "]" +
                "}";
    }

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

    public static String generateLeaderboard(String type) {
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
            double value = playerData.getDouble(type);

            players.append("{")
                    .append("\"uuid\": \"").append(uuid).append("\",")
                    .append("\"name\": \"").append(name).append("\",")
                    .append("\"value\": \"").append(value).append("\"")
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

        List<String> m1Crafts = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".1mCrafts", 0);
            m1Crafts.add("\"m1crafts\": " + value);
        }

        List<String> s90Crafts = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".90sCrafts", 0);
            s90Crafts.add("\"s90crafts\": " + value);
        }

        List<String> m2Crafts = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".2mCrafts", 0);
            m2Crafts.add("\"m2crafts\": " + value);
        }

        List<String> m5Crafts = new ArrayList<>();
        for (String map : maps) {
            int value = playerData.getInt(map + ".5mCrafts", 0);
            m5Crafts.add("\"m5crafts\": " + value);
        }

        List<String> TimeAll = new ArrayList<>();
        for (String map : maps) {
            double value = playerData.getDouble(map + ".timeAll", 0);
            TimeAll.add("\"allCrafts\": " + value);
        }

        StringBuilder mapData = new StringBuilder();

        for (int i = 0; i < maps.size(); i++) {
            String mgc = mapGameCount.get(i);
            String mrc = mapRoundCount.get(i);
            String mt = mapTime.get(i);
            String mn = maps.get(i);

            String m1 = m1Crafts.get(i);
            String s90 = s90Crafts.get(i);
            String m2 = m2Crafts.get(i);
            String m5 = m5Crafts.get(i);
            String ta = TimeAll.get(i);

            ConfigurationSection craftSection = playerData.getConfigurationSection(mn+".crafts");
            StringBuilder craftData = new StringBuilder();
            if (craftSection != null) {
                List<String> crafts = new ArrayList<>(craftSection.getKeys(false));

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
            } else {
                craftData.append("\"crafts\": []");
            }

            mapData.append("{")
                    .append("\"name\": \"").append(mn).append("\",")
                    .append(mgc).append(",")
                    .append(mrc).append(",")
                    .append(mt).append(",")
                    .append(m1).append(",")
                    .append(s90).append(",")
                    .append(m2).append(",")
                    .append(m5).append(",")
                    .append(ta).append(",")
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
