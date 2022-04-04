import java.util.Comparator;

class ServerComparator implements Comparator<Server> {
    // Used for sorting in ascending order of
    // numCores
    public int compare(Server a, Server b) {
        return a.getNumCores() - b.getNumCores();
    }
}
