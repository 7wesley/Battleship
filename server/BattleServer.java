package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
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
    private Hashtable<String, ConnectionAgent> players;
    private boolean start = false;

    //possibly add size param
    public BattleServer(int port) throws IOException {
        System.out.println("Listening on port " + port);
        serverSocket = new ServerSocket(port);
    }

    public void listen() throws IOException {
        private BufferedReader in;

        while (!serverSocket.isClosed()) {
            while (!start) {
                //blocks while waiting for connection
                Socket clientSocket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String username = in.readLine();
                this.broadcast(username + " has joined the battle!");
                ConnectionAgent player = new ConnectionAgent(clientSocket);
                player.run();
                this.players.put(username, player);
            }
            while (this.game.isActive()) {
                String turnUsername = this.game.getNextMove();
                players.get(turnUsername).sendMessage(turnUsername + "s turn!");
            }

        }

        /*
        Scanner sc = new Scanner(System.in);
        int numPlayers = 0;
        int gridSize = 0;
        String turnUsername;
        int x, y;

        try {
            System.out.println("How many players?");
            numPlayers = Integer.parseInt(sc.nextLine());
            System.out.println("Grid size?");
            gridSize = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Expected a number but got a string!");
            System.exit(1);
        }
        
        String[] players = this.createPlayers(numPlayers, sc);
        this.game = new Game(gridSize, players);
        
        while (this.game.isActive()) {
            turnUsername = this.game.getNextMove();
            System.out.println(turnUsername + ", enter your move!");
            String[] move = sc.nextLine().split(" ");
        
            if (move[0].equals("/fire") && move.length == 4 && move[1].matches("^\\d+") && move[2].matches("^\\d+")) {
                x = Integer.parseInt(move[1]);
                y = Integer.parseInt(move[2]);
                if (this.game.checkValidMove(move[3])) {
                    this.game.makeMove(move[3], y, x);
                }
            } else if (move[0].equals("/surrender")) {
                this.game.removePlayer(turnUsername);
            } else if (move[0].equals("/display") && move.length == 2) {
                this.game.displayGrid(move[1]);
            } else {
                System.out.println("Invalid command! (Did you supply all arguments?)");
            }
        }

        System.out.println("Player " + this.game.getWinner() + ", you win!!!");
        sc.close();
        */
    }

    public String[] createPlayers(int numPlayers, Scanner sc) {
        String[] players = new String[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player " + (i + 1) + " enter your username");
            players[i] = sc.nextLine();
        }
        return players;
    }

    public void broadcast(String message) {
        for (ConnectionAgent player: this.players.values()) {
            player.sendMessage(message);
        }
    }

    public void messageReceived(String message, MessageSource source) {
        String[] command = message.split(" ");
        String sender = command[0];

        switch (command[1]) {
            case "/start":
                game = new Game(10, this.players);
                start = true;
                break;
            case "/fire":
                this.fire(message);
                break;
            case "/display":
                String grid = this.game.getGrid(sender, command[3]);
                this.players.get(sender).sendMessage(grid);
            default:
                break;
        }
    }

    public void fire(String message) {
        String[] command = message.split(" ");
        int x = Integer.parseInt(command[1]);
        int y = Integer.parseInt(command[2]);
        if (this.game.checkValidMove(command[3])) {
            this.game.makeMove(command[3], y, x);
        }
    }

    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
        System.out.println("BattleServer Connection ended");
    }
}
