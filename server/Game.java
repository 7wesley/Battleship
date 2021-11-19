package server;

import java.util.ArrayList;
import java.util.Random;

import common.Ship;

/**
 * Logic for the game of BattleShip
 */
public class Game {
    //Possible change to array
    //"It has a Grid for each client."
    private Grid[] gridsList;
    private int turnIndex = 0;
    private int gridSize;

    public Game(int gridSize, String[] players) {
        if (gridSize > 10 || gridSize < 5) {
            System.out.println("Invalid grid size provided");
            System.exit(1);
        }
        this.gridSize = gridSize;
        gridsList = new Grid[players.length];
        for (int i = 0; i < players.length; i++) {
            gridsList[i] = new Grid(this.gridSize);
        }
        this.placeShipsRandomly();
    }

    public void placeShipsRandomly() {
        Random generator = new Random();
        int x;
        int y;
        Ship ship;
        ArrayList<int[]> possibleMoves = new ArrayList<int[]>();
        int[][] path;

        for (Grid grid: gridsList) {
            for (int i = 0; i < this.getNumberShips(); i++) {
                ship = Ship.getRandomShip();
                x = generator.nextInt(this.gridSize + 1);
                y = generator.nextInt(this.gridSize + 1);

                if (this.validPath(grid, x, x- ship.getSize(), y)) {
                    possibleMoves.add(this.getPath(grid, x,  x- ship.getSize(), y));
                }
                if (this.validPath(move)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                if (this.validPath(move)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                if (this.validPath(move)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                
                path = possibleMoves[generator.nextInt(possibleMoves.length)];

                for (int j = 0; j < possibleMoves.length; j++) {
                    grid.setSquare(ship, possibleMoves[i][0], possibleMoves[i][0]);
                    grid.setSquare(ship, possibleMoves[i][0], possibleMoves[i][1]);
                }
            }
            
        }
    }

    public boolean validPath(Grid grid, int startingPoint, int endPoint, int fixedCordinate) {
        boolean isValid = true;
        if (endPoint > this.gridSize) {
            isValid = false;
        }

        for (int i = startingPoint; i < endPoint + 1; i++) {
            if (grid.getSquare(i, fixedCordinate) != Ship.Water) {
                isValid = false;
            }
        }

        return isValid;
    }

    public int[][] getPath(Grid grid, int startingPoint, int endPoint, int fixedCordinate) {
        int[][] path = new int[endPoint - startingPoint][2];

        for (int i = 0; i < path.length; i++) {
            path[i][0] = startingPoint + i;
            path[i][1] 
        }

        for (int i = startingPoint; i < endPoint + 1; i++) {
           path[]
        }

        return isValid;
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


    /**
     * Attempts to hit ship and returns true if successful
     * @param username
     * @param x
     * @param y
     * @return
     */
    public boolean validHit(String username, int x, int y) {
        Boolean validMove = false;

        for (Grid grid: gridsList) {
            if (grid.getUsername() == username) {
                if (grid.getSquare(x, y) == Ship.Water) {
                    grid.setSquare(Ship.Miss, x, y);
                    System.out.println("Miss!");
                    validMove = true;
                } else if (grid.getSquare(x, y).isShip()) {
                    grid.setSquare(Ship.Hit, x, y);
                    System.out.println("Hit!");
                    validMove = true;
                } else {
                    System.out.println("Invalid move");
                    validMove = false;
                }
            }
        }

        if (!username.equals(this.gridsList[turnIndex].getUsername())) {
            validMove = false;
        }
        return validMove;
    }

    public Grid getGrid(String username) {
        Grid wantedGrid = null;

        for (Grid grid: gridsList) {
            if (grid.getUsername() == username) {
                wantedGrid = grid;
            }
        }
        return wantedGrid;
    }

    public void removePlayer(String username) {
        for (Grid grid: this.gridsList) {
            if (grid.getUsername().equals(username)) {
                grid = null;
            }
        }
    }

    public String getNextMove() {
        Grid player = this.gridsList[turnIndex];

        while (player.hasLost()) {
            this.turnIndex++;
            if (this.turnIndex > this.gridsList.length) {
                this.turnIndex = 0;
            }
            player = this.gridsList[turnIndex];
        }

        return player.getUsername();
    }



    public boolean isActive() {
        int lossCounter = 0;
        boolean isActive = true;

        for (Grid grid: gridsList) {
            if (grid.hasLost()) {
                lossCounter++;
            }
        }
        if (lossCounter == this.gridsList.length - 1) {
            isActive = false;
        }

        return isActive;
    }

    public String getWinner() {
        String winner = "";

        for (Grid grid: gridsList) {
            if (!grid.hasLost()) {
                winner = grid.getUsername();
            }
        }

        return winner;
    }
}
