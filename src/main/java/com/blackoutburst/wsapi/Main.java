package com.blackoutburst.wsapi;

import org.bukkit.plugin.java.JavaPlugin;
import spark.Spark;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Main extends JavaPlugin {

    public static String TOKEN = readEnv();

    private static String readEnv() {
        try {
            List<String> lines = Files.readAllLines(Paths.get(".env"));
            return lines.get(1).split("=")[1].replace("\"", "");
        } catch (Exception e) {
            System.err.println("Invalid .env file");
            System.exit(0);
        }
        return null;
    }

    @Override
    public void onEnable() {
        Spark.port(18499);

        Spark.get("/users", (req, res) -> {
            final String token = req.queryParams("token");

            if (token == null || !token.equals(TOKEN)) {
                res.status(401);
                return "Invalid token";
            }

            return JSONUtils.generatePlayerListJSON();
        });

        Spark.get("/user", (req, res) -> {
            final String uuid = req.queryParams("uuid");
            final String token = req.queryParams("token");

            if (token == null || !token.equals(TOKEN)) {
                res.status(401);
                return "Invalid token";
            }

            if (uuid == null) {
                res.status(400);
                return "Bad request";
            }

            final File file = new File("./plugins/Workshop/playerData/"+uuid+".yml");
            if (!file.exists()) {
                res.status(404);
                return "Not found";
            }

            return JSONUtils.generatePlayerJSON(file);
        });

        Spark.get("/leaderboards", (req, res) -> {
            final String token = req.queryParams("token");

            if (token == null || !token.equals(TOKEN)) {
                res.status(401);
                return "Invalid token";
            }

            return JSONUtils.generateLeaderboardsList();
        });

        Spark.get("/leaderboard", (req, res) -> {
            final String token = req.queryParams("token");
            final String type = req.queryParams("type");

            if (token == null || !token.equals(TOKEN)) {
                res.status(401);
                return "Invalid token";
            }

            if (type == null) {
                res.status(400);
                return "Bad request";
            }

            return JSONUtils.generateLeaderboard(type);
        });
    }
}
