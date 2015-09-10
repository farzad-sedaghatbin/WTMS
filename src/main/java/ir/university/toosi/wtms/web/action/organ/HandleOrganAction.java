package ir.university.toosi.wtms.web.action.organ;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
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

/**
 * @author : Mostafa Rastgar
 * @version : 0.8
 */

@Named(value = "handleOrganAction")
@SessionScoped
public class HandleOrganAction implements Serializable {
    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    private DataModel<Organ> organList = null;
    private DataModel<Person> personList = null;
    private Organ parentOrgan = null;
    private String editable = "false";
    private String organName;
    private String organTitle;
    private String organCode;
    private BLookup organType;
    private List<BLookup> organTypes;
    private Organ currentOrgan = null;
    private String currentPage;
    private int page = 1;
    private int pageInPopup = 1;
    private boolean selected;
    private Set<Organ> selectedOrgans = new HashSet<>();
    private RulePackage selectedRulePackage;
    private String rulePackageName;
    private String calendarName;
    private boolean antiPassBack, allowExit, allowExitGadget;
    private DataModel<RulePackage> rulePackageList = null;
    private List<Organ> rootOrgans;
    private boolean inheritance;
    private String name;
    private ArrayList<Rule> ruleArrayList = new ArrayList<>();

    private boolean ruleAniPassBack = false;
    private boolean ruleAllowExit = false;
    private boolean ruleAllowExitGadget = false;
    private boolean selectRow = false;
    private DataModel<Rule> ruleListTemp = null;
    private Calendar selectedCalendar;
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
    private String editableRule = "false";
    private boolean addNewRuleFlag = false;
    private Rule currentRule;
    private SelectItem[] dayTypeItems;
    private SelectItem[] calendarItems = me.calendarItem;
    private Hashtable<String, DayType> dayTypeHashtable = new Hashtable<>();
    private String organNameFilter;
    private String organTypeFilter;
    private String organDescriptionFilter;
    private SortOrder organNameOrder = SortOrder.UNSORTED;
    private SortOrder organTypeOrder = SortOrder.UNSORTED;
    private SortOrder organDescriptionOrder = SortOrder.UNSORTED;
    private int personPage = 1;
    private String listPerson;


    public String begin() {
//        me.setActiveMenu(MenuType.MANAGEMENT);

        refresh();
        return "list-organ";
    }

    public DataModel<Organ> getSelectionGrid() {
        refresh();
        return organList;
    }

    private void refresh() {
        init();
        WebServiceInfo organService = new WebServiceInfo();
        try {
            List<Organ> organs;
            if (parentOrgan != null) {
                organService.setServiceName("/getAllActiveOrganByParent");
                organs = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(organService.getServerUrl(), organService.getServiceName(), String.valueOf(parentOrgan.getId())), new TypeReference<List<Organ>>() {
                });
            } else {
                organService.setServiceName("/getAllActiveOrgan");
                organs = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(organService.getServerUrl(), organService.getServiceName()), new TypeReference<List<Organ>>() {
                });
            }
            organList = new ListDataModel<>(organs);
        } catch (IOException e) {
            e.printStackTrace();
        }
        calendarItems = me.calendarItem;
        page = 1;
        fillDayTypeCombo();
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

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentOrgan.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteOrgan");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOrgan)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/organ/list-organ.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        organName = "";
        organTitle = "";
        organCode = "";
        organType = null;
        page = 1;
        organTypeFilter = "";
        personPage = 1;
        rootOrgans = null;
        currentOrgan = null;
        organNameFilter = "";
        organDescriptionFilter = "";

        setSelectRow(false);
    }

    public void changeParentOrgan(Long id) {
        if (id.equals(-1l)) {
            parentOrgan = null;
        } else {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findOrganById");
            try {
                parentOrgan = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(id)), Organ.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        refresh();
    }

    public void changeParentOrgan() {
        parentOrgan = organList.getRowData();
        refresh();
    }

    public void listPerson() {

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPersonByOrganId");
        try {
            personList = new ListDataModel<>((List<Person>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentOrgan.getId())), new TypeReference<List<Person>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    public void edit() {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findOrganById");
        try {
            currentOrgan = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentOrgan.getId())), Organ.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        organName = currentOrgan.getName();
        organTitle = currentOrgan.getTitle();
        organCode = currentOrgan.getCode();
        organType = currentOrgan.getOrganType();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editOrgan");
        currentOrgan.setOrganType(getOrganType());
        currentOrgan.setCode(organCode);
        currentOrgan.setTitle(organTitle);
        currentOrgan.setName(organName);
        currentOrgan.setInheritance(inheritance);
        currentOrgan.setEffectorUser(me.getUsername());
        if (parentOrgan != null) {
            currentOrgan.setParentOrgan(parentOrgan);
        } else {
            currentOrgan.setParentOrgan(null);
        }
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOrgan)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/organ/list-organ.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doAdd() {
        Organ newOrgan = new Organ();
        newOrgan.setName(organName);
        newOrgan.setTitle(organTitle);
        newOrgan.setCode(organCode);
        newOrgan.setInheritance(inheritance);
        newOrgan.setOrganType(getOrganType());
        if (parentOrgan != null) {
            newOrgan.setParentOrgan(parentOrgan);
            if (parentOrgan.isInheritance())
                newOrgan.setRulePackage(parentOrgan.getRulePackage());
            else
                newOrgan.setRulePackage(null);
        } else {
            newOrgan.setParentOrgan(null);
            newOrgan.setRulePackage(null);
        }
        newOrgan.setDeleted("0");
        newOrgan.setStatus("c");
        newOrgan.setEffectorUser(me.getUsername());

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existOrgan");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOrgan)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("organ.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createOrgan");
        Organ insertedOrgan = null;
        try {
            insertedOrgan = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOrgan)), Organ.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedOrgan != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/organ/list-organ.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }


    public void assignRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findOrganById");
        try {
            currentOrgan = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentOrgan.getId())), Organ.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        selectedRulePackage = currentOrgan.getRulePackage();
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
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editOrgan");
        currentOrgan.setEffectorUser(me.getUsername());
        currentOrgan.setRulePackage(selectedRulePackage);
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOrgan)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/organ/list-organ.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void selectNewRuleForOrgan() {
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

    public void editRule() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findOrganById");
        try {
            currentOrgan = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentOrgan.getId())), Organ.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if (currentOrgan.getRulePackage() == null) {
            refresh();
            me.addErrorMessage("has.not.rulePackage");
            me.redirect("/organ/list-organ.htm");
            return;
        }

        editOrganRule(currentOrgan.getRulePackage());
    }


    public void editOrganRule(RulePackage rulePackage) {
        ruleArrayList = new ArrayList<>();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getRulesByRulePackage");
        try {
            ruleArrayList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(rulePackage.getId())), new TypeReference<List<Rule>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ruleListTemp = new ListDataModel<>(ruleArrayList);
        name = rulePackage.getName();
        ruleAllowExitGadget = rulePackage.isAllowExitGadget();
        ruleAniPassBack = rulePackage.isAniPassBack();
        ruleAllowExit = rulePackage.isAllowExit();
        selectedCalendar = rulePackage.getCalendar();
        selectedCalendarIdTemp = String.valueOf(selectedCalendar.getId());
        editable = "true";
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
        ruleStartTime = startHour + ":" + startMinute + ":" + startSecond;
        ruleEndTime = endHour + ":" + endMinute + ":" + endSecond;
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

        if (rule.isDeny())
            return true;
        long startTime = time2long(rule.getStartTime());
        long endTime = time2long(rule.getEndTime());
        boolean flag = true;

        if (endTime < startTime)
            return false;

        for (Rule rule1 : ruleArrayList) {
            if (rule1.getDayType().getId() != rule.getDayType().getId())
                continue;
            if (startTime >= time2long(rule1.getStartTime()) && endTime <= time2long(rule1.getStartTime()))
                flag = true;
            else if (startTime > time2long(rule1.getEndTime()) && endTime > time2long(rule1.getEndTime()))
                flag = true;
            else {
                flag = false;
                break;
            }
        }

        return flag;
    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public void doEditOrganRule() {
        RulePackage newRulePackage = new RulePackage();
        newRulePackage.setStatus("c");
        newRulePackage.setDeleted("0");
        newRulePackage.setEffectorUser(me.getUsername());
        newRulePackage.setName(name + "_" + currentOrgan.getCode());
        newRulePackage.setAllowExit(ruleAllowExit);
        newRulePackage.setAniPassBack(ruleAniPassBack);
        newRulePackage.setAllowExitGadget(ruleAllowExitGadget);
        newRulePackage.setCalendar(me.calendarHashtable.get(selectedCalendarIdTemp));
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRulePackage");

        RulePackage addedRulePackage = null;
        try {
            addedRulePackage = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newRulePackage)), RulePackage.class);
            if (addedRulePackage != null) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRule");
                for (Rule rule : ruleArrayList) {
                    rule.setRulePackage(addedRulePackage);
                    new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(rule)), Rule.class);
                }

                currentOrgan.setRulePackage(addedRulePackage);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editOrgan");
                try {
                    String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOrgan)), String.class);
                    if (condition.equalsIgnoreCase("true")) {
                        refresh();
                        me.addInfoMessage("operation.occurred");
                        me.redirect("/organ/list-organ.htm");
                    } else {
                        me.addInfoMessage("operation.not.occurred");
                        return;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

/*
    public Filter<?> getOrganNameFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return organNameFilter == null || organNameFilter.length() == 0 || organ.getName().toLowerCase().contains(organNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getOrganTypeFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return StringUtils.isEmpty(organTypeFilter) || organ.getOrganType().getTitleText().toLowerCase().contains(organTypeFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getOrganDescriptionFilterImpl() {
        return new Filter<Organ>() {
            public boolean accept(Organ organ) {
                return organDescriptionFilter == null || organDescriptionFilter.length() == 0 || organ.getTitle().toLowerCase().contains(organDescriptionFilter.toLowerCase());
            }
        };
    }*/

    public void sortByOrganName() {
        organNameOrder = newSortOrder(organNameOrder);
    }

    public void sortByOrganType() {
        organTypeOrder = newSortOrder(organTypeOrder);
    }

    public void sortByOrganDescription() {
        organDescriptionOrder = newSortOrder(organDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        organNameOrder = SortOrder.UNSORTED;
        organTypeOrder = SortOrder.UNSORTED;
        organDescriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }
    }

    public List<Organ> getAllParents() {
        List<Organ> parents = new ArrayList();
        addParents(parents, parentOrgan);
        return parents;
    }

    private void addParents(List<Organ> parents, Organ parentOrgan) {
        if (parentOrgan != null) {
            addParents(parents, parentOrgan.getParentOrgan());
        }
        parents.add(parentOrgan);
    }

    public void selectOrganType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getOrganTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                organType = bLookup;
            }
        }
    }

    public void selectForEdit() {
        currentOrgan = organList.getRowData();
        setSelectRow(true);
    }

    public void setOrganList(DataModel<Organ> organList) {
        this.organList = organList;
    }

    public String getOrganDescriptionFilter() {
        return organDescriptionFilter;
    }

    public void setOrganDescriptionFilter(String organDescriptionFilter) {
        this.organDescriptionFilter = organDescriptionFilter;
    }

    public SortOrder getOrganDescriptionOrder() {
        return organDescriptionOrder;
    }

    public void setOrganDescriptionOrder(SortOrder organDescriptionOrder) {
        this.organDescriptionOrder = organDescriptionOrder;
    }

    public Organ getParentOrgan() {
        return parentOrgan;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getOrganName() {
        return organName;
    }

    public void setOrganName(String organName) {
        this.organName = organName;
    }

    public String getOrganTitle() {
        return organTitle;
    }

    public void setOrganTitle(String organTitle) {
        this.organTitle = organTitle;
    }

    public String getOrganCode() {
        return organCode;
    }

    public void setOrganCode(String organCode) {
        this.organCode = organCode;
    }

    public BLookup getOrganType() {
        if (organType == null) {
            if (getOrganTypes().size() > 0) {
                organType = getOrganTypes().get(0);
            }
        }
        return organType;
    }

    public void selectInheritance(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            inheritance = true;
        } else {
            inheritance = false;
        }
    }


    public void setOrganType(BLookup organType) {
        this.organType = organType;
    }

    public List<BLookup> getOrganTypes() {
        if (organTypes == null) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                organTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.ORGAN_TYPE_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : organTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return organTypes;
    }

    public void resetPersonPage() {
        setPersonPage(1);
    }

    public Set<Organ> getSelectedOrgans() {
        return selectedOrgans;
    }

    public void setSelectedOrgans(Set<Organ> selectedOrgans) {
        this.selectedOrgans = selectedOrgans;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentOrgan = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<Organ> getOrganList() {
        return organList;
    }

    public Organ getCurrentOrgan() {
        return currentOrgan;
    }

    public void setCurrentOrgan(Organ currentOrgan) {
        this.currentOrgan = currentOrgan;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public List<Organ> getRootOrgans() {
        if (rootOrgans == null) {
            try {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllOrgan");
                List<Organ> organs = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Organ>>() {
                });
//                rootOrgans = Organ.prepareHierarchy(organs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rootOrgans;
    }

    public void setRootOrgans(List<Organ> rootOrgans) {
        this.rootOrgans = rootOrgans;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
    }

    public void setParentOrgan(Organ parentOrgan) {
        this.parentOrgan = parentOrgan;
    }

    public void setOrganTypes(List<BLookup> organTypes) {
        this.organTypes = organTypes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Rule> getRuleArrayList() {
        return ruleArrayList;
    }

    public void setRuleArrayList(ArrayList<Rule> ruleArrayList) {
        this.ruleArrayList = ruleArrayList;
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

    public DataModel<Rule> getRuleListTemp() {
        return ruleListTemp;
    }

    public void setRuleListTemp(DataModel<Rule> ruleListTemp) {
        this.ruleListTemp = ruleListTemp;
    }

    public Calendar getSelectedCalendar() {
        return selectedCalendar;
    }

    public void setSelectedCalendar(Calendar selectedCalendar) {
        this.selectedCalendar = selectedCalendar;
    }

    public DayType getRuleDayType() {
        return ruleDayType;
    }

    public void setRuleDayType(DayType ruleDayType) {
        this.ruleDayType = ruleDayType;
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

    public Boolean getRuleDeny() {
        return ruleDeny;
    }

    public void setRuleDeny(Boolean ruleDeny) {
        this.ruleDeny = ruleDeny;
    }

    public String getEditableRule() {
        return editableRule;
    }

    public void setEditableRule(String editableRule) {
        this.editableRule = editableRule;
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

    public SelectItem[] getDayTypeItems() {
        return dayTypeItems;
    }

    public void setDayTypeItems(SelectItem[] dayTypeItems) {
        this.dayTypeItems = dayTypeItems;
    }

    public SelectItem[] getCalendarItems() {
        return calendarItems;
    }

    public void setCalendarItems(SelectItem[] calendarItems) {
        this.calendarItems = calendarItems;
    }

    public Hashtable<String, DayType> getDayTypeHashtable() {
        return dayTypeHashtable;
    }

    public void setDayTypeHashtable(Hashtable<String, DayType> dayTypeHashtable) {
        this.dayTypeHashtable = dayTypeHashtable;
    }

    public boolean isInheritance() {
        return inheritance;
    }

    public void setInheritance(boolean inheritance) {
        this.inheritance = inheritance;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public int getPageInPopup() {
        return pageInPopup;
    }

    public void setPageInPopup(int pageInPopup) {
        this.pageInPopup = pageInPopup;
    }

    public SortOrder getOrganNameOrder() {
        return organNameOrder;
    }

    public void setOrganNameOrder(SortOrder organNameOrder) {
        this.organNameOrder = organNameOrder;
    }

    public String getOrganNameFilter() {
        return organNameFilter;
    }

    public void setOrganNameFilter(String organNameFilter) {
        this.organNameFilter = organNameFilter;
    }

    public String getOrganTypeFilter() {
        return organTypeFilter;
    }

    public void setOrganTypeFilter(String organTypeFilter) {
        this.organTypeFilter = organTypeFilter;
    }

    public SortOrder getOrganTypeOrder() {
        return organTypeOrder;
    }

    public void setOrganTypeOrder(SortOrder organTypeOrder) {
        this.organTypeOrder = organTypeOrder;
    }

    public DataModel<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(DataModel<Person> personList) {
        this.personList = personList;
    }

    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public String getListPerson() {
        return listPerson;
    }

    public void setListPerson(String listPerson) {
        this.listPerson = listPerson;
    }
}
