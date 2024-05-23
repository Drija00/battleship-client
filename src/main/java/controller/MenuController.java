/* @autor prof. dr Sinisa Vlajic,
* Univerzitet u Beogradu'
* Fakultet organizacionih nauka
* Katedra za softversko inzenjerstvo
* Laboratorija za softversko inzenjerstvo
/*  * Datum:2024-05-10 poruka1
*/
package main.java.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.GameStage;
import main.java.model.Player;
import main.java.network.ConnectionHandler;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import javax.swing.*;

public class MenuController {

    private static MenuController instance;

    public static MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public MenuController(){}
    @FXML
    private Button startGameButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button rankingsButton;
    @FXML
    private Button registerButton;
    @FXML
    private Label labelWelcome;
    @FXML
    private Label labelName;
    @FXML
    private Label labelUsername;
    MsgHandler msgHandler;

    @FXML
    public void initialize(){
        msgHandler = MsgHandler.getInstance();
        msgHandler.setMenuController(this);
        labelUsername.setText(Player.getInstance().getUsername());
        System.out.println(Player.getInstance().getPlayerId());
        System.out.println(labelUsername.getText());
        if(labelUsername.getText().isEmpty() || labelUsername.getText()==null){
            startGameButton.setDisable(true);
        }else {
            startGameButton.setDisable(false);
        }
    }

    @FXML
    public void handleStartGameButton(){
        GameStage.getInstance().setScene("main/java/Client.fxml");
        Msg msg = new Msg(MsgType.START_GAME,Player.getInstance().getPlayerId());
        ConnectionHandler.getInstance().getMessagesToSend().add(msg);
    }
    @FXML
    public void handleLoginButton(){
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/Login.fxml");});
    }
    @FXML
    public void handleRegisterButton(){
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/Register.fxml");});
    }
    @FXML
    public void handleRankingsButton(){
        Msg msg = new Msg(MsgType.GET_PLAYERS,Player.getInstance().getPlayerId());
        ConnectionHandler.getInstance().getMessagesToSend().add(msg);
    }

    public void startRankings() {
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/TableForm.fxml");});
    }
}
