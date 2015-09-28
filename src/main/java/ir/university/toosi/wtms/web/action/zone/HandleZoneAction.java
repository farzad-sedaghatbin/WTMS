package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.Operation;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.ZoneServiceImpl;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.HardwareTree;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;


import javax.ejb.EJB;
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

    @EJB
    private ZoneServiceImpl zoneService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private RulePackageServiceImpl rulePackageService;


    private String editable = "false";
    private DataModel<Zone> zoneList = null;
    private String zoneName;
    private Zone currentZone;
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
    private List<RulePackage> rulePackageList = null;
    private String zoneNameFilter;
    private String zoneDescriptionFilter;
    private List<HardwareTree> rootZones;
    private SortOrder zoneNameOrder = SortOrder.UNSORTED;
    private SortOrder zoneDescriptionOrder = SortOrder.UNSORTED;
    private boolean disableFields;


    public void begin() {
        me.setActiveMenu(MenuType.ZONE);
        refresh();
        me.redirect("/zone/list-zone.xhtml");
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
        List<Zone> zones = zoneService.getAllZone();
        for (Zone zone : zones) {
            zone.setDescText(zone.getDescription());
            zone.setEnabled(zone.isEnabled());
        }
        zoneList = new ListDataModel<>(zones);
    }

    public void onTransfer(TransferEvent event) {
        if (event.isAdd()) {
            for (Object item : event.getItems()) {
                ((Operation) item).setSelected(true);
                handleGatewayAction.getSelectedGateways().add((Gateway) item);
                if (handleGatewayAction.getGatewayDualsForZone().getTarget() != null)
                    handleGatewayAction.getGatewayDualsForZone().getTarget().add((Gateway) item);
                if (handleGatewayAction.getGatewayDualsForZone().getSource() != null)
                    handleGatewayAction.getGatewayDualsForZone().getSource().remove(item);
            }
        } else {
            for (Object item : event.getItems()) {
                ((Operation) item).setSelected(false);
                handleGatewayAction.getSelectedGateways().remove(item);
                if (handleGatewayAction.getGatewayDualsForZone().getTarget() != null)
                    handleGatewayAction.getGatewayDualsForZone().getTarget().remove(item);
                if (handleGatewayAction.getGatewayDualsForZone().getSource() != null)
                    handleGatewayAction.getGatewayDualsForZone().getSource().add((Gateway) item);
            }
        }
    }

    public void add() {
        init();
        handleGatewayAction.refresh();
        handleGatewayAction.setGatewayDualsForZone(new DualListModel<Gateway>(handleGatewayAction.getGateways(),new ArrayList<Gateway>()));
        setEditable("false");
        setDisableFields(false);
    }

    public void doDelete() {
        currentZone.setEffectorUser(me.getUsername());

        String condition = zoneService.deleteZone(currentZone);
        refresh();
        me.addInfoMessage(condition);
        me.redirect("/zone/list-zone.xhtml");
    }

    public void init() {
        zoneName = "";
        descText = "";
        page = 1;
        selectAll = false;
        zoneEnabled = false;
        currentZone = null;
        zoneDescriptionFilter = "";
        zoneNameFilter = "";
        setSelectRow(false);
    }

    public void view() {
        setDisableFields(true);
        handleGatewayAction.refresh();
        zoneEnabled = currentZone.isEnabled();
        descText = currentZone.getDescText();
        zoneName = currentZone.getName();
        truePassControl = currentZone.isTruePass();
        List<Gateway> gatewayList = null;
        currentZone = zoneService.findById(currentZone.getId());

        gatewayList = gatewayService.findByZone(currentZone);
        Set<Gateway> selectedList = new HashSet<>();

        List<Gateway> sourceGateways = new ArrayList<>();
        List<Gateway> targetGateways = new ArrayList<>();

        for (Gateway gateway : handleGatewayAction.getGateways()) {
            for (Gateway gateway1 : gatewayList) {
                if (gateway1.getId() == gateway.getId()) {
                    gateway.setSelected(true);
                    selectedList.add(gateway);
                    targetGateways.add(gateway);
                }else {
                    sourceGateways.add(gateway);
                }

            }
        }
        handleGatewayAction.setGatewayList(handleGatewayAction.getGateways());
        handleGatewayAction.setSelectedGateways(selectedList);
        handleGatewayAction.setGatewayDualsForZone(new DualListModel<Gateway>(sourceGateways,targetGateways));
    }


    public void edit() {
        handleGatewayAction.refresh();
        setEditable("true");
        setDisableFields(false);
        zoneEnabled = currentZone.isEnabled();
        descText = currentZone.getDescText();
        zoneName = currentZone.getName();
        truePassControl = currentZone.isTruePass();
        List<Gateway> gatewayList = null;
        currentZone = zoneService.findById(currentZone.getId());

        gatewayList = gatewayService.findByZone(currentZone);
        Set<Gateway> selectedList = new HashSet<>();

        List<Gateway> sourceGateways = new ArrayList<>();
        List<Gateway> targetGateways = new ArrayList<>();

        for (Gateway gateway : handleGatewayAction.getGateways()) {
            for (Gateway gateway1 : gatewayList) {
                if (gateway1.getId() == gateway.getId()) {
                    gateway.setSelected(true);
                    selectedList.add(gateway);
                    targetGateways.add(gateway);
                }else {
                    sourceGateways.add(gateway);
                }

            }
        }
        handleGatewayAction.setGatewayList(handleGatewayAction.getGateways());
        handleGatewayAction.setSelectedGateways(selectedList);
        handleGatewayAction.setGatewayDualsForZone(new DualListModel<Gateway>(sourceGateways,targetGateways));
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
        List<Gateway> gatewayList = null;
        zoneService.editZone(currentZone);
        currentZone = zoneService.findById(currentZone.getId());

        gatewayList = gatewayService.findByZone(currentZone);

        for (Gateway gateway : gatewayList) {
            gateway.setZone(null);
            gatewayService.editGateway(gateway);
        }
        boolean condition = false;
        for (Gateway gateway : handleGatewayAction.getSelectedGateways()) {
            gateway.setZone(currentZone);
            condition = gatewayService.editGateway(gateway);
        }

        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-zone.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
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
        Zone insertedZone = null;
        insertedZone = zoneService.createZone(newZone);
        Set<Gateway> list = handleGatewayAction.getSelectedGateways();
        for (Gateway gateway : list) {
            if (gateway.getZone() == null) {
                gateway.setZone(insertedZone);
                boolean condition = gatewayService.editGateway(gateway);
            }
        }


        if (insertedZone != null) {
//
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-zone.xhtml");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void assignRule() {
        currentZone = zoneService.findById(currentZone.getId());
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

        rulePackageList = rulePackageService.getAllRulePackage();
    }


    private void doAssignRule() {
        currentZone.setEffectorUser(me.getUsername());
        currentZone.setRulePackage(selectedRulePackage);
        boolean condition = zoneService.editZone(currentZone);
        if (condition) {
            refresh();
            me.addInfoMessage("operation.occurred");
        } else {
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }
//
//    public Filter<?> getZoneNameFilterImpl() {
//        return new Filter<Zone>() {
//            public boolean accept(Zone zone) {
//                return zoneNameFilter == null || zoneNameFilter.length() == 0 || zone.getName().toLowerCase().contains(zoneNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getZoneDescriptionFilterImpl() {
//        return new Filter<Zone>() {
//            public boolean accept(Zone zone) {
//                return zoneDescriptionFilter == null || zoneDescriptionFilter.length() == 0 || zone.getDescription().toLowerCase().contains(zoneDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByZoneName() {
        zoneDescriptionOrder = SortOrder.UNSORTED;

        if (zoneNameOrder.equals(SortOrder.ASCENDING)) {
            setZoneNameOrder(SortOrder.DESCENDING);
        } else {
            setZoneNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByZoneDescription() {
        zoneNameOrder = SortOrder.UNSORTED;

        if (zoneDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setZoneDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setZoneDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public void selectNewRuleForZone() {
        rulePackageName = selectedRulePackage.getName();
        if (selectedRulePackage.getCalendar() != null)
            calendarName = selectedRulePackage.getCalendar().getName();
        else
            calendarName = "";
        antiPassBack = selectedRulePackage.isAniPassBack();
        allowExit = selectedRulePackage.isAllowExit();
        allowExitGadget = selectedRulePackage.isAllowExitGadget();
        doAssignRule();
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
        List<Zone> zones = zoneService.getAllZone();
//                rootZones = Zone.prepareHierarchy(zones, me);
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

    public List<RulePackage> getRulePackageList() {
        return rulePackageList;
    }

    public void setRulePackageList(List<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public void setRootZones(List<HardwareTree> rootZones) {
        this.rootZones = rootZones;
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

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }
}

