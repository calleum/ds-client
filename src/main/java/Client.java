import java.io.*;
import java.net.*;
import java.util.logging.Logger;

/**
 * Client
 */
public class Client {
    Socket socket;
    PrintStream out;
    BufferedReader in;

    private final static Logger LOG = Logger.getLogger(Client.class.getName());

    public Client(InetAddress address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.out = new PrintStream(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        LOG.info("Created new Client with address: " + address + "and port: " + port);
    }

    public void sendMsg(String msg) {

        LOG.info("Attempting to send message: " + msg + "to socket.");
        out.print(msg);
    }

    public String recvMsg() {
        LOG.info("Attempting to receive message");
        String msgRcvd;

        try {
            msgRcvd = in.readLine();
            msgRcvd.trim();
            LOG.info("Received message:" + msgRcvd);
            return msgRcvd;
        } catch (IOException e) {
            LOG.info("Unable to receive message due to :" + e);
        }
        return null;
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            LOG.info("Handling exception at close not implemented: " + e);
        }
    }
}
