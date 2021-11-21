package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Responsible for sending messages to and receiving messages 
 * from remote hosts.
 * Runs in its own thread. Waits for messages,
 * calls notifyReciept() when a message is received
 * 
 * SUBJECT
 */
public class ConnectionAgent extends MessageSource implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent() {
        super();
        //super.addMessageListener(new BattleClient)
    }

    public ConnectionAgent(Socket socket) {
        this.socket = socket;
        thread = new Thread(this);
    }

    //notify all of our observers of message
    public void sendMessage(String message) {
        super.notifyReceipt(message);
    }

    public boolean isConnected() {
        //return socket.isConnected();
        return true;
    }

    public void close() {
        super.closeMessageSource();
        //socket.close()?
    }

    //does something w socket field??
    public void run() {
        //socket already connected to host, ready to send msg

        
    }
}
