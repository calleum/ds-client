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
                sendMsg(scheduleJob(servers, currentJob, SchedulerType.LRR));
                currentJob = null;
            }
        }
    }

    private void makeHandshake() throws IOException {
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

    private String scheduleJob(ArrayList<Server> servers, Job job, SchedulerType algorithm) {
        if (!checkJobSchedulable(job)) {
            return CmdConstants.PSHJ; // there is no server that can handle the job.
        }

        String schedulerResult = new String();
        if (algorithm.equals(SchedulerType.LRR)) {
            schedulerResult = Scheduler.runSchedulerLRR(servers, job);
        } else if (algorithm.equals(SchedulerType.STCF)) {

            HashMap<Server, Integer> srvWaits = getServerWaitTime(servers);
            schedulerResult = Scheduler.runSchedulerSTCF(servers, job, srvWaits);
        } else {
            throw new UnsupportedOperationException("algorithm '" + algorithm + "' is not supported");
        }
        return schedulerResult;
    }

    private boolean checkJobSchedulable(Job job) {
        return getLargestServerFromFile(FileConstants.serverConfigFile).isCapable(job);
    }

    private Server getLargestServerFromFile(File fileName) {
        return ServerUtils.getLargestServer(fileName);
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
