package ir.university.toosi.wtms.web.action.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.AccessControlAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.PC;
import ir.university.toosi.tms.model.entity.SystemParameterType;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.Role;
import ir.university.toosi.tms.model.entity.User;
import ir.university.toosi.tms.model.entity.WorkGroup;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.LangUtils;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
//import org.apache.commons.lang.StringUtils;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;


import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
//import javax.validation.constraints.AssertTrue;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleUserAction")
@SessionScoped
public class HandleUserAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleWorkGroupAction handleWorkGroupAction;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private AccessControlAction accessControlAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    private DataModel<User> userList = null;
    private DataModel<Role> roleList = null;
    private DataModel<Role> workGroupRoleList = null;
    private User currentUser = null;
    private User newUser = null;
    private String nationalCode;
    private String firstname;
    private String lastname;
    private String username;
    private String email;
    private String mobile;
    private String address;
    private String status;
    private String extraField1;
    private String extraField2;
    private String extraField3;
    private String extraField4;
    private boolean extraField1Enable;
    private boolean extraField2Enable;
    private boolean extraField3Enable;
    private boolean extraField4Enable;
    private String password = "";
    private String newPassword;
    private String rePassword;
    private String chatFirstName = "";
    private String chatLastName = "";
    private DataModel<PC> pcList;
    private DataModel<Person> personList;
    private PC currentPC;
    private Person currentPerson;
    private String searchOrganizationName;
    private String editable = "false";
    private String selectedParticipant = "0";
    private String selectedWorkGroupId = "0";
    private String workgroup;
    private boolean enabled = true;
    private boolean selectAll = false;
    private String lock = "false";
    private boolean roleEnabled;
    private int page = 1;
    private int pcPage = 1;
    private int personPage = 1;
    private SelectItem[] personItem;
    private Set<WorkGroup> selectedWorkGroup;
    private Set<PC> selectedPCs;
    private Person selectedPerson;
    private boolean selectRow = false;
    private String oldPassword;

    private String usernameFilter;
    private String workgroupFilter;
    private String personNameFilter;
    private String personLastNameFilter;

    private SortOrder usernameOrder = SortOrder.UNSORTED;
    private SortOrder workgroupOrder = SortOrder.UNSORTED;
    private SortOrder pcIpOrder = SortOrder.UNSORTED;
    private SortOrder roleDescriptionOrder = SortOrder.UNSORTED;
    private SortOrder personNameOrder = SortOrder.UNSORTED;
    private SortOrder personLastNameOrder = SortOrder.UNSORTED;

    private boolean disableFields;

    public String begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        return "list-user";
    }

    public void init() {
        firstname = "";
        lastname = "";
        nationalCode = "";
        username = "";
        password = "";
        password = "";
        newPassword = "";
        rePassword = "";
        oldPassword = "";
        enabled = true;
        roleEnabled = true;
        selectedWorkGroup = null;
        page = 1;
        handleWorkGroupAction.refresh();
        handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
        currentUser = null;
        setSelectRow(false);
        extraField1Enable = me.getSystemParameter().get(SystemParameterType.USER_EXTRA_FIELD_1).equalsIgnoreCase("true") ? true : false;
        extraField2Enable = me.getSystemParameter().get(SystemParameterType.USER_EXTRA_FIELD_2).equalsIgnoreCase("true") ? true : false;
        extraField3Enable = me.getSystemParameter().get(SystemParameterType.USER_EXTRA_FIELD_3).equalsIgnoreCase("true") ? true : false;
        extraField4Enable = me.getSystemParameter().get(SystemParameterType.USER_EXTRA_FIELD_4).equalsIgnoreCase("true") ? true : false;
        usernameFilter = "";
        workgroupFilter = "";
        personNameFilter = "";
        personLastNameFilter = "";
    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllUser");
        List<User> innerUserList = null;
        try {
            innerUserList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<User>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        userList = new ListDataModel<>(innerUserList);
        handleRoleAction.setRoleList(new ArrayList<Role>());
    }

    public void personChange(ValueChangeEvent event) {
        String id = (String) event.getNewValue();
        if (!id.equalsIgnoreCase("0")) {
            selectedPerson = null;//me.getGeneralHelper().getWorkGroupService().findById(id);
        } else {
            selectedPerson = null;
        }
    }

    public void selectPCs(ValueChangeEvent event) {
        currentPC = pcList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentPC.setSelected(true);
            selectedPCs.add(currentPC);
        } else {
            currentPC.setSelected(false);
            selectedPCs.remove(currentPC);
        }
    }

    public void selectAllPC(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (PC pc : pcList) {
                pc.setSelected(true);
                selectedPCs.add(pc);
            }
        } else {
            for (PC pc : pcList) {
                pc.setSelected(false);
            }
            selectedPCs.clear();
        }
    }


    public void doDelete() {
        if(currentUser.getWorkGroups() !=null && currentUser.getWorkGroups().size()>0){
            me.addInfoMessage("user.has.role");
            me.redirect("/user/list-user.htm");
        }
        if (currentUser.getUsername().equalsIgnoreCase(me.getUsername())) {
            me.addInfoMessage("delete.self");
            me.redirect("/user/list-user.htm");
        }
        String currentDate = LangUtils.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd"));
        String currentTime = CalendarUtil.getTime(new Date(), new Locale("fa"));
        currentUser.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteUser");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentUser)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/user/list-user.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        setEditable("false");
    }

    public void assignPC() {
        selectedPCs = new HashSet<>();
        selectAll = false;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPC");
        try {
            List<PC> pcs = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<PC>>() {
            });
            for (PC pc : pcs) {
                for (PC pc1 : currentUser.getPcs()) {
                    if (pc1.getId() == pc.getId())
                        pc.setSelected(true);
                    selectedPCs.add(pc);
                }
            }
            pcList = new ListDataModel<>(pcs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void associatePCs() {
        currentUser.setPcs(selectedPCs);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentUser)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                selectedPCs = new HashSet<>();
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/user/list-user.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void assignPerson() {
        handlePersonAction.setPersonnameOrder(SortOrder.UNSORTED);
        handlePersonAction.setPersonFamilyOrder(SortOrder.ASCENDING);
        handlePersonAction.setPersonnameFilter("");
        handlePersonAction.setPersonFamilyFilter("");
        personPage=1;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPersonDataModel");
        List<Person> innerPersonList = null;
        try {
            innerPersonList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        personList = new ListDataModel<>(innerPersonList);
    }

    public void associatePerson() {
        currentPerson = personList.getRowData();
        currentUser.setPerson(currentPerson);
        currentUser.setFirstname("");
        currentUser.setLastname("");

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentUser)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/user/list-user.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void doAdd() {
        newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setFirstname(firstname);
        newUser.setLastname(lastname);
        newUser.setDeleted("0");
        newUser.setEnable(enabled == true ? "true" : "false");
        newUser.setStatus("c");
        newUser.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existUser");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newUser)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        Set<WorkGroup> selectedWorkGroup = new HashSet<>();
        for (WorkGroup workGroup : handleWorkGroupAction.getSelectWorkGroups()) {

            if (workGroup.isSelected()) {
                selectedWorkGroup.add(workGroup);
            }
        }
        if (selectedWorkGroup.size() == 0) {
            me.addErrorMessage("no_workgroup_selected");
            return;
        }
        newUser.setWorkGroups(selectedWorkGroup);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createUser");
        User user = null;
        try {
            user = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newUser)), User.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (user != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/user/list-user.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }


    public void edit() {
        setEditable("true");
        setDisableFields(false);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findUserById");
        try {
            currentUser = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentUser.getId())), User.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        firstname = currentUser.getFirstname();
        lastname = currentUser.getLastname();
        username = currentUser.getUsername();
        enabled = currentUser.getEnable().equalsIgnoreCase("true") ? true : false;

        List<WorkGroup> sourceWorkgroups = new ArrayList<>();
        List<WorkGroup> targetWorkgroups = new ArrayList<>();

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllWorkGroup");
        List<WorkGroup> workGroups = null;
        try {
            workGroups = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<WorkGroup>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
        for (WorkGroup workGroup : workGroups) {
            workGroup.setDescText(me.getValue(workGroup.getDescription()));
        }
        for (WorkGroup currentWorkGroup : currentUser.getWorkGroups()) {
            for (WorkGroup workGroup : workGroups) {
                if ((currentWorkGroup.getId() == workGroup.getId())) {
                    workGroup.setSelected(true);
                    workGroup.setDescription(me.getValue(currentWorkGroup.getDescription()));
                    handleWorkGroupAction.getSelectWorkGroups().add(workGroup);
                    targetWorkgroups.add(workGroup);
                } else {
                    sourceWorkgroups.add(workGroup);
                }
            }
        }

        handleWorkGroupAction.setWorkGroupList(new ListDataModel<>(workGroups));
        handleWorkGroupAction.setWorkgroups(new DualListModel<WorkGroup>(sourceWorkgroups,targetWorkgroups));

    }


    public void doEdit() {
        if (handleWorkGroupAction.getSelectWorkGroups() == null) {
            me.addErrorMessage("workGroup_not_selected");
            return;
        }
        currentUser.setEnable(status);
        currentUser.setFirstname(firstname);
        currentUser.setEnable(enabled == true ? "true" : "false");
        currentUser.setLastname(lastname);
        currentUser.setWorkGroups(handleWorkGroupAction.getSelectWorkGroups());
        currentUser.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");
        if(firstname.trim().length() > 0 || lastname.trim().length() > 0 )
            currentUser.setPerson(null);
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentUser)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                handleWorkGroupAction.setSelectWorkGroups(new HashSet<WorkGroup>());
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/user/list-user.htm");
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

    public void userDetail() {
//        workGroupRoleList = new DataModel<>(new ArrayList<>(currentUser.getWorkGroups().getRoles()));
        enabled = currentUser.getEnable() == "true" ? true : false;
        username = currentUser.getUsername();
        List<User> users = null;//me.getGeneralHelper().getUserService().getAllPending(currentUser);


    }
//
//    public Filter<?> getUsernameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return usernameFilter == null || usernameFilter.length() == 0 || user.getUsername().toLowerCase().contains(usernameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getPersonNameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return StringUtils.isEmpty(personNameFilter) || user.getPerson().getName().contains(personNameFilter);
//            }
//        };
//    }
//
//    public Filter<?> getPersonLastNameFilterImpl() {
//        return new Filter<User>() {
//            public boolean accept(User user) {
//                return StringUtils.isEmpty(personLastNameFilter) || user.getPerson().getLastName().contains(personLastNameFilter);
//            }
//        };
//    }

    public void sortByUsername() {
        usernameOrder = newSortOrder(usernameOrder);
    }

    public void sortByworkgroup() {
        workgroupOrder = newSortOrder(workgroupOrder);
    }

    public void sortByPcIp() {
        pcIpOrder = newSortOrder(pcIpOrder);
    }

    public void sortByRoleDescription() {
        roleDescriptionOrder = newSortOrder(roleDescriptionOrder);
    }

    public void sortByPersonName() {
        personNameOrder = newSortOrder(personNameOrder);
    }

    public void sortByPersonLastName() {
        personLastNameOrder = newSortOrder(personLastNameOrder);
    }


    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        usernameOrder = SortOrder.UNSORTED;
        workgroupOrder = SortOrder.UNSORTED;
        pcIpOrder = SortOrder.UNSORTED;
        roleDescriptionOrder = SortOrder.UNSORTED;

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }
    }

//    @AssertTrue
    public boolean isPasswordsEquals() {
        if (!rePassword.equals(newPassword)) {
            me.addInfoMessage(" Different passwords entered!");
            return false;
        }
        return true;
    }

    public void storeNewPassword() {
        me.addInfoMessage("success");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editUser");
        currentUser = me.getUser();
        currentUser.setPassword(newPassword);
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentUser)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/user/list-user.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectForEdit() {
        currentUser = userList.getRowData();
        setSelectRow(true);

    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<User> getUserList() {
        return userList;
    }

    public void setUserList(DataModel<User> userList) {
        this.userList = userList;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User getNewUser() {
        return newUser;
    }

    public void setNewUser(User newUser) {
        this.newUser = newUser;
    }

    public SortOrder getUsernameOrder() {
        return usernameOrder;
    }

    public void setUsernameOrder(SortOrder usernameOrder) {
        this.usernameOrder = usernameOrder;
    }

    public String getUsernameFilter() {
        return usernameFilter;
    }

    public void setUsernameFilter(String usernameFilter) {
        this.usernameFilter = usernameFilter;
    }

    public SortOrder getWorkgroupOrder() {
        return workgroupOrder;
    }

    public void setWorkgroupOrder(SortOrder workgroupOrder) {
        this.workgroupOrder = workgroupOrder;
    }

    public String getWorkgroupFilter() {
        return workgroupFilter;
    }

    public void setWorkgroupFilter(String workgroupFilter) {
        this.workgroupFilter = workgroupFilter;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getSearchOrganizationName() {
        return searchOrganizationName;
    }

    public void setSearchOrganizationName(String searchOrganizationName) {
        this.searchOrganizationName = searchOrganizationName;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSelectedParticipant() {
        return selectedParticipant;
    }

    public void setSelectedParticipant(String selectedParticipant) {
        this.selectedParticipant = selectedParticipant;
    }

    public String getSelectedWorkGroupId() {
        return selectedWorkGroupId;
    }

    public void setSelectedWorkGroupId(String selectedWorkGroupId) {
        this.selectedWorkGroupId = selectedWorkGroupId;
    }

    public String getChatFirstName() {
        return chatFirstName;
    }

    public void setChatFirstName(String chatFirstName) {
        this.chatFirstName = chatFirstName;
    }

    public String getChatLastName() {
        return chatLastName;
    }

    public void setChatLastName(String chatLastName) {
        this.chatLastName = chatLastName;
    }


    public String getWorkgroup() {
        return workgroup;
    }

    public void setWorkgroup(String workgroup) {
        this.workgroup = workgroup;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DataModel<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(DataModel<Role> roleList) {
        this.roleList = roleList;
    }

    public void userEnable(ValueChangeEvent event) {
        boolean status = (Boolean) event.getNewValue();
        if (!status) {
            currentUser.setEnable("false");
        } else
            currentUser.setEnable("true");

    }

    public void userEnableChange(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            status = "true";
        } else
            status = "false";

    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public boolean isRoleEnabled() {
        return roleEnabled;
    }

    public void setRoleEnabled(boolean roleEnabled) {
        this.roleEnabled = roleEnabled;
    }

    public int getPage() {
        currentUser = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<Role> getWorkGroupRoleList() {
        return workGroupRoleList;
    }

    public void setWorkGroupRoleList(DataModel<Role> workGroupRoleList) {
        this.workGroupRoleList = workGroupRoleList;
    }

    public SelectItem[] getPersonItem() {
        return personItem;
    }

    public void setPersonItem(SelectItem[] personItem) {
        this.personItem = personItem;
    }

    public Person getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(Person selectedPerson) {
        this.selectedPerson = selectedPerson;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }

    public DataModel<PC> getPcList() {
        return pcList;
    }

    public void setPcList(DataModel<PC> pcList) {
        this.pcList = pcList;
    }

    public DataModel<Person> getPersonList() {
        return personList;
    }

    public void setPersonList(DataModel<Person> personList) {
        this.personList = personList;
    }

    public PC getCurrentPC() {
        return currentPC;
    }

    public void setCurrentPC(PC currentPC) {
        this.currentPC = currentPC;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Set<PC> getSelectedPCs() {
        return selectedPCs;
    }

    public void setSelectedPCs(Set<PC> selectedPCs) {
        this.selectedPCs = selectedPCs;
    }

    public Set<WorkGroup> getSelectedWorkGroup() {
        return selectedWorkGroup;
    }

    public void setSelectedWorkGroup(Set<WorkGroup> selectedWorkGroup) {
        this.selectedWorkGroup = selectedWorkGroup;
    }

    public int getPcPage() {
        return pcPage;
    }

    public void setPcPage(int pcPage) {
        this.pcPage = pcPage;
    }

    public int getPersonPage() {
        return personPage;
    }

    public void setPersonPage(int personPage) {
        this.personPage = personPage;
    }

    public boolean isSelectAll() {
        return selectAll;
    }

    public void setSelectAll(boolean selectAll) {
        this.selectAll = selectAll;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public HandleWorkGroupAction getHandleWorkGroupAction() {
        return handleWorkGroupAction;
    }

    public void setHandleWorkGroupAction(HandleWorkGroupAction handleWorkGroupAction) {
        this.handleWorkGroupAction = handleWorkGroupAction;
    }

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isExtraField1Enable() {
        return extraField1Enable;
    }

    public void setExtraField1Enable(boolean extraField1Enable) {
        this.extraField1Enable = extraField1Enable;
    }

    public boolean isExtraField2Enable() {
        return extraField2Enable;
    }

    public void setExtraField2Enable(boolean extraField2Enable) {
        this.extraField2Enable = extraField2Enable;
    }

    public boolean isExtraField3Enable() {
        return extraField3Enable;
    }

    public void setExtraField3Enable(boolean extraField3Enable) {
        this.extraField3Enable = extraField3Enable;
    }

    public boolean isExtraField4Enable() {
        return extraField4Enable;
    }

    public void setExtraField4Enable(boolean extraField4Enable) {
        this.extraField4Enable = extraField4Enable;
    }

    public SortOrder getPcIpOrder() {
        return pcIpOrder;
    }

    public void setPcIpOrder(SortOrder pcIpOrder) {
        this.pcIpOrder = pcIpOrder;
    }

    public SortOrder getRoleDescriptionOrder() {
        return roleDescriptionOrder;
    }

    public void setRoleDescriptionOrder(SortOrder roleDescriptionOrder) {
        this.roleDescriptionOrder = roleDescriptionOrder;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public SortOrder getPersonNameOrder() {
        return personNameOrder;
    }

    public void setPersonNameOrder(SortOrder personNameOrder) {
        this.personNameOrder = personNameOrder;
    }

    public SortOrder getPersonLastNameOrder() {
        return personLastNameOrder;
    }

    public void setPersonLastNameOrder(SortOrder personLastNameOrder) {
        this.personLastNameOrder = personLastNameOrder;
    }

    public String getPersonNameFilter() {
        return personNameFilter;
    }

    public void setPersonNameFilter(String personNameFilter) {
        this.personNameFilter = personNameFilter;
    }

    public String getPersonLastNameFilter() {
        return personLastNameFilter;
    }

    public void setPersonLastNameFilter(String personLastNameFilter) {
        this.personLastNameFilter = personLastNameFilter;
    }

    public boolean isDisableFields() {
        return disableFields;
    }

    public void setDisableFields(boolean disableFields) {
        this.disableFields = disableFields;
    }
}