package ir.university.toosi.tms.model.service.zone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.dao.zone.VirdiDAOImpl;
import ir.university.toosi.tms.model.entity.BLookup;
import ir.university.toosi.tms.model.entity.EventLogType;
import ir.university.toosi.tms.model.entity.Permission;
import ir.university.toosi.tms.model.entity.PermissionType;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.PDPSync;
import ir.university.toosi.tms.model.entity.zone.Virdi;
import ir.university.toosi.tms.model.entity.zone.VirdiSync;
import ir.university.toosi.tms.model.service.EventLogServiceImpl;
import ir.university.toosi.tms.model.service.GatewayPersonServiceImpl;
import ir.university.toosi.tms.model.service.PermissionServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.util.*;
import org.jboss.ejb3.annotation.TransactionTimeout;

import javax.ejb.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
@TransactionTimeout(unit = TimeUnit.HOURS, value = 4)
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class VirdiServiceImpl<T extends Virdi> {

    @EJB
    private VirdiDAOImpl virdiDAO;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private CalendarDateServiceImpl calendarDateService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private PermissionServiceImpl permissionService;
    @EJB
    private RuleServiceImpl ruleService;

    public T findById(long id) {
        try {
            return (T) virdiDAO.findById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByGatewayId(String id) {
        try {
            return (List<T>) virdiDAO.findByGatewayId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllPdpbyIDs(List<Long> id) {
        try {
            return (List<T>) virdiDAO.getAllPdpbyIDs(id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> findByCameraId(long id) {
        try {
            return (List<T>) virdiDAO.findByCameraId(id);
        } catch (Exception e) {
            return null;
        }
    }

    public T findByIp(String ip) {
        try {
//            EventLogManager.eventLog(eventLogService, null, BLookup.class.getSimpleName(), EventLogType.SEARCH, Virdi.getEffectorUser());
            return (T) virdiDAO.findByIp(ip);
        } catch (Exception e) {
            return null;
        }
    }

    public List<T> getAllVirdis() {
        try {
            return (List<T>) virdiDAO.findAll("Virdi.list", true);
        } catch (Exception e) {
            return null;
        }
    }

    public long getMaximumId() {
        try {
            return virdiDAO.maximumId("Virdi.maximum", true);
        } catch (Exception e) {
            return 1;
        }
    }

    public String deleteVirdi(T entity) {
        try {
            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.DELETE, entity.getEffectorUser());
            virdiDAO.delete(findById(entity.getId()));
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "FALSE";
        }
    }

    public T createVirdi(T entity) {
        try {
            T t = (T) virdiDAO.create(entity);
            Permission permission = new Permission(String.valueOf(t.getId()), t.getName(), PermissionType.VIRDI);
            permissionService.createPermission(permission);
            EventLogManager.eventLog(eventLogService, String.valueOf(t.getId()), BLookup.class.getSimpleName(), EventLogType.ADD, entity.getEffectorUser());
            return t;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean editVirdi(T entity) {
        try {
            Virdi old = findById(entity.getId());
            Virdi newVirdi = new Virdi();

            newVirdi.setName(old.getName());
            newVirdi.setEnabled(old.isEnabled());
            newVirdi.setGateway(old.getGateway());
            newVirdi.setCamera(old.getCamera());
            newVirdi.setIp(old.getIp());
            newVirdi.setOnline(old.isOnline());
            newVirdi.setDescription(old.getDescription());
            newVirdi.setStatus("o," + entity.getEffectorUser());

            virdiDAO.createOld(newVirdi);

            EventLogManager.eventLog(eventLogService, String.valueOf(entity.getId()), BLookup.class.getSimpleName(), EventLogType.EDIT, entity.getEffectorUser());
            virdiDAO.update(entity);
            return true;
        } catch (Exception e) {
            return false;
        }
    }






    public boolean synchronizePdp(VirdiSync virdiSync) {
        Set<Virdi> pdps = virdiSync.getVirdiList();
        if (pdps == null || pdps.size() == 0)
            return false;
        boolean flag = true;
//        cleanDirectory(pdpSync);
//        try {
//            generateGate(pdpSync);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        for (Virdi pdp : pdps) {
//            try {
//                if (ping(pdp.getIp())) {
//                    if (generateFiles(pdpSync, pdp)) {
//                        new Thread(new FileUploaderUtil(pdp, this)).start();
//                    } else {
//                        flag = false;
//
//                    }
//                } else {
                    T t = findByIp(pdp.getIp());
                    t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                    t.setSuccess(false);
                    editVirdi(t);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }


        return flag;
    }

    public boolean synchronizeOnePdp(VirdiSync pdpSync) {
        Set<Virdi> pdps = pdpSync.getVirdiList();
        if (pdps == null || pdps.size() == 0)
            return false;
        boolean flag = true;
//        cleanDirectory(pdpSync);
//        try {
//            generate4OneGate(pdpSync);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for (Virdi pdp : pdps) {
//            try {
//                if (generateFiles4One(pdpSync, pdp)) {
//                    new Thread(new FileUploaderUtil(pdp, this)).start();
//                } else {
//                    flag = false;
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }


        return flag;
    }

    public boolean fingerPrint(Set<Virdi> pdps) {
        List<Person> persons = personService.findWithRulePackage();
        for (Virdi pdp : pdps) {
            try {
                if (ping(pdp.getIp())) {
                    for (Person person : persons) {

                        String zeros = "";
                        for (int i = 1; i <= 8 - (person.getPersonOtherId().length()); i++) {
                            zeros += "0";
                        }
                        byte[] b = TFTPUtility.get(pdp.getIp(), zeros + person.getPersonOtherId() + ".fpt");
                        person.setFinger(b.length == 0 ? null : b);
                        if (person.getFinger() != null) {
                            personService.editPerson(person);
                        }
                    }
                } else {
                    T t = findByIp(pdp.getIp());
                    t.setUpdateDate(CalendarUtil.getDate(new Date(), new Locale("fa")));
                    t.setSuccess(false);
                    editVirdi(t);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean ping(String ip) throws IOException {
        InetAddress inet;
        inet = InetAddress.getByName(ip);
        return inet.isReachable(2000);
    }

    public boolean exist(String ip, long id) {
        try {
            return virdiDAO.exist(ip, id);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean existNotId(String ip) {
        try {
            return virdiDAO.existNotId(ip);
        } catch (Exception e) {
            return false;
        }
    }

}