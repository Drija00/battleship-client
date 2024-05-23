package main.java.controller;

import javafx.application.Platform;
import main.java.GameStage;
import main.java.model.Player;
import main.java.network.ConnectionHandler;
import main.java.shared.model.Coordinates;
import main.java.shared.model.PlayerMsg;
import main.java.shared.protocol.Msg;
import main.java.shared.protocol.MsgType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

public class MsgHandler extends Thread {
    private static MsgHandler instance;

    public static MsgHandler getInstance() {
        if (instance == null) {
            instance = new MsgHandler();
        }
        return instance;
    }
    private Controller controller;
    private LoginController loginController;
    private RegisterController registerController;
    private MenuController menuController;
    private ArrayBlockingQueue<Msg> messagesReceived;
    MsgHandler(){
        System.out.println(ConnectionHandler.getInstance().toString());
        this.messagesReceived = ConnectionHandler.getInstance().getMessagesReceived();
        this.start();
    }

    public void setController(Controller controller) {
        System.out.println("Kontroler napravljen");
        this.controller = controller;
    }

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setRegisterController(RegisterController registerController) {
        this.registerController = registerController;
    }

    public void setMenuController(MenuController menuController) {
        this.menuController = menuController;
    }

    MsgHandler(Controller controller) {
        this.controller = controller;
        this.messagesReceived = ConnectionHandler.getInstance().getMessagesReceived();
    }



    public <R> R invokeGenericMethod(String methodName, Object... arguments) {
        try {
            Method method = findMethod(controller.getClass(), methodName, arguments);
            return (R) method.invoke(controller, arguments);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    private Method findMethod(Class<?> clazz, String methodName, Object... arguments) throws NoSuchMethodException {
        Class<?>[] parameterTypes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            parameterTypes[i] = arguments[i].getClass();
        }
        System.out.println(clazz.getDeclaredMethod(methodName, parameterTypes));
        return clazz.getDeclaredMethod(methodName, parameterTypes);
    }

    @Override
    public void run() {
        this.setName("MsgHanlder");
        Msg msg;
        try {
            while ((msg = messagesReceived.take()) != null) {
                System.out.println(msg.toString());
                if(msg.getMsgType().equals(MsgType.ENEMY_SHIP_SUNK)){
                    System.out.println("MSGHandler entered.");
                    ArrayList<Coordinates> coordinatesList;
                    coordinatesList = (ArrayList<Coordinates>) msg.getDataObj();
                    for (Coordinates c: coordinatesList){
                        System.out.println(c.toString());
                    }
                    invokeGenericMethod("handleEnemyShipSunk",coordinatesList);
                }else {

                    Coordinates coordinates = (Coordinates) msg.getDataObj();
                    Integer row = (coordinates != null) ? coordinates.getRow() : null;
                    Integer col = (coordinates != null) ? coordinates.getCol() : null;


                    switch (msg.getMsgType()) {
                        case SET_ID:
                            System.out.println("OLA");
                            invokeGenericMethod("handleSetId", msg);
                            break;
                        case PLACE_SHIPS:
                            invokeGenericMethod("handlePlaceShips");
                            break;
                        case MAKE_MOVE:
                            invokeGenericMethod("handleMakeMove");
                            break;
                        case WAIT_FOR_MOVE:
                            invokeGenericMethod("handleWaitForMove");
                            break;
                        case HIT_MAKE_MOVE:
                            invokeGenericMethod("handleHitMakeMove", row, col);
                            break;
                        case HIT_WAIT_FOR_MOVE:
                            invokeGenericMethod("handleHitWaitForMove", row, col);
                            break;
                        case MISS_MAKE_MOVE:
                            invokeGenericMethod("handleMissMakeMove", row, col);
                            break;
                        case MISS_WAIT_FOR_MOVE:
                            invokeGenericMethod("handleMissWaitForMove", row, col);
                            break;
                        case LOSE:
                            invokeGenericMethod("handleLose", row, col);
                            break;
                        case WIN:
                            invokeGenericMethod("handleWin", row, col);
                            break;
                        case LOGIN:
                            System.out.println("maju");
                            loginController.login(msg.getUsername(),msg.getPassword(), msg.getWins());
                            Player.getInstance().setUsername(msg.getUsername());
                            break;
                        case INIT_SET_ID:
                            System.out.println("Init player id"+msg.getPlayerID());
                            Player.getInstance().setPlayerId(msg.getPlayerID());
                            break;
                        case REGISTER:
                            registerController.register(msg.getFirstname(), msg.getLastname(), msg.getUsername(), msg.getPassword());
                            break;
                        case GET_PLAYERS:
                            System.out.println("HANDLE GET ALL");
                            List<Player> players = new ArrayList<>();
                            int rank = 1;
                            for (PlayerMsg p: msg.getPlayerList()){
                                Player p1 = new Player();
                                p1.setPlayerId(rank++);
                                p1.setUsername(p.getUsername());
                                p1.setWins(p.getWins());
                                players.add(p1);
                            }
                            Player.getInstance().setPlayerList(players);
                            for(Player x: Player.getInstance().getPlayerList()){
                                System.out.println(x.toString());
                            }
                            menuController.startRankings();

                            break;
                    }
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
