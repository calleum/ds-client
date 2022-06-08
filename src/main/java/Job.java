public class Job {
    private int id;
    private int submissionTime;
    private int estRuntime;
    private int numCores;
    private int memory;
    private int diskSpace;
    private JobState state;
    private Server assignedTo;

    public Job() {
    };

    public Job(
            int id,
            JobState state,
            int submissionTime,
            int estRuntime,
            int numCores,
            int memory,
            int diskSpace
    ) {
        this.id = id;
        this.state = state;
        this.estRuntime = estRuntime;
        this.submissionTime = submissionTime;
        this.numCores = numCores;
        this.memory = memory;
        this.diskSpace = diskSpace;
        this.assignedTo = null;
    }

    public JobState getState() {
        return state;
    }

    public void setState(JobState state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSubmissionTime() {
        return submissionTime;
    }

    public void setSubmissionTime(int submissionTime) {
        this.submissionTime = submissionTime;
    }

    public int getEstRuntime() {
        return estRuntime;
    }

    public void setEstRuntime(int estRuntime) {
        this.estRuntime = estRuntime;
    }

    public int getNumCores() {
        return numCores;
    }

    public void setNumCores(int numCores) {
        this.numCores = numCores;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(int diskSpace) {
        this.diskSpace = diskSpace;
    }

    @Override
    public String toString() {
        return "Job [diskSpace=" + diskSpace + ", estRuntime=" + estRuntime + ", id=" + id + ", memory=" + memory
                + ", numCores=" + numCores + ", submissionTime=" + submissionTime + "]";
    }
}
