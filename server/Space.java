package server;

import java.util.Random;

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
 
    private String symbol;
    private int size;

    /**
     * Initialize CardType with value.
     * @param value - value associated with enum
     */
    Space(String symbol, int size) {
        this.symbol = symbol;
        this.size = size;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isShip() {
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

    public static Space getRandomShip() {
        Random generator = new Random();

        //Bottom 4 are not ships
        return values()[generator.nextInt(values().length - 4)];
    }

}