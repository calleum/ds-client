import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Client
 */
public class Client extends ConnectionHandler {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    private boolean connected = true;
    private Job currentJob;
    private SchedulerType schedulerType;
    private Server largestServer;

    ArrayList<Server> servers = new ArrayList<Server>();

    public Client(final String address, final int port) throws IOException {
        super(address, port);
    }

    public void run(String[] args) {
        if (args.length > 1 && args[1].equals("-a".toString())) {
            setSchedulerType(args[1]);
        } else {
            setSchedulerType("LRR");
        }
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
                if (currentJob == null) {
                    LOG.info("Job was not created, disconnecting from server.");
                    disconnect();
                    return;
                }
                sendMsg(fmtGetsCapable(currentJob));
                ev = recvMsg();
                sendMsg(CmdConstants.OK);
                servers = createServersFromData(recvMsg());
                sendMsg(CmdConstants.OK);
                ev = recvMsg();
                sendMsg(scheduleJob(servers, currentJob));
                currentJob = null;
            }
        }
    }

    private void makeHandshake() throws IOException {
        if (!connected) {
            return;
        }
        LOG.info("Initiating Handshake");
        sendMsg(CmdConstants.HELO);
        final String msg = recvMsg();
        LOG.info(msg);
        final String auth = authMsg();
        LOG.info(auth);
        sendMsg(authMsg());
        this.connected = true;
    }

    private HashMap<Server, Integer> getServerWaitTime(ArrayList<Server> servers) {
        HashMap<Server, Integer> srvWaits = new HashMap<>();
        for (Server server : servers) {
            sendMsg(CmdConstants.EJWT + " " + server.getType() + " " + server.getId());
            Integer waitTime = Integer.parseInt(recvMsg());
            srvWaits.put(server, waitTime);
        }
        return srvWaits;
    }

    private void setSchedulerType(String schedulerArg) throws IllegalArgumentException {
        try {
            schedulerType = SchedulerType.valueOf(schedulerArg.toUpperCase());
            System.out.println(schedulerType.name());
        } catch (IllegalArgumentException e) {
            System.err.println("Setting scheduler type: " + schedulerArg + " failed with exception: " + e);
            e.printStackTrace();
            disconnect();
        }
    }

    private String scheduleJob(ArrayList<Server> servers, Job job) {
        String schedulerResult = new String();
        if (this.schedulerType.equals(SchedulerType.LRR)) {
            schedulerResult = Scheduler.runSchedulerLRR(servers, job, getLargestServer());
        } else if (this.schedulerType.equals(SchedulerType.STCF)) {
            HashMap<Server, Integer> srvWaits = getServerWaitTime(servers);
            schedulerResult = Scheduler.runSchedulerSTCF(servers, job, srvWaits);
        } else {
            throw new UnsupportedOperationException("algorithm '" + schedulerType + "' is not supported");
        }
        return schedulerResult;
    }

    private Server getLargestServer() {
        if (largestServer == null) {
            this.largestServer = ServerUtils.getLargestServer(FileConstants.serverConfigFile);
        }
        return largestServer;
    }

    private ArrayList<Server> createServersFromData(File fileName) {
        return ServerUtils.createServersFromFile(fileName);
    }

    private ArrayList<Server> createServersFromData(final String serverResponse) {
        return ServerUtils.createServersFromResponse(serverResponse);
    }

    private String fmtGetsCapable(final Job j) {
        return ("GETS Capable " + j.getNumCores() + " " + j.getMemory() + " " + j.getDiskSpace());
    }

    private Job createJobFromData(final String jobnResponse) {
        return JobUtils.createJobFromJobN(jobnResponse);
    }

    private boolean isConnected() {
        return this.connected;
    }

    private boolean disconnect() {
        sendMsg(CmdConstants.QUIT);
        return this.connected = false;
    }
}
