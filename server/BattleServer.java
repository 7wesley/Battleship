package server;

import java.net.ServerSocket;
import java.util.Scanner;

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

        /*
        for (int i = 0; i < playersize; i++)
        serverSocket.accept()

        players = new ConnectionAgent[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            ConnectionAgent player = new ConnectionAgent();
            player.addMessageListener(this);
            players[i] = player;
        }*/

        //TODO: Change this when implementing networking
        Scanner sc = new Scanner(System.in);
        System.out.println("How many players?");
        int numPlayers = sc.nextInt();
        System.out.println("Grid size?");
        int gridSize = sc.nextInt();

        String[] players = new String[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + " enter your username");
            players[i] = sc.next();
        }

        String turnUsername;
        this.game = new Game(gridSize, players);
        int x, y;
        
        while (this.game.isActive()) {
            turnUsername = this.game.getNextMove();
            System.out.println(turnUsername + ", enter your move!");
            String[] move = sc.nextLine().split(" ");
            //TESTING
            for (String str: move) {
                System.out.println(str + " ");
            }
            //TESTING
            if (move[0].equals("/fire")) {
                x = Integer.parseInt(move[1]);
                y = Integer.parseInt(move[2]);
                if (!this.game.validHit(move[1], x, y)) {
                    System.out.println("Invalid move, try again!");
                }
            } else if (move[0].equals("/surrender")) {
                this.game.removePlayer(turnUsername);
            } else if (move[0].equals("/display")) {
                System.out.println(this.game.getGrid(turnUsername));
            }
        }

        this.game.getWinner();
        sc.close();
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
