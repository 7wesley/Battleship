package server;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import common.ConnectionAgent;

/**
 * Logic for the game of BattleShip
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 12/8/2021
 */
public class Game {
    /** The players in the game respresented by grids */
    private ArrayList<Grid> gridsList;
    /** The index of whose turn it is */
    private int turnIndex = 0;
    /** The size of each player's grid */
    private int gridSize;

    /**
     * Constructor for Game
     * @param gridSize - The size of each player's grid
     * @param players - The players that will be part of the game
     */
    public Game(int gridSize, Hashtable<String, ConnectionAgent> players) {
        if (gridSize > 10 || gridSize < 5) {
            System.out.println("Invalid grid size provided");
            System.exit(1);
        }
        this.gridSize = gridSize;
        gridsList = new ArrayList<Grid>();
        for (String username: players.keySet()) {
            gridsList.add(new Grid(this.gridSize, username));
        }
        this.placeShipsRandomly();
    }

    /**
     * Logic for placing ships randomly on each player's grid
     */
    public void placeShipsRandomly() {
        Random generator = new Random();
        int x;
        int y;
        Space ship;
        int numShips;

        for (Grid grid : gridsList) {
            numShips = this.getNumberShips();
            for (int i = 0; i < numShips; i++) {

                boolean foundValidPlacement = false;
                while (!foundValidPlacement) {
                    ship = Space.getRandomShip();
                    x = generator.nextInt(this.gridSize + 1);
                    y = generator.nextInt(this.gridSize + 1);

                    int direction = generator.nextInt(4);

                    switch (direction) {
                    case 0:
                        if (this.validHorizontalPath(grid, x - ship.getSize(), x, y)) {
                            foundValidPlacement = true;
                            this.setShipHorizontal(grid, x - ship.getSize(), x, y, ship);
                        }
                        break;
                    case 1:
                        if (this.validHorizontalPath(grid, x, x + ship.getSize(), y)) {
                            foundValidPlacement = true;
                            this.setShipHorizontal(grid, x, x + ship.getSize(), y, ship);
                        }
                        break;
                    case 2:
                        if (this.validVerticalPath(grid, y - ship.getSize(), y, x)) {
                            foundValidPlacement = true;
                            this.setShipVertical(grid, y - ship.getSize(), y, x, ship);
                        }
                        break;
                    case 3:
                        if (this.validVerticalPath(grid, y, y + ship.getSize(), x)) {
                            foundValidPlacement = true;
                            this.setShipVertical(grid, y, y + ship.getSize(), x, ship);
                        }
                        break;
                    }

                }
            }

        }
    }

    /**
     * Determines if it is possible to place a ship in the coordinates
     * passed in
     * @param grid - The grid being checked for validation
     * @param startX - The starting x coordinate
     * @param endX - The ending x coordinate
     * @param y - The constant y coordinate
     * @return true if ship can be placed, else false
     */
    public boolean validHorizontalPath(Grid grid, int startX, int endX, int y) {
        boolean isValid = true;
        if (endX > this.gridSize) {
            isValid = false;
        }

        for (int i = startX; i < endX + 1; i++) {
            if (grid.getSquare(i, y) != Space.Water) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Determines if it is possible to place a ship in the coordinates
     * passed in
     * @param grid - The grid being checked for validation
     * @param startY - The starting y coordinate
     * @param endY - The ending y coordinate
     * @param x - The constant x coordinate
     * @return true if ship can be placed, else false
     */
    public boolean validVerticalPath(Grid grid, int startY, int endY, int x) {
        boolean isValid = true;
        if (endY > this.gridSize) {
            isValid = false;
        }

        for (int i = startY; i < endY + 1; i++) {
            if (grid.getSquare(x, i) != Space.Water) {
                isValid = false;
            }
        }

        return isValid;
    }

    /**
     * Sets a ship horizontally using the coordinates passed in
     * @param grid - The grid to place the ship on
     * @param startX - The starting x coordinate
     * @param endX - The ending x coordinate
     * @param y - The constant y coordinate
     * @param ship - The ship being placed
     */
    public void setShipHorizontal(Grid grid, int startX, int endX, int y, Space ship) {
        for (int i = startX; i < endX; i++) {
            grid.setSquare(ship, i, y);
        }
    }

    /**
     * Sets a ship vertically using the coordinates passed in
     * @param grid - The grid to place the ship on
     * @param startY - The starting y coordinate
     * @param endY - The ending y coordinate
     * @param x - The constant x coordinate
     * @param ship - The ship being placed
     */
    public void setShipVertical(Grid grid, int startY, int endY, int x, Space ship) {
        for (int i = startY; i < endY; i++) {
            grid.setSquare(ship, x, i);
        }
    }

    /**
     * Determines the number of ships that will be placed on a
     * player's board based on the grid size.
     * @return - The number of ships to be placed
     */
    public int getNumberShips() {
        int lowerBound;
        int upperBound;
        Random generator = new Random();

        if (this.gridSize > 7) {
            lowerBound = (int) Math.floor(this.gridSize / 2) - 1;
            upperBound = (int) Math.floor(this.gridSize / 2) + 1;
        } else {
            lowerBound = (int) Math.floor(this.gridSize / 2) - 1;
            upperBound = (int) Math.floor(this.gridSize / 2);
        }
        return generator.nextInt((upperBound - lowerBound + 1)) + lowerBound;
    }

    /**
     * Determines if a move from one player to another is valid
     * @param sender - The person attempting to make the move
     * @param target - The person that is being attacked
     * @return true if the move is valid, else false
     */
    public boolean checkValidMove(String sender, String target) {
        boolean validMove = true;
        boolean playerFound = false;

        if (!sender.equals(this.gridsList.get(turnIndex).getUsername())) {
            validMove = false;
        } 

        for (Grid grid: gridsList) {
            if (grid.getUsername().equals(target)) {
                playerFound = true;
            }
        }
        if (!playerFound) {
            validMove = false;
        }
        if (sender.equals(target)) {
            validMove = false;
        } 
        
        return validMove;
    }
    
    /**
     * Attempts to hit a player's grid at a passed in coordinate, and
     * returns a string based on the result
     * @param target - The person being attacked
     * @param x - The x coordinate of the move
     * @param y - The y coordinate of the move
     * @return a string containing the result of the attack
     */
    public String makeMove(String target, int x, int y) {
        Grid grid;
        String message = "";

        for (Iterator<Grid> iterator = gridsList.iterator(); iterator.hasNext();) {
            grid = iterator.next();

            if (grid.getUsername().equals(target)) {
                if (grid.getSquare(x, y) == Space.Water) {
                    grid.setSquare(Space.Miss, x, y);
                    message = this.gridsList.get(turnIndex).getUsername() + " misses " + target;
                    this.turnIndex++;
                } else if (grid.getSquare(x, y).isShip()) {
                    grid.setSquare(Space.Hit, x, y);
                    message = this.gridsList.get(turnIndex).getUsername() + " hits " + target;
                    if (grid.hasLost()) {
                        iterator.remove();
                    }
                    this.turnIndex++;
                } else {
                    System.out.println("Invalid move");
                }
            }
        }
        return message;
    }

    /**
     * Gets the grid of a specific player. Only the sender can view
     * their own ships.
     * @param sender - The user attempting to display a grid
     * @param username - The username of the grid that is attempting to
     * be displayed
     * @return a string containing the grid of the desired user
     */
    public String getGrid(String sender, String username) {
        boolean myGrid = sender.equals(username);
        String formattedGrid = "";

        for (Grid grid: gridsList) {
            if (grid.getUsername().equals(username)) {
                formattedGrid += grid.display(myGrid);
            }
        }
        return formattedGrid;
    }

    /**
     * Removes a player from the game
     * @param username - The name of the player to be removed
     */
    public void removePlayer(String username) {
        Grid grid;

        for (Iterator<Grid> iterator = gridsList.iterator(); iterator.hasNext();) {
            grid = iterator.next();
            if (grid.getUsername().equals(username)) {
                iterator.remove();
            }
        }
    }

    /**
     * Finds out whose turn it is next
     * @return The username of whose turn it currently is
     */
    public String getNextMove() {
        if (this.turnIndex >= this.gridsList.size()) {
            this.turnIndex = 0;
        }

        Grid player = this.gridsList.get(turnIndex);

        return player.getUsername();
    }

    /**
     * Determines if a game still has more than one player in it
     * @return true if there is more than one player, else false
     */
    public boolean isActive() {
        boolean isActive = true;
        if (gridsList.size() <= 1) {
            isActive = false;
        }
    
        return isActive;
    }

    /**
     * Find the winner of the game, which will always be the
     * last remaining player
     * @return the last remaining player
     */
    public String getWinner() {
        //Only 1 player left at this point
        return gridsList.get(0).getUsername();
    }

    /**
     * Get the username of whose turn it currently is
     * @return the username of whose turn it currently is
     */
    public String getTurn() {
        return this.gridsList.get(this.turnIndex).getUsername();
    }

    /**
     * Determines if there are still players left in the game
     * @return - true if there are 1 players or more, else false
     */
    public boolean hasPlayers() {
        return this.gridsList.size() >= 1;
    }
}
