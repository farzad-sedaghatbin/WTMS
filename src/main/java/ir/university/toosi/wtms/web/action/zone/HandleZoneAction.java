package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.model.entity.zone.HardwareTree;
import ir.university.toosi.wtms.web.model.entity.zone.Zone;
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
 * Date: 10/27/13
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Named(value = "handleZoneAction")
@SessionScoped
public class HandleZoneAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleGatewayAction handleGatewayAction;
    private String editable = "false";
    private DataModel<Zone> zoneList = null;
    private String zoneName;
    private Zone currentZone ;
    private Zone newZone = null;
    private int page = 1;
    private Set<Zone> selectedZones = new HashSet<>();
    private Zone selectedZone;
    private boolean selectAll;
    private boolean zoneEnabled;
    private boolean truePassControl;
    private String description;
    private String descText;
    private RulePackage selectedRulePackage;
    private String rulePackageName;
    private String calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private boolean selectRow = false;
    private DataModel<RulePackage> rulePackageList = null;
    private String zoneNameFilter;
    private String zoneDescriptionFilter;
    private List<HardwareTree> rootZones;
    private SortOrder zoneNameOrder = SortOrder.unsorted;
    private SortOrder zoneDescriptionOrder = SortOrder.unsorted;


    public String begin() {
        me.setActiveMenu(MenuType.ZONE);
        refresh();
        return "list-zone";
    }

    public String beginTree() {
        return "zone-tree";
    }

    public void selectZones(ValueChangeEvent event) {
        currentZone = zoneList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentZone.setSelected(true);
            selectedZones.add(currentZone);
        } else {
            currentZone.setSelected(false);
            selectedZones.remove(currentZone);
        }
    }

    public void changeZones(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            zoneEnabled = true;
        } else
            zoneEnabled = false;
    }


    public void selectAllZone(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Zone zone : zoneList) {
                zone.setSelected(true);
                selectedZones.add(zone);
            }
        } else {
            for (Zone zone : zoneList) {
                zone.setSelected(false);
            }
        }
        selectedZones.clear();
    }

    public DataModel<Zone> getSelectionGrid() {
        List<Zone> zones = new ArrayList<>();
        refresh();
        return zoneList;
    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllZone");

        try {
            List<Zone> zones = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Zone>>() {
            });
            for (Zone zone : zones) {
                zone.setDescText(zone.getDescription());
                zone.setEnabled(zone.isEnabled());
            }
            zoneList = new ListDataModel<>(zones);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        handleGatewayAction.refresh();
        setEditable("false");
    }

    public void doDelete() {
        currentZone.setEffectorUser(me.getUsername());

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteZone");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentZone)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/zone/list-zone.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void init() {
        zoneName = "";
        descText = "";
        page = 1;
        selectAll = false;
        zoneEnabled = false;
        currentZone = null;
        zoneDescriptionFilter="";
        zoneNameFilter="";
        setSelectRow(false);
    }

    public void edit() {
        handleGatewayAction.refresh();
        setEditable("true");
        zoneEnabled = currentZone.isEnabled();
        descText = currentZone.getDescText();
        zoneName = currentZone.getName();
        truePassControl = currentZone.isTruePass();
        List<Gateway> gatewayList = null;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findZoneById");
        try {
            currentZone = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentZone.getId())), Zone.class);

            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayByZone");

            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentZone)), new TypeReference<List<Gateway>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Gateway> selectedList = new HashSet<>();
        for (Gateway gateway : handleGatewayAction.getGateways()) {
            for (Gateway gateway1 : gatewayList) {
                if (gateway1.getId() == gateway.getId()) {
                    gateway.setSelected(true);
                    selectedList.add(gateway);
                }

            }
        }
        handleGatewayAction.setGatewayList(new ListDataModel<>(handleGatewayAction.getGateways()));
        handleGatewayAction.setSelectedGateways(selectedList);
    }


    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }

    }

    public void doEdit() {
        currentZone.setDescription(descText);
        currentZone.setName(zoneName);
        currentZone.setEnabled(zoneEnabled);
        currentZone.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editZone");
        List<Gateway> gatewayList = null;
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentZone)), String.class);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findZoneById");
            currentZone = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentZone.getId())), Zone.class);

            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayByZone");

            gatewayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentZone)), new TypeReference<List<Gateway>>() {
            });

            for (Gateway gateway : gatewayList) {
                gateway.setZone(null);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway)), String.class);
            }
            for (Gateway gateway : handleGatewayAction.getSelectedGateways()) {
                gateway.setZone(currentZone);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway)), String.class);
            }

            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-zone.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void doAdd() {
        newZone = new Zone();
        newZone.setDescription(descText);
        newZone.setName(zoneName);
        newZone.setDeleted("0");
        newZone.setEnabled(zoneEnabled);
        newZone.setTruePass(truePassControl);
        newZone.setEffectorUser(me.getUsername());
        newZone.setStatus("c");
        ////???????????????
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createZone");
        Zone insertedZone = null;
        try {
            insertedZone = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newZone)), Zone.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Gateway> list = handleGatewayAction.getSelectedGateways();
        for (Gateway gateway : list) {
            if(gateway.getZone()==null) {
                gateway.setZone(insertedZone);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
                try {
                    String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gateway)), String.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        if (insertedZone != null) {
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(newZone.getDescription());
//            languageManagement.setType(me.getLanguages());
//            try {
//
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/languageManagementMaximumId");
//                long id = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageManagement)), Long.class);
//                languageManagement.setId(id);
//
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageManagement");
//                languageManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageManagement)), LanguageManagement.class);
//
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageKeyManagement");
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/languageKeyManagementMaximumId");
//                id = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageManagement)), Long.class);
//                languageManagement.setId(id);
//                LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
//                languageKeyManagement.setDescriptionKey("Role" + id);
//                Set list = new HashSet();
//                list.add(languageManagement);
//                languageKeyManagement.setLanguageManagements(list);
//                languageKeyManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageKeyManagement)), LanguageKeyManagement.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            try {
                me.setLanguage();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-zone.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void assignRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findZoneById");
        try {
            currentZone = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentZone.getId())), Zone.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        selectedRulePackage = currentZone.getRulePackage();
        if (selectedRulePackage != null) {
            rulePackageName = selectedRulePackage.getName();
            if (selectedRulePackage.getCalendar() != null)
                calendarName = selectedRulePackage.getCalendar().getName();
            else
                calendarName = "";
            antiPassBack = selectedRulePackage.isAniPassBack();
            allowExit = selectedRulePackage.isAllowExit();
            allowExitGadget = selectedRulePackage.isAllowExitGadget();
        } else {
            rulePackageName = "";
            calendarName = "";
            antiPassBack = false;
            allowExit = false;
            allowExitGadget = false;
        }

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRulePackage");
        List<RulePackage> rulePackages = null;
        try {
            rulePackages = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<RulePackage>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        rulePackageList = new ListDataModel<>(rulePackages);
    }


    public void doAssignRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editZone");
        currentZone.setEffectorUser(me.getUsername());
        currentZone.setRulePackage(selectedRulePackage);
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentZone)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-zone.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Filter<?> getZoneNameFilterImpl() {
        return new Filter<Zone>() {
            public boolean accept(Zone zone) {
                return zoneNameFilter == null || zoneNameFilter.length() == 0 || zone.getName().toLowerCase().contains(zoneNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getZoneDescriptionFilterImpl() {
        return new Filter<Zone>() {
            public boolean accept(Zone zone) {
                return zoneDescriptionFilter == null || zoneDescriptionFilter.length() == 0 || zone.getDescription().toLowerCase().contains(zoneDescriptionFilter.toLowerCase());
            }
        };
    }

    public void sortByZoneName() {
        zoneDescriptionOrder = SortOrder.unsorted;

        if (zoneNameOrder.equals(SortOrder.ascending)) {
            setZoneNameOrder(SortOrder.descending);
        } else {
            setZoneNameOrder(SortOrder.ascending);
        }
    }

    public void sortByZoneDescription() {
        zoneNameOrder = SortOrder.unsorted;

        if (zoneDescriptionOrder.equals(SortOrder.ascending)) {
            setZoneDescriptionOrder(SortOrder.descending);
        } else {
            setZoneDescriptionOrder(SortOrder.ascending);
        }
    }

    public void selectNewRuleForZone() {
        selectedRulePackage = rulePackageList.getRowData();
        rulePackageName = selectedRulePackage.getName();
        if (selectedRulePackage.getCalendar() != null)
            calendarName = selectedRulePackage.getCalendar().getName();
        else
            calendarName = "";
        antiPassBack = selectedRulePackage.isAniPassBack();
        allowExit = selectedRulePackage.isAllowExit();
        allowExitGadget = selectedRulePackage.isAllowExitGadget();
    }

    public void selectForEdit() {
        currentZone = zoneList.getRowData();
        setSelectRow(true);

    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }


    public int getPage() {
//        currentZone = null;
        setSelectRow(false);
        return page;
    }

    public List<HardwareTree> getRootZones() {
            try {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllZone");
                List<Zone> zones = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Zone>>() {
                });
                rootZones = Zone.prepareHierarchy(zones, me);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return rootZones;
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

    public DataModel<Zone> getZoneList() {
        return zoneList;
    }

    public void setZoneList(DataModel<Zone> zoneList) {
        this.zoneList = zoneList;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Zone getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(Zone currentZone) {
        this.currentZone = currentZone;
    }

    public Zone getNewZone() {
        return newZone;
    }

    public void setNewZone(Zone newZone) {
        this.newZone = newZone;
    }

    public Set<Zone> getSelectedZones() {
        return selectedZones;
    }

    public void setSelectedZones(Set<Zone> selectedZones) {
        this.selectedZones = selectedZones;
    }

    public Zone getSelectedZone() {
        return selectedZone;
    }

    public void setSelectedZone(Zone selectedZone) {
        this.selectedZone = selectedZone;
    }

    public boolean isZoneEnabled() {
        return zoneEnabled;
    }

    public void setZoneEnabled(boolean zoneEnabled) {
        this.zoneEnabled = zoneEnabled;
    }

    public RulePackage getSelectedRulePackage() {
        return selectedRulePackage;
    }

    public void setSelectedRulePackage(RulePackage selectedRulePackage) {
        this.selectedRulePackage = selectedRulePackage;
    }

    public String getRulePackageName() {
        return rulePackageName;
    }

    public void setRulePackageName(String rulePackageName) {
        this.rulePackageName = rulePackageName;
    }

    public String getCalendarName() {
        return calendarName;
    }

    public void setCalendarName(String calendarName) {
        this.calendarName = calendarName;
    }

    public boolean isAntiPassBack() {
        return antiPassBack;
    }

    public void setAntiPassBack(boolean antiPassBack) {
        this.antiPassBack = antiPassBack;
    }

    public boolean isAllowExit() {
        return allowExit;
    }

    public void setAllowExit(boolean allowExit) {
        this.allowExit = allowExit;
    }

    public boolean isAllowExitGadget() {
        return allowExitGadget;
    }

    public void setAllowExitGadget(boolean allowExitGadget) {
        this.allowExitGadget = allowExitGadget;
    }

    public DataModel<RulePackage> getRulePackageList() {
        return rulePackageList;
    }

    public void setRulePackageList(DataModel<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public SortOrder getZoneNameOrder() {
        return zoneNameOrder;
    }

    public void setZoneNameOrder(SortOrder zoneNameOrder) {
        this.zoneNameOrder = zoneNameOrder;
    }

    public String getZoneNameFilter() {
        return zoneNameFilter;
    }

    public void setZoneNameFilter(String zoneNameFilter) {
        this.zoneNameFilter = zoneNameFilter;
    }

    public SortOrder getZoneDescriptionOrder() {
        return zoneDescriptionOrder;
    }

    public void setZoneDescriptionOrder(SortOrder zoneDescriptionOrder) {
        this.zoneDescriptionOrder = zoneDescriptionOrder;
    }

    public String getZoneDescriptionFilter() {
        return zoneDescriptionFilter;
    }

    public void setZoneDescriptionFilter(String zoneDescriptionFilter) {
        this.zoneDescriptionFilter = zoneDescriptionFilter;
    }

    public HandleGatewayAction getHandleGatewayAction() {
        return handleGatewayAction;
    }

    public void setHandleGatewayAction(HandleGatewayAction handleGatewayAction) {
        this.handleGatewayAction = handleGatewayAction;
    }

    public boolean isTruePassControl() {
        return truePassControl;
    }

    public void setTruePassControl(boolean truePassControl) {
        this.truePassControl = truePassControl;
    }
}

