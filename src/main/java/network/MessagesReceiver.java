package main.java.network;

import main.java.shared.protocol.Msg;

import java.io.ObjectInputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class MessagesReceiver extends Thread {
    private ArrayBlockingQueue<Msg> messagesReceived;
    private ObjectInputStream fromServer;

    MessagesReceiver(ObjectInputStream fromServer) {
        this.messagesReceived = new ArrayBlockingQueue<>(10);
        this.fromServer = fromServer;
    }

    public ArrayBlockingQueue<Msg> getMessagesReceived() {
        return messagesReceived;
    }

    @Override
    public void run() {
        this.setName("MessagesReceiver");

        Msg msg;
        try {
            while ((msg = (Msg) fromServer.readObject()) != null) {
                messagesReceived.add(msg);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
