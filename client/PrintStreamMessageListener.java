package client;
import java.io.PrintStream;
import common.MessageListener;
import common.MessageSource;

/**
 * Responsible for writing messages to a PrintStream
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
public class PrintStreamMessageListener implements MessageListener {

    /** The printstream used for output */
    private PrintStream out;

    /**
     * Constructor for PrintStreamMessageListener
     * @param out The printstream used for output
     */
    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;
    }

    /**
     * Used to print a message when it is recieved. 
     */
    public void messageReceived(String message, MessageSource source) {
        out.println(message);
    }

    /**
     * Used to deregister listener
     * @param source Not used.
     */
    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }

}
