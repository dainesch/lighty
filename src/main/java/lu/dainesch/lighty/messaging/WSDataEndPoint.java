package lu.dainesch.lighty.messaging;

import lu.dainesch.lighty.messaging.code.WSMessageEncoder;
import lu.dainesch.lighty.messaging.code.WSMessageDecoder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.websocket.EncodeException;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@ServerEndpoint(value = "/ws", encoders = WSMessageEncoder.class, decoders = WSMessageDecoder.class)
public class WSDataEndPoint {

    private static final Logger LOG = LoggerFactory.getLogger(WSDataEndPoint.class);

    private static final Set<Session> SESSIONS = Collections.synchronizedSet(new HashSet<Session>());
    private static final String KEY = "lightyKey";

    @Inject
    private Event<LightStateMessage> lightEv;
    @Inject
    private Event<WSCommand> commandEV;

    @OnOpen
    public void open(Session session) {
        SESSIONS.add(session);
        LOG.info("Client connected " + session.getId());
    }

    @OnMessage
    public void onLightMessage(Session session, WSMessage msg) {
        if (msg==null) {
            return;
        }
        if (msg.getKey() != null) {
            session.getUserProperties().put(KEY, msg.getKey());
        }

        if (msg instanceof LightStateMessage) {
            lightEv.fire((LightStateMessage) msg);
        } else if (msg instanceof WSCommand) {
            commandEV.fire((WSCommand) msg);
        }

    }

    public void sendMessageAll(LightStateMessage msg) {
        if (msg.getKey() != null) {
            msg.setKey(null);
        }
        for (Session sess : new ArrayList<>(SESSIONS)) {
            if (sess.isOpen()) {
                try {
                    sess.getBasicRemote().sendObject(msg);
                } catch (IOException | EncodeException ex) {
                    LOG.error("Error sending message", ex);
                }
            } else {
                SESSIONS.remove(sess);
            }
        }
    }
    
    public List<String> getActiveKeys() {
        List<String> ret = new LinkedList<>();
        for (Session sess : new ArrayList<>(SESSIONS)) {
            if (sess.getUserProperties().containsKey(KEY)) {
                ret.add((String) sess.getUserProperties().get(KEY));
            }
        }
        return ret;
    }

    //@OnClose
    public void onClose(Session session) {
        SESSIONS.remove(session);
    }

    //@OnError
    public void onError(Throwable t, Session session) {
        SESSIONS.remove(session);
    }

}
