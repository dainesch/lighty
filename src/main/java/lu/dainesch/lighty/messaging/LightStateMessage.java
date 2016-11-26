package lu.dainesch.lighty.messaging;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lu.dainesch.lighty.control.Light;


@XmlRootElement
public class LightStateMessage extends WSMessage {

    private List<Light> lights = new LinkedList<>();

    public LightStateMessage() {
    }

    public LightStateMessage(List<Light> lights) {
        this.lights = lights;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    @Override
    public String toString() { 
        return "LightStateMessage{" + "lights=" + Arrays.toString(lights.toArray()) + '}';
    }
    
    

}
