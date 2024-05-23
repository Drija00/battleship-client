package main.java.shared.protocol;

import main.java.model.Player;
import main.java.shared.model.Coordinates;
import main.java.shared.model.Map;
import main.java.shared.model.PlayerMsg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Msg implements Serializable {
    private MsgType msgType;
    private Integer playerID;
    private Object dataObj;
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private int wins;

    List<PlayerMsg> playerList = new ArrayList<>();

    public List<PlayerMsg> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(List<PlayerMsg> playerList) {
        this.playerList = playerList;
    }

    public void setDataObj(Object dataObj) {
        this.dataObj = dataObj;
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

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }

    public Integer getPlayerID() {
        return playerID;
    }

    public void setPlayerID(Integer playerID) {
        this.playerID = playerID;
    }

    public Object getDataObj() {
        return dataObj;
    }

    public Msg(MsgType msgType, Integer playerID, Map map) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Map(map);
    }

    public Msg(MsgType msgType, Integer playerID, Coordinates coordinates) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = new Coordinates(coordinates);
    }

    public Msg(MsgType msgType, Integer playerID, List<Coordinates> coordinates) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.dataObj = coordinates;
    }

    public Msg(MsgType msgType, int playerID) {
        this.msgType = msgType;
        this.playerID = playerID;
    }
    public Msg(MsgType msgType,int playerID, List<PlayerMsg> players) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.playerList = players;
    }

    public Msg(MsgType msgType,int playerID, String username, String password) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.username = username;
        this.password = password;
    }

    public Msg(MsgType msgType,int playerID,String firstname, String lastname, String username, String password,int wins) {
        this.msgType = msgType;
        this.playerID = playerID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.username = username;
        this.password = password;
        this.wins = wins;
    }

    public Msg() {
    }

    @Override
    public String toString() {
        return "Msg{" +
                "msgType=" + msgType +
                ", playerID=" + playerID +
                ", dataObj=" + dataObj +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}