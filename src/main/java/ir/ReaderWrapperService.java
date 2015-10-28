package ir;

import ir.university.toosi.tms.model.entity.Languages;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.EJB3RemoteInterface;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.readerwrapper.AccessEventData;
import ir.university.toosi.tms.readerwrapper.GetAccessEventDataDelegate;
import ir.university.toosi.tms.readerwrapper.Person;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by farzad on 10/21/2015.
 */
@Stateless
@Remote(IReaderWrapperService.class)
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class ReaderWrapperService implements IReaderWrapperService {
    @EJB
    PersonServiceImpl personService;
    @EJB
    GatewayServiceImpl gatewayService;
    @EJB
    VirdiServiceImpl virdiService;
    @EJB
    TrafficLogServiceImpl trafficLogService;

    private HandleMonitoringAction monitoringAction;

    @Override
    public void forceOpenDoor(int terminalId) {

    }

    @Override
    public void lockDoor(int terminalId) {

    }

    @Override
    public void unLockDoor(int terminalId) {

    }

    @Override
    public void lockTerminal(int terminalId) {

    }

    @Override
    public void unLockTerminal(int terminalId) {

    }

    @Override
    public void getUserList(int terminalId) {

    }

    @Override
    public void setUserList(int terminalId, List<Person> personList, boolean isSucceed, String failedMessage) {
        System.out.println(personList.size());
        for (Person person : personList) {
            ir.university.toosi.tms.model.entity.personnel.Person person1 = new ir.university.toosi.tms.model.entity.personnel.Person();
            person1.setPersonOtherId(String.valueOf(person.getUserId()));
            person1.setId(person.getUserId());
            person1.setName(person.getUserName());
            person1.setPersonnelNo(person.getEmplymentCode());
            if (personService.findById(person1.getId()) == null) {
                personService.createPerson(person1);
            } else {
                personService.editPerson(person1);
            }
        }

    }

    @Override
    public void addOnGetAccessEventData(int terminalId, AccessEventData value) {
        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setTime(value.getDateTime());
        trafficLog.setDate(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
        trafficLog.setExit(true);
        trafficLog.setValid(value.isAuthorized());
        trafficLog.setVirdi(virdiService.findByTerminalId(terminalId));
        trafficLog.setPerson(personService.findById(value.getUserID()));
        trafficLog.setGateway(trafficLog.getVirdi().getGateway());
        trafficLog.setCard(null);
        trafficLog.setFinger(true);
        trafficLog.setLast(true);
        trafficLog.setOffline(false);
        trafficLog.setPictures("");
        trafficLog.setDeleted("0");
        trafficLog.setStatus("c");
        trafficLog.setZone(trafficLog.getGateway().getZone());
        trafficLog.setCurrentLang(new Languages());
        trafficLog.setVideo("");
        trafficLogService.createTrafficLog(trafficLog);
        monitoringAction.sendMessage(trafficLog);

    }

    public HandleMonitoringAction getMonitoringAction() {
        return monitoringAction;
    }

    public void setMonitoringAction(HandleMonitoringAction monitoringAction) {
        this.monitoringAction = monitoringAction;
    }
}
