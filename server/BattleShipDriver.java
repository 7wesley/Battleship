package server;

import java.io.IOException;

/**
 * Parses command line options, instantiates
 * a BattleServer, and calls its listen() method.
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 12/8/2021
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
