package server;

/**
 * Logic for the game of BattleShip
 */
public class Game {
    //Possible change to array
    //"It has a Grid for each client."
    private Grid grid;

    public Game(int size) {
        this.grid = new Grid(size);
    }
}
