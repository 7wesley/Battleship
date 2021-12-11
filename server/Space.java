package server;

import java.util.Random;

/**
 * A space that can be placed on a grid
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
public enum Space {
    Carrier("C", 2),
    BattleShip("B", 3),
    Cruiser("R", 4),
    Submarine("S", 3),
    Destroyer("D", 5),
    Hit("H", 0),
    Miss("O", 0),
    Water(" ", 0),
    Unknown("U", 0);
 
    /** The symbol associated with the space */
    private String symbol;
    /** The size associated with the space */
    private int size;

    /**
     * Constructor for Space
     * @param symbol - The symbol associated with the space
     * @param size - The size associated with the space
     */
    Space(String symbol, int size) {
        this.symbol = symbol;
        this.size = size;
    }

    /**
     * Gets the symbol associated with the space
     * @return the symbol associated with the space
     */
    protected String getSymbol() {
        return this.symbol;
    }

    /**
     * Gets the size associated with the space
     * @return the size associated with the space
     */
    protected int getSize() {
        return this.size;
    }

    /**
     * Determines if this space is a ship
     * @return true if it a ship, else false
     */
    protected boolean isShip() {
        boolean isShip = false;
        switch (this) {
            case Carrier:
               isShip = true;
               break;
            case BattleShip:
                isShip = true;
                break;
            case Cruiser:
                isShip = true;
                break;
            case Destroyer:
                isShip = true;
                break;
            case Submarine:
                isShip = true;
                break;
            default:
                isShip = false;
                break;
        }
        return isShip;
    }

    /**
     * Randomly gets a Space
     * @return a random Space
     */
    protected static Space getRandomShip() {
        Random generator = new Random();

        //Bottom 4 are not ships
        return values()[generator.nextInt(values().length - 4)];
    }

}