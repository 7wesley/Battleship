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


                if (this.validHorizontalPath(grid, x, x - ship.getSize(), y)) {
                    possibleMoves.add(this.getPath(grid, x,  x- ship.getSize(), y));
                }
                if (this.validHorizontalPath(grid, x, x + ship.getSize(), y)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                if (this.validVerticalPath(grid, y, y - ship.getSize(), x)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                if (this.validPath(validVerticalPath, y, y + ship.getSize(), x)) {
                    possibleMoves.push(x - ship.getSize(), y);
                }
                
                int[] chosenMove = possibleMoves.get(generator.nextInt(possibleMoves.length + 1));
                int chosenY = chosenMove[1];
                int chosenX = chosenMove[0];

                if(chosenX == x) {
                    this.setShipVertical(grid, y, chosenY, x, ship);
                } else {
                    this.setShipHorizontall(grid, x, chosenX, y, ship);
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
            if (grid.getSquare(i, y) != Ship.Water) {
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
            if (grid.getSquare(i, x) != Ship.Water) {
                isValid = false;
            }
        }

        return isValid;
    }

    public int[][] setShipVertical(Grid grid, int startX, int endX, int y, Ship ship) {
        for (int i = startX; i < endX + 1; i++) {
                grid.setSquare(ship, i, y);
            }
        }

    public int[][] setShipHorizontal(Grid grid, int startY, int endY, int x, Ship ship) {
            for (int i = startY; i < endY + 1; i++) {
                    grid.setSquare(ship, i, x);
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
