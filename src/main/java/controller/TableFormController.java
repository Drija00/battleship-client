package main.java.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.GameStage;
import main.java.model.Player;
import main.java.shared.model.PlayerMsg;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableFormController implements Initializable {
    @FXML
    private TableView<Player> table;
    @FXML
    private TableColumn<Player,Integer> Rank;
    @FXML
    private TableColumn<Player,String> Username;
    @FXML
    private TableColumn<Player,Integer> Wins;
    @FXML
    private Button menuButton;

    @FXML
    public void handleMenuButton(){
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/Menu.fxml");});
    }

    ObservableList players = FXCollections.observableArrayList(
            Player.getInstance().getPlayerList()
    );


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Rank.setCellValueFactory(new PropertyValueFactory<Player,Integer>("playerId"));
        Username.setCellValueFactory(new PropertyValueFactory<Player,String>("username"));
        Wins.setCellValueFactory(new PropertyValueFactory<Player,Integer>("wins"));
        table.setItems(players);
    }

    private List<Player> getPlayers(){
        List<Player> players1 = new ArrayList<>();
        players1.addAll(Player.getInstance().getPlayerList());
        return players1;
    }
}
