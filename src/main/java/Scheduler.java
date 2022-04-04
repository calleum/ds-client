import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Scheduler
 */
public class Scheduler {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    private static ArrayDeque<Server> serverQueue = new ArrayDeque<Server>();
    private static final Server largestServer = ServerUtils.getLargestServer(FileConstants.serverConfigFile);

    public static String scheduleJob(final ArrayList<Server> servers, final Job job,
            final SchedulerType algorithm) {

        if (!algorithm.equals(SchedulerType.LRR)) {
            throw new UnsupportedOperationException("algorithm '" + algorithm + "' is not supported");
        }

        return runSchedulerLRR(servers, job);
    }

    private static String runSchedulerLRR(final ArrayList<Server> servers, final Job job) {
        Server allocatedServer;

        if (null == serverQueue || serverQueue.isEmpty()) {

            LOG.info("numcores = " + largestServer.getNumCores());
            for (final Server s : servers) {
                // LOG.info(s.getId() + ", " + s.getType() + " " + s.getNumCores());
                if (s.getType().equals(largestServer.getType()) && s.getNumCores() == largestServer.getNumCores()) {
                    LOG.info("pushed to queue: " + s.getId() + ", " + s.getType());
                    serverQueue.add(s);
                }
            }
        }

        if (!serverQueue.isEmpty()) {
            allocatedServer = serverQueue.poll();
            serverQueue.add(allocatedServer);
            return allocateServerToJob(allocatedServer, job);
        }

        return CmdConstants.ERR;
    }

    public static ArrayDeque<Server> getServerQueue() {
        return serverQueue;
    }

    public static Server getLargestserver() {
        return largestServer;
    }

    private static String allocateServerToJob(final Server s, final Job j) {
        if (!s.isCapable(j)) {
            return CmdConstants.PSHJ;
        }
        final String allocatedServerDetails = s.getType() + " " + s.getId();
        return CmdConstants.SCHD + " " + j.getId() + " " + allocatedServerDetails;
    }

}
