package client;

import java.net.UnknownHostException;

/**
 * parses command  line  options,  instantiates  aBattleClient,
 * reads messages from the keyboards, and sends them to client.
 */
public class BattleDriver {
    public static void main(String args[]) {
        String host;
        int port;
        String username;

        if (args.length < 3) {
            System.out.println("Usage: java BattleShipDriver <host> <port> <username>");
        } else {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                username = args[2];

                BattleClient client = new BattleClient(host, port, username);
            } catch (NumberFormatException | UnknownHostException e) {
                System.out.println(e);
            }
        }
    }
}
