public class Server {
    int id;
    ServerType type;
    int limit;
    int bootupTime;
    int hourlyRate;
    int numCores;
    int memory;
    int diskSpace;

    public Server(
            int id,
            ServerType type,
            int limit,
            int bootupTime, 
            int hourlyRate,
            int numCores,
            int memory,
            int diskSpace
            ){
        this.id = id;
        this.type = type;
        this.limit = limit;
        this.bootupTime = bootupTime;
        this.hourlyRate = hourlyRate;
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

    public String getType() {
        return type.type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getBootupTime() {
        return bootupTime;
    }

    public void setBootupTime(int bootupTime) {
        this.bootupTime = bootupTime;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(int hourlyRate) {
        this.hourlyRate = hourlyRate;
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

    public boolean isLargerThan(Server other) {
        return (this.numCores - other.numCores) > 0;
    }

}

