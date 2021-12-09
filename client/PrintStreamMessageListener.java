package client;

/**
 * @author Wesley Miller, Justin Clifton
 * @version 11/22/2021
 */
import java.io.PrintStream;

import common.MessageListener;
import common.MessageSource;

/**
 * Responsible for writing messages to a PrintStream
 * 
 * OBSERVER
 */
public class PrintStreamMessageListener implements MessageListener {
    private PrintStream out;

    public PrintStreamMessageListener(PrintStream out) {
        this.out = out;
    }

    public void messageReceived(String message, MessageSource source) {
        out.println(message);
    }

    public void sourceClosed(MessageSource source) {
        source.removeMessageListener(this);
    }

}
