package server;

import common.Ship;

/**
 * Logic for a single board of Battleship.
 */
public class Grid {
    private Ship[][] grid;

    public Grid(int size) {
        grid = new Ship[size][size]; 
    }

    public void setSquare(Ship ship, int x, int y) {
        this.grid[x][y] = ship;
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
