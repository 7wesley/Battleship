package server;

import java.io.IOException;

/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */

/**
 * Parses command line options, instantiates
 * a BattleServer, and calls its listen() method.
 */
public class BattleShipDriver {
    public static void main(String args[]) {
        int port;
        
        if (args.length != 1) {
            System.out.println("Usage: java BattleShipDriver <port>");
        } else {
            try {
                port = Integer.parseInt(args[0]);
                BattleServer server = new BattleServer(port);
                server.listen();
            } catch (IOException | IllegalArgumentException e) {
                System.out.println(e);
            }
        }
  
    }
}
