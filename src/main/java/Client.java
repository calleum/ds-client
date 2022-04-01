import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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

    /*
     * TO DO: Extend to be a factory for different algorithms in part 2,
     * Currently uses LRR always.
     */

    public String scheduleJob(final ArrayList<Server> servers, final ArrayList<Job> job, final String algorithm) {
        if (!algorithm.equals(CmdConstants.LRR)) {
            throw new UnsupportedOperationException("algorithm '" + algorithm + "' is not supported");
        }
        return runSchedulerLRR(servers, job);
    }

    private String runSchedulerLRR(final ArrayList<Server> servers, final ArrayList<Job> job) {
        ArrayList<Server> serversCopy = new ArrayList<Server>(servers);
        Collections.sort(serversCopy, new ServerComparator());
        final int largestServerCores = serversCopy.get(0).getNumCores();

        for (final Server s : serversCopy) {
            if (s.getNumCores() < largestServerCores) {
                serversCopy.remove(s);
            }
        }
        return allocateServerToJob(serversCopy.get(0), job.get(0));
    }

    String allocateServerToJob(final Server s, final Job j) {
        final String allocatedServerDetails = s.getType() + " " + s.getId();
        return "SCHD " + j.getId() + " " + allocatedServerDetails;
    }

    public ArrayList<Server> createServerFromData(final String serverResponse) {

        final ArrayList<Server> serversFromData = new ArrayList<Server>();

        final String[] serverResponseMultiLine = serverResponse.split("\\r?\\n");

        for (final String line : serverResponseMultiLine) {
            final String[] serverResponseLine = line.split("\\s+");
            serversFromData.add(new Server(
                    Integer.parseInt(serverResponseLine[1]),
                    new ServerType(serverResponseLine[0]),
                    Integer.parseInt(serverResponseLine[3]),
                    Integer.parseInt(serverResponseLine[4]),
                    Integer.parseInt(serverResponseLine[5]),
                    Integer.parseInt(serverResponseLine[6]),
                    Integer.parseInt(serverResponseLine[6]),
                    Integer.parseInt(serverResponseLine[6])));
        }

        return serversFromData;
    }

    public String sendGetsCapable(final Job j) {
        return ("GETS Capable " + j.getNumCores() + " " + j.getMemory() + " " + j.getDiskSpace());
    }

    public Job createJobFromJobN(final String job) {

        final String[] serverJobTokens = job.trim().split("\\s+");

        final Job j = new Job(Integer.parseInt(serverJobTokens[2]), Integer.parseInt(serverJobTokens[1]),
                Integer.parseInt(serverJobTokens[3]),
                Integer.parseInt(serverJobTokens[4]),
                Integer.parseInt(serverJobTokens[5]),
                Integer.parseInt(serverJobTokens[6]));

        return j;
    }

    private void handleEvent(String ev) {
        if (ev.startsWith(CmdConstants.JCPL)) {
            sendMsg(CmdConstants.REDY);
        } else if (ev.startsWith(CmdConstants.NONE) || emptyMsg(ev)) {
            connected = false;
            sendMsg(CmdConstants.QUIT);
        } else {

            if (ev.startsWith(CmdConstants.OK)) {
                sendMsg(CmdConstants.REDY);
            } else if (ev.startsWith("JOBN") || ev.startsWith("JOBP")) {
                jobs.add(createJobFromJobN(ev));
                sendMsg(sendGetsCapable(jobs.get(0)));
                ev = recvMsg();
                sendMsg("OK");
                ev = recvMsg();
                servers = createServerFromData(ev);
                sendMsg("OK");
                ev = recvMsg();
                sendMsg(scheduleJob(servers, jobs, CmdConstants.LRR));
                jobs.remove(0);
            }
        }
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

    public boolean isConnected() {
        return this.connected;
    }

    public boolean disconnect() {
        return this.connected = false;
    }
}
