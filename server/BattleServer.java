package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;

import common.ConnectionAgent;
import common.MessageListener;
import common.MessageSource;

/**
 * Implements the server-side logic of this client-server
 * application. It is responsible for accepting incoming 
 * connections, creating ConnectionAgents, and passing 
 * the ConnectionAgent off to threads for processing.
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 12/8/2021
 */
public class BattleServer implements MessageListener {
    /** Server socket to receive and send data from */
    private ServerSocket serverSocket;
    /** The game of Battleship */
    private Game game;
    /** List of players in the game with usernames as keys */
    private Hashtable<String, ConnectionAgent> players;
    /** The default size of each player's grid */
    private final int DEFAULT_SIZE = 5;

    /**
     * Constructor for BattleServer
     * @param port - Port to host the server on
     * @throws IOException - If port is not available
     */
    public BattleServer(int port) throws IOException {
        System.out.println("Listening on port " + port);
        serverSocket = new ServerSocket(port);
        players = new Hashtable<>();
    }

    /**
     * Listens for incoming client connections. Once connected, a 
     * connectionAgent is created, the server is registered as a listener
     * for it, and it is ran in a separate thread.
     * @throws IOException - If server crashes
     */
    public void listen() throws IOException {
        BufferedReader in;
        PrintWriter clientOut;
        
        while (!serverSocket.isClosed()) {
            //blocks while waiting for connection
            Socket clientSocket = serverSocket.accept();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String[] command = in.readLine().split(" ");

            //Create and run thread with connectionAgent if username isnt already present
            if (command.length == 2 && !this.players.containsKey(command[1])) {
                ConnectionAgent player = new ConnectionAgent(clientSocket);
                player.addMessageListener(this);
                Thread thread = new Thread(player);
                thread.start();
                this.players.put(command[1], player);
                this.broadcast(command[1] + " has joined the battle!");
            } else {
                clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
                clientOut.println("Invalid command provided or username already exists");
                clientSocket.close();
            }
        }
    }

    /**
     * Gets the next turn from the game field and then broadcasts
     * to the room whose turn it is
     */
    private void getNextTurn() {
        if (this.game.isActive()) {
            String turnUsername = this.game.getNextMove();
            this.broadcast("It's " + turnUsername + "'s turn!");
        } else if (this.game.hasPlayers()) {
            this.broadcast(this.game.getWinner() + " wins the battleship royale!");
            this.broadcast("Enter '/start' to play again!");
            game = null;
        }
    }

    /**
     * Sends a message to all clients connected to the server
     * @param message - The message being sent to all clients
     */
    public void broadcast(String message) {
        for (ConnectionAgent player: this.players.values()) {
            player.sendMessage(message);
        }
    }

    /**
     * How the server reacts when it receives a new message from
     * one of its connectionAgents.
     * @message - The message the server has received from a client
     * @source - The subject that sent the message
     */
    public void messageReceived(String message, MessageSource source) {
        String[] command = message.split(" ");
        String sender = "";
        for (String key: this.players.keySet()){
            if (this.players.get(key) == source) {
                sender = key;
            }
        }

        switch (command[0]) {
            case "/start":
                this.start(sender);
                break;
            case "/surrender":
                this.surrender(sender);
                break;
            case "/fire":
                this.fire(sender, command);
                break;
            case "/display":
                this.display(sender, command);
                break;
            default:
                this.players.get(sender).sendMessage("Command doesn't exist!");
                break;
        }
    }

    /**
     * Logic for starting the game. If the game is already in progress,
     * the player attempting to start will be alerted
     * @param sender - The client sending the start request
     */
    private void start(String sender) {
        if (this.game != null) {
            this.players.get(sender).sendMessage("Game has already started!");
        } else if (this.players.size() == 1) {
            this.players.get(sender).sendMessage("Not enough players to play the game!");
        } else {
            game = new Game(DEFAULT_SIZE, this.players);
            this.broadcast("The game begins!");
            this.getNextTurn();
        }
    }

    /**
     * Displays a specific grid to the sender
     * @param sender - The player who sent the command
     * @param command - The command the player sent
     */
    private void display(String sender, String[] command) {
        if (this.game == null) {
            this.players.get(sender).sendMessage("Game not in progress!");
        } else if (command.length != 2) {
            this.players.get(sender).sendMessage("Invalid command provided!");
        } else {
            String grid = this.game.getGrid(sender, command[1]);
            this.players.get(sender).sendMessage(grid);
        }
    }

    /**
     * Removes the sender from the game
     * @param sender - The player who sent the command
     */
    private void surrender(String sender) {
        if (this.game != null) {
            this.game.removePlayer(sender);
            this.players.get(sender).sendMessage("You lost");
            this.getNextTurn();
        } else {
            this.players.get(sender).sendMessage("Game not in progress!");
        }
    }

    /**
     * Logic for attempting to fire at a player. Will fail and alert
     * the sender if the checkValidMove method in Game returns false
     * @param sender - The client sending the fire request
     * @param message - The entire fire command
     */
    private void fire(String sender, String[] command) {
        boolean validMove = false;

        if (this.game == null) {
            this.players.get(sender).sendMessage("Game not in progress!");
            validMove = true;
        } else if (command.length == 4 && command[1].matches("^\\d+") && command[2].matches("\\d+")) {
            int x = Integer.parseInt(command[1]);
            int y = Integer.parseInt(command[2]);
            validMove = this.game.checkValidMove(sender, command[3]);
            if (validMove) {
                String result = this.game.makeMove(command[3], y, x);
                this.broadcast(result);
                this.getNextTurn();
            } 
        }
        if (!validMove) {
            this.players.get(sender).sendMessage("Invalid command provided!");
        }
    }

    /**
     * Removes the server as a listener from some subject and removes the
     * corresponding player from the game
     * @source - The subject that is being unsubscribed from
     */
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);

        for (Iterator<String> iterator = players.keySet().iterator(); iterator.hasNext();){
            String key = iterator.next();
            if (this.players.get(key) == source) {
                iterator.remove();
                String currentTurn = "";

                if (this.game != null) {
                    currentTurn = this.game.getTurn();
                    this.game.removePlayer(key);
                    if (this.game.isActive() == false) {
                        this.getNextTurn();
                    }
                }
                if (this.game != null && currentTurn.equals(key)) {
                    this.getNextTurn();
                }
            }
        }
    }
}
