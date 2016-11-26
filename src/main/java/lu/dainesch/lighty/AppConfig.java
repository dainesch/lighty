package lu.dainesch.lighty;

import java.util.LinkedHashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class AppConfig {

    private final String server = "http://lights.tmk:1234/";
    private final boolean enableControl = false;
    private final String adminPass = "d00m";
    
    private final Set<String> cssFiles = new LinkedHashSet<String>() {
        {
            add("/external/bootstrap/dist/css/bootstrap.min.css");
            add("/external/seiyria-bootstrap-slider/dist/css/bootstrap-slider.min.css");
            add("/css/lighty.css");
        }
    };
    private final Set<String> jsFiles = new LinkedHashSet<String>() {
        {
            add("/external/jquery/dist/jquery.min.js");
            add("/external/tether/dist/js/tether.min.js");
            add("/external/bootstrap/dist/js/bootstrap.min.js");
            add("/external/seiyria-bootstrap-slider/dist/bootstrap-slider.min.js");
            add("/external/hashmap/hashmap.js");
            add("/external/pixi/bin/pixi.min.js");
        }
    };

    public Set<String> getCssFiles() {
        return cssFiles;
    }

    public Set<String> getJsFiles() {
        return jsFiles;
    }

    public String getServer() {
        return server;
    }

    public String getAdminPass() {
        return adminPass;
    }

    public boolean isEnableControl() {
        return enableControl;
    }
    
    

}
