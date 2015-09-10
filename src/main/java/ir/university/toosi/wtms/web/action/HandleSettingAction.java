package ir.university.toosi.wtms.web.action;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.SystemConfiguration;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleSettingAction")
@SessionScoped
public class HandleSettingAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    private int page = 1;
    private DataModel<SystemConfiguration> systemConfigurationDataModel = null;
    private List<SystemConfiguration> systemConfigurationList = null;
    private SortOrder descriptionOrder = SortOrder.UNSORTED;

    public String begin() {
//        me.setActiveMenu(MenuType.SETTING);
        refresh();
        return "system-setting";
    }

    public void changeSetting(ValueChangeEvent event) {
        SystemConfiguration systemConfiguration = systemConfigurationDataModel.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            systemConfiguration.setValue("True");
        } else {
            systemConfiguration.setValue("False");
        }
        systemConfigurationList.add(systemConfiguration);
    }


    private void refresh() {
        List<SystemConfiguration> systemConfigurations;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllConfiguration");
        try {
            systemConfigurations = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<SystemConfiguration>>() {
            });
            systemConfigurationDataModel = new ListDataModel<>(systemConfigurations);
        } catch (IOException e) {
            e.printStackTrace();
        }
        page = 1;
        systemConfigurationList = new ArrayList<>();

    }

    public void save() {
        Iterator iterator = systemConfigurationDataModel.iterator();
        while (iterator.hasNext()) {
            SystemConfiguration systemConfiguration = (SystemConfiguration) iterator.next();
            if (systemConfiguration.getType().equalsIgnoreCase("boolean"))
                continue;
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editSystemConfiguration");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(systemConfiguration)), String.class);
                if (!condition.equalsIgnoreCase("true")) {
                    me.addInfoMessage("operation.not.occurred");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (SystemConfiguration systemConfiguration : systemConfigurationList) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editSystemConfiguration");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(systemConfiguration)), String.class);
                if (!condition.equalsIgnoreCase("true")) {
                    me.addInfoMessage("operation.not.occurred");
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            me.fillSystemConfiguration();
        }

        me.initParameter();

    }

    public void sortByDescription() {
        if (descriptionOrder.equals(SortOrder.ASCENDING)) {
            setDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<SystemConfiguration> getSystemConfigurationDataModel() {
        return systemConfigurationDataModel;
    }

    public void setSystemConfigurationDataModel(DataModel<SystemConfiguration> systemConfigurationDataModel) {
        this.systemConfigurationDataModel = systemConfigurationDataModel;
    }

    public SortOrder getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(SortOrder descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }
}