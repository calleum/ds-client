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
public class ServerConfigHandler {

    static ArrayList<Server> serverList;

    private static Logger LOG = Logger.getLogger(ServerConfigHandler.class.getName());

    public static ArrayList<Server> createServersFromFile(File fileName) {
        serverList = new ArrayList<Server>();
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

                Server s = new Server(
                    i,
                    type,
                    limit,
                    bootupTime,
                    hourlyRate,
                    numCores,
                    memory,
                    disk
                );

                serverList.add(s);
            }

        } catch (Exception e) {
            LOG.info("Unhandled exception: " + e);
        }
        return serverList;
    }
}
