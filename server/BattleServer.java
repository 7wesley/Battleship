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
    final int DEFAULT_SIZE = 10;

    //possibly add size param
    public BattleServer(int port) throws IOException {
        System.out.println("Listening on port " + port);
        serverSocket = new ServerSocket(port);
        players = new Hashtable<>();
    }

    public void listen() throws IOException {
        BufferedReader in;

        while (!serverSocket.isClosed()) {
            //blocks while waiting for connection
            System.out.println("WAITING FOR NEW CLIENT");
            Socket clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String username = in.readLine();

            //Create and run thread with connectionAgent
            ConnectionAgent player = new ConnectionAgent(clientSocket);
            player.addMessageListener(this);
            Thread thread = new Thread(player);
            thread.start();

            this.players.put(username, player);
            this.broadcast(username + " has joined the battle!");
        }
    }

    public void getNextTurn() {
        if (this.game.isActive()) {
            String turnUsername = this.game.getNextMove();
            this.broadcast("It's " + turnUsername + "'s turn!");
        } else {
            this.broadcast("Winner: " + this.game.getWinner());
            this.broadcast("Enter '/start' to play again!");
            game = null;
        }
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
        System.out.println("I GOT A MESSAGE");
        String[] command = message.split(" ");
        String sender = command[0];

        switch (command[1]) {
            case "/start":
                if (game == null) {
                    game = new Game(DEFAULT_SIZE, this.players);
                    System.out.println("Game started");
                    getNextTurn();
                }
                break;
            case "/surrender":
                this.game.removePlayer(sender);
                this.players.get(sender).sendMessage("You lost");
            case "/fire":
                this.fire(sender, message);
                break;
            case "/display":
                if (command.length == 3) {
                    String grid = this.game.getGrid(sender, command[2]);
                    this.players.get(sender).sendMessage(grid);
                } else {
                    this.players.get(sender).sendMessage("Invalid command provided!");
                }
            default:
                break;
        }
    }

    public void fire(String sender, String message) {
        String[] command = message.split(" ");
        try {
            int x = Integer.parseInt(command[2]);
            int y = Integer.parseInt(command[3]);
            if (this.game.checkValidMove(sender, command[4])) {
                String result = this.game.makeMove(command[4], y, x);
                this.broadcast(result);
                this.getNextTurn();
            } else {
                System.out.println("Not valid move!");
                this.players.get(sender).sendMessage("Invalid command provided!");
            }
        } catch (NumberFormatException e) {
            this.players.get(sender).sendMessage("Invalid command provided!");
        }
    }

    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
        System.out.println("BattleServer Connection ended");
    }
}
