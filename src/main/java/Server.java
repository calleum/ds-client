public class Server {
    private int id;
    private ServerType type;
    private ServerState state;
    private int limit;
    private int bootupTime;
    private float hourlyRate;
    private int numCores;
    private int memory;
    private int diskSpace;
    private int estRunTime;

    public Server(
            final int id,
            final ServerType type,
            final ServerState state,
            final int limit,
            final int bootupTime,
            final float hourlyRate,
            final int numCores,
            final int memory,
            final int diskSpace,
            final int estRunTime) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
        this.numCores = numCores;
        this.memory = memory;
        this.diskSpace = diskSpace;
        this.estRunTime = estRunTime;
    }

    public Server() {
    }

    public ServerState getState() {
        return state;
    }

    public void setState(final ServerState state) {
        this.state = state;
    }

    public void setHourlyRate(final float hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getType() {
        return type.type;
    }

    public void setType(final ServerType type) {
        this.type = type;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public int getBootupTime() {
        return bootupTime;
    }

    public void setBootupTime(final int bootupTime) {
        this.bootupTime = bootupTime;
    }

    public float getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(final int hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getNumCores() {
        return numCores;
    }

    public void setNumCores(final int numCores) {
        this.numCores = numCores;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(final int memory) {
        this.memory = memory;
    }

    public int getDiskSpace() {
        return diskSpace;
    }

    public void setDiskSpace(final int diskSpace) {
        this.diskSpace = diskSpace;
    }

    public boolean isLargerThan(final Server other) {
        return (this.numCores - other.numCores) > 0;
    }

    public boolean isCapable(final Job j) {
        return (this.getNumCores() >= j.getNumCores() &&
                this.getMemory() >= j.getMemory() &&
                this.getDiskSpace() >= j.getDiskSpace());
    }

    public int getEstRuntime() {
        return estRunTime;
    }

    public void setEstRunTime(int estRunTime) {
        this.estRunTime = estRunTime;
    }

    @Override
    public String toString() {
        return "Server [estRunTime=" + estRunTime + ", type=" + type.getType() + ", memory=" + memory + ", numCores=" + numCores + ", hourlyRate=" + hourlyRate + ", id=" + id + ", limit=" + limit
                 + ", state=" + state + "]";
    }
}
