package main.java.network;

import main.java.shared.protocol.Msg;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;

public class ConnectionHandler {

    private static ConnectionHandler instance;
    private Socket socket;
    private ObjectOutputStream toServer;
    private ObjectInputStream fromServer;
    private MessagesReceiver messagesReceiver;
    private MessagesSender messagesSender;

    public static ConnectionHandler getInstance() {
        if (instance == null) {
                instance = new ConnectionHandler();
        }
        return instance;
    }

    @Override
    public String toString() {
        return "ConnectionHandler{" +
                "socket=" + socket +
                '}';
    }

    public ConnectionHandler(){
        try {
            socket = new Socket("127.0.0.1", 8189);
            toServer = new ObjectOutputStream(socket.getOutputStream());
            fromServer = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        messagesReceiver = new MessagesReceiver(fromServer);
        messagesSender = new MessagesSender(toServer);

        messagesReceiver.start();
        messagesSender.start();
    }

    public ArrayBlockingQueue<Msg> getMessagesReceived() {
        return messagesReceiver.getMessagesReceived();
    }

    public ArrayBlockingQueue<Msg> getMessagesToSend() {
        return messagesSender.getMessagesToSend();
    }

    public void closeConnection(){
        if (messagesReceiver.isAlive()) {
            messagesReceiver.interrupt();
        }

        if (messagesSender.isAlive()) {
            messagesSender.interrupt();
        }

        try {
            if (socket!=null) {
                socket.close();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}

