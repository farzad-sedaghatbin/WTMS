package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.zone.Camera;
import ir.university.toosi.wtms.web.model.entity.zone.DeviceDataModel;
import ir.university.toosi.wtms.web.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Filter;

import javax.enterprise.context.SessionScoped;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Named(value = "handleDeviceAction")
@SessionScoped
public class HandleDeviceAction implements Serializable {
    @Inject
    private UserManagementAction me;

    private String editable = "false";
    private List<DeviceDataModel> listModel;
    private ListDataModel deviceDataModel = null;
    private List<BaseEntity> deviceList = null;
    private int page = 1;
    private SortOrder ipOrder = SortOrder.unsorted;
    private SortOrder nameOrder = SortOrder.unsorted;
    private SortOrder descriptionOrder = SortOrder.unsorted;
    private String deviceNameFilter;
    private String deviceDescriptionFilter;
    private String deviceIPFilter;

    public String beginDevice() {
        me.setActiveMenu(MenuType.HARDWARE);
        refreshDevice();
        return "pdp-monitor";
    }


    private void refreshDevice() {
//        init();
        page = 1;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPdp");

        try {
            List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<PDP>>() {
            });
            DeviceDataModel pdpDataModel;
            listModel = new ArrayList<>();
            for (PDP pdp : pdps) {
                pdpDataModel = new DeviceDataModel();
                pdpDataModel.setName(pdp.getName());
                pdpDataModel.setEnabled(pdp.isEnabled());
                pdpDataModel.setIp(pdp.getIp());
                pdpDataModel.setDescription(pdp.getDescription());
                listModel.add(pdpDataModel);
            }
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllCamera");

            List<Camera> cameras = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Camera>>() {
            });
            for (Camera camera : cameras) {
                pdpDataModel = new DeviceDataModel();
                pdpDataModel.setName(camera.getName());
                pdpDataModel.setEnabled(camera.isEnabled());
                pdpDataModel.setIp(camera.getIp());
                pdpDataModel.setDescription(camera.getDescription());
                listModel.add(pdpDataModel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ping();
        deviceDescriptionFilter = "";
        deviceIPFilter = "";
        deviceNameFilter = "";
    }

    public void ping() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/ping");
        for (DeviceDataModel pdpDataModel : listModel) {

            try {
                String status = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(pdpDataModel.getIp())), String.class);
                if ("true".equalsIgnoreCase(status))
                    pdpDataModel.setEnabled(true);
                else
                    pdpDataModel.setEnabled(false);

            } catch (IOException e) {
                e.printStackTrace();
            }

            deviceDataModel = new ListDataModel<>(listModel);

        }
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    public void sortByName() {
        nameOrder = newSortOrder(nameOrder);
    }

    public void sortByDescription() {
        descriptionOrder = newSortOrder(descriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        ipOrder = SortOrder.unsorted;
        nameOrder = SortOrder.unsorted;
        descriptionOrder = SortOrder.unsorted;

        if (currentSortOrder.equals(SortOrder.descending)) {
            return SortOrder.ascending;
        } else {
            return SortOrder.descending;
        }

    }
    public Filter<?> getDeviceIPFilterImpl() {
        return new Filter<DeviceDataModel>() {
            public boolean accept(DeviceDataModel device) {
                return deviceIPFilter == null || deviceIPFilter.length() == 0 || device.getIp().startsWith(deviceIPFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getDeviceNameFilterImpl() {
        return new Filter<DeviceDataModel>() {
            public boolean accept(DeviceDataModel device) {
                return deviceNameFilter == null || deviceNameFilter.length() == 0 || device.getName().toLowerCase().contains(deviceNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getDeviceDescriptionFilterImpl() {
        return new Filter<DeviceDataModel>() {
            public boolean accept(DeviceDataModel device) {
                return deviceDescriptionFilter == null || deviceDescriptionFilter.length() == 0 || device.getDescription().toLowerCase().contains(deviceDescriptionFilter.toLowerCase());
            }
        };
    }
    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public List<DeviceDataModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<DeviceDataModel> listModel) {
        this.listModel = listModel;
    }

    public ListDataModel getDeviceDataModel() {
        return deviceDataModel;
    }

    public void setDeviceDataModel(ListDataModel deviceDataModel) {
        this.deviceDataModel = deviceDataModel;
    }

    public List<BaseEntity> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<BaseEntity> deviceList) {
        this.deviceList = deviceList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SortOrder getIpOrder() {
        return ipOrder;
    }

    public void setIpOrder(SortOrder ipOrder) {
        this.ipOrder = ipOrder;
    }

    public SortOrder getDescriptionOrder() {
        return descriptionOrder;
    }

    public void setDescriptionOrder(SortOrder descriptionOrder) {
        this.descriptionOrder = descriptionOrder;
    }

    public SortOrder getNameOrder() {
        return nameOrder;
    }

    public void setNameOrder(SortOrder nameOrder) {
        this.nameOrder = nameOrder;
    }

    public String getDeviceNameFilter() {
        return deviceNameFilter;
    }

    public void setDeviceNameFilter(String deviceNameFilter) {
        this.deviceNameFilter = deviceNameFilter;
    }

    public String getDeviceDescriptionFilter() {
        return deviceDescriptionFilter;
    }

    public void setDeviceDescriptionFilter(String deviceDescriptionFilter) {
        this.deviceDescriptionFilter = deviceDescriptionFilter;
    }

    public String getDeviceIPFilter() {
        return deviceIPFilter;
    }

    public void setDeviceIPFilter(String deviceIPFilter) {
        this.deviceIPFilter = deviceIPFilter;
    }
}

