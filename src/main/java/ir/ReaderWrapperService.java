package ir;

import ir.university.toosi.parking.entity.ParkingLog;
import ir.university.toosi.parking.service.ParkingLogServiceImpl;
import ir.university.toosi.tms.model.entity.Languages;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.zone.Zone;
import ir.university.toosi.tms.model.service.EJB3RemoteInterface;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.GatewayServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.readerwrapper.AccessEventData;
import ir.university.toosi.tms.readerwrapper.GetAccessEventDataDelegate;
import ir.university.toosi.tms.readerwrapper.Person;
import ir.university.toosi.tms.readerwrapper.PersonHolder;
import ir.university.toosi.tms.util.Configuration;
import ir.university.toosi.tms.util.Initializer;
import ir.university.toosi.tms.util.LangUtil;
import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.util.CalendarUtil;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
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
    CardServiceImpl cardService;
    @EJB
    TrafficLogServiceImpl trafficLogService;
    @EJB
    ParkingLogServiceImpl parkingLogService;
    @Inject
    private HandleMonitoringAction monitoringAction;

    public void setParkingLogService(ParkingLogServiceImpl parkingLogService) {
        this.parkingLogService = parkingLogService;
    }


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
    public void setUserList(int terminalId, PersonHolder personList) {
        System.out.println(personList.getPersons().length);
        for (Person person : personList.getPersons()) {
            ir.university.toosi.tms.model.entity.personnel.Person person1 = new ir.university.toosi.tms.model.entity.personnel.Person();
            person1.setPersonOtherId(String.valueOf(person.getUserId()));
            person1.setName(person.getUserName());
            person1.setPersonnelNo(person.getEmplymentCode());
            person1.setFinger(person.getFingers());

            if (personService.getPersonByPersonOtherId(person1.getPersonOtherId()) == null) {
                person1 = personService.createPerson(person1);
                if (person.getCards() != null)
                    for (String s : person.getCards()) {
                        Card card = new Card();
                        card.setCode(s);
                        card.setPerson(person1);
                        card.setVisible(true);
                        cardService.createCard(card);
                    }
            } else {
                personService.editPerson(person1);
            }
        }

    }

    @Override
    public void addOnGetAccessEventData(int terminalId, AccessEventData value) {
        TrafficLog trafficLog = new TrafficLog();
        trafficLog.setTime(LangUtil.getEnglishNumber(CalendarUtil.getTimeWithoutDot(new Date(), new Locale("fa"))));
        trafficLog.setDate(LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));
        trafficLog.setExit(true);
        trafficLog.setValid(value.isAuthorized());
        trafficLog.setVirdi(virdiService.findByTerminalId(terminalId));
        trafficLog.setPerson(personService.getPersonByPersonOtherId(String.valueOf(value.getUserID())));
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
//        try {
//            createPicture(trafficLog);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        monitoringAction.sendMessage(trafficLog);

    }

    @Override
    public boolean addOnGetOnlineVerifyAccessControl(int terminalId, AccessEventData accessEventData) {
        return false;
    }

    @Override
    public void addUserInfo(int terminal, PersonHolder personHolder) {

    }

    @Override
    public void sendParking(String pelak, byte[] pic) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        calendar.add(Calendar.SECOND, -10);
        Date tenMinAgo = calendar.getTime();
        List logs = parkingLogService.findParkingInDurationTime(LangUtil.getEnglishNumber(CalendarUtil.getTimeWithoutDot(tenMinAgo, new Locale("fa"))),LangUtil.getEnglishNumber(CalendarUtil.getTimeWithoutDot(now, new Locale("fa"))),pelak);
        if (logs == null || logs.size() == 0) {
            ParkingLog parkingLog = new ParkingLog();
            parkingLog.setTraffic_time(LangUtil.getEnglishNumber(CalendarUtil.getTimeWithoutDot(now, new Locale("fa"))));
            parkingLog.setTraffic_date(LangUtil.getEnglishNumber(CalendarUtil.getPersianDateWithoutSlash(new Locale("fa"))));
            parkingLog.setPictures("/" + pelak + new Date().getTime());
            parkingLog.setDeleted("0");
            parkingLog.setStatus("c");
            parkingLog.setCurrentLang(new Languages());
            parkingLog.setNumber(pelak);
            parkingLogService.createParkingLog(parkingLog);
            try {
                createPicture(pic, parkingLog.getPictures());
            } catch (IOException e) {
                e.printStackTrace();
            }
//        try {
//            createPicture(trafficLog);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
            monitoringAction.sendMessage(parkingLog, true);
        }

    }

    public HandleMonitoringAction getMonitoringAction() {
        return monitoringAction;
    }

    public void setMonitoringAction(HandleMonitoringAction monitoringAction) {
        this.monitoringAction = monitoringAction;
    }

    private void createPicture(TrafficLog trafficLog) throws IOException {
        File folder = new File(Configuration.getProperty("jboss.name") + trafficLog.getPictures());
        List<byte[]> pics = Initializer.pics.get(trafficLog.getPdp().getCamera().getId());
        if (!folder.exists()) {
            folder.mkdir();
        }
        for (int i = 0; i < pics.size(); i++) {
            File file = new File(Configuration.getProperty("jboss.name") + trafficLog.getPictures() + "/" + i + ".png");

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(pics.get(i));
            fileOutputStream.flush();
            fileOutputStream.close();

        }

    }

    private void createPicture(byte[] pics, String picName) throws IOException {
//        File folder = new File(Configuration.getProperty("jboss.name") + picName);
//        if (!folder.exists()) {
//            folder.mkdir();
//        }
        File file = new File(Configuration.getProperty("jboss.name") + picName + "/" + 1 + ".png");
        File dir = file.getParentFile();
        if (!dir.exists()){
            dir.mkdir();
        }

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(pics);
        fileOutputStream.flush();
        fileOutputStream.close();

    }
}
