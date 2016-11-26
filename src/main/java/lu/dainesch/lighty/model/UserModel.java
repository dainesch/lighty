package lu.dainesch.lighty.model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;


@Named("userMod")
@SessionScoped
public class UserModel implements Serializable {

    private String name;
    private String key;
    private boolean admin;
    private boolean loggedIn;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
