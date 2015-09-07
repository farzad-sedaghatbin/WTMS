package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.zone.*;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Filter;

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Named(value = "handlePDPAction")
@SessionScoped
public class HandlePDPAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    private String editable = "false";

    private DataModel<PDP> pdpList = null;
    private DataModel<DeviceDataModel> pdpListModel = null;
    private List<DeviceDataModel> listModel = null;
    private DataModel<Gateway> gatewayGrid = null;
    private String pdpName;
    private Gateway gateway;
    private PDP currentPdp = null;
    private PDP newPdp = null;
    private int page = 1;
    private Set<PDP> selectedPdps = new HashSet<>();
    private PDP selectedPdp;
    private boolean selectAll;
    private boolean fingers;
    private boolean schedule;
    private boolean db;
    private boolean pictures;
    private boolean pdpEnabled;
    private String description;
    private String descText;
    private String ip;
    private String gatewayId;
    private String cameraId;
    private SelectItem[] cameraItems;
    private SelectItem[] gatewayItems;
    private boolean selectRow = false;
    private boolean finger = false;
    private boolean online = false;
    private boolean entrance = false;
    private SortOrder pdpNameOrder = SortOrder.unsorted;
    private SortOrder pdpDescriptionOrder = SortOrder.unsorted;
    private SortOrder ipOrder = SortOrder.unsorted;
    private String pdpNameFilter;
    private String pdpDescriptionFilter;
    private String pdpIPFilter;


    public String begin() {
        me.setActiveMenu(MenuType.HARDWARE);
        refresh();
        return "list-pdp";
    }

    public String beginDevice() {
        refreshDevice();
        return "pdp-monitor";
    }

    public void selectPdps(ValueChangeEvent event) {
        currentPdp = pdpList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentPdp.setSelected(true);
            selectedPdps.add(currentPdp);
        } else {
            currentPdp.setSelected(false);
            for (PDP pdp : pdpList) {
                if (pdp.getId() == currentPdp.getId())
                    selectedPdps.remove(pdp);
            }
        }
    }

    public void changePdps(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            pdpEnabled = true;
        } else
            pdpEnabled = false;
    }


    public void selectAllPdp(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (PDP pdp : pdpList) {
                pdp.setSelected(true);
                selectedPdps.add(pdp);
            }
        } else {
            for (PDP pdp : pdpList) {
                pdp.setSelected(false);
            }
            selectedPdps.clear();
        }
    }

    public DataModel<PDP> getSelectionGrid() {
        List<PDP> pdps = new ArrayList<>();
        refresh();
        return pdpList;
    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPdp");

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        try {
            List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<PDP>>() {
            });
            for (PDP pdp : pdps) {
                pdp.setDescText(pdp.getDescription());
                pdp.setNameText(pdp.getName());
            }
            pdpList = new ListDataModel<>(pdps);
            for (PDP pdp : pdpList) {
                pdp.setSelected(false);
            }
            selectedPdps = new HashSet<>();
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");

            List<Gateway> gateways = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllCamera");

            List<Camera> cameras = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Camera>>() {
            });
            cameraItems = new SelectItem[cameras.size()];
            gatewayItems = new SelectItem[gateways.size()];
            int i = 0;
            for (Camera camera : cameras) {
                cameraItems[i++] = new SelectItem(camera.getId(), camera.getName());
            }
            i = 0;
            for (Gateway gateway1 : gateways) {
                gatewayItems[i++] = new SelectItem(gateway1.getId(), gateway1.getName());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshDevice() {
//        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPdp");

        handleGatewayAction.setSelectedGateways(new HashSet<Gateway>());
        try {
            List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<PDP>>() {
            });
            DeviceDataModel pdpDataModel = new DeviceDataModel();
            listModel = new ArrayList<>();
            for (PDP pdp : pdps) {
                pdpDataModel = new DeviceDataModel();
                pdpDataModel.setName(pdp.getName());
                pdpDataModel.setEnabled(pdp.isEnabled());
                pdpDataModel.setIp(pdp.getIp());
                pdpDataModel.setDescription(pdp.getDescription());
                listModel.add(pdpDataModel);
            }
            pdpListModel = new ListDataModel<>(listModel);


        } catch (IOException e) {
            e.printStackTrace();
        }
        ping();
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

            pdpListModel = new ListDataModel<>(listModel);

        }
    }

    public void add() {
        init();
        gatewayGrid = handleGatewayAction.getSelectionGrid();
        setEditable("false");
    }

    public void synch() {
        me.setActiveMenu(MenuType.SEND_RECEIVE);
        finger = false;
        refresh();
    }

    public void finger() {
        me.setActiveMenu(MenuType.SEND_RECEIVE);
        finger = true;
        refresh();
    }

    public void synchronize() {
        if (!finger) {
            PDPSync pdpSync = new PDPSync();
            pdpSync.setDb(db);
            pdpSync.setFinger(fingers);
            pdpSync.setPdpList(selectedPdps);
            pdpSync.setPicture(pictures);
            pdpSync.setSchedule(schedule);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/synchronizePdp");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(pdpSync)), String.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getFinger");
            try {
                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(selectedPdps)), String.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        me.redirect("/home.htm");
    }

    public void synchronizeOneByOne() {
        PDPSync pdpSync = new PDPSync();
        pdpSync.setFinger(fingers);
        pdpSync.setPdpList(selectedPdps);
        pdpSync.setPicture(pictures);
        pdpSync.setPerson(handlePersonAction.getCurrentPerson());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/synchronizeOnePdp");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(pdpSync)), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.redirect("/person/list-person.htm");
    }

    public void ruleInitialize() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/ruleInitialize");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doDelete() {

        currentPdp.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");
        List<Gateway> gatewayList = null;
        try {
            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean flag = false;
        PDP removablePdp = null;
//        for (Gateway gateway1 : gatewayList) {
//            for (Camera camera : gateway1.getCameras()) {
//                if (camera.getId() == currentCamera.getId()) {
//                    flag = true;
//                    removableCamera = camera;
//                }
//            }
//            if (flag) {
//                flag = false;
//                gateway1.getCameras().remove(removableCamera);
//            }
//            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
//            try {
//                String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1)), String.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deletePdp");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPdp)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/zone/list-pdp.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void init() {
        pdpName = "";
        descText = "";
        ip = "";
        page = 1;
        cameraId = null;
        selectAll = false;
        pdpEnabled = false;
        currentPdp = null;
        selectAll = false;
        fingers = false;
        schedule = false;
        db = false;
        pictures = false;
        pdpEnabled = false;
        setSelectRow(false);
        pdpDescriptionFilter = "";
        pdpIPFilter = "";
        pdpNameFilter = "";
    }

    public void edit() {
        setEditable("true");
        pdpEnabled = currentPdp.isEnabled();
        ip = currentPdp.getIp();
        descText = currentPdp.getDescText();
        pdpName = currentPdp.getNameText();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPdpById");
        try {
            currentPdp = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentPdp.getId())), PDP.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (currentPdp.getCamera() != null)
            cameraId = String.valueOf(currentPdp.getCamera().getId());
        gatewayId = String.valueOf(currentPdp.getGateway().getId());
        selectedPdp = currentPdp;
        entrance = currentPdp.isEntrance();
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        selectedPdp.setDescription(descText);
        selectedPdp.setName(pdpName);
        selectedPdp.setIp(ip);
        selectedPdp.setEnabled(pdpEnabled);
        selectedPdp.setEntrance(entrance);
        selectedPdp.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayById");
        Gateway gateway = null;
        Camera camera = null;
        try {
            gateway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), gatewayId), Gateway.class);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findCameraById");
            if (cameraId != null && !cameraId.equalsIgnoreCase("") )
                camera = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), cameraId), Camera.class);

        } catch (IOException e) {
        }
        boolean flag = false;
        selectedPdp.setGateway(gateway);
        selectedPdp.setCamera(camera);
        selectedPdp.setOnline(online);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existPDP");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(selectedPdp)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("pdp.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editPdp");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(selectedPdp)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-pdp.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doAdd() {
        newPdp = new PDP();
        newPdp.setDescription(descText);
        newPdp.setName(pdpName);
        newPdp.setDeleted("0");
        newPdp.setEnabled(pdpEnabled);
        newPdp.setStatus("c");
        newPdp.setIp(ip);
        newPdp.setOnline(online);
        newPdp.setEntrance(entrance);
        newPdp.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayById");
        Gateway gateway = null;
        Camera camera = null;
        try {
            gateway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), gatewayId), Gateway.class);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findCameraById");
            if (cameraId != null && !cameraId.equalsIgnoreCase("") )
                camera = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), cameraId), Camera.class);



        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        boolean flag = false;
        newPdp.setGateway(gateway);
        newPdp.setCamera(camera);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/PDPexistNotId");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newPdp)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("pdp.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createPdp");
        PDP insertedPdp = null;
        try {
            insertedPdp = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newPdp)), PDP.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (insertedPdp != null) {
//            for (Gateway gateway1 : selecteGateway) {
//                gateway1.getCameras().add(insertedPdp);
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
//                try {
//                    gateway1 = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway1)), Gateway.class);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-pdp.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


    public Filter<?> getPDPIPFilterImpl() {
        return new Filter<PDP>() {
            public boolean accept(PDP pdp) {
                return pdpIPFilter == null || pdpIPFilter.length() == 0 || pdp.getIp().startsWith(pdpIPFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPdpNameFilterImpl() {
        return new Filter<PDP>() {
            public boolean accept(PDP pdp) {
                return pdpNameFilter == null || pdpNameFilter.length() == 0 || pdp.getName().toLowerCase().contains(pdpNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPdpDescriptionFilterImpl() {
        return new Filter<PDP>() {
            public boolean accept(PDP pdp) {
                return pdpDescriptionFilter == null || pdpDescriptionFilter.length() == 0 || pdp.getDescription().toLowerCase().contains(pdpDescriptionFilter.toLowerCase());
            }
        };
    }

    public void sortByPdpName() {
        pdpNameOrder = newSortOrder(pdpNameOrder);
    }

    public void sortByPdpDescription() {
        pdpDescriptionOrder = newSortOrder(pdpDescriptionOrder);
    }

    public void sortByIp() {
        ipOrder = newSortOrder(ipOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        pdpNameOrder = SortOrder.unsorted;
        pdpDescriptionOrder = SortOrder.unsorted;
        ipOrder = SortOrder.unsorted;

        if (currentSortOrder.equals(SortOrder.descending)) {
            return SortOrder.ascending;
        } else {
            return SortOrder.descending;
        }

    }


    public void selectForEdit() {
        currentPdp = pdpList.getRowData();
        setSelectRow(true);
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
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

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }


    public int getPage() {
        currentPdp = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
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

    public DataModel<PDP> getPdpList() {
        return pdpList;
    }

    public void setPdpList(DataModel<PDP> pdpList) {
        this.pdpList = pdpList;
    }

    public String getPdpName() {
        return pdpName;
    }

    public void setPdpName(String pdpName) {
        this.pdpName = pdpName;
    }

    public PDP getCurrentPdp() {
        return currentPdp;
    }

    public void setCurrentPdp(PDP currentPdp) {
        this.currentPdp = currentPdp;
    }

    public PDP getNewPdp() {
        return newPdp;
    }

    public void setNewPdp(PDP newPdp) {
        this.newPdp = newPdp;
    }

    public Set<PDP> getSelectedPdps() {
        return selectedPdps;
    }

    public void setSelectedPdps(Set<PDP> selectedPdps) {
        this.selectedPdps = selectedPdps;
    }

    public PDP getSelectedPdp() {
        return selectedPdp;
    }

    public void setSelectedPdp(PDP selectedPdp) {
        this.selectedPdp = selectedPdp;
    }

    public boolean isPdpEnabled() {
        return pdpEnabled;
    }

    public void setPdpEnabled(boolean pdpEnabled) {
        this.pdpEnabled = pdpEnabled;
    }

    public SelectItem[] getCameraItems() {
        return cameraItems;
    }

    public void setCameraItems(SelectItem[] cameraItems) {
        this.cameraItems = cameraItems;
    }

    public SelectItem[] getGatewayItems() {
        return gatewayItems;
    }

    public void setGatewayItems(SelectItem[] gatewayItems) {
        this.gatewayItems = gatewayItems;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public boolean isFinger() {
        return finger;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    public SortOrder getPdpNameOrder() {
        return pdpNameOrder;
    }

    public void setPdpNameOrder(SortOrder pdpNameOrder) {
        this.pdpNameOrder = pdpNameOrder;
    }

    public String getPdpNameFilter() {
        return pdpNameFilter;
    }

    public void setPdpNameFilter(String pdpNameFilter) {
        this.pdpNameFilter = pdpNameFilter;
    }

    public SortOrder getPdpDescriptionOrder() {
        return pdpDescriptionOrder;
    }

    public void setPdpDescriptionOrder(SortOrder pdpDescriptionOrder) {
        this.pdpDescriptionOrder = pdpDescriptionOrder;
    }

    public String getPdpDescriptionFilter() {
        return pdpDescriptionFilter;
    }

    public void setPdpDescriptionFilter(String pdpDescriptionFilter) {
        this.pdpDescriptionFilter = pdpDescriptionFilter;
    }

    public boolean isFingers() {
        return fingers;
    }

    public void setFingers(boolean fingers) {
        this.fingers = fingers;
    }

    public boolean isSchedule() {
        return schedule;
    }

    public void setSchedule(boolean schedule) {
        this.schedule = schedule;
    }

    public boolean isDb() {
        return db;
    }

    public void setDb(boolean db) {
        this.db = db;
    }

    public boolean isPictures() {
        return pictures;
    }

    public void setPictures(boolean pictures) {
        this.pictures = pictures;
    }

    public DataModel<DeviceDataModel> getPdpListModel() {
        return pdpListModel;
    }

    public void setPdpListModel(DataModel<DeviceDataModel> pdpListModel) {
        this.pdpListModel = pdpListModel;
    }

    public List<DeviceDataModel> getListModel() {
        return listModel;
    }

    public void setListModel(List<DeviceDataModel> listModel) {
        this.listModel = listModel;
    }

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    public SortOrder getIpOrder() {
        return ipOrder;
    }

    public void setIpOrder(SortOrder ipOrder) {
        this.ipOrder = ipOrder;
    }

    public HandlePersonAction getHandlePersonAction() {
        return handlePersonAction;
    }

    public void setHandlePersonAction(HandlePersonAction handlePersonAction) {
        this.handlePersonAction = handlePersonAction;
    }

    public String getPdpIPFilter() {
        return pdpIPFilter;
    }

    public void setPdpIPFilter(String pdpIPFilter) {
        this.pdpIPFilter = pdpIPFilter;
    }
}

