import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler {
    Socket socket;

    DataOutputStream out;
    DataInputStream in;

    private final static Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.out = new DataOutputStream(socket.getOutputStream());
        this.in = new DataInputStream(socket.getInputStream());

        LOG.info("Connected new Socket with address: " + address + " and port: " + port);
    }

    public String authMsg() {
        String am = CmdConstants.AUTH + " " + System.getProperty("user.name");
        return am;
    }

    public void sendMsg(String msg) {

        LOG.info("Attempting to send message: " + msg + " to socket.");
        try {
            out.write(msg.getBytes());
        } catch (IOException e) {
            LOG.info("Handling exception sendMsg not implemented: " + e);
        }
    }

    public String recvMsg() {
        String msgRcvd;

        try {
            LOG.info("Attempting to receive message");
            msgRcvd = in.readLine();
            LOG.info("Received message :" + msgRcvd);
            return msgRcvd;
        } catch (IOException e) {
            LOG.info("Unable to receive message due to : " + e);
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
