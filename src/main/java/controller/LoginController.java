package main.java.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.java.GameStage;
import main.java.model.Player;
import main.java.network.ConnectionHandler;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import javax.swing.*;

public class LoginController {
    private static LoginController instance;
    private MsgHandler msgHandler;

    public static LoginController getInstance() {
        if (instance == null) {
            instance = new LoginController();
        }
        return instance;
    }
    public LoginController(){}
    @FXML
    Button loginButton;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    Button menuButton;
    @FXML
    private void handleMenuButton(){
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/Menu.fxml");
        });
    }
    @FXML
    void initialize(){
        msgHandler = MsgHandler.getInstance();
        msgHandler.setLoginController(this);
    }

    @FXML
    void handleLoginButton(){
        Msg msg = new Msg(MsgType.LOGIN,Player.getInstance().getPlayerId(), username.getText(),password.getText());
        System.out.println(password.getText());
        ConnectionHandler.getInstance().getMessagesToSend().add(msg);
    }

    public void login(String username, String password,int wins) {
        if(password!=null && username!=null){
            Player.getInstance().setUsername(username);
            Player.getInstance().setPassword(password);
            Player.getInstance().setWins(wins);
            System.out.println(Player.getInstance().getPassword() + " dsaddasd");
            System.out.println("Wins:" + Player.getInstance().getWins());
            JOptionPane.showMessageDialog(null,"Welcome!","",JOptionPane.INFORMATION_MESSAGE);
            Platform.runLater(()->{
                GameStage.getInstance().setScene("main/java/Menu.fxml");
            });
        }else {
            JOptionPane.showMessageDialog(null,"Bad credentials!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setPLayerId(Integer playerID) {
        Player.getInstance().setPlayerId(playerID);
    }
}
