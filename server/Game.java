package server;

import common.Ship;

/**
 * Logic for the game of BattleShip
 */
public class Game {
    //Possible change to array
    //"It has a Grid for each client."
    private Grid[] gridsList;
    private int turn;

    public Game(int gridSize, int numPlayers) {
        gridsList = new Grid[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            gridsList[i] = new Grid(gridSize);
        }
    }


    public void setHit(int playerPosition, int x, int y) {
        gridsList[playerPosition].setSquare(Ship.Hit, x, y);
    }

    public void getNextMove() {
        
    }



    public boolean isActive() {
        return true;
    }
}
