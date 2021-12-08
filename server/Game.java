package server;

/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;

import common.ConnectionAgent;

/**
 * Logic for the game of BattleShip
 */
public class Game {
    //Possible change to array
    //"It has a Grid for each client."
    private ArrayList<Grid> gridsList;
    private int turnIndex = 0;
    private int gridSize;

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

    public void setShipHorizontal(Grid grid, int startX, int endX, int y, Space ship) {
        for (int i = startX; i < endX; i++) {
            grid.setSquare(ship, i, y);
        }
    }

    public void setShipVertical(Grid grid, int startY, int endY, int x, Space ship) {
        for (int i = startY; i < endY; i++) {
            grid.setSquare(ship, x, i);
        }
    }

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

    public boolean checkValidMove(String sender, String target) {
        System.out.println("sender: " + sender + " target: " + target);
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
     * Attempts to hit ship and returns true if successful
     * @param username
     * @param x
     * @param y
     * @return
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

    //lose or surrender
    public void removePlayer(String username) {
        Grid grid;

        for (Iterator<Grid> iterator = gridsList.iterator(); iterator.hasNext();) {
            grid = iterator.next();
            if (grid.getUsername().equals(username)) {
                iterator.remove();
            }
        }
    }

    public String getNextMove() {
        if (this.turnIndex >= this.gridsList.size()) {
            this.turnIndex = 0;
        }

        Grid player = this.gridsList.get(turnIndex);

        return player.getUsername();
    }



    public boolean isActive() {
        boolean isActive = true;
        if (gridsList.size() == 1) {
            isActive = false;
        }
    
        return isActive;
    }

    public String getWinner() {
        //Only 1 player left at this point
        return gridsList.get(0).getUsername();
    }

    public String getTurn() {
        return this.gridsList.get(this.turnIndex).getUsername();
    }

}
