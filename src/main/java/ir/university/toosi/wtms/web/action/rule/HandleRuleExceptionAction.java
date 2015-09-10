package ir.university.toosi.wtms.web.action.rule;//**

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.rule.RuleException;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;
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
import java.util.List;

@Named(value = "handleRuleExceptionAction")
@SessionScoped
public class HandleRuleExceptionAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private GeneralHelper generalHelper;
    private DataModel<RulePackage> rulePackageList = null;
    private DataModel<RuleException> ruleExceptionList = null;
    private int page = 1;
    private int pageInPopup = 1;
    private String name;
    private String fromDate;
    private String toDate;
    private String rulePackageId;
    private String ruleStartTime;
    private String ruleEndTime;
    private String ruleEntranceCount;
    private String ruleExitCount;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;
    private String editable = "false";
    private ArrayList<RulePackage> rulePackages = new ArrayList<>();
    private ArrayList<RulePackage> selectedRulePackage = new ArrayList<>();
    private ArrayList<RuleException> ruleExceptions = new ArrayList<>();
    private RuleException currentRuleException;
    private boolean selected;
    private boolean selectAll;
    private boolean selectRow = false;
    private boolean ruleDeny = false;
    private SortOrder exceptionNameOrder = SortOrder.UNSORTED;
    private String exceptionNameFilter;


    public String begin() {
        me.setActiveMenu(MenuType.CALENDAR);

        refresh();
        return "list-exception-rule";
    }

    private void refresh() {
        init();
    }

    public void init() {
        page = 1;
        rulePackageId = "";
        ruleStartTime = "";
        ruleEndTime = "";
        ruleEntranceCount = "";
        ruleExitCount = "";
        fromDate = "";
        toDate = "";
        rulePackageId = "";
        ruleEntranceCount = "";
        ruleExitCount = "";
        startHour = "";
        startMinute = "";
        startSecond = "";
        endHour = "";
        endMinute = "";
        endSecond = "";
        selectAll = false;
        currentRuleException = null;
        selectedRulePackage = new ArrayList<>();
        setSelectRow(false);
        fillDataModel();
        exceptionNameFilter = "";
    }

    private void fillDataModel() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRuleException");
        try {
            ruleExceptions = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<RuleException>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ruleExceptionList = new ListDataModel<RuleException>(ruleExceptions);
    }

    public void doDelete() {

        currentRuleException.setStatus("o," + me.getUsername());
        currentRuleException.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteRuleException");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRuleException)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/business-rules/list-exception-rule.htm");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void edit() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRulePackage");
        try {
            rulePackages = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<RulePackage>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        selectedRulePackage = new ArrayList<>();
        for (RulePackage rulePackage : rulePackages) {
            for (RulePackage rulePackage1 : currentRuleException.getRulePackage()) {

                if (rulePackage.getId() == rulePackage1.getId()) {
                    rulePackage.setSelected(true);
                    selectedRulePackage.add(rulePackage);
                    break;
                } else
                    rulePackage.setSelected(false);

            }
        }
        rulePackageList = new ListDataModel<>(rulePackages);
        fromDate = currentRuleException.getFromDate();
        toDate = currentRuleException.getToDate();
        ruleStartTime = currentRuleException.getStartTime();
        String[] start = ruleStartTime.split(":");
        if (start.length == 3) {
            startHour = start[0];
            startMinute = start[1];
            startSecond = start[2];
        }
        ruleEndTime = currentRuleException.getEndTime();
        String[] end = ruleEndTime.split(":");
        if (end.length == 3) {
            endHour = end[0];
            endMinute = end[1];
            endSecond = end[2];
        }

        ruleEntranceCount = currentRuleException.getEntranceCount();
        ruleExitCount = currentRuleException.getExitCount();
        name = currentRuleException.getName();
        ruleDeny = currentRuleException.isDeny();
        editable = "true";
    }

    public void doEdit() {
        currentRuleException.setEffectorUser(me.getUsername());
        currentRuleException.setFromDate(fromDate);
        currentRuleException.setToDate(toDate);
        currentRuleException.setName(name);
        ruleStartTime = (startHour.length() == 2 ? startHour : '0' + startHour) + ":" + (startMinute.length() == 2 ? startMinute : '0' + startMinute) + ":" + (startSecond.length() == 2 ? startSecond : '0' + startSecond);
        ruleEndTime = (endHour.length() == 2 ? endHour : '0' + endHour) + ":" + (endMinute.length() == 2 ? endMinute : '0' + endMinute) + ":" + (endSecond.length() == 2 ? endSecond : '0' + endSecond);
        currentRuleException.setEndTime(ruleEndTime);
        currentRuleException.setStartTime(ruleStartTime);
        currentRuleException.setExitCount(ruleExitCount);
        currentRuleException.setEntranceCount(ruleEntranceCount);

        currentRuleException.setRulePackage(new ArrayList<RulePackage>());
//        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editRuleException");
//        try {
//            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRuleException)), String.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (!feasibleDate(currentRuleException) || !feasibleTime(currentRuleException)) {
            me.addInfoMessage("conflict");
            return;
        }
        currentRuleException.getRulePackage().addAll(selectedRulePackage);
        currentRuleException.setDeny(ruleDeny);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editPureRuleException");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRuleException)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/fillRulePackageHashTable");
                new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(1));
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/business-rules/list-exception-rule.htm");
            } else {
                me.redirect("/business-rules/list-exception-rule.htm");
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void add() {
        name = "";
        init();
        editable = "false";
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRulePackage");
        try {
            rulePackages = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<RulePackage>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        rulePackageList = new ListDataModel<>(rulePackages);
    }
//
//

    public void selectAllRulePackage(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (RulePackage rulePackage : rulePackages) {
                rulePackage.setSelected(true);
                selectedRulePackage.add(rulePackage);
            }
        } else {
            for (RulePackage rulePackage : rulePackages) {
                rulePackage.setSelected(false);
            }
            selectedRulePackage.clear();
        }
    }

    public void selectRulePackage(ValueChangeEvent event) {
        RulePackage rulePackage = rulePackageList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            rulePackage.setSelected(true);
            selectedRulePackage.add(rulePackage);
        } else {
            rulePackage.setSelected(false);
            for (RulePackage rulePackage1 : selectedRulePackage) {
                if (rulePackage1.getId() == rulePackage.getId()) {
                    selectedRulePackage.remove(rulePackage1);
                    break;
                }
            }
        }
    }

    public void doAdd() {
        RuleException ruleException = new RuleException();
        ruleException.setEffectorUser(me.getUsername());
        ruleException.setFromDate(fromDate);
        ruleException.setToDate(toDate);
        ruleException.setName(name);
        ruleStartTime = (startHour.length() == 2 ? startHour : '0' + startHour) + ":" + (startMinute.length() == 2 ? startMinute : '0' + startMinute) + ":" + (startSecond.length() == 2 ? startSecond : '0' + startSecond);
        ruleEndTime = (endHour.length() == 2 ? endHour : '0' + endHour) + ":" + (endMinute.length() == 2 ? endMinute : '0' + endMinute) + ":" + (endSecond.length() == 2 ? endSecond : '0' + endSecond);
        ruleException.setEndTime(ruleEndTime);
        ruleException.setStartTime(ruleStartTime);
        ruleException.setExitCount(ruleExitCount);
        ruleException.setEntranceCount(ruleEntranceCount);
        ruleException.setRulePackage(selectedRulePackage);
        if (!feasibleDate(ruleException) || !feasibleTime(ruleException)) {
            me.addInfoMessage("conflict");
            return;
        }
        ruleException.setDeny(ruleDeny);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRuleException");
        RuleException addedRulePackage = null;
        try {
            addedRulePackage = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(ruleException)), RuleException.class);
            if (addedRulePackage != null) {

                refresh();
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/fillRulePackageHashTable");
                new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(1));
                me.addInfoMessage("operation.occurred");
                me.redirect("/business-rules/list-exception-rule.htm");
            } else {
                me.redirect("/business-rules/list-exception-rule.htm");
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

    public boolean feasibleDate(RuleException rule) {
        String startDateTime = rule.getFromDate() + rule.getStartTime();
        startDateTime = startDateTime.replace(":", "").replace("/", "");
        String endDateTime = rule.getToDate() + rule.getEndTime();
        endDateTime = endDateTime.replace(":", "");
        endDateTime = endDateTime.replace("/", "");

        return Long.valueOf(endDateTime) >= Long.valueOf(startDateTime);
    }

    public boolean feasibleTime(RuleException rule) {


        long startTime = time2long(rule.getStartTime());
        long endTime = time2long(rule.getEndTime());

        if (endTime <= startTime)
            return false;

        else
            return true;
    }


    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }
//
//    public Filter<?> getExceptionNameFilterImpl() {
//        return new Filter<RuleException>() {
//            public boolean accept(RuleException ruleException) {
//                return exceptionNameFilter == null || exceptionNameFilter.length() == 0 || ruleException.getName().toLowerCase().contains(exceptionNameFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByExceptionName() {
        if (exceptionNameOrder.equals(SortOrder.ASCENDING)) {
            setExceptionNameOrder(SortOrder.DESCENDING);
        } else {
            setExceptionNameOrder(SortOrder.ASCENDING);
        }
    }

    public void selectForEdit() {
        currentRuleException = ruleExceptionList.getRowData();
        setSelectRow(true);

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

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public int getPage() {
        currentRuleException = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public ArrayList<RulePackage> getRulePackages() {
        return rulePackages;
    }

    public void setRulePackages(ArrayList<RulePackage> rulePackages) {
        this.rulePackages = rulePackages;
    }

    public RuleException getCurrentRuleException() {
        return currentRuleException;
    }

    public void setCurrentRuleException(RuleException currentRuleException) {
        this.currentRuleException = currentRuleException;
    }

    public DataModel<RuleException> getRuleExceptionList() {
        return ruleExceptionList;
    }

    public void setRuleExceptionList(DataModel<RuleException> ruleExceptionList) {
        this.ruleExceptionList = ruleExceptionList;
    }

    public ArrayList<RuleException> getRuleExceptions() {
        return ruleExceptions;
    }

    public void setRuleExceptions(ArrayList<RuleException> ruleExceptions) {
        this.ruleExceptions = ruleExceptions;
    }

    public String getRulePackageId() {
        return rulePackageId;
    }

    public void setRulePackageId(String rulePackageId) {
        this.rulePackageId = rulePackageId;
    }

    public DataModel<RulePackage> getRulePackageList() {
        return rulePackageList;
    }


    public void setRulePackageList(DataModel<RulePackage> rulePackageList) {
        this.rulePackageList = rulePackageList;
    }

    public ArrayList<RulePackage> getSelectedRulePackage() {
        return selectedRulePackage;
    }

    public void setSelectedRulePackage(ArrayList<RulePackage> selectedRulePackage) {
        this.selectedRulePackage = selectedRulePackage;
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

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public boolean isRuleDeny() {
        return ruleDeny;
    }

    public void setRuleDeny(boolean ruleDeny) {
        this.ruleDeny = ruleDeny;
    }

    public SortOrder getExceptionNameOrder() {
        return exceptionNameOrder;
    }

    public void setExceptionNameOrder(SortOrder exceptionNameOrder) {
        this.exceptionNameOrder = exceptionNameOrder;
    }

    public String getExceptionNameFilter() {
        return exceptionNameFilter;
    }

    public void setExceptionNameFilter(String exceptionNameFilter) {
        this.exceptionNameFilter = exceptionNameFilter;
    }
}
