package client;
import java.io.IOException;
import java.util.Scanner;

/**
 * Parses command  line  options,  instantiates  a BattleClient,
 * reads messages from the keyboards, and sends them to client.
 * 
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
public class BattleDriver {
    /**
     * Main method for BattleDriver
     * @param args commandLine arguments
     */
    public static void main(String args[]) {

        String host;
        int port;
        String username;
        Scanner sc = new Scanner(System.in);
        String input;

        if (args.length != 3) {
            System.out.println("Usage: java BattleShipDriver <host> <port> <username>");
        } else {
            try {
                host = args[0];
                port = Integer.parseInt(args[1]);
                username = args[2];

                BattleClient client = new BattleClient(host, port, username);
                client.connect();

                while (client.isConnected()) {
                    input = sc.nextLine();
                    //append username
                    input = username + " " + input;
                    client.send(input);
                }

                sc.close();
            } catch (NumberFormatException | IOException e) {
                System.out.println(e);
            }
        }
    }
}
