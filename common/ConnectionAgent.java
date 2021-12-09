package common;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Responsible for sending messages to and receiving messages 
 * from remote hosts.
 * Runs in its own thread. Waits for messages,
 * calls notifyReciept() when a message is received
 *
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021 
*/
public class ConnectionAgent extends MessageSource implements Runnable {

    /** The socket for the connection*/
    private Socket socket;

    /** Scanner used for input */
    private Scanner in;

    /** PrintStream used for output */
    private PrintStream out;

    /** Thread for ConnectionAgent*/
    private Thread thread;

    /**
     * Constructor for a connection agent
     * @param socket Socket for connection
     * @throws IOException if InputStream or OutputStream throw exceptions
     */
    public ConnectionAgent(Socket socket) throws IOException {
        this.socket = socket;
        in = new Scanner(socket.getInputStream());
        out = new PrintStream(socket.getOutputStream());
        this.thread = new Thread(this);
    }

    /**
     * Used to send message
     * @param message The message to send.
     */
    public void sendMessage(String message) {
        out.println(message);
    }

    /**
     * Determines if the socket is connected.
     * @return true or false depending on if socket is connected.
     */
    public boolean isConnected() {
        return this.socket.isConnected();
    }

    /**
     * Closes message source and socket
     * @throws IOException if socket throws IOException
     */
    public void close() throws IOException {
        super.closeMessageSource();
        this.socket.close();
    }

    /**
     * Used to read input from server and notify
     */
    public void run() {
        while (!this.socket.isClosed()) {
            String serverResponse = in.nextLine();
            super.notifyReceipt(serverResponse);
        }
    }
}
