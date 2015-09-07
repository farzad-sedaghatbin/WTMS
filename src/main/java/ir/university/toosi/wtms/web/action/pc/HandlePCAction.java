package ir.university.toosi.wtms.web.action.pc;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.role.HandleRoleAction;
import ir.university.toosi.wtms.web.model.entity.*;
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

@Named(value = "handlePCAction")
@SessionScoped
public class HandlePCAction implements Serializable {

    @Inject
    private UserManagementAction me;
    @Inject
    private HandleRoleAction handleRoleAction;
    private DataModel<PC> pcList = null;
    private String editable = "false";
    private String pcName;
    private boolean pcEnabled;
    private String pcIP;
    private BLookup pcLocation;
    private PC currentPC = null;
    private String currentPage;
    private int page = 1;
    private String pcDescriptionFilter;
    private String pcIPFilter;
    private boolean selected;
    private Set<PC> selectedPCs = new HashSet<>();
    private List<BLookup> locations;
    private boolean selectRow = false;
    private String pcNameFilter;
    private String pcLocationFilter;
    private SortOrder pcIpOrder = SortOrder.unsorted;
    private SortOrder pcNameOrder = SortOrder.unsorted;
    private SortOrder pcLocationOrder = SortOrder.unsorted;
    private SortOrder pcDescriptionOrder = SortOrder.unsorted;


    public String begin() {
        me.setActiveMenu(MenuType.USER);
        refresh();
        return "list-pc";
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

    public void changePCs(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            pcEnabled = true;
        } else
            pcEnabled = false;
    }

    public DataModel<PC> getSelectionGrid() {
        List<PC> pcs = new ArrayList<>();
        refresh();
        return pcList;
    }

    private void refresh() {
        init();
        WebServiceInfo pcService = new WebServiceInfo();
        pcService.setServiceName("/getAllPC");
        try {
            List<PC> pcs = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(pcService.getServerUrl(), pcService.getServiceName()), new TypeReference<List<PC>>() {
            });
            for (PC pc : pcs) {
                pc.getLocation().setTitleText(me.getValue(pc.getLocation().getCode()));
            }
            pcList = new ListDataModel<>(pcs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        init();
        setEditable("false");

    }


    public void doDelete() {

        currentPC.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deletePC");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPC)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/pc/list-pc.htm");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        pcName = "";

        pcEnabled = true;
        pcIP = "";
        page = 1;
        currentPC = null;
        pcDescriptionFilter = "";
        pcIPFilter = "";
        pcNameFilter = "";
        pcLocationFilter = "";
        setSelectRow(false);
    }

    public void edit() {
        setEditable("true");
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPCById");
        try {
            currentPC = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentPC.getId())), PC.class);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        pcName = currentPC.getName();
        pcIP = currentPC.getIp();
        pcLocation = currentPC.getLocation();
    }

    public void saveOrUpdate() {
        if (editable.equalsIgnoreCase("false")) {
            doAdd();
        } else {
            doEdit();
        }
    }

    private void doEdit() {
        currentPC.setIp(pcIP);
        currentPC.setName(pcName);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/existPC");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPC)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("pc.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        currentPC.setEffectorUser(me.getUsername());
        try {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editPC");

            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentPC)), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/pc/list-pc.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void doAdd() {
        PC newPC = new PC();
        newPC.setName(pcName);
        newPC.setIp(pcIP);
        newPC.setDeleted("0");
        newPC.setStatus("c");
        newPC.setEffectorUser(me.getUsername());
        newPC.setLocation(pcLocation);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/PCexistNotId");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newPC)), String.class);
            if (condition.equalsIgnoreCase("true")) {

                me.addInfoMessage("pc.exist");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/createPC");
        PC insertedPC = null;
        try {
            insertedPC = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(newPC)), PC.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedPC != null) {
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/pc/list-pc.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }

    }

    public Filter<?> getPCIPFilterImpl() {
        return new Filter<PC>() {
            public boolean accept(PC pc) {
                return pcIPFilter == null || pcIPFilter.length() == 0 || pc.getIp().startsWith(pcIPFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPcNameFilterImpl() {
        return new Filter<PC>() {
            public boolean accept(PC pc) {
                return pcNameFilter == null || pcNameFilter.length() == 0 || pc.getName().toLowerCase().contains(pcNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPcLocationFilterImpl() {
        return new Filter<PC>() {
            public boolean accept(PC pc) {
                return pcLocationFilter == null || pcLocationFilter.length() == 0 || pc.getLocation().getTitleText().toLowerCase().contains(pcLocationFilter.toLowerCase());
            }
        };
    }

    public void sortByPcIp() {
        pcIpOrder = newSortOrder(pcIpOrder);
    }

    public void sortByPcName() {
        pcNameOrder = newSortOrder(pcNameOrder);
    }

    public void sortByPcLocation() {
        pcLocationOrder = newSortOrder(pcLocationOrder);
    }

    public void sortByPCDescription() {
        pcDescriptionOrder = newSortOrder(pcDescriptionOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        pcIpOrder = SortOrder.unsorted;
        pcNameOrder = SortOrder.unsorted;
        pcLocationOrder = SortOrder.unsorted;
        pcDescriptionOrder = SortOrder.unsorted;

        if (currentSortOrder.equals(SortOrder.descending)) {
            return SortOrder.ascending;
        } else {
            return SortOrder.descending;
        }
    }

    public void selectForEdit() {
        currentPC = pcList.getRowData();
        setSelectRow(true);
    }

    public void selectLocation(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getLocations()) {
            if (selectedId.equals(bLookup.getId())) {
                pcLocation = bLookup;
            }
        }
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public DataModel<PC> getPCList() {
        return pcList;
    }

    public void setPCList(DataModel<PC> pcList) {
        this.pcList = pcList;
    }

    public String getPCDescriptionFilter() {
        return pcDescriptionFilter;
    }

    public void setPCDescriptionFilter(String pcDescriptionFilter) {
        this.pcDescriptionFilter = pcDescriptionFilter;
    }

    public SortOrder getPCDescriptionOrder() {
        return pcDescriptionOrder;
    }

    public void setPCDescriptionOrder(SortOrder pcDescriptionOrder) {
        this.pcDescriptionOrder = pcDescriptionOrder;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getPCName() {
        return pcName;
    }

    public void setPCName(String pcName) {
        this.pcName = pcName;
    }

    public boolean isPCEnabled() {
        return pcEnabled;
    }

    public void setPCEnabled(boolean pcEnabled) {
        this.pcEnabled = pcEnabled;
    }

    public String getPcIP() {
        return pcIP;
    }

    public void setPcIP(String pcIP) {
        this.pcIP = pcIP;
    }

    public Set<PC> getSelectedPCs() {
        return selectedPCs;
    }

    public void setSelectedPCs(Set<PC> selectedPCs) {
        this.selectedPCs = selectedPCs;
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        currentPC = null;
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public DataModel<PC> getPcList() {
        return pcList;
    }

    public void setPcList(DataModel<PC> pcList) {
        this.pcList = pcList;
    }

    public String getPcName() {
        return pcName;
    }

    public void setPcName(String pcName) {
        this.pcName = pcName;
    }

    public boolean isPcEnabled() {
        return pcEnabled;
    }

    public void setPcEnabled(boolean pcEnabled) {
        this.pcEnabled = pcEnabled;
    }

    public PC getCurrentPC() {
        return currentPC;
    }

    public void setCurrentPC(PC currentPC) {
        this.currentPC = currentPC;
    }

    public SortOrder getPcDescriptionOrder() {
        return pcDescriptionOrder;
    }

    public void setPcDescriptionOrder(SortOrder pcDescriptionOrder) {
        this.pcDescriptionOrder = pcDescriptionOrder;
    }

    public String getPcDescriptionFilter() {
        return pcDescriptionFilter;
    }

    public void setPcDescriptionFilter(String pcDescriptionFilter) {
        this.pcDescriptionFilter = pcDescriptionFilter;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public SortOrder getPcNameOrder() {
        return pcNameOrder;
    }

    public void setPcNameOrder(SortOrder pcNameOrder) {
        this.pcNameOrder = pcNameOrder;
    }

    public String getPcNameFilter() {
        return pcNameFilter;
    }

    public void setPcNameFilter(String pcNameFilter) {
        this.pcNameFilter = pcNameFilter;
    }

    public SortOrder getPcLocationOrder() {
        return pcLocationOrder;
    }

    public void setPcLocationOrder(SortOrder pcLocationOrder) {
        this.pcLocationOrder = pcLocationOrder;
    }

    public String getPcLocationFilter() {
        return pcLocationFilter;
    }

    public void setPcLocationFilter(String pcLocationFilter) {
        this.pcLocationFilter = pcLocationFilter;
    }

    public List<BLookup> getLocations() {
        if (locations == null || locations.size() == 0) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                locations = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.PC_LOCATION_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : locations) {
                bLookup.setTitleText(me.getValue(bLookup.getCode()));
            }
        }

        return locations;
    }

    public void setLocations(List<BLookup> locations) {
        this.locations = locations;
    }

    public BLookup getPcLocation() {
        if (pcLocation == null) {
            if (getLocations().size() > 0) {
                pcLocation = getLocations().get(0);
            }
        }
        return pcLocation;
    }

    public void setPcLocation(BLookup pcLocation) {
        this.pcLocation = pcLocation;
    }

    public SortOrder getPcIpOrder() {
        return pcIpOrder;
    }

    public void setPcIpOrder(SortOrder pcIpOrder) {
        this.pcIpOrder = pcIpOrder;
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

    public String getPcIPFilter() {
        return pcIPFilter;
    }

    public void setPcIPFilter(String pcIPFilter) {
        this.pcIPFilter = pcIPFilter;
    }
}