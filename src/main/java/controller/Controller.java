package main.java.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import main.java.GameStage;
import main.java.model.*;
import main.java.shared.model.Coordinates;
import main.java.shared.model.FieldState;
import main.java.network.ConnectionHandler;
import main.java.shared.model.Map;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Controller {
    private int shipLength;
    private int currentNumOfFieldsTaken;
    private int currentSelectedField;
    private int previousSelectedField;
    private int secondPreviousSelectedField;
    private int numOfShipsPlaced;
    private List<Coordinates> shipCoordinates;
    List<MenuItem> items;
    private MsgHandler msgHandler;
    private Map enemyMap;

    public Controller() {
        shipCoordinates = new ArrayList<>();
        enemyMap = new Map();
    }


    @FXML
    private GridPane yourGrid;

    @FXML
    private GridPane enemyGrid;

    @FXML
    private Label status;

    @FXML
    private Button finishedButton;

    @FXML
    private MenuButton shipsMenuBar;

    @FXML
    private Label playerLabel;

    @FXML
    private Button menuButton;

    @FXML
    private void initialize() {
        menuButton.setDisable(true);
        finishedButton.setDisable(true);
        shipsMenuBar.setDisable(true);
        status.setText("Not connected");
        playerLabel.setText(Player.getInstance().getUsername()+" (You)");

        currentNumOfFieldsTaken = 0;
        numOfShipsPlaced = 0;
        items = shipsMenuBar.getItems();
        System.out.println("Init gotov");
        msgHandler = MsgHandler.getInstance();
        msgHandler.setController(this);
        System.out.println("Init form kontroler set");
    }

    @FXML
    private void handleMenuButton(){
        Player.getInstance().setPlayerMap(new Map());
        Player.getInstance().setShips(new ArrayList<>());
        Msg answer = new Msg(MsgType.RESET, Player.getInstance().getPlayerId(), Player.getInstance().getPlayerMap());
        ConnectionHandler.getInstance().getMessagesToSend().add(answer);
        Platform.runLater(()->{
            GameStage.getInstance().setScene("main/java/Menu.fxml");
        });
    }

    @FXML
    private void handleFinishedButtonFired() {
        Msg answer = new Msg(MsgType.SHIPS_PLACED, Player.getInstance().getPlayerId(), Player.getInstance().getPlayerMap());
        ConnectionHandler.getInstance().getMessagesToSend().add(answer);

        status.setText("Wait for server response");
        finishedButton.setDisable(true);
        setGridIsDisable(yourGrid, true);
    }

    @FXML
    private void handleMenuItemSelected(ActionEvent event) {
        status.setText("Place ship");
        shipsMenuBar.setDisable(true);

        setGridIsDisable(yourGrid, false);

        MenuItem menuItem = (MenuItem) event.getSource();
        menuItem.setDisable(true);

        setOpositeShipDisabled(menuItem);

        shipLength = getShipLength(menuItem);
    }

    @FXML
    private void handleEnemyGridCellButtonFired(ActionEvent event) {
        Node node = (Node) event.getSource();
        int row = GridPane.getRowIndex(node);
        int col = GridPane.getColumnIndex(node);

        if(enemyMap.getFieldState(row,col)==FieldState.EMPTY) {
            setGridIsDisable(enemyGrid, true);

            Coordinates shotCoordinates = new Coordinates(GridPane.getRowIndex(node), GridPane.getColumnIndex(node));
            Msg shotMsg = new Msg(MsgType.SHOT_PERFORMED, Player.getInstance().getPlayerId(), shotCoordinates);

            ConnectionHandler.getInstance().getMessagesToSend().add(shotMsg);
        }else{
            JOptionPane.showMessageDialog(null,"Selected field has been tried already! Choose another field!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    @FXML
    private void handleYourGridCellButtonFired(ActionEvent event) {
        Node node = (Node) event.getSource();
        int row = GridPane.getRowIndex(node);
        int col = GridPane.getColumnIndex(node);
        if (Player.getInstance().getPlayerMap().getFieldState(row,col) == FieldState.EMPTY) {
            currentSelectedField = getNumOfField(row,col);
            System.out.println("Num of current field: " + currentSelectedField + " \tNum of previous field: "+previousSelectedField);
            System.out.println("Num of selected fields: " + currentNumOfFieldsTaken + " \tNum of ship length: "+shipLength);
            if(shipLength>0){
                if(checkCoordinatesForHorizontalShipPlacement(row,col,Math.abs(shipLength))) {
                    handleGridCell(node, row, col,new HorizontalShipCondition());
                }else {
                    JOptionPane.showMessageDialog(null,"There is no space for placing chosen horizontal ship!","ERROR",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if(checkCoordinatesForVerticalShipPlacement(row,col,Math.abs(shipLength))) {
                    handleGridCell(node, row, col, new VerticalShipCondition());
                }else {
                    JOptionPane.showMessageDialog(null,"There is no space for placing chosen vertical ship!","ERROR",JOptionPane.ERROR_MESSAGE);

                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Selected field is already taken!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleGridCell(Node node, int row, int col,ShipPlacementCondition shipPlacementCondition){
        if(shipPlacementCondition.check(currentNumOfFieldsTaken,currentSelectedField,previousSelectedField,secondPreviousSelectedField))
        {
            currentNumOfFieldsTaken = shipPlacementCondition.setCurrentNumOfFields(currentNumOfFieldsTaken);
            if(shipPlacementCondition.setSecondPreviousField(currentNumOfFieldsTaken)){
                secondPreviousSelectedField = previousSelectedField;
            }
            previousSelectedField = currentSelectedField;
            node.setDisable(true);
            node.setStyle("-fx-background-color: deepskyblue");

            Player.getInstance().getPlayerMap().setFieldState(row,col, FieldState.SHIP);
            shipCoordinates.add(new Coordinates(row,col));

            if (currentNumOfFieldsTaken == shipLength) {
                Player.getInstance().getShips().add(new Ship(numOfShipsPlaced,shipCoordinates,Math.abs(shipLength)));
                shipCoordinates = new ArrayList<>();
                currentNumOfFieldsTaken = 0;
                ++numOfShipsPlaced;

                shipsMenuBar.setDisable(false);
                setGridIsDisable(yourGrid, true);

                if (numOfShipsPlaced == 5) {
                    shipsMenuBar.setDisable(true);
                    finishedButton.setDisable(false);
                    printShips();
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Selected field must be placed next to the previous selected one!","ERROR",JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getOpositeShip(MenuItem item){
        return switch (item.getText()) {
            case "Carrier Horizontal [Size 5]" -> "Carrier Vertical [Size 5]";
            case "Carrier Vertical [Size 5]" -> "Carrier Horizontal [Size 5]";
            case "Battleship Horizontal [Size 4]" -> "Battleship Vertical [Size 4]";
            case "Battleship Vertical [Size 4]" -> "Battleship Horizontal [Size 4]";
            case "Cruiser Horizontal [Size 3]" -> "Cruiser Vertical [Size 3]";
            case "Cruiser Vertical [Size 3]" -> "Cruiser Horizontal [Size 3]";
            case "Submarine Horizontal [Size 3]" -> "Submarine Vertical [Size 3]";
            case "Submarine Vertical [Size 3]" -> "Submarine Horizontal [Size 3]";
            case "Destroyer Horizontal [Size 2]" -> "Destroyer Vertical [Size 2]";
            case "Destroyer Vertical [Size 2]" -> "Destroyer Horizontal [Size 2]";
            default -> null;
        };
    }

    private void setOpositeShipDisabled(MenuItem item){
        String opositeShipName = getOpositeShip(item);
        for(MenuItem i : items){
            if(i.getText().equals(opositeShipName)){
                i.setDisable(true);
            }
        }
    }

    private int getNumOfField(int row, int col){
        return (row*10)+col;
    }

    void handleSetId(Msg msg) {
        Player.getInstance().setPlayerId(msg.getPlayerID());
        System.out.println("Id " + msg.getPlayerID());
        ConnectionHandler.getInstance().getMessagesToSend().add(new Msg(MsgType.ID_IS_SET, msg.getPlayerID()));

        Platform.runLater(() -> {
            status.setText("Waiting for game to begin");
        });
    }

    void handlePlaceShips() {
        Platform.runLater(() -> {
            shipsMenuBar.setDisable(false);
            status.setText("Choose ship from menu bar");
        });
    }

    void handleMakeMove() {
        Platform.runLater(() -> {
            status.setText("Make move");
            setGridIsDisable(enemyGrid, false);
        });
    }

    void handleWaitForMove() {
        ConnectionHandler.getInstance().getMessagesToSend().add(new Msg(MsgType.WAITING, Player.getInstance().getPlayerId()));

        Platform.runLater(() -> {
            status.setText("Wait for move");
        });
    }

    private void printShips(){
        for(Ship s: Player.getInstance().getShips()){
            System.out.println(s.toString());
        }
    }

    public List<Integer> getAllShipFieldsForGivenRow(int row){
        List<Integer> cordNums = new ArrayList<>();
        cordNums.add(row*10);
        cordNums.add((row+1)*10);
        for (Ship ship: Player.getInstance().getShips()){
            for(Coordinates c: ship.getPositions()){
                if(c.getRow()==row){
                    cordNums.add(getNumOfField(c.getRow(),c.getCol()));
                }
            }
        }
        for (Integer i : cordNums){
            System.out.println(i);
        }
        return cordNums;
    }
    public List<Integer> getAllShipFieldsForGivenColumn(int col){
        List<Integer> cordNums = new ArrayList<>();
        cordNums.add(0);
        cordNums.add(100);
        for (Ship ship: Player.getInstance().getShips()){
            for(Coordinates c: ship.getPositions()){
                if(c.getCol()==col){
                    cordNums.add(getNumOfField(c.getRow(),c.getCol()));
                }
            }
        }
        for (Integer i : cordNums){
            System.out.println(i);
        }
        return cordNums;
    }

    public boolean checkCoordinatesForHorizontalShipPlacement(int row, int col, int shipLength){
        List<Integer> cordNums = getAllShipFieldsForGivenRow(row);
        int min = cordNums.get(0);
        int max = cordNums.get(1);
        int curPosition = getNumOfField(row, col);

        if(cordNums.size()>2){
            for (int num : cordNums) {
                if (num < curPosition && num > min) {
                    min = num;
                } else if (num > curPosition && num < max) {
                    max = num;
                }
            }
        }
        System.out.println("Min: "+min+ " Max: "+max);

        return ((min == row * 10 && max - min >= shipLength) || (max - min > shipLength));
    }
    public boolean checkCoordinatesForVerticalShipPlacement(int row, int col, int shipLength){
        List<Integer> cordNums = getAllShipFieldsForGivenColumn(col);
        int min = cordNums.get(0);
        int max = cordNums.get(1);
        int curPosition = getNumOfField(row, col);

        if(cordNums.size()>2){
            for (int num : cordNums) {
                if (num < curPosition && num > min) {
                    min = num;
                } else if (num > curPosition && num < max) {
                    max = num;
                }
            }
        }
        System.out.println("Min: "+min+ " Max: "+max);

        return (min == 0 && max >= shipLength * 10) || (max == 100 && (100 - min) > shipLength * 10) || (max - min - 10 == shipLength * 10);
    }

    public List<Coordinates> updateShipHit(Integer row, Integer col){
        for(Ship ship : Player.getInstance().getShips()){
           for (Coordinates c: ship.getPositions()) {
               if(c.equals(row,col)){
                   if(ship.incrementHits()){
                       return ship.getPositions();
                   }else{
                       return null;
                   }
               }
           }
        }
        return null;
    }

    void handleHitMakeMove(Integer row, Integer col) {

        List<Coordinates> shipCords = updateShipHit(row, col);
        if(shipCords != null) {
            for (Coordinates c : shipCords) {
                yourGrid.getChildren().get(c.getRow() * 10 + c.getCol()).setStyle("-fx-background-color: " + "red");
            }
            Platform.runLater(() -> {
                status.setText("Your ship has been sunk! Your turn");
            });
            setGridIsDisable(enemyGrid, false);
            ConnectionHandler.getInstance().getMessagesToSend().add(new Msg(MsgType.SHIP_SUNK, Player.getInstance().getPlayerId(),shipCords));

        }else{
            updateGUI("You have been shot! Your turn", yourGrid, enemyGrid, false, "yellow",
                    row, col);
        }
    }

    public void handleEnemyShipSunk(ArrayList<Coordinates> coordinatesList) {
        for (Coordinates c : coordinatesList) {
            System.out.println(c.toString());
            enemyGrid.getChildren().get(c.getRow() * 10 + c.getCol()).setStyle("-fx-background-color: " + "red");
        }
    }

    void handleHitWaitForMove(Integer row, Integer col) {
        ConnectionHandler.getInstance().getMessagesToSend().add(new Msg(MsgType.WAITING, Player.getInstance().getPlayerId()));
        enemyMap.setFieldState(row,col,FieldState.SHOT);
        updateGUI("You have hit the enemy! Good job", enemyGrid, enemyGrid, true, "yellow",
                row, col);
    }

    void handle_login(String u,String p){
        Player.getInstance().setUsername(u);
        Player.getInstance().setPassword(p);
    }

    void handleMissMakeMove(Integer row, Integer col) {
            updateGUI("Enemy didn't hit you. Your move", yourGrid, enemyGrid, false, "black",
            row, col);
    }

    void handleMissWaitForMove(Integer row, Integer col) {
        ConnectionHandler.getInstance().getMessagesToSend().add(new Msg(MsgType.WAITING, Player.getInstance().getPlayerId()));
        enemyMap.setFieldState(row,col,FieldState.HIT);
            updateGUI("You didn't hit. Wait for move", enemyGrid, enemyGrid, true, "black",
                row, col);
    }

    void handleLose(Integer row, Integer col) {
        updateGUI("You have lost", yourGrid, enemyGrid, true, "red", row, col);
        menuButton.setDisable(false);
    }

    void handleWin(Integer row, Integer col) {updateGUI("You have won", enemyGrid, enemyGrid, true, "red", row, col);
        Msg msg = new Msg(MsgType.WIN,Player.getInstance().getPlayerId());
        ConnectionHandler.getInstance().getMessagesToSend().add(msg);
        menuButton.setDisable(false);
    }

    /**
     * @param statusVal             Text which will appear in status TextField
     * @param gridToUpdate          Grid which will be updated with a shot result
     * @param gridToChangeIsDisable Grid which status will be changed to enable or prevent shooting
     * @param newGridState          A value to which gridToChangeIsDisable will be changed
     * @param color                 Color which will appear in cell of updated grid
     * @param row                   Row of cell to update
     * @param col                   Column of cell to update
     */
    void updateGUI(String statusVal, GridPane gridToUpdate, GridPane gridToChangeIsDisable, boolean newGridState,
                   String color, Integer row, Integer col) {
        Platform.runLater(() -> {
            status.setText(statusVal);

            gridToUpdate.getChildren().get(row * 10 + col).setStyle("-fx-background-color: " + color);
            setGridIsDisable(gridToChangeIsDisable, newGridState);
        });
    }

    private Integer getShipLength(MenuItem menuItem) {
        HashMap<String, Integer> tmp = new HashMap<>();
        tmp.put("Carrier Horizontal [Size 5]", 5);
        tmp.put("Carrier Vertical [Size 5]", -5);
        tmp.put("Battleship Horizontal [Size 4]", 4);
        tmp.put("Battleship Vertical [Size 4]", -4);
        tmp.put("Cruiser Horizontal [Size 3]", 3);
        tmp.put("Cruiser Vertical [Size 3]", -3);
        tmp.put("Submarine Horizontal [Size 3]", 3);
        tmp.put("Submarine Vertical [Size 3]", -3);
        tmp.put("Destroyer Horizontal [Size 2]", 2);
        tmp.put("Destroyer Vertical [Size 2]", -2);

        return tmp.get(menuItem.getText());
    }

    private void setGridIsDisable(GridPane gridPane, boolean isGridDisable) {
        for (Node node : gridPane.getChildren()) {
            node.setDisable(isGridDisable);
        }
    }

    public void close() {
        if (ConnectionHandler.getInstance() != null) {
            ConnectionHandler.getInstance().closeConnection();
        }

        if (msgHandler != null && msgHandler.isAlive()) {
            msgHandler.interrupt();
        }
    }
}
