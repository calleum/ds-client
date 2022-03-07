import java.util.*;
import java.util.logging.Logger;
import java.io.*;
import javax.xml.parsers.*;

import org.w3c.dom.*;

/**
 * ServerConfig
 */
public class ServerConfig {

    ArrayList<Server> serverList;

    private static Logger LOG = Logger.getLogger(ServerConfig.class.getName());

    public ArrayList<Server> readXML(String fileName) {
        serverList = new ArrayList<Server>();

        try {
            // XML file to read
            File systemXML = new File(fileName);

            // Setup XML document parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(systemXML);

            // String converting to normalized form
            doc.getDocumentElement().normalize();
            NodeList servers = doc.getElementsByTagName("server");
            for (int i = 0; i < servers.getLength(); i++) {
                Element server = (Element) servers.item(i);

                // Parse all XML attributes to new Server object
                ServerType type = new ServerType(server.getAttribute("type"));
                int limit = Integer.parseInt(server.getAttribute("limit"));
                int bootupTime = Integer.parseInt(server.getAttribute("bootupTime"));
                Double hourlyRate = Double.parseDouble(server.getAttribute("hourlyRate"));
                int numCores = Integer.parseInt(server.getAttribute("coreCount"));
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
