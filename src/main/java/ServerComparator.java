import java.util.Comparator;
import java.util.logging.Logger;

class ServerComparator implements Comparator<Server> {
    private final static Logger LOG = Logger.getLogger(ServerComparator.class.getName());
    // Used for sorting in ascending order of
    // estimated total runtime of jobs on server
    public int compare(Server a, Server b) {
        LOG.info("comparing servers");
        return b.getEstRuntime() - a.getEstRuntime();
    }
}
