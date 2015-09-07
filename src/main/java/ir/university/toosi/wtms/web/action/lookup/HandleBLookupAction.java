package ir.university.toosi.wtms.web.action.lookup;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.Lookup;
import ir.university.toosi.wtms.web.model.entity.WebServiceInfo;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.richfaces.component.SortOrder;
import org.richfaces.model.Filter;

import javax.enterprise.context.SessionScoped;
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

@Named(value = "handleBLookupAction")
@SessionScoped
public class HandleBLookupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleLookupAction handleLookupAction;
    private DataModel<BLookup> bLookupList = null;
    private String editable = "false";
    private String bLookupTitle;
    private BLookup currentBLookup = null;
    private Lookup currentLookup = null;
    private String currentPage;
    private int page = 1;
    private SortOrder bLookupDescriptionOrder = SortOrder.unsorted;
    private String bLookupDescriptionFilter;
    private boolean selected;
    private Set<BLookup> selectedBLookups = new HashSet<>();

    public String begin() {

        refresh();
        return "list-bLookup";
    }


    public DataModel<BLookup> getSelectionGrid() {
        List<BLookup> bLookups = new ArrayList<>();
        refresh();
        return bLookupList;
    }

    private void refresh() {
        init();
        WebServiceInfo bLookupService = new WebServiceInfo();
        bLookupService.setServiceName("/getByLookupId");
        try {
            List<BLookup> bLookups = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), String.valueOf(handleLookupAction.getCurrentLookup().getId())), new TypeReference<List<BLookup>>() {
            });
            for (BLookup bLookup : bLookups) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
            bLookupList = new ListDataModel<>(bLookups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentBLookup = bLookupList.getRowData();
        currentBLookup.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteBLookup");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentBLookup)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/bLookup/list-blookup.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        bLookupTitle = "";
        page = 1;
        bLookupDescriptionFilter="";
    }

    public void edit() {
        setEditable("true");
        currentBLookup = bLookupList.getRowData();
//        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getByLookupId");
//        try {
//            currentBLookup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentBLookup.getId())), BLookup.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        bLookupTitle=me.getValue(currentBLookup.getCode());
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editBLookup");
        currentBLookup.setTitleText(bLookupTitle);
        currentBLookup.setEffectorUser(me.getUsername());
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentBLookup)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/lookup/list-blookup.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doAdd() {
        BLookup newBLookup = new BLookup();
        newBLookup.setTitleText(bLookupTitle);
        newBLookup.setDeleted("0");
        newBLookup.setStatus("c");
        newBLookup.setEffectorUser(me.getUsername());
        newBLookup.setCurrentLang(me.getLanguages());
        newBLookup.setLookup(handleLookupAction.getCurrentLookup());


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createBLookup");
        BLookup insertedBLookup = null;
        try {
            insertedBLookup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newBLookup)), BLookup.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedBLookup != null) {
            try {
                me.setLanguage();
            } catch (IOException e) {
                e.printStackTrace();
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/lookup/list-blookup.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }


    public void sortByBLookupDescription() {
        if (bLookupDescriptionOrder.equals(SortOrder.ascending)) {
            setBLookupDescriptionOrder(SortOrder.descending);
        } else {
            setBLookupDescriptionOrder(SortOrder.ascending);
        }
    }

    public Filter<?> getBLookupDescriptionFilterImpl() {
        return new Filter<BLookup>() {
            public boolean accept(BLookup bLookup) {
                return bLookupDescriptionFilter == null || bLookupDescriptionFilter.length() == 0 || bLookup.getTitleText().toLowerCase().contains(bLookupDescriptionFilter.toLowerCase());
            }
        };
    }

    public DataModel<BLookup> getBLookupList() {
        return bLookupList;
    }

    public void setBLookupList(DataModel<BLookup> bLookupList) {
        this.bLookupList = bLookupList;
    }

    public String getBLookupDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setBLookupDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }

    public SortOrder getBLookupDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setBLookupDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getBLookupName() {
        return bLookupTitle;
    }

    public void setBLookupName(String bLookupName) {
        this.bLookupTitle = bLookupName;
    }


    public Set<BLookup> getSelectedBLookups() {
        return selectedBLookups;
    }

    public void setSelectedBLookups(Set<BLookup> selectedBLookups) {
        this.selectedBLookups = selectedBLookups;
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

    public DataModel<BLookup> getPcList() {
        return bLookupList;
    }

    public void setPcList(DataModel<BLookup> bLookupList) {
        this.bLookupList = bLookupList;
    }

    public String getPcName() {
        return bLookupTitle;
    }

    public void setPcName(String bLookupName) {
        this.bLookupTitle = bLookupName;
    }


    public BLookup getCurrentBLookup() {
        return currentBLookup;
    }

    public void setCurrentBLookup(BLookup currentBLookup) {
        this.currentBLookup = currentBLookup;
    }

    public SortOrder getPcDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setPcDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public DataModel<BLookup> getbLookupList() {
        return bLookupList;
    }

    public void setbLookupList(DataModel<BLookup> bLookupList) {
        this.bLookupList = bLookupList;
    }

    public String getbLookupTitle() {
        return bLookupTitle;
    }

    public void setbLookupTitle(String bLookupTitle) {
        this.bLookupTitle = bLookupTitle;
    }

    public Lookup getCurrentLookup() {
        return currentLookup;
    }

    public void setCurrentLookup(Lookup currentLookup) {
        refresh();
        this.currentLookup = currentLookup;
    }

    public SortOrder getbLookupDescriptionOrder() {
        return bLookupDescriptionOrder;
    }

    public void setbLookupDescriptionOrder(SortOrder bLookupDescriptionOrder) {
        this.bLookupDescriptionOrder = bLookupDescriptionOrder;
    }

    public String getbLookupDescriptionFilter() {
        return bLookupDescriptionFilter;
    }

    public void setbLookupDescriptionFilter(String bLookupDescriptionFilter) {
        this.bLookupDescriptionFilter = bLookupDescriptionFilter;
    }
}