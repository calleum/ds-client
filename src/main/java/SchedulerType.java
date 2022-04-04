import java.util.ArrayList;

public enum SchedulerType {
    LRR,;

    /**
     * TO DO: Extend to be a factory for different algorithms in part 2,
     * Currently uses LRR always.
     * @param servers TODO
     * @param job TODO
     * @param scheduler TODO
     */
    public String scheduleJob(final ArrayList<Server> servers, final ArrayList<Job> job, Scheduler scheduler) {
        if (!equals(SchedulerType.LRR)) {
            throw new UnsupportedOperationException("algorithm '" + this + "' is not supported");
        }
        return scheduler.runSchedulerLRR(servers, job);
    }
}
