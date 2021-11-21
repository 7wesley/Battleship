package server;

import common.Ship;

/**
 * Logic for a single board of Battleship.
 */
public class Grid {
    private Ship[][] grid;
    private String username;

    public Grid(int size, String username) {
        this.grid = new Ship[size][size]; 
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = Ship.Water;
            }
        }
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public Ship getSquare(int x, int y) {
        if (x >= this.grid.length || x < 0 || y >= this.grid.length || y < 0) {
            return Ship.Unknown;
        } 
        return this.grid[x][y];
    }

    public void setSquare(Ship ship, int x, int y) {
        this.grid[x][y] = ship;
    }   

    public boolean hasLost() {
        boolean hasLost = true;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j].isShip()) {
                    hasLost = false;
                }
            }
        }

        return hasLost;
    }

    @Override
    public String toString() {
        String text = " ";
        for (int i = 0; i < this.grid.length; i++) {
            text += "   " + i;
        }
        for (int i = 0; i < this.grid.length; i++) {
            text += "\n  " + "+---".repeat(this.grid.length) + "+";
            text += "\n" + i + " | ";
            for (int j = 0; j < this.grid[i].length; j++) {
                text += this.grid[i][j].getSymbol() + " | ";
            }
        }
        text += "\n  " + "+---".repeat(this.grid.length) + "+";
        return text;
    }
}
