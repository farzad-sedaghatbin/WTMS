package ir.university.toosi.tms.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.TrafficLog;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.service.CrossingServiceImpl;
import ir.university.toosi.tms.model.service.TrafficLogServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Locale;

/**
 * Created by farzad on 6/8/2014.
 */
public class T implements Runnable {
    Socket socket;
    CrossingServiceImpl crossingService;
    TrafficLogServiceImpl trafficLogService;
    PersonServiceImpl personService;

    public T(Socket socket, CrossingServiceImpl crossingService, TrafficLogServiceImpl trafficLogService, PersonServiceImpl personService) {
        this.socket = socket;
        this.crossingService = crossingService;
        this.trafficLogService = trafficLogService;
        this.personService = personService;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public CrossingServiceImpl getCrossingService() {
        return crossingService;
    }

    public void setCrossingService(CrossingServiceImpl crossingService) {
        this.crossingService = crossingService;
    }

    public TrafficLogServiceImpl getTrafficLogService() {
        return trafficLogService;
    }

    public void setTrafficLogService(TrafficLogServiceImpl trafficLogService) {
        this.trafficLogService = trafficLogService;
    }

    public PersonServiceImpl getPersonService() {
        return personService;
    }

    public void setPersonService(PersonServiceImpl personService) {
        this.personService = personService;
    }

    @Override
    public void run() {

        TrafficLog trafficLog=null;
       long  start=System.currentTimeMillis();
        trafficLog=null;
        try {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String receivedMSG = bufferedReader.readLine();

            receivedMSG = bufferedReader.readLine();

        System.out.println("2222>>>>>>>>>>" + receivedMSG);
//                socket.getOutputStream().write(("rsc 0").getBytes());
//                System.out.println("send");
        String[] messageType = receivedMSG.split(" ");
        Person person=null;
        if (messageType[0].equalsIgnoreCase("rqi")) {
            String[] s = messageType[1].split(",");
//                    System.out.println("1111111111111");
            trafficLog = crossingService.authorize(socket.getInetAddress().toString().replace("/", ""), s[0], s[1],start);
//                    System.out.println("33333333333333333");
            if (trafficLog != null) {
                if (trafficLog.isValid()) {
                    socket.getOutputStream().write(("rsc 0").getBytes());
                    System.out.println("send true");
                    socket.getOutputStream().flush();
                    System.out.println(System.currentTimeMillis()-start);
                    person= personService.getPersonByPersonOtherId(s[0]);
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
                    socket.getOutputStream().write(("rsc 1").getBytes());
                    System.out.println("send false");
                    socket.getOutputStream().flush();
                    System.out.println(System.currentTimeMillis() - start);
                    person=personService.getPersonByPersonOtherId(s[0]);
                    trafficLog.setPerson(person);
                    trafficLog.setOrgan(person.getOrganRef());
                }
                Initializer.connections.put(trafficLog.getPdp().getId(),socket);
                trafficLogService.createTrafficLog(trafficLog);

            }
        } else if (messageType[0].equalsIgnoreCase("off")) {
            String[] s = messageType[1].split(",");

            trafficLog = trafficLogService.offline(socket.getInetAddress().toString().replace("/", ""), s[0], s[1], s[2]);
            if (trafficLog != null && trafficLog.isValid()) {
                socket.getOutputStream().write(("off tok," + s[0] + "\r\n").getBytes());
                socket.getOutputStream().flush();
                Thread.sleep(1000);

            } else {

                socket.getOutputStream().write(("off notok," + s[0] + "\r\n").getBytes());
                socket.getOutputStream().flush();
                Thread.sleep(1000);

            }
            if (trafficLog != null) {

                TrafficLog lastTrafficLog = trafficLogService.findLastByPerson(trafficLog.getPerson().getId(), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                if (lastTrafficLog != null) {
                    lastTrafficLog.setLast(false);
                    trafficLogService.editTrafficLog(lastTrafficLog);
                }
                trafficLog.setLast(true);
                trafficLogService.createTrafficLog(trafficLog);
            }
        }
        try {
            new RESTfulClientUtil().restFullService("http://" + Configuration.getProperty("server.ip") + ":" + Configuration.getProperty("kernel.port") + "/WTMS/restful/TMSService", "/Cross", new ObjectMapper().writeValueAsString(trafficLog));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
