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
        int size = 10;
        
        if (args.length < 1) {
            System.out.println("Usage: java BattleShipDriver <port> [size]");
        } else {
            try {
                port = Integer.parseInt(args[0]);
                if (args.length >= 2 && args[1] != null) {
                    size = Integer.parseInt(args[1]);
                }
                
                BattleServer server = new BattleServer(port);
                server.listen();
            } catch (IOException | NumberFormatException e) {
                System.out.println(e);
            }
        }
  
    }
}
