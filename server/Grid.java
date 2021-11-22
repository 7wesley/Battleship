package server;

/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */

/**
 * Logic for a single board of Battleship.
 */
public class Grid {
    private Space[][] grid;
    private String username;

    public Grid(int size, String username) {
        this.grid = new Space[size][size]; 
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = Space.Water;
            }
        }
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public Space getSquare(int x, int y) {
        if (x >= this.grid.length || x < 0 || y >= this.grid.length || y < 0) {
            return Space.Unknown;
        } 
        return this.grid[y][x];
    }

    public void setSquare(Space ship, int x, int y) {
        this.grid[y][x] = ship;
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

    public String display(boolean myGrid) {
        String text = " ";
        for (int i = 0; i < this.grid.length; i++) {
            text += "   " + i;
        }
        for (int i = 0; i < this.grid.length; i++) {
            text += "\n  " + "+---".repeat(this.grid.length) + "+";
            text += "\n" + i + " | ";
            for (int j = 0; j < this.grid[i].length; j++) {
                if (myGrid || !this.grid[i][j].isShip()) {
                    text += this.grid[i][j].getSymbol() + " | ";
                } else {
                    text += Space.Water.getSymbol() + " | ";
                }
            }
        }
        text += "\n  " + "+---".repeat(this.grid.length) + "+";
        return text;
    }

}
