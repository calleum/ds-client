import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler {
    Socket socket;
    PrintWriter out;
    BufferedReader in;

    private final static Logger LOG = Logger.getLogger(ConnectionHandler.class.getName());

    public ConnectionHandler(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        this.out = new PrintWriter(socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        LOG.info("Connected new Socket with address: " + address + " and port: " + port);
    }

    public String authMsg() {
        String am = CmdConstants.AUTH + " " + System.getProperty("user.name");
        return am;
    }

    public void sendMsg(String msg) {
        LOG.info("Attempting to send message: " + msg + " to socket.");
        out.print(msg);
        out.flush();
    }

    public String recvMsg() {
        char[] buffer = new char[8192];
        LOG.info("Attempting to receive message");
		try {
			in.read(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String msgRcvd = new String(buffer, 0, buffer.length) ;
        LOG.info("Received message :" + msgRcvd);
        return msgRcvd;
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

    public boolean emptyMsg(String ev) {
        return ev.isEmpty() || ev.startsWith("\n");
    }

}
