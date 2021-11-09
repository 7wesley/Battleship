package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import common.MessageListener;
import common.MessageSource;

/**
 * implements  the  client-side  logic  of  this  client-server
 * application.   Itis responsible for creating 
 * a ConnectionAgent, reading input from the user, and sending 
 * that input to the server via the ConnectionAgent.
 */
public class BattleClient extends MessageSource implements MessageListener {
    private InetAddress host;
    private int port;
    private String username;

    public BattleClient(String hostname, int port, String username) throws UnknownHostException {
        this.host = InetAddress.getByName(hostname);
        this.port = port;
        this.username = username;
    }

    public void connect() {

    }

    public void messageReceived(String message, MessageSource source) {

    }

    public void sourceClosed(MessageSource source) {

    }

    public void send(String message) {

    }
}
