import java.util.ArrayList;
import java.util.Collections;

/**
 * Scheduler
 */
public class Scheduler {

    /**
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

}
