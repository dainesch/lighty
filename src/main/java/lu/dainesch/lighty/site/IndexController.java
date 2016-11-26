package lu.dainesch.lighty.site;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mvc.annotation.Controller;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import lu.dainesch.lighty.UserStore;
import lu.dainesch.lighty.control.LightController;
import lu.dainesch.lighty.model.SiteModel;

@Controller
@Path("index")
@Stateless
public class IndexController {
    
    @EJB
    private LightController lightCont;
    
    @Inject
    private SiteModel siteMod;
    @Inject
    private UserStore userStore;
    
    @GET
    public String getIndex() {
        siteMod.getJsFiles().add("/js/custom/lighty.js");
        siteMod.getJsFiles().add("/js/custom/image.js");
        siteMod.setLights(lightCont.getAllLights());
        siteMod.setUserMap(userStore.getUserMap());
        
        return "/WEB-INF/jspf/index.jsp";
    }
    
}
