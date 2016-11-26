package lu.dainesch.lighty.messaging;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WSMessage implements Serializable {

    public static enum Type {
        LIGHTSTATE, COMMAND
    }

    protected Type type;
    protected String key;
    protected String username;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
    

}
