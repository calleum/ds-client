import java.io.File;
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
    private static File serverConfigFile = new File("ds-system.xml");

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
                jobs.add(createJobFromJobN(ev));
                sendMsg(fmtGetsCapable(jobs.get(0)));
                ev = recvMsg();
                sendMsg("OK");
                ev = recvMsg();
                servers = createServerFromData(serverConfigFile);
                sendMsg("OK");
                ev = recvMsg();
                sendMsg(scheduleJob(servers, jobs, SchedulerType.LRR));
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

    /*
     * TO DO: Extend to be a factory for different algorithms in part 2,
     * Currently uses LRR always.
     */

    public String scheduleJob(final ArrayList<Server> servers, final ArrayList<Job> job, SchedulerType algorithm) {
        if (!algorithm.equals(SchedulerType.LRR)) {
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

    public ArrayList<Server> createServerFromData(File fileName) {
        return ServerConfigHandler.createServersFromFile(fileName);
    }

    public ArrayList<Server> createServerFromData(final String serverResponse) {

        final ArrayList<Server> serversFromData = new ArrayList<Server>();
        final String[] serverResponseMultiLine = serverResponse.split("\\r?\\n");

        for (final String line : serverResponseMultiLine) {
            serversFromData.add(parseDataTokens(line.split("\\s+")));
        }
        return serversFromData;
    }

    public String fmtGetsCapable(final Job j) {
        return ("GETS Capable " + j.getNumCores() + " " + j.getMemory() + " " + j.getDiskSpace());
    }

    public Job createJobFromJobN(final String jobnResponse) {
        return parseJobTokens(jobnResponse.trim().split("\\s+"));
    }

    private Server parseDataTokens(String[] serverDataTokens) {
        Server s = new Server();
        s.setType(new ServerType(serverDataTokens[0]));
        s.setId(Integer.parseInt(serverDataTokens[1]));
        s.setLimit(Integer.parseInt(serverDataTokens[2]));
        s.setBootupTime(Integer.parseInt(serverDataTokens[3]));
        s.setHourlyRate(Integer.parseInt(serverDataTokens[4]));
        s.setNumCores(Integer.parseInt(serverDataTokens[5]));
        s.setMemory(Integer.parseInt(serverDataTokens[6]));
        s.setDiskSpace(Integer.parseInt(serverDataTokens[7]));
        return s;
    }

    public Job parseJobTokens(String[] serverJobTokens) {
        Job j = new Job();
        j.setId(Integer.parseInt(serverJobTokens[2]));
        j.setEstRuntime(Integer.parseInt(serverJobTokens[1]));
        j.setNumCores(Integer.parseInt(serverJobTokens[3]));
        j.setMemory(Integer.parseInt(serverJobTokens[4]));
        j.setDiskSpace(Integer.parseInt(serverJobTokens[5]));

        return j;
    }

    public boolean isConnected() {
        return this.connected;
    }

    public boolean disconnect() {
        return this.connected = false;
    }
}
