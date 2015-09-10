package ir.university.toosi.wtms.web.action.zone;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.model.entity.GatewayPerson;
import ir.university.toosi.wtms.web.model.entity.GatewaySpecialState;
import ir.university.toosi.wtms.web.model.entity.GatewayStatus;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.model.entity.zone.Camera;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.model.entity.zone.PreRequestGateway;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;


@Named(value = "handleGatewayAction")
@SessionScoped
public class HandleGatewayAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleCameraAction handleCameraAction;
    @Inject
    private HandlePersonAction handlePersonAction;
    private List<Gateway> gateways;
    private List<Gateway> preRequestGateways;
    private List<Long> preRequestGatewayIds;
    private List<Camera> selectedCamera = new ArrayList<>();
    private List<Person> persons = new ArrayList<>();
    private List<Person> selectedPersons = new ArrayList<>();
    private List<Person> unSelectedPersons = new ArrayList<>();
    private List<Person> newSelectedPersons = new ArrayList<>();

    private DataModel<Gateway> gatewayList = null;
    private DataModel<Gateway> preRequier = null;
    private DataModel<Camera> cameras = null;
    private String editable = "false";
    private String gatewayName;
    private boolean passBackControl;
    private boolean gatewayEnabled;
    private String description;
    private Gateway currentGetway = null;
    private Gateway currentPreGateway = null;
    private Gateway selectGetway = null;
    private String currentPage;
    private int page = 1;
    private int personPage = 1;
    private int pageInPopup = 1;
    private boolean selected;
    private boolean selectAll;
    private boolean selectAllPerson;
    private boolean selectAllPre;
    private boolean selectAllCamera;
    private Set<Gateway> selectedGateways = new HashSet<>();
    private SortOrder operationDescriptionOrder = SortOrder.UNSORTED;
    private String operationDescriptionFilter;
    private RulePackage selectedRulePackage;
    private String rulePackageName;
    private String calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private boolean selectRow = false;
    private DataModel<RulePackage> rulePackageList = null;
    private List<GatewaySpecialState> gatewaySpecialStates = new ArrayList<>();
    private DataModel<GatewaySpecialState> gatewaySpecialStateList = null;
    private GatewaySpecialState currentGatewaySpecialStatus = null;
    private String statusHour;
    private String statusMinute;
    private String statusSecond;
    private String statusDate;
    private String until;
    private String statusStateName;
    private SelectItem[] gatewayStatusItem;
    private Hashtable<String, GatewayStatus> statusHashtable = new Hashtable<>();
    private DataModel<Person> notAssignPersonList = new ListDataModel<>();
    private String gatewayNameFilter;
    private String gatewayDescriptionFilter;
    private SortOrder gatewayNameOrder = SortOrder.UNSORTED;
    private SortOrder gatewayDescriptionOrder = SortOrder.UNSORTED;


    public String begin() {
        me.setActiveMenu(MenuType.ZONE);
        refresh();
        return "list-gateway";
    }

    public void selectGateWays(ValueChangeEvent event) {
        currentGetway = gatewayList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentGetway.setSelected(true);
            selectedGateways.add(currentGetway);
        } else {
            currentGetway.setSelected(false);
            selectedGateways.remove(currentGetway);
        }
    }

    public void changedList(ValueChangeEvent event) {
        selectedPersons = (List<Person>) event.getNewValue();

    }

    public void changeGateways(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            gatewayEnabled = true;
        } else
            gatewayEnabled = false;
    }

    public void selectAllGateWay(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Gateway gateway : gatewayList) {
                gateway.setSelected(true);
                selectedGateways.add(gateway);
            }
        } else {
            for (Gateway gateWay : gatewayList) {
                gateWay.setSelected(false);
            }
            selectedGateways.clear();
        }
    }


    public void selectAllPreRequest(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Gateway gateway : preRequier) {
                gateway.setSelected(true);
                preRequestGateways.add(gateway);
                preRequestGatewayIds.add(gateway.getId());
            }
        } else {
            for (Gateway gateway : preRequier) {
                gateway.setSelected(false);
            }
            preRequestGateways.clear();
            preRequestGatewayIds.clear();
        }
    }

    public void selectAllCameraListener(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Camera camera : cameras) {
                camera.setSelected(true);
                selectedCamera.add(camera);
            }
        } else {
            for (Camera camera : cameras) {
                camera.setSelected(false);
            }
            selectedCamera.clear();
        }
    }

    public void selectPreRequest(ValueChangeEvent event) {
        currentPreGateway = preRequier.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentPreGateway.setSelected(true);
            preRequestGateways.add(currentPreGateway);
            preRequestGatewayIds.add(currentPreGateway.getId());
        } else {
            currentPreGateway.setSelected(false);
            for (Gateway gateway : preRequier) {
                if (gateway.getId() == currentPreGateway.getId())
                    preRequestGateways.remove(gateway);
                preRequestGatewayIds.remove(gateway.getId());
            }
        }
    }

    public void selectAllPersonListener(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Person person : notAssignPersonList) {
                if (!person.isSelected()) {
                    newSelectedPersons.add(person);
                    unSelectedPersons.remove(person);
                }
                person.setSelected(true);
                selectedPersons.add(person);
            }
        } else {
            for (Person person : notAssignPersonList) {
                person.setSelected(false);

            }
            unSelectedPersons.addAll(persons);
            newSelectedPersons.clear();
            selectedPersons.clear();
        }
    }

    public void selectPerson(ValueChangeEvent event) {
        Person person = notAssignPersonList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            person.setSelected(true);
            selectedPersons.add(person);
            newSelectedPersons.add(person);
            unSelectedPersons.remove(person);
        } else {
            person.setSelected(false);
            unSelectedPersons.add(person);
            for (Person person1 : notAssignPersonList) {
                if (person1.getId() == person.getId()) {
                    selectedPersons.remove(person1);
                    newSelectedPersons.remove(person);
                }
            }
        }
    }

    public void selectCamera(ValueChangeEvent event) {
        Camera cam = cameras.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            cam.setSelected(true);
            selectedCamera.add(cam);
        } else {
            cam.setSelected(false);
            for (Camera camera : cameras) {
                if (camera.getId() == cam.getId())
                    selectedCamera.remove(cam);
            }
        }
    }

    public DataModel<Gateway> getSelectionGrid() {
        gateways = new ArrayList<>();
        refresh();
        return gatewayList;
    }

    public void refresh() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");
        try {
            gateways = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {

            });
            for (Gateway gateway : gateways) {
                gateway.setSelected(false);
//                gateway.setDescription(me.getValue(gateway.getDescription()));
//                gateway.setName(gatewayName);
//                gateway.setEnabled(gatewayEnabled);
            }
            gatewayList = new ListDataModel<>(gateways);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPersonDataModel");
            persons = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        preRequier = new ListDataModel<>(new ArrayList<>(gateways));
        setEditable("false");

    }

    public void doDelete() {
if(currentGetway.getZone()!=null){
    refresh();
    me.addInfoMessage("gateway.zone");
    me.redirect("/zone/list-gateway.htm");
}
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteGateway");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentGetway)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/zone/list-gateway.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        gatewayName = "";
        description = "";
        page = 1;
        selectAll = false;
        selectAllPre = false;
        selectAllPerson = false;
        gatewayEnabled = false;
        currentGetway = null;
        setSelectRow(false);
        statusHour = "0";
        statusMinute = "0";
        statusSecond = "0";
        statusDate = "0";
        until = "0";
        preRequestGateways = new ArrayList<>();
        preRequestGatewayIds = new ArrayList<>();
        gatewayNameFilter = "";
        gatewayDescriptionFilter = "";

    }

    public void edit() {
        setEditable("true");
        gatewayEnabled = currentGetway.isEnabled();
        description = currentGetway.getDescription();
        gatewayName = currentGetway.getName();
        preRequestGatewayIds = currentGetway.getPreRequestGateways();
        preRequestGateways = new ArrayList<>();
        for (Long preRequestGatewayId : preRequestGatewayIds) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPreGatewayById");
            PreRequestGateway preGateway = new PreRequestGateway();
            try {
                preGateway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(preRequestGatewayId)), PreRequestGateway.class);
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            preRequestGateways.add(preGateway.getPreGateway());
        }
        Gateway removed = null;
        ArrayList<Gateway> list = new ArrayList<>(gateways);
        for (Gateway gateway : list) {
            if (gateway.getId() == currentGetway.getId())
                removed = gateway;
            for (Gateway innerGateway : preRequestGateways) {
                if (gateway.getId() == innerGateway.getId())
                    gateway.setSelected(true);
            }
        }
        list.remove(removed);
        preRequier = new ListDataModel<>(list);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayById");
        try {
            currentGetway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentGetway.getId())), Gateway.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentGetway.setName(gatewayName);
        currentGetway.setDescription(description);
        currentGetway.setEnabled(gatewayEnabled);
        currentGetway.setEffectorUser(me.getUsername());
        currentGetway.setPreRequestGateways(preRequestGatewayIds);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentGetway)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-gateway.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doAdd() {
        Gateway newGateway = new Gateway();
        newGateway.setDescription(description);
        newGateway.setName(gatewayName);
        newGateway.setEnabled(gatewayEnabled);
        newGateway.setDeleted("0");
        newGateway.setStatus("c");
        newGateway.setCurrentLang(me.getLanguages());
        newGateway.setEffectorUser(me.getUsername());
        newGateway.setPreRequestGateways(preRequestGatewayIds);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createGateway");
        Gateway insertedGateway = null;
        try {
            insertedGateway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newGateway)), Gateway.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedGateway != null) {
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(newGateway.getDescription());
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
//            try {
//                me.setLanguage();
//            } catch (IOException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-gateway.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


//        public void sortByOperationDescription() {
//            if (operationDescriptionOrder.equals(SortOrder.ASCENDING)) {
//                setOperationDescriptionOrder(SortOrder.DESCENDING);
//            } else {
//                setOperationDescriptionOrder(SortOrder.ASCENDING);
//            }
//        }
//
//        public Filter<?> getOperationDescriptionFilterImpl() {
//            return new Filter<Operation>() {
//                public boolean accept(Operation operation) {
//                    return operationDescriptionFilter == null || operationDescriptionFilter.length() == 0 || operation.getDescription().toLowerCase().contains(operationDescriptionFilter.toLowerCase());
//                }
//            };
//        }


    public void assignRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayById");
        try {
            currentGetway = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentGetway.getId())), Gateway.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        selectedRulePackage = currentGetway.getRulePackage();
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
            e.printStackTrace();
        }
        rulePackageList = new ListDataModel<>(rulePackages);
    }


    public void doAssignRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editGateway");
        currentGetway.setEffectorUser(me.getUsername());
        currentGetway.setDeleted("0");
        currentGetway.setRulePackage(selectedRulePackage);
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentGetway)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/zone/list-gateway.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void selectNewRuleForGateway() {
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


    public void assignPerson() {
        handlePersonAction.init();
        personPage = 1;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayPersonByGatewayId");
        List<GatewayPerson> gatewayPersons = null;
        try {
            gatewayPersons = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentGetway.getId())), new TypeReference<List<GatewayPerson>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        selectedPersons.clear();
        notAssignPersonList = new ListDataModel(persons);
        for (GatewayPerson gatewayPerson : gatewayPersons) {
            for (Person person : notAssignPersonList) {
                if (gatewayPerson.getPerson().getId() == person.getId()) {
                    selectedPersons.add(person);
                    person.setSelected(true);
                    break;
                }
            }

        }
        selectGetway = currentGetway;
    }

    public void doAssignPerson() throws IOException {

        try {
            for (Person gatewayPerson : unSelectedPersons) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteGatewayPerson");
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(gatewayPerson.getId()) + "#" + selectGetway.getId()), String.class);
            }
            GatewayPerson gatewayPerson = null;
            for (Person selectedPerson : newSelectedPersons) {
                gatewayPerson = new GatewayPerson();
                gatewayPerson.setPerson(selectedPerson);
                gatewayPerson.setGateway(selectGetway);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createGatewayPerson");
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(gatewayPerson)), GatewayPerson.class);
            }
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/fillRulePackageHashTable");
            new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(1));
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/zone/list-gateway.htm");
        } catch (IOException e) {
            e.printStackTrace();
            me.addInfoMessage("operation.not.occurred");
            return;
        }
    }

    Gateway thisGateway = null;

    public void assignSpecialStatus() {
        statusSecond = "0";
        statusMinute = "0";
        statusHour = "0";
        statusDate = "";
        until = "0";
        statusStateName = null;
        gatewayStatusItem = new SelectItem[GatewayStatus.values().length];
        int i = 0;
        for (GatewayStatus gatewayStatus : GatewayStatus.values()) {
            statusHashtable.put(String.valueOf(gatewayStatus.name()), gatewayStatus);
            gatewayStatusItem[i] = new SelectItem(gatewayStatus.name(), gatewayStatus.getDescription());
            i++;
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewaySpecialStateByGatewayId");
        try {
            gatewaySpecialStates = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentGetway.getId())), new TypeReference<List<GatewaySpecialState>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        thisGateway = currentGetway;
        gatewaySpecialStateList = new ListDataModel<>(gatewaySpecialStates);
    }

    public void doAssignSpecialStatus() throws IOException {
        String time = statusHour + ":" + statusMinute + ":" + statusSecond;
        GatewayStatus gatewayStatus = statusHashtable.get(statusStateName);
        GatewaySpecialState newGatewaySpecialState = new GatewaySpecialState();
        newGatewaySpecialState.setDate(statusDate);
        newGatewaySpecialState.setTime(time);
        newGatewaySpecialState.setGateway(thisGateway);
        newGatewaySpecialState.setGateWayStatus(gatewayStatus);
        newGatewaySpecialState.setDeleted("0");
        newGatewaySpecialState.setStatus("c");
        newGatewaySpecialState.setUntil(until);
        newGatewaySpecialState.setCurrentLang(me.getLanguages());
        newGatewaySpecialState.setEffectorUser(me.getUsername());
        if (!feasible(newGatewaySpecialState)) {
            me.addInfoMessage("conflict");
            return;
        }
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createGatewaySpecialState");
        GatewaySpecialState insertedGatewaySpecialState = null;
        try {
            insertedGatewaySpecialState = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newGatewaySpecialState)), GatewaySpecialState.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (insertedGatewaySpecialState != null) {
            statusSecond = "0";
            statusMinute = "0";
            statusHour = "0";
            statusDate = "";
            gatewaySpecialStates.add(insertedGatewaySpecialState);
            gatewaySpecialStateList = new ListDataModel<>(gatewaySpecialStates);
        }
    }


    public boolean feasible(GatewaySpecialState gatewaySpecialState) {

        long startTime = time2long(gatewaySpecialState.getTime());
        long endTime = until2long(gatewaySpecialState.getTime(), gatewaySpecialState.getUntil());
        boolean flag = true;

        if (endTime < startTime)
            return false;
        if (endTime / 10000 > 23)
            return false;

        for (GatewaySpecialState gatewaySpecialState1 : gatewaySpecialStates) {
            if (!gatewaySpecialState1.getDate().equalsIgnoreCase(gatewaySpecialState.getDate()))
                continue;

            if (startTime < time2long(gatewaySpecialState1.getTime()) && endTime < until2long(gatewaySpecialState1.getTime(), gatewaySpecialState1.getUntil()))
                flag = true;
            else if (startTime > time2long(gatewaySpecialState1.getTime()) && endTime > until2long(gatewaySpecialState1.getTime(), gatewaySpecialState1.getUntil()))
                flag = true;
            else {
                return false;
            }
        }

        return flag;
    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public long until2long(String time, String until) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]);
        s = String.valueOf((Long.valueOf(s) + Long.valueOf(until)));
        s += (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }


    public void removeSpecialStatus() {
        currentGatewaySpecialStatus = gatewaySpecialStateList.getRowData();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteGatewaySpecialStatus");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentGatewaySpecialStatus)), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gatewaySpecialStates.remove(currentGatewaySpecialStatus);
        gatewaySpecialStateList = new ListDataModel<>(gatewaySpecialStates);
    }

    public void selectForEdit() {
        currentGetway = gatewayList.getRowData();
        setSelectRow(true);

    }

    public void specialStatusInitialize() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/specialStatusInitialize");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), String.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public Filter<?> getGatewayNameFilterImpl() {
//        return new Filter<Gateway>() {
//            public boolean accept(Gateway gateway) {
//                return gatewayNameFilter == null || gatewayNameFilter.length() == 0 || gateway.getName().toLowerCase().contains(gatewayNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getGatewayDescriptionFilterImpl() {
//        return new Filter<Gateway>() {
//            public boolean accept(Gateway gateway) {
//                return gatewayDescriptionFilter == null || gatewayDescriptionFilter.length() == 0 || gateway.getDescription().toLowerCase().contains(gatewayDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByGatewayName() {
        gatewayDescriptionOrder = SortOrder.UNSORTED;

        if (gatewayNameOrder.equals(SortOrder.ASCENDING)) {
            setGatewayNameOrder(SortOrder.DESCENDING);
        } else {
            setGatewayNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByGatewayDescription() {
        gatewayNameOrder = SortOrder.UNSORTED;

        if (gatewayDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setGatewayDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setGatewayDescriptionOrder(SortOrder.ASCENDING);
        }
    }


    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public HandleCameraAction getHandleCameraAction() {
        return handleCameraAction;
    }

    public void setHandleCameraAction(HandleCameraAction handleCameraAction) {
        this.handleCameraAction = handleCameraAction;
    }

    public DataModel<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(ListDataModel<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public String getEditable() {
        return editable;
    }

    public List<Gateway> getGateways() {
        return gateways;
    }

    public void setGateways(List<Gateway> gateways) {
        this.gateways = gateways;
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

    public boolean isPassBackControl() {
        return passBackControl;
    }

    public void setPassBackControl(boolean passBackControl) {
        this.passBackControl = passBackControl;
    }

    public boolean isGatewayEnabled() {
        return gatewayEnabled;
    }

    public void setGatewayEnabled(boolean gateWayEnabled) {
        this.gatewayEnabled = gateWayEnabled;
    }


    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SortOrder getOperationDescriptionOrder() {
        return operationDescriptionOrder;
    }

    public void setOperationDescriptionOrder(SortOrder operationDescriptionOrder) {
        this.operationDescriptionOrder = operationDescriptionOrder;
    }

    public String getOperationDescriptionFilter() {
        return operationDescriptionFilter;
    }

    public void setOperationDescriptionFilter(String operationDescriptionFilter) {
        this.operationDescriptionFilter = operationDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public Gateway getCurrentGetway() {
        return currentGetway;
    }

    public void setCurrentGetway(Gateway currentGetway) {
        this.currentGetway = currentGetway;
    }

    public Set<Gateway> getSelectedGateways() {
        return selectedGateways;
    }

    public void setSelectedGateways(Set<Gateway> selectedGateways) {
        this.selectedGateways = selectedGateways;
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

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public List<Person> getSelectedPersons() {
        return selectedPersons;
    }

    public void setSelectedPersons(List<Person> selectedPersons) {
        this.selectedPersons = selectedPersons;
    }

    public void setGatewayList(DataModel<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    public List<GatewaySpecialState> getGatewaySpecialStates() {
        return gatewaySpecialStates;
    }

    public void setGatewaySpecialStates(List<GatewaySpecialState> gatewaySpecialStates) {
        this.gatewaySpecialStates = gatewaySpecialStates;
    }

    public DataModel<GatewaySpecialState> getGatewaySpecialStateList() {
        return gatewaySpecialStateList;
    }

    public void setGatewaySpecialStateList(DataModel<GatewaySpecialState> gatewaySpecialStateList) {
        this.gatewaySpecialStateList = gatewaySpecialStateList;
    }

    public GatewaySpecialState getCurrentGatewaySpecialStatus() {
        return currentGatewaySpecialStatus;
    }

    public void setCurrentGatewaySpecialStatus(GatewaySpecialState currentGatewaySpecialStatus) {
        this.currentGatewaySpecialStatus = currentGatewaySpecialStatus;
    }

    public String getStatusHour() {
        return statusHour;
    }

    public void setStatusHour(String statusHour) {
        this.statusHour = statusHour;
    }

    public String getStatusMinute() {
        return statusMinute;
    }

    public void setStatusMinute(String statusMinute) {
        this.statusMinute = statusMinute;
    }

    public String getStatusSecond() {
        return statusSecond;
    }

    public void setStatusSecond(String statusSecond) {
        this.statusSecond = statusSecond;
    }

    public String getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(String statusDate) {
        this.statusDate = statusDate;
    }

    public String getStatusStateName() {
        return statusStateName;
    }

    public void setStatusStateName(String statusStateName) {
        this.statusStateName = statusStateName;
    }

    public SelectItem[] getGatewayStatusItem() {
        return gatewayStatusItem;
    }

    public void setGatewayStatusItem(SelectItem[] gatewayStatusItem) {
        this.gatewayStatusItem = gatewayStatusItem;
    }

    public Hashtable<String, GatewayStatus> getStatusHashtable() {
        return statusHashtable;
    }

    public void setStatusHashtable(Hashtable<String, GatewayStatus> statusHashtable) {
        this.statusHashtable = statusHashtable;
    }

    public Gateway getThisGateway() {
        return thisGateway;
    }

    public void setThisGateway(Gateway thisGateway) {
        this.thisGateway = thisGateway;
    }

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public Gateway getSelectGetway() {
        return selectGetway;
    }

    public void setSelectGetway(Gateway selectGetway) {
        this.selectGetway = selectGetway;
    }

    public Gateway getCurrentPreGateway() {
        return currentPreGateway;
    }

    public void setCurrentPreGateway(Gateway currentPreGateway) {
        this.currentPreGateway = currentPreGateway;
    }

    public boolean isSelectAllPerson() {
        return selectAllPerson;
    }

    public void setSelectAllPerson(boolean selectAllPerson) {
        this.selectAllPerson = selectAllPerson;
    }

    public DataModel<Person> getNotAssignPersonList() {
        return notAssignPersonList;
    }

    public void setNotAssignPersonList(DataModel<Person> notAssignPersonList) {
        this.notAssignPersonList = notAssignPersonList;
    }

    public SortOrder getGatewayNameOrder() {
        return gatewayNameOrder;
    }

    public void setGatewayNameOrder(SortOrder gatewayNameOrder) {
        this.gatewayNameOrder = gatewayNameOrder;
    }

    public String getGatewayNameFilter() {
        return gatewayNameFilter;
    }

    public void setGatewayNameFilter(String gatewayNameFilter) {
        this.gatewayNameFilter = gatewayNameFilter;
    }

    public SortOrder getGatewayDescriptionOrder() {
        return gatewayDescriptionOrder;
    }

    public void setGatewayDescriptionOrder(SortOrder gatewayDescriptionOrder) {
        this.gatewayDescriptionOrder = gatewayDescriptionOrder;
    }

    public String getGatewayDescriptionFilter() {
        return gatewayDescriptionFilter;
    }

    public void setGatewayDescriptionFilter(String gatewayDescriptionFilter) {
        this.gatewayDescriptionFilter = gatewayDescriptionFilter;
    }

    public List<Gateway> getPreRequestGateways() {
        return preRequestGateways;
    }

    public void setPreRequestGateways(List<Gateway> preRequestGateways) {
        this.preRequestGateways = preRequestGateways;
    }

    public boolean isSelectAllPre() {
        return selectAllPre;
    }

    public void setSelectAllPre(boolean selectAllPre) {
        this.selectAllPre = selectAllPre;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }

    public boolean isSelectAllCamera() {
        return selectAllCamera;
    }

    public void setSelectAllCamera(boolean selectAllCamera) {
        this.selectAllCamera = selectAllCamera;
    }

    public List<Camera> getSelectedCamera() {
        return selectedCamera;
    }

    public void setSelectedCamera(List<Camera> selectedCamera) {
        this.selectedCamera = selectedCamera;
    }

    public DataModel<Camera> getCameras() {
        if (cameras == null || cameras.getRowCount() == 0) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllCamera");

            try {
                List<Camera> camerasLis = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Camera>>() {
                });
                cameras = new ListDataModel<>(camerasLis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return cameras;
    }

    public void setCameras(DataModel<Camera> cameras) {
        this.cameras = cameras;
    }

    public List<Long> getPreRequestGatewayIds() {
        return preRequestGatewayIds;
    }

    public void setPreRequestGatewayIds(List<Long> preRequestGatewayIds) {
        this.preRequestGatewayIds = preRequestGatewayIds;
    }

    public HandlePersonAction getHandlePersonAction() {
        return handlePersonAction;
    }

    public void setHandlePersonAction(HandlePersonAction handlePersonAction) {
        this.handlePersonAction = handlePersonAction;
    }

    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public DataModel<Gateway> getPreRequier() {
        return preRequier;
    }

    public void setPreRequier(DataModel<Gateway> preRequier) {
        this.preRequier = preRequier;
    }
}
