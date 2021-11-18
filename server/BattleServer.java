package server;

import java.net.ServerSocket;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * Implements the server-side logic of this client-server
 * application. It isresponsible for accepting incoming 
 * connections, creating ConnectionAgents, and passing 
 * the ConnectionAgent off to threads for processing.  
 * 
 * OBSERVER
 */
public class BattleServer implements MessageListener {
    private ServerSocket serverSocket;
    private int current;
    private Game game;
    private ConnectionAgent[] players;

    //possibly add size param
    public BattleServer(int port) {
       //serverSocket = new ServerSocket(port);
    }

    public void listen() {

        //for (int i = 0; i < playersize; i++)
        //serverSocket.accept()

        
        //once all player quota met:
        int gridSize = 10;
        int numPlayers = 2;

        players = new ConnectionAgent[numPlayers];

        for (int i = 0; i < numPlayers; i++) {
            ConnectionAgent player = new ConnectionAgent();
            player.addMessageListener(this);
            players[i] = player;
        }

        this.game = new Game(gridSize, numPlayers);

        while (this.game.isActive()) {
            String msg = this.game.getNextMove();
            //BROADCAST WHO'S TURN IT IS
            this.broadcast(msg);
        }

       //this.game.getWinner();
    }


    public void broadcast(String message) {
        for (ConnectionAgent player: this.players) {
            player.sendMessage(message);
        }
    }

    public void messageReceived(String message, MessageSource source) {
        System.out.println("Source " + source.toString() + " sent " + message);
        //RECEIVE A PLAYER'S MOVE THEN BROADCAST TO ALL
        this.broadcast("Player move: " + message);
    }

    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
        System.out.println("BattleServer Connection ended");
    }
}
