import org.w3c.dom.Element;

/**
 * ServerFactory
 */
public class ServerFactory {
ServerType type; 
int limit 

    public ServerConfig(Element e){
                = new ServerType(e.getAttribute("type"));
                = Integer.parseInt(e.getAttribute("limit"));
                = Integer.parseInt(e.getAttribute("bootupTime"));
                Double hourlyRate = Double.parseDouble(e.getAttribute("hourlyRate"));
                int numCores = Integer.parseInt(e.getAttribute("cores"));
                int memory = Integer.parseInt(e.getAttribute("memory"));
                int disk = Integer.parseInt(e.getAttribute("disk"));
    }
}
