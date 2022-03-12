import java.io.*;
import java.util.logging.Logger;

/**
 * Client
 */
public class Client extends ConnectionHandler {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    ServerConfig serverConfig = new ServerConfig();

    public Client(String address, int port) throws IOException {
        super(address, port);
    }

    public void makeHandshake() throws IOException {
        LOG.info("Initiating Handshake");
        sendMsg(authMsg());
        String server_response = recvMsg();
        if (server_response.equals(CmdConstants.OK)) {
            LOG.info("Handshake Completed");
        } else {
            throw new Error("Error in handshake sequence");
        }
    }

    private void handleEvent(String ev) {
        if (ev.equals(CmdConstants.JOBN)) {
            LOG.info("unimplemented!");
        } else if (ev.equals(CmdConstants.JCPL)) {
            LOG.info("unimplemented!");
        } else if (ev.equals(CmdConstants.RESF)) {
            LOG.info("unimplemented!");
        } else if (ev.equals(CmdConstants.NONE)) {
            LOG.info("unimplemented!");
        } else {
            LOG.info("unimplemented!");
            
        }
    }

    public void run() {
        while (true) {
            sendMsg(CmdConstants.REDY);
            handleEvent(recvMsg());

        }
    }
}
