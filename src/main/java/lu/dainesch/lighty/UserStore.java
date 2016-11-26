package lu.dainesch.lighty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import lu.dainesch.lighty.messaging.WSDataEndPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@Lock(LockType.READ)
public class UserStore {

    private static final Logger LOG = LoggerFactory.getLogger(UserStore.class);

    private String activeKey;
    private Long keyTime;
    private final Map<String, String> nameKeyMap = new HashMap<>();
    private final Set<String> adminUsers = new HashSet<>();

    @Inject
    private WSDataEndPoint endPoint;

    @Lock(LockType.WRITE)
    @Schedule(hour = "*", minute = "*", second = "*/10", persistent = false)
    public void checkKeyTimeout() {
        List<String> admKeys = new ArrayList<>(adminUsers);
        List<String> userKeys = new ArrayList<>(nameKeyMap.keySet());
        for (String key : endPoint.getActiveKeys()) {
            admKeys.remove(key);
            userKeys.remove(key);
        }
        for (String missing : admKeys) {
            LOG.info("Removing admin: " + missing);
            adminUsers.remove(missing);
        }
        for (String missing : userKeys) {
            LOG.info("Removing user: " + nameKeyMap.get(missing));
            nameKeyMap.remove(missing);
        }

        // active key
        if (activeKey == null || keyTime == null) {
            return;
        }
        if (System.currentTimeMillis() - keyTime > 60 * 1000) {
            LOG.info("Active timeout: " + activeKey);
            activeKey = null;
            keyTime = null;

        }
    }

    public String getActiveKey() {
        return activeKey;
    }

    @Lock(LockType.WRITE)
    public void setActiveKey(String activeKey) {
        this.activeKey = activeKey;
        if (activeKey != null) {
            keyTime = System.currentTimeMillis();
        } else {
            keyTime = null;
        }
    }

    @Lock(LockType.WRITE)
    public void addAdminUser(String key) {
        adminUsers.add(key);
    }

    public boolean isAdminUser(String key) {
        return adminUsers.contains(key);
    }

    @Lock(LockType.WRITE)
    public void addNewUser(String key, String name) {
        nameKeyMap.put(key, name);
    }

    public String getUserName(String key) {
        return nameKeyMap.get(key);
    }

    public Map<String, String> getUserMap() {
        return new HashMap<>(nameKeyMap);
    }

}
