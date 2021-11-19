package server;

import common.Ship;

/**
 * Logic for a single board of Battleship.
 */
public class Grid {
    private Ship[][] grid;
    private String username;

    public Grid(int size) {
        grid = new Ship[size][size]; 
    }

    public String getUsername() {
        return this.username;
    }

    public Ship getSquare(int x, int y) {
        if (x > this.grid.length || x < 0 || y > this.grid.length || y < 0) {
            return null;
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
        String text = "";
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                text += grid[i][j].getSymbol();
            }
            text += "\n";
        }
        return text;
    }
}
