package lu.dainesch.lighty.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import lu.dainesch.lighty.AppConfig;
import lu.dainesch.lighty.UserStore;
import lu.dainesch.lighty.messaging.WSDataEndPoint;
import lu.dainesch.lighty.messaging.LightStateMessage;
import lu.dainesch.lighty.messaging.WSCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class LightController {

    private static final Logger LOG = LoggerFactory.getLogger(LightController.class);

    @Inject
    private AppConfig conf;
    @Inject
    private WSDataEndPoint endPoint;
    @Inject
    private UserStore store;

    public static final String SET = "set/";
    public static final String GET = "get/";
    public static final String BOTH = "both/";

    private String convertColorToPath(Light l) {
        StringBuilder b = new StringBuilder();
        b.append(l.getR()).append("/");
        b.append(l.getG()).append("/");
        b.append(l.getB());
        return b.toString();
    }

    public List<Light> getAllLights() {
        List<Light> ret = new LinkedList<>();
        ret.add(new Light(1, "1 - Top Light"));
        ret.add(new Light(2, "2 - Right Light"));
        ret.add(new Light(3, "3 - Left Light"));
        ret.add(new Light(4, "4 - Bottom"));
        ret.add(new Light(5, "5 - Test"));
        return ret;
    }

    public void updateColor(Light l) {
        if (!conf.isEnableControl()) {
            return;
        }
        try {
            URL u = new URL(conf.getServer() + GET + l.getId() + "/");

            URLConnection con = u.openConnection();

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if (inputLine.contains("currentColorLeft=[")) {
                        inputLine = inputLine.replaceAll(".*currentColorLeft=\\[", "");
                        inputLine = inputLine.substring(0, inputLine.indexOf("]"));
                        StringTokenizer tok = new StringTokenizer(inputLine, ",");

                        l.setR(Integer.parseInt(tok.nextToken()));
                        l.setG(Integer.parseInt(tok.nextToken()));
                        l.setB(Integer.parseInt(tok.nextToken()));

                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("Error reading light state", ex);
        }
    }

    public void setColor(Light l) {
        if (!conf.isEnableControl()) {
            return;
        }
        try {
            URL u = new URL(conf.getServer() + SET + l.getId() + "/" + BOTH + convertColorToPath(l));

            URLConnection con = u.openConnection();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    //System.out.println(inputLine);
                }
            }
        } catch (IOException ex) {
            LOG.error("Error reading light state", ex);
        }

    }

    public void onLightStateMessage(@Observes LightStateMessage msg) {
        if (store.getActiveKey() != null && store.getActiveKey().equals(msg.getKey())) {

            msg.getLights().forEach(l -> setColor(l));

            // prepare broadcast
            msg.setUsername(store.getUserName(msg.getKey()));
            //refresh 
            store.setActiveKey(msg.getKey());
            msg.setKey(null);
            endPoint.sendMessageAll(msg);
        }

    }

    public void onCommand(@Observes WSCommand cmd) {
        switch (cmd.getCommand()) {
            case KICK:
                if (store.isAdminUser(cmd.getKey())) {
                    store.setActiveKey(null);
                    LightStateMessage msg = new LightStateMessage();
                    msg.setUsername("");
                    endPoint.sendMessageAll(msg);
                }
                break;
            case STREAM:
                if (store.getActiveKey() == null || store.isAdminUser(cmd.getKey())) {
                    store.setActiveKey(cmd.getKey());
                    LightStateMessage msg = new LightStateMessage();
                    msg.setUsername(store.getUserName(cmd.getKey()));
                    endPoint.sendMessageAll(msg);
                }
                break;
            case OTHERSTREAM:
                if (store.isAdminUser(cmd.getKey())) {
                    store.setActiveKey(cmd.getUsername());
                    LightStateMessage msg = new LightStateMessage();
                    msg.setUsername(store.getUserName(cmd.getUsername()));
                    endPoint.sendMessageAll(msg);
                }
                break;
        }
    }

}
