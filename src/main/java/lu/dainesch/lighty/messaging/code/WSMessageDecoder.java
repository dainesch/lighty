package lu.dainesch.lighty.messaging.code;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import lu.dainesch.lighty.control.Light;
import lu.dainesch.lighty.messaging.LightStateMessage;
import lu.dainesch.lighty.messaging.WSCommand;
import lu.dainesch.lighty.messaging.WSMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Disaster
 */
public class WSMessageDecoder implements Decoder.Text<WSMessage> {
    
    private static final Logger LOG = LoggerFactory.getLogger(WSMessageDecoder.class);
    
    @Override
    public void init(EndpointConfig config) {
        
    }
    
    @Override
    public boolean willDecode(String s) {
        JsonObject obj = Json.createReader(new StringReader(s)).readObject();
        try {
            if (obj.containsKey("type")) {
                // trigger error or not
                WSMessage.Type.valueOf(obj.getString("type"));
                return true;
            }
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
    
    @Override
    public WSMessage decode(String s) throws DecodeException {
        try {
            JsonObject obj = Json.createReader(new StringReader(s)).readObject();
            switch (WSMessage.Type.valueOf(obj.getString("type"))) {
                case LIGHTSTATE:
                    LightStateMessage lt = new LightStateMessage();
                    lt.setType(WSMessage.Type.LIGHTSTATE);
                    
                    JsonArray arr = obj.getJsonArray("states");
                    
                    if (obj.containsKey("key")) {
                        lt.setKey(obj.getString("key"));
                    }
                    
                    for (int i = 0; i < arr.size(); i++) {
                        JsonObject o = arr.getJsonObject(i);
                        Light l = new Light();
                        l.setId(o.getInt("id"));
                        l.setR(o.getInt("r"));
                        l.setG(o.getInt("g"));
                        l.setB(o.getInt("b"));
                        lt.getLights().add(l);
                    }
                    return lt;
                case COMMAND:
                    WSCommand cmd = new WSCommand();
                    cmd.setType(WSMessage.Type.COMMAND);
                    
                    if (obj.containsKey("key")) {
                        cmd.setKey(obj.getString("key"));
                    }
                    if (obj.containsKey("username")) {
                        cmd.setUsername(obj.getString("username"));
                    }
                    if (obj.containsKey("command")) {
                        cmd.setCommand(WSCommand.Command.valueOf(obj.getString("command")));
                    }
                    
                    return cmd;
            }
        } catch (Exception ex) {
            LOG.error("Error parsing message", ex);
        }
        return null;
    }
    
    @Override
    public void destroy() {
        
    }
    
}
