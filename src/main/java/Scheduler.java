import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Scheduler
 */
public class Scheduler {
    private final static Logger LOG = Logger.getLogger(Client.class.getName());
    private static ArrayDeque<Server> largestServerQueue;
    private HashMap<Integer, Job> scheduledJobs;

    // Servers returned by GETS All, sorted by estimated runtimes in descending order.
    private ArrayList<Server> sortedServerList;

    public Scheduler(ArrayList<Server> servers) {
        LOG.info("Creating new scheduler serv q");
        largestServerQueue = new ArrayDeque<Server>();
        sortedServerList = new ArrayList<Server>();
        sortedServerList.addAll(servers);
        for (Server srv : servers) {
            LOG.info("adding new server to list: \n" + srv.toString());
        }
        // sortedServerList.sort(new ServerComparator());
        LOG.info("creating new scheduler jobmap");
        scheduledJobs = new HashMap<Integer, Job>();

        LOG.info("Created new scheduler");
    }
    
    /**
     * Using an {@link ArrayList}, order the servers from
     * lowest to highest total estimated running time.
     *
     * @return {@link Server}
     */
    private Server getLowestEstimatedRuntimeServer() {
        return sortedServerList.get(0);
    }

    /**
     * Find the first server in the ArrayList which is capable to run
     * the job that has been sent from ds-server.
     *
     * @param job {@link Job}
     * @return {@link Server}
     */
    private Server getLowestCapableEstimatedRuntimeServer(Job job) {

        Server lowestCapableEstimatedRTServer = getLowestEstimatedRuntimeServer();
        LOG.info(sortedServerList.size() + " :)");

        // iterate over the servers and return the first capable 
        for (Server server : sortedServerList) {
            if (server.isCapable(job)) {
                LOG.info(server.toString());
                lowestCapableEstimatedRTServer = server;
            }
        }

        return lowestCapableEstimatedRTServer;
    }

    public String runSchedulerLERT(final Job job) {
        return scheduleJob(getLowestCapableEstimatedRuntimeServer(job), job);
    }

    public String runSchedulerLRR(final ArrayList<Server> servers, final Job job, Server largestServer) {

        assignLargestServerType(servers, largestServer);
        Server allocatedServer;
        if (!largestServerQueue.isEmpty()) {
            allocatedServer = largestServerQueue.poll();
            largestServerQueue.add(allocatedServer);
            return scheduleJob(allocatedServer, job);
        }

        return CmdConstants.ERR;
    }
 
    public static String runSchedulerSTCF(final ArrayList<Server> servers, final Job job,
            HashMap<Server, Integer> srvWaits) {
        /**
         * LSTJ:
         * 
         * @return jobID jobState(1|2) submitTime startTime estRunTime core memory disk
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
  
    /**
     * helper function to assign a {@link ServerType} to a 
     * round robin lazy linked list masquerading as a queue.
     *
     * @param largestServer, the largest {@link ServerType} derived
     * from the ds-system config file.
     * 
     */
    private void assignLargestServerType(ArrayList<Server> servers, Server largestServer) {

        if (null == largestServerQueue || largestServerQueue.isEmpty()) {

            LOG.info("numcores = " + largestServer.getNumCores());
            for (final Server s : servers) {
                // LOG.info(s.getId() + ", " + s.getType() + " " + s.getNumCores());
                if (s.getType().equals(largestServer.getType()) && s.getNumCores() == largestServer.getNumCores()) {
                    LOG.info("pushed to queue: " + s.getId() + ", " + s.getType());
                    largestServerQueue.add(s);
                }
            }
        }
    }

    public static ArrayDeque<Server> getServerQueue() {
        return largestServerQueue;
    }

    public void markJobScheduled(Job j) {
        scheduledJobs.put(j.getId(), j);
    }

    public void markJobCompleted(int jobId) {
        LOG.info("Marking Job Completed.");
        Job j = scheduledJobs.get(jobId);
        j.getAssignedTo().setEstRuntime(j.getAssignedTo().getEstRuntime() - j.getEstRuntime());
    }

    private String scheduleJob(Server s, Job j) {
        LOG.info("scheduling");
        j.setAssignedTo(s);
        s.setEstRuntime(s.getEstRuntime() + j.getEstRuntime());
        return formatSchedMsg(s, j);
    }

    private static String formatSchedMsg(final Server s, final Job j) {

        LOG.info("scheduled job: " + j.toString());
        LOG.info("to server: " + s.toString());
        final String allocatedServerDetails = s.getType() + " " + s.getId();
        return CmdConstants.SCHD + " " + j.getId() + " " + allocatedServerDetails;
    }

}
