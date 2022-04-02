public class Job {
    private int id;
    private int submissionTime;
    private int estRuntime;
    private int numCores;
    private int memory;
    private int diskSpace;

    public Job() {};

    public Job(
            int id,
            int submissionTime,
            int estRuntime,
            int numCores,
            int memory,
            int diskSpace) {
        this.id = id;
        this.estRuntime = estRuntime;
        this.submissionTime = submissionTime;
        this.numCores = numCores;
        this.memory = memory;
        this.diskSpace = diskSpace;
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

}
