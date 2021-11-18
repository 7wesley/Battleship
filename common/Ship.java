package common;

public enum Ship {
    Carrier("C", 5),
    BattleShip("B", 3),
    Cruiser("R", 2),
    Submarine("S", 3),
    Destroyer("D", 5),
    Hit("H", 0),
    Miss("M", 0);

    private String symbol;
    private int size;

    /**
     * Initialize CardType with value.
     * @param value - value associated with enum
     */
    Ship(String symbol, int size) {
        this.symbol = symbol;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

}