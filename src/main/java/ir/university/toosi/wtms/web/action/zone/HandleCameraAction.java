package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.zone.Camera;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Filter;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: behzad
 * Date: 10/6/13
 * Time: 12:52 AM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "handleCameraAction")
@SessionScoped
public class HandleCameraAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;

    private String editable = "false";

    private DataModel<Camera> cameraList = null;
    private DataModel<Gateway> gatewayGrid = null;
    private String cameraName;
    private Gateway gateway;
    private Camera newCamera = null;
    private int page = 1;
    private Set<Camera> selectedCameras = new HashSet<>();
    private Camera selectedCamera;
    private boolean selectAll;
    private boolean cameraEnabled;
    private Camera currentCamera = null;
    private boolean selectRow = false;
    private String description;
    private String descText;
    private String ip;
    private String frames;
    private String cameraNameFilter;
    private String cameraDescFilter;
    private String cameraIPFilter;
    private SortOrder cameraNameOrder = SortOrder.unsorted;
    private SortOrder ipOrder = SortOrder.unsorted;
    private SortOrder descriptionOrder = SortOrder.unsorted;


    public void begin() {
        me.setActiveMenu(MenuType.HARDWARE);
        refresh();
        //return "list-camera";
    }

    public void selectCameras(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentCamera.setSelected(true);
            selectedCameras.add(currentCamera);
        } else {
            currentCamera.setSelected(false);
            selectedCameras.remove(currentCamera);
        }
    }

    public void changeCameras(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            cameraEnabled = true;
        } else
            cameraEnabled = false;
    }


    public void selectAllCamera(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Camera camera : cameraList) {
                camera.setSelected(true);
                selectedCameras.add(camera);
            }
        } else {
            for (Camera camera : cameraList) {
                camera.setSelected(false);
            }
        }
        selectedCameras.clear();
    }

    public DataModel<Camera> getSelectionGrid() {
        List<Camera> cameras = new ArrayList<>();
        refresh();
        return cameraList;
    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllCamera");

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        try {
            List<Camera> cameras = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Camera>>() {
            });
            cameraList = new ListDataModel<>(cameras);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        gatewayGrid = handleGatewayAction.getSelectionGrid();
        setEditable("false");
    }

    public void doDelete() {
        currentCamera.setEffectorUser(me.getUsername());

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPdpByCameraId");
        List<PDP> pdpList = null;
        try {
            pdpList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(),String.valueOf(currentCamera.getId())), new TypeReference<List<PDP>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(pdpList!=null && pdpList.size()>0){
            me.addInfoMessage("camera.pdp");
            me.redirect("/zone/list-camera.htm");
        }

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");
        List<Gateway> gatewayList = null;
        try {
            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        Camera removableCamera = null;
        for (Gateway gateway1 : gatewayList) {
            for (Camera camera : gateway1.getCameras()) {
                if (camera.getId() == currentCamera.getId()) {
                    flag = true;
                    removableCamera = camera;
                }
            }
            if (flag) {
                flag = false;
                gateway1.getCameras().remove(removableCamera);
            }
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1)), String.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteCamera");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentCamera)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/zone/list-camera.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void init() {
        cameraName = "";
        descText = "";
        ip = "";
        selectAll = false;
        cameraEnabled = false;
        page = 1;
        frames = "";
        handleGatewayAction.init();
        currentCamera = null;
        cameraNameFilter = "";
        cameraDescFilter = "";
        setSelectRow(false);
    }

    public void edit() {

        setEditable("true");
        cameraEnabled = currentCamera.isEnabled();
        ip = currentCamera.getIp();
        descText = currentCamera.getDescription();
        cameraName = currentCamera.getName();
        frames = String.valueOf(currentCamera.getFrames());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findCameraById");
        try {
            currentCamera = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentCamera.getId())), Camera.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");
        List<Gateway> gatewayList = null;
        try {
            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        for (Gateway gateway : gatewayList) {
            gateway.setDescription(me.getValue(gateway.getDescription()));
        }
        for (Gateway gateway : gatewayList) {
            for (Camera camera : gateway.getCameras()) {

                if ((camera.getId() == currentCamera.getId())) {
                    gateway.setSelected(true);
                    handleGatewayAction.getSelectedGateways().add(gateway);
                }
            }
        }
        handleGatewayAction.setGatewayList(new ListDataModel<>(gatewayList));
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        currentCamera.setDescription(descText);
        currentCamera.setName(cameraName);
        currentCamera.setIp(ip);
        currentCamera.setEnabled(cameraEnabled);
        currentCamera.setFrames(Long.valueOf(frames));
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existCamera");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentCamera)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("camera.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");
        List<Gateway> gatewayList = null;
        try {
            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        Camera removableCamera = null;
        for (Gateway gateway1 : gatewayList) {
            for (Camera camera : gateway1.getCameras()) {
                if (camera.getId() == currentCamera.getId()) {
                    flag = true;
                    removableCamera = camera;
                }
            }
            if (flag) {
                flag = false;
                gateway1.getCameras().remove(removableCamera);
            }
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1)), String.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Gateway gateway1 : handleGatewayAction.getSelectedGateways()) {
            for (Gateway gateway2 : gatewayList) {
                if (gateway1.getId() == gateway2.getId()) {
                    gateway2.getCameras().add(currentCamera);
                    me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
                    try {
                        String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway2)), String.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editCamera");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentCamera)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-camera.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doAdd() {
        newCamera = new Camera();
        newCamera.setDescription(descText);
        newCamera.setName(cameraName);
        newCamera.setDeleted("0");
        newCamera.setEnabled(cameraEnabled);
        newCamera.setStatus("c");
        newCamera.setEffectorUser(me.getUsername());
        newCamera.setIp(ip);
        newCamera.setFrames(Long.valueOf(frames));

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/CameraexistNotId");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newCamera)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("camera.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<Gateway> selecteGateway = new HashSet<>();
        for (Gateway gateway : handleGatewayAction.getSelectedGateways()) {

            if (gateway.isSelected()) {

                selecteGateway.add(gateway);
            }
        }
        if (selecteGateway.size() == 0) {
            me.addErrorMessage("no_operation_selected");
            return;
        }

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createCamera");
        Camera insertedCamera = null;
        try {
            insertedCamera = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newCamera)), Camera.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedCamera != null) {
            for (Gateway gateway1 : selecteGateway) {
                gateway1.getCameras().add(insertedCamera);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
                try {
                    new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-camera.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public Filter<?> getCameraNameFilterImpl() {
        return new Filter<Camera>() {
            public boolean accept(Camera camera) {
                return cameraNameFilter == null || cameraNameFilter.length() == 0 || camera.getName().toLowerCase().contains(cameraNameFilter.toLowerCase());
            }
        };
    }    public Filter<?> getCameraDescFilterImpl() {
        return new Filter<Camera>() {
            public boolean accept(Camera camera) {
                return cameraDescFilter == null || cameraDescFilter.length() == 0 || camera.getDescription().toLowerCase().contains(cameraDescFilter.toLowerCase());
            }
        };
    }
    public Filter<?> getCameraIPFilterImpl() {
        return new Filter<Camera>() {
            public boolean accept(Camera camera) {
                return cameraIPFilter == null || cameraIPFilter.length() == 0 || camera.getIp().contains(cameraIPFilter.toLowerCase());
            }
        };
    }




    public void sortByCameraName() {
        cameraNameOrder = newSortOrder(cameraNameOrder);
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    public void sortByDescription() {
        descriptionOrder = newSortOrder(descriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        ipOrder = SortOrder.unsorted;
        cameraNameOrder = SortOrder.unsorted;
        descriptionOrder = SortOrder.unsorted;

        if (currentSortOrder.equals(SortOrder.descending)) {
            return SortOrder.ascending;
        } else {
            return SortOrder.descending;
        }
    }

    public void selectForEdit() {
        currentCamera = cameraList.getRowData();
        setSelectRow(true);
    }


    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<Gateway> getGatewayGrid() {
        return gatewayGrid;
    }

    public void setGatewayGrid(DataModel<Gateway> gatewayGrid) {
        this.gatewayGrid = gatewayGrid;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }


    public DataModel<Camera> getCameraList() {
        return cameraList;
    }

    public void setCameraList(DataModel<Camera> cameraList) {
        this.cameraList = cameraList;
    }


    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }

    public void setCurrentCamera(Camera currentCamera) {
        this.currentCamera = currentCamera;
    }

    public Camera getNewCamera() {
        return newCamera;
    }

    public void setNewCamera(Camera newCamera) {
        this.newCamera = newCamera;
    }

    public int getPage() {
        currentCamera = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Set<Camera> getSelectedCameras() {
        return selectedCameras;
    }

    public void setSelectedCameras(Set<Camera> selectedCameras) {
        this.selectedCameras = selectedCameras;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public HandleGatewayAction getHandleGatewayAction() {
        return handleGatewayAction;
    }

    public void setHandleGatewayAction(HandleGatewayAction handleGatewayAction) {
        this.handleGatewayAction = handleGatewayAction;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Camera getSelectedCamera() {
        return selectedCamera;
    }

    public void setSelectedCamera(Camera selectedCamera) {
        this.selectedCamera = selectedCamera;
    }

    public boolean isCameraEnabled() {
        return cameraEnabled;
    }

    public void setCameraEnabled(boolean cameraEnabled) {
        this.cameraEnabled = cameraEnabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFrames() {
        return frames;
    }

    public void setFrames(String frames) {
        this.frames = frames;
    }

    public SortOrder getCameraNameOrder() {
        return cameraNameOrder;
    }

    public void setCameraNameOrder(SortOrder cameraNameOrder) {
        this.cameraNameOrder = cameraNameOrder;
    }

    public String getCameraNameFilter() {
        return cameraNameFilter;
    }

    public void setCameraNameFilter(String cameraNameFilter) {
        this.cameraNameFilter = cameraNameFilter;
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

    public String getCameraIPFilter() {
        return cameraIPFilter;
    }

    public void setCameraIPFilter(String cameraIPFilter) {
        this.cameraIPFilter = cameraIPFilter;
    }

    public String getCameraDescFilter() {
        return cameraDescFilter;
    }

    public void setCameraDescFilter(String cameraDescFilter) {
        this.cameraDescFilter = cameraDescFilter;
    }
}
