package main.java.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.java.GameStage;
import main.java.model.Player;
import main.java.network.ConnectionHandler;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import javax.swing.*;

public class RegisterController {
    private static RegisterController instance;

    private MsgHandler msgHandler;

    public static RegisterController getInstance() {
        if (instance == null) {
            instance = new RegisterController();
        }
        return instance;
    }

    public RegisterController(){}
    @FXML
    Button registerButton;
    @FXML
    TextField firstname;
    @FXML
    TextField lastname;
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
    private void initialize(){
        msgHandler = MsgHandler.getInstance();
        msgHandler.setRegisterController(this);
    }
    @FXML
    void handleRegisterButton(){
        Msg msg = new Msg(MsgType.REGISTER,Player.getInstance().getPlayerId(), firstname.getText(),lastname.getText(),username.getText(),password.getText(),0);
        ConnectionHandler.getInstance().getMessagesToSend().add(msg);
    }

    void register(String firstname,String lastname,String username,String password){
        if(username!=null){
            Platform.runLater(()->{
                GameStage.getInstance().setScene("main/java/Login.fxml");
            });
        }else {
            JOptionPane.showMessageDialog(null,"User already exists in database!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

}
