package server;

import java.net.ServerSocket;

import common.MessageListener;
import common.MessageSource;

/**
 * Implements the server-side logic of this client-server
 * application. It isresponsible for accepting incoming 
 * connections, creating ConnectionAgents, and passing 
 * the ConnectionAgent off to threads for processing.  
 */
public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;

    public BattleServer(int port) {
        this.game = new Game(10);
    }

    public void listen() {

    }


    public void broadcast(String message) {

    }

    public void messageReceived(String message, MessageSource source) {

    }

    public void sourceClosed(MessageSource source) {

    }
}
