import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Client
 */
public class Client extends ConnectionHandler {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    private boolean connected;
    private Job currentJob;

    ArrayList<Server> servers = new ArrayList<Server>();

    public Client(final String address, final int port) throws IOException {
        super(address, port);
    }

    public void run() {
        try {
            makeHandshake();
        } catch (Exception e) {
            LOG.info("Exception" + e);
        }

        while (isConnected()) {
            handleEvent(recvMsg());
        }
    }

    private void handleEvent(String ev) {
        if (ev.startsWith(CmdConstants.JCPL)) {
            sendMsg(CmdConstants.REDY);
        } else if (ev.startsWith(CmdConstants.NONE) || emptyMsg(ev)) {
            disconnect();
        } else {

            if (ev.startsWith(CmdConstants.OK)) {
                sendMsg(CmdConstants.REDY);
            } else if (ev.startsWith(CmdConstants.JOBP) || ev.startsWith(CmdConstants.JOBN)) {
                currentJob = createJobFromData(ev);
                if (servers.isEmpty()) {
                    sendMsg(CmdConstants.GETS_ALL);
                    ev = recvMsg();
                    sendMsg(CmdConstants.OK);
                    servers = createServersFromData(recvMsg());
                    sendMsg(CmdConstants.OK);
                    ev = recvMsg();
                }
                if (currentJob == null) {
                    LOG.info("Job was not created, disconnecting from server.");
                    disconnect();
                }
                sendMsg(Scheduler.scheduleJob(servers, currentJob, SchedulerType.LRR));
                currentJob = null;
            }
        }
    }

    public void makeHandshake() throws IOException {
        LOG.info("Initiating Handshake");
        sendMsg(CmdConstants.HELO);
        final String msg = recvMsg();
        LOG.info(msg);
        final String auth = authMsg();
        LOG.info(auth);
        sendMsg(authMsg());
        this.connected = true;
    }

    public Server getLargestServerFromFile(File fileName) {
        return ServerUtils.getLargestServer(fileName);
    }

    public ArrayList<Server> createServersFromData(File fileName) {
        return ServerUtils.createServersFromFile(fileName);
    }

    public ArrayList<Server> createServersFromData(final String serverResponse) {
        return ServerUtils.createServersFromResponse(serverResponse);
    }

    public String fmtGetsCapable(final Job j) {
        return ("GETS Capable " + j.getNumCores() + " " + j.getMemory() + " " + j.getDiskSpace());
    }

    public Job createJobFromData(final String jobnResponse) {
        return JobUtils.createJobFromJobN(jobnResponse);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean disconnect() {
        sendMsg(CmdConstants.QUIT);
        return this.connected = false;
    }
}
