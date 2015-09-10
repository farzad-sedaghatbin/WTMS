package ir.university.toosi.wtms.web.action.language;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.*;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.primefaces.model.SortOrder;
//import org.richfaces.event.DataScrollEvent;
//

import javax.enterprise.context.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author : Hamed Hatami , Arsham Sedaghatbin, Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Named(value = "handleLanguageAction")
@SessionScoped
public class HandleLanguageAction implements Serializable {

    @Inject
    private UserManagementAction me;
    private String languageName;
    private boolean rtl;
    private boolean edit;
    private DataModel<EditLanguageModel> languageList = null;
    private List<EditLanguageModel> editLanguageModels = new ArrayList<>();
    private int page = 1;
    private List<LanguageKeyManagement> languageManagements;
    private String languageNameFilter;
    private String languageFilter;
    private String languageDescriptionFilter;
    private SortOrder languageNameOrder = SortOrder.UNSORTED;
    private SortOrder languageDescriptionOrder = SortOrder.UNSORTED;
    private DataModel<Languages> languageses = null;
    private Languages currentLanguage = null;
    private boolean selectRow = false;

    public void begin() {
        me.setActiveMenu(MenuType.SETTING);
        refresh();
    }

    private void refresh() {
        page = 1;
        editLanguageModels = new ArrayList<>();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllLanguage");
        try {
            languageses = new ListDataModel<>((List<Languages>) new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Languages>>() {
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add() {
        languageName = "";
        rtl = false;
    }

    public void doDelete() {

        currentLanguage.setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteLanguage");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(currentLanguage)), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/language/list-language.htm");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String editLang() {
        refreshEditLang();
        selectRow = false;
        return "edit-language";
    }

//    public void scrollListener(DataScrollEvent dataScrollEvent) {
//        if (edit) {
//            edit = false;
//            save();
//        }
//
//    }

    public void editInPlace(ValueChangeEvent event) {
        edit = true;
    }

    public void addLanguage() {
        Languages language = new Languages();
        language.setName(languageName);
        language.setEffectorUser(me.getUsername());
        try {
            language.setRtl(rtl);
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/createLanguage");
            new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(language));
            refreshEditLang();
            me.addInfoMessage("operation.occurred");
            me.redirect("/language/edit-language.htm");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    private void refreshEditLang() {
        page = 1;
        editLanguageModels = new ArrayList<>();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllLanguageKeyManagement");
        try {
            languageManagements = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<LanguageKeyManagement>>() {
            });
            EditLanguageModel editLanguageModel;
            for (LanguageKeyManagement languageManagement : languageManagements) {
                editLanguageModel = new EditLanguageModel();
                editLanguageModel.setKey(languageManagement.getDescriptionKey());
                for (LanguageManagement management : languageManagement.getLanguageManagements()) {

                    if (management.getType().getId() == currentLanguage
                            .getId()) {

                        editLanguageModel.setTitle(management.getTitle());
                        editLanguageModel.setEdited(false);
                        editLanguageModel.setType(management.getType().getName());
                    }
                }
                if (editLanguageModel.getTitle() == null)
                    continue;
                editLanguageModels.add(editLanguageModel);
            }
            languageList = new ListDataModel<>(editLanguageModels);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        boolean flag = false;
        Iterator iterator = languageList.iterator();
        while (iterator.hasNext()) {
            EditLanguageModel editLanguageModel = (EditLanguageModel) iterator.next();
            flag = false;
            if (editLanguageModel.isEdited()) {
                for (LanguageKeyManagement languageManagement : languageManagements) {
                    if (languageManagement.getDescriptionKey().equalsIgnoreCase(editLanguageModel.getKey()))
                        for (LanguageManagement management : languageManagement.getLanguageManagements()) {
                            if (management.getType().getId() == (me.getLanguages().getId())) {
                                management.setTitle(editLanguageModel.getTitle());
                                me.getGeneralHelper().getWebServiceInfo().setServiceName("/editLanguageManagement");
                                try {
                                    String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(management)), String.class);
                                    if (!condition.equalsIgnoreCase("true")) {
                                        me.addInfoMessage("operation.not.occurred");
                                        return;
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                flag = true;
                                break;
                            }
                        }
                    if (flag)
                        break;
                }
            }
        }
        try {
            me.setLanguage();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public Filter<?> getLanguageNameFilterImpl() {
//        return new Filter<EditLanguageModel>() {
//            public boolean accept(EditLanguageModel languages) {
//                return languageNameFilter == null || languageNameFilter.length() == 0 || languages.getKey().toLowerCase().contains(languageNameFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getLanguageFilterImpl() {
//        return new Filter<Languages>() {
//            public boolean accept(Languages languages) {
//                return languageFilter == null || languageFilter.length() == 0 || languages.getName().toLowerCase().contains(languageFilter.toLowerCase());
//            }
//        };
//    }
//
//    public Filter<?> getLanguageDescriptionFilterImpl() {
//        return new Filter<EditLanguageModel>() {
//            public boolean accept(EditLanguageModel languages) {
//                return languageDescriptionFilter == null || languageDescriptionFilter.length() == 0 || languages.getTitle().toLowerCase().contains(languageDescriptionFilter.toLowerCase());
//            }
//        };
//    }

    public void sortByLanguageName() {
        languageDescriptionOrder = SortOrder.UNSORTED;

        if (languageNameOrder.equals(SortOrder.ASCENDING)) {
            setLanguageNameOrder(SortOrder.DESCENDING);
        } else {
            setLanguageNameOrder(SortOrder.ASCENDING);
        }
    }

    public void sortByLanguageDescription() {
        languageNameOrder = SortOrder.UNSORTED;

        if (languageDescriptionOrder.equals(SortOrder.ASCENDING)) {
            setLanguageDescriptionOrder(SortOrder.DESCENDING);
        } else {
            setLanguageDescriptionOrder(SortOrder.ASCENDING);
        }
    }

    public void changeRtl(ValueChangeEvent event) {
        boolean temp = (Boolean) event.getNewValue();
        if (temp) {
            rtl = true;
        } else
            rtl = false;
    }

    public void selectForEdit() {
        currentLanguage = languageses.getRowData();
        selectRow = true;
    }

    public void resetPage() {
        setPage(1);
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<EditLanguageModel> getEditLanguageModels() {
        return editLanguageModels;
    }

    public void setEditLanguageModels(List<EditLanguageModel> editLanguageModels) {
        this.editLanguageModels = editLanguageModels;
    }

    public DataModel<EditLanguageModel> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(DataModel<EditLanguageModel> languageList) {
        this.languageList = languageList;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isRtl() {
        return rtl;
    }

    public void setRtl(boolean rtl) {
        this.rtl = rtl;
    }

    public int getPage() {
        return page;
    }

    public List<LanguageKeyManagement> getLanguageManagements() {
        return languageManagements;
    }

    public void setLanguageManagements(List<LanguageKeyManagement> languageManagements) {
        this.languageManagements = languageManagements;
    }

    public SortOrder getLanguageNameOrder() {
        return languageNameOrder;
    }

    public void setLanguageNameOrder(SortOrder languageNameOrder) {
        this.languageNameOrder = languageNameOrder;
    }

    public String getLanguageNameFilter() {
        return languageNameFilter;
    }

    public void setLanguageNameFilter(String languageNameFilter) {
        this.languageNameFilter = languageNameFilter;
    }

    public SortOrder getLanguageDescriptionOrder() {
        return languageDescriptionOrder;
    }

    public void setLanguageDescriptionOrder(SortOrder languageDescriptionOrder) {
        this.languageDescriptionOrder = languageDescriptionOrder;
    }

    public String getLanguageDescriptionFilter() {
        return languageDescriptionFilter;
    }

    public void setLanguageDescriptionFilter(String languageDescriptionFilter) {
        this.languageDescriptionFilter = languageDescriptionFilter;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public DataModel<Languages> getLanguageses() {
        return languageses;
    }

    public void setLanguageses(DataModel<Languages> languageses) {
        this.languageses = languageses;
    }

    public String getLanguageFilter() {
        return languageFilter;
    }

    public void setLanguageFilter(String languageFilter) {
        this.languageFilter = languageFilter;
    }

    public Languages getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Languages currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }
}