import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * Client
 */
public class Client {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());

    ConnectionHandler con; 

    public Client(InetAddress address, int port) throws IOException {
       this.con = new ConnectionHandler(address, port);
    }

}
