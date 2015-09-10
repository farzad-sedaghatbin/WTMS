package ir.university.toosi.wtms.web.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.User;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.inject.Inject;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@WebFilter(urlPatterns = {
        "/authorization/*",
        "/report/*",
        "/business-rules/*",
        "/calendar/*",
        "/card/*",
        "/comment/*",
        "/operation/*",
        "/event/*",
        "/language/*",
        "/lookup/*",
        "/role/*",
        "/user/*",
        "/workgroup/*",
        "/map/*",
        "/menu/*",
        "/monitoring/*",
        "/organ/*",
        "/pc/*",
        "/person/*",
        "/setting/*",
        "/traffic/*",
        "/zone/*",
        "/bLookup//*",
        "/home.htm",
        "/exception.htm",
        "/password.htm",
        "/template-popup.htm",
        "/template-reset-password.htm",
        "/template.htm",
        "/menu/menu.htm",
        "/change-password.htm",
        "/progress.htm",
        "/top.htm",
        "/menuHandler"
})
public class RestrictFilter implements Filter {

    @Inject
    private GeneralHelper generalHelper;

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect(request.getRequestURL().substring(0, request.getRequestURL().indexOf(Configuration.getProperty("app.name")) + Configuration.getProperty("app.name").length()) + "/login.htm");
        } else {
            WebServiceInfo loginService = new WebServiceInfo();
            loginService.setServiceName("/findUserByUserName");
            User currentUser = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(loginService.getServerUrl(), loginService.getServiceName(), session.getAttribute("username").toString()), User.class);
            //generalHelper.getUserService().findByUsername(session.getAttribute("username").toString());
            boolean flag = true;
//            for(WorkGroup workGroup:currentUser.getWorkGroups()){
//            for (Role role : workGroup.getRoles()) {
//                if (role.getName().equals(request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1, request.getRequestURI().lastIndexOf(".")))) {
//                    flag = true;
//                    break;
//                }
//                if(flag)
//                    break;
//            }
//            }
            if (currentUser == null || currentUser.getWorkGroups().iterator().next().getRoles().size() == 0) {
                flag = false;
            }
            if (flag) {
                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                session.invalidate();
                response.sendRedirect(request.getRequestURL().substring(0, request.getRequestURL().indexOf(Configuration.getProperty("app.name")) + Configuration.getProperty("app.name").length()) + "/login.htm");
            }

        }
    }

    public void destroy() {

    }
}