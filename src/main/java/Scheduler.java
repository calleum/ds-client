import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Scheduler
 */
public class Scheduler {
    private SchedulerType schedulerType;
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    private static ArrayDeque<Server> serverQueue;
    private static HashMap<Server, Job[]> scheduledJobs;

    public Scheduler(SchedulerType schedulerType) {
        this.schedulerType = schedulerType;
        serverQueue = new ArrayDeque<Server>();
        scheduledJobs = new HashMap<Server, Job[]>();
        LOG.info(this.schedulerType.toString());
    }

    /* public static String scheduleJob(final ArrayList<Server> servers, final Job job,
            final SchedulerType algorithm) {

    } */

    public static String runSchedulerSTCF(final ArrayList<Server> servers, final Job job, HashMap<Server, Integer> srvWaits) {
        /*
         * LSTJ:
         * @Returns
         * jobID jobState(1|2) submitTime startTime estRunTime core memory disk
         * jobState 1: waiting, 2: running
         * EXAMPLE:
         * LSTJ medium 3
         * DATA 3 59 // 3 jobs and the length of each message is 59 character long
         * OK
         * 2 2 139 1208 172 2 100 200
         * 7 2 192 1224 328 1 120 450
         * 11 1 324 -1 49 4 380 1000
         * // -1 for unknown start time since the job 11 is waiting
         */

        return CmdConstants.ERR;
    }

    public static String runSchedulerLRR(final ArrayList<Server> servers, final Job job, Server largestServer) {
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
            return formatSchedMsg(allocatedServer, job);
        }

        return CmdConstants.ERR;
    }

    public static ArrayDeque<Server> getServerQueue() {
        return serverQueue;
    }

    private static String formatSchedMsg(final Server s, final Job j) {
        /* if (!s.isCapable(j)) {
            return CmdConstants.PSHJ;
        } */
        final String allocatedServerDetails = s.getType() + " " + s.getId();
        return CmdConstants.SCHD + " " + j.getId() + " " + allocatedServerDetails;
    }

}
