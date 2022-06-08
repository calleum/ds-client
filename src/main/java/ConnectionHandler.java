import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.logging.Logger;

public class ConnectionHandler {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    private final static Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(final String address, final int port) throws IOException {
        this.socket = new Socket(address, port);
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        LOG.info("Connected new Socket with address: " + address + " and port: " + port);
    }

    public String authMsg() {
        final String am = CmdConstants.AUTH + " " + System.getProperty("user.name");
        return am;
    }

    public void sendMsg(final String msg) {
        LOG.info("Attempting to send message: " + msg + " to socket.");
        out.print(msg);
        out.flush();
    }

    /**
     * Parse the msg split by whitespace to
     * retrieve the incoming data message
     * size.
     *
     * @param dataMsg deserialised message from ds-server
     */
    public int dataMsg(String dataMsg) {
        String[] toks = dataMsg.trim().split("\\s+");

        if (toks.length != 3) {
            throw new InputMismatchException(dataMsg);
        }

        return Integer.parseInt(toks[1]) * Integer.parseInt(toks[2]);
    }

    public void listSrvJobs() {
        sendMsg(CmdConstants.LSTJ + " xlarge 1");
        recvMsg();
        sendMsg(CmdConstants.OK);
        recvMsg();
        sendMsg(CmdConstants.OK);
        recvMsg();
    }

    /**
     * call the backing method recvMsg(int bufsize) after setting the
     * buffer size to the default value via polymorphism
     *
     * @return msg message received from ds-server
     */
    public String recvMsg() {
        int bufsize = 8192;
        return recvMsg(bufsize);
    }

    public String recvMsg(int bufsize) {
        final char[] buffer = new char[bufsize];
        LOG.info("Attempting to receive message with bufsize=" + bufsize);
        try {
            in.read(buffer);
        } catch (final IOException e) {
            e.printStackTrace();
        }

        final String msgRcvd = new String(buffer, 0, buffer.length);
        if (msgRcvd.contains("ERR")) {
            stopConnection();
            System.exit(1);
        }

        if (msgRcvd.contains("\n")) {
            LOG.info("Received message :" + msgRcvd.substring(0, msgRcvd.indexOf("\n")
                    - 1));
        } else if (msgRcvd.contains("\0")) {
            LOG.info("Received message :" + msgRcvd.substring(0, msgRcvd.indexOf("\0")
                    - 1));
        } else {
            LOG.info("Received message :" + msgRcvd);
        }
        return msgRcvd;
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (final IOException e) {
            LOG.info("Handling exception at close not implemented: " + e);
        }
    }

    public boolean emptyMsg(final String ev) {
        return ev.isEmpty() || ev.startsWith("\n");
    }

}
