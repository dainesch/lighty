package lu.dainesch.lighty.site;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.mvc.annotation.Controller;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import lu.dainesch.lighty.AppConfig;
import static lu.dainesch.lighty.SiteFilter.KEY_COOKIE;
import static lu.dainesch.lighty.SiteFilter.NAME_COOKIE;
import static lu.dainesch.lighty.SiteFilter.PASS_COOKIE;
import lu.dainesch.lighty.UserStore;
import lu.dainesch.lighty.model.UserModel;


@Controller
@Path("user")
@Stateless
public class UserController {

    @Context
    private HttpServletRequest req;
    @Context
    private HttpServletResponse resp;

    @Inject
    private UserModel userMod;
    @Inject
    private AppConfig conf;
    @Inject
    private UserStore store;

    @GET
    @Path("/login")
    public String getLogin() {

        return "/WEB-INF/jspf/login.jsp";
    }

    @POST
    @Path("/login")
    public String doLogin(@FormParam("user") String user, @FormParam("pass") String pass) {
        if (user != null && !user.isEmpty()) {
            userMod.setName(user);
            userMod.setLoggedIn(true);
            userMod.setAdmin(conf.getAdminPass().equals(pass));
            
            store.addNewUser(userMod.getKey(), userMod.getName());
            
            Cookie cookie = new Cookie(KEY_COOKIE, userMod.getKey());
            cookie.setPath("/");
            resp.addCookie(cookie);
            cookie = new Cookie(NAME_COOKIE, userMod.getName());
            cookie.setPath("/");
            resp.addCookie(cookie);

            if (userMod.isAdmin()) {
                store.addAdminUser(userMod.getKey());
                cookie = new Cookie(PASS_COOKIE, pass);
                cookie.setPath("/");
                resp.addCookie(cookie);
            }
        }
        return "redirect:/index";
    }

}
