package model;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Player {
    private static ArrayList<Player> allPlayers = new ArrayList<>();
    private static Player loggedInPlayer;
    private String username;
    private String password;
    private int score;
    private String levelTime;
    private String avatarResource;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
        this.score = 0;
        this.levelTime = "00:00";
    }

    public static Player getPlayerByUsername(String username) {
        for (Player player : allPlayers)
            if (player.username.equals(username))
                return player;
        return null;
    }

    public static void initializePlayers(int playerSize) {
        String pass = "";
        for (int i = 0; i < 10 - playerSize; i++) {
            allPlayers.add(new Player("empty", pass));
            pass+="1";
        }
    }

    public String getUsername() {
        return username;
    }

    public boolean isPasswordCorrect(String password) {
        return password.equals(this.password);
    }

    public static void addPlayer(Player player) {
        allPlayers.add(player);
    }

    public static void recoveryPlayers() {
        try {
            Gson gson = new Gson();
            Reader reader = Files.newBufferedReader(Paths.get("PLAYERS.json"));
            ArrayList<Player> allPlayers = new ArrayList<>();
            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);
            if (jsonArray != null) {
                for (JsonElement jsonElement : jsonArray) {
                    allPlayers.add(gson.fromJson(jsonElement, Player.class));
                }
                Player.allPlayers = allPlayers;
            }
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void savePlayers() {
        Gson gson = new Gson();
        String data = gson.toJson(Player.getAllPlayers());
        try {
            FileWriter output = new FileWriter("PLAYERS.json");
            output.write(data);
            output.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    public static ArrayList<Player> getAllPlayers() {
        return allPlayers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getLevelTime() {
        return levelTime;
    }

    public void setLevelTime(String levelTime) {
        this.levelTime = levelTime;
    }

    public static Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    public static void setLoggedInPlayer(Player loggedInPlayer) {
        Player.loggedInPlayer = loggedInPlayer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static void removePlayer(Player player) {
        allPlayers.remove(player);
    }

    public String getAvatarResource() {
        return avatarResource;
    }

    public void setAvatarResource(String avatarResource) {
        this.avatarResource = avatarResource;
    }
}
