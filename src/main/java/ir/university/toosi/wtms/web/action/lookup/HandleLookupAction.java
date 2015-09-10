package ir.university.toosi.wtms.web.action.lookup;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;


import javax.enterprise.context.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleLookupAction")
@SessionScoped
public class HandleLookupAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleBLookupAction handleBLookupAction;
    private DataModel<Lookup> lookupList = null;
    private String editable = "false";
    private String lookupTitle;
    private Lookup currentLookup = null;
    private String currentPage;
    private int page = 1;
    private SortOrder lookupDescriptionOrder = SortOrder.UNSORTED;
    private String lookupDescriptionFilter;
    private boolean selected;
    private Set<Lookup> selectedLookups = new HashSet<>();
    private boolean selectRow = false;
    private boolean definable = false;

    public String begin() {
//        me.setActiveMenu(MenuType.SETTING);
        refresh();
        return "list-lookup";
    }


    private void refresh() {
        init();
        WebServiceInfo lookupService = new WebServiceInfo();
        lookupService.setServiceName("/getAllLookup");
        try {
            List<Lookup> lookups = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(lookupService.getServerUrl(), lookupService.getServiceName()), new TypeReference<List<Lookup>>() {
            });
            for (Lookup lookup : lookups) {
//                lookup.setTitleText(me.getValue(lookup.getTitle()));
                lookup.setTitleText(lookup.getTitle());
            }
            lookupList = new ListDataModel<>(lookups);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        setEditable("false");

    }

    public void doDelete() {
        currentLookup.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteLookup");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentLookup)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/lookup/list-lookup.htm");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        lookupTitle = "";
        lookupDescriptionFilter="";
        page = 1;
        currentLookup = null;
        setSelectRow(false);
        setDefinable(false);
    }

    public String edit() {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findLookupById");
        try {
            currentLookup = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentLookup.getId())), Lookup.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        handleBLookupAction.setCurrentLookup(currentLookup);

        return "list-blookup";
    }

    public void saveOrUpdate() {
        doEdit();
    }

    private void doEdit() {
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editLookup");
        currentLookup.setEffectorUser(me.getUsername());
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentLookup)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/lookup/list-lookup.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void sortByLookupDescription() {
        if (lookupDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setLookupDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setLookupDescriptionOrder(SortOrder.ASCENDING);
        }
    }

   /* public Filter<?> getLookupDescriptionFilterImpl() {
        return new Filter<Lookup>() {
            public boolean accept(Lookup lookup) {
                return lookupDescriptionFilter == null || lookupDescriptionFilter.length() == 0 || lookup.getTitle().toLowerCase().contains(lookupDescriptionFilter.toLowerCase());
            }
        };
    }*/

    public void selectForEdit() {
        currentLookup = lookupList.getRowData();
        setSelectRow(true);
        setDefinable(currentLookup.isDefinable());
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<Lookup> getLookupList() {
        return lookupList;
    }

    public void setLookupList(DataModel<Lookup> lookupList) {
        this.lookupList = lookupList;
    }

    public String getLookupDescriptionFilter() {
        return lookupDescriptionFilter;
    }

    public void setLookupDescriptionFilter(String lookupDescriptionFilter) {
        this.lookupDescriptionFilter = lookupDescriptionFilter;
    }

    public SortOrder getLookupDescriptionOrder() {
        return lookupDescriptionOrder;
    }

    public void setLookupDescriptionOrder(SortOrder lookupDescriptionOrder) {
        this.lookupDescriptionOrder = lookupDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getLookupTitle() {
        return lookupTitle;
    }

    public void setLookupTitle(String lookupTitle) {
        this.lookupTitle = lookupTitle;
    }

    public Set<Lookup> getSelectedLookups() {
        return selectedLookups;
    }

    public void setSelectedLookups(Set<Lookup> selectedLookups) {
        this.selectedLookups = selectedLookups;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentLookup = null;
        setSelectRow(false);
        setDefinable(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<Lookup> getPcList() {
        return lookupList;
    }

    public void setPcList(DataModel<Lookup> lookupList) {
        this.lookupList = lookupList;
    }

    public String getPcName() {
        return lookupTitle;
    }

    public void setPcName(String lookupName) {
        this.lookupTitle = lookupName;
    }

    public Lookup getCurrentLookup() {
        return currentLookup;
    }

    public void setCurrentLookup(Lookup currentLookup) {
        this.currentLookup = currentLookup;
    }

    public SortOrder getPcDescriptionOrder() {
        return lookupDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder lookupDescriptionOrder) {
        this.lookupDescriptionOrder = lookupDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return lookupDescriptionFilter;
    }

    public void setPcDescriptionFilter(String lookupDescriptionFilter) {
        this.lookupDescriptionFilter = lookupDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDefinable() {
        return definable;
    }

    public void setDefinable(boolean definable) {
        this.definable = definable;
    }
}