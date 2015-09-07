package ir.university.toosi.wtms.web.action.monitoring;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.wtms.web.action.HandleCommentAction;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.action.person.HandlePersonAction;
import ir.university.toosi.wtms.web.helper.GeneralHelper;
import ir.university.toosi.wtms.web.model.entity.*;
import ir.university.toosi.wtms.web.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.model.entity.usermanagement.Role;
import ir.university.toosi.wtms.web.model.entity.usermanagement.WorkGroup;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.util.CalendarUtil;
import ir.university.toosi.wtms.web.util.LangUtil;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;
import org.richfaces.application.push.TopicKey;
import org.richfaces.application.push.TopicsContext;
import org.richfaces.cdi.push.Push;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Event;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.*;


@Named(value = "handleMonitoringAction")
@ApplicationScoped
public class HandleMonitoringAction implements Serializable {

    @Inject
    private UserManagementAction me;

    @Inject
    private GeneralHelper generalHelper;
    @Inject
    private HandleCommentAction handleCommentAction;
    @Inject
    private HandlePersonAction handlePersonAction;

    @Inject
    @Push(topic = CDI_PUSH_TOPIC)
    private Event<SentryDataModel> pushEvent;

    private static final String CDI_PUSH_TOPIC = "pushCdi";
    private String message;
    private String width;
    private boolean person;
    private boolean personAndGate;
    private boolean personAndTime;
    private int personPage;
    private int page;
    private DataModel<TrafficLog> eventLogList = null;
    private DataModel<List<DataModel<SentryDataModel>>> trafficLogs = null;
    private SentryDataModel currentSentryDataModel;
    private TrafficLog currentTrafficLog;
    private boolean validForComment;


    private DataModel<Person> personList = null;
    private SelectItem[] gatewayItems;
    private String gatewayId;
    private String startTime;
    private String endTime;
    private String startHour;
    private String startMinute;
    private String startSecond;
    private String endHour;
    private String endMinute;
    private String endSecond;
    private Hashtable<Long, LinkedList<SentryDataModel>> sentries = new Hashtable<>();
    private List<DataModel<SentryDataModel>> trafficLogsbygate = new ArrayList<>();
    private long sentryCount;

    @PostConstruct
    public void init() {
        try {
            TopicsContext topicsContext = TopicsContext.lookup();
            topicsContext.getOrCreateTopic(new TopicKey(CDI_PUSH_TOPIC));
            sentryCount = Long.valueOf(me.SENTRY_COUNT);

        } catch (Exception e) {
            throw new RuntimeException("Unable to initialize topics", e);
        }
    }

    public void sendMessage(TrafficLog log) {
        if (log == null || log.getGateway() == null || log.getPerson() == null)
            return;

        LinkedList<SentryDataModel> sentryDataModels = sentries.get(log.getPdp().getId());
        if (sentryDataModels != null) {
            SentryDataModel dataModel = new SentryDataModel();
            dataModel.setVideo(log.getVideo());
            dataModel.setId(log.getId());
            dataModel.setTime(log.getTime());
            dataModel.setDate(log.getDate());
            dataModel.setExit(log.isExit());
            dataModel.setValid(log.isValid());
            dataModel.setGate(log.getGateway().getName());
            dataModel.setPdpName(log.getPdp().getName());
            dataModel.setPersonId(log.getPerson().getId());
            dataModel.setName(log.getPerson().getName() + "  " + log.getPerson().getLastName());
            sentryDataModels.addFirst(dataModel);
            sentries.put(log.getPdp().getId(), sentryDataModels);
            trafficLogsbygate=new ArrayList<>() ;
            for (Queue<SentryDataModel> dataModels : sentries.values()) {
                trafficLogsbygate.add(new ListDataModel<>(new ArrayList<>(dataModels)));
            }

            pushEvent.fire(dataModel);
        }


    }

    public void forceOpen(DataModel<SentryDataModel> gate) {
        String logId = String.valueOf(gate.iterator().next().getId());
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/forceOpen");
        new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), logId);


    }


    private void initialize() {
        page = 1;
        trafficLogsbygate = new ArrayList<>();
        LinkedList<SentryDataModel> trafficLogList;
        List<Gateway> notAccess = new ArrayList<>();
        List<Long> pdpID = new ArrayList<>();
        for (WorkGroup workGroup : me.getUser().getWorkGroups()) {
            for (Role role : workGroup.getRoles()) {
                for (Permission permission : role.getPermissions()) {
                    if (permission.getPermissionType().equals(PermissionType.PDP)) {
                        pdpID.add(Long.valueOf(permission.getObjectId()));
                    }
                }
            }
        }

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPdpbyIDs");
        try {
            List<PDP> pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(pdpID)), new TypeReference<List<PDP>>() {

            });
            for (PDP pdp : pdps) {
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/findTrafficByPDP");
                List<TrafficLog> traffic = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(pdp.getId())), new TypeReference<List<TrafficLog>>() {
                });
                SentryDataModel dataModel;
                trafficLogList = new LinkedList<SentryDataModel>() {
                    @Override
                    public boolean add(SentryDataModel sentryDataModel) {
                        if (size() == sentryCount)
                            remove();
                        return super.add(sentryDataModel);
                    }
                };
                sentries.put(pdp.getId(), trafficLogList);
                for (TrafficLog log : traffic) {
                    dataModel = new SentryDataModel();
                    dataModel.setVideo(log.getVideo());
                    dataModel.setTime(log.getTime());
                    dataModel.setDate(log.getDate());
                    dataModel.setExit(log.isExit());
                    dataModel.setValid(log.isValid());
                    dataModel.setGate(log.getGateway().getName());
                    dataModel.setPersonId(log.getPerson().getId());
                    dataModel.setId(log.getId());
                    dataModel.setPdpName(log.getPdp().getName());
                    dataModel.setName(log.getPerson().getName() + "  " + log.getPerson().getLastName());
                    trafficLogList.add(dataModel);
                }
                trafficLogsbygate.add(new ListDataModel<>((new ArrayList<>(trafficLogList))));
            }
//                else{
//                    notAccess.add(gateway);
//                }


//            }
//            gateways.remove(notAccess);
//            trafficLogs = new ListDataModel<List<DataModel<SentryDataModel>>>(trafficLogsbygate);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String begin() {
        me.setActiveMenu(MenuType.SENTRY);
        refresh();
        return "monitoring";
    }

    public String beginSentry() {
        me.setActiveMenu(MenuType.SENTRY);
        initialize();
        return "sentry";
    }

    public void trackByPerson() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        personPage = 1;
        person = true;
        personAndGate = false;
        personAndTime = false;
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

    public void selectForComment(SentryDataModel sentryDataModel) {
        handleCommentAction.setMessage("");
        currentSentryDataModel = sentryDataModel;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findTrafficLogById");
        try {
            currentTrafficLog = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentSentryDataModel.getId())), TrafficLog.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        validForComment = true;
        long logMin = time2Minute(currentTrafficLog.getTime());
        long commentMin = time2Minute(LangUtil.getEnglishNumber(CalendarUtil.getTime(new Date(), new Locale("fa"))));
        if ((commentMin - logMin) > Long.valueOf(me.getSystemParameter().get(SystemParameterType.TRAFFIC_LOG_COMMENT_VALID_TIME)))
            validForComment = false;


        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findByTrafficLog");
        try {
            Comment comment = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentTrafficLog.getId())), Comment.class);
            handleCommentAction.setMessage(comment.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void paint(OutputStream stream, Object object) throws IOException, URISyntaxException {
        Long personId = (Long) object;
        Person person = null;

        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPersonById");
        try {
            person = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(personId)), Person.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (person.getPicture() != null)
            stream.write(person.getPicture());
        else

            stream.write(generalHelper.getAnonymous());


        stream.flush();
        stream.close();
    }

    public void dotrackByPerson() {
        Person currentPerson = personList.getRowData();
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findTrafficLogByPersonId");
        List<TrafficLog> innerTrafficLogList = null;
        try {
            innerTrafficLogList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(currentPerson.getId())), new TypeReference<List<TrafficLog>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        init();
        eventLogList = new ListDataModel<>(innerTrafficLogList);
        me.redirect("/monitoring/monitor-log.htm");
    }


    public void trackByPersonAndGate() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = true;
        personAndTime = false;
        personPage = 1;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllPersonDataModel");
        List<Person> innerPersonList = null;
        try {
            innerPersonList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Person>>() {
            });

            personList = new ListDataModel<>(innerPersonList);

            me.getGeneralHelper().getWebServiceInfo().setServiceName("/getAllGateway");

            List<Gateway> gateways = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName()), new TypeReference<List<Gateway>>() {
            });
            gatewayItems = new SelectItem[gateways.size()];
            int i = 0;
            for (Gateway gateway1 : gateways) {
                gatewayItems[i++] = new SelectItem(gateway1.getId(), gateway1.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        gatewayId = gatewayItems[0].getValue().toString();

    }

    public void dotrackByPersonAndGate() {
        List<TrafficLog> innerTrafficLogList = null;
        handlePersonAction.init();
        try {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findByPersonAndGate");
            String ids = String.valueOf(personList.getRowData().getId()) + "#" + gatewayId;
            innerTrafficLogList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), ids), new TypeReference<List<TrafficLog>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        init();
        eventLogList = new ListDataModel<>(innerTrafficLogList);
        me.redirect("/monitoring/monitor-log.htm");
    }

    public void trackByPersonAndTime() {
        handlePersonAction.init();
        me.setActiveMenu(MenuType.REPORT);
        person = false;
        personAndGate = false;
        personAndTime = true;
        personPage = 1;
        startTime = "0";
        endTime = "0";
        startHour = "0";
        startMinute = "0";
        startSecond = "0";
        endHour = "0";
        endMinute = "0";
        endSecond = "0";
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

    public void dotrackByPersonAndTime() {
        startTime = (startHour.length() == 2 ? startHour : '0' + startHour) + ":" + (startMinute.length() == 2 ? startMinute : '0' + startMinute) + ":" + (startSecond.length() == 2 ? startSecond : '0' + startSecond);
        endTime = (endHour.length() == 2 ? endHour : '0' + endHour) + ":" + (endMinute.length() == 2 ? endMinute : '0' + endMinute) + ":" + (endSecond.length() == 2 ? endSecond : '0' + endSecond);
        long startTime1 = time2long(startTime);
        long endTime1 = time2long(endTime);

        if (endTime1 < startTime1) {
            me.addInfoMessage(me.getValue("wrong.time"));
            me.redirect("/home.htm");
            return;
        }

        String data = personList.getRowData().getId() + "#" + startTime + "#" + endTime;
        me.getGeneralHelper().getWebServiceInfo().setServiceName("/findByPersonLocationInDuration");
        List<TrafficLog> innerTrafficLogList = null;
        try {
            innerTrafficLogList = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), data), new TypeReference<List<TrafficLog>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        eventLogList = new ListDataModel<>(innerTrafficLogList);
        me.redirect("/monitoring/monitor-log.htm");
    }


    private void refresh() {
        initialize();
    }

    public void mapEdition() {

    }

    public long time2long(String time) {
        String[] d = time.split(":");
        String s = (d[0].length() == 2 ? d[0] : '0' + d[0]) + (d[1].length() == 2 ? d[1] : '0' + d[1]) + (d[2].length() == 2 ? d[2] : '0' + d[2]);
        return Long.valueOf(s);
    }

    public long time2Minute(String time) {
        String[] d = time.split(":");
        long min = Long.valueOf(d[0]) * 60 + Long.valueOf(d[1]);
        return min;
    }

    public void resetPersonPage() {
        setPersonPage(1);
    }

    public static String getCdiPushTopic() {

        return CDI_PUSH_TOPIC;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserManagementAction getMe() {
        return me;
    }

    public void setMe(UserManagementAction me) {
        this.me = me;
    }

    public GeneralHelper getGeneralHelper() {
        return generalHelper;
    }

    public void setGeneralHelper(GeneralHelper generalHelper) {
        this.generalHelper = generalHelper;
    }

    public Event<SentryDataModel> getPushEvent() {
        return pushEvent;
    }

    public void setPushEvent(Event<SentryDataModel> pushEvent) {
        this.pushEvent = pushEvent;
    }

    public Hashtable<Long, LinkedList<SentryDataModel>> getSentries() {
        return sentries;
    }

    public void setSentries(Hashtable<Long, LinkedList<SentryDataModel>> sentries) {
        this.sentries = sentries;
    }

    public boolean isPerson() {
        return person;
    }

    public void setPerson(boolean person) {
        this.person = person;
    }

    public boolean isPersonAndGate() {
        return personAndGate;
    }

    public void setPersonAndGate(boolean personAndGate) {
        this.personAndGate = personAndGate;
    }

    public boolean isPersonAndTime() {
        return personAndTime;
    }

    public void setPersonAndTime(boolean personAndTime) {
        this.personAndTime = personAndTime;
    }

    public DataModel<TrafficLog> getEventLogList() {
        return eventLogList;
    }

    public void setEventLogList(DataModel<TrafficLog> eventLogList) {
        this.eventLogList = eventLogList;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public SelectItem[] getGatewayItems() {
        return gatewayItems;
    }

    public void setGatewayItems(SelectItem[] gatewayItems) {
        this.gatewayItems = gatewayItems;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public DataModel<List<DataModel<SentryDataModel>>> getTrafficLogs() {
        return trafficLogs;
    }

    public void setTrafficLogs(DataModel<List<DataModel<SentryDataModel>>> trafficLogs) {
        this.trafficLogs = trafficLogs;
    }

    public List<DataModel<SentryDataModel>> getTrafficLogsbygate() {
        return trafficLogsbygate;
    }

    public void setTrafficLogsbygate(List<DataModel<SentryDataModel>> trafficLogsbygate) {
        this.trafficLogsbygate = trafficLogsbygate;
    }

    public long getSentryCount() {
        return sentryCount;
    }

    public void setSentryCount(long sentryCount) {
        this.sentryCount = sentryCount;
    }

    public SentryDataModel getCurrentSentryDataModel() {
        return currentSentryDataModel;
    }

    public void setCurrentSentryDataModel(SentryDataModel currentSentryDataModel) {
        this.currentSentryDataModel = currentSentryDataModel;
    }

    public TrafficLog getCurrentTrafficLog() {
        return currentTrafficLog;
    }

    public void setCurrentTrafficLog(TrafficLog currentTrafficLog) {
        this.currentTrafficLog = currentTrafficLog;
    }

    public String getWidth() {

        return (100 / trafficLogsbygate.size()) + "%";
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public boolean isValidForComment() {
        return validForComment;
    }

    public void setValidForComment(boolean validForComment) {
        this.validForComment = validForComment;
    }
}
