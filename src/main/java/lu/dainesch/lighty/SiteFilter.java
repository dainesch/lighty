package lu.dainesch.lighty;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lu.dainesch.lighty.model.SiteModel;
import lu.dainesch.lighty.model.UserModel;


@WebFilter(asyncSupported = true, urlPatterns = "/site/*")
public class SiteFilter implements Filter {

    public static final String KEY_COOKIE = "lightyKey";
    public static final String NAME_COOKIE = "lightyUser";
    public static final String PASS_COOKIE = "lightyPass";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Inject
    private UserModel userMod;
    @Inject
    private AppConfig appConf;
    @Inject
    private SiteModel siteMod;
    @Inject
    private UserStore store;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;

            siteMod.readAppConf(appConf);

            // read cookies
            if (req.getCookies() != null) {
                for (Cookie cookie : req.getCookies()) {
                    if (KEY_COOKIE.equals(cookie.getName())) {
                        userMod.setKey(cookie.getValue());
                    } else if (NAME_COOKIE.equals(cookie.getName()) && cookie.getValue() != null && !cookie.getValue().trim().isEmpty()) {
                        userMod.setName(cookie.getValue());
                    } else if (PASS_COOKIE.equals(cookie.getName()) && cookie.getValue().equals(appConf.getAdminPass())) {
                        userMod.setAdmin(true);
                        store.addAdminUser(userMod.getKey());
                    }
                }
            }
            if (userMod.getName() != null && userMod.getKey() != null) {
                userMod.setLoggedIn(true);
            }

            if (userMod.getKey() == null) {
                // first or new
                userMod.setKey(new BigInteger(130, RANDOM).toString(32));
                Cookie cookie = new Cookie(KEY_COOKIE, userMod.getKey());
                cookie.setPath("/");
                resp.addCookie(cookie);
                if (userMod.getName() != null && !userMod.getName().trim().isEmpty()) {
                    cookie = new Cookie(NAME_COOKIE, userMod.getName());
                    cookie.setPath("/");
                    resp.addCookie(cookie);
                    store.addNewUser(userMod.getKey(), userMod.getName());
                }

            } else if (store.getUserName(userMod.getKey()) == null) {
                store.addNewUser(userMod.getKey(), userMod.getName());
            }

            userMod.setAdmin(store.isAdminUser(userMod.getKey()));

        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

}
