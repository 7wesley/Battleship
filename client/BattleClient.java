package client;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * Implements the  client-side  logic  of  this  client-server
 * application. It is responsible for creating 
 * a ConnectionAgent, reading input from the user, and sending 
 * that input to the server via the ConnectionAgent.
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
public class BattleClient extends MessageSource implements MessageListener {

    /** The host adress */
    private InetAddress host;

    /** The port */
    private int port;

    /** The clients username */
    private String username;

    /** Stream used for printing */
    private PrintStreamMessageListener stream;

    /** Connection agent used for passing messages */
    ConnectionAgent agent;

    /**
     * Constructor for a BattleClient
     * @param hostname The host 
     * @param port The port
     * @param username Username of the client
     * @throws UnknownHostException if host not found
     */
    public BattleClient(String hostname, int port, String username) throws UnknownHostException {
        //Adding stream as an observer
        stream = new PrintStreamMessageListener(new PrintStream(System.out));
        super.addMessageListener(stream);

        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

    /**
     * Used for creating connectionAgent and registering it.
     * @throws IOException
     */
    public void connect() throws IOException {
        Socket clientSocket = new Socket(this.host, this.port);
        agent = new ConnectionAgent(clientSocket);
        agent.sendMessage(this.username);
        agent.addMessageListener(this);
        Thread thread = new Thread(agent);
        thread.start();
    }

    /**
     * Used to notify observers that the subject has received a message.
     * @param message The message received by the subject
     * @param source  The source from which this message originated (if needed).
    */
    public void messageReceived(String message, MessageSource source) {
        super.notifyReceipt(message);
    }

    /**
     * Used to notify observers that the subject will not receive new messages; observers can
     * deregister themselves.
     *
     * @param source The <code>MessageSource</code> that does not expect more messages.
     */
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }

    /**
     * Determines if the BattleClients agent is connected.
     * @return
     */
    public boolean isConnected() {
        return this.agent.isConnected();
    }

    /**
     * Used to send message
     * @param message The message to send. 
     */
    public void send(String message) {
        agent.sendMessage(message);
    }
}
