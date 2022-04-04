import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * ServerConfigHandler
 */
public class ServerUtils {

    private static Logger LOG = Logger.getLogger(ServerUtils.class.getName());

    public static Server getLargestServer(File fileName) {
        Server largestServer = null;
        ArrayList<Server> serverList = createServersFromFile(fileName);
        for (Server s : serverList) {
            if (null == largestServer || s.getNumCores() > largestServer.getNumCores()) {
                largestServer = s;
            }
        }
        return largestServer;
    }

    public static ArrayList<Server> createServersFromFile(File fileName) {
        ArrayList<Server> serverList = new ArrayList<Server>();
        LOG.info("Reading XML file: " + fileName);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fileName);

            doc.getDocumentElement().normalize();
            NodeList servers = doc.getElementsByTagName("server");
            for (int i = 0; i < servers.getLength(); i++) {
                Element server = (Element) servers.item(i);

                ServerType type = new ServerType(server.getAttribute("type"));
                int limit = Integer.parseInt(server.getAttribute("limit"));
                int bootupTime = Integer.parseInt(server.getAttribute("bootupTime"));
                float hourlyRate = Float.parseFloat(server.getAttribute("hourlyRate"));
                int numCores = Integer.parseInt(server.getAttribute("cores"));
                int memory = Integer.parseInt(server.getAttribute("memory"));
                int disk = Integer.parseInt(server.getAttribute("disk"));
                ServerState state = ServerState.inactive;

                Server s = new Server(
                        i,
                        type,
                        state,
                        limit,
                        bootupTime,
                        hourlyRate,
                        numCores,
                        memory,
                        disk);

                serverList.add(s);
            }

        } catch (Exception e) {
            LOG.info("Unhandled exception: " + e);
        }
        return serverList;
    }

    private static Server parseDataTokens(String[] serverDataTokens) {
        Server s = new Server();
        s.setType(new ServerType(serverDataTokens[0]));
        s.setId(Integer.parseInt(serverDataTokens[1]));
        s.setState(ServerState.valueOf(serverDataTokens[2]));
        s.setBootupTime(Integer.parseInt(serverDataTokens[3]));
        s.setNumCores(Integer.parseInt(serverDataTokens[4]));
        s.setMemory(Integer.parseInt(serverDataTokens[5]));
        s.setDiskSpace(Integer.parseInt(serverDataTokens[6]));
        return s;
    }

    public static ArrayList<Server> createServersFromResponse(String serverResponse) {
        final ArrayList<Server> serversFromData = new ArrayList<Server>();
        final String[] serverResponseMultiLine = serverResponse.split("\\r?\\n");

        for (final String line : serverResponseMultiLine) {
            serversFromData.add(parseDataTokens(line.trim().split("\\s+")));
        }
        return serversFromData;
    }
}
