package lu.dainesch.lighty.messaging.code;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import lu.dainesch.lighty.control.Light;
import lu.dainesch.lighty.messaging.LightStateMessage;
import lu.dainesch.lighty.messaging.WSMessage;

/**
 *
 * @author Disaster
 */
public class WSMessageEncoder implements Encoder.Text<LightStateMessage> {

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public String encode(LightStateMessage msg) throws EncodeException {
        JsonObjectBuilder ret = Json.createObjectBuilder();
        ret.add("type", WSMessage.Type.LIGHTSTATE.toString());
        if (msg.getUsername()!=null) {
            ret.add("name", msg.getUsername());
        }

        JsonArrayBuilder arr = Json.createArrayBuilder();
        for (Light l : msg.getLights()) {
            JsonObjectBuilder o = Json.createObjectBuilder();
            o.add("id", l.getId());
            o.add("r", l.getR());
            o.add("g", l.getG());
            o.add("b", l.getB());
            arr.add(o);
        }
        ret.add("states", arr);
        return ret.build().toString();
    }

    @Override
    public void destroy() {

    }

}
