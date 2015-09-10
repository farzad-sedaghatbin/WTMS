package ir.university.toosi.wtms.web.action.rule;//**

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.calendar.Calendar;
import ir.university.toosi.wtms.web.model.entity.calendar.DayType;
import ir.university.toosi.wtms.web.model.entity.rule.Rule;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

@Named(value = "handleRuleAction")
@SessionScoped
public class HandleRuleAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;
    private DataModel<RulePackage> rulePackageList = null;
    private DataModel<Rule> ruleListTemp = null;
    private int page = 1;
    private String name;
    private String calendar;
    private DayType ruleDayType;
    private String selectedCalendarIdTemp;
    private String dayTypeIdTemp;
    private String ruleStartTime;
    private String ruleEndTime;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;
    private String ruleEntranceCount;
    private String ruleExitCount;
    private Boolean ruleDeny;
    private String editable = "false";
    private Calendar selectedCalendar;
    private ArrayList<Rule> ruleArrayList = new ArrayList<>();
    private ArrayList<RulePackage> rulePackages = new ArrayList<>();
    private boolean addNewRuleFlag = false;
    private boolean ruleAniPassBack = false;
    private boolean ruleAllowExit = false;
    private boolean ruleAllowExitGadget = false;
    private Rule currentRule;
    private SelectItem[] dayTypeItems;
    private RulePackage currentRulePackage;
    private RulePackage selectedRulePackage;
    private SelectItem[] calendarItems = me.calendarItem;
    private boolean selectRow = false;
    private SortOrder ruleNameOrder = SortOrder.UNSORTED;
    private String ruleNameFilter;

    private Hashtable<String, DayType> dayTypeHashtable = new Hashtable<>();

    public String begin() {
        me.setActiveMenu(MenuType.CALENDAR);
        refresh();
        return "list-rule";
    }

    private void refresh() {
        init();
    }

    public void init() {
        page = 1;
        ruleAllowExit = false;
        ruleAniPassBack = false;
        ruleAllowExitGadget = false;
        ruleStartTime = "0";
        ruleEndTime = "0";
        startHour = "0";
        startMinute = "0";
        startSecond = "0";
        endHour = "0";
        endMinute = "0";
        endSecond = "0";
        fillDayTypeCombo();
        fillDataModel();
        try {
            me.fillCalendar();
        } catch (IOException e) {
            e.printStackTrace();
        }
        calendarItems = me.calendarItem;
        currentRulePackage = null;
        setSelectRow(false);
        ruleNameFilter="";

    }

    private void fillDataModel() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRulePackage");
        try {
            rulePackages = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<RulePackage>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        rulePackageList = new ListDataModel<RulePackage>(rulePackages);
    }

    private void fillDayTypeCombo() {

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllDayType");
        ArrayList<DayType> dayTypes = null;
        try {
            dayTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<DayType>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        dayTypeItems = new SelectItem[dayTypes.size()];
        int i = 0;
        for (DayType dayType : dayTypes) {
            dayTypeHashtable.put(String.valueOf(dayType.getId()), dayType);
            dayTypeItems[i] = new SelectItem(dayType.getId(), dayType.getTitle());
            i++;
        }
    }


    public void doDelete() {

        currentRulePackage.setStatus("o," + me.getUsername());
        currentRulePackage.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteRulePackage");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRulePackage)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/business-rules/list-rule.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void edit() {
        ruleArrayList = new ArrayList<>();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getRulesByRulePackage");
        try {
            ruleArrayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentRulePackage.getId())), new TypeReference<List<Rule>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ruleListTemp = new ListDataModel<>(ruleArrayList);
        name = currentRulePackage.getName();
        ruleAllowExitGadget = currentRulePackage.isAllowExitGadget();
        ruleAniPassBack = currentRulePackage.isAniPassBack();
        ruleAllowExit = currentRulePackage.isAllowExit();
        selectedCalendar = currentRulePackage.getCalendar();
        selectedCalendarIdTemp = String.valueOf(selectedCalendar.getId());
        editable = "true";
        selectedRulePackage = currentRulePackage;
    }

    public void doEdit() {

        selectedRulePackage.setStatus("c");
        selectedRulePackage.setDeleted("0");
        selectedRulePackage.setEffectorUser(me.getUsername());
        selectedRulePackage.setName(name);
        selectedRulePackage.setAllowExit(ruleAllowExit);
        selectedRulePackage.setAniPassBack(ruleAniPassBack);
        selectedRulePackage.setAllowExitGadget(ruleAllowExitGadget);
//        currentRulePackage.setRules(new HashSet<Rule>());
//        currentRulePackage.getRules().addAll(ruleArrayList);
        selectedRulePackage.setCalendar(me.calendarHashtable.get(selectedCalendarIdTemp));
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editRulePackage");

        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(selectedRulePackage)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/fillRulePackageHashTable");
                new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf("2"));
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRule");
                for (Rule rule : ruleArrayList) {
                    rule.setRulePackage(selectedRulePackage);
                    new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(rule)), Rule.class);
                }
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/business-rules/list-rule.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        ruleArrayList = new ArrayList<>();
        name = "";
        calendar = "";
        init();
        selectedCalendar = null;
        addNewRuleFlag = false;
        ruleListTemp = new ListDataModel<>(ruleArrayList);
        editable = "false";
    }

    public void remove() {
        currentRule = ruleListTemp.getRowData();
        ruleArrayList.remove(currentRule);
        ruleListTemp = new ListDataModel<>(ruleArrayList);
    }

    public void addNewRule() {
        ruleDayType = null;
        ruleStartTime = "";
        ruleEndTime = "";
        ruleEntranceCount = "";
        ruleExitCount = "";
        ruleDeny = false;
        addNewRuleFlag = true;
    }

    public void doAddNewRule() {
        ruleStartTime = (startHour.length() == 2 ? startHour : '0' + startHour) + ":" + (startMinute.length() == 2 ? startMinute : '0' + startMinute) + ":" + (startSecond.length() == 2 ? startSecond : '0' + startSecond);
        ruleEndTime = (endHour.length() == 2 ? endHour : '0' + endHour) + ":" + (endMinute.length() == 2 ? endMinute : '0' + endMinute) + ":" + (endSecond.length() == 2 ? endSecond : '0' + endSecond);
        Rule rule = new Rule();
        rule.setDayType(dayTypeHashtable.get(dayTypeIdTemp));
        rule.setStartTime(ruleStartTime);
        rule.setEndTime(ruleEndTime);
        rule.setEntranceCount(ruleEntranceCount);
        rule.setExitCount(ruleExitCount);
        rule.setDeny(ruleDeny);
        if (feasible(rule)) {
            ruleArrayList.add(rule);
            ruleListTemp = new ListDataModel<>(ruleArrayList);
            addNewRuleFlag = false;
        } else me.addInfoMessage("conflict");
    }

    public boolean feasible(Rule rule) {

        long startTime = time2long(rule.getStartTime());
        long endTime = time2long(rule.getEndTime());
        boolean flag = true;
        if (rule.isDeny()) {
            if (endTime < startTime)
                return false;

            for (Rule rule1 : ruleArrayList) {
                if (rule1.getDayType().getId() != rule.getDayType().getId()|| !rule1.isDeny() )
                    continue;
                if (startTime < time2long(rule1.getStartTime()) && endTime < time2long(rule1.getStartTime()))
                    flag = true;
                else if (startTime > time2long(rule1.getEndTime()) && endTime > time2long(rule1.getEndTime()))
                    flag = true;
                else {
                    return false;
                }
            }
        }

        if (endTime < startTime)
            return false;

        for (Rule rule1 : ruleArrayList) {
            if (rule1.getDayType().getId() != rule.getDayType().getId()|| rule1.isDeny() )
                continue;
            if (startTime < time2long(rule1.getStartTime()) && endTime < time2long(rule1.getStartTime()))
                flag = true;
            else if (startTime > time2long(rule1.getEndTime()) && endTime > time2long(rule1.getEndTime()))
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

    public void doAdd() {

        RulePackage rulePackage = new RulePackage();
//        rulePackage.setRules(new HashSet<Rule>());
//        rulePackage.getRules().addAll(ruleArrayList);
        rulePackage.setName(name);
        rulePackage.setEffectorUser(me.getUsername());
        rulePackage.setCalendar(me.calendarHashtable.get(selectedCalendarIdTemp));
        rulePackage.setAllowExit(ruleAllowExit);
        rulePackage.setAniPassBack(ruleAniPassBack);
        rulePackage.setAllowExitGadget(ruleAllowExitGadget);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRulePackage");
        RulePackage addedRulePackage = null;
        try {
            addedRulePackage = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(rulePackage)), RulePackage.class);
            if (addedRulePackage != null) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRule");
                for (Rule rule : ruleArrayList) {
                    rule.setRulePackage(addedRulePackage);
                    new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(rule)), Rule.class);
                }
                refresh();
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/fillRulePackageHashTable");
                new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(1));
                me.addInfoMessage("operation.occurred");
                me.redirect("/business-rules/list-rule.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();

        }
    }
//
//    public Filter<?> getRuleNameFilterImpl() {
//        return new Filter<RulePackage>() {
//            public boolean accept(RulePackage rulePackage) {
//                return ruleNameFilter == null || ruleNameFilter.length() == 0 || rulePackage.getName().toLowerCase().contains(ruleNameFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByRuleName() {
        if (ruleNameOrder.equals(SortOrder.ASCENDING)) {
            setRuleNameOrder(SortOrder.DESCENDING);
        } else {
            setRuleNameOrder(SortOrder.ASCENDING);
        }
    }


    public void selectForEdit() {
        currentRulePackage = rulePackageList.getRowData();
        setSelectRow(true);

    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<RulePackage> getRulePackageList() {
        return rulePackageList;
    }

    public void setRulePackageList(DataModel<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public int getPage() {
        currentRulePackage = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<Rule> getRuleArrayList() {
        return ruleArrayList;
    }

    public void setRuleArrayList(ArrayList<Rule> ruleArrayList) {
        this.ruleArrayList = ruleArrayList;
    }

    public String getCalendar() {
        return calendar;
    }

    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DayType getRuleDayType() {
        return ruleDayType;
    }

    public void setRuleDayType(DayType ruleDayType) {
        this.ruleDayType = ruleDayType;
    }

    public String getRuleStartTime() {
        return ruleStartTime;
    }

    public void setRuleStartTime(String ruleStartTime) {
        this.ruleStartTime = ruleStartTime;
    }

    public String getRuleEndTime() {
        return ruleEndTime;
    }

    public void setRuleEndTime(String ruleEndTime) {
        this.ruleEndTime = ruleEndTime;
    }

    public String getRuleEntranceCount() {
        return ruleEntranceCount;
    }

    public void setRuleEntranceCount(String ruleEntranceCount) {
        this.ruleEntranceCount = ruleEntranceCount;
    }

    public String getRuleExitCount() {
        return ruleExitCount;
    }

    public void setRuleExitCount(String ruleExitCount) {
        this.ruleExitCount = ruleExitCount;
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public DataModel<Rule> getRuleListTemp() {
        return ruleListTemp;
    }

    public void setRuleListTemp(DataModel<Rule> ruleListTemp) {
        this.ruleListTemp = ruleListTemp;
    }

    public boolean isAddNewRuleFlag() {
        return addNewRuleFlag;
    }

    public void setAddNewRuleFlag(boolean addNewRuleFlag) {
        this.addNewRuleFlag = addNewRuleFlag;
    }

    public Rule getCurrentRule() {
        return currentRule;
    }

    public void setCurrentRule(Rule currentRule) {
        this.currentRule = currentRule;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public SelectItem[] getDayTypeItems() {
        return dayTypeItems;
    }

    public void setDayTypeItems(SelectItem[] dayTypeItems) {
        this.dayTypeItems = dayTypeItems;
    }

    public RulePackage getCurrentRulePackage() {
        return currentRulePackage;
    }

    public void setCurrentRulePackage(RulePackage currentRulePackage) {
        this.currentRulePackage = currentRulePackage;
    }

    public SelectItem[] getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(SelectItem[] calendarItems) {
        this.calendarItems = calendarItems;
    }

    public boolean isRuleAniPassBack() {
        return ruleAniPassBack;
    }

    public void setRuleAniPassBack(boolean ruleAniPassBack) {
        this.ruleAniPassBack = ruleAniPassBack;
    }

    public boolean isRuleAllowExit() {
        return ruleAllowExit;
    }

    public void setRuleAllowExit(boolean ruleAllowExit) {
        this.ruleAllowExit = ruleAllowExit;
    }

    public boolean isRuleAllowExitGadget() {
        return ruleAllowExitGadget;
    }

    public void setRuleAllowExitGadget(boolean ruleAllowExitGadget) {
        this.ruleAllowExitGadget = ruleAllowExitGadget;
    }

    public String getSelectedCalendarIdTemp() {
        return selectedCalendarIdTemp;
    }

    public void setSelectedCalendarIdTemp(String selectedCalendarIdTemp) {
        this.selectedCalendarIdTemp = selectedCalendarIdTemp;
    }

    public String getDayTypeIdTemp() {
        return dayTypeIdTemp;
    }

    public void setDayTypeIdTemp(String dayTypeIdTemp) {
        this.dayTypeIdTemp = dayTypeIdTemp;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public AccessControlAction getAccessControlAction() {
        return accessControlAction;
    }

    public void setAccessControlAction(AccessControlAction accessControlAction) {
        this.accessControlAction = accessControlAction;
    }

    public String getStartHour() {
        return startHour;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public String getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    public String getStartSecond() {
        return startSecond;
    }

    public void setStartSecond(String startSecond) {
        this.startSecond = startSecond;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public String getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    public String getEndSecond() {
        return endSecond;
    }

    public void setEndSecond(String endSecond) {
        this.endSecond = endSecond;
    }

    public ArrayList<RulePackage> getRulePackages() {
        return rulePackages;
    }

    public void setRulePackages(ArrayList<RulePackage> rulePackages) {
        this.rulePackages = rulePackages;
    }

    public Hashtable<String, DayType> getDayTypeHashtable() {
        return dayTypeHashtable;
    }

    public void setDayTypeHashtable(Hashtable<String, DayType> dayTypeHashtable) {
        this.dayTypeHashtable = dayTypeHashtable;
    }

    public Boolean getRuleDeny() {
        return ruleDeny;
    }

    public void setRuleDeny(Boolean ruleDeny) {
        this.ruleDeny = ruleDeny;
    }

    public SortOrder getRuleNameOrder() {
        return ruleNameOrder;
    }

    public void setRuleNameOrder(SortOrder ruleNameOrder) {
        this.ruleNameOrder = ruleNameOrder;
    }

    public String getRuleNameFilter() {
        return ruleNameFilter;
    }

    public void setRuleNameFilter(String ruleNameFilter) {
        this.ruleNameFilter = ruleNameFilter;
    }
}
