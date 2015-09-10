package ir.university.toosi.wtms.web.action.person;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.Lookup;
import ir.university.toosi.tms.model.entity.MenuType;
import ir.university.toosi.tms.model.entity.WebServiceInfo;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.util.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.SortOrder;


import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * @author : Mostafa Rastgar
 * @version : 0.8
 */
@Named(value = "handleCardAction")
@SessionScoped
public class HandleCardAction implements Serializable {

    @Inject
    private UserManagementAction me;

    private DataModel<Card> cardList;
    private String cardData;
    private Long personId;
    private Card card;
    private Card currentCard;
    private List<BLookup> cardTypes;
    private List<BLookup> cardStatuses;
    private BLookup cardType;
    private BLookup cardStatus;
    private String currentPage;
    private int page = 1;
    private String messageText;
    private boolean canSave;
    private boolean selectRow = false;
    private long selectedCard;
    private List<Card> selectedCards = new ArrayList<>();
    private List<Card> allCard = new ArrayList<>();
    private List<Card> notSelectedCard = new ArrayList<>();
    private List<Card> tempNotSelectedCard = new ArrayList<>();
    private Collection<Object> selection;
    private List<Person> personItems;
    private List<Person> selectionItems = new ArrayList<>();
    private boolean enablePoll;
    private Person currentPerson;
    private Long startTime;

    private SortOrder cardNameOrder = SortOrder.UNSORTED;
    private SortOrder cardCodeOrder = SortOrder.UNSORTED;
    private SortOrder cardOwnerNameOrder = SortOrder.UNSORTED;
    private SortOrder cardOwnerFamilyOrder = SortOrder.UNSORTED;
    private SortOrder cardPersonnelNoOrder = SortOrder.UNSORTED;
    private SortOrder startDateOrder = SortOrder.UNSORTED;
    private SortOrder expirationDateOrder = SortOrder.UNSORTED;

    private String cardNameFilter;
    private String cardTypeCodeFilter;
    private String cardStatusCodeFilter;
    private String cardCodeFilter;
    private String cardOwnerNameFilter;
    private String cardOwnerFamilyFilter;
    private String cardPersonnelNoFilter;
    private String startDateFilter;
    private String expirationDateFilter;


    public String getCardData() {
        if (cardData == null) {
            cardData = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("cardData");
            if (cardData != null) {
//            cardData = new EncryptUtil().decrypt(cardData);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/findCardByCode");
                try {
                    card = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(cardData)), Card.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (card != null) {
                    messageText = me.getValue("CARD_ALREADY_EXIST") + " " + card.getPerson().getName() + " " + card.getPerson().getLastName();
                    if (card.getCardStatus().getCode().equalsIgnoreCase(BLookup.CARD_STATUS_LOST)) {
                        canSave = false;
                        messageText = me.getValue("CARD_IS_LOST");
                    } else if (card.getCardStatus().getCode().equalsIgnoreCase(BLookup.CARD_STATUS_STOLEN)) {
                        canSave = false;
                        messageText = me.getValue("CARD_IS_STOLEN");
                    } else if (card.getCardStatus().getCode().equalsIgnoreCase(BLookup.CARD_STATUS_CLOSED)) {
                        canSave = false;
                        messageText = me.getValue("CARD_IS_CLOSED");
                    } else if (card.getCardStatus().getCode().equalsIgnoreCase(BLookup.CARD_IS_INVALID)) {
                        canSave = false;
                        messageText = me.getValue("CARD_IS_INVALID");
                    } else {
                        canSave = true;
                    }
                } else {
                    canSave = true;
                }
            } else {
                canSave = false;
                messageText = me.getValue("DEVICE_ERROR");
            }
        }
        return cardData;
    }

    public void setCardData(String cardData) {
        this.cardData = cardData;
    }

    public void changeFilter(ValueChangeEvent valueChangeEvent) {
        String newValue = String.valueOf(valueChangeEvent.getNewValue());
        List<Card> cards = new ArrayList<>();
        List<Card> selectedCard = new ArrayList<>();

        for (Card card1 : tempNotSelectedCard) {
            if (card1.getCode().toLowerCase().startsWith(newValue.toLowerCase())) {
                cards.add(card1);
            }
        }
        for (Card selectedCard1 : cards) {
            selectedCard1.getCardType().getCode().equalsIgnoreCase(BLookup.CARD_SPECIAL);
            selectedCard.add(selectedCard1);
        }
        notSelectedCard = cards;
        selectedCards = selectedCard;

    }

    public Long getPersonId() {
        if (personId == null) {
            String sPersonId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getParameter("personId");
            if (sPersonId != null && !sPersonId.isEmpty()) {
                personId = Long.parseLong(sPersonId);
            }
        }
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public void begin() {
//        me.setActiveMenu(MenuType.CARD);
        refresh();

        //return "list-card";
    }

    public String beginInvisible() {
//        me.setActiveMenu(MenuType.CARD);

        refreshInvis();
        return "list-invisible-card";
    }

    public String beginAssignCardToPersonList() {
//        me.setActiveMenu(MenuType.CARD);

        refreshAssignCardToPersonList();
        return "list-assign-card-to-person";
    }

    public void doDelete() {
        setCard(currentCard);
        getCard().setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteCard");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard())), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/card/list-card.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadJnlp() throws IOException {
        showNextPersonDetails();
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        String base = "http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("server.port") + ((HttpServletRequest) ec.getRequest()).getServletContext().getContextPath() + "/jnlp";
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<jnlp spec=\"1.0+\" codebase=\"" + base + "\" href=\"testAction\">\n" +
                "    <information>\n" +
                "        <title>Jnlp Testing</title>\n" +
                "        <vendor>Aria</vendor>\n" +
                "        <homepage href=\"" + base + "/\"/>\n" +
                "        <description>Testing Testing</description>\n" +
                "    </information>\n" +
                "    <security>\n" +
                "        <all-permissions/>\n" +
                "    </security>\n" +
                "    <resources>\n" +
                "        <j2se version=\"1.6+\"/>\n" +
                "        <jar href=\"TestJnlp.jar\"/>\n" +
                "    </resources>\n" +
                "    <application-desc main-class=\"ir.university.toosi.wtms.jnlp.CardReader\">\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getLocalAddr() + "</argument>\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getLocalPort() + "</argument>\n" +
                "        <argument>" + ((HttpServletRequest) ec.getRequest()).getServletContext().getContextPath() + "</argument>\n" +
                "        <argument>" + 1 + "</argument>\n" +
                "        <argument>" + Configuration.getProperty("com.port") + "</argument>\n" +
                "    </application-desc>\n" +
                "</jnlp>";
        byte[] xmlBytes = xml.getBytes("UTF-8");

        ec.responseReset();
        ec.setResponseContentType("application/x-java-jnlp-file");
        ec.setResponseContentLength(xmlBytes.length);
        ec.setResponseHeader("Content-Disposition", "attachment; filename=\"test.jnlp\"");

        OutputStream output = ec.getResponseOutputStream();
        output.write(xmlBytes);
        output.flush();
        fc.responseComplete();

    }

    public String action() throws IOException {
        return "multiple-card";
    }

    public String showNextPersonDetails() throws IOException {
        if (selection.iterator().hasNext()) {
            enablePoll = true;

            Object personId = personItems.get((Integer) selection.iterator().next()).getId();
            try {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPersonById");
                String serverUrl = me.getGeneralHelper().getWebServiceInfo().getServerUrl();
                String serviceName = me.getGeneralHelper().getWebServiceInfo().getServiceName();
                currentPerson = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(serverUrl, serviceName, String.valueOf(personId)), Person.class);
                selection.remove(selection.iterator().next());
            } catch (IOException e) {
                e.printStackTrace();
            }
            startProcess();
            return "";
        } else {
            enablePoll = false;
            selectionItems.clear();
            return "list-assign-card-to-person";
        }
    }

    public String startProcess() {
        setStartTime(new Date().getTime());
        return null;
    }

    public Long getCurrentValue() {
        if (isEnablePoll()) {
            Long current = (new Date().getTime() - startTime) / 1000;
            if (current >= 100) {
            } else if (current.equals(0L)) {
                return (long) 1;
            }
            return (new Date().getTime() - startTime) / 1000;
        }

        return startTime == null ? (long) -1 : (long) 100;
    }

    public String exportExcel() {
        List<String> titles = new ArrayList<>();
        titles.add(me.getValue("name"));
        titles.add(me.getValue("lastname"));
        titles.add(me.getValue("card_code"));
        titles.add(me.getValue("card_name"));
        titles.add(me.getValue("card_type"));
        titles.add(me.getValue("card_status"));
        titles.add(me.getValue("startDate"));
        titles.add(me.getValue("expirationDate"));
        titles.add(me.getValue("personnelCode"));
        Workbook currentWorkbook = new HSSFWorkbook();
        Sheet sheet = currentWorkbook.createSheet("");
        sheet.autoSizeColumn(0);

        int rowCounter = 0;
        Row row = sheet.createRow(rowCounter);
        int cellCounter = 0;
        int rowSize = titles.size();
        Cell cell;
        cell = row.createCell(cellCounter);
        for (String title : titles) {
            cell = row.createCell(cellCounter);
            cellCounter++;
            cell.setCellValue(title);
        }

        for (Card report : allCard) {
            rowCounter++;
            row = sheet.createRow(rowCounter);
            cellCounter = 0;
            cell = row.createCell(cellCounter);
            String s1 = report.getPerson().getName();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getPerson().getLastName();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getCode();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getName();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = me.getValue(report.getCardType().getTitle());
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = me.getValue(report.getCardStatus().getTitle());
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getStartDate();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getExpirationDate();
            cell.setCellValue(s1);
            cellCounter++;
            cell = row.createCell(cellCounter);
            s1 = report.getPerson().getPersonnelNo();
            cell.setCellValue(s1);

        }
        for (int i = 0; i < titles.size(); i++) {
            sheet.autoSizeColumn(i);
        }

        for (int i = titles.size(); i < (rowSize + titles.size()); i++) {
            sheet.autoSizeColumn(i);
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            currentWorkbook.write(byteArrayOutputStream);

            new ReportUtils<>().readyForDownload(byteArrayOutputStream.toByteArray(), "vnd.ms-excel", LangUtils.getEnglishNumber(CalendarUtil.getDateWithoutSlash(new Date(), new Locale("fa"), "yyyyMMdd")) + ".xls");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void doDeleteInvisible() {
        setCard(currentCard);
        getCard().setEffectorUser(me.getUsername());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/deleteCard");
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard())), String.class);
            refresh();
            me.addInfoMessage(condition);
            me.redirect("/card/list-invisible-card.htm");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void edit() {
        setCard(currentCard);
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findCardById");
        try {
            setCard(new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard().getId())), Card.class));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doEdit() {
        getCard().setCardType(getCardType());
        getCard().setCardStatus(getCardStatus());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editCard");
        getCard().setEffectorUser(me.getUsername());
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard())), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refresh();
                me.addInfoMessage("operation.occurred");
                me.redirect("/card/list-card.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void doEditInvisible() {
        getCard().setCardType(getCardType());
        getCard().setCardStatus(getCardStatus());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editCard");
        getCard().setEffectorUser(me.getUsername());
        try {
            String condition = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard())), String.class);
            if (condition.equalsIgnoreCase("true")) {
                refreshInvis();
                me.addInfoMessage("operation.occurred");
                me.redirect("/card/list-invisible-card.htm");
            } else {
                me.addInfoMessage("operation.not.occurred");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addNewCard() {
        getCard().setCode(getCardData().toUpperCase());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPersonById");
        Person person = null;
        try {
            person = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), getPersonId().toString()), Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }


        getCard().setPerson(person);
        getCard().setCardType(getCardType());
        getCard().setCardStatus(getCardStatus());
        getCard().setEffectorUser(me.getUsername());
        if (getCard().getId() > 0) {
            getCard().setDeleted("0");
            getCard().setStatus("c");
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editCard");
        } else {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/createCard");
        }
        Card insertedCard = null;
        try {
            insertedCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(getCard())), Card.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (insertedCard != null) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/editPerson");
            person.setHasCard(true);
            try {
                new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(person));
            } catch (IOException e) {
                e.printStackTrace();
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/card/list-card.htm");
        } else {
            me.addInfoMessage("operation.not.occurred");
        }
    }

    private void refresh() {
        setCard(null);
        setCardData(null);
        setPersonId(null);
        setCardType(null);
        setCardStatus(null);
        setCurrentCard(null);
        setSelectRow(false);
        cardNameFilter = "";
        cardTypeCodeFilter = "";
        cardStatusCodeFilter = "";
        cardCodeFilter = "";
        cardOwnerNameFilter = "";
        cardOwnerFamilyFilter = "";
        cardPersonnelNoFilter = "";
        startDateFilter = "";
        expirationDateFilter = "";
        setPage(1);
        WebServiceInfo cardService = new WebServiceInfo();
        try {
            notSelectedCard = new ArrayList<>();
            cardService.setServiceName("/getAllActiveCard");
            allCard = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(cardService.getServerUrl(), cardService.getServiceName()), new TypeReference<List<Card>>() {
            });
            for (Card card : allCard) {
                notSelectedCard.add(card);
                if (card.getCardType() != null)
                    card.getCardType().setTitleText(me.getValue(card.getCardType().getTitle()));
                card.getCardStatus().setTitleText(me.getValue(card.getCardStatus().getTitle()));
            }
            cardList = new ListDataModel<>(allCard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshInvis() {
        cardNameFilter = "";
        cardTypeCodeFilter = "";
        cardStatusCodeFilter = "";
        cardCodeFilter = "";
        cardOwnerNameFilter = "";
        cardOwnerFamilyFilter = "";
        cardPersonnelNoFilter = "";
        startDateFilter = "";
        expirationDateFilter = "";
        setCard(null);
        setCardData(null);
        setPersonId(null);
        setCardType(null);
        setCardStatus(null);
        setCurrentCard(null);
        setSelectRow(false);
        setPage(1);
        WebServiceInfo cardService = new WebServiceInfo();
        try {
            List<Card> cards;
            cardService.setServiceName("/getAllInvisibleCard");
            cards = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(cardService.getServerUrl(), cardService.getServiceName()), new TypeReference<List<Card>>() {
            });
            for (Card card : cards) {
                card.getCardType().setTitleText(me.getValue(card.getCardType().getTitle()));
                card.getCardStatus().setTitleText(me.getValue(card.getCardStatus().getTitle()));
            }
            cardList = new ListDataModel<>(cards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshAssignCardToPersonList() {
        setPage(1);
        personItems = null;
        selectionItems.clear();
        selection = null;
        cardOwnerFamilyFilter = "";
        cardOwnerNameFilter = "";
        cardPersonnelNoFilter = "";
        resetAllSortOrders();
        cardOwnerFamilyOrder = SortOrder.ASCENDING;
        enablePoll = false;

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPersonDataModel");
        try {
            String serverUrl = me.getGeneralHelper().getWebServiceInfo().getServerUrl();
            String serviceName = me.getGeneralHelper().getWebServiceInfo().getServiceName();
            String restFullService = new RESTfulClientUtil().restFullService(serverUrl, serviceName);
            personItems = new ObjectMapper().readValue(restFullService, new TypeReference<List<Person>>() {
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void selectForEdit() {
        setCurrentCard(getCardList().getRowData());
        setSelectRow(true);

    }

    public DataModel<Card> getCardList() {
        return cardList;
    }

    public void setCardList(DataModel<Card> cardList) {
        this.cardList = cardList;
    }

    public Card getCard() {
        if (card == null) {
            card = new Card();
        }
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public List<BLookup> getCardTypes() {
        if (cardTypes == null) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                cardTypes = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.CARD_TYPE_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : cardTypes) {
                bLookup.setTitleText(me.getValue(bLookup.getTitle()));
            }
        }
        return cardTypes;
    }

    public void setCardTypes(List<BLookup> cardTypes) {
        this.cardTypes = cardTypes;
    }

    public List<BLookup> getCardStatuses() {
        if (cardStatuses == null) {
            WebServiceInfo bLookupService = new WebServiceInfo();
            bLookupService.setServiceName("/getByLookupId");
            try {
                cardStatuses = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(bLookupService.getServerUrl(), bLookupService.getServiceName(), new ObjectMapper().writeValueAsString(Lookup.CARD_STATUS_ID)), new TypeReference<List<BLookup>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (BLookup bLookup : cardStatuses) {
                bLookup.setTitleText(me.getValue(bLookup.getTitle()));
            }
        }
        return cardStatuses;
    }

    public void setCardStatuses(List<BLookup> cardStatuses) {
        this.cardStatuses = cardStatuses;
    }

    public BLookup getCardType() {
        if (cardType == null) {
            cardType = getCardTypes().get(0);
        }
        return cardType;
    }

    public void setCardType(BLookup cardType) {
        this.cardType = cardType;
    }

    public BLookup getCardStatus() {
        if (cardStatus == null) {
            cardStatus = getCardStatuses().get(0);
        }
        return cardStatus;
    }

    public void setCardStatus(BLookup cardStatus) {
        this.cardStatus = cardStatus;
    }

    public void selectCardType(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getCardTypes()) {
            if (selectedId.equals(bLookup.getId())) {
                cardType = bLookup;
            }
        }
    }

    public void selectCardStatus(ValueChangeEvent event) {
        Long selectedId = (Long) event.getNewValue();
        for (BLookup bLookup : getCardStatuses()) {
            if (selectedId.equals(bLookup.getId())) {
                cardStatus = bLookup;
            }
        }
    }

    public void changedList(ValueChangeEvent event) {
        selectedCards = (List<Card>) event.getNewValue();

    }


    public void specializeCard() {
        tempNotSelectedCard = notSelectedCard;
        selectedCards.clear();
        for (Card card : notSelectedCard) {
            if (card.getCardType().getCode().equalsIgnoreCase(BLookup.CARD_SPECIAL)) {
                selectedCards.add(card);
            }
        }
    }

    public void doSpecializeCard() throws IOException {

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/editCard");
        getCard().setEffectorUser(me.getUsername());
        BLookup specialCard = null;
        BLookup normalCard = null;
        for (BLookup type : cardTypes) {
            if (type.getCode().equalsIgnoreCase(BLookup.CARD_SPECIAL))
                specialCard = type;
            if (type.getCode().equalsIgnoreCase(BLookup.CARD_NORMAL))
                normalCard = type;
        }

        try {
            for (Card card : notSelectedCard) {
                card.setCardType(normalCard);
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(card)), String.class);
            }
            for (Card card : selectedCards) {
                card.setCardType(specialCard);
                new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(card)), String.class);
            }
            refresh();
            me.addInfoMessage("operation.occurred");
            me.redirect("/card/list-card.htm");

        } catch (IOException e) {
            me.addInfoMessage("operation.not.occurred");
            e.printStackTrace();
        }
    }

/*    public void selectionListener(AjaxBehaviorEvent event) {
        UIExtendedDataTable dataTable = (UIExtendedDataTable) event.getComponent();
        Object originalKey = dataTable.getRowKey();
        selectionItems.clear();

        for (Object selectionKey : selection) {
            dataTable.setRowKey(selectionKey);
            if (dataTable.isRowAvailable()) {
                selectionItems.add((Person) dataTable.getRowData());
            }
        }
        dataTable.setRowKey(originalKey);
    }

    public Filter<?> getCardNameFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardNameFilter) || card.getName().toLowerCase().contains(cardNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getCardTypeCodeFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardTypeCodeFilter) || card.getCardType().getCode().toLowerCase().contains(cardTypeCodeFilter.toLowerCase());
            }
        };
    }*/

   /* public Filter<?> getCardStatusCodeFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardStatusCodeFilter) || card.getCardStatus().getCode().toLowerCase().contains(cardStatusCodeFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getCardCodeFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardCodeFilter) || card.getCode().toLowerCase().contains(cardCodeFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getCardOwnerNameFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardOwnerNameFilter) || card.getPerson().getName().toLowerCase().contains(cardOwnerNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getCardOwnerFamilyFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardOwnerFamilyFilter) || card.getPerson().getLastName().toLowerCase().contains(cardOwnerFamilyFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getCardPersonnelNoFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(cardPersonnelNoFilter) || card.getPerson().getPersonnelNo().toLowerCase().contains(cardPersonnelNoFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPersonNameFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return StringUtils.isEmpty(cardOwnerNameFilter) || person.getName().toLowerCase().contains(cardOwnerNameFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPersonFamilyFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return StringUtils.isEmpty(cardOwnerFamilyFilter) || person.getLastName().toLowerCase().contains(cardOwnerFamilyFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getPersonPersonnelNoFilterImpl() {
        return new Filter<Person>() {
            public boolean accept(Person person) {
                return StringUtils.isEmpty(cardPersonnelNoFilter) || person.getPersonnelNo().toLowerCase().contains(cardPersonnelNoFilter.toLowerCase());
            }
        };
    }

    public Filter<?> getStartDateFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(startDateFilter) ||
                        LangUtils.getEnglishNumber(card.getStartDate()).equals(LangUtils.getEnglishNumber(startDateFilter));
            }
        };
    }

    public Filter<?> getExpirationDateFilterImpl() {
        return new Filter<Card>() {
            public boolean accept(Card card) {
                return StringUtils.isEmpty(expirationDateFilter) ||
                        LangUtils.getEnglishNumber(card.getExpirationDate()).equals(LangUtils.getEnglishNumber(expirationDateFilter));
            }
        };
    }
*/
    public void sortByCardName() {
        cardNameOrder = newSortOrder(cardNameOrder);
    }

    public void sortByCardCode() {
        cardCodeOrder = newSortOrder(cardCodeOrder);
    }

    public void sortByCardOwnerName() {
        cardOwnerNameOrder = newSortOrder(cardOwnerNameOrder);
    }

    public void sortByCardOwnerFamily() {
        cardOwnerFamilyOrder = newSortOrder(cardOwnerFamilyOrder);
    }

    public void sortByCardPersonnelNo() {
        cardPersonnelNoOrder = newSortOrder(cardPersonnelNoOrder);
    }

    public void sortByStartDate() {
        startDateOrder = newSortOrder(startDateOrder);
    }

    public void sortByExpirationDate() {
        expirationDateOrder = newSortOrder(expirationDateOrder);
    }

    private SortOrder newSortOrder(SortOrder currentSortOrder) {
        resetAllSortOrders();

        if (currentSortOrder.equals(SortOrder.DESCENDING)) {
            return SortOrder.ASCENDING;
        } else {
            return SortOrder.DESCENDING;
        }

    }

    private void resetAllSortOrders() {
        cardNameOrder = SortOrder.UNSORTED;
        cardCodeOrder = SortOrder.UNSORTED;
        cardOwnerNameOrder = SortOrder.UNSORTED;
        cardOwnerFamilyOrder = SortOrder.UNSORTED;
        cardPersonnelNoOrder = SortOrder.UNSORTED;
        startDateOrder = SortOrder.UNSORTED;
        expirationDateOrder = SortOrder.UNSORTED;
    }

    public void resetPage() {
        setPage(1);
    }

    public String getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public int getPage() {
        setCurrentCard(null);
        setSelectRow(false);
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean isCanSave() {
        return canSave;
    }

    public void setCanSave(boolean canSave) {
        this.canSave = canSave;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card currentCard) {
        this.currentCard = currentCard;
    }

    public boolean isSelectRow() {
        return selectRow;
    }

    public void setSelectRow(boolean selectRow) {
        this.selectRow = selectRow;
    }

    public long getSelectedCard() {
        return selectedCard;
    }

    public void setSelectedCard(long selectedCard) {
        this.selectedCard = selectedCard;
    }

    public List<Card> getSelectedCards() {
        return selectedCards;
    }

    public void setSelectedCards(List<Card> selectedCards) {
        this.selectedCards = selectedCards;
    }

    public List<Card> getAllCard() {
        return allCard;
    }

    public void setAllCard(List<Card> allCard) {
        this.allCard = allCard;
    }

    public List<Card> getNotSelectedCard() {
        return notSelectedCard;
    }

    public void setNotSelectedCard(List<Card> notSelectedCard) {
        this.notSelectedCard = notSelectedCard;
    }

    public SortOrder getCardNameOrder() {
        return cardNameOrder;
    }

    public void setCardNameOrder(SortOrder cardNameOrder) {
        this.cardNameOrder = cardNameOrder;
    }

    public String getCardNameFilter() {
        return cardNameFilter;
    }

    public void setCardNameFilter(String cardNameFilter) {
        this.cardNameFilter = cardNameFilter;
    }

    public SortOrder getCardOwnerNameOrder() {
        return cardOwnerNameOrder;
    }

    public void setCardOwnerNameOrder(SortOrder cardOwnerNameOrder) {
        this.cardOwnerNameOrder = cardOwnerNameOrder;
    }

    public String getCardOwnerNameFilter() {
        return cardOwnerNameFilter;
    }

    public void setCardOwnerNameFilter(String cardOwnerNameFilter) {
        this.cardOwnerNameFilter = cardOwnerNameFilter;
    }

    public SortOrder getCardOwnerFamilyOrder() {
        return cardOwnerFamilyOrder;
    }

    public void setCardOwnerFamilyOrder(SortOrder cardOwnerFamilyOrder) {
        this.cardOwnerFamilyOrder = cardOwnerFamilyOrder;
    }

    public String getCardOwnerFamilyFilter() {
        return cardOwnerFamilyFilter;
    }

    public void setCardOwnerFamilyFilter(String cardOwnerFamilyFilter) {
        this.cardOwnerFamilyFilter = cardOwnerFamilyFilter;
    }

    public SortOrder getCardCodeOrder() {
        return cardCodeOrder;
    }

    public void setCardCodeOrder(SortOrder cardCodeOrder) {
        this.cardCodeOrder = cardCodeOrder;
    }

    public String getCardCodeFilter() {
        return cardCodeFilter;
    }

    public void setCardCodeFilter(String cardCodeFilter) {
        this.cardCodeFilter = cardCodeFilter;
    }

    public SortOrder getCardPersonnelNoOrder() {
        return cardPersonnelNoOrder;
    }

    public void setCardPersonnelNoOrder(SortOrder cardPersonnelNoOrder) {
        this.cardPersonnelNoOrder = cardPersonnelNoOrder;
    }

    public String getCardPersonnelNoFilter() {
        return cardPersonnelNoFilter;
    }

    public void setCardPersonnelNoFilter(String cardPersonnelNoFilter) {
        this.cardPersonnelNoFilter = cardPersonnelNoFilter;
    }

    public SortOrder getStartDateOrder() {
        return startDateOrder;
    }

    public void setStartDateOrder(SortOrder startDateOrder) {
        this.startDateOrder = startDateOrder;
    }

    public SortOrder getExpirationDateOrder() {
        return expirationDateOrder;
    }

    public void setExpirationDateOrder(SortOrder expirationDateOrder) {
        this.expirationDateOrder = expirationDateOrder;
    }

    public String getStartDateFilter() {
        return startDateFilter;
    }

    public void setStartDateFilter(String startDateFilter) {
        this.startDateFilter = startDateFilter.replaceAll("/", "");
    }

    public String getExpirationDateFilter() {
        return expirationDateFilter;
    }

    public void setExpirationDateFilter(String expirationDateFilter) {
        this.expirationDateFilter = expirationDateFilter.replaceAll("/", "");
    }

    public String getCardTypeCodeFilter() {
        return cardTypeCodeFilter;
    }

    public void setCardTypeCodeFilter(String cardTypeCodeFilter) {
        this.cardTypeCodeFilter = cardTypeCodeFilter;
    }

    public String getCardStatusCodeFilter() {
        return cardStatusCodeFilter;
    }

    public void setCardStatusCodeFilter(String cardStatusCodeFilter) {
        this.cardStatusCodeFilter = cardStatusCodeFilter;
    }

    public Collection<Object> getSelection() {
        return selection;
    }

    public void setSelection(Collection<Object> selection) {
        this.selection = selection;
    }

    public List<Person> getPersonItems() {
        return personItems;
    }

    public void setPersonItems(List<Person> personItems) {
        this.personItems = personItems;
    }

    public List<Person> getSelectionItems() {
        return selectionItems;
    }

    public void setSelectionItems(List<Person> selectionItems) {
        this.selectionItems = selectionItems;
    }

    public boolean isEnablePoll() {
        return enablePoll;
    }

    public void setEnablePoll(boolean enablePoll) {
        this.enablePoll = enablePoll;
    }

    public Person getCurrentPerson() {
        return currentPerson;
    }

    public void setCurrentPerson(Person currentPerson) {
        this.currentPerson = currentPerson;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public int getSelectionSize() {
        return selection == null ? 0 : selectionItems.size() - selection.size();
    }

    public int getSelectionItemsSize() {
        return selectionItems == null ? 0 : selectionItems.size();
    }
}
