package ir.university.toosi.wtms.web.action.operation;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.Operation;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.DualListModel;
import org.primefaces.model.SortOrder;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
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

@Named(value = "handleOperationAction")
@SessionScoped
public class HandleOperationAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    private DataModel<Operation> operationList = null;
    private String editable = "false";
    private String operationName;
    private boolean operationEnabled;
    private String description;
    private Operation currentOperation = null;
    private String currentPage;
    private int page = 1;
    private SortOrder operationDescriptionOrder = SortOrder.UNSORTED;
    private String operationDescriptionFilter;
    private boolean selected;
    private boolean selectAll;
    private Set<Operation> selectedOperations = new HashSet<>();
    private DualListModel<Operation> operations;

    public String begin() {
        refresh();
        return "list-operation";
    }

    public void selectOperations(ValueChangeEvent event) {
        currentOperation = operationList.getRowData();
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            currentOperation.setSelected(true);
            selectedOperations.add(currentOperation);
        } else {
            currentOperation.setSelected(false);
            selectedOperations.remove(currentOperation);
        }
    }

    public void changeOperations(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            operationEnabled = true;
        } else
            operationEnabled = false;
    }

    public void selectAllOperation(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            for (Operation operation : operationList) {
                operation.setSelected(true);
                selectedOperations.add(operation);
            }
        } else {
            for (Operation operation : operationList) {
                operation.setSelected(false);
            }
            selectedOperations.clear();
        }
    }

    public DataModel<Operation> getSelectionGrid() {
        List<Operation> operations = new ArrayList<>();
        refresh();
        return operationList;
    }

    public void refresh() {
        page = 1;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllOperation");
        try {
            List<Operation> operations = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Operation>>() {

            });
            for (Operation operation : operations) {
                operation.setSelected(false);
                operation.setDescription(me.getValue(operation.getDescription()));
            }
            operationList = new ListDataModel<>(operations);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentOperation = operationList.getRowData();

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteOperation");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOperation)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/operation/list-operation.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        operationName = "";
        operationEnabled = true;
        description = "";
    }

    public void edit() {
        setEditable("true");
        currentOperation = operationList.getRowData();
        operationEnabled = currentOperation.isEnabled();
        description = currentOperation.getDescription();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentOperation.setEnabled(operationEnabled);
        currentOperation.setDescription(description);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editOperation");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentOperation)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/operation/list-operation.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void backUp() {
        String db = Configuration.getProperty("db");
        if (db.equalsIgnoreCase("derby")) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/backUpDerby");
        }
        new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), "derby");

        me.addInfoMessage("operation.occurred");
    }

    private void doAdd() {
        Operation newOperation = new Operation();
        newOperation.setDescription(description);
        newOperation.setEnabled(operationEnabled);
        newOperation.setDeleted("0");
        newOperation.setStatus("c");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existOperation");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOperation)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createOperation");
        Operation insertedOperation = null;
        try {
            insertedOperation = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newOperation)), Operation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedOperation != null) {
//            me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageManagement");
//            LanguageManagement languageManagement = new LanguageManagement();
//            languageManagement.setTitle(newOperation.getDescription());
//            languageManagement.setType(me.getLanguages());
//            try {
//                languageManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageManagement)), LanguageManagement.class);
//
//                me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguageKeyManagement");
//                LanguageKeyManagement languageKeyManagement = new LanguageKeyManagement();
//                languageKeyManagement.setDescriptionKey(newOperation.getName());
//                Set list = new HashSet();
//                list.add(languageManagement);
//                languageKeyManagement.setLanguageManagements(list);
//
//                languageKeyManagement = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(languageKeyManagement)), LanguageKeyManagement.class);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/operation/list-operation.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }


    public void sortByOperationDescription() {
        if (operationDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setOperationDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setOperationDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public DataModel<Operation> getOperationList() {
        return operationList;
    }

    public void setOperationList(DataModel<Operation> operationList) {
        this.operationList = operationList;
    }

    public String getOperationDescriptionFilter() {
        return operationDescriptionFilter;
    }

    public void setOperationDescriptionFilter(String operationDescriptionFilter) {
        this.operationDescriptionFilter = operationDescriptionFilter;
    }

    public SortOrder getOperationDescriptionOrder() {
        return operationDescriptionOrder;
    }

    public void setOperationDescriptionOrder(SortOrder operationDescriptionOrder) {
        this.operationDescriptionOrder = operationDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public boolean isOperationEnabled() {
        return operationEnabled;
    }

    public void setOperationEnabled(boolean operationEnabled) {
        this.operationEnabled = operationEnabled;
    }

    public Set<Operation> getSelectedOperations() {
        return selectedOperations;
    }

    public void setSelectedOperations(Set<Operation> selectedOperations) {
        this.selectedOperations = selectedOperations;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
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

    public HandleRoleAction getHandleRoleAction() {
        return handleRoleAction;
    }

    public void setHandleRoleAction(HandleRoleAction handleRoleAction) {
        this.handleRoleAction = handleRoleAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Operation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(Operation currentOperation) {
        this.currentOperation = currentOperation;
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

    public void setOperations(DualListModel<Operation> operations) {
        this.operations = operations;
    }

    public Object findForConverter(String s) {

        return null;
    }
}