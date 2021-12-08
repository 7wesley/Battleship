package client;

import java.io.IOException;
/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * implements  the  client-side  logic  of  this  client-server
 * application.   Itis responsible for creating 
 * a ConnectionAgent, reading input from the user, and sending 
 * that input to the server via the ConnectionAgent.
 * 
 * SUBJECT AND OBSERVER
 */
public class BattleClient extends MessageSource implements MessageListener {
    private InetAddress host;
    private int port;
    private String username;
    private PrintStreamMessageListener stream;
    ConnectionAgent agent;

    public BattleClient(String hostname, int port, String username) throws UnknownHostException {
        //Adding stream as an observer
        stream = new PrintStreamMessageListener(new PrintStream(System.out));
        super.addMessageListener(stream);

        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

    public void connect() {
        try {
        
            Socket clientSocket = new Socket(this.host, this.port);
            
            agent = new ConnectionAgent(clientSocket);
            agent.sendMessage(this.username);
            agent.addMessageListener(this);
            Thread thread = new Thread(agent);
            thread.start();

        } catch (IOException e) {
            System.out.println(e);
        }
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
        //deregister
        source.removeMessageListener(this);
        System.out.println("My connection has ended :(");
    }

    public boolean isConnected() {
        return this.agent.isConnected();
    }

    public void send(String message) {
        agent.sendMessage(message);
    }
}
