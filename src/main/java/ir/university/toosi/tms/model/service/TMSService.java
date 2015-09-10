package ir.university.toosi.tms.model.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.university.toosi.tms.model.entity.*;
import ir.university.toosi.tms.model.entity.Map;
import ir.university.toosi.tms.model.entity.calendar.Calendar;
import ir.university.toosi.tms.model.entity.calendar.CalendarDate;
import ir.university.toosi.tms.model.entity.calendar.DayType;
import ir.university.toosi.tms.model.entity.personnel.Card;
import ir.university.toosi.tms.model.entity.personnel.Job;
import ir.university.toosi.tms.model.entity.personnel.Organ;
import ir.university.toosi.tms.model.entity.personnel.Person;
import ir.university.toosi.tms.model.entity.rule.Rule;
import ir.university.toosi.tms.model.entity.rule.RuleException;
import ir.university.toosi.tms.model.entity.rule.RulePackage;
import ir.university.toosi.tms.model.entity.zone.*;
import ir.university.toosi.tms.model.service.calendar.CalendarDateServiceImpl;
import ir.university.toosi.tms.model.service.calendar.CalendarServiceImpl;
import ir.university.toosi.tms.model.service.calendar.DayTypeServiceImpl;
import ir.university.toosi.tms.model.service.personnel.CardServiceImpl;
import ir.university.toosi.tms.model.service.personnel.JobServiceImpl;
import ir.university.toosi.tms.model.service.personnel.OrganServiceImpl;
import ir.university.toosi.tms.model.service.personnel.PersonServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleExceptionServiceImpl;
import ir.university.toosi.tms.model.service.rule.RulePackageServiceImpl;
import ir.university.toosi.tms.model.service.rule.RuleServiceImpl;
import ir.university.toosi.tms.model.service.zone.*;
import ir.university.toosi.tms.util.CalendarUtil;
import ir.university.toosi.tms.util.SystemParameterManager;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;


/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@ApplicationPath("restful")
@Path("/TMSService")
public class TMSService extends Application {

    @EJB
    private UserServiceImpl userService;
    @EJB
    private RoleServiceImpl roleService;
    @EJB
    private PermissionServiceImpl permissionService;
    @EJB
    private OperationServiceImpl operationService;
    @EJB
    private WorkGroupServiceImpl workGroupService;
    @EJB
    private ZoneServiceImpl zoneService;
    @EJB
    private LanguageServiceImpl languageService;
    @EJB
    private LanguageKeyManagementServiceImpl languageKeyManagementService;
    @EJB
    private LanguageManagementServiceImpl languageManagementService;
    @EJB
    private GatewayServiceImpl gatewayService;
    @EJB
    private PDPServiceImpl pdpService;
    @EJB
    private CameraServiceImpl cameraService;
    @EJB
    private PersonServiceImpl personService;
    @EJB
    private OrganServiceImpl organService;
    @EJB
    private JobServiceImpl jobService;
    @EJB
    private CardServiceImpl cardService;
    @EJB
    private EventLogServiceImpl eventLogService;
    @EJB
    private BLookupServiceImpl bLookupService;
    @EJB
    private SavedQueryServiceImpl savedQueryService;
    @EJB
    private LookupServiceImpl lookupService;
    @EJB
    private CalendarServiceImpl calendarService;
    @EJB
    private PCServiceImpl pcService;
    @EJB
    private DayTypeServiceImpl dayTypeService;
    @EJB
    private RulePackageServiceImpl rulePackageService;
    @EJB
    private RuleServiceImpl ruleService;
    @EJB
    private AuthenticateServiceImpl authenticateService;
    @EJB
    private RuleExceptionServiceImpl ruleExceptionService;
    @EJB
    private TrafficLogServiceImpl trafficLogService;
    @EJB
    private PhotoServiceImpl photoService;
    @EJB
    private CrossingServiceImpl crossingService;
    @EJB
    private CalendarDateServiceImpl calendarDateService;
    @EJB
    private GatewayPersonServiceImpl gatewayPersonService;
    @EJB
    private SystemConfigurationServiceImpl systemConfigurationService;
    @EJB
    private GatewaySpecialStateServiceImpl gatewaySpecialStateService;
    @EJB
    private GatewaySpecialStateScheduler gatewaySpecialStateScheduler;
    @EJB
    private MapServiceImpl mapService;
    @EJB
    private CommentServiceImpl commentService;
    @EJB
    private PreRequestGatewayServiceImpl preRequestGatewayService;

    /**
     * User Service Implementation
     */
    @POST
    @Path("/authenticate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String authenticate(User userEntity) {
        User user;
        user = userService.authenticate(userEntity.getUsername(), userEntity.getPassword());
        try {
            if (user == null)
                return new ObjectMapper().writeValueAsString(new User("null", "null", "null"));
            String result = new ObjectMapper().writeValueAsString(user);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Path("/existUser")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existUser(User entity) {
        try {
            if (userService.exist(entity.getUsername()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findUserById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findUserById(String id) {
        try {
            User user = userService.findById(id);
            try {
                if (user == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(user);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findUserByUserName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findUserByUserName(String user1) {
        try {
            User userList = userService.findByUsername(user1);
            try {
                if (userList == null)
                    return new ObjectMapper().writeValueAsString(new User());
                return new ObjectMapper().writeValueAsString(userList);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findAllUserByUserName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findAllUserByUserName(User user) {
        try {
            List<User> userList = userService.findAllByUsername(user);
            try {
                if (userList == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<User>());
                return new ObjectMapper().writeValueAsString(userList);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllUser() {

        try {
            List userList = userService.getAllUser();
            try {
                if (userList == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<User>());
                return new ObjectMapper().writeValueAsString(userList);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteUser(User entity) {
        try {
            return userService.deleteUser(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(User entity) {
        try {
            User user = new User(new HashSet<PC>(), entity.getUsername(), entity.getPassword(), entity.getEnable(), entity.isOnline(), new HashSet<WorkGroup>(), entity.getPerson());
            user = userService.createUser(user);
            user.getPcs().addAll(entity.getPcs());
            user.getWorkGroups().addAll(entity.getWorkGroups());
            userService.editUser(user);
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editUser")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editUser(User entity) {
        try {
            userService.editUser(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Operation Service Implementation
     */
    @POST
    @Path("/findRoleById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findRoleById(String id) {
        try {
            Role Role = roleService.findById(id);
            try {
                if (Role == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Role);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllRole")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllRole() {

        try {
            List Roles = roleService.getAllRole();
            try {
                if (Roles == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Role>());
                return new ObjectMapper().writeValueAsString(Roles);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findByPermissionType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findByPermissionType(Permission permission) {

        try {
            List permissions = permissionService.findByPermissionType(permission);
            try {
                if (permissions == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Permission>());
                return new ObjectMapper().writeValueAsString(permissions);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteRole")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRole(Role entity) {
        try {
            return roleService.deleteRole(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/deleteRoleList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRole(List<Role> entities) {
        try {
            for (Role entity : entities) {
                if (!roleService.deleteRole(entity).equalsIgnoreCase("operation.occurred"))
                    return "FALSE";
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createRole")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRole(Role entity) {
        try {
            Role role = roleService.createRole(entity);
            return new ObjectMapper().writeValueAsString(role);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editRole")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editRole(Role entity) {
        try {
            roleService.editRole(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/pureEditRole")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pureEditRole(Role entity) {
        try {
            roleService.pureEditRole(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Operation Service Implementation
     */
    @POST
    @Path("/findOperationById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findOperationById(String id) {
        try {
            Operation Operation = operationService.findById(id);
            try {
                if (Operation == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Operation);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllOperation")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllOperation() {

        try {
            List Operations = operationService.getAllOperation();
            try {
                if (Operations == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Operation>());
                return new ObjectMapper().writeValueAsString(Operations);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getOperationByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getOperationByName(Operation entity) {

        try {
            List Operations = operationService.findByName(entity);
            try {
                if (Operations == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Operation>());
                return new ObjectMapper().writeValueAsString(Operations);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteOperation")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteOperation(Operation entity) {
        try {
            return operationService.deleteOperation(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/existOperation")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existOperation(Operation entity) {
        try {
            if (operationService.exist(entity))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteOperationList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteOperation(List<Operation> entities) {
        try {
            for (Operation entity : entities) {
                operationService.deleteOperation(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createOperation")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createOperation(Operation entity) {
        try {
            Operation Operation = operationService.createOperation(entity);
            return new ObjectMapper().writeValueAsString(Operation);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editOperation")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editOperation(Operation entity) {
        try {
            operationService.editOperation(entity);
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    /**
     * WorkGroup Service Implementation
     */
    @POST
    @Path("/findWorkGroupById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findWorkGroupById(String id) {
        try {
            WorkGroup WorkGroup = workGroupService.findById(id);
            try {
                if (WorkGroup == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(WorkGroup);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllWorkGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllWorkGroup() {

        try {
            List WorkGroups = workGroupService.getAllWorkGroup();
            try {
                if (WorkGroups == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<WorkGroup>());
                return new ObjectMapper().writeValueAsString(WorkGroups);
            } catch (IOException e) {
                e.printStackTrace();
                return new ObjectMapper().writeValueAsString(new ArrayList<WorkGroup>());
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteWorkGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteWorkGroup(WorkGroup entity) {
        try {
            return workGroupService.deleteWorkGroup(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createWorkGroup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createWorkGroup(WorkGroup entity) {
        try {
            WorkGroup WorkGroup = workGroupService.createWorkGroup(entity);

            return new ObjectMapper().writeValueAsString(WorkGroup);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editWorkGroup")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editWorkGroup(WorkGroup entity) {
        try {
            workGroupService.editWorkGroup(entity);
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }


    /**
     * Zone Service Implementation
     */


    @POST
    @Path("/findZoneById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findZoneById(String id) {
        try {
            Zone Zone = zoneService.findById(id);
            try {
                if (Zone == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Zone);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllZone")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllZone() {

        try {
            List Zones = zoneService.getAllZone();
            try {
                if (Zones == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Zones);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteZone")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteZone(Zone entity) {
        try {
            return zoneService.deleteZone(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createZone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createZone(Zone entity) {
        try {
            Zone Zone = zoneService.createZone(entity);
            return new ObjectMapper().writeValueAsString(Zone);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editZone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editZone(Zone entity) {
        try {
            zoneService.editZone(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    /**
     * Gateway Service Implementation
     */


    @POST
    @Path("/findGatewayById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findGatewayById(String id) {
        try {
            Gateway Gateway = gatewayService.findById(id);
            try {
                if (Gateway == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Gateway);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findGatewayByZone")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findGatewayByZone(Zone zone) {
        try {
            List<Gateway> Gateway = gatewayService.findByZone(zone);
            try {
                return new ObjectMapper().writeValueAsString(Gateway);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }
//    @POST
//    @Path("/existGateway")
//    @Produces(MediaType.TEXT_PLAIN)
//    @Consumes(MediaType.APPLICATION_JSON)
//    public String existGateway(Gateway entity) {
//        try {
//            if (gatewayService.exist(entity))
//                return "true";
//            else
//                return "false";
//        } catch (Exception e) {
//            return "NULL";
//        }
//    }

    @POST
    @Path("/getAllGateway")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllGateway() {

        try {
            List Gateways = gatewayService.getAllGateway();
            try {
                if (Gateways == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Gateways);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllEnableGateway")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllEnableGateway() {

        try {
            List Gateways = gatewayService.getAllEnableGateway();
            try {
                if (Gateways == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Gateways);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteGateway")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteGateway(Gateway entity) {
        try {
            return gatewayService.deleteGateway(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createGateway")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGateway(Gateway entity) {
        try {
            Gateway Gateway = gatewayService.createGateway(entity);
            return new ObjectMapper().writeValueAsString(Gateway);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editGateway")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editGateway(Gateway entity) {
        try {
            gatewayService.editGateway(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Camera Service Implementation
     */


    @POST
    @Path("/findCameraById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findCameraById(String id) {
        try {
            Camera Camera = cameraService.findById(id);
            try {
                if (Camera == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Camera);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/existCamera")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existCamera(Camera entity) {
        try {
            if (cameraService.exist(entity.getIp(), entity.getId()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }
    @POST
    @Path("/CameraexistNotId")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String CameraexistNotId(Camera entity) {
        try {
            if (cameraService.existNotId(entity.getIp()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllCamera")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAllCamera() {

        try {
            List Cameras = cameraService.getAllCamera();
            try {
                if (Cameras == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(Cameras);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteCamera")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteCamera(Camera entity) {
        try {
            return cameraService.deleteCamera(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createCamera")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCamera(Camera entity) {
        try {
            Camera Camera = cameraService.createCamera(entity);
            return new ObjectMapper().writeValueAsString(Camera);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editCamera")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editCamera(Camera entity) {
        try {
            cameraService.editCamera(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Person Service Implementation
     */


    @POST
    @Path("/findPersonById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPersonById(String id) {
        try {
            Person person = personService.findById(id);
            try {
                if (person == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(person);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findPersonByOrganId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPersonByOrganId(String id) {
        try {
            List<Person> persons = personService.findByOrgan(Long.valueOf(id));
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/existPerson")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existPerson(Person entity) {
        try {
            if (personService.exist(entity.getPersonnelNo(), entity.getNationalCode()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllPerson")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPerson() {

        try {
            List persons = personService.getAllPerson();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }@POST
    @Path("/getAllPersonID")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonID() {

        try {
            List<Long> persons = personService.getAllPersonID();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }
    @POST
    @Path("/getAllPersonDataModel")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllPersonDataModel() {

        try {
            List persons = personService.getAllPersonModel();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getDeletedPerson")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDeletedPerson() {

        try {
            List persons = personService.getDeleted();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getPersonByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getPersonByName(String name) {

        try {
            List persons = personService.getPersonByName(name);
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getPersonByLastName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getPersonByLastName(String lastName) {

        try {
            List persons = personService.getPersonByLastName(lastName);
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getPersonByNationalCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getPersonByNationalCode(String nationalCode) {

        try {
            List persons = personService.getPersonByNationalCode(nationalCode);
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/personWithCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String personWithCard() {

        try {
            List<Person> persons = personService.withCard();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/personWithOutCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String personWithOutCard() {

        try {
            List<Person> persons = personService.withOutCard();
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getPersonByPersonnelNo")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getPersonByPersonnelNo(String personnelNo) {

        try {
            Person persons = personService.getPersonByPersonnelNo(personnelNo);
            try {
                if (persons == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deletePerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePerson(Person entity) {
        try {

            return personService.deletePerson(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/deletePersons")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePersons(List<Person> entities) {
        try {
            for (Person entity : entities) {
                if (!personService.deletePerson(entity).equalsIgnoreCase("operation.occurred"))
                    return personService.deletePerson(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createPerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPerson(Person entity) {
        try {
            Person person = personService.createPerson(entity);
            return new ObjectMapper().writeValueAsString(person);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editPerson")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editPerson(Person entity) {
        try {
            if (personService.editPerson(entity))
                return "true";
            return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Organ Service Implementation
     */


    @POST
    @Path("/findOrganById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findOrganById(String id) {
        try {
            Organ organ = organService.findById(id);
            try {
                if (organ == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(organ);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllOrgan() {

        try {
            List organs = organService.getAllOrgan();
            try {
                if (organs == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(organs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllActiveOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllActiveOrgan() {

        try {
            List organs = organService.getAllActiveOrgan();
            try {
                if (organs == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(organs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllActiveOrganByParent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getAllActiveOrganByParent(String parentId) {

        try {
            List organs = organService.getAllActiveOrganByParent(Long.parseLong(parentId));
            try {
                if (organs == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(organs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteOrgan(Organ entity) {
        try {
            return organService.deleteOrgan(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/existOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existOrgan(Organ entity) {
        try {
            return String.valueOf(organService.existOrgan(entity));
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createOrgan")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createOrgan(Organ entity) {
        try {
            Organ organ = organService.createOrgan(entity);
            return new ObjectMapper().writeValueAsString(organ);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editOrgan")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editOrgan(Organ entity) {
        try {
            if (organService.editOrgan(entity))
                return "true";
            else
                return "false";

        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * Job Service Implementation
     */

    @POST
    @Path("/findJobById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findJobById(String id) {
        try {
            Job job = jobService.findById(id);
            try {
                if (job == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(job);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findJobByPersonId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findByPersonId(String id) {
        try {
            Job job = jobService.findByPersonId(id);
            try {
                if (job == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(job);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllJob")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllJob() {

        try {
            List jobs = jobService.getAllJob();
            try {
                if (jobs == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(jobs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteJob")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteJob(Job entity) {
        try {
            return jobService.deleteJob(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createJob")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createJob(Job entity) {
        try {
            Job job = jobService.createJob(entity);
            return new ObjectMapper().writeValueAsString(job);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editJob")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editJob(Job entity) {
        try {
            jobService.editJob(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    /**
     * Card Service Implementation
     */


    @POST
    @Path("/findCardById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findCardById(String id) {
        try {
            Card card = cardService.findById(id);
            try {
                if (card == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(card);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findCardByPersonId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findCardByPersonId(String id) {
        try {
            List<Card> card = cardService.findByPersonId(id);
            try {
                if (card == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(card);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findCardByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findCardByCode(String code) {
        try {
            Card card = cardService.findByCode(code.replace("\"", ""));
            try {
                if (card == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(card);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllCard() {

        try {
            List cards = cardService.getAllCard();
            try {
                if (cards == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllActiveCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllActiveCard() {

        try {
            List cards = cardService.getAllActiveCard();
            try {
                if (cards == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllInvisibleCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllInvisible() {

        try {
            List cards = cardService.getAllInvisible();
            try {
                if (cards == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteCard(Card entity) {
        try {
            return cardService.deleteCard(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCard(Card entity) {
        try {
            Card card = cardService.createCard(entity);
            return new ObjectMapper().writeValueAsString(card);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editCard(Card entity) {
        try {
            if (cardService.editCard(entity))
                return "true";
            return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findEventLogById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findEventLogById(String id) {
        try {
            EventLog eventLog = eventLogService.findById(id);
            return new ObjectMapper().writeValueAsString(eventLog);
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllEventLog")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEventLog() {

        try {
            List events = eventLogService.getAllEventLog();
            try {
                if (events == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<EventLog>());
                return new ObjectMapper().writeValueAsString(events);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getEventLogByTable")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getEventLogByTable(EventLog eventLog) {

        try {
            List events = eventLogService.findByTable(eventLog);
            try {
                if (events == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<EventLog>());
                return new ObjectMapper().writeValueAsString(events);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteEventLog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteEventLog(EventLog entity) {
        try {
            return eventLogService.deleteEventLog(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createEventLog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createEventLog(EventLog entity) {
        try {
            EventLog eventLog = eventLogService.createEventLog(entity);
            return new ObjectMapper().writeValueAsString(eventLog);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editEventLog")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editEventLog(EventLog entity) {
        try {
            eventLogService.editEventLog(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findBLookupById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findBLookupById(String id) {
        try {
            BLookup bLookup = bLookupService.findById(id);
            return new ObjectMapper().writeValueAsString(bLookup);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findBLookupByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findBLookupByTitle(BLookup blookup) {
        try {
            BLookup bLookup = bLookupService.findByTitle(blookup);
            return new ObjectMapper().writeValueAsString(bLookup);
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllBLookup")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllBLookup() {

        try {
            List bLookups = bLookupService.getAllBLookup();
            try {
                if (bLookups == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<BLookup>());
                return new ObjectMapper().writeValueAsString(bLookups);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getByLookupTitle")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getByLookup(String lookupCode) {
        try {
            List bLookups = bLookupService.getByLookup(lookupCode);
            try {
                if (bLookups == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<BLookup>());
                return new ObjectMapper().writeValueAsString(bLookups);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getByLookupId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getByLookupId(String lookupId) {
        try {
            List bLookups = bLookupService.getByLookupId(lookupId);
            try {
                if (bLookups == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<BLookup>());
                return new ObjectMapper().writeValueAsString(bLookups);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/deleteBLookup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteBLookup(BLookup entity) {
        try {
            return bLookupService.deleteBLookup(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteBLookups")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteBLookup(List<BLookup> entities) {
        try {
            for (BLookup entity : entities) {
                bLookupService.deleteBLookup(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createBLookup")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createBLookup(BLookup entity) {
        try {
            BLookup bLookup = bLookupService.createBLookup(entity);
            return new ObjectMapper().writeValueAsString(bLookup);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editBLookup")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editBLookup(BLookup entity) {
        try {
            bLookupService.editBLookup(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findLookupById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findLookupById(String id) {
        try {
            Lookup lookup = lookupService.findById(id);
            return new ObjectMapper().writeValueAsString(lookup);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findLookupByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findLookupByTitle(String title) {
        try {
            Lookup lookup = lookupService.findByCode(title);
            return new ObjectMapper().writeValueAsString(lookup);
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllLookup")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllLookup() {
        try {
            List allLookup = lookupService.getAllLookup();
            try {
                if (allLookup == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<BLookup>());
                return new ObjectMapper().writeValueAsString(allLookup);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllDefinableLookup")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllDefinableLookup() {
        try {
            List allLookup = lookupService.getAllDefinableLookup();
            try {
                if (allLookup == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<BLookup>());
                return new ObjectMapper().writeValueAsString(allLookup);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/findCalendarById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findCalendarById(String id) {
        try {
            Calendar calendar = calendarService.findById(id);
            return new ObjectMapper().writeValueAsString(calendar);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findCalendarByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findCalendarByCode(Calendar calendar) {
        try {
            List calendars = calendarService.findByCode(calendar);
            try {
                if (calendars == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Calendar>());
                return new ObjectMapper().writeValueAsString(calendars);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findCalendarByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findCalendarByName(Calendar calendar) {
        try {
            List calendars = calendarService.findByName(calendar);
            try {
                if (calendars == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Calendar>());
                return new ObjectMapper().writeValueAsString(calendars);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllCalendar")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllCalendar() {

        try {
            List calendars = calendarService.getAllCalendar();
            try {
                if (calendars == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Calendar>());
                return new ObjectMapper().writeValueAsString(calendars);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findDefaultCalendar")
    @Produces(MediaType.APPLICATION_JSON)
    public String findDefaultCalendar() {
        try {
            List calendars = calendarService.findDefault();
            try {
                if (calendars == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Calendar>());
                return new ObjectMapper().writeValueAsString(calendars);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteCalendar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteCalendar(Calendar entity) {
        try {
            return calendarService.deleteCalendar(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteCalendars")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteCalendar(List<Calendar> entities) {
        try {
            for (Calendar entity : entities) {
                calendarService.deleteCalendar(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createCalendar")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCalendar(Calendar entity) {
        try {
            Calendar calendar = calendarService.createCalendar(entity);
            return new ObjectMapper().writeValueAsString(calendar);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editCalendar")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editCalendar(Calendar entity) {
        try {
            calendarService.editCalendar(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     * language services
     */
    @POST
    @Path("/findLanguageById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findLanguageById(String id) {
        try {
            Languages Language = languageService.findById(id);
            return new ObjectMapper().writeValueAsString(Language);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findLanguageByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findLanguageByName(String name) {
        try {
            Languages Language = languageService.findByName(name);
            return new ObjectMapper().writeValueAsString(Language);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllLanguage")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllLanguage() {

        try {
            List language = languageService.getAllLanguage();
            try {
                if (language == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Languages>());
                return new ObjectMapper().writeValueAsString(language);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteLanguage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteLanguage(Languages entity) {
        try {
            return languageService.deleteLanguage(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createLanguage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createLanguage(Languages entity) {
        try {
            Languages Language = languageService.createLanguage(entity);
            return new ObjectMapper().writeValueAsString(Language);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editLanguage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editLanguage(Languages entity) {
        try {
            languageService.editLanguage(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findLanguageManagementById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findLanguageManagementById(String id) {
        try {
            LanguageManagement LanguageManagement = languageManagementService.findById(id);
            return new ObjectMapper().writeValueAsString(LanguageManagement);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllLanguageManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllLanguageManagement() {

        try {
            List management = languageManagementService.getAllLanguageManagement();
            try {
                if (management == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<LanguageManagement>());
                return new ObjectMapper().writeValueAsString(management);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/deleteLanguageManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteLanguageManagement(LanguageManagement entity) {
        try {
            return languageManagementService.deleteLanguageManagement(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/loadLanguage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String loadLanguage(String locale) {
        try {
            Hashtable<String, LanguageManagement> lang = languageService.loadLanguage(locale);
            return new ObjectMapper().writeValueAsString(lang);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createLanguageManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createLanguageManagement(LanguageManagement entity) {
        try {
            LanguageManagement LanguageManagement = languageManagementService.createLanguageManagement(entity);
            return new ObjectMapper().writeValueAsString(LanguageManagement);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editLanguageManagement")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editLanguageManagement(LanguageManagement entity) {
        try {
            languageManagementService.editLanguageManagement(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/editLanguageManagementList")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editLanguageManagement(List<LanguageManagement> entities) {
        try {
            languageManagementService.editLanguageManagements(entities);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findLanguageKeyManagementById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findLanguageKeyManagementById(String id) {
        try {
            LanguageKeyManagement LanguageKeyManagement = languageKeyManagementService.findById(id);
            return new ObjectMapper().writeValueAsString(LanguageKeyManagement);
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllLanguageKeyManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllLanguageKeyManagement() {

        try {
            List keyManagement = languageKeyManagementService.getAllLanguageKeyManagement();
            try {
                if (keyManagement == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<LanguageKeyManagement>());
                return new ObjectMapper().writeValueAsString(keyManagement);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteLanguageKeyManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteLanguageKeyManagement(LanguageKeyManagement entity) {
        try {
            return languageKeyManagementService.deleteLanguageKeyManagement(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createLanguageKeyManagement")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createLanguageKeyManagement(LanguageKeyManagement entity) {
        try {
            LanguageKeyManagement LanguageKeyManagement = languageKeyManagementService.createLanguageKeyManagement(entity);
            return new ObjectMapper().writeValueAsString(LanguageKeyManagement);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editLanguageKeyManagement")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editLanguageKeyManagement(LanguageKeyManagement entity) {
        try {
            languageKeyManagementService.editLanguageKeyManagement(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findPCById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPCById(String id) {
        try {
            PC pc = pcService.findById(id);
            return new ObjectMapper().writeValueAsString(pc);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllPC")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllPc() {

        try {
            List pcs = pcService.getAllPCs();
            try {
                if (pcs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<PC>());
                return new ObjectMapper().writeValueAsString(pcs);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findPCByName")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findPCByName(PC pc) {
        try {
            List<PC> pcSearch = pcService.findByName(pc);
            if (pcSearch == null)
                return new ObjectMapper().writeValueAsString(new ArrayList<PC>());
            return new ObjectMapper().writeValueAsString(pcSearch);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findPCByIP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findPCByIP(PC pc) {
        try {
            PC pcSearch = pcService.findByIp(pc);
            if (pcSearch == null)
                return "NULL";
            return new ObjectMapper().writeValueAsString(pcSearch);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deletePC")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePC(PC entity) {
        try {
            return pcService.deletePC(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/deletePCs")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePCs(List<PC> entities) {
        try {
            for (PC entity : entities) {
                if (!pcService.deletePC(entity).equalsIgnoreCase("operation.occurred"))
                    return "FALSE";
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createPC")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPC(PC entity) {
        try {
            PC pc = pcService.createPC(entity);
            return new ObjectMapper().writeValueAsString(pc);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editPC")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editPC(PC entity) {
        try {
            pcService.editPC(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editPCs")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editPCs(List<PC> entities) {
        try {
            for (PC entity : entities) {
                pcService.editPC(entity);
            }
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/existPC")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existPC(PC entity) {
        try {
            if (pcService.exist(entity.getIp(), entity.getId()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }
    @POST
    @Path("/PCexistNotId")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String PCexistNotId(PC entity) {
        try {
            if (pcService.existNotId(entity.getIp()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/PCMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String PCMaximumId() {
        try {
            return String.valueOf(pcService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/personMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String personMaximumId() {
        try {
            return String.valueOf(personService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/userMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String userMaximumId() {
        try {
            return String.valueOf(userService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/roleMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String roleMaximumId() {
        try {
            return String.valueOf(roleService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/workGroupMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String workGroupMaximumId() {
        try {
            return String.valueOf(workGroupService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/languageManagementMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String languageManagementMaximumId() {
        try {
            return String.valueOf(languageManagementService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/languageKeyManagementMaximumId")
    @Produces(MediaType.TEXT_PLAIN)
    public String languageKeyManagementMaximumId() {
        try {
            return String.valueOf(languageKeyManagementService.getMaximumId());
        } catch (Exception e) {
            return "NULL";
        }
    }


    /**
     * day type
     */
    @POST
    @Path("/findDayTypeById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findDayTypeById(String id) {
        try {
            DayType dayType = dayTypeService.findById(id);
            return new ObjectMapper().writeValueAsString(dayType);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllDayType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllDayType() {

        try {
            List dayTypes = dayTypeService.getAllDayType();
            try {
                if (dayTypes == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<DayType>());
                return new ObjectMapper().writeValueAsString(dayTypes);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteDayType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteDayType(DayType entity) {
        try {
            return dayTypeService.deleteDayType(entity);
        } catch (Exception e) {
            return "false";
        }
    }

    @POST
    @Path("/deleteDayTypes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteDayTypes(List<DayType> entities) {
        try {
            for (DayType entity : entities) {
                if (!dayTypeService.deleteDayType(entity).equalsIgnoreCase("operation.occurred"))
                    return dayTypeService.deleteDayType(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createDayType")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createDayType(DayType entity) {
        try {
            DayType dayType = dayTypeService.createDayType(entity);
            return new ObjectMapper().writeValueAsString(dayType);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editDayType")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editDayType(DayType entity) {
        try {
            dayTypeService.editDayType(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editDayTypes")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editDayTypes(List<DayType> entities) {
        try {
            for (DayType entity : entities) {
                dayTypeService.editDayType(entity);
            }
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    /**
     * RulePackage
     */
    @POST
    @Path("/findRulePackageById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findRulePackageById(String id) {
        try {
            RulePackage rulePackage = rulePackageService.findById(id);
            return new ObjectMapper().writeValueAsString(rulePackage);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllRulePackage")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllRulePackage() {

        try {
            List allRulePackage = rulePackageService.getAllRulePackage();
            try {
                if (allRulePackage == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<RulePackage>());
                return new ObjectMapper().writeValueAsString(allRulePackage);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findRulePackageByCalendarID")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findByCalendarID(String id) {

        try {
            List allRulePackage = rulePackageService.findByCalendarID(id);
            try {
                if (allRulePackage == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<RulePackage>());
                return new ObjectMapper().writeValueAsString(allRulePackage);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteRulePackage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRulePackage(RulePackage entity) {
        try {
            return rulePackageService.deleteRulePackage(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/deleteRulePackages")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRulePackages(List<RulePackage> entities) {
        try {
            for (RulePackage entity : entities) {
                if (!rulePackageService.deleteRulePackage(entity).equalsIgnoreCase("operation.occurred"))
                    return rulePackageService.deleteRulePackage(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/createRulePackage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRulePackage(RulePackage entity) {
        try {
            RulePackage rulePackage = rulePackageService.createRulePackage(entity);
            return new ObjectMapper().writeValueAsString(rulePackage);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editRulePackage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editRulePackage(RulePackage entity) {
        try {
            rulePackageService.editRulePackage(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/pureEditRulePackage")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String pureEditRulePackage(RulePackage entity) {
        try {
            rulePackageService.pureEditRulePackage(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/fillRulePackageHashTable")
    public void fillRulePackageHashTable(String id) {
        try {
            rulePackageService.fillRulePackageHashTable();
        } catch (Exception e) {
        }
    }

    @POST
    @Path("/editRulePackages")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editRulePackages(List<RulePackage> entities) {
        try {
            for (RulePackage entity : entities) {
                rulePackageService.editRulePackage(entity);
            }
            return "true";
        } catch (Exception e) {
            return "NULL ";
        }
    }

    @POST
    @Path("/newCalendar")
    @Produces(MediaType.TEXT_PLAIN)
    public String getCalendar() {
        return calendarService.newCalendar();
    }

    @POST
    @Path("/getEditCalendar")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getEditCalendar(String id) {
        try {
            System.out.println("8888>>>>>>>>>>>>>>>>>>>>>" + id);
            String content = new String(calendarService.findById(id).getContent(), "utf-8");
            System.out.println(content);
            return content;

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "NULL";

    }

    @POST
    @Path("/getEditCalendarByYear")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String getEditCalendarByYear(String year) {
        return calendarService.newCalendar().replace("2014", year);
    }


//    @POST
//    @Path("/getFilledCalendars")
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getFilledCalendars() {
//
//        try {
//            try {
//                if (Initializer.calendars == null) {
//                    List<Calendar> calendarList = calendarService.getAllCalendar();
//
//                    return new ObjectMapper().writeValueAsString(calendarList);
//                }
//                return new ObjectMapper().writeValueAsString(Initializer.calendars);
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            }
//        } catch (Exception e) {
//            return null;
//        }
//    }

    @POST
    @Path("/getRulesByRulePackage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getRulesByRulePackage(String rulePackageId) {

        try {
            List list = ruleService.getByRulePackageId(Long.valueOf(rulePackageId));
            try {
                if (list == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Rule>());
                return new ObjectMapper().writeValueAsString(list);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getRuleExceptionsByRulePackage")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getRuleExceptionsByRulePackage(String rulePackageId) {

        try {
            List list = ruleExceptionService.getByRulePackageId(Long.valueOf(rulePackageId));
            try {
                if (list == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<RuleException>());
                return new ObjectMapper().writeValueAsString(list);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getRuleExceptionsByRulePackageAndId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getRuleExceptionsByRulePackageAndId(String rulePackageId, String id) {

        try {
            RuleException ruleException = ruleExceptionService.getByRulePackageIdAndId(Long.valueOf(rulePackageId), Long.valueOf(id));
            return new ObjectMapper().writeValueAsString(ruleException);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createRuleException")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRuleException(RuleException entity) {
        try {
            RuleException ruleException = ruleExceptionService.createRuleException(entity);
            return new ObjectMapper().writeValueAsString(ruleException);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editRuleException")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editRuleException(RuleException entity) {
        try {
            ruleExceptionService.editRuleException(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editPureRuleException")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editPureRuleException(RuleException entity) {
        try {
            ruleExceptionService.editPureRuleException(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteRuleException")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRuleException(RuleException entity) {
        try {
            return ruleExceptionService.deleteRuleException(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }


    @POST
    @Path("/createRule")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRule(Rule entity) {
        try {
            Rule rule = ruleService.createRule(entity);
            return new ObjectMapper().writeValueAsString(rule);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteRule")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteRule(List<Rule> entities) {
        try {
            for (Rule entity : entities) {
                if (!ruleService.deleteRule(entity).equalsIgnoreCase("operation.occurred"))
                    return "FALSE";
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    /**
     * Operation Service Implementation
     */
    @POST
    @Path("/findTrafficLogById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findTrafficLogById(String id) {
        try {
            TrafficLog TrafficLog = trafficLogService.findById(id);
            try {
                if (TrafficLog == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(TrafficLog);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllTrafficLog")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTrafficLog() {

        try {
            List TrafficLogs = trafficLogService.getAllTrafficLog();
            try {
                if (TrafficLogs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(TrafficLogs);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getTrafficByQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getTrafficByQuery(String query) {

        try {
            List TrafficLogs = trafficLogService.getTrafficByQuery(query);
            try {
                if (TrafficLogs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(TrafficLogs);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findByPersonLocationInDuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findByPersonLocationInDuration(String data) {
        try {
            String[] spilitedData = data.split("#");
            try {
                List<TrafficLog> logs = trafficLogService.findByPersonLocationInDuration(Long.valueOf(spilitedData[0]), spilitedData[1], spilitedData[2], CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findTrafficInDuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findTrafficInDuration(String data) {
        try {
            String[] spilitedData = data.split("#");
            try {
                List<TrafficLog> logs = trafficLogService.findTrafficInDuration(spilitedData[0].replace("/", ""), spilitedData[1].replace("/", ""));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findEventInDuration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findEventInDuration(String data) {
        try {
            String[] spilitedData = data.split("#");
            try {
                List<EventLog> logs = eventLogService.findEventInDuration(spilitedData[0].replace("/", ""), spilitedData[1].replace("/", ""));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<EventLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findEventBeforeDate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findEventBeforeDate(String data) {
        try {
            try {
                List<EventLog> logs = eventLogService.findEventBeforeDate(data.replace("/", ""));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<EventLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findEventAfterDate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findEventAfterDate(String data) {
        try {
            try {
                List<EventLog> logs = eventLogService.findEventAfterDate(data.replace("/", ""));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<EventLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findByPersonAndGate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findByPersonAndGate(String ids) {
        try {
            try {
                String[] id = ids.split("#");
                List<TrafficLog> logs = trafficLogService.findByPersonAndGate(Long.valueOf(id[0]), Long.valueOf(id[1]), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findTrafficByGate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findTrafficByGate(String id) {
        try {
            try {
                List<TrafficLog> logs = trafficLogService.findByGate(Long.valueOf(id), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }    @POST
    @Path("/findTrafficByPDP")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findTrafficByPDP(String id) {
        try {
            try {
                List<TrafficLog> logs = trafficLogService.findByPDP(Long.valueOf(id), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
                if (logs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<TrafficLog>());
                return new ObjectMapper().writeValueAsString(logs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findTrafficLogByPersonId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findLastByPerson(String personId) {
        try {
            List<TrafficLog> TrafficLog = trafficLogService.findByPerson(Long.valueOf(personId), CalendarUtil.getPersianDateWithoutSlash(new Locale("fa")));
            try {
                if (TrafficLog == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<ir.university.toosi.tms.model.entity.TrafficLog>());
                return new ObjectMapper().writeValueAsString(TrafficLog);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteTrafficLog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteTrafficLog(TrafficLog entity) {
        try {
            return trafficLogService.deleteTrafficLog(entity);
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/deleteTrafficLogList")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteTrafficLog(List<TrafficLog> entities) {
        try {
            for (TrafficLog entity : entities) {
                if (!trafficLogService.deleteTrafficLog(entity).equalsIgnoreCase("operation.occurred"))
                    return "FALSE";
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "FALSE";
        }
    }

    @POST
    @Path("/TestSaman")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String testSaman() {
        try {
            Camera camera = new Camera();
            camera.setIp("192.168.0.99");
//            photoService.takePhoto(camera, 5, 200, "aaa");
            return "true";
        } catch (Exception e) {
            return "false";
        }
    }

    @POST
    @Path("/createTrafficLog")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createTrafficLog(TrafficLog entity) {
        try {
            TrafficLog TrafficLog = trafficLogService.createTrafficLog(entity);
            return new ObjectMapper().writeValueAsString(TrafficLog);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editTrafficLog")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editTrafficLog(TrafficLog entity) {
        try {
            trafficLogService.editTrafficLog(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    /**
     * PDP Service Implementation
     */
    @POST
    @Path("/existPDP")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String existPDP(PDP entity) {
        try {
            if (pdpService.exist(entity.getIp(), entity.getId()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }
    @POST
    @Path("/PDPexistNotId")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String PDPexistNotId(PDP entity) {
        try {
            if (pdpService.existNotId(entity.getIp()))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/findPdpById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPdpById(String id) {
        try {
            PDP PDP = pdpService.findById(id);
            try {
                if (PDP == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(PDP);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findPdpByGatewayId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPdpByGatewayId(String id) {
        try {
            List<PDP> PDP = pdpService.findByGatewayId(id);
            try {
                if (PDP == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<>());
                return new ObjectMapper().writeValueAsString(PDP);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findPdpByCameraId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPdpByCameraId(String id) {
        try {
            List<PDP> PDP = pdpService.findByCameraId(id);
            try {
                if (PDP == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(PDP);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findPdpByIp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPdpByIp(String ip) {
        try {
            PDP PDP = pdpService.findByIp(ip);
            try {
                if (PDP == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(PDP);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/ping")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String ping(String ip) {
        try {
            if (pdpService.ping(ip.replace("\"", "")))
                return "true";
            else
                return "false";
        } catch (Exception e) {
            return "false";
        }
    }


    @POST
    @Path("/getAllPdp")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllPdp() {

        try {
            List PDPs = pdpService.getAllPDPs();
            try {
                if (PDPs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<PDP>());
                return new ObjectMapper().writeValueAsString(PDPs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }
    @POST
    @Path("/getAllPdpbyIDs")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllPdpbyIDs(List<Long> ids) {

        try {
            List PDPs = pdpService.getAllPdpbyIDs(ids);
            try {
                if (PDPs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<PDP>());
                return new ObjectMapper().writeValueAsString(PDPs);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deletePdp")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deletePdp(PDP entity) {
        try {
            return pdpService.deletePDP(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createPdp")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPdp(PDP entity) {
        try {
            PDP PDP = pdpService.createPDP(entity);
            return new ObjectMapper().writeValueAsString(PDP);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editPdp")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editPdp(PDP entity) {
        try {
            if (pdpService.editPDP(entity))
                return "true";
            return "false";

        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/synchronizePdp")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String synchronizePdp(PDPSync pdpSync) {
        try {
            if (pdpService.synchronizePdp(pdpSync))
                return "true";
            else
                return "false";

        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/synchronizeOnePdp")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String synchronizeOnePdp(PDPSync pdpSync) {
        try {
            if (pdpService.synchronizeOnePdp(pdpSync))
                return "true";
            else
                return "false";

        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getFinger")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String fingerPrint(Set<PDP> pdpSet) {
        try {
            if (pdpService.fingerPrint(pdpSet))
                return "true";
            else
                return "false";

        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/ruleInitialize")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String ruleInitialize() {
        try {
            rulePackageService.fillRulePackageHashTable();
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/specialStatusInitialize")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String specialStatusInitialize() {
        try {
            gatewaySpecialStateScheduler.stopService();
            gatewaySpecialStateScheduler.startService();
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/authenticatePDP")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String authenticatePDP(String content) {
        try {
            String[] splited = content.split("#");
            PDP pdp = pdpService.findByIp(splited[1]);
            Person person = personService.getPersonByPersonOtherId(splited[0]);
            if (person == null || pdp == null)
                return "false";
            List<Card> cards = cardService.findByPersonId(String.valueOf(person.getId()));
//            if (crossingService.authorize(pdp, cards.get(0)))
//                return "true";
            return "false";
        } catch (Exception e) {
            return "false";
        }
    }

    @POST
    @Path("/getAllCalendarDate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllCalendarDate() {

        try {
            List CalendarDates = calendarDateService.getAllCalendarDate();
            try {
                if (CalendarDates == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(CalendarDates);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findCalendarDateByCalendarId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findCalendarDateByCalendarId(String id) {

        try {
            List CalendarDates = calendarDateService.findByCalendarID(id);
            try {
                if (CalendarDates == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(CalendarDates);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllRuleException")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllRuleException() {

        try {
            List allRuleException = ruleExceptionService.getAllRuleException();
            try {
                if (allRuleException == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(allRuleException);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteCalendarDate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteCalendarDate(CalendarDate entity) {
        try {
            return calendarDateService.deleteCalendarDate(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createCalendarDate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCalendarDate(CalendarDate entity) {
        try {
            CalendarDate CalendarDate = calendarDateService.createCalendarDate(entity);
            return new ObjectMapper().writeValueAsString(CalendarDate);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editCalendarDate")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editCalendarDate(CalendarDate entity) {
        try {
            calendarDateService.editCalendarDate(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    //

    @POST
    @Path("/deleteGatewayPerson")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String deleteGatewayPerson(String id) {
        try {
            return gatewayPersonService.deleteGatewayPerson(id.split("#")[0],id.split("#")[1]);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createGatewayPerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGatewayPerson(GatewayPerson entity) {
        try {
            GatewayPerson GatewayPerson = gatewayPersonService.createGatewayPerson(entity);
            return new ObjectMapper().writeValueAsString(GatewayPerson);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editGatewayPerson")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editGatewayPerson(GatewayPerson entity) {
        try {
            gatewayPersonService.editGatewayPerson(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findGatewayPersonByGatewayId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findGatewayPersonByGatewayId(String id) {

        try {
            List GatewayPerson = gatewayPersonService.findByGatewayId(id);
            try {
                if (GatewayPerson == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(GatewayPerson);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findGatewayPersonByPersonId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findGatewayPersonByPersonId(String id) {

        try {
            List GatewayPerson = gatewayPersonService.findByPersonId(id);
            try {
                if (GatewayPerson == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(GatewayPerson);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createSystemConfiguration")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSystemConfiguration(SystemConfiguration entity) {
        try {
            SystemConfiguration configuration = SystemParameterManager.updateSystemConfiguration(systemConfigurationService, entity);
            return new ObjectMapper().writeValueAsString(configuration);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editSystemConfiguration")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editSystemConfiguration(SystemConfiguration entity) {
        try {
            SystemConfiguration configuration = SystemParameterManager.updateSystemConfiguration(systemConfigurationService, entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteGatewaySpecialStatus")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteGatewaySpecialStatus(GatewaySpecialState entity) {
        try {
            return gatewaySpecialStateService.deleteGatewaySpecialState(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getAllConfiguration")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllConfiguration() {

        try {
            List allConfiguration = systemConfigurationService.getAllConfiguration();
            try {
                if (allConfiguration == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(allConfiguration);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/getSystemConfiguration")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getSystemConfiguration(SystemParameterType entity) {
        try {
            String value = SystemParameterManager.getSystemConfigurationStringHashtable().get(entity);
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findGatewaySpecialStateByGatewayId")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findGatewaySpecialStateByGatewayId(String id) {

        try {
            List<GatewaySpecialState> gatewaySpecialStates = gatewaySpecialStateService.findByGatewayId(id);
            try {
                if (gatewaySpecialStates == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(gatewaySpecialStates);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createGatewaySpecialState")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createGatewaySpecialState(GatewaySpecialState entity) {
        try {
            GatewaySpecialState gatewaySpecialState = gatewaySpecialStateService.createGatewaySpecialState(entity);
            return new ObjectMapper().writeValueAsString(gatewaySpecialState);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editGatewaySpecialState")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editGatewaySpecialState(GatewaySpecialState entity) {
        try {
            gatewaySpecialStateService.editGatewaySpecialState(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/getAllGatewaySpecialState")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String getAllGatewaySpecialState() {

        try {
            List<GatewaySpecialState> gatewaySpecialStates = gatewaySpecialStateService.getAllGatewaySpecialState();
            try {
                if (gatewaySpecialStates == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(gatewaySpecialStates);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/findMapById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findMapById(String id) {
        try {
            Map map = mapService.findById(id);
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findMapByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findMapByCode(Map map) {
        try {
            List maps = mapService.findByCode(map);
            try {
                if (maps == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Map>());
                return new ObjectMapper().writeValueAsString(maps);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllMap")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllMap() {

        try {
            List maps = mapService.getAllMap();
            try {
                if (maps == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Map>());
                return new ObjectMapper().writeValueAsString(maps);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteMap")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteMap(Map entity) {
        try {
            return mapService.deleteMap(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createMap")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createMap(Map entity) {
        try {
            Map map = mapService.createMap(entity);
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/saveMap")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String saveMap(Map entity) {
        try {
            Map map = mapService.createMap(entity);
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editMap")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editMap(Map entity) {
        try {
            mapService.editMap(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    /**
     *
     *
     *
     */

    @POST
    @Path("/getAllComment")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllComment() {

        try {
            List maps = commentService.getAllComment();
            try {
                if (maps == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Comment>());
                return new ObjectMapper().writeValueAsString(maps);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findByEffectorUser")
    @Produces(MediaType.APPLICATION_JSON)
    public String findByEffectorUser(String userName) {

        try {
            List maps = commentService.findByEffectorUser(userName);
            try {
                if (maps == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Comment>());
                return new ObjectMapper().writeValueAsString(maps);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/getAllAuthorizeComment")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllAuthorizeComment() {

        try {
            List maps = commentService.getAllAuthorizeComment();
            try {
                if (maps == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Calendar>());
                return new ObjectMapper().writeValueAsString(maps);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/deleteComment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteComment(Comment entity) {
        try {
            return commentService.deleteComment(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/rejectComment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String rejectComment(Comment entity) {
        try {
            return commentService.rejectComment(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createComment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createComment(Comment entity) {
        try {
            Comment map = commentService.createComment(entity);
            return new ObjectMapper().writeValueAsString(map);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editComment")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editComment(Comment entity) {
        try {
            commentService.editComment(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/findByTrafficLog")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public String findByTrafficLog(String id) {
        try {
            Comment comment = commentService.findByTrafficLog(Long.valueOf(id));
            if (comment != null)
                return new ObjectMapper().writeValueAsString(comment);
            else {
                comment = new Comment();
                comment.setMessage("");
                return new ObjectMapper().writeValueAsString(comment);
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/alert")
    @Produces(MediaType.TEXT_XML)
    public String alert() {
        try {
            return mapService.alert();
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/initMap")
    @Produces(MediaType.TEXT_XML)
    public String initMap() {
        try {
            String s = mapService.init();
            System.out.println(s);
            return s;
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/viewMap")
    @Produces(MediaType.TEXT_PLAIN)
    public String viewMap() {
        try {

            return new String(((Map) mapService.getAllMap().get(0)).getContent(), "utf-8");
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/queryView")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String queryView(String query) {

        try {
            List TrafficLogs = trafficLogService.queryView(query);
            try {
                if (TrafficLogs == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<ReportEntity>());
                return new ObjectMapper().writeValueAsString(TrafficLogs);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/queryCountView")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String queryCountView(String query) {

        try {
            Long result = trafficLogService.queryCountView(query);
            try {
                return new ObjectMapper().writeValueAsString(result);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/queryPerson")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String queryPerson(String query) {

        try {
            List persons = personService.query(query);
            try {
                if (persons == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Person>());
                System.out.println(persons.size());
                return new ObjectMapper().writeValueAsString(persons);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/assignCard")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String assignCard(String dates) {

        try {
            List cards = cardService.assign(dates.split("#")[0], dates.split("#")[1]);
            try {
                if (cards == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Card>());
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/closedCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String closedCard() {

        try {
            List cards = cardService.closed();
            try {
                if (cards == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Card>());
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/openCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String openCard() {

        try {
            List cards = cardService.open();
            try {
                if (cards == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Card>());
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/lostCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String lostCard() {

        try {
            List cards = cardService.lost();
            try {
                if (cards == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Card>());
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/stolenCard")
    @Produces(MediaType.APPLICATION_JSON)
    public String stolenCard() {

        try {
            List cards = cardService.stolen();
            try {
                if (cards == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<Card>());
                return new ObjectMapper().writeValueAsString(cards);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/findSavedQueryById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findSavedQueryById(String id) {
        try {
            SavedQuery savedQuery = savedQueryService.findById(id);
            return new ObjectMapper().writeValueAsString(savedQuery);
        } catch (Exception e) {
            return null;
        }
    }

    @POST
    @Path("/findSavedQueryByCode")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String findSavedQueryByTitle(SavedQuery query) {
        try {
            SavedQuery savedQuery = savedQueryService.findByTitle(query);
            return new ObjectMapper().writeValueAsString(savedQuery);
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getAllSavedQuery")
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllSavedQuery() {

        try {
            List savedQuerys = savedQueryService.getAllSavedQuery();
            try {
                if (savedQuerys == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<SavedQuery>());
                return new ObjectMapper().writeValueAsString(savedQuerys);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/getScheduledQuery")
    @Produces(MediaType.APPLICATION_JSON)
    public String getScheduledQuery() {

        try {
            List scheduledQuerys = savedQueryService.findScheduled();
            try {
                if (scheduledQuerys == null)
                    return new ObjectMapper().writeValueAsString(new ArrayList<SavedQuery>());
                return new ObjectMapper().writeValueAsString(scheduledQuerys);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }


    @POST
    @Path("/deleteSavedQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteSavedQuery(SavedQuery entity) {
        try {
            return savedQueryService.deleteSavedQuery(entity);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/deleteSavedQuerys")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String deleteSavedQuery(List<SavedQuery> entities) {
        try {
            for (SavedQuery entity : entities) {
                savedQueryService.deleteSavedQuery(entity);
            }
            return new ObjectMapper().writeValueAsString("operation.occurred");
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/createSavedQuery")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createSavedQuery(SavedQuery entity) {
        try {
            SavedQuery savedQuery = savedQueryService.createSavedQuery(entity);
            return new ObjectMapper().writeValueAsString(savedQuery);
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/editSavedQuery")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_JSON)
    public String editSavedQuery(SavedQuery entity) {
        try {
            savedQueryService.editSavedQuery(entity);
            return "true";
        } catch (Exception e) {
            return "NULL";
        }
    }


    @POST
    @Path("/findPreGatewayById")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String findPreGatewayById(String id) {
        try {
            PreRequestGateway requestGateway = preRequestGatewayService.findById(id);
            try {
                if (requestGateway == null)
                    return "NULL";
                return new ObjectMapper().writeValueAsString(requestGateway);
            } catch (IOException e) {
                e.printStackTrace();
                return "NULL";
            }
        } catch (Exception e) {
            return "NULL";
        }
    }

    @POST
    @Path("/forceOpen")
    @Consumes(MediaType.TEXT_PLAIN)
    public void forceOpen(String id) {

        gatewayService.forceOpen(pdpService.findById(id).getIp());

    }

    @POST
    @Path("/backUpDerby")
    @Consumes(MediaType.TEXT_PLAIN)
    public void backUpDerby(String type) {

        try {
            operationService.backupDerby();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
