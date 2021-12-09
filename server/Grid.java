package server;

/**
 * Logic for a single board of Battleship.
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
public class Grid {
    /** A 2D array of Space enums representing a grid */
    private Space[][] grid;
    /** The username of this grid */
    private String username;

    /**
     * Constructor for Grid
     * @param size - The size of the grid to be created
     * @param username - The username of the grid
     */
    public Grid(int size, String username) {
        this.grid = new Space[size][size]; 
        for (int i = 0; i < this.grid.length; i++) {
            for (int j = 0; j < this.grid[i].length; j++) {
                this.grid[i][j] = Space.Water;
            }
        }
        this.username = username;
    }

    /**
     * Gets the username of the grid
     * @return the username of the grid
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Gets the Space Enum at a specific x and y coordinate
     * @param x - The x coordinate
     * @param y - The y coordinate
     * @return the Enum found at the passed in coordinates
     */
    public Space getSquare(int x, int y) {
        if (x >= this.grid.length || x < 0 || y >= this.grid.length || y < 0) {
            return Space.Unknown;
        } 
        return this.grid[y][x];
    }

    /**
     * Sets a square at the passed in coordinates to a specific space
     * @param space - The Enum to be placed at the specific coordinates
     * @param x - The x coordinate
     * @param y - The y coordinate
     */
    public void setSquare(Space space, int x, int y) {
        this.grid[y][x] = space;
    }   

    /**
     * Determines if this grid has any more ships
     * @return true if the grid has no ships, else false
     */
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

    /**
     * Formats the grid and displays it in a string
     * @param myGrid - If the user is displaying their own grid
     * @return the formatted grid
     */
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
