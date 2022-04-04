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
    ArrayList<Server> servers = new ArrayList<Server>();
    ArrayList<Job> jobs = new ArrayList<Job>();

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
            sendMsg(CmdConstants.QUIT);
        } else {

            if (ev.startsWith(CmdConstants.OK)) {
                sendMsg(CmdConstants.REDY);
            } else if (ev.startsWith("JOBN") || ev.startsWith("JOBP")) {
                jobs.add(createJobFromData(ev));
                if (servers.isEmpty()) {
                    sendMsg(CmdConstants.GETS_ALL);
                    ev = recvMsg();
                    sendMsg("OK");
                    servers = createServersFromData(recvMsg());
                    sendMsg("OK");
                    ev = recvMsg();
                }
                sendMsg(Scheduler.scheduleJob(servers, jobs, SchedulerType.LRR));
                jobs.remove(0);
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
        return this.connected = false;
    }
}
