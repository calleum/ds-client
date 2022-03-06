public class Job {
    int id;
    JobType type;
    JobState state;
    int submissionTime;
    int estRuntime;
    int numCores;
    int memory;
    int diskSpace;

    public Job(
            int id,
            JobType type,
            JobState state,
            int submissionTime,
            int estRuntime,
            int numCores,
            int memory,
            int diskSpace
            ){
        this.id = id;
        this.type = type;
        this.state = state;
        this.estRuntime = estRuntime;
        this.submissionTime = submissionTime;
        this.numCores = numCores;
        this.memory = memory;
        this.diskSpace = diskSpace;
            }


}
