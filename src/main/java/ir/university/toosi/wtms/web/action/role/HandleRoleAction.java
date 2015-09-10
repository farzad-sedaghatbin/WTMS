package ir.university.toosi.wtms.web.action.role;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.operation.HandleOperationAction;
import ir.university.toosi.wtms.web.action.workgroup.HandleWorkGroupAction;
import ir.university.toosi.wtms.web.model.entity.MenuType;
import ir.university.toosi.wtms.web.model.entity.Permission;
import ir.university.toosi.wtms.web.model.entity.PermissionType;
import ir.university.toosi.wtms.web.model.entity.usermanagement.Operation;
import ir.university.toosi.wtms.web.model.entity.usermanagement.Role;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleRoleAction")
@SessionScoped
public class HandleRoleAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleOperationAction handleOperationAction;
    @Inject
    private HandleWorkGroupAction handleWorkGroupAction;
    private DataModel<Role> roleList = null;
    private DataModel<Permission> zoneList = null;
    private DataModel<Permission> gateList = null;
    private DataModel<Permission> pdpList = null;
    private DataModel<Permission> organList = null;
    private Set<Permission> selectedPermission = new HashSet<>();
    private Set<Permission> sPermision = new HashSet<>();
    private Permission currentPermission = null;
    private String editable = "false";
    private boolean roleEnabled;
    private String description;
    private String descText;
    private String name;
    private Role currentRole = null;
    private String currentPage;
    private int page = 1;
    private int zonePage = 1;
    private int gatePage = 1;
    private int pdpPage = 1;
    private int organPage = 1;
    private String roleDescriptionFilter;
    private boolean selected;
    private boolean selectAll = false;
    private Set<Role> selectedRoles = new HashSet<>();
    private boolean selectRow = false;
    private SortOrder roleDescriptionOrder = SortOrder.UNSORTED;

    public String begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        return "list-role";
    }

    public void selectRoles(ValueChangeEvent event) {
        currentRole = roleList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentRole.setSelected(true);
            selectedRoles.add(currentRole);
        } else {
            currentRole.setSelected(false);
            selectedRoles.remove(currentRole);
        }
    }

    public void changeRoles(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            roleEnabled = true;
        } else
            roleEnabled = false;
    }

    public void selectAllRole(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Role role : roleList) {
                role.setSelected(true);
                selectedRoles.add(role);
            }
        } else {
            for (Role role : roleList) {
                role.setSelected(false);
            }
            selectedRoles.clear();
        }
    }

    public DataModel<Role> getSelectionGrid() {
        List<Role> roles = new ArrayList<>();
        refresh();
        return roleList;
    }

    private void refresh() {
        init();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllRole");
        handleOperationAction.setSelectedOperations(new HashSet<Operation>());
        try {
            List<Role> roles = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Role>>() {
            });
            for (Role role : roles) {
                role.setDescText(me.getValue(role.getDescription()));
            }
            roleList = new ListDataModel<>(roles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        handleOperationAction.refresh();
        setEditable("false");

    }


    public void doDelete() {

        currentRole.setEffectorUser(me.getUsername());

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteRole");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRole)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/role/list-role.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        roleEnabled = true;
        selectAll = false;
        description = "";
        descText = "";
        page = 1;
        currentRole = null;
        setSelectRow(false);
        zonePage = 1;
        gatePage = 1;
        pdpPage = 1;
        organPage = 1;
        roleDescriptionFilter = "";
    }

    public void edit() {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findRoleById");
        try {
            currentRole = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentRole.getId())), Role.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        roleEnabled = currentRole.isEnabled();
        descText = me.getValue(currentRole.getDescription());
        name = currentRole.getName();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllOperation");
        List<Operation> operationList = null;
        try {
            operationList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Operation>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        handleOperationAction.setSelectedOperations(new HashSet<Operation>());
        for (Operation operation : operationList) {
            operation.setDescription(me.getValue(operation.getDescription()));
        }
        for (Operation currentOperation : currentRole.getOperations()) {
            for (Operation operation : operationList) {
                if ((currentOperation.getId() == operation.getId())) {
                    operation.setSelected(true);
                    handleOperationAction.getSelectedOperations().add(operation);
                }
            }
        }

        handleOperationAction.setOperationList(new ListDataModel<>(operationList));
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentRole.setEnabled(roleEnabled);
        currentRole.setDescText(descText);
        currentRole.setName(name);
        currentRole.setOperations(handleOperationAction.getSelectedOperations());
        currentRole.setEffectorUser(me.getUsername());
        currentRole.setCurrentLang(me.getLanguages());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editRole");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRole)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.setLanguage();
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/role/list-role.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doAdd() {
        Role newRole = new Role();
        newRole.setDescription(descText);
        newRole.setDescText(descText);
        newRole.setName(name);
        newRole.setEnabled(roleEnabled);
        newRole.setDeleted("0");
        newRole.setStatus("c");
        newRole.setEffectorUser(me.getUsername());
        newRole.setCurrentLang(me.getLanguages());

        Set<Operation> selectedOperations = new HashSet<>();
        for (Operation operation : handleOperationAction.getSelectedOperations()) {

            if (operation.isSelected()) {
                selectedOperations.add(operation);
            }
        }
       /* if (selectedOperations.size() == 0) {
            me.addErrorMessage("no_operation_selected");
            return;
        }*/

        newRole.setOperations(selectedOperations);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createRole");
        Role insertedRole = null;
        try {
            insertedRole = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newRole)), Role.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedRole != null) {

            try {
                me.setLanguage();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/role/list-role.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    public void assignPermission() {
        currentPermission = new Permission();
        sPermision.clear();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findByPermissionType");
        try {
            currentPermission.setPermissionType(PermissionType.ZONE);
            zoneList = new ListDataModel<>((List<Permission>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPermission)), new TypeReference<List<Permission>>() {
            }));
            currentPermission.setPermissionType(PermissionType.GATEWAY);
            gateList = new ListDataModel<>((List<Permission>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPermission)), new TypeReference<List<Permission>>() {
            }));
            currentPermission.setPermissionType(PermissionType.PDP);
            pdpList = new ListDataModel<>((List<Permission>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPermission)), new TypeReference<List<Permission>>() {
            }));
            currentPermission.setPermissionType(PermissionType.ORGAN);
            organList = new ListDataModel<>((List<Permission>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPermission)), new TypeReference<List<Permission>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Permission permissions : currentRole.getPermissions()) {
            switch (permissions.getPermissionType()) {
                case ORGAN:
                    for (Permission permission1 : organList) {
                        if (permission1.getId() == permissions.getId()) {
                            permission1.setSelected(true);
//                            sPermision.add(permission1);
                            break;
                        }
                    }
                    break;
                case ZONE:
                    for (Permission permission1 : zoneList) {
                        if (permission1.getId() == permissions.getId()) {
                            permission1.setSelected(true);
//                            sPermision.add(permission1);
                            break;
                        }
                    }
                    break;
                case PDP:
                    for (Permission permission1 : pdpList) {
                        if (permission1.getId() == permissions.getId()) {
                            permission1.setSelected(true);
//                            selectedPermission.add(permission1);
                            sPermision.add(permission1);
                            break;
                        }
                    }
                    break;
                case GATEWAY:
                    for (Permission permission1 : gateList) {
                        if (permission1.getId() == permissions.getId()) {
                            permission1.setSelected(true);
//                            sPermision.add(permission1);
                            break;
                        }
                    }
                    break;
                default:
                    break;
            }

        }
    }

    public void doAssignPermission() {
        currentRole.setPermissions(sPermision);
        currentRole.setCurrentLang(me.getLanguages());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editRole");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentRole)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/role/list-role.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortByRoleDescription() {
        if (roleDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setRoleDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setRoleDescriptionOrder(SortOrder.ASCENDING);
        }
    }

//    public Filter<?> getRoleDescriptionFilterImpl() {
//        return new Filter<Role>() {
//            public boolean accept(Role role) {
//                return roleDescriptionFilter == null || roleDescriptionFilter.length() == 0 || role.getDescText().toLowerCase().contains(roleDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void selectForEdit() {
        currentRole = roleList.getRowData();
        setSelectRow(true);

    }
//
//
//    public void selectAllZone(ValueChangeEvent event) {
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            for (Permission zone : zoneList) {
//                zone.setSelected(true);
//                sPermision.add(zone);
//            }
//        } else {
//            for (Permission zone : zoneList) {
//                zone.setSelected(false);
//                sPermision.remove(zone);
//            }
//        }
//    }
//
//    public void selectZone(ValueChangeEvent event) {
//        currentPermission = zoneList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            currentPermission.setSelected(true);
//            sPermision.add(currentPermission);
//        } else {
//            currentPermission.setSelected(false);
//            for (Permission permission : zoneList) {
//                if (permission.getId() == currentPermission.getId())
//                    sPermision.remove(permission);
//            }
//        }
//    }
//
//    public void selectAllOrgan(ValueChangeEvent event) {
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            for (Permission organ : organList) {
//                organ.setSelected(true);
//                sPermision.add(organ);
//            }
//        } else {
//            for (Permission organ : organList) {
//                organ.setSelected(false);
//                sPermision.remove(organ);
//            }
//        }
//    }
//
//    public void selectOrgan(ValueChangeEvent event) {
//        currentPermission = organList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            currentPermission.setSelected(true);
//            sPermision.add(currentPermission);
//        } else {
//            currentPermission.setSelected(false);
//            for (Permission permission : organList) {
//                if (permission.getId() == currentPermission.getId())
//                    sPermision.remove(permission);
//            }
//        }
//    }

//    public void selectAllPdp(ValueChangeEvent event) {
//        boolean temp = (Boolean) event.getNewValue();
//        sPermision.clear();
//        if (temp) {
//            for (Permission pdp : pdpList) {
//                pdp.setSelected(true);
//                sPermision.add(pdp);
//            }
//        } else {
//            for (Permission pdp : pdpList) {
//                pdp.setSelected(false);
//                sPermision.remove(pdp);
//            }
//        }
//    }

    public void selectPdp(ValueChangeEvent event) {
        currentPermission = pdpList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentPermission.setSelected(true);
            sPermision.add(currentPermission);
        } else {
            sPermision.remove(currentPermission);
            currentPermission.setSelected(false);

/*
            int i = 0;
            for (Permission permission : selectedPermission) {

                if (permission.getId() == currentPermission.getId()) {
                    break;
                }
                i++;
            }
            selectedPermission.remove(i);
*/
        }
    }

//    public void selectAllGate(ValueChangeEvent event) {
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            for (Permission gate : gateList) {
//                gate.setSelected(true);
//                selectedPermission.add(gate);
//            }
//        } else {
//            for (Permission gate : gateList) {
//                gate.setSelected(false);
//                selectedPermission.remove(gate);
//            }
//        }
//    }
//
//    public void selectGate(ValueChangeEvent event) {
//        Permission selectedGate = gateList.getRowData();
//        boolean temp = (Boolean) event.getNewValue();
//        if (temp) {
//            selectedGate.setSelected(true);
//            selectedPermission.add(selectedGate);
//        } else {
//            selectedGate.setSelected(false);
//            for (Permission permission : gateList) {
//                if (permission.getId() == selectedGate.getId())
//                    selectedPermission.remove(permission);
//            }
//        }
//    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(DataModel<Role> roleList) {
        this.roleList = roleList;
    }

    public String getRoleDescriptionFilter() {
        return roleDescriptionFilter;
    }

    public void setRoleDescriptionFilter(String roleDescriptionFilter) {
        this.roleDescriptionFilter = roleDescriptionFilter;
    }

    public SortOrder getRoleDescriptionOrder() {
        return roleDescriptionOrder;
    }

    public void setRoleDescriptionOrder(SortOrder roleDescriptionOrder) {
        this.roleDescriptionOrder = roleDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public boolean isRoleEnabled() {
        return roleEnabled;
    }

    public void setRoleEnabled(boolean roleEnabled) {
        this.roleEnabled = roleEnabled;
    }

    public Set<Role> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(Set<Role> selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentRole = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public HandleOperationAction getHandleOperationAction() {
        return handleOperationAction;
    }

    public void setHandleOperationAction(HandleOperationAction handleOperationAction) {
        this.handleOperationAction = handleOperationAction;
    }

    public HandleWorkGroupAction getHandleWorkGroupAction() {
        return handleWorkGroupAction;
    }

    public void setHandleWorkGroupAction(HandleWorkGroupAction handleWorkGroupAction) {
        this.handleWorkGroupAction = handleWorkGroupAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
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

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public DataModel<Permission> getZoneList() {
        return zoneList;
    }

    public void setZoneList(DataModel<Permission> zoneList) {
        this.zoneList = zoneList;
    }

    public DataModel<Permission> getGateList() {
        return gateList;
    }

    public void setGateList(DataModel<Permission> gateList) {
        this.gateList = gateList;
    }

    public DataModel<Permission> getPdpList() {
        return pdpList;
    }

    public void setPdpList(DataModel<Permission> pdpList) {
        this.pdpList = pdpList;
    }

    public DataModel<Permission> getOrganList() {
        return organList;
    }

    public void setOrganList(DataModel<Permission> organList) {
        this.organList = organList;
    }

    public Permission getCurrentPermission() {
        return currentPermission;
    }

    public void setCurrentPermission(Permission currentPermission) {
        this.currentPermission = currentPermission;
    }

    public int getZonePage() {
        return zonePage;
    }

    public void setZonePage(int zonePage) {
        this.zonePage = zonePage;
    }

    public int getGatePage() {
        return gatePage;
    }

    public void setGatePage(int gatePage) {
        this.gatePage = gatePage;
    }

    public int getPdpPage() {
        return pdpPage;
    }

    public void setPdpPage(int pdpPage) {
        this.pdpPage = pdpPage;
    }

    public int getOrganPage() {
        return organPage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrganPage(int organPage) {
        this.organPage = organPage;
    }
}