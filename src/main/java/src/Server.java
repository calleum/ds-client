public class Server {
    int id;
    ServerType type;
    int limit;
    int bootupTime;
    double hourlyRate;
    int numCores;
    int memory;
    int diskSpace;

    public Server(
            int id,
            ServerType type,
            int limit,
            int bootupTime, 
            double hourlyRate,
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
}

