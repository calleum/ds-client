import java.util.*;
import java.util.logging.Logger;
import java.io.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * ServerConfig
 */
public class ServerConfigHandler {

    ArrayList<Server> serverList;

    private static Logger LOG = Logger.getLogger(ServerConfigHandler.class.getName());

    public ArrayList<Server> readXML(String fileName) {
        serverList = new ArrayList<Server>();

        try {
            File systemXML = new File(fileName);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(systemXML);

            doc.getDocumentElement().normalize();
            NodeList servers = doc.getElementsByTagName("server");
            for (int i = 0; i < servers.getLength(); i++) {
                Element server = (Element) servers.item(i);

                ServerType type = new ServerType(server.getAttribute("type"));
                int limit = Integer.parseInt(server.getAttribute("limit"));
                int bootupTime = Integer.parseInt(server.getAttribute("bootupTime"));
                int hourlyRate = Integer.parseInt(server.getAttribute("hourlyRate"));
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
