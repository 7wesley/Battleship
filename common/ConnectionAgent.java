package common;

import java.io.IOException;
/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
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

    public ConnectionAgent(Socket socket) throws IOException {
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        thread = new Thread(this);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public boolean isConnected() {
        return this.socket.isConnected();
    }

    public void close() throws IOException {
        super.closeMessageSource();
        this.socket.close();
    }

    public void run() {
        while (!this.socket.isClosed()) {
            String serverResponse = in.nextLine();
            super.notifyReceipt(serverResponse);
        }
    }
}
