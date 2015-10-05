package ir.university.toosi.wtms.web.action.skin;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.apache.commons.lang.StringUtils;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Named(value = "handleSkinAction")
@SessionScoped
public class handleSkinAction implements Serializable {
    @Inject
    private UserManagementAction me;

    private String defaultSkin = "wine";

    public String getSkin() {
        if (me.getUser() != null
                && !StringUtils.isEmpty(me.getUser().getSkin())
                && getSkinList().contains(me.getUser().getSkin())) {
            return me.getUser().getSkin();
        }
        return defaultSkin;
    }

    public void setSkin(String skin) {
        this.defaultSkin = skin;
    }

    public void changeSkin(ValueChangeEvent event) {
        defaultSkin = (String) event.getNewValue();
        setCurrentUserSkin();

        FacesContext context = FacesContext.getCurrentInstance();
        String currentPage = context.getViewRoot().getViewId().replaceAll(".xhtml", ".htm");

        me.redirect(currentPage);
    }

    private void setCurrentUserSkin() {
        me.getUser().setSkin(defaultSkin);

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(me.getUser())), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSkinList() {
        List<String> skinList = new LinkedList<>();
        skinList.add("wine");
        skinList.add("ruby");
        skinList.add("deepMarine");
        return skinList;
    }

    public String getLoginImageUrl() {
        if (me.getDirection().equals("rtl")) {
            return "/images/" + getSkin() + "-persian.png";
        }
        return "/images/" + getSkin() + "-english.png";
    }
}
