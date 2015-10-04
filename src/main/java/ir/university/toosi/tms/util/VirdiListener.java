package ir.university.toosi.tms.util;

import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.service.AuthenticateServiceImpl;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.zone.VirdiServiceImpl;
import ir.university.toosi.tms.readerwrapper.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by farzad on 10/4/15.
 */
public class VirdiListener {
    private static VirdiServiceImpl virdiService;
    private static PersonServiceImpl personService;
    private static AuthenticateServiceImpl authenticateService;
    private static CrossingServiceImpl crossingService;
    private static CardServiceImpl cardService;
    private static TrafficLogServiceImpl trafficLogService;
    static ReaderWrapper readerWrapper = new ReaderWrapper();


    public static void accessControl() {
        readerWrapper.addOnVerifyFinger1To1(new VerifyFinger1To1Delegate() {
            @Override
            public void Invoke(int terminalId, int userId, VirdiAuthMode authMode, byte[] fingerData) {

                ir.university.toosi.tms.model.entity.personnel.Person personEntity = personService.findById(userId);

                TrafficLog trafficLog = null;
                Person person = new Person();
                person.setFingerprints(personEntity.getFinger());
                person.setUserIdforTerminal(userId);
                if (readerWrapper.Verify(person, fingerData)) {
                    try {
                        trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(userId), String.valueOf(authMode.GetHashCode()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trafficLog != null) {
                        try {
                            finalizeTerrificLog(trafficLog, personService.findById(userId));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, trafficLog.isValid());
                    } else {
                        readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
                    }

                }
                readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
            }
        });

        readerWrapper.addOnVerifyCard(new VerifyCardDelegate() {
            TrafficLog trafficLog = null;

            @Override
            public void Invoke(int terminalId, VirdiAuthMode authMode, String card) {

                ir.university.toosi.tms.model.entity.personnel.Person person = cardService.findByCode(card).getPerson();
                if (person != null) {
                    try {
                        trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(person.getId()), String.valueOf(authMode.GetHashCode()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (trafficLog != null) {
                        try {
                            finalizeTerrificLog(trafficLog, person);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, trafficLog.isValid());

                    } else {
                        readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, false);
                    }
                } else {
                    readerWrapper.SendAuthResultToTerminal(terminalId, Integer.parseInt(String.valueOf(person.getId())), null, false);
                }

            }
        });

        readerWrapper.addOnVerifyUserPass(new VerifyUserPassDelegate() {
            @Override
            public void Invoke(int terminalId, int userId, VirdiAuthMode authMode, String password) {
                TrafficLog trafficLog = null;
                try {
                    trafficLog = crossingService.authorize(virdiService.findById(terminalId), String.valueOf(userId), String.valueOf(authMode.GetHashCode()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (trafficLog != null) {
                    try {
                        finalizeTerrificLog(trafficLog, personService.findById(userId));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, trafficLog.isValid());
                } else {
                    readerWrapper.SendAuthResultToTerminal(terminalId, userId, null, false);
                }

            }
        });

    }

    public static void VirdiHealth(){
        readerWrapper.addOnTerminalConnected(new TerminalConnectedDelegate() {
            @Override
            public void Invoke(int terminalId, String terminalIP) {
                Virdi virdi=virdiService.findById(terminalId);
                virdi.setHealthStatus(true);
                virdiService.editVirdi(virdi);
            }
        });
        readerWrapper.addOnTerminalConnected(new TerminalConnectedDelegate() {
            @Override
            public void Invoke(int terminalId, String terminalIP) {
                Virdi virdi=virdiService.findById(terminalId);
                virdi.setHealthStatus(false);
                virdiService.editVirdi(virdi);
            }
        });

    }

    private static void finalizeTerrificLog(TrafficLog trafficLog, ir.university.toosi.tms.model.entity.personnel.Person person) throws IOException {
        if (trafficLog.isValid()) {

            if (trafficLog.getPdp().getCamera() != null) {
                createPicture(trafficLog);
            }
            trafficLog.setPerson(person);
            trafficLog.setOrgan(person.getOrganRef());
            TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(person.getId(), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
            if (lastTrafficLog != null) {
                lastTrafficLog.setLast(false);
                trafficLogService.editTrafficLog(lastTrafficLog);
            }
            trafficLog.setLast(true);
        } else {
            trafficLog.setLast(false);
            if (trafficLog.getPdp().getCamera() != null) {
                createPicture(trafficLog);
            }
            trafficLog.setPerson(person);
            trafficLog.setOrgan(person.getOrganRef());
        }
        trafficLogService.createTrafficLog(trafficLog);
    }

    private static void createPicture(TrafficLog trafficLog) throws IOException {
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


}
