public interface CmdConstants {
    public static final String HELO = "HELO"; // HELO
    public static final String AUTH = "AUTH"; // AUTH username
    public static final String REDY = "REDY"; // REDY
    public static final String QUIT = "QUIT"; // QUIT
    public static final String DATA = "DATA"; // DATA nRecs recLen
    public static final String JOBN = "JOBN"; // JOBN submitTime jobID estRuntime core memory disk
    public static final String JOBP = "JOBP"; // JOBP submitTime jobID estRuntime core memory disk
    public static final String JCPL = "JCPL"; // JCPL endTime jobID serverType serverID
    public static final String RESF = "RESF"; // RESF serverType serverID timeOfFailure
    public static final String RESR = "RESR"; // RESR serverType serverID timeOfRecovery
    public static final String NONE = "NONE"; // NONE
    public static final String GETS = "GETS"; // GETS All|Type serverType|Capable core memory disk|Avail core memory disk
    public static final String GETS_ALL = "GETS All"; // GETS All
    public static final String SCHD = "SCHD"; // SCHD jobID serverType serverID
    public static final String CNTJ = "CNTJ"; // CNTJ serverType serverID jobState
    public static final String EJWT = "EJWT"; // EJWT serverType serverID
    public static final String LSTJ = "LSTJ"; // LSTJ serverType serverID
    public static final String PSHJ = "PSHJ"; // PSHJ
    public static final String MIGJ = "MIGJ"; // MIGJ jobID srcServerType srcServerID tgtServerType tgtServerID
    public static final String KILJ = "KILJ"; // KILJ serverType serverID jobID
    public static final String TERM = "TERM"; // TERM serverType serverID
    public static final String ERR = "ERR";   // ERR: error message
    public static final String OK = "OK";     // OK
}
