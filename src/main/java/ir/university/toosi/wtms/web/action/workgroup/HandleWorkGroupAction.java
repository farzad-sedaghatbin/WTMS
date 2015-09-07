package ir.university.toosi.wtms.web.action.workgroup;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.action.user.HandleUserAction;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.usermanagement.Role;
import ir.university.toosi.wtms.web.model.entity.usermanagement.WorkGroup;
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
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleWorkGroupAction")
@SessionScoped
public class HandleWorkGroupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    @Inject
    private HandleUserAction handleUserAction;
    private DataModel<WorkGroup> workGroupList = null;
    private WorkGroup currentWorkGroup = null;
    private String editable = "false";
    private String description;
    private String descText;
    private String name;
    private String status;
    private List<WorkGroup> workGroups;
    private String currentPage;
    private String selectedWorkgroup = "0";
    private String workGroupTitleFilter;
    private DataModel<Role> roleSelectionGrid = null;
    private WorkGroup selectedWorkGroup = new WorkGroup();
    private List<Role> lastSelected;
    private Set<WorkGroup> selectWorkGroups;
    private String titleFilter;
    private boolean workGroupEnabled;
    private boolean selectAll;
    private int page = 1;
    private String workGroupDescriptionFilter;
    private boolean selectRow = false;
    private SortOrder titleOrder = SortOrder.unsorted;
    private SortOrder workGroupTitleOrder = SortOrder.unsorted;
    private SortOrder workGroupDescriptionOrder = SortOrder.unsorted;

    public String begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        return "list-workgroup";
    }

    public void init() {
        description = "";
        descText = "";
        selectAll = false;
        workGroupEnabled = true;
        status = "true";
        page = 1;
        currentWorkGroup = null;
        setSelectRow(false);
        titleFilter="";
        workGroupDescriptionFilter="";
        workGroupTitleFilter="";
    }

    public void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllWorkGroup");
        try {
            workGroups = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<WorkGroup>>() {
            });
            for (WorkGroup workGroup : workGroups) {
                workGroup.setDescText(me.getValue(workGroup.getDescription()));
            }
            workGroupList = new ListDataModel<>(workGroups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fillGrid() {
        for (WorkGroup workGroup : workGroups) {
            for (WorkGroup selectWorkGroup : selectWorkGroups) {
                if (workGroup.getId() == selectWorkGroup.getId())
                    workGroup.setSelected(true);
            }
        }
        workGroupList = new ListDataModel<>(workGroups);
    }


    public void doDelete() {
        currentWorkGroup.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteWorkGroup");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentWorkGroup)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/workgroup/list-workgroup.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void selectAllWorkGroup(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (WorkGroup workGroup : workGroupList) {
                workGroup.setSelected(true);
                selectWorkGroups.add(workGroup);
            }
        } else {
            for (WorkGroup workGroup : workGroupList) {
                workGroup.setSelected(false);

            }
            selectWorkGroups.clear();
        }
    }

    public void add(String currentPage) {
        init();
        setEditable("false");
        roleSelectionGrid = handleRoleAction.getSelectionGrid();

        for (Role role : roleSelectionGrid) {
            role.setSelected(false);
        }

        handleRoleAction.setCurrentPage(currentPage);
    }

    public void doAdd() {
      /*  if((descText == null) || (descText.isEmpty())){
             me.addErrorMessage("description_is_empty");
            return;
        }
        if((descText == null) || (descText.isEmpty())){
             me.addErrorMessage("description_text_is_empty");
            return;
        }
        if((status == null) || (status.isEmpty())){
             me.addErrorMessage("status_is_empty");
            return;
        }*/
        WorkGroup newWorkgroup = new WorkGroup();
        newWorkgroup.setDescription(descText);
        newWorkgroup.setDescText(descText);
        newWorkgroup.setName(name);
        newWorkgroup.setEnabled(status);
        newWorkgroup.setDeleted("0");
        newWorkgroup.setStatus("c");
        newWorkgroup.setEffectorUser(me.getUsername());
        newWorkgroup.setCurrentLang(me.getLanguages());

        Set<Role> selectedRole = new HashSet<>();
        for (Role role : roleSelectionGrid) {

            if (role.isSelected()) {
                selectedRole.add(role);
            }
        }
        if ((selectedRole.size() == 0)) {
            me.addErrorMessage("no_role_selected");
            return;
        }

        newWorkgroup.setRoles(selectedRole);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createWorkGroup");
        WorkGroup insertedWorkGroup = null;
        try {
            insertedWorkGroup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newWorkgroup)), WorkGroup.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedWorkGroup != null) {
            try {
                me.setLanguage();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/workgroup/list-workgroup.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

    public void edit(String currentPage) {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findWorkGroupById");
        try {
            currentWorkGroup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentWorkGroup.getId())), WorkGroup.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        workGroupEnabled = Boolean.valueOf(currentWorkGroup.getEnabled());
        descText = me.getValue(currentWorkGroup.getDescription());
        status = currentWorkGroup.getEnabled();
        name = currentWorkGroup.getName();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRole");
        List<Role> roles = null;
        try {
            roles = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Role>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        for (Role role : roles) {
            role.setDescText(me.getValue(role.getDescription()));
        }
        handleRoleAction.setSelectedRoles(new HashSet<Role>());
        for (Role role : roles) {
            role.setDescription((me.getValue(role.getDescription())));
        }
        for (Role currentRole : currentWorkGroup.getRoles()) {
            for (Role role : roles) {
                if ((currentRole.getId() == role.getId())) {
                    role.setSelected(true);
                    handleRoleAction.getSelectedRoles().add(role);
                }
            }
        }

        handleRoleAction.setRoleList(new ListDataModel<>(roles));
    }

    public void doEdit() {
        currentWorkGroup.setDescText(descText);
        currentWorkGroup.setEnabled(status);
        currentWorkGroup.setRoles(handleRoleAction.getSelectedRoles());
        currentWorkGroup.setEffectorUser(me.getUsername());
        currentWorkGroup.setCurrentLang(me.getLanguages());
        currentWorkGroup.setName(name);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editWorkGroup");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentWorkGroup)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                handleRoleAction.setSelectedRoles(new HashSet<Role>());
                me.setLanguage();
                refresh();
                handleRoleAction.setSelectedRoles(new HashSet<Role>());
                me.addInfoMessage("operation.occurred");
                me.redirect("/workgroup/list-workgroup.htm");
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

    public void selectWorkGroups(ValueChangeEvent event) {
        currentWorkGroup = workGroupList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentWorkGroup.setSelected(true);
            selectWorkGroups.add(currentWorkGroup);
        } else {
            currentWorkGroup.setSelected(false);
            selectWorkGroups.remove(currentWorkGroup);
        }
    }

    public void workGroupChange(ValueChangeEvent event) {
        handleRoleAction.setRoleList(new ListDataModel<>(new ArrayList<Role>()));
        String id = (String) event.getNewValue();
        if (!id.equalsIgnoreCase("0")) {
            WorkGroup workGroup = null;//me.getGeneralHelper().getWorkGroupService().findById(id);
            if (workGroup != null)
                handleRoleAction.setRoleList(new ListDataModel<>(new ArrayList<>(workGroup.getRoles())));
            selectedWorkGroup = workGroup;
        }
    }

    public void workGroupEnableChange(ValueChangeEvent event) {
        workGroupEnabled = (Boolean) event.getNewValue();
        if (workGroupEnabled) {
            status = "true";
        } else
            status = "false";

    }

    public Filter<?> getWorkGroupDescriptionFilterImpl() {
        return new Filter<WorkGroup>() {
            public boolean accept(WorkGroup workGroup) {
                return workGroupDescriptionFilter == null || workGroupDescriptionFilter.length() == 0 || workGroup.getDescText().toLowerCase().contains(workGroupDescriptionFilter.toLowerCase());
            }
        };
    }

    public void selectForEdit() {
        currentWorkGroup = workGroupList.getRowData();
        setSelectRow(true);

    }


    public void sortByTitle() {
        titleOrder = newSortOrder(titleOrder);
    }


    public void sortByWorkGroupTitle() {
        workGroupTitleOrder = newSortOrder(workGroupTitleOrder);
    }

    public void sortByWorkGroupDescription() {
        workGroupDescriptionOrder = newSortOrder(workGroupDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        titleOrder = SortOrder.unsorted;
        workGroupTitleOrder = SortOrder.unsorted;
        workGroupDescriptionOrder = SortOrder.unsorted;

        if (currentSortOrder.equals(SortOrder.descending)) {
            return SortOrder.ascending;
        } else {
            return SortOrder.descending;
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

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
    }

    public HandleUserAction getHandleUserAction() {
        return handleUserAction;
    }

    public void setHandleUserAction(HandleUserAction handleUserAction) {
        this.handleUserAction = handleUserAction;
    }

    public DataModel<WorkGroup> getWorkGroupList() {
        return workGroupList;
    }

    public void setWorkGroupList(DataModel<WorkGroup> workGroupList) {
        this.workGroupList = workGroupList;
    }

    public WorkGroup getCurrentWorkGroup() {
        return currentWorkGroup;
    }

    public void setCurrentWorkGroup(WorkGroup currentWorkGroup) {
        this.currentWorkGroup = currentWorkGroup;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public String getSelectedWorkgroup() {
        return selectedWorkgroup;
    }

    public void setSelectedWorkgroup(String selectedWorkgroup) {
        this.selectedWorkgroup = selectedWorkgroup;
    }

    public SortOrder getWorkGroupTitleOrder() {
        return workGroupTitleOrder;
    }

    public void setWorkGroupTitleOrder(SortOrder workGroupTitleOrder) {
        this.workGroupTitleOrder = workGroupTitleOrder;
    }

    public DataModel<Role> getRoleSelectionGrid() {
        return roleSelectionGrid;
    }

    public void setRoleSelectionGrid(DataModel<Role> roleSelectionGrid) {
        this.roleSelectionGrid = roleSelectionGrid;
    }

    public WorkGroup getSelectedWorkGroup() {
        return selectedWorkGroup;
    }

    public void setSelectedWorkGroup(WorkGroup selectedWorkGroup) {
        this.selectedWorkGroup = selectedWorkGroup;
    }

    public SortOrder getTitleOrder() {
        return titleOrder;
    }

    public void setTitleOrder(SortOrder titleOrder) {
        this.titleOrder = titleOrder;
    }

    public boolean isWorkGroupEnabled() {
        return workGroupEnabled;
    }

    public void setWorkGroupEnabled(boolean workGroupEnabled) {
        this.workGroupEnabled = workGroupEnabled;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        currentWorkGroup = null;
        setSelectRow(false);
        this.page = page;
    }

    public Set<WorkGroup> getSelectWorkGroups() {
        return selectWorkGroups;
    }

    public void setSelectWorkGroups(Set<WorkGroup> selectWorkGroups) {
        this.selectWorkGroups = selectWorkGroups;
    }

    public SortOrder getWorkGroupDescriptionOrder() {
        return workGroupDescriptionOrder;
    }

    public void setWorkGroupDescriptionOrder(SortOrder workGroupDescriptionOrder) {
        this.workGroupDescriptionOrder = workGroupDescriptionOrder;
    }

    public String getWorkGroupDescriptionFilter() {
        return workGroupDescriptionFilter;
    }

    public void setWorkGroupDescriptionFilter(String workGroupDescriptionFilter) {
        this.workGroupDescriptionFilter = workGroupDescriptionFilter;
    }

    public List<WorkGroup> getWorkGroups() {
        return workGroups;
    }

    public void setWorkGroups(List<WorkGroup> workGroups) {
        this.workGroups = workGroups;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setWorkGroupTitleFilter(String workGroupTitleFilter) {
        this.workGroupTitleFilter = workGroupTitleFilter;
    }

    public List<Role> getLastSelected() {
        return lastSelected;
    }

    public void setLastSelected(List<Role> lastSelected) {
        this.lastSelected = lastSelected;
    }

    public void setTitleFilter(String titleFilter) {
        this.titleFilter = titleFilter;
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

    public String getWorkGroupTitleFilter() {
        return workGroupTitleFilter;
    }

    public String getTitleFilter() {
        return titleFilter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}