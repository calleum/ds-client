import java.util.Comparator;

class ServerComparator implements Comparator<Server> {
    // Used for sorting in ascending order of
    // estimated total runtime of jobs on server
    public int compare(Server a, Server b) {
        return b.getEstRuntime() - a.getEstRuntime();
    }
}
