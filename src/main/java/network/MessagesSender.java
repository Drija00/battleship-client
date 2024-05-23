package main.java.network;

import main.java.shared.protocol.Msg;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.concurrent.ArrayBlockingQueue;

public class MessagesSender extends Thread {
    private ArrayBlockingQueue<Msg> messagesToSend;
    private ObjectOutputStream toServer;

    MessagesSender(ObjectOutputStream toServer) {
        this.messagesToSend = new ArrayBlockingQueue<Msg>(10);
        this.toServer = toServer;
    }

    public ArrayBlockingQueue<Msg> getMessagesToSend() {
        return messagesToSend;
    }

    private void send(Msg msgToServer) {
        try {
            toServer.writeObject(msgToServer);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void run() {
        this.setName("MessagesSender");

        Msg msg;
        try {
            while ((msg = messagesToSend.take()) != null) {
                send(msg);
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
