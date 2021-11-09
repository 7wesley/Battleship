package common;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Responsible for sending messages to and receiving messages 
 * from remote hosts.
 * Runs in its own thread. Waits for messages,
 * calls notifyReciept() when a message is received
 */
public class ConnectionAgent extends MessageSource implements Runnable {
    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private Thread thread;

    public ConnectionAgent(Socket socket) {
        this.socket = socket;
    }

    public void sendMessage(String message) {

    }

    public boolean isConnected() {
        return true;
    }

    public void close() {

    }

    public void run() {

    }
}
