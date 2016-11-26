package lu.dainesch.lighty.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import lu.dainesch.lighty.AppConfig;
import lu.dainesch.lighty.control.Light;


@Named("site")
@RequestScoped
public class SiteModel {

    private String title = "Lighty";
    private String activeMenu = "Home";
    private final Set<String> jsFiles = new LinkedHashSet<>();
    private final Set<String> cssFiles = new LinkedHashSet<>();
    private Map<String,String> userMap;
    
    private List<Light> lights;

    public void readAppConf(AppConfig conf) {
        jsFiles.addAll(conf.getJsFiles());
        cssFiles.addAll(conf.getCssFiles());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<String> getCssFiles() {
        return cssFiles;
    }

    public Set<String> getJsFiles() {
        return jsFiles;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public String getActiveMenu() {
        return activeMenu;
    }

    public void setActiveMenu(String activeMenu) {
        this.activeMenu = activeMenu;
    }

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public void setUserMap(Map<String, String> userMap) {
        this.userMap = userMap;
    }

    
    
}
