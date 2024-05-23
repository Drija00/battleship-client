package main.java.model;

import javafx.stage.Stage;
import main.java.GameStage;
import main.java.shared.model.Map;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Map playerMap;
    private Integer playerId;
    private List<Ship> ships;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int wins;

    List<Player> playerList = new ArrayList<>();

    private static Player instance;

    public Player() {
        this.playerMap = new Map();
        this.playerId = null;
        ships = new ArrayList<>();
        this.username="";
        this.password="";
        this.firstname="";
        this.lastname="";
        this.wins = 0;
    }

    public synchronized static void setPlayer() {
        if(instance == null) {
            instance = new Player();
        }
    }

    public void setPlayerMap(Map playerMap) {
        this.playerMap = playerMap;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<Player> playerList) {
        this.playerList = playerList;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static Player getInstance() {
        return instance;
    }

    public static void setInstance(Player instance) {
        Player.instance = instance;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public Map getPlayerMap() {
        return playerMap;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", wins=" + wins +
                '}';
    }
}
