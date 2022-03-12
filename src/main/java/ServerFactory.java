import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * ServerFactory
 */
public class ServerFactory {

    public ServerFactory(NodeList nl){
        for (Element e : nl) {
            
                ServerType type = new ServerType(e.getAttribute("type"));
                int limit = Integer.parseInt(e.getAttribute("limit"));
                int bootupTime = Integer.parseInt(e.getAttribute("bootupTime"));
                Double hourlyRate = Double.parseDouble(e.getAttribute("hourlyRate"));
                int numCores = Integer.parseInt(e.getAttribute("cores"));
                int memory = Integer.parseInt(e.getAttribute("memory"));
                int disk = Integer.parseInt(e.getAttribute("disk"));
        }
    }
}
