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
//            String s="<element error=\"Load\" status=\"true\" operation=\"Initialize\"><collections><number><alert><number color=\"#fff\" animate=\"\" _id=\"1\" name=\"popup\" num=\"5\" nodeheight=\"0\" nodex=\"0\" nodez=\"12\" translationx=\"0\" translationy=\"0\" translationz=\"6\" switchduration=\"2000\" baseduration=\"5000\" viewface=\"Camera_lock\"><pattern type=\"Default\"><number pattern=\"true\" def=\"foo\" rotation=\"Auto\" translation=\"Auto\" scale=\"1 1  1\" nodename=\"transform\"></number></pattern><node type=\"Default\"><number nodename=\"transform\" scale=\"1 1 1\" translation=\"Auto\" rotation=\"0.999977302559263   0 0.00673753414091492 1.5737577306294326\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"false\" def=\"float\"><number nodename=\"Appearance\"><material nodename=\"Material\" diffusecolor=\"{$alertdiffusecolor}\" specularcolor=\".1 .1 .1\"></material></number><number nodename=\"Box\" size=\"32 12 0\"></number></number><number nodename=\"transform\" scale=\"1 1  1\" translation=\"4.4 3.3 0.24\" rotation=\"0.9999892642023637 1.5612488335498067 0 0.004633732838142135\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"false\"><number nodename=\"Appearance\"><number nodename=\"Material\" ambientintensity=\"0.0933\" diffusecolor=\"0.06 0.06 0.06\" shininess=\"0.100\" specularcolor=\"0.06 0.06 0.06\"></number></number><number nodename=\"Text\" string=\"&quot;{$alertname}&quot;\" solid=\"true\"><number nodename=\"FontStyle\" justify=\"&quot;Middle&quot; &quot;RIGHT&quot;\" text_align=\"RIGHT\" lefttoright=\"false\" family=\"'Arial'\" style=\"BOLD\" size=\"10.5\" horizontal=\"false\" spacing=\"0\"></number></number></number></number><number nodename=\"transform\" scale=\"1 1 1\" translation=\"-0.6 1.1 0.23\" rotation=\"0.9999892642023637 1.5612488335498067 0 0.004633732838142135\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\"><number nodename=\"Appearance\"><number nodename=\"Material\" ambientintensity=\"0.0933\" diffusecolor=\"0.06 0.06 0.06\" shininess=\"0.100\" specularcolor=\"0.06 0.06 0.06\"></number></number><number nodename=\"Text\" string=\"&quot;{$alertlastname}&quot; \" solid=\"true\"><number nodename=\"FontStyle\" justify=\"&quot;Middle&quot; &quot;RIGHT&quot;\" text_align=\"RIGHT\" lefttoright=\"false\" family=\"'Arial'\" style=\"BOLD\" size=\"10.5\" horizontal=\"false\" spacing=\"0\"></number></number></number></number><number nodename=\"transform\" scale=\"1 1 1\" translation=\"-2.2 -1.5 0.28\" rotation=\"1 1.5612488335498067 0 0.004633732838142135\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"false\"><number nodename=\"Appearance\"><number nodename=\"Material\" ambientintensity=\"0.0933\" diffusecolor=\"0.06 0.06 0.06\" shininess=\"0.100\" specularcolor=\"0.06 0.06 0.06\"></number></number><number nodename=\"Text\" string=\"&quot;{$alertpersonnum}&quot; \" solid=\"true\"><number nodename=\"FontStyle\" justify=\"&quot;END&quot;\" lefttoright=\"false\" family=\"'Arial'\" style=\"BOLD\" size=\"10.5\" horizontal=\"true\" spacing=\"1\" toptobottom=\"false\"></number></number></number></number><number nodename=\"transform\" scale=\"1 1 1\" translation=\"-1.2 -4 0.21\" rotation=\"0.9999892642023637 1.5612488335498067 0 0.004633732838142135\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\"><number nodename=\"Appearance\"><number nodename=\"Material\" ambientintensity=\"0.0933\" diffusecolor=\"0.06 0.06 0.06\" shininess=\"0.100\" specularcolor=\"0.06 0.06 0.06\"></number></number><number nodename=\"Text\" string=\"&quot;{$alertdatetime}&quot; \" solid=\"true\"><number nodename=\"FontStyle\" justify=\"&quot;Middle&quot; &quot;RIGHT&quot;\" text_align=\"RIGHT\" lefttoright=\"false\" family=\"'Arial'\" style=\"BOLD\" size=\"10.5\" horizontal=\"false\" spacing=\"0\"></number></number></number></number><number nodename=\"transform\" scale=\".25 .0  .35\" translation=\"12 0 0.1\" rotation=\"0.9999892642023637 0 0.004633732838142135 1.5612488335498067\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\" def=\"float\"><number nodename=\"Appearance\"><number nodename=\"ImageTexture\" repeats=\"false\" id=\"ImageTest1\" repeatt=\"false\" url=\"{$alertimageurl}\"></number></number><number nodename=\"Box\" size=\"30 30 30\"></number></number></number></number></node></number></alert><shapes><number _id=\"1\" type=\"Default\" name=\"Cone\" color=\"#fff\"><number node=\"true\" nodename=\"transform\" scale=\".10 .6  .10\" translation=\"Auto\" rotation=\"Auto\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\" def=\"float\"><number nodename=\"Appearance\"><number nodename=\"Material\" diffusecolor=\"0 1 0\" specularcolor=\".5 .5 .5\"></number></number><number nodename=\"Cone\" size=\"10 10 10\"></number></number></number></number><number color=\"#fff\" _id=\"2\" type=\"Default\" name=\"Box\"><number node=\"true\" nodename=\"transform\" scale=\".10 .10  .10\" translation=\"Auto\" rotation=\"Auto\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\" def=\"float\"><number nodename=\"Appearance\"><number nodename=\"Material\" diffusecolor=\"0 1 0\" specularcolor=\".5 .5 .5\"></number></number><number nodename=\"Box\" size=\"10 10 10\"></number></number></number></number><number color=\"#fff\" _id=\"3\" type=\"Default\" name=\"Sphere\"><number node=\"true\" nodename=\"transform\" scale=\".10 .10  .10\" translation=\"Auto\" rotation=\"Auto\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\" def=\"float\"><number nodename=\"Appearance\"><number nodename=\"Material\" diffusecolor=\"0 1 0\" specularcolor=\".5 .5 .5\"></number></number><number nodename=\"Sphere\" size=\"10 10 10\"></number></number></number></number><number color=\"#fff\" _id=\"5\" type=\"Default\" name=\"Cylinder\"><number nodename=\"transform\" scale=\".10 .10  .10\" translation=\"Auto\" rotation=\"Auto\" def=\"foo\"><number nodename=\"Shape\" ispickable=\"true\" def=\"float\"><number nodename=\"Appearance\"><number nodename=\"Material\" diffusecolor=\"0 1 0\" specularcolor=\".5 .5 .5\"></number></number><number nodename=\"Cylinder\" size=\"10 10 10\" def=\"cylinder\" radius=\"1.0\" height=\"10.0\" solid=\"false\" bottom=\"false\" top=\"false\" side=\"true\"></number></number></number></number></shapes><initialize map=\"PHRyYW5zZm9ybSBzY2FsZW9yaWVudGF0aW9uPSIwLDAsMCwwIiBzY2FsZT0iMSwxLDEiIHJvdGF0aW9uPSIwLDAsMCwwIiBjZW50ZXI9IjAsMCwwIiBiYm94c2l6ZT0iLTEsLTEsLTEiIGJib3hjZW50ZXI9IjAsMCwwIiByZW5kZXI9InRydWUiIHRyYW5zbGF0aW9uPSIwIDAgMCIgaWQ9Ik1hcEJnIj4gPHNoYXBlIGJib3hzaXplPSItMSwtMSwtMSIgYmJveGNlbnRlcj0iMCwwLDAiIHJlbmRlcj0idHJ1ZSIgaXNwaWNrYWJsZT0idHJ1ZSI+IDxhcHBlYXJhbmNlIHNvcnR0eXBlPSJhdXRvIj48aW1hZ2V0ZXh0dXJlIGhpZGVjaGlsZHJlbj0idHJ1ZSIgc2NhbGU9InRydWUiIHJlcGVhdHM9ImZhbHNlIiBpZD0iSW1hZ2VUZXN0MSIgcmVwZWF0dD0iZmFsc2UiIHVybD0iJnF1b3Q7ZGF0YTppbWFnZS9qcGVnO2Jhc2U2NCwvOWovNEFBUVNrWkpSZ0FCQVFFQVNBQklBQUQvMndCREFBTUNBZ01DQWdNREF3TUVBd01FQlFnRkJRUUVCUW9IQndZSURBb01EQXNLQ3dzTkRoSVFEUTRSRGdzTEVCWVFFUk1VRlJVVkRBOFhHQllVR0JJVUZSVC8yd0JEQVFNRUJBVUVCUWtGQlFrVURRc05GQlFVRkJRVUZCUVVGQlFVRkJRVUZCUVVGQlFVRkJRVUZCUVVGQlFVRkJRVUZCUVVGQlFVRkJRVUZCUVVGQlFVRkJUL3dnQVJDQUZIQWxnREFSRUFBaEVCQXhFQi84UUFIQUFCQUFJREFRRUJBQUFBQUFBQUFBQUFBQU1FQVFJRkJnY0kvOFFBR3dFQkFBTUJBUUVCQUFBQUFBQUFBQUFBQUFFQ0F3UUZCZ2YvMmdBTUF3RUFBaEFERUFBQUFmemdkczJCNGcrM2N0Kzk5ZDhsNGJ4dmM0M0ozVVRwbmx6akFBQUFBQUFBQUFBOXVWeTJSbktQUEFBQUFBQUFBQUFBQUF0bm9UenhnOW9jZ3FIdVQ5L2cvTkJlTUdUUTdXK0Y3VExrNWJVTWR0VFkxTUFBQUFBQUFBQUFBeVpCZ0dBQUFBQUFBQUFBQUFBRFkxQmt0RlE3Sjl6QlNBQUFBQUFCNU9hUXkySkNJbU85VzNMbXRNclRFNmRTTkYxTmFHa3I1ZWkzQW1reVNKMDFpUkZpSjdjVzRjMXhMVTNLSktqcVJibXpGMkZLVnVGcFBObXVzVHVVcGRxSnFJdnA3TVdBQUFBQUhNbXZXaTFvSGxDNmM0dkZVdGxZd2Jsa3JFcENZSnlzYm1oTVFGb3JGMHBGZ3lRRWhXTFprckhSQUFBQUFBQUFBQUFBQUFBQUFBQUJNZFFIaHdWem9uQ093Y3dHeDBTc2JtcFdKREJFV2k1RGlTdm5QT3NWa1dVMkNtYWtKSVdqbG5mTmdBQUFBQUFBQUFBQUFBQUFBQUFhbG85QUR4SEowU2RHT2IwNW5YeDkvazdjRUpJYm5PSkMrVXpVdm5MNk9XM3o5VnRIRnBreDZlVnIwV2RPUzJXS1liVnB4YTU5UFRvNmQ5dUxyak56UmYwM0ZRcjc2ZGJHbkN4dkIwOHZvSzJoMHo2ZVBRQUFBQUFBQUFBQUFBQUFBSzVrOU1Ed09XblAwejg1SzhSRnNwRjQ3ZU9sVzlZcjF2NVgwbUtPbGVoRmExY0lMOU5NdmtoV0xCMURZQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBdW5xUWVPclBQdm5GVEszYm8wTWdGYWsySmIyaUdzNENpMDE2NTI3YXpFUktWcTRiVzMySXljR2FOSVJ6RTFiWk1taHFiRzV1UkltSzIrTzJHK1RVbE5pTkdTcmVzOUw0SkRCdVlOTklTMkpqY0FBQUFBb0ZraU93ZWpCNDA1eHlxODFpKzJrV3NwNTVNYkY0eWJta1JWaUtkcmRQTkVUeVFuT1ZYbjF0MFNndEc1TG5NRm9vYVYwcGFhMWRLMjJOQ1EyT25Ddkt6RGw5T0dPZmVNc2t4TFdsbWI1bHg5YVQ1WGpMaFdMOE55SFZOTFFzbVRKa3dnbkJrd0FDa1hTbkRYbjM5NTA0RDVxV0NVcWI4MWFta09ldGVGeVVCa3dUbldCVkwzUEk1RytVZWVpclV5V0V4bGlIUUlwUldpYlVLUFh4Yy9rN0pVN2tSckRTVXhNZFFFSjZnSG5hY2U5K3lxWFN1YkVCNlVrS2h6NllkS2RvSTE0K1hsWGUvZTdsdHo3TllTMXQwSW1qcmoxY3R3QUJYT09lL0I0QWgzaXhyRktHWlRHdVZlclRtcFJOYm12MEtSWGx1Um04SzhyUlFKeXM2TzVucFNSVTF2dGZkcFNQb3B6YTgzVXB6MWFYdFlhV00rcVhEcHJUQk9ZZWc3K083ZW9BQUFBQUFBQThxY3ZTMmM2MURjcGx3dW5XQUFBQmtuS3g3Z0h6UG80ZlNjdlhvamxhMGR1bEhXSzlZNnZpWXB0N0RibzJNZ0FBQXluQU1tRE1nTUl5SUU0U0NDWlpqWUdESU1BeURCa2hKUVpCZythOFAxSHVlNzVhd25rNjRVYzlZSXQwVG5raEtWaU02eDJnQVpQUG5uRDZpRGdtdU00bU84amdWdDUzMGFWZlFiVmRqeE02dUdXNkwvbzlISTdteGI4SDFNK1YwRWRiMXVPTGEyMDUyVmFjYVIxbm9YNGV4TmdBQUFBQUFBQUFBSUNjQUJIZ2VENkwyUGQ4OWVseGRNS3ROTll2U0JPZHM2SUFBQVBJRXg2a0hreTU1K3UydGV2akdKVjcyOGg2bTlEYm5vV2RySzNUcm55dlF3MWliM0RGVHorakhHOUIwMDVQVG4zYlR6Wm14RnFsNDYrVmV4ZXdBQUFBQUFBQUFBQWdKd0FZUG0vaWZUKy93RForWnZKNHVtRU5OT1pGL1Vsa3diQUFBQUh6OEgwQUh6UTlSUzIxbzcxcWN6R2VCMFBYMGIySUpqRVBIK2pGck1xMHM2M25VbzFwbVpzVE52VGJveElBQUFBQUFBQUFBQUFFQk9BQWZOdlArajkzNkh6bDQ0ZXVHdWV0R0xSSHNRQUFBQUQ1ekRpM243QldCODdQU1pvYlBUV2VVUjU3dWpicnJMVjF2R3BSNThacG52YjlYWnRZQUFBQUFBQUFBQUFBQUFBQUNBbkFBUG1QaS9UZlFQWitadlM0bXVHdWVzRVdoTnlZeVVqWXRIb1FBRDUyZWRQc3dQbjh6M2M4N0V6MkxXMEtzUEVkZGVQdEZ5Rm1qdTV1WDBWdWVOanJDNTZIUnkrNkpNbTJEbzQybm1PYW13U0VFTEJabHhvUlM5QkRncGx0SFl4V0pzbHNBQUFBQUFSRW9BQjRMNTM2YjZEOUQ4emVseGRNSTZheFJhUWhycE1ta2l5UkpuSkVkMjJZQStkbmtUN2tEeTlMU3N0cG5wMnRVT2JlTzVTY2dIUDNyNWIwYXcyanM4MXNja1dhMjFwTzVJU0c1c1FGVXFrUk9DMFhsZkxldEhlOGphSG45Rzd0NVhvcld5QUFBQUFDQW5BQU9WNU43SHE1M1puaTY0VnM5YTBXeWJDR0pSa2NUdVdqMHN3QVBuaHp6Nm1EZ2tWS1ZTNWFrRVhqM3Q2aktaQUFBQUFBQUFBQUFBQUFBQUFBQUFRRTRBRVBtSG5mVGUvOUw1bm9IRDF3cTU2NnhiUXlhR0RVMExJTkQxb0I0ZWEzNWoxTmJqZ2tWS1F5NG5SVzUwcTIwNG91K0xoaWIrejM2TndBQUFBQUFBQUFBQUFBQUFBQUNBbkFBUG0zbWUvN2IwL24rbW5pYVlZcHJtTGRrRVJ1YkFBR3dCNDFGRStnSkhCSTV6ekZ1L0Z2TTZSemRZOXR6MkdzUktZbGxPcU5vWW1TQmxLQ1l3a1pRU0JneUVZQUFCa0FBQUFBRUJPQUFlQytjOXIxdjBmaTlGUEYwd2hwcnBGc0Z3cm5mTWdBQUE4MmVPUHFvT0NSem5NdDJrNmtDUE1iYTdJNW11VlM4ZFBOaVVmWEZDMGFiUWgyT1czRjZhOTN6azNPNWV1blJxbzBtM3BGSXhMdDg4Y0t0ZWxlOWk2NU5hblJGZnlzTlVYcjZlaDZOd0FBQUFCQ1RBQUZQdzk1Zll6dkhFMXdxNTZVWXZHZEFoTFJNZDBBQUE0WjVnK2hnOE9XZE1yOHpOTEFNZ0FBOHgyMWtvcGF0TFJacE1VVjZYSFBUclBtUFFqVXNWYzNwYlpwcGRQZ216elo5blhhT1d0SzRCalNjd3hLV0p4V01HQ2FaQUFBaUpRQUN0NU9rbnAxc2xLMmU4WGppYXhaSmlNa0xBQUFCekliVnQ2Y0h6YzVuVmpYbUxScVlOU29kRXJGa3FIb0pqQmtBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUE0VkxkRzBTMG4wbWR2VFo2RDU0VDQ2ZEM5TzFLaGhwemVuSVN3ekxZdG1rS2N1b2R1QUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQXdlTWxUUjlBU1Bubkx2MWVuQzVFK2h0Rk9JNC9MMGM3cXdtbE1RRmt5VGcwS1ozam9nQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUE4ZWRFNzRQTTVYNTJsT3JFK2l0Q0ZHWEY0OXM5bVBIT3lha3hWT2tkb0FBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUE4UlN6QzN0K3JNY0k0NTF6dkFBOFp6VHYweE1VaTRjSTlBYWtKRVRFeFNKQzZZTkRCRVlMSkdha0plTUVKc1V6MkJNQUFBQUFBQUFBQUFBQUFBQUR5Sng0ZlJwRHdCTmZQdVUwN29BUEY4MXArbXV4VEx4eGpzbUNNaUpDeVVDUXNrcFhNbFkzSmlNd1JsMHBFcEFXejFRQUFBQUFBQUFBQUFBQUFBQUI0ODY1MlFmTURxelh0UmIwQUFQSjZaYTU2N2xFNkJ4RHNHNVhJeVlzSFBKeTBjNDZSVUppb1RHeG9YU3NXQ3VhRUpjS3BZSlRrRjRrT3dkTUFBQUFBQUFBQUFBQUE4VFNZZVRQM25kY2ZQUy9hdldyYnVnQThucGxEbnJNVVMyY003cHVRa1JJU2xNbExSUU9nVmljcUd4SVlMUmdnSXl3U0VCZ3VGUTlFWEFBQUFBQUFBQUFBQUFBQWVST1lmUVFmTmNiMk9qSHNvOVRYUUFlUDB5WjZ6Rk10SERPOGJFUkNia3hWSkMwVWk0Vml3VlFTR1MwY3N2bWhJYkhQTFJraE5DWTY1MWdBQUFBQUFBQUFBQUFBZVFLNTdjSENPSmwxZGErUG9yWmdEeU9tVzJlc2hRT2djTTdnSzVFVEZrb2t4YktKYkt4S1ZpUTJCYk9RWGlxWERCWU9JVGs1ZzlXU0FBQUFBQUFBQUFBQUFBSGtDZ2UrQjg3T3BiUG94YnZ4WUFlRzVlV3oxZFVoUkx4eGpzZ2hOQ1VuT2VXakp1UWxnbEI2UUFBQUFBQUFBQUFBQUFBQUFBQUFBQUhrVHlwOVlCOHpwWHZheDA2MzdZQWg0em5qZnBtUXBsczRwMkNZaklUY21LUllNa0pJRG5uYU9lV2pRc25FT3VUbk5Pd2QwQUFBQUFBQUFBQUFBQUFBQUE4Z1FudFFlWnRURmIzRHRnQThYelJMMHpPVWkyY003aElRRUpJVGxRbEpDSWxOU0V0R3BXT3FjOHNIcGdBQUFBQUFBQUFBQUFBQUFBQUFEeDUyVHJnK1FIdUM0ZHVDUkNKOFh6eHYwcktlY1hUaG5vREJYSXlVbktaTVNBZ0xBTmk2YzhsS3AyVHJnQUFBQUFBQUFBQUFBQUFBQUFBUThwYXV1bFBXNWFqNXdyNkJienZwOFh4ejdINS9wODJzZmorajAvUTRhOFRwcFdIU0xHY3BqaTkyQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUFBQUEycGI5SC9ubjFubFpmUmdlYUswUXREYW1US09KZkxwVjEwTVNtaUs5NldLYWFtdG1JY2VHSmlST0lWcGl5bkIxSmllczVKWW5jME53UkVKYk1HcEJhdGlKUk9EY3daTUFBQUFBQUFBQUFBQWhOemN5VFlhMEQyZ1BJRkRMbXEzMm10ZU12a0djNzNpT1ZROUdlZjVNNSt2U0lySGJQS0ZHMUk2M25JQWRVOUpTT1JyRzFiU2xncWtoMHlRb25QSlRwbFdrVXBtNVpnMUpEcmdBQUFBQUFBQUFBd0RKenltUkVwMVRvSGVCODhMNW9jNGlMUkNaTWtoZ3NGWXFGd3VGY3lVekJZSkRZcEY0aU82VEFBQUFBQUFBQUFBQUFBQUFBNFpFVGtCMXpsbXAxU0lHeGFQUmcvL0VBRFFRQUFFRUFRUUJBd0lFQmdJQ0F3QUFBQU1BQWdRRkFSRVNFeFFWQmhBWElEUUhJVE5RSWpBeFFFRkRGaVVqSkNZeU52L2FBQWdCQVFBQkJRS21yTTIwNFBwUTczczlLR2MzSHBHVTl2OEFUSmE0MWJWbWhsTlllb2FVc3VRZjBoS2pySHB3bmFmNk5rN1pGVElqTS90cWIwOEswQTcweklaRWdlbXNUUnM5Tk9jRzZxMjFjais2aVJuVEpVejA1SWhEbFJud3BFVU9KRW00OU9CcTRGZEJqdWh1cWlQdFB3dS8vZGUwVDhGSVhXK0Y2eGZDMVl2aGVzWHdwVktIK0U5WENjLzhOb2hKMC84QUNPdXNKSHd2V0w0V3ExOEwxaStGYXRmQ2xVdmhTcVh3cFZMNFVxbDhLVlMrRktwZkNsVXZoU3FYd3BWTDRVcWw4S1ZTK0ZLcGZDbFV2aFNxWHdwVkw0VXFsOEtWUytGS3BmQ2xVdmhTcVh3cFZMNFVxbDhLMWErRnF0ZkM5WXZoV3JXZndWcThyNFVxbDhLVlMrRktwZkNsVXZoU3FYd3BWTDRVcWw4S1ZTK0ZLcGZDbFV2aFNxWHdwVkw0VXFsOEtWUytGS3BmQ2xVdmhTcVh3cFZMNFVxbDhLVlMrRktwZkNsVXZoU3FYd3BWTDRVcWw4S1ZTK0ZLcFovQmFyeXZoU3FYd3BWTDRWcTlJLzRRUW9idmhXclZIK0YwYjA3ZSsyNWJsdVc1Ymx1VzVibHVXNWJsdVc1Yms5OW5sMjYwYTVocmNiQ1NiUWJpUzdiQzdkbmhCZWR6R09rNGlCbldKTUNzYlI1M0ZzaVpkbTB6bHpyYlRFbXozWWsyL1hmTHRudWZLbjVmV3VQbUZoOC9FWitiVmpobHRtWnhKczhrNzF0Z2JKTnF4b0N5MzJPVFBFMHA1K2M0bTJXRjJMSWFETHN5RmJJdG80NVVtMXkyTEluR1daOXJwMjUrSXJwTnF6TWVSWU9tU256R1l4THRNT2l5TEhhVjlqM0k1clpzYzVKR0pPRHplRVJ6dXNOeTNMY3R5M0xjdHkzTGN0eTNMY3BQYnlkanY0dllsbUVNbGgya1hsNDZES1pKSDVTUHlNa3NMbnlRT1oxbkhhL05nREJIU0dzSDVBTzBNdGtoajdJQWl2c2dNS0tZTTZmYVJ4ekdXSUNaYmF4blpMTkdDTm0wanRDS1dPUWgyWUN2SElhWm83TUpjc21ESmtrdGdzaXNSR0lhY0tPN3k4WGg3b3R4NWJJdzMya1laTjYzcmV0NjNyZXQ2M3JldDYzcmV0NjNyZXQ2M3JldDYzcmV0NjNyZXQ2M3JldDYzcmV0NjNyZXQ2M3JldDYzcmV0NkM3VW5zU0tRcG9jRnRleng3eVBDMTRHT294bWNDTm1OazFSMlQrR1pobmhnYTVqdjZ2UWM1UW9ESy9CcTlzbCtLZ2JEUkt6RVE4aXM3Y25OSEhkZ2xkZ3FsRExMQTZFQnVBR0VCU1Foa3ViS1l4WmlkdG1JZWhDRGViT0laY0huUVh6bDRvZkc2c0c1MG9CcFl2Q0N3WEhLM0dwbHVNdFRMY1piakxjWmFtV3BscVphbVdwbHFaYW1XcGxxWmFtV3BscVphbVdwbHFaYW1XcGxxWmFtV3BscVphbVdwbHFaYW1XcGxxWmFtV3BscVphbVdwbGw1VzRpUGU4dnRKRTdNcHpEZENFMHpWR2lGeEtKaHpoZ1k1alJpTGc1ZDJXQTNZd0FXY0VrQ3lWemNiY2hBNEpEaGNRbjlYUkFTUm5NTWpuL0FPWXNRZ0N5OHYwak9rdGJpVGFiUXpKbWNTZDhtSldnUEdpeFlFc1ZuT2ptSzhUWE1FS01WdGxZamZ2NHVLTkgzNFRZdWVhekNSN2hiOENpUVRqbTNBRE9MRzNkWmtYR0p2N0FjWE15dUdRUnZZcHl0YysyMk94NnhIMTMrcHNNbU85V0JYL0pSYzdQVm9TSGkrb3d6WkRwSmRaRnJpSU5sMHdoSHlDdHc2ZnhOWmRzSmswcDRBUjdQTWhubkdaTG4xRTNFbVJkRGpFWmF0SVp0M3ZMR3RPNWhwWE94eU9YSTVjamx5T1c5eTVITGtjdVJ5NUhMa2N1UnkzdVhJNWNqbHZjdVJ5NUhMa2N1Unk1SExrY3VSeTVITGtjdVJ5NUhMa2N1Unk1SExrY3VSeTVITGtjdVJ5NUhMa2N1Unk1SExrY3VSeTVITGtjdVJ5NUhMa2N1Unk1SExrY3VSeWh2ZG1UN09oWWVnQncwSnpqakxPTnEvaFdkTU8vaFdtRm5nZmxqRzZaYnNYRmgrWHRiR0VGK0RNZTFqbUNCSGhZM01YR3Y2dDQ4ckJHdU0xN0NEWm81ckRESXNmeEozOEN3M09YbS84QUMxcG1PRitTL0xkakdNckRtT2E0akdZZGxySGFZV1I1d3RGeFpUSHRJN0dXNXpqTGN1WTloQjdkVmtlaTI2NUkzSXg0T3piakxjczNOdy9SZmtzTTNMYi9BQmJtdFp5QjJaY0p1V0Q1R2NDNEZ3TGdYQXVCY0M0RndMZ1hBc0dFNlFUWUpqTTRLb29NdGtlejVUbVdBaEd5S2JURm1GbDFtWlNiVDZPYlhQYmtkSzNZS215Ri9oODdjUWlNS1dHUTdlQSs0c1V4UnRySE5DV2t5VmpxOTcwMnJlMXhPUVNIQklNWEViS0ZWR0RNeFN2YTBOVmtZQlZMaFlGQUlGeElSQ3JBRDRkSWpsTGdOUzhBblV1N0dLMytQRkpoakhWTDh1YlN0dzRsVGtyV1ZXV1p5STJjTWlQRTNZWlpyallJNnFjNXphWDgzZW1pT0d6MCtRWmpDZGtqSytRMUdneU1pSDZlS3MrbmlPUnZUK2M0eFNHd3pQcDU1Q3g2azhZZml6OHJJcnlENnp0dlR6aE5HVmpkcGx0TXRoMXNPdGgxdE10aDF0TXRwbHRNdHBsaUJvZllYUndITDA5Qk5EZDdIaXRrZW9SUHpnWEpsU1daa0RKRjVUamhsYWgxam1NZkhNL0xvRDhrWkJlTjNUTTBYV05oY21WeU9UR1pZL2x5dVhLbmdmS2F5T1hNVWNRZzNOaGwyNWdiM3hnT2p2ZEdLN0VjYndQNWNvanNrWTNWaTVNcmt5aGl5TS9qMzREbUcvSzZybXBzUStHOUV1L3h6Mm9zVXprMEJtdjVNcmt5b3JlRW5hYXUwMVNZM1BPbFJPWkVydVJzSUdZTVljTjdGTmd0bU1kWGF3eG1ZUEhhYXRXWmttRVNSRFl4elJnRVVlZWxKemNXa1YwdUlNRFFScThIRGtzWWpyR1pEN01nTE5nb0VUclorb242dnBrYldzOWloWml6Sko0UkdQeFo3cnR6SmpuT3lkN0VFNUNsMzVlTEV0K1VTYVJtT2JRRDVlZGN5YzVhd3htRWJMM2k3ejJnNW5iKzRkenhUbkZWZWM1NURDNGVWMDdMUnlqSFpNWkxjODNaYzQwcVVkc09SUGs0bVBua3lhd21tYVl4ellzWk1sN0VTUmdiOFMzYXZJWStHeTNQZEQvaWphTFJhTFJhTFJhZTJuOG5SWUkvQkxHd0pFZEt0SFJXanZYUHg1M0dHTTlTZitxYTlhRjRMVEVoMjV5M09XNXkzT1c1eTNPVzV5M09XN0tmK1VyMDQvSkdlejRNcDFxSWJla3pMZUNMSWtIZE5uOU4rYkE0VlczaGJJRHBKd0tOTExJanRpaWN6cEJYVEN1bUZkTVM2WVYwd3JwaFhURXVtRmRNUzZZbDB4THBpWFRDdW1KZE1TNllsMHhMcGlYVEV1bUpkSVM2UVYwd3JwQldJN2NZNGNMaHd1SEM0Y0xod3VIQzRjTGh3dUhDNGNMaHd1UEhMdzRYRGhjT0Z3NFhEalM2OVFrcmJtTkVFOFdhK081VDNnaElsaEVHYVBjd0pRWmt5SEN3NjVyMlN6V3NFSkxHd2dWa2p5OEREQzNOY0hNTThTYkk2SVYwUXJvaFhSQ3VrRlArODlFSGlraGUzK1FmWXcybXdmUnFsWW1kb2JMUWtJRXErYzJiTHVSVFN0bVlCaVJkYXlKTnEyWE9kTWgxdzMya2RsVkx1WkRING40THp6V29McHp5c2ZNNldUU3NrT1dXNWhER3cwcHBNY1pHempLSTJRdy85djhBN3ZwbmVvc1FybUgraXJHZjAwUzVhd2diMFJTNDlRQUVURjlIWk5INmdFU05GWXdnL3dDUVQ3cW9qdmkrN3BlR1RHTy82cUV6MndwQThsRkZvWlVWalBTOHJ0RDlKVHpneDZhZUl0TFRPcWxtbktRc2FHOTdod3dURE5wSlhVelg0bFRJNDh4aXVqWllCN3VpQmtaODFZcUM0ZkpwcEQ0bmkydnpvdEZvdEZvdEZvdEZvdEZvdEZvdEZvdEZvdEZvdEZvdEZvdEZwLzV0Rm90Rm90Rll3b1pQVU1QOUJXTTdFUkh0UmlBUDFERDFqdFpnV2kweC9Lc3l1QzZyblBQTzlwak5mVVFjYm9FSGl4bmFpVEJqZmoxTkVjbVp3Um0xYlZ0V3haOUt4c0FKNmJpbWViMDZHUWcrbVlvVkxyQlRjam9JNFNZb1krSHhhZVBETm1DSjVQN3YvZjhBVFl2YmkraC9vcXhtc2laZGJBQ01ucUVBc0I5U0RMTC9BSmR3dlRCQlBzUFluTSs4ai9aUWZ1Rk9tWkZQaVdHREFENnRjWnMzMVZtRk5mY0Y2K1BVajNPa2VvY2dseHBKM1NQMlAvZjlObFlPRDZpaC9vS3htRGk1elpnQ0xOdUVpSDZoamxUdlVFZHVDM1FnNXpkQnd4dnFXTzR1ZlVZTVlaZEJKSSttMy9yNlN6bnMreEROYk1GOWhDeVJiMnJWdXNyTHVDSXkzYU1qdlVBaXNCZUZHZHQwNVNCeU1zcncybVJOelk3NDdaN3N5R1RCd2NZdG5TWVBtSE5LTzBZdExIRGg5eE5aYmRuT0xOOGZaTkl2KzB5U2M2d3pnN1puR3h0c1I0OFhRb3o4eW1SMmx1V3lKR0oySVkydUV0VnF0SExSeTBjdEhMUnkwY3RITFJ5MGN0SExSeTBjdEhMUjNMbzVhT1dqbG81YU9WaTUrUFVNUDlCV0V0a1ZIc2doQTY1QmhqYldQbFNMSmtNZzdSaGNGdFl4UStXQXh2bVE2c3RnNWNHMkZJSjlGdC9YMFNJVEQrMlkrSFNSZllSV1p3dUZ5STlvVU8yaG1YRm5LNFhMaGN1RnltUVN5R2k5T1RHSW5wYVk4akthWGlYTW9TellXS2NyRm1ua0VZV3BPOFpheVNSQ3JUQ2xkR1R1WkJNMnV6V0Z5NWxSTGJtVlF5akNpMFZqSHd6MCtUQnBkSE1rVkhpSkRud0szcEJqZW01a2NCNmQwc0k2YzdET3JNN1A1djhBditsd2NPZkQvUlZoTEhHUkxLT044YXpoSERNdDRzSVI3YUpHSUsxaXVCR3U0Y3RlY2hPalI3bUs1ZVlpdFBDblF5eWZvdGNaem4wOVh1Z3kvYi9JdnNJN2d3eHZ1Z0RJSnNTZExkNmFoRUl4bUJzL2FQOEFmOU5saXcvNUZCenVqS3hraGpaSll4eFpoM1VlVkZsMjhhTEdQZHh3T3haeERRWWwxRE5qTjVDZEFEZWdKZzk1RkNzWEVkcnYrVFE5UGVhYnJ1cnB1Sk1qMi95TDdBUmRvSTVqUzFNdVNWN24zazBDcXZVTXkyalp0SjBkUjdhWkpqc3pxMzlsL3dCLzB6SDQvd0NRVjMyYXNKQW8rY1RvN0ZWV1FyYUx0d3R1RVVMVERhTnJHN2NMYmhiY1pXM0MyNCtnN0dra2VuU2xrbjl2OGgrd2hzTGd1aW10bmR1bURaNUorUy9KWTJOd3QyRnF0K0ZxdDJGcXRWcXRWcXNPeGxhNHg3YSsycTF3dFZxdGNZV3VpMVdxMVdxMVdxMVdxMVdxMVdxMVdxMVdxLzNhclZhclZhcWUyTzIycjhhUlZZR0NEUGRqakxEczR6aGs5UXdoQWRheDJaZGVSR1E4Zm4vSktIYWYwSVVENFB0L2tQMkVJTG1aM0xHZnpPM21FNmtlNVI2Y3NlWkg5TFlqS3k5S25sdkZRU0FnZ1VCNDFnV21BVEZoV3NsdWgxYmlWWnFqeVJwRVVSaitLd1NLMkYwcEdhNEJwY0dFMkpISEdZTlRLUnM0b1dDSEpIR2p6MlNHTmt4bk5ZK0VhR0EwZDFQSUs3TlVUaU5WaXpXUnFiankrbmtObWVFa01qd3Fkc2MvOHpINjMwdXorY1A5RldKZ0FScE5jeHpKTlNkcHBkTEZqWmsxbVJqTFVteTMxQkJkSEhjQUpuNjVQLzM5STd1aDdIbXZIYWlsTjZncHpHNDhnTmVRR3ZJRFhrQnJ5QTE1QWE4Z05lUUd2SURSNjJ2a1pGQmdndzZCQnpFRld3WG1pVmRmQmYwWXUxb1ljYUl5YVBNYm9SV3NmVDFyRStEQU8xdFRYWk95cGhSSHdhZXNpWmpoaDFwUUJoZ2srUUdzV1RNdXpadHdURm0zTG5XYmNZemFZdzR0bTFqV1dqWFA4aXpjK3lZMW83TnJrU3phekhrQnJ5QTE1QWE4Z05lUUd2SURYa0Jydk01UElEWGtCcnlBMTVBYThnTlpsaXptUE9ZTVhraEtSTUE5YzhSYzhQTHBIUU9NcElaZ3NseDI1NVlXUjRreFd1OGtKZVNFdkpDWGtoTHlRbDVJUzhrSkZrdEtTcWdpcjhleHhoSDZoS0Y1VzRpblFvNTJwa1lqVjFTOE0rUEpQZzlmSk16RVp6aFBpeWN1SEhJd3ZSTTBXNWJsdVc1Ymx1VzVibHVXNWJsdVc1Ymx1VzVibHVXNWJsdVc1Ymx1VzVibHVXNWJsdVc1Ymx1VzVibHVXNWJsdVc1Ymx1VzVibHVXNWJsdVc1Ymx1VzVibHVXNWJsdVRZUk1XQitYSUdGY05WcFhHZjdQaFNDM3I1Ym93cTQ3cFltTVBpSEZFVWJ4TnRHeW80YmJsNjFuSkJOeFpqYTBWazkvRko3Wmd6dURiY2JhL0V6WDk1T3plYjAyNTJTZTBnTUo5bXdqUXc0aFdteW83VFlIRUdjV1E1dGhJN0xWenNzc2lPaWh0aElXTFBHTWVUeTZNMndjUWc1ZVJ2WlAxQzIzNVlmTnQvZGkvZFJJNHh5dmJNWWJqOVRzQmlCYkh6N1JtR2Foc2tNSzE5amcyTTNXaEgyRG5FelA3RWp2OEpDMnFyOHkzTy9lWm1IdWZSdW1ZTDc1Qnp3UU53d24wSGVZTmxsOW9LV3cxdHhFTGJzZXlYWnJFdTdBZVpKc1FaWWV6ZXpPYlJ6eE91TXhKSkxiYjJMUGtGS3VOT2U1eXNTTExlQTlrUno1Vm5oQkphNWxkcTIyRm0yWThoUFp1WUoxcUpZazIzR09mYUVsdW16T25KbHo0N1RTclFNUmg3VjVJOG01eGdPWE9GK3hQKzc5TEh5ZlB0SUc5MTJVamcxd1AxUG9PUXdiUGRaTWw0TGE4RGpYTFZ6Mk9HZHE2R2VjYXdDZ2tzeUJ6NU56bWVXNmh5VzNIeTJITkhOYjVieTI3azAxcHVFV3dma3A3WER3dnMzU1dTTG5pZkxzMlBqbnRYamE2MkV1eGI4UGN0WHlpRW5QakNuV2ppeXBOa0F4RFhMaVlOYTgvN0dYN3FNM0RTKzBwL3dEOG1MSURHZ3hzdGM3NkdZUGk1dzYweE1hYTI0WEd1c09hVzB3M3RYUTVOZ2F4SGdiN1FnY2VWYzhlTGhzUTc3ZmozMldaQUgzTGxnOXM1Q05ZNUhHemNkWjc1NVdsSmNNWC9ZdnpobHh3bkphdkR2dFhHR1czZTE3N0RiTFBhc3hoOWxoMkRXdTZUbTBlVXI3Z3VNSHRkVFRyUVRkMW8vSVMybVJBODgrc2w1dEh5ZXpaY3NKOGwrZjd1WGg3aWVucEU0enZaK0hGdnVOcjRRY2JTL1F6bnhjOGx0aVdNMXZscmpYUE1BdHhybWRkZ2t6cEZrRFBKYXVacmF1SU4xeGlKSkpjYmVTM3ljUmJ4WWsyN2tJOW5zakd1c3h5bHNYdHlhNXdzT3NYNWJpNXlLU1c0SUhrdUhHYWE1Y2lQc25NNTdSMGd4N1FUR0d1MnVjZTF3NXI1K1Z2dU12Rkl0OHVDZWRnVFpOdTVReUZMRS92bi9kK2wybnl6MmxTaXg1a2hobkFyR3ZhUDZHWU5pN3lhMGJMR2UweTE4aTJ3WU1tenpueU53Q1JNbHo0MmVheXkzSmJMS1lhMzZrazF0dHhJcytRY3EzVFpOdGxDUFlaWUdUYnVBWTgvYmlUYmJPYXd6aHI3ZklKSjdaNGNIdGNrYkt0TkhHbjRiTU5jWmo0a1dHYzVrV2JVVTFqaHJUMk9TY2xxK0xtVlo0YUdmT01BY2kxZUJrcTN6QjcwL01xR1dVUi93RGVFL0tYNmZoanJ2Zi9BQytRNlBEaVA1UHBaejR1dTFQSE1iTXNPQjgyMFkvRmhMempGcFpoa1Rac3VPaHpKeng1a1dMc3NrMm1Za2lYWllIM1pmS0N3c1hMRTZ4emdVdVk4WUpsazZNYVhNeU4wNnhZTE1pY21tdGNoUE1zSEF4TW44ckxDYzRiNVV6am1UYlRZMDlsdWl6YkxmMnA3NHc1bGk3RFpjMUNtWExzRHNiSG54WXpldml4c2NqSG5MbWYzcFB1L1RPWDdmWTBnbUwvQUFicndveE1GZDlEcEU1dHgyckZrdkUyeTRIenJWbVd6cCtubExRVW1kTm1nUTVkZ1VPRFdXU2lQYTlTUkxzMmp6TG5kZ0U2MGRodGhZWlRwdG14L2RuN0MyRTV1ZTdPZG11a3pPRE15WnArMlA4QXZQUWp3T2llempqOHEwV0R3aC9yZlJnMGhsNXoyZmJaSnRPTjB1MjNpTmE0VHJPMkRLbnk3Q01tbnNpQzViTE9XR3RzUkpFaTF3em1zM1NBeUxoeUhOc3QrWmRvM1BjdFhrbnlyVmtZWnJiTTV0bmFIWjJMSEJRMmRnUUw1dGl1ZTJjVjBxdzFzU1g3RklQWnJ0enNLUWE1YWV0TE5MbjlqSjkzNmVFVVF2WjhjUlNSdnRoL3JmUnpTbVhuYnNNVEJ6WitXT25XV0RobXpzNTh4WkFrelo4eUt1M09jenVXT1V5YmE5U1ROc3NNeE1uWktPZlpPeXl5bmJzenJCdUcyRmc4czJkT0ZGSE1zblRJZHRLbHV6UG44aDVVL0VpUmJUUUxNMlJ2bVM3TWFobm5sbC9zNWZ1Z2ZxK3hycVNQMVhHKzJIK3Q5SFlrRHZNMk13VXRsbEx5TWxwTkdZZHVkenNYODRNaVphSGl1eFp5WE16WVQ4dUZaV0w0a215bXRaNVNSeUN1WmJrMjJsNVRyT1l4T3RKYk1XdGpPRFZ5N1U4Vlp0SkxjTnNwVGl1bG5XYmFSaHNXek1aa2F4bTRiRmtHTWI5bnNSdVlPdFpKYks5cFZnU0xMamZiZXBqUEJVZVdtcWZaUEdjMW5LMGt5Y3R5V1labURTemlISXNKTE1GbGxhaVNqRFhscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hsWnE4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPWGxweTh0T1hscHk4dE9YbHB5OHRPVGJXYnVsL3FlbENsa045dHhVMXo0NEhPd1JjTWZkd0F5dXVGZFhBcHZYWnVhMk83TFFzY1JzWnVvTUFPN0FSYk91eHk2aWRGVHBzZGplOEJ6bXlncDBzT1c5dHVWMnc2aWxDS1Z3TURHMEluTmVFVEhjQUd0NndWZ01keTY0VjF3cDQ0d203b09SOVlLNEFaVFF4M0krWWNiR0F4M0xJQU53ME1kK091RmRjQzY0VjF3cnJoWFhDdXVGZGNLNjRWMXdycmhYWEN1dUZkY0s2NFYxd3JyaFhYQ3V1RmRjSzY0VjF3cnJoWFhDdXVGZGNLNjRWcEZ3L0FvMlhjQUYxd29yaWt6UlF6UUdlenJDT3gxbUZ0akZqUUd4b2tpQ0NSSzhlTEdZRE1Rb3RpRmswSXdERkxkREE2VkZxWXNSdk8xUklJNFV3TU1BWU9hOFBKNHBtOE93T0RRekVheXJKbGphVjdHanFYamVhbGNabmhQemhRM1JjelhPTUY5WU9WR2RWamUyVFhCbEljVVRKV0t5TzFycThMaVI4RGpJejJsSEdBTUEvRkFUcTRUbnhHaWhBbnRlZkRZTzdJNG8yTXpERG5IU0VuUlJPTHpzWFlhdXcxZGhxN0RWMkdyc05YWWF1dzFkaHE3RFYyR3JzTlhZYXV3MWRocTdEVjJHcnNNWE8xYzdWenRXSkRNcnNNWE8xUENGMGlSQ0FjamEwRFRuaE1Qa1JHaERHTTE1dmFmQk5KZEpodU9vc1I0VENnVEJFaFZzeU84ZGFkeURYVFJRQlZzc0xoVmtuam1WNTVNaVJWa0srTEZtQ095RE1ZMDlaUEt1aEx5N01NbVJ1ckg1eUtKTllZZFhMQ1psVEkzc2dUTlRRWmVYNWlUTXM4ZFA0VFJwam80NDA3SlFNNHcvc1VpT1owck5jWE9JTUo4UXNpRVNUTHlOL1lQWFpsSE5XTzQ5anNHeUkvZE9GNUh5UXVORnJSWkMzMi8vRUFEZ1JBQUVEQWdRRUJBTUhBd1VCQUFBQUFBRUFBaEVERXhJaFVWSUVFQ0F4SWpCQVlESkJZUVVVSTFCeGdhRkNZckVWY01IUjhKSC8yZ0FJQVFNQkFUOEI2ZU0reEtOUGh6eExnTTF3L0J6d3JoUHhSL2xjSFJzVVF6MlRWcG1zM0FYR0Y5MmJhdGZKVXFZcE53RDJsbXMxNGxtcEt6NTVxU3MxbXMxbXMxSldhQ3pXYThTelVsWnJQbG1zMUpVbFpyTlpxU3MxbXMwWlVsWnJOWjgvbjVwbi9acHJnQkNkM01JZlZNWTdGSkhtVTJWQThseHk1MG1HbXpDVXluVHFPaW9ZQzRxTHBoMkw2am5YYTUxTnpXOTF3ektsT21HMURKVkduVVk5N25tWjdMQzY1aW5Ma0MvR1FSa3FybnRBd0NlaHBIelI3NUp6OGFxT2NCNFFtVGhFb1UveE1YNUswQXpKVWcvQ1o2MnRCQkpLSWprMEE5MDRSeUpnU3FGVTFHNWlEK2VBa2RrMW9ia0ZVck1wdURYZlBxQkk3SWtudnlCSTdKemljeVVDSENRdTZaVFpUK0FSMHNyTmU4c0hjZFp5UUlkMjh0dFZqamhIa0J3UFkrdXEwcmtad25PRGU2Qm5NZVFST1NhME5FRHJiUmlwam5ySWtRbWdORURrSHRKaWZKcDBoVDdkT051TERPZk5yUTN0NmdkMVZjeDBZQjFWcVpxZkNxYk1EY1BtdmJqUXlFZVl5Z1d2eFQ1MVdrWHZEaEhsVld1Y3docFRjdSthYzVwK0VRalJxbmliZ1BoWEVVcnJJSGRNYUdBTlRBUjNSK0xzaUo3OHFkUEFTZlF1N0tYS1hJeXBLa3FTczFKV2FrcVNzMHp0emIzVU5SQVZUNkp4eVdhQktZbWQxRFZEZEZoQ2h1aWQzOUkwVDZTcFRjNm9Dd1IrL2RPTU5KVStGQnhsRXFZV0lrWkJYSE5tV3FrODFQRkdYT09uTHJueUpVcVZQbytCK3lxWEZjRlU0cDc0TFp5NTFLbURKTk9JU1BSZ1Rrb1Vjb0tpcG9vcVQyVDJWUUpZaDk1MFRqWERnSXlXYlJxczBNUlhEamh4VEZ6TjJmNmRzdjVSYncvZE8rN2dRRTRVYnAyb0NoQmNFMFVSOFJRYlR4U2V5WUtUbmVJcjhEQzREVCtVTWhtZlhjUDlrdTRqZ1g4WUpodjZmOS84YzZqOENhUTRTUFIwejRnaWN5cFVwcndDblZRNVNOVkkxV0lKeEMvZGZXVmhFeW11QUVLOHc1U24xRzZyRzJKUmNCM1FQS0Zna3lWQ2hRb1VLRkNoUW9VS0ZDaFFvVUtGQ2hRb1VLRkNoUW9VS0Z3dGJpVy9aNzZiQzNCbit2T28vQW1rT0VqMGJUQkJSektoUnloUW9VS0ZoQ3doWVZoQ2RTYTd1aFFZTTFaYjNUYUxXbVFvL0l1R1lUd0R6ZkxmN1pFSDlwbitPZFI0WW1rRVNQUEpucm1GS3gvUkY4R0ZVZTVtY0w3d2RwVHVJd3V3WVUyZjZ2eXJoZUVhL3dDejMxamdrVDNuRiszT284TVRTQ0pIb21DNFNHL0pQK0VvZHVqTlpyTlpvaFE0NVN2eGdleUFxaytML3dCa29MUXZFczBBUjV6V24wM0M0UDhBVDN6UURqbjRzV1kvYm5VZUdwcEJFam1HWWdyYXdISllDclpWc29zSUVueVFZRUovd2xEdDVCV0VyQ2RWQldINStlR2dackNWU051cTJyb244VzE3TU9ENUVmOEEwelAvQUFnME5HWHBNVUNJNTFIQnFhUVJJNXowU3BLaytVLzRDcGhxeGxOZEtqOHc0WjNBdCt6WGlxd0dwOGpuUCtJL25uVWMxdmROZ2lXK2NUQWxBazlEL2hLaVFzSVh3OWxpUWNTTWxkYzJaQ3BWSFZCaWpMOHRERC9wdUtUOFhiNWY0aWYzL2JuVWMxdmROZ2lXK2pxZkFVM3NPZjYvbUZPOC9naTBPR0VaL0tmKytkUXRiM1RZakwwZFQ0Q205aDBCNDdRblBEbXhIS09aRXFGSDFVZlhsSDFSSWIzS0dHVGg1eXBDbFNFWEJ1WlZ4dmFWUEtBQkFXR2U2ZFNmaXlPU0ZGd0VOZDhsVHBZREp6UG93UEQzNTFTMXZjSnNSNGZSdk10SVFPU2xTcENsU3BVcVZLeUt5V1N5WGhXU2RCRUlSQ3lSaFpGWkxKQU5DSkFRcHNCeEtWSVdKWWxpVXFWS2xGeURwUmRDbFNwVXFWS2xTcFVxVktsQ3JBaFlncENkSGVFQ0JrRmlDeEJZZ3NRV0lMRUZJVWhTRklVaFNGSVV6N0orWElKcnA2R054b2lEQ0hkT00reHhSZTVsd2R1UkdFeHpjV21mWTJOd0dHY3VSSmNaUFJrZllMWW5OVkhOYzd3anluRDhQR0RuSXk5aXZkaGJJNm5OL0N4ZzV5TXZZcE1kWGp4L1QyQUluTlhSVXFPRFd3QjBFVDFlTEg5UFlsT2tYeWNVUW5USGhUTVJIaTZmSGorbnNXaFNiVkpEakNxREM4anA4V1A2ZXhYdndDWVRUaUU5Sk5iSEVaZXhKNy9ST2FIaUQxTWE1empwSHNRc2E3TWpyYTF6bkhUMld4cm5PT2tld0hUQmhOeGYxY3cxN2lTRGtPWEZrdHBFaFhYN2xVcVFZWTRxc1I0aU1vL3VuK0VYYVBXTCs5RnhFK05GN3R5eGYzckVSL1dydFRWWGFtcXUxTlZkcWFxN1UxVjJwcXJ0VFZYYW1xdTFOVmRxYXE3VTFWMnBxcnRUVlhhbXF1MU5WZHFhcTdVMVYycHFydFRWWGFtcXUxTlZkcWFxN1UxVjJwcXJ0VFZYYW1xdTFOVmRxYXE3VTFWMnBxcnRUVlhhbXF1MU5WZHFhcTdVMVYycHFydFRWWGFtcXUxTlZkcWFxN1UxVjJwcXJ0VFZYYW1xdTFOVmRxYXE3VTFWMnBxcnRUVlhhbXF1MU5WZHFhcTdVMVYycHFydFRWWGFtcXUxTlZkcWFwdFY4alBxT1NJRGhtclZQYXJUTnF0VTlxc2VLZmwraXRNMmhXbWJRclRORmFab0UxdEozWUJXbWJWYVp0VnB1MEswemFGYVp0VnBtZ1ZwbTFXbWJWYWJ0Q3RNMmhXbWJRclROQXJUTkFyVE5xdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cUxLVGU0Q3RVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRVOXF0VTlxdFU5cXRNMDZ1SW9EaUdZQ1V4dUJvYnAwQjJIc09talJwVVE3RDNkLzdMcWEzRDFGQVI1a0NaUHZMLy9FQUR3UkFBRURBZ1FEQXdrSUFnRUZBQUFBQUFFQUFoRURFaE1oVXBFRUZERVFJRkVpTURKQVFXQmhnYUVGSTFCeHdkSGg4RE94OFVKaWNJS3kvOW9BQ0FFQ0FRRS9BZTd3ZjIxVnJjVHlyWnlYRWNUSEVETDBaWEZWY1dxWGU1TktwaE91RFJLNWwrTGl3cXRRMVhYbjNUeVdTeVdTeTdjbGtzbGtzbGtzbGtzdTNKWkxKWkxMdHlXU3lXU3lXU3lXU3lXU3lXWFprc2xsL3dDS25zSmZLWk1DVVU5N2JZQjg1VWN3aUdqdFlMUkJWWjFabHJxUHovSk02ZHI1TFRDcHRjMFE1TWE1cmpLZzNUMkVOc21jMDRrZE81VElBelJpY2t4bGlZQVRtVTZMakNML0FDTGZ3VjduQWdBSVQ3UkhmZTV3SURRbW00ZGozT2JFQk5kZDJ0TS9qaGFEMUNKTHVxTGdESGVMV3U5SUlBTnlIWVd0ZDZRUUFZTWtDSENSMkFBZE82SEFtTzhYUWc2VEFSSmIxQ3ZWNnZWNnZWNnZWNnZWNmN4N1JjUXIxZXIxZXIxZXIwYm05UXIxZXIxZXIxZjZ3UktBTHVpNmVZT2FBRFJBN0x3cndybGVGZUZkbkt2Q3ZDdkN2Q3ZDY1FVekl5RSs1NWtxd28wbkFTclNyU3JDckNyU3JGWXJFK282b00xYVZhVllWYVZiQ3NWa0o1Yy9xckNyU3JGYVZZclBVajBWRnRSczRoNzFLb0dkVTkxN3A4M2FWYVZTSnBub25TNHlyU3JTb0tncTBxMHEwcTBwb0k3WFZnVzJ4NTRqT2ZOT0JJeVhzUWtLMStKZDdGUnFZYjVLZTY4a3B4QjZML3A2b0hzZStZSG5HUkdhaHE4bndVTlVCUGp0ZE1xU3MxbXMxbXMxbXBLelRpWVV1VXU4VXc1WjlqT3FnU25SMlA2S2NsSnQ2cTUzaXBkNHE1M2lybmVLYjZQcWpHM2VxVTNCckNIbWZETG9uR0FWSmlVUU90eXBjTml0YWJ4bi9mNy9BQW04S3lvU0ErTS8wWEY4R09HTG00Z0pFL1JZam0ra0V4NWVMdlozNDlRaFFvVWVwMWE3bVZBd0RyMnNaZG1pTFRIcWRSd3RPYURteDFWelBGQnpKTnlwUDRhOXVlVWZPYy80VG0vWjBtSEZVNmZBdXBPYzV4dThQNkZWRnY4QWhLbXQ4RVRWdUFIUmNLeGhxQnRjK1Q3VVJ3cnprZFA4cmlhZkNOeXBPTTUvSDhrUzV1Ukt2ZFBWQTFIZEZTdXd4NHJ5dWlOM3NXY0l5T2lOeFFFZXZQNHF5c0tOdlh0WXk2VVFRWVBxYnFUcGxHbVMyMEJjdTljdTlVNkxtdWtvdUI2dFJxc3RJREU2dFRrMnNRcUNQS2JLcVZMemtJV093QXVGR0ovYUQ3UGpLYzROYUtXRDVXWDcvVlc0WkpMZlNYRVUzVktsNENGTXprcUxNUHFyZ3BBN1dPdGNDaUFUSlVxVktsU3BVcVZLbFNwVXFWS2xTcFVxVktsU3BVcVZLbFNwVmFwd2c0b05xTzh2dFl5NUVFR0Q2bTdKcFFPU2xHcEJoRU9BSjhGY3BVcVZLSEgxYmc0K3o5b1E0MnFPbncrblJONHA3UFI4Q04wL2pLait2OXlqMnB6UTdxc05xc0hWQmpSbitCMUhuR0RjT2ZqMnNZWElpREI4KzQzZHlwNkJUZWc3QlRhK1pXQUhQRksvcUoybE8relEyZnZSa3FmMmZpVWpWdnlWZTZpZllmeVdOLzJvMW9kYkNFKzM4S3JjUVc4VTJsZkh3N1dNTGtRUVlQcVRoYUFTbmVpVU9uY2RoWkVJY3VRYzBlWEJGcWJnZFNxZUhMci9BSkxqT1dmbHc4OUIvSys5VGNRNWxjT0tkMzN4eVI1VU5EZnkvWCtGV0hDZ2ZkejdmNFRNTU5nOWMvT3RpUmQwUXk5VzRoNUhITkFEZDgvOWZyMnNhWElnZ3dlMlZLbFNwVXFmTWtFbVpUdlJLSFR0Tk40bVIwN3RLbzJuTnpaVHVOcEcwQ25BSDZpUDVWUGo2VExacDlBSTNsYzFUdzdSVHo4Zm1FSE1iVXZEVTUxM25aVG9jOHZUdU5wT2RkaC82OENGVnRxRElRc1BQcWdBT25xbFg3T1pWNGtjU1h1a2V5Y3UxalM1RUVHRDZtLzBTdWdUUTU0eVhRd1ZqT3RMZnhCOVJ4NGdNYWUxalM3b25TRG41NENUQ0lqdU85RW9aaFliV2lRVlE0TnRVQjE0RXB2Q1U2aElEb3ovQUVYRjhHM2hpNW9lREVqWlh1YjFDWTh2emo4TnRQTjNmRHgvVHRZMHU2SWdnNStwanFqMk50enVWVXNrWVhiSGNnL2hMK0pGUDdSc2NXd1Ivd0MzYXdFOUVablAxTWRleUZDaEI0OEVYZ2lJVHExM1grNVFtVjJ0eXRXSUNlaWZWYVcydGJDcDhTOWhCdDZXL1Q5MHlyWUFEVDloK3Y3S3B4TGNYL0Q1TFNjdmtCK2twbkVDaXkzQjZnZi9BRk0vUG9tdWMxditPWUg2OWZxbTE3SDNQb3ptZitGVTRsa1JhQjArbjdwam1pNkJtVThQZTY2T3F3bnhNS3gzZ3NLcGJkR1NkUnFNQWM1dVI2STBLalhBRWRZK3ZSTzhndzVYdDZUMk1OcFZJaWs0T1ZldUt6R3N0aUJIOS9OR202NFFjbGhPQWdPVEtkdVo5VHJjUlRieFRhWnB1SjhZeTM3V0J4Nkl6T2ZxWWFlcXNNcTBxMHEwcTBxMHEwcTBxMHEwcW54TmVuTnZ3K24vQUFuVjZyekx2N25LYlhyQi9YMi9MWkhpYXpXMkNDSUgwTXFweFhFVlcyditQMWcvb2h4VmErNzgvcUZWdnEra210SWlGajFIT014L2NsenZFNVo5RFB6VGVKcnQ4cHBnNWZUb3VhNGdBZ1ZQOS9ES2Y3MFI0dXRVRGJveVArMVc0emlhZ2d1VlI5UjRnckJ6dVZwVmhVSHdWcnZCV3U4Rmg1ZFVHRW8wNEFNcXd4S3RLQUtMU3JTclNyU3JTclNyU3JTclNyU3JTclNyU3JTcW4yWFFxMXh4TG0rV1BpVllWWTVOYTVGcmoxVmpsWVZZNVdGV0ZXT1ZqbFk1V09WamxZNVdGV09SYVIzUjBYejdQbXZuM3ZtcFVxVktsU3BVcVZLbFNwVXFWS2xTcFVxVktsU3BVcVZLbFNwVXFWS2xTcFVxVktsU3BVcVZLbFNwVXFWS2xTcFVxVkdjb3AyWVRtZ2R5bzhzNkJOTndsSG9tTkxZOXgzY1RUYlZGRTlleHJyaEk3V0J3anI3akdtd3V2SXo3R3REUkE3alE0SDIrNEw1TFRDb0NxMEVWZk5WSHZhNW9hTXZjVmd1ZEI3MVI3MnVhMW9rSDNGQW52ZVRaOGZjQjF4YWJlcTRaOVZ3SXFqcDNBWTczazJmSDNFcTE4S0cyektiRTVwMFRsM2ZKcytQdUs5eGIwUXpFOTN5YlBqN2l0YmNZUkZwanV6VnZpTXZjUnpnMHRCOXFCTFRQZWZVTk40a1pIM0VEbkRwMzMxRFRjQVJrZmN0OVhEY0FlaDl3QjF6VHJmWjJ1cVUyUUNNejJmWnJHdjRnTmNKWEswTkEyVkRod1d6V3B0bjhsd3pNMkIzbFQxKzdpTXZIODAybjB2b2pvUFloVGtrNFEyVGFiWEZ2M0lIeS9OTm9VL2JUSFR3VGFZTURCRS9rbTAydTYwUVBsOFZ5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmx5dERRTmsvaGFGcDhnYmQ5cmkzTnBXUFYxbmRZOVhXVnpGYldkMXpMN1l1TS9tdVlxYWp1c2VycU82eDZrZWtkMWoxTlJUcXRadlZ4M1hNVmRSM1hNVk5SM1hNVk5SM1hNVk5SM1hNVmRSM1dQVjFIZFk5VFVkMXpGVFVkMXpGVFVkMWoxTlIzV1BWMUhkWTlTZlNPNng2dW83cm1LdW83ckhxNmp1c2Vyck82eDZ1czdySHE2anVzZXJyTzZ4NnVzN3JIcTZ6dXNlcnJPNng2dXM3ckhxNnp1bTFhN3VqaXNldHFLeDZ1czdySHE2enVzZXJyTzZ4NnVzN3JIcTZ6dXNlcnJPNng2dXM3ckhxNnp1c2VycU82eDZ1bzdySHE2enVzZXJyTzZ4NnVzN3JIcTZ6dXNlcnJPNng2dXM3ckhxNmp1c2Vyck82eDZ1czdySHE2enVzZXJyTzZ4NnVzN3JIcTZ6dXNlcnJPNng2dXM3ckhxNnp1c2Vyck82eDZ1bzdySHE2enVzZXJyTzZ4NnVzN3JIcTZ6dXNlcnFQZWV6RUVJQ0JIY2V6RUVUSGR3bm11YXBkbDNuR2U4OXBjMjBHRVRQbktnTDJGZ01Kb2dSNzQvL3hBQk5FQUFCQWdRQ0JRWUpDZ1FGQkFNQUF3QUJBaEVBQXhJaEJERVRJakpCVVVKaGNaR2gwUlFqTkZLQms2S3g4QVVRTXpWaWNvS1N3ZUVnSk1MU1FGQnpzdkV3UTJQaUZTVlRnN1B5LzlvQUNBRUJBQVkvQWt5SzlFaGlwYzA1SUh4NzRDSmsxRXFaL3dEblNzbmJwTzV1MkhWT2xvQ3FTZzNMZ3BVUmtQc2tkTHhKVUpza0ptNU9TK1Q1TnpmTjhtNGlYOHF6SnVrWExTcVNRd1krYVl3ZUxxUUpVb0t6TjlZbEh2RUt4ZWxscFRTMUpDbmQyYkxlL2UwRUtteVFvRUM3M2NGUWEzQUhPRnlEaUpHbFRYcWhWN0pLdCs2MFNpaWRLVUZtbTRVTDlEUEdJVXVpbVJOMEsvR0I2dWpQL0Q0ZGE4WU1PcWJQTWtKS0hkcWNyM0xyVGJwTUhFR2JKcEF5MW5PcVZObHdTYjVSZ3BpcHN5VExuaVlWS01wMnA0Y1hlUGxDWnBmSlh0WjFrRysvSnQ4SVFpYVp5Rm9xQ2lnb09aR1J1TXY4WEtrSVlMbUtDQlViWGdMVk1rcWRkREFuaVE5dzI2SmtpYTJrUVdWU29LRDlJaVZLTXhNb0xVRTFyeVR6eE5uSnhZeEswenhLMU1tcGVGWW5FNlZhU3N5a29rQzRWVForazVkQjRST3dVa3BVcENsQjVpZ2pMcExSOG1mL0FNbi9BUFVyNTBlRS9LR0kwN2EraVlKZm1qNnd4bnM5MGZXR005bnVqNnd4bnM5MGVYWXYyZTZFS1RpTVF0U01pc2d0RXJGSEhZbDVhYVVvMWFXdnpjOEdjdkhZcEpObVRTM3VqNnd4bnM5MGVYNHoyZTZQckRHK3ozUjVmalBaN284dXhuczkwZVhZejJlNlBMc1o3UGRIbDJNOW51ank3R2V6M1I1ZGkvWjdvOHV4bnM5MGVYWXoyZTZQTHNaN1BkSGwyTTludWp5N0dlejNSNWRpL1o3bzh1eG5zOTBlWFl6MmU2UExzWDdQZEhsMkw5bnVqeS9GK3ozUjVkalBaN284dXhuczkwZVhZdjJlNlBMc1g3UGRIbDJMOW51ankvR2V6M1I1ZmpQWjdvK3NNWjdQZEhsK005bnVqeS9HZXozUjVkalBaN284dXhuczkwZVhZejJlNlBMc1o3UGRIbDJNOW51ank3R2V6M1I1ZGpQWjdvOHV4bnM5MGVYWXoyZTZQTHNaN1BkSGwyTDludWp5N0YrejNSNWRqUFo3bzh1eG5zOTBlWFl2MmU2UExzWDdQZEhsMk05bnVqeTdGK3ozUjVkalBaN284dXhuczkwZVhZejJlNlBMc1o3UGRIbDJNOW51ank3R2V6M1I1ZGpQWjdvOHV4bnM5MGVYNHoyZTZQckRHZXozUjVmalBaN284dXhuczkwZVg0ejJlNkNaSHlyOG9TQ1F4TXRRUzQ2bzh2eG5zOTBZSDVSd21NbXpOQ1ZWb25BWEJRUlp1bi9BNmhVRjZhNFdFYUlvMW1ZNStiQ2RlWk1JUVhaS0VwSmN0ejNzR2hSVWhNMVZnRTJ2Wnoyc24wUGVGRVNVVEFaYmdjRmNQM2lsTWhKSnExaTJyYTIrOFdrQlRteFUyVDlQeHhpVVYwb0xhNlczeE9LRktLbm1BQllOVlZSYlBkbENpbVNreTA2UUFxelVRV0crSm9UaGtLUWt0Yzc2Qlllbm45TVRCZExFclFRa0J4UnM1OFQyUVRLbUxFclZ0UFNpdmF2bGFGNkpTeTVLUVpxVUJ0NExEZFp2VEFHaFNBcGk2cjBncUhQdUQ5VUpUb1U2V2xldVd6cDFlMkZVeU5HZ09BUTFSc1c2TDlNTEJrQk1xcG5HYlB1dnd1OFlNekRmUXAwZ21BMXUwZUtWaWE5RWFiSlBqTDdWVitFRUttS1ZyaHRBbERFTjlyTHRoVlRUQUJhd3ZUYjJySDBSTFVaU1VvclpTQjV0SXZueGZxM3dGK0NwVW9wK2ozQTFIZS9Cb1U4clNMZG1zQm12bis1M1FqU2dvOFV2SjZPUXo4KzFBcVNaaXY4QXhqOTRtcWxTalNzS3BDdHBESnQydkNpckNvSUMycFNxNVR4K09JZ25SQ1lwYjIzSVpQNm4zd0F2RFM1U0tVbXAzdTErMkpwMFBoQk9zZ0tzMWt1T3NxNm9LWmNvSk5kbHBiSzNIMHhOMHNxWEpaZW9EZXBNRnNFbkxsSGZiOTRVb3lBWjUyVUp5SHBlSmpTd3RJVVNuVkZ4ck1NL3U5Y0lFNldFeWFWZzA4WDFld0hyaVRvcVZxcU5iQ3pVbHUxb1Q0aEJCV2tGN1VwYThFVGtMcUtrQUhVc09WR3FWNk9zc3dSUzJydGIvT3lqRFZCYzJZRlZUOUlFSjFXR3FsdWs5VVlnSjArajBZb0tBaXl1Wi8xakIxSm02VkswNlpnbGxCci9BQUlMcG1qRHFsQmdvSlpLditySjBDa0NWLzNBck0zR1hiL0JvRktVSmxBWHNLWmlxa1haczROSkNtdGFGT3NwcG1hTFdRcExxeXM0djZJQzVacVNkOEpScE5aVXZTaHdkbTNlSVVFTFNvcHpZNVJObGFVYVNWdGc3ckEvMUNBa3prQWtGV2U0RmoybU5IcFUxc1N6OE0vZkJtRTZnRHZ6UTVtQk5uWmVxUjZEQVZMVUZDRExYTlNsWWF4UHA5d2dTMVRVaGRLbCtoTFZmN2hIaTVpVjlCandWVXhwOUdrcFl0VGZmbHlWZFVMQ1pxVFNXTitaL2NZbUFUMGVMTktyNWFvVjdpSVhpRnE4U2hKVVZDOXZSQ1pwbWFpZ1NMRjdCemFKbWpVRjBLb1UzRmdmMUVURUNaclM5b0tCVDZiNWpuaXBDZ3NjUVlJZGFDQzNqWmFwYjlGUUR3QWxhU1RsZk9HV3BpejlyUUVJS3FpbXRpaFF0MVFsTXhkS2w3STR3WnVuUlFFaFo0Z0VQbDBSVHBVVlBTMVcrQ3Vhb0lRTHVZU2hVNUtWS0JJYzhHZi9BSEQvQUNrZk9xWXBLU0ZCQXByODFWUTVQR0ZwdzJGa1NVcVZXUWhUT2Z5eFZOYWF4ZExyYW00TFdUeEE2b0V0RXVXdzNhUTkwRTRpUkp4RzFhWWJDb2dtMVAyUkJLSlNBL0dhVHZKNGM4VFprMUNGaWJ0b015eDJmcy9ZVEFFdVdKUlNneXdwTTR1QVFrSGQ5bE1UajRMSjhkOUlDdDZ1blZ2a0k4SFRMbG9sMFVBQmVRL0xDVE1BbVRBK3VacGUvb2hZdytHa1NxelVxbFdmc3dsY3lSTFVwSkpmU25la3BPN2dUR2xSSWx5NWxSVVZKbUc3MHZ5ZnNKalRpVEpWaWxKb1ZQZGxLRnMyVHpEcWhVMmJMUW9MbGlVdEJYWlNkYjdQMnpDQWNISXBRQ0VpdXdCUlJiVnRxMmlaVktTZEp0K09PdFpJNGNFcDZvbVNsSlFCTURFaVovNndsRTh5bEtTRkRYeERiV2RtaGRFekQ2NnFpK0pmY0J3NEFRRmFXUWd0U2FjUm1PR1VGbFlZUGY2ZjlvWHBOSGlaYTNCR2xzUndzbUVMME1zclFTb0tNdzVuUGRDQ3FWTEpRYmVNUGRFcVptSmVTVlRTZVAyZWVFVmhJQ2VUWGJqNXZOQWxpU2dDZ290UFZrVXBTZDNCSWhKVmg1WnBJSUJtbGcyVzdkMzhZS05WQUlJTEx6OW1CTWw0ZVhLWFd0WlZMbVVsUlZ0UHE4d2dBUzVZSCtwKzBiRXYxaDdvMkpmckQzUjlITDlZZTZOaVg2dzkwYkV2MWg3bzJKZnJEM1JzUy9XSHVqWWwrc1A5c2JFdjFoL3RqWWwrc1A4QWJHeEw5WWY3WTJKZnJEL2JHeEw5WWY3WTJKZnJEL2JHeEw5WWY3WTJKZnJEL2JHeEw5WWY3WTJKZnJEL0FHeHNTL1dIKzJOaVg2dy8yeHNTL1dIKzJOaVg2dy8yeHNTL1dIKzJOaVg2dy8yeHNTL1dIKzJOaVg2dy93QnNiRXYxaC90allsK3NQOXNiRXYxaC90allsK3NQOXNiRXYxaC90allsK3NQOXNiRXYxaC90allsK3NQOEFiR3hMOVlmN1kySmZyRC9iR3hMOVlmN1kySmZyRC9iRGxNc0RpWm43UWd0TG9QS1N0LzArZXZRekpnb0FCbHpLV04zM2ptaVVCOU9CTGVvdmNFUEV3emdBN01CMGRKaEV4VXFsU1ZGNWo3U1c5NWZzaFlRYVZjWTExVkdLMXJzMlFoZEljeHJpNzU4WWxLcEkxZForaUZDNzd1R1VJSEJQZEF5SXlKZ2tKNkNZVHV0RTFVMmE2Q2RuamxmbTZJU1VyWWU2QkNLMld5ZnBCYmVyZDZZbUpFdFN4VzlEYlZvdzJnRXpEU0JWWEttRFdlcExaRGdGL21Ib0o4U1Y2Tm1aZE5ibm15YUoyazFUWDRzaENqcXhpQWtHc29ZUGE4SlJpSnVsWHg0YzBUSnExaFVrcVVSZjQrQkFLRFVpbHFPZmpFc0syaG0wTDFHdyswRlZiK3FKVXlUSzBreW9KT3RrSGVCTFE2bXRteGcxSW95M3dna05UdlR2aENwUXhCSUIraG1CSFc4U1JOTHphZFlqaTE0bXJWTUtVQTJIblJwRVNwazVrcHBDVFo2alZaK0RSaHF3UXVnVkE1dTBMbk5UWnJEUGova0xPMXdiaDRXRjBzVlZBcDd2blNFQXJKKzAwU1EvMHFpbHlxMXVlRlRqS25vUWhLVnFLdHp2MTVBMjR3cVFaYzNWSzZsaTlOSUpQWlQrY1FhRXoxQk8yYlcyeHg0eXp6YzhhTTZSQUFtRlNscVNHb2JuNTRvU0p0SG5FanpacDNIL0FNUkVhQkdtQ2lhYWl6UHJjLzJEelFnSkZaTHZyTmxGVXpTUGV3NXM0bG9HbHJYa0xkZWNKcGRheXFsbmJjOEtNMVJsc1d1WUNVaWRVejAyNStmbTcyaWJNSlZxSnJhSnlrVktveWExWFhBbERTS21sRllRQ2wydHovYUhYRXhGS3loS0tuQkRtL0Q0UE5FeENsVEswWmkzMmI1NWE0dnpHTkdOSTRVVXVXYXhBNDhTSXBDSmxKcEFXNHpKSTQvWkppWFNKc3ZTbzBpQ3RyaTNBbmlJMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFkyakcwWTJqRzBZMmpHMFlSYy9BK2NWVUs2VVFta3BTQ0hhbjk0OFpNQUFhOUZoMnhlYWdkSS9lUHBaZWJaZnZEYVZEOEtmM2o2V1gxZnZCOGFqOHY3d1VLbVNWRkJwWlNNaXorNkdRdVczQktmM2k4eENlbFA3eFFvb1VuTzZIL1dDb1VoSTNKUis4VkJZRjIyZjNoUVZOUVVxSERkMXc2WnFFaFF6TG13OVBQSDAwdmR1L2VOdFA1ZjNpcE14Q3NoWWMvVEcybjh2N3hvdEltdG4yYysyQXBNOUZLZ0dMY2N0OENtYWhtSEovZU5YRVN6Y3B5M2dzZDhXbW9QUW45NDFwcUIwajk0SXJUYjdQN3hVVmpOdG45NEV6U3BibVMvdU1mVElIU245NHAweUg0Tis4V25JUG8vZUFvVDViRVZPMjdyaDFUMEFQVGRPOTI0OFlBVk9RQ2ZzL3ZIMHlPcjk0dk1TUHcvdkNXbUpMbHRuOTQyeCtYOTRLVXprRWpNTmwyd1Jwa09DMnp2NjRwRTVCUDNmM2hDMHowRkN4VWxWTmlPdVBwa2RYN3hlWWtmaC9lQTB4SkIzMC92Q2xsWVpJZlovZUZrelVpaXhaTCs0d0ZhWkRIbS9lS05PaXBpZG5vNStjUWZHb3R6ZnZIMHlPR1g3eGFhazlDZjNpblNvcTROKzhBekpxVVo1aTFvVXJUcEtVbGlRSDU0SGowWC9BT2YwaEtncXlnK1ViWFpHMTJSdGRrYlhaRzEyUnRka2JYWkcxMlJ0ZGtiWFpHMTJRWkdsOFlQczJpdGMxS1U4VEVzb1VDbGF5bkxnL2RDVFYyZlBLa1Vwb1h2ZStTaitnK01rTm8ycEdhajNScEs1U0ZVMHZkeDJRb3pkRmNNejg3K2JGYWFMbXJiUG5LVjV2RlJoeG9tc1FLaXc5bm1nSWxtVlFrblZTdmlHODNoQ2xKS0tsVk9xcTVxTG5rd3NDaElWWXNzK2FFK2J3VENwZzBRVVEyMGVjOE9lS1ZpVXBQQ285ME84cjh4N29LWGxYKzBlNkRLOFVaWjVOUjROd2hTVnFRWlpRVWtWbTdseWNvVURvaUMvTE8vMFFWZUxVc2xLaW9yTnlQd3hyS2xEOFN1NkFpWG9Rak1NczkwRzhtMzJ6M1FyRWFTVFVwa3M1R1E2SVNoQzBKWU1OYzVmbGhNcEJsVUFVYlp1QlpzdWFHU1plYm5YTjlZbnplSmhhazZJVnFxT3VlQUhEbWhOZWlOSnFHdWJIcWdsNVYvdEh1Z0lKbDhkbzkwTGxoU0FoU25ZS051YkxoQ2ttalJsTk5OWjgybmh3aVlRcEFVdFZaWmZLZDMyWWtwWkhpa1VKTlpkcVFuemVZUnFyQ2VHdVMydFZ2VEFLVXlnVWdKRExOcVZWRGs4UkRLVWs1SGJPYlo3TUpLZEhZaHRiLzFpK2ovTWU2RXBsaVNoS1M3QlI3by83WDVqM1FsU0ppRVVrbHFpM3VoM1NOYW8rTU44K2JuaVg5R3BZV2xRSlZ2Rmh5WVJMSlFaYUJTa2FRMkRBTnM4MEptZ1M2azVlTVA5dlBIZzB4V0hxWHlDczM3SUFSb0dScXRwRGJzaFFVWkNRclZmU0hmYmhDbEpYS3BtQWJDenpNY3VhRXVVRUpkaHBEdlNVK2J6dzh6Uk1TUnJUTjZpT0tlTUxUNHNCUmZiL3dEV0ROOFhVUyszL3dDc1VJMFRmZlA5c2FXbVRwR3Bxck9YVkRLbHlsTVNObzhlaUNtaVdFbmRXZTZQb3BYNXozUUFFU3dCWWE1N28yWmY1ejNSc3kvem51alpSK2M5MGJNdjg1N28yWmY1ejNSc3kvem51alpsL25QZEd6TC9BRG51alpsL25QZEd6TC9PZTZObVgrYzkwR2RvcGRaKzJlNkdwbHQ5ODkwU3BhbUNTcFExVlgyVmN3aGVuRXdLVWJWcUJzM044K0dtQ1lwNUlKS1VrdGx2NGJ1dFhHRVc1SWpLS2JqcS9XSk0wcVVETFNvTWtzN2xKL3BoZFdJV3NrNnU2bnRpWWpTa3BXVkZRNlVnY2VhQ2RNUVNPVFlmN29DL0NKZ1VINGI5M0hoa1JsQUtacDJpcnBmOFVTMENlcFRacVZuc1V2bnh2QThlck1IcTlQUkdYYkdYYkV4VHFOZTRrTU9qNTlSUlFSa3ltdjNRaVdwZHdFdnZKcExwdThQcHk1Sko2aHo4MEpDc1NwUlM5MjV6OXJuYjBRbFUxWm0wNVB4NjRVZElWSkpVYVR6cUo0ODhJOGVVc0dMYi9hZ2t6VE1zekgvbjVpbTZYM2czaVdMcXBET1NIaktNdTJETWNsOTFvVkxSTjBUaFd3R1p3ejdXNkNETXJUZXl3L0tjRFAwUUtWbEdzNUF5Nm50QlQ0UXBqeXVWazNIMHhYNFF1cW1sdHpaOFg3WVhST1VpcnMxbEs4NzdYWkFTbWFRbXFweVhJdGxuQ1RwMUVBdXgvd0NlbjRFWlJsQTFsS2RZT3NvV3ZHMk91TnNkY0NmcFpSUUtkVlE0SFAzOWNZb3lzWWNQTW5sNmhkdFVESithR09PSkduRTgxT2YrNVcyMTBKOUdVQ1VuR0JiTlNWQzNVL2RDWCtVRkthbmpkaGM3VytKVXZ3c29sb21JV3prMVVyQytQTjZJblNSajFvbXJHck9DaTZWTXo3VjRJckdaUGJHMk91Qk8weHlwb3ExWVVKVTNSazFlbThCS3BsYXZQYURwSjVuZEtRUGRDNTZabWlrV3RuWGFDbVg5THlTN1Jvd210SVRzNXYxeE5mQzZCeTRPcmx3c1lsekFnRkkvN25tODJjWWRkS1RvbnVjK1QzZTZBazIvRlY3NG5ta0lyWFpJeVlaZng0Zi9BRlA2VlFTSllsTERTMUpGUXlHK3E3NjJlKzNvK2FWUFI4bG9tVHRKUWNYU2dLU21uYWZhNW9sdFdUbzBuNlFwRFV3dzBpc3krbFY4Yi9mQ1FaYzNXeWFjcUJxclNHZW96MWNZVlZ1ZGduRUtjOElsNnEwMUJXcXFjcS9kRW1ZaEs5WmlwSzVxZ1FHZnJqeWVaVFNEOU1ybnk2dTBRdEtKVTJZdEtBb0swaW1OcnhXeXEzVUdFNVRXVlRuKzBDbVd1amxWelZnNUh1N1lUUWhWWEtxbkxBSGZDWks1YXBrd2g2MHpGSlQ3eTBURjZLY0tYQ1VHWVhWdzN3aVlyRFR0WTdHbFZVQnp4U1VMUXpPZEtvMmZ1aFpUSldtWEx6U3VZcDFlbDdkUmlVK0dub0V4UUY1cXRVYzhUYXYvQU01UlVsYzFTYUNVbCsyQ2lsYVFBazE2VWtGNEt2Qlp5dFdxblNxZk5taEFBWU5NMWRPcGxBVU1UMW1GUzZKcVdMQldrVmVKcVVKWHFKSlNWVFZNdTBZbVpMbGt6SlkxQnBsR3MwdjhjZWFKU1phVFRwQ0dWT1d5aGQ3OXNMUkx3ODVocEV1cGE3bElCSG9PVVMweUVUSFN2bFRWVXpmRnJQdkVTMElCb0NWNXoxTXZWU2YxaWFVb1dhRzFSTVZyWGhJYWFRWGM2UlZyUENYbHJwVlN3RTVkVitNZUlXSlF1YXBpbHFmOExqcmVLUkttaFRyQXFtcWEzSGc4Skp1YjczMzhmOEhTSHBkVjN5dkV1bFdmRzhKdFd0UURJTTJsU25MV2hSMEt3d1VxZ3IxMkNpTm5QbjdJbGxVdWFsYXlsSWwxYXpsUVJrKzR4TG1MUXNMT2FhNlc0bk93M3YwY1lJWmF3RjZQVlZ2clNqK3JzaEZOZEt5d1ZVNHlmakcwZXVObzljYlI2NDJqMXh0SHJqYVBYRzBldU5vOWNiUjY0a0Q3VzgvWk1MWG81cUtwaEkwOHN5MW5WRnluZDgrbVNtbVVKZ08zdEMyNzBSTFV3SjBRekhOQlZvS3lGRVVvUUgrTi9waENWL0o4cVdTdFFjS0xCQUl2ZEdkOHVhSjM4aXVhbVhmVVE5WFF5YzlycUhHSlZlQnJDc1BXb3BRUXk2Z0d5dG1UNklsbFB5Uk9sS21wU3BLeUJRQWNxaVc0YmdkMFRkTmdVcnBXRXAwRjZoeDJZVGlSZzBpVWM1ZkwvRGErNkFTaS9PQkd3a2VnUnNKNm8yQjFSc0RxallUMVJzRHFqWUhWR3dPcU5nZFViQTZvMlIxUnNEcWpZSFZHd09xTmtkVWJBNm8yQjFSc0RxallIVkd3T3FOZ2RVYkE2bzJFOVViQTZvMkIxUXdzT2I1dDhiL24zeHYrZmZHK0NOelJ2amY4KytNSGd3QVV6bElTNUhFdERsTG1wVy9uaTh0NFFnSWVZdmMvN3hoVTBPaWZLVk1HdGV6Ym41NE15VEpuTFJXVUFqbEVEZGVKaWxTMXFTaENaaFVsVm1KWWI0bjRjb21CVWhJVXMxQmc2aWxzOCs4UWlXcVV0UzFxU2xrcWU1V0VjZHhJZUpVbWRMWFZNNEhJVXFVVG5scW5zaVVveUpxVXpkazI3K05va3BWTFZMVk5wVUJNVTJxZVZuRXlWTGtxZVdMcXF0NytucWpZN1RHeDJtTmp0TWJIYVkyTzB4aHZ2ZjBtS01MTXd5Z2xWMFlhY3ViVHEyY3J2OE51L2dsY05FUGRCclVvcDRNS1czYnZpL05HNkpabEpsbkQxaDBwMm11K2ZvNnZSR0w4SmtTbFRaazVOTXRiRkdqcFFGYitOY0kwbUR3eVNVM3BYc3FiTFBqYUpVcVJoSlUyUXF4bXEvNXNJcWtwU2lhbXdrQWdvYnFFQ3JEeVFDL0t5MWJiK05vUkxsU0Vxa2twR2tKL052dEUwNFo4Vml2KzJKeEFjOWtOb0prd2pUYXlsZ2ppamxaYnZSQ1Y0ckNvbG9YTGxLenBJSkd2YStWb1dsTS9TWWNKRFpWcU5kNzh3Y1FrRUFKckxySnlTM1R4aXBhVkRWUnFwVlo3MWIraUpab1Q0UnlnY3MrbUV5bEpGd1RxZEhUeGhZbFNsSnpBS2luaFk1OFlBbGhLNXRXc09hRFRMVWFVazFySWJhNlh5aVgvQU52eGl3VExPU0xzZW5aaFltTHJrdHFxTzAvK0lQM1IvRkkrVDZVUE5hNVVwNy9oYnRqMHE5L3pJU0dNeFc0eGhnbVdWb25TeXNGSkQ3cmRzVEplam1QTFhveXlhaTd0a04zUENKUWt6eXVZYldLaCtiSVJvUmg1d25LWFFwVkREWVVyYXkzSDB3cWZvNW1qQlpxVFViSGtrUHV0eGlYTm9ZNWlvdVIvMGNQMC93QkppaGExcjFyS21UVE1MTjg4dkQwTEtsQjZoa004K3I0dkNSLzRmNlkycW1MdjA3dTMzZk1ZS1VyTXMrY21KaUUvS0NxVnZ5U1dKcXVIVWVJL0x6eEluVGZsU2ZQVEpOUWxrcURtMzJ2Znh1OFljWW41WHhCcHBVdVhXdlBQYUMzaWF2RDRyd2JTVEprMVdpUTFSVng0dGVKNVhpSm1JWE5vcVV0U2pjSlo3cU9meGxFdEN2bGN6TkdLMXkxQjM4WlVEdGZaYUp1TS93RG1wNHcra21HbFNTbWkxTGEzQmp1Z1RKT0xuSW1TWnFCaUZNVW1hVTNBT1hIZG5CbEl4bWdYNE40T0ZTM1BSTTNYNk9PWnRGc2VGNk9iTVdKUkpOTHk2U25QZFUvcGFKdGVLSGptb1NkMXQxNGxFZktjelhBbGhlc29FdnFuUC9sN3hXK2xwZlZLcjNQRlJnVFU0MHBFeHBvUUwwdUxjcHYwaEt2REpvYW13VXBpeEpQSzN2MmI0blMwNGt6bExsTGx0TlVxbldPZVp5K0dpV1o1MDZwVGFNbXhHeitxWC94Sis2UDRzTk9tREZlRUpwcDBZOFhudmowcTkveklBWXpGR3dNSW1JUlhWTE14TitBeWhLa1NKb1hPQlY5RXhMQXMvU0UyZmhDYVpRbFBlbHN2K3BJVWtzYXY2VEV0T2tDaGQyNkQ4L3llcFV0TnF0SE0wTG42T1k0cjNiclJMSEdVUGRHalJkaVRWenVYSHY4QW1tSURybUp6UWtYNW93WDBpZkNiNjZHb0ZLaTZ2eUdBb0Z3YmorRmNwSzEwTFRRdEttWlFxcTlHOFd0ZUtsMUtaVXhTUVcxYTlwcmZEOExSNHlkT1Y0MlhQTnhkYU4rVzloMWJvQXFtdG85SFpkRnFxdVMzN1E4d3EyRlN5MjhHS2tsUTJiV2F5YWVIQ0VycW1hUktBaEtueUFJSTl3Nm9NMldEVVNvMzUyL3RFQmF4V29MckQ3ai9BSXcvZEg4V0hIL3hzcWVkWCtaVktVVkp2eHBidGowcTkveklGSVhOVmtERWxRU1NtWkxWTUZMWkFQeGhLbFNab1FwS2xCZXF4Wk5SMy90RXlUb2lBbFV0QTFrNXJUVnhiL3FTUHY4QTlLb0dpbTZSbEZ3ekZOam44OGswek5Da3Q5R0NuWVZlcmRjdGJod01TdjhBVEh1aFhpcUxxYlg1emR1ZjlmbWxBNGZKUVNKcWhrNDNIcFlSTXhTdms5QVZKbnBseVU2TmxKQlNqOVZxeWhIOGhpVTFvckRwNW5ZODdSTHd4d3MyWk1YNW1YUUlPSVJJbWFNRnFKaUNoZlZEZUI0aTdzYWJXUzhKdytnbUtXU2tPT2VORk1sOG1xdE96L2toKzZQNHNOSUNzWUVxcHRMYlJaNzdSNlZlL3dDWkRvQzVpc2h6VytPbUpSQU5DNVptQ2xyQVJMUXFWTmFjYUJZRWRoNS8xeXZCYVhPMnBpZG56RkZKN1VtQTRYZkpxVCt0dlRIakpjMUkxN3NEc2grUERkbkVoUlRNR21kcmM3ZkRSbzlITkNxSzdnZmJ0bi80bGMwUG81bkFBMGdrMHBQSDdRRUlrcFNzcVV2Ujd2TlVlUDJEL3dBZnhZZjcvd0RTWVFGS2xGbEV0SjJRNk9GMnUvem4rZVk2WlBpbXNCNXZQeXI4L01JbC93Q2tQZEJyWVh6ZmRkdjBqTVFiaURvVEwwbTZ2S0owdWNzTWFpaGJDc2JWczI4emNkOFlSSVJLbWdzQ3BRZWtoQWR6dUJOVitpTVZwSjRsVENsSWxDWFN3TGh6bDB4TVFoY3VXSFhTdExGVFU2bWZPM1hHRDBhblVoWTBqdHJCaS93SVduRjRoQ0hLaTZOdE43Y3pOelF0NkNrVlVnSFBLbCsySlNweW1WVUN0S1RacUwrMUNrNFdicGNWeVZ6dW5taWV0Z2xBclRLUlU3N05KUHQ5a0R3a3lFV2x1d0pPenJ0NmNvbUJFelRZY0JKUzMweTlkMWNBTld3aENIU1U4cVk0ZmR1YnBoYWx0VzJxbDlWMlA3UktVVnAwWExRNC93REh2Ym1tZGZWdElsenFGaGdYRlhKdTN4endnbWFaSnFXNkUwbXphdjZmOFJKdkxFdXRGZjNLRFY2YTI5RUtPRkNFdFJTRjc5ZlhmOE9YVEV4VW1Za3pMbENGQ3p0WUU5TUtybW9scEUyU1JReGRMRFNBMjQxUXp5NXM3UklHdVFCWHExWkQ3eCtMVGlHWE9OV2pCc2xPZEw5a0txVElWS2VTMnV4eThiKzNUQ3FGbFUvUnFTR1VOcXJWUGZDcXB5cGpxSjFtdHpmUGtZeU1aR01qR1JqSXhrWXlNWkdNakdSakl4a1lPZVVaR01qR1JqSXhrWXdvSHloTWtwMWY1Y1NDb0t2NTBlbFh2K1pHcWxjdzVBL0h3WWtyU21wSzZXNkNvRDlZTElYVUVWZ0xUVHg3b1E0THFWUmx2Y2o5REZLcFdwVXpqcFQrcW9yMGFrb3FDUVZpbFQ5QmlZdlJUVmVEUy9DR29JVHN1ejVITVFLMEVMckV1bHQ5UVQ3MWlDclJuUWhHa3JBdXpFNWZoaGloYUhacWtzZVNMamR0UkpTaEt2R0FGK0RoUkgrMC93QU9IKy8vQUVtSk5BQ1psTkJTRFpJQUxBYzExZk9tYStXNzlZbC82UTkwRkdrZGUxNlBnanMrWTFxQXMvb2pENlBGU3BneEJhVlFwNjg4dW8vd3lnaWNaTk14S2xVOG9EZEU5WnhxWmsrYXp6RlN6dVhVTitURWlNU1I4b2xDY1JOVk1XQWszR2pwQXozUUp5c2NzeTlUeEllbXlTRHYza2craUJLbVlyeHhzdWVBVWxTWGR0VWhvT2p4QmxxTTJaTVVvY29LZlBvZFA1WWxKbTRzemFkQ1ZWQTZ5a0Z5Yzk5b1dnWW1nR1Zvd2tEVlRtSGJvTUwvQUo2WWx3c0J0ejVkVUxYNFFUS1VYMGQzeWJONzd1MkUvd0F3QWdVMkZWMnplL3h6d01QNFN2UzZPblQzcXE4N09Dc3pndFlXdFVzcUIxQVVzMmNKSnhwcWVVN0E1SkZ4bnY4QTE2R0tKZnlndVhkS2tpbHdDSjJrZnExWVZYOHFxbkV6WnN6V1NkbFExVWJXUVB3SXdzM3dtbGN2Um1ZVUkra0tVTFR2KzhPcUptR1I4cHpwV0tLUWxPS0R1TlozWi9SQ2pPeE9uVHBWckV0UUxVRkRVWnhMVHRyU2xLVkw4NWczNlFKZmg1SmJXbXNRdFJyQ2ljK0FiMHhpSmVJbUNkWFdFRXBIaTBuZHp4VjRRV014SzFKRDNaRkxaNVpHS0phOUNrSlpOSEp2L3dBZjlZL2RIOFFVNXRIcFY3L21RNlVybUUyQmpDSm9Da3pSVWh0MEp4WWtGQ1Z5ZElsU2tab2FyOWUySnM3RXlsUzVjb2cxTUR5WGYwQitxSzUwcFNDRnRwQ2dXdUVQNlNRSUJHSFdrVk1pV1piRi9jTTRlWEtXUVdRNWxzNGR1cHpDc1pvWmhscEcwWlRIanZ2endWK0RHV3NUREpzQS93QklwQTZ5a3hvZkIyRmtiUE1vM0hBVW1GcGtJYWFvc3JWK3dGZTVZNi80Y093ZlgvcE1TazF6NWliM25YNVBSL0JML3dCSWU2RnoxVDE2SkpVYVN6TzVmYytieFNxdHFGTHJwTEFBQW4zaUZMUTRtMldwSlNVdndjR0pLMUpKTWtVcGRSeVlqM0tNSlNteVVoaC9sSis2UDRwQmxUcGlNRUFDdElhazV2dmhKNGsrL3dDWkZVdE15WWNuRVlSQWsxcG1pcEZMYXZ4K2tKbkNVdVRLTW5Tb3JDZFpMUFpqd2JyaWNyRlNETGtTNlhyb2JKK080ZTZ6d05QSm1TMDZTaXRRRERXQ1FjOTZpQkN2RTZoc0pBS1hWMnQyd05CS0pCS1UyQ2VZY2QyWG9oYy9SVllXNmxIVkE3VDZmM3RCVWNKTVNvVEZTK1RucEtCdjNsUFowUk5WTVFwNWFTczVHd1M1Mzdzb0lUSVVGdVNvYXUwNVR4ekpCalZyV1dTb2htWko1Vjl6TjEvd1M1alBTNWI4SmdvTXRVcVlqTkttL2dsLzZROTBLSWtLTEtJb1NlYzM5T2ZwaVduRWZKc2o2UmJPK29uSjdveklVWW0wNEtiTkV0dm9nVFYwVzVqMmNZbEZlR3JTY1BXcWtGd3VwSWJteko5RUJZK1QxNGRTa0pXa3pEcTNmbzRidWFKdW13d21VekFsT2dlNmVOeENNU25EZ1NUL0FOdTlaNk96dGdGbS93QW1QM1IvRVUwU3F0QTlWcTgrbDI5RVMvVDcvbVFWU2t6WmgyUVJ1Y2Z0MlJoMFM1WkltSnFTRUpGaDhHQk9sUzFvbGxtckF1Q2tLL1dNb3loU0ZPRW56U1VuckVCS1VnQVpBUmxHVVpSbEdYOEVoS2dDa2xpQ0g1SmlaTm5ZSHdOYkRsazN1NHVsT1hFT0M5ai9BQVMvOUllNkRVdFNzM0RDa0RrN24rRHpmTWd5UWt5SzAxQk8wMi9QMEg0dmlQOEE1RkVzUzFyZWttcTFDUmJnSHFqZDh6Q2tBVytiUDVzeDgyZjhXY1ovdzUvNFUvZEg4U1Y2T2FjU3BBUlVFcktXZnFoQTRQNy9BSmtGY3RNeVlka0VjOFlkQ1VDaGNzcVFwSXl5dEU1Y3RPamtJVWhDVkJPM1VFMDI5SUVhWmN4U1pXZFJscWJZSytIbWd3UVNzTWFieXp4QTk1ZzRvclBnK2pNMnVnN0lUVS9WL3dCTERLZi9BTGpleXFLWkM1YkFoNWFKbGRHb045Uk80NSs0RCtDWC9wRDNRWG1LVmVxL085dmpoOHhncEMxUy90SXppVXJ3bFdtUlhyMzNnaXdKM1BDNXljVHFMVTZwZXMyN240RHRoTk01MlMxM1AvZDBuSDBRcWFQbENZcWFGcW1TOUp5ZUFCR1F5Z0lIeWpNMG1sbHpGVFd1cWtBRWRrU01WaU1lckZtVW1Za0JTVzJsUEU0REZKR2wwNEpKZmI5TzVvMHl2bEZhMGlmS21wbG9UcEFtZ1ZNa0RqM1E0K1ZpSitJa0ovbUwxRFdVb1ozNWJYNFFzcStWMHpRakVUVkNYNWp5cWFjOXprK21DQmoweVZUcHlWMElYdDZqRlBVSUV1Ujh0TGtKUmhVSUNRV3A0TGJPQ3JFL0tVeFRtWXNKV3FsbjdnazlzSVhJeEtwU3BScW5DV3E4ek5xdlRWQWxlSHBscVJvMXJValpLUUFrWjJ2UmY0TUNTckhpWXBHSG9VRnE2MTU3ejdvcVg4b0ZTdEpVbDIxZGVYTUFETi8rUTZ6QmtUUGxLWk9WTmxvVWxCVXpCTEFseHhKSFhIeWhoZkQ5SlNtaWFnYXVqSk5UOXZaMHhpNWtyNVRteTViNGhEU3dwUlNwcWJEUFZLVkczR01MWGlVNko1YTBMVUdLbTF2MGlaaDVueWxxS2xHWFRwT0s3SFAwUVppZmxTZXlqTlVuTFZDeFp2dTduaENUaUZUU0prdFJYTU4yUzNhU08yRjRLU2tTWlpTd0EzUmk5UE9WaUU0aVl0UlNjcVZjazlBdENGeWNVcEdIMHlWNks3SlNFRU1QUzBKa3ljZXVVRXlFeVF5Y21PMTFRdWZOVjRSUE14UzB6RkM2UWQzL0FGVDkwZnhNeGowcTkveklWTWxwbVRPUzRqQnlseTBLMHQ1QTBUNWEzb3lmMFJNTXR0SE9yMGdsalZtRnRaeHZMUk1LNVVuUnlwUXFHaXExS0ZIL0FHVmRzWXJVbHFGYWt6VWlXOVJzRmRPNkZ5SmNpU3ZSblJLU21UWkxpaHZaYjBSTm5pYjRtVUhXdHJDS2RZVEw2alh6VU8yaFhWLzBNTi9xZjBxaEZZWlQvd0Q1MGNrYnZoc3QzejRYRGlqUnpIZDlySlJ0K1dFSll2b3dPeUdLVlJrcU1sUmtxTWxSa3FNbFJrcU1sUmtxRVZDZHFsWllLdHJWUC91TUkwYVppVkpVbFpXR3FYU21rT2VpRC9MelNzU05HQnExZ0M0U0ZidDNaQzV4azRpUk0wMHhZMXhjcVRTOWp3aVd1U21jbFNCTFNDNDVDU2tkaWpBa1VUbFM2VUFyWFNYQ1ZPQjJ4aVpNbVZOU21lb3FXQXB5U2JITXdsS2tMdWhpRk0vcGhOUGhTWENFbEZTTEFLY2RUbUppa1NadmpFR1V0S0tSVWs1MytNb2w2VERURmdrcUtWMG1seGZ0NGVpSmN3NEdZQ05XOUZQM3FjcjBqODBZa1NKZUxDSjBzVlVURXNwc2t3S2NOTmRDalNxWVFyTy92OU1hZVJoNXE1eTBwbHFWWHVBdFlsdHd5alRwUXZTM3Vla245VDF4a3FGRFJyQUcrMSsyRzBVd3BaNnJkOE5vcGdGNzI3NFI0cVlYenkxZTJGalFySUFjRnhmbXpoMHlsek03QnUrRmd5WmlBbkpSYlc3WUFvVzNHMW9jSVdvOEEzZkJlV3RIUzNmR3JLV3ZtRGQ4WktqSlVaS2pKVVpLakpVWktnbGxNMFpLakpVWktqSlVaS2g2VlF4U3AzUHZqSlVCWGcrbVdDR3FBNHdqeEE4WHNhbzFlaUMrRkdadlFuZm5DcFM4R0Z5NW9aZXFtNEdUeE1sS2srTG1GMWhtcWhSRW1ra3VTd3ZCbCtEcG9OeW1nTm0vdmlvU1dWZTlJMzNNWktqSlVaS2pKVVpLakpVWktqRGdBdlc5L3VxZ1NwTDB1K3NYK2ZDTE1nR2JOQkFta214Q1ZaYnN0Mzd3aWlmb3RVZkhhZmdRaitkS3RjbFRqTVZPQm53dDZJRldLS3RRcE50L0dFK09wUUF4VExEUWxJeEpjWm5qbno4OEs4SHhBa3FKVG45NU5Yc3BQWEdJUjRiU0pwbVpBdUFVc25mdWlpYk9LOWVwNFJUaXJET29mWVVQZVhoSzF6ek1wVFQySjd1MkdUaTFhUUlwVE1OMk84cy9SL25TcCttMVQxOTBOS1dOSmJXVWYyaVd1WXpwcU9wZmttS2lRVUhaTkxINTVPSlVsT2dsVkJLa3pDQ1FVNUtEWHUvczhJd1lBQkNrWGM4d2lVc3VoUlVzZFZRaFFXdXVjeHVMUWdCT0lTbmZwcGdWK3NZalgxRk1wQVV6SjFBQ09tb1ArS0VpWk04VWxUMVZKZFdiQTI1N3R3dEdHRXlhTVBORXdsYXBaZTFKYmc5LzNqeEJUTk5tcXlxcUhEY3o5a1lZekpsQ1VMQldFRk9zbWs1MjR0Q2xHdWlpWUxMdG1LVzlEeGhORk1BbW9McmMyVnFsZ2M5N2RVU1RWS0NyVkpOK1gvYjBYRUs4S0lhbExETTFPWFBWVDIvNTFLUzVSVVNLazVqVlZlRjF5eWcwcFV5dEhiYWNhcVJ3K0wvUEtLNnZDVXpCTUJENXMzVkVwYXR5RXhKV25aSlB1UHpIVEtDbHVjb2tEeCtmak5Jb0tHWGUwWVNVc2liTkVvYVZaQXBVb1VBLzFrUktUTFhTaW9WekhUVTFYQnVFUzNJUWtUUVRTb0YwdmQ3Y0ltYVNjRitOVVV1Mnp1ZnQ3SXhTVnFSa25RcllPL0srUGZDaXFoS1F0d0VrR3BON1pmZDdZUGhDa1MwdS9pNytqTDQ1b1hvMXFTb3E1YldUcEx0K0hMMFFoUW1QVEt1aXpLWDFRdlNMbGhGNldZOGdON1ZVSzAyZHQ3OGtQMnYvQUp2aCtuK2t3WmlVQUxVR1VlT1ovcVBYODRtbEEwZzVXK01LdXFoU0piQXMrWUhkRWlXaXlVbXpsOXgrZGVtV0ZhMnEzQ0xLeExtWmVxa29wZnVoT282ZFJCcXAzUFV2UEkyNnNvbitMWlM1bFEyU0FBaEZocmNRcnIzUUJvMkduRjB0c1ZmZTRSTUNCTXBNdVl5dFNnS3RSejhlYU1LY1B0ZytNVE9hNHBMTzNQVGx3aVVaY3BCRnF3b0J4cjM1WG0zOUVMVmlVaEFLUXlkOVRsK3ludC96cVdKYkJaZG4rNHFOSGlxbFVqNlJRQXZlM1A4QXdTUnlxRXNZbEpHUVA2SCtGT2l3cWxWVEJWT1l0VFlHRnJFdlN5VmNpMnFOZks0K3dQVENsS2t5U3RySVNPbjdYUjF4Tm5KdytsT2hsaEVxb1UxNjFlLzd2NlFoS3NIZDBWcUZOTjNxYlgzVy9lRFZocXRMaXFRbXhTRWFITWExaFdrNThjb1JvNUtKb00xQUpDY2ttWUFlVm5TWHlheDZJbFZ5QWhWaXFtbno3amE4MzQzUklwQlFUTUNwdFJTVUJGQ3RVYjdGcjgvQzBKMVd4SVRMY3pLYVZHbld5NTdRcVhMbEkzaXQvd0R4cXl2NTFHN2VlbUV2aGhTR2VtbTlqOXJqQ2xMd2d1cW9JMWJKdmJienQwYTJmQmpJbEpPa3pHVkZhaDUzbTBucmhRT0hRMWFnRGJaYXgydmp0aEltWWRFcDFQVllnSnZiYTJ1eSsrRTA0VkoxQ1ZaTzQzYlcrMzZ0Q2d1WEtUSjBTVk9mUGRUcHp5WUo2NGZ3UkJOTHNHekIrL3YzZHZDQStGUzJtU2l3ZDB2YzdYRC9BSWhBbTRkRXRaY0tLV0lUZjczcDlHN0tKQVVFVG5wQzFGckRlZDBCOEtpcWptMnF2djhBbTNoU1BCRWlXS0FTZHp2Vnl0MXVmbU1hc2xQaG0rUVNIYjh6ZHNXdytsT3FRWllIblhUZFdiYjhyUldaRXRVMEpXVkJBY1phbzJ2am1oQ1ZTSmNwQkRsV2JlMW4xaStaaHB1R1FXR2RuUGkwL2I4K29lajB3Z3JUU3NqV0hBLzVIaC92ZjBtQzVtR21YTFNyU1RLdGZXcTNkRnhxbmQ4K0RVRU9oSUpVdDhyS0Rkdlp2M1Nsb0xFSlRFcTczejlCL2hTSldFSnFtQ3FmVHliQm9VcW1xUW83TnRRYTl4MUk2NFVyUlNsVFd0TDJROTk5K2JyaWJOR0hRdGVobDB5TklBalNYcnV6K2JFcnhBVk1La1Z1UUVnY3B1aU5hVFVKbUlwQUFCU2xHZ2ZxMGlUYytkMFFqUm8wanpaWUxKeVFaZ0I5bjlmUkswOHBFcWE2U29TbFZEYnZjL1ovV01PVTZpak1lYWxkSlFFVXF0eHphRTVIRTBTeWEycEtxZFlkZnZncGxTNWRWeFUvMkZON1ZHN2ViUUhsYWc4MXI2cC9YNDQxVHBJUVRNMkVrS1pITWJYNzRBTWxDVHBNeDV0YWh4ODJrOWNMcXc4b0N0UVRTdDlWdFVtM1pGSzVTWmV2dDU2blh0UWpSNGFVcEZCS2lwYkdxOWdHUE5mc2hWYUVJazZKS2cvbnVwMDU4QW5yaDE0V1NGMDdLWmozZmp6OW5QRjhNQWpTcFNDbldKRDN0dXR6bnRzalQ0ZVZMbUY2dEd1b0o5M3dJdzRvbFRuS0JNVXBiTW5lY3M0dGhwWm02UElxWVYxZDE0cFJoVWFNVVZHWWFXY3FxYk43TndpVlNuUjRnTEpVa01iTVc3VytMeGhFVE1HSlpXbWFaakdwS1dXa0oxdWRKSitER0hRaVVKb1hNcFVzSXlHalVYejg0YitMUTJnbHk1WW1HOHRWUktRdVcyZkZPazZoQ2djUEowSW1JWWhkeW5sZERlbi9BQ1REOVA4QVNZREJ2bndDVzNLWS9nWDhkZk84aFU1TmVxbGhaOHVlSkJRMUc1dWcvd0FLOVU2SW5hYm1oYW1xdzZ5d0JBOFdOYS9Qa2kzMm9XclFTZEkyckxOdU85enpkY1RweVpDRmVKbDB5Rkt0cEwxc2VyUGpDQ1pjcGFpcEZTR3BwSEt1NWVBRlNuMG1JWUpwZEtVYUI4L3ZwVUhQSG9oT2lTN3pwUU5DZVFab3E5bm0zeEswaUpVbVk2U3NTOWNiZC9aOThZZFlkSk16eHN1WlJRRVVLM2krWlNQdzlhYjE0bWlXU0psTk5WT3VMYzQ3WXBsUzVkV1ZSUDJGZjFVYnQ1dENkVkFsQzVZWjZwdG54YUtweVVJT2xhaERLQVN4RDduNHRuQkJrb2xxZFJTZHpCU21CdndvUHBNQXpaUVFWUFpJZW56ZDhZVVRXMDZaWU0xd0xxMGY5ejVjMEJBR2hXZDdDMnJkamZlZUc0d2hTSlVxWXBtS01rdStiNTMvQUZ2Q1ppU1FTWDBTZ0FpbTV2dmZJZnBIMG5qNkU3UVNFQXN4UFBtVCtFRHBVSlVzUzFxQmFvQWxHb1NPVm5VdzlFWWQwb1JMcThhMS9SNmJYN0lTcGNxWExWV3hRTGlsbFhmOHR1YUU2aVhQRGt0ZnR5OU1Id2FUTG1NZ25XczVBdHYzbnFqRU9pV1VwSUVzQVhXS1E1ejg1K3FKb011V0JXMHNnYnFVbHpmZnJqcWdHVHF5L0NKZXF5Zm8rVjhadzhzSVFvU1ZwVW5kcExVa2ZIVENudzhsT3RNQVpUMmJVUFhFeFJ3aVFrU2dwSlRybXR0bG40NzRLbElSTEtGR2xFdFRpWU4xUkl0NklxbXlwS1YwalVTWHZVWHZiYzFvMGF4Smw0cndkTFRpWE9rMGVzNHkydW1KU3BEcGtHYW1wQnBjSVpkWGJSQ3RIaDB6Sld0ZFpwTDEySDVZbitFeTB5d0pqUzZTOVNmOFpLRXNoSzdzVDkwd3RHTmswS1FXcTg3NTVKUXNVU2cweEpHWnBMTmI3VzdqRW1xWXFUcUoxa3FhSlE1LzBQOEFDcDlKb1h0dE5sMVJNV2hLVlllWlpLVkpIaTlyVzJnK1NiZmF6dEMxTGt5UmJWbHRjWjc2Nzd1R2NUWnlKS1NnU1VVeUMxNWw2NzFEZ00rUFRBMHlKSkJVaXlKYlVqbFgwbDkzY1lwVkpTODdGVXBTVXVrUzlDOWkvbklVSFBISzRoRkNVS1NaMHRPcWpKSm1nSGxlYSs3M1JLMUVTenFsZXEvTHZ5dk5mNHRHSFVBcEx6UEdvWFJRRVVLNFh6cDZ1RUoxWHhGTXNrVENpbDZkY1d5djA1d1VTcGN2aFVXZHFGYy9uVWJ0K1crRWFrcEVwOVlCRDdqWjYrTGJvVVZ5NVkxd0FtZ1dERyszeFlzL3A0S1NxUkxscUpVVUVKY1VoU2dIMXVGQjNabUFaMHRLYWlvTWhGMCtieWpHSEUxS1BDQktCbWlqbGFQaTdIWHF5NXVtS0VwMGF5TTZSYlZ2ZHp2UERjYzRRclF5UzZkWkNSc2wrTmQzM2NJUk1TNGMxVUdrSnB1V1BLZklRYitPb1N4Tk5BTE1ybnpKUG9icFdKTXBFdFNrbW1wSUpScUUrZnhaUGJHSDFKY3VYVU5KcWcyMzNxL1RuK3pBSmxTMDNhbWtlYXErM3hwdHpaM3NHU2dGWG1oaWxyM3VjOHVaOHpDQUpNdEVvdFVwUWR1UEs5RVM2cFNKZ0FlYXFTbGp4c0tqMGI0bXBWS2tMQVFLRmdacXFZOHJjQSs1NmdMTkNpTU9nL1I2dTdJMUI2dWk5dWd3Vk1xemhLR1MzRFczODRaczRscWFTbEFtaXBBUmRTS2I4cmoxTnlvVFhoNVlBVWtLeTFoVmNqV3RZOCt6endGNGxLSmRpQ0VqbGJqdFpidWR4bEUxOE5MUXlqUWJGdzF1WHhQc0hpSWtybnk5RFBVZ0ZjdDNwVTF3L3dEajhQOEFlL3BNVno1MCtacWhIajAwbXo2M3BkajkzNTBGNXUyMmpDQlNRL1hHRDBLTkl5TDY5TzRmcFZHSFRNVFNvRWhpcXBoZHI5SDhLejQzUlB3Tk9YVkMxQ1dGWWRWZ0tRNk51K1k0SXQ5cUZLWEpsRGdrRHArMXpEcmliTUVoMEprb3BrMlpVeTlWM0hBZGNKMGtoSWVoMlJrNzFjdmRhS1Z5QTg3RlVJU1UyQ05CVmEvbnBWYzhjc29SVEtUTUNwcUUyUmtrekFEdjgwOE9mbWlXOHBLVHExYXYyNzhyemZqZEdIS1pWSlZNZVlsZExKUlFxejhYYi9pQWRBbndtbVdTRkVNVFRyRG12YmZCVEtrb2ZLcmhxSzUvT0NOMi93Qk1JZVNsS0xWQUpmZHhxNHdvcnc2UnJXMWNrc2Z0OUZ1ZnFVRGg1YVM2aW0zSkNsQWI4eUtENllTWnNvSnFxc0UzSG04bzlNU0RNa1MwelRMZVloc2xVRG44NnJzalVsYTJiQkk0WHZVenVld3hLVWNPaDFEV1NFN0ovUHZoSkV0eVRVMWdLYjZ2VGtJSW9ScHFFc29zRWhUTXJxejdJVUpNcEtGS0JZcVNIUnFFK2RtN0NKRHlVSlM0MGpCMkc4Ylh4bnpRRDRNbk5tWnJVa3Z0Y1dEZHQ3Sk9pU1NkeVJrMStPL0xtZUU2S1VrVEFxWFVVcEYybUpyTjFaRk5WdUc5NGZRcFk3cWN0M25jWFBSQ1drSVdDUzVwWmhaclZmSG91YVpJVW9hd3BzK1dydjRtL3dCbVBvRUJGVy9ObkhQd2MraU1RU2d5OFFRZEVFaE5LVFNPZmkvN1FwNUNMUGR2dFp0VnZGMjNQdmpEekJoUnJNVjlITmY0YUUxWWNTcHJwZDJQTEQ4cnpYK013czRXWDRSb2txTXI3ZERsTDFlZGJtNTR4SVJobmxTcGpEVnVwTkNEblZtNVYxUXZ3aVVKWTNOL3pmcy94dUgrOS9TWVhoMExRYVZQU2xUa0NrQVArWHMvZ3d4UWpTS0tScWNiUmgxT0M5N2RIOEs3S0VsODJ0bC94Q3lxVVY0ZFJBREoyQnIzNThrZm1oU2poVTErYmZuNmVBNjRuVGZCcXFaTXVpU05sVXd2VmRuc3c0WjM1cFk4Rk9rSlJXNExCM2UvTkIwbUdKRXpFNk5LYWRsT2dDdjk2VkIrZm9oQVRJcnFtb1RxakpKbUFFOVJpWHBNT0pheVV1RU9vTld4ekEzUmg2WlRLVk1lWWhhYkpSUW81OFhZZkR3azZFSEUweXlVcURCNlhVUGg4NFVpWGhocExpcmhxTEk5b0ovTkNRY01RbXpzQ2VTYmRmQjRLcHVFcDhaa2tFdEx2Zm55Z3ZoUWdncU4zdUFwUTdXU2Z4UW12RDZKU3FyTTdjSXd5MTRjSlhvM21vTzVWQVArNXgxUjR1UW9MenBwM1U3bkxPNTdERXBYZ2JyVURVbnpUMi9IWEFhVzZ5YXFLV0ZGMkQrZGtJWUlRWjlDVGNVcEJJRDlybjBOMGtTcEJSTVVDUVNtNmRRa2N6dmFKQ1ZZZGt1TklRSGJjM1cxL2RDVkt3aktydW0reXhMKzYwSk9nY25odURQMjVlbUVDWGh3blhsMUxTQ1ZNSmdFeXpaVXZ2ZG9uTDBTV29UUktOcjczUFQyUlAwbUhjcVVUS1J5UW5SSXM3RGxWWmlFcU1sRXRkVGtKYzZtL01DN0hKb21QaEVKYVdGSmRSS2xHbkxJRE54R0pLc09nQ1dUUUFWRXE3UGM4VitDSnV5dEdvczNpeWFjdk9ZUEU2WDRNRktwQzBaMDVaTzNIakV4UzhMU3ZTb1NnSWN1azBPYnQ1eDRiTWVRaXJwTGMyNSt6dmhKSXBKR1hEL0hZZjczOUpnNlNXWmEzZHFDaFBvQjUzUHArZkRTUlBaSkJKbFU1aWs3M3ZlbmQrc1NsbEtsQ2dQUzNDSkN4a3E5K2cvdzBJbHBNblNDNUoyYlB2NllXZEZWaGxsazZuMGUzcmNUa20zMm9XczRSQVcxcGI3Nzl3NjRuVFJoYWtpVExLWkRaekwxZ0gwQWVtRVBJQldWSXFSUVJRRG5mZkRMdzVJbVltaEthTEJPZ3F6MzY2VkIrZm9oRk1sM25TMDZnNUptZ0hzOS9ORW9ya3BrekNVMUJLU3ExZC9aOS9ORWdwUW9WVFBHUzVpUlNsRkNqbU43c1BSNlNsVkZlSm9sbFNGZ0JMMDZ3K0h6Z29sNGNGZHhWK0JSZjh3U1BUQ1JvUW1YdjFDZHh0MTc0SzV1SHA4WnNwVGtoanJjK1R0bkd2Z3lHS2xNbk9rS1VPc2dKL05DaytCSVdBbmNyYVYzZm9JV3J3YWswZ3BTWE4rVVBSMjdvdmh6TEFXTDAxV2NEMDV2YmhFbDhLVUZqcEU1Z0hjSC9YS01PbkZTanBUSlFWYXZMYS9NTHhhUlZxcE95UmQ3ajQ5UCtXNGI3MzlKaFFrbERnaW9TbFRDallGeFg4K21WSlRYTDFOSnBpS1VrdGsxK2lKU0RsU2crNkpmVCtoL2hXa3BtSERscnVwbjZNb1dVeTArRExzaXBGNWUxclozeVRiN1VMV3JEb0Z0V1dCY1ovYXZselp4Tm1wdzNpeEpRUklwQkptWHJEMURnM3BlQXFhaVdxcFNOUkVxNkJ5bk5kK25zaE10Y2xJTTdFMG9TVVdvMEZiTytkU0ZoL1MyVUlLVUlLVFBsbzFVM3BNMEE3L045L05Fb21VbVNyVnJCUS9Mdnl2Tjkvb2pEclRMMnBuakphMlpLS1ZIUGk5UFY2WVNyUWhXSXBsa29KRFBUckRtdjA1dzByRHBmaWQyb3JuODRKL05DTlNXaVZtb2FPcmNiUFZ4YmQreEs1U0U2OUlCbDVCaTZ0ckoyTGRzTk13amF4TEk4d0tVTTN6SW9PN1BtZy95aVZNbmR5anpYNG5xVHp4TUtjS0V5cVVNQ0hWVTZxaHRESmgwMVBFdzRmREJjMHBtRkliSXRxaTZ1Ty8vQUppWUZTcFl3NlphU0xiU25WVUJlM0pNVEZ5Sk11ZUpST3lHRTNWZGhyWnVLWHl2elFmRUpYTFNSWUJpb0gwN3UzbWpTNkJGR2xJZG1aTlJGOWJjenYyUkxtU3NJbWJLV2hTcWNsQzJydjMyNkc2cGFSSmxvUTZhbGxPN1VxNVcrcFhSVHZnRk9Gc1ZiTm5BNmF2K09lRDROb1YybXF0SzRCTkNicXplb1AyUWxNcVdrRWt1b3k2Z24ydzdkdk5HSS9sSHBVTkhjQ29PeDMrbnNpVm9aY295Mk5Ub2ZmTGJsY0RNUDRZVWNYTEVyVVN5UnhkVC93QlArU1lmNzM5SmhweVpxVlZINmFicEZNM0g1MHJYTFNwYWNpUmxFbjdnOTBTK245RC9BQXFCUk5PR0xNcHl6OEcvWG1oZmlud3lySTFOamIxdWZaVGI3VUtVdkRCUEJJRjkvZDJ4Tm1lRG5Rb2tvS1pTVVBXc3ZWZm1adnhRTkxJWjZMSmxuVmQzMzlzQkV5UjlOaWRITFNVNUowRmJEanJKV0hoRFNVcUNwMHRHeWJKVk1DWDZuaVcwa0pPclZVZzVWMzM4SXc1VEkyNW12TFVscVVVS08wN085UHhlQXJ3WkJuMHl6UmNYcGRRNXI5TzZLWmVHRlJzN1phaXo3d244MFMza0JDZVVLQ3B0WEozemVGRldIQ1Uxc1BGbDJZNjJmSGRHdmd5QlVvNm81QVVvZGJCSi9GQ253Z0pTTWh5anpIcFBZOEwvQUpTaVVFSUlzNXFOVlF6dXpEODBUVlNNUHBKd1NzcFJTY3dMRHJpWWc0WkFrcGxwVUZOdEtkUVVPd0VkTUtLSkltU2d1Z1RFaGdlSjlGeEFBd1ZRVXBuZHFCeDUvUnp3OHVSNHRJVVRMS2JuSnI4OTRrL3kydE1jQUVabWxSYjJSMHZFMytYTkFRU2l4ZFIzZkhORXBVbkRwSUt6V2doeUJTcm44NmtSSjA4c1M1Vk02cGh2Q2toQjlJcVArVVlmcC9wTUQ1NU9EWlNzTXRlanRSLytkWDN0L1EzcGlUOXdlNkpmVCtoL2hVa2hSdzVaaUZaSGczVmZtaVpWaGxyd3gyYVVIVjI3MkY5bFA1aENscXdKUTI1eno4M04yeE9tSERURlNVU1VVeTBJT3VzdS9KZTFJNGJWK1pDVllPWWxSb2ZVV3lhbjMwN203YnRDaE53YXlKbUswS0VGSkZJMEFWYTE5ZEt3L2EwSUhncFZWTVFtejJCbUJMdXpiM2FKZjhzVWxWTDZxOHEyTzRidmhva1U0YTY1bXZMVkxJb1FVS08wN080L1RuaEt2Qkh4Rk1zcWxsQ2tCeWwxQnpDa0l3YXRKY1ZBSFYxRmw4cjNTa2ZpaEFPRFdrV3ExVkZyRzJYSGc0N0lVcFdDV0VWQnZGcmVoamZaNXNzOHVaelZnRnBwS2lXQkpLQXBRdGJPeVMzMm9VRGdEVUFMSkpOVzZ4cGJQc0R3L2dTaTZhcVErcjV6MjNkWjNDTVJNdytGVVoyaU9qb0JVUXFrdFp2T1llbUhPRUpsMUtGVjdYWWJ0OW9QOHFUYXB3RjJCTmdkWFBvZHQ3UkxRckJxUldBYTdrRG15NllYVElxS0N4VGNQbGtXaEo4RFVYdVdDOHFtVnljd0x0djNROHpDTGxLS1VsS2FWWGNPMnpiZUk4ZGhWT3BVeHJHekd3c08yRnBtU2RFRUVqbVZsZjMvQU9VSVZKSGpuTlBUUXFEcGxsYUdzN2NUdzVtK2VSV3FsQzVsQVJvczd0bkVuN2c5MExYTFdxV3NLVHJJTEhPUExjVDY1VUpHRStVTWF1WFNDVE1uSE9GMDR1ZWxzbXhKTDlzTDBQeXRpQ0F0WXZOT1FadDk0UUI4cFRpUzduVGt0MUdNUVUvS2s2YXBJMUduRzUxZWZuUFZDcU1mUGFzaFA4d1M0NDU5RVRGRDVWbjBoWkFTSnhKYmR2aFgvd0J0T21iclRUNWo4ZU5vOHR4SHJWUjViaVBXcWp5M0VldFZIbHVJOWFxUExjUjYxVWVXNGoxcW84dHhIclZSNWJpUFdxankzRWV0VkhsdUk5YXFQTGNSNjFVZVc0ajFxbzh0eEhyVlI1YmlQV3FqeTNFZXRWSGx1STlhcVBMY1I2MVVlVzRqMXFvOHR4SHJWUjViaVBXcWp5M0VldFZIbHVJOWFxUExjUjYxVWVXNGoxcW84dHhIclZSNWJpUFdxankzRWV0VkhsdUk5YXFQTGNSNjFVZVc0ajFxbzh0eEhyVlI1YmlQV3FqeTNFZXRWSGx1STlhcVBMY1I2MVVlVzRqMXFvOHR4SHJWUjViaVBXcWp5M0VldFZIbHVJOWFxUExjUjYxVWVXNGoxcW84dHhIclZSNWJpUFdxankzRWV0VkhsdUk5YXFQTGNSNjFVZVc0ajFxbzh0eEhyVlI1YmlQV3FqeTNFZXRWSGx1STlhcVBMY1I2MVVlVzRqMXFvOHR4SHJWUjViaVBXcWp5M0VldFZIbHVJOWFxQi9PNGoxcW93LzhBcS8wcWlkT21yS2lxWnNxa21VVWF1VkpVcHMrYjB1L3o3Q1B6L3RDUW9JWktRSHIvQUdoT2tseVZvZlpVdlBzaW53SENWWnM5L3dEYkhrR0YrUHd4OVg0YjQvREFyR0YwWnZvVklUNzZmaG9LZkE4RStZRGZ0QnB3MkFOMkRjZXFIR0d3Q3BhdGtBWDU3eHJZTEJrY3lmMmhWR0d3UzJONlJsMlJVY0hncVd6M2U2RTA0UEJXMnJQK2tIK1V3VC9jaFRZVEJadU5UYy9kQWZBWVVYRnlQMmgwNERDME1lVENuK1Q4S2VHckJwK1Q4S0R1TlA3UXd3ZURzNzZuUGJzZy93RDErRjZ2MmcwNExCbEN0VkxKeUxsMzdPb3dvcnd1QkZ6clVXWjdRNHdlREw1RnYyaDFZUEJKUnovOGRNYTJCd25UOENQcS9DL0g0WUxZSENGcldQOEE2eDlYNGI0L0RIMWZoZmo4TUZTOEZoRUpIS1VRQi90aXZ3YkFsRHRWV0dmcWo2dnczeCtHUElNTDhmaGkyQndoM1dQL0FLdzgzQjRTV09jLytzV3dHRlB4OTJDVGdNS0FONS8vQU13Q25BNFFnN3dmL1dQcS9EZkg0WStyOEw4ZmhqNnZ3M3grR1BxL0MvSDRZK3I4TDhmaGo2dnd2eCtHUHEvQy9INFkrcjhMOGZoajZ2d3Z4K0dQcS9DL0g0WStyOEw4ZmhqNnZ3dngrR1BxL0MvSDRZK3I4TDhmaGo2dnd2eCtHUHEvQy9INFkrcjhMOGZoajZ2d3Z4K0dQcS9DL0g0WStyOEw4ZmhqNnZ3dngrR1BxL0MvSDRZK3I4TDhmaGo2dnd2eCtHUHEvQy9INFkrcjhMOGZoajZ2d3Z4K0dLUEFzSlZ3Zi8xZ3BHQndoVW5NUGNlekhrR0YrUHd4OVg0YjQvREVzMEpGQ3F0dm1JNGM4SmxURlMxaHlYUnFoTnNnbjUyVk1wUEFvVjNRWlZVeEYzQ3RFcnVpVEpFMmY0dWJwWENGOVhSY3g0UVY0bE15Z0kxRUVibGg5blB4aXV5UEtjY05hVW8yTjZQdzc5L0dCSk15ZFBJS2pXdVdwN3FKNGM3UVVVbTdWVlMxc1FNdDNHTk9GVG5LQWlqUnFhMlc2RGlIbjFxWHBQb3kyU0I1dVhpazlzWWRLVlloV2hacXBXYlpaSTl6UHZlTXBucWxkMExueTF6V1Z5VExYM1FNTFZQb0JkOUdYemZoRlluNHhGNVJwUWtoT3B6VTc5N2NCSGxlT29vVW1saTkxMU85TzdMb2lheXA2NjFGZXVoVnVZV3lpMHVZQytkQmhJbW9Xb3BYV0NFR0VpckZsZ0E1enMvTjlyc0VWZnpLdnZBOTNOMndwTDRxV0ZWL1JpbHFzK1RFOGdZbEJuS3FOSXl5RnJmWkVKQVJOcDBoV1hTZDZxajc0cGtyVkxYenlsdEFSaUpzOVV3TDBxVm9scUZLMkFCeTVvV0ZZckczQ2tnaEpCUzRaOW5POW9aZUl4b0ZOTEpTemF3VSt4bmFCUDAyTEtndGE2U0ZVNndadG5MaEJTbWRqRUoxd3lVa2JRYnpkMjdoRldueHViMHNwdWpaeWlZeTU2NjFsZXVoUmJtRnNvS1htSjU5RXJ1Z29ybkw4WUpqbVdwN056YzBKL21jZWFVaE42dnQvWi84bnNwNFFwUXhPT1E0U05WN00vMmQ3M2dTa21jc0FxTlV4Q2lTNUpPN25oNUUxY3FaU1UzbEwzK2lLcGsrZWxYaXlkRExVSHBXdFRaWmF6ZWlGb00vRnJDMFVheVMvVHM1eDlOaXhxbExzcDhtODJFZU94YkpTUXpLMXRVSnZibTZ6R2swMkxHc2xWSUNtc0c4Mk9YNnBYZEhMOVVydWpsK3FWM1J5L1ZLN281ZnFsZDBjdjFTdTZPWDZwWGRITDlVcnVqbCtxVjNSeS9WSzdvNWZxbGQwY3YxU3U2T1g2cFhkSEw5VXJ1amwrcVYzUnkvVks3bzVmcWxkMGN2MVN1Nk9YNnBYZEdVejFTdTZNcG5xbGQwWlRQVks3bzVmcWxkMGN2MVN1Nk1wbnFsZDBDYTAycDMralYzUk9XWjJNbDZYTVM2Z05scmF0dHg2WWx6ak94aTF5NXVtVFdra1BSUjV1VFA2U1lKOEp4aVRacVVsZ3hmemY4QW1FSUptS3BTQlVaYXI5a0pBcS9Jb2ZwODg2aWhsaHJxNW9GTTB5ck1wdXNkUmlXc3Bsb1NFTVFrazc4cndTSjRGYnFWZDJOQ0VCdnl2NllUVlBTWlltVEY2TUxMYXkxSzRicXZUelJnL0NKK24wT0oweXF0NG9VQU11SkN1YjBSS2wrRkF6MHlRZ3J5RlZzcld5TWF1S3BsbkVLbkZJNEdhdFJHWEJTUjZJbXBtVGtrelp0YXplNDBhVWZvOFloU0ZJbG9XRVpLT3NSVmMyKzBQeUNKNnd0RlV5MVJUZW5SMHMvVGVFS200blNvQlU0SjNidHc2ZXk4U1phY1FFcGxvRXV2TXNuZTI4bS9YelFQNWhLVTFwbUZOWnZTb0dsMit5ejlrSXJ4R2xTSnRlc2R3STREczdZeEZTaE5LOWxDanFzNy9yejVDQXJTSlZNRXFWTEMxSmM2cWlWSDBpQVZZcXRGWk4vTjZHL1h1Z0xHTHJ1dlZKM0tVNDZ2MGFKeXBrNUtsS3JwVnpsS1VwUFliYzhKMG1KMHFkSUZheDNBam0vNDU0Qms0clJEZ3o4dC93RGJhRUVZbWlZSmlTUm1LSDFrOVVTZ2NYV3BLblhVcHF4cldjRGpSdTNkYTBveEFTc3phZ2VDT0dYcHk1dWVDVnpnRUNZYmVjbTNjYmZhNW9RZzVwU0JiL0l6TVRKa0xEQ2txbUVGM2UrcWZSR01DbENhSnhKQVYwMkdXN0xmbDZJbUhVcFVCc2pNOVZ1aS9vaEJhV3JEY3NMUEN2a3N4MmgxUXFaWDR1aHFPZmpDVlRSTElsa1ViK1drbm9zRzlNU0VTVkpscFFvRWdBSkhUWVp3dGRib0lTQWpoQVdsWTBEYXlDVG5mTHNod3VrTnVnb0cxRXRLc3g4Ly84UUFLaEFCQUFJQ0FnRUNCZ01BQXdFQUFBQUFBUkVoQURGQlVXRng4SUdSb2NIUjhSQWdzVUJRNFRELzJnQUlBUUVBQVQ4aGhkakVrSmEyZUF2WU1hcUZKRXdDa3VsOUhNWlhQQUJ0NkpOMVNZQ0t5Wk1SeHgyQ2VSRzlWWmlVWWhyQUZZYWtsakFJTWthVVJBSEVqNDFNT0h0RTJHUVFieUVPaldDajdrVVg4VU8wMTNrbHNaTjVaQUxHM25EN09nWE42WHFEdGlDYzJMY0hLYW5JcHNPUCtQWGpmWUMvUUx3c3JJNU9ONUREYXRpMmxMakVRUTB4SURPcFJQakdjU01JeUNJNlJNSnRBbmVlYit6UUh6aC9uL0xUZG1ZQXFMY2o5a0Rtd0pBRXZiSnlZZEJqd2prU0kvQnhFVFJvSllsNE1GSE5yM0Q1ZG5qMXFITENESUxmRkFPZmlnQXVnNlRWVWhROC93Qk5ReWhHcnVDUXNaKzl5L2E1ZnZmNGorWW8xRHNJaWNVa2RGZ1ZxOGs0RElpaXBoZHRmamxNemQzT1g3TEpkbC80NWxsbGwxM2wxMTExMzExMzN6bDEzMzNnUkZWbWRIMzhvNDRkWFgrR2FjclBsLzVlWFhYWFhYV1hYWGZlWFhlZlhmWFdXV1dXV1dCaDdCZlYvbURMMEt1cHlubm1uZERHbVh6Wm54VU5vTGtDeEgvQi93RCtBblp3UWtRQ1oyV3grT0E1bU9HNnBwNkpFVE11Q2xXamlVMnloQXpwSXdPWklpQzI5ekVlV3RHRFR6bUFqYU1JdFRQMXlUQmhVVUpUSVVnaXIzMHlUcUc0dm9aL09OQUNOVEV4V3BRaldkQXVTQzc1UXN1OVZ2RmtNU1pBU2l0cm5aVFRDNmE0QmdiTG1iOWRPQy9CR1NzcXg0VmZFUkR1K0lkVnhJYVN1cTM0eDAvVmRhQXdKV0VlcVdjdkVaT3BZOWhkRWo1NUhpQXBOdUlxVUh4RTg0SGUyYk9NYVNwdkpTUWN3MjkwU09aVjNPNU1RV0ROQnkwOHFCWHd6Z0lLVzRKc0Z0MnVDc1N4RWpET1NlT1FsUklrbkl1ZEFrMlZkcFVQeVV3cndMSk5sOUw4THhoTWdGTTV2Qk5wcWZLWVliekZ2Rk1RVTlvT1MxcmJHYk9IeStqbDhieEJSMVpMODJqWmhhS1RJM05vSmdUMTBYaEpYeVE4UUJZd01uemF4YmpSWkxUQXh5TjRQZ3dSRTFaVGlDZFE0MUJ6eUF0Nnd1dDQ0NDNra0RZa1BnWFFQejNpa0xkQ0dPeUYrVG93Z1M1SHpBaWgycHJXTVc3SkZabE5SZmZsR05LamhJckV5enNWNHVzV3VreXRsTFBwT0ZET2RRMkpFeXlPOVZ2aWJqRW9GQ21QRW5PeU1URFZYWTlpVWp5WXVkQmJBd2J0QnJ6NHg3NUpsRjA5aXljdE1acTVEdVdmNXd1Y1pIUUdTYjFldlgvNi93RDgzTnZWUEEzSHRza0IvTHd4V0RIV2RqTWlacVlpOEVSaWxLWWNoVEJpdkJnUk4rWkhuRER1UUhZdy9Vd1NQS3dCa0psSUdhRzcxamdFQXpXQkorQ1B4d2VLeEVvdUw1bnpZMk1Edkg4SVRUb2dENTlPQzBLOEdoOWpBSmtqdWVXYUVZdVFUM0Fub21OM1lNbm4vTWFUZ1lWb2dudVJqWVF0SmlkVkNXdHN1aVBxK0hPaUtkL01ab01zMXdrYTI0Sm03R00wUEZtUCtSeVRBdElDN0c3VmQ1SFJBMXF0UUN2d3lNNi9hQ2FFa0F6SjQzbk9FamhVZkw1bUg2SjZvRFlnYUdrbGJ5d0F4YU1OelZHcWIwRHlTWkpKV0J3RXNmUEljSWtIa1JUdTBQaWRtTnNSa05sRnpUVEEyN0t3VGpFU2VDMThCOXBuUHpoWWdHZGl3eEU1Ym9OUk8xZlIrV0JORlhBQkxsSUtFQ0diYUUwN3Z3NVBKWlBKWkxKWkxKWlBKNUxKWkxKWkxKNUxKWkxKNUxKWkxKWkxKWkxKNVBKNVBKNVBKNVBKNVBKNVBKWkMvZXY1VTFBbmk2MjFKY1pJWmFFNStReEVsK0phYUlWSmR6cmd5R3ZGbDJxZ0szQlZsV3pqeTRzb3E2YWhldHZoQndZZTBTVFdZZVJ4YVlocFpZVFlWeUFucjhxZHM0WFN0SWlhTld6a2JZdWNaSDRWWXlBQ21zaVFLZzRNSlNLekFWbXhPUWkrMXM3NXdtQmVIZnlsMzZ3YWtqRXRLZktiUEJHbHh0SkNpUFFRNkIvYkx5Y0FFcVB0MTBZMGxJVmFBTkhBUldXV2lpdmsyZlA5b3loaUxLU2NYSUoxcmh3SlovNXR3U1lzYkQzZWkrSG5McUZjTDJWZFo2L3pIblBaZWNSL25BalNOZ20xUS92dHhwMFdNd1F2WGlsRVBHVWovZUkxQ29hVEFtV1V3dWNXQ1JWaVpMRDdKN3dJaXd4R0lKOEg4Tzh0ZThSRU1TdGl1cDdNWUpkbHdTSGVJekVrNERKRmwyYm9oeGdDRUFLdjZhSkdDbHN2K2pKR2pSb1VLRkNoUW9VS0ZDaFFvVUtGQ2hRb1VLRkNoUW9VS0ZDaFFvT3g2MUlHWjlVakw0dXVYei9ubFMxdVQ1UTRYWnVjZ01tYjA4M2lqT3RhQUVpS0xyZVFFencyeGFkZzlEMHljTmRMTGF2ekVSNHhCSlVVcTVyNGQ0QXA5cTRpdDRXQUg3RERaQ0U2UTArTnhnUXVweXcvYU1rVGFHRlFsa0J2MWN1QkJhaUh4a3lrbEt1dFpNQW1JaU9JRjNvbUxzUnJYSy9QcG56b2Z0bXl6RENOa2N2bTV5RkJDSlFJY3hHLzh4c0ZaOUMyNU84bndqQUVxVFE4bDZnOHlZR0FLaERUZXIzaGVaeG0wUE9RQWZodzh1ZlhJNTUweGhTQ09QOEFIYmpMMmlkZkt6cnJ6UHhIVEVJa2ttT0hIMkJrSUtwV09YTFJCRWJ3ZTJTSUljb2g2M3dUdkdPSTRhSVRlb2l2VERaaGFMWHpGMXhsS2JuVjVQMHdQTFZkTlJaaE1CQTFIOHpLd2pNRURmUmVoZDd2S2tqK2VKQ1h5RE1jUm5paDVodG1PWnl0aFZBVGJTN2ZqSHgvNkZJWVFGZ1V6ckpXTDVRMldkUG4vTXNRRk5CN2NLVFVSUW9uNG5nNXhCVXRHcnZVb0NRR2o1aGVFSTJGbEh4Y0phVnp5U1FjcHM2bG9NaGNJOW5kbWRVbVBST3BTRmtMV0pKc0NURDFyOUpHejRqeDhjam50QjB1OENtQ3VCSUgvY01BRVJjWDUzQkw2SGNEZm1idUV3K2h5YjhZTkg0b0pMNlp4NkZHMkJqNjQvQTNWSWpSNE5xNHJZUzZwS0ZNNm1HSjYza0tHOWtoNElScm12Tk1IUzJVQzN4aTJZNlRtd0tXcWh5cmVtUFV4OHlWR0RCM0Q1QjZHT2JnbUFVUGlBY3NsUWpoaXE4TmpBa1pFZkFIY0RROGxIVlUvVUVid0MvVXo5cG43WFAydWZ0TS9lWisxejlybjdYUDJ1ZnRjL2E1Kzh6OXJuN1hQM21mdGMvYTUrMXo5cm43WFAydWZ0Yy9hNSsxejlybjdYUDJ1ZnRjL2E1KzF6OXJuN1hQMnVmdGMvYTUrMXo5cm43WFAydWZ0Yy9hNSsxejlybjdYUDJ1ZnRjL2E1KzF6OXJoR3NYUytYOHF5d05YUjljakV4R3FVeE9RY21TQllsd1NSa0phYXhOdmhQUHJBbVI3c0lFVVRNZW5lSm9FZ014cHhoUzFQa1RHTnZnL3JOVjd3ejVZSVNaM0JoaUpnUE91L2JuSjNJRmgzcWNET0JEOGVMSVlMZUhHbm5FRVpXL2tZRmVwVjVjdUlaVUVUYmlQaWt4VnI4emhnd2F5U2xnLzFuN0Rpb3lnT3JFaWlGR2hPM0l4dlhCRXlCMVducGg2d0hHMmc5Q09SamM4eVlBQmtkVTQxN0F6UG1mc3h4cWlPRC9jM0FyRnNYNG42dkJZTnlPQnZ3NC85bmRoL28rZVFOdHFKelUvUmtSRlJUYlRpcTNiQllrK0lHTWxLQVYwdm9vY3VpM1VkOENLSWJYQldueWswcCt6UDIvR2l6NWZwSnBnQWlET3dtUGdaeWhFRFM1VTM1RGhKK2dValYrckFJQkZKQTNNSVpHN2NCREdGaExyQ0tDV0YyQ2NNa29Ba3kwUUxOMUhlQXZqMWpoblFVRXRDRy9hY2lDcVVMeGNFNTRtVXh2V0kwbGFtVEZSYVh6L0FDd1NnZThFSlRjd1VUbFJuQVFHRGc5STRZdG9KcUNSRXM5TENwZUdlTDhjbDE5L1hKZGZmMXlYWDM5Y2wxOS9YSmRmZjF5WFgzOWNsMTkvWEpkZmYxeVhYMzljbDE5L1hKZGZmMXc3VVpiK29uWFh6d1BpdVlxLzI4QW13YjFUNTd5NndoTkVlSHovQUQxM256OVg1dDYyNFlWTkJBZFpDUTFncTJXUFdmbkRzSVlrQTNLbFFWdFliazhZeklZVkVTKzBmV1VNU3NaSTE4RERobzZxSnBNYkNQUGM0V1Z1V1JFbnlkYW9yTlJTOENZWHdCaFFBbkJrRG85Zk9SL1N6M2VJR2RzZ1ErZmxyaEtJRWhrUGhjdWlDczJCeldmRjhZZHNqSDFYMDRoV1pvTkZyRTJLMmVnNzY0RW9rQkp1Y2pnTTBRb3pXUVlFZ2dDdXBPdURnQWhKVUVpRWhQVk52bW9ySUNUeEdnRTNwajg4Z2ZBaW1zbWVrdVN1bkVzNkhwT1ZaRzhLYWM2MUlNNDlzN0hIL3dDOHJQeU5DSWhMOE82N3ZKZnZweExiUG0rbU1Ub3VtZ1BrRVBsYzRSVEVWQVd4b3hjUTFndFRxSk1GaEp6M2ttNVVEQUdXVkJYem5FcENaRm1FSlRiek5WcmN3WUJnbFVFUjdUNWlzU0ZMV3ZNZDB5TE42K1BJK2NjcGZBM1NrYTlUaEtjNU9kVjFMNmpneUprVlR0VWtVMVgvQUxlQS9HdXFCNEk1ZHp1OEoxbXNoekhoQWl1STNCQjJFNE52bXZYRFhqTklqU3VoQjhNZk13ZHBXQjBmWGtzcTFvU0tLV1ZwNDhZZUptTEVLRDhDOEhFWTZVZ0dZS0tJR1FSTTc5TUhhWGNNajA5bnpqV25aRHBtNCtiMDRqSU1HY3cxWStyS2Zoa0Fwa251VG1NdTRBNXFQcEl3WklUSXljSWg4cTRNNG9DZy9qaHZpekNsczhORTFZUk5meEhDaHN2Nm5EaHc0WU1Fb1g1MWU5WURBY0lqZ3pIRE1oOXYzR01ZTVQ2YVNqNDcva290enV5S0ZMaE1VVnB3WitZT3ZYUEsrWitjWE5saXlmTTlqamt4UUJUMlRJYk92TStNTGM5bE1CQUpHOHd2eHFNVWdFaEUrUjZ0Yy9mSzBWTUdTWm1QcytlQ3VuN1FpVlV0T21oZlFSaUZGeElQMUgxdWJrZUljcm1XaDlMNThXY0RqaDFTRU43OVd1WnoyaCtjOWdmbkpOOVQvd0NjTThYMVB6bmlmbWZuS3M5S09Zc0gvV1FqckN6T0d1WW1yOFlvT2tEY2diNDlwcCtPRDJsbGNxZkFlYWxtQVkxaXJLWE0rdnpPOEh2RWZHcDBydy9HRUk2dlBlNXR6MzZSaFJwUG9PYmZ2Yzd6eFB6UHpreitvajRNaGpFOXNhMzhzOXBQem50RDg1cmtGdFk4NythNHQyQ3pjUk9SZHBWK0dTcHlSallXeWJnSjNRM3hnYndoTHZSN1huS3Q4bkxiSVdJeThJVUtNNE9RY0NZZmlabjBxUnpuQVlpdDdZZ2VQdEFwZVlnR3IyelBaR0tmUjZzNitQNUVPejdTZm5QZVQ4NHRSTXJQUTZQSDhxVTk0Q0VVWm9QZndmSk9VSERhRVJ4RDV6VzQwWENSZERHbVR3cURnSmVDUUNLbEFSRHcrajhXRzgwcFlPQk5wOVVIVHZFNUFYNWhtOXNSN2N3UjE5elh0UGdtS01Famp1OXArLzhBQ214UTFIeFIzazY1YWdrMVV1dmhqY0lXVUJmZ1ZpcU02SXI1TXROU1loUUhISFU3dzFxSnhWa3o4SndqTUFWMDJlcnpreUJ1QXZ3c1B3MWhiQTVFam5rdndSOWNuKytrc0ZZUkQ1ZDZ3d0VSTWt2cXM1czJsRXREcmxMWGY5L3IyVWZORDNUeUFMbVpKRTlmeWpRVXRTVWk2aWExenh0Q2ltQVN6aXBxQTVVeTVZUkVaUU5STXRQbHRFWW1BbGttZUgveWQxckJsWmtIT0R4MTNGMTV4U2Q1UXdScEhTckV4SFBFZEFoV0E3bTdSZm5XQVppYk5xZ3VlRVZrNmlCckhMTWdSZFROVG9KemlMamtqby9LTnZWdU44OWh2dWo4WFdFZUszQTJ3Qk10ZWVHODRyQkpCSVV3Mm1UVmJqRnBCOUY4bitweHBIcVplZkltOTVlcHEyQTZYWlhOMzFuZndSS2tYamxQd2FxVnIvcllKQkJxNVBrRjVOWkFRbEZGREZrYTZ6ZlFCN0tTYjR4ZW96MVhiVmFMNDd3QXduQUVxbFBQTDFIT0NXTnpiQ2lLZUFuMXlGMFNSa0JzL0grNHh4NVF4Qk8wdm45Wkp5TERpbmlwaWFJd1RjRUVJQVRGZ0loQnFGNktmaHJoU1h5UHF3UXNNTUJZcEN0RDlPODFxUnpjbnpDZ0tKTjNrS2FZZEdzUHBkWVVoajRFTzNuOTVQTmh5RUdZUlVSMzh0WnBzQWR3WWlFYzAvUTV4b3dJSDF3b25aWG83eDd1TTJmUHl0eUhSa09qSWRHUTZNaDBaRG95SFdRZFpEcklPc2c2eURySU9zZzZ5RHJJT3NnNnlIV0t5bWFMQzNDaEFZS3UydG5YKzlaRm9RejZDaUlZbTd5aGdCSTFyTWhSdHk4c29xbkNGc1Eyd3VxOG1ERUR2RkNTREtCc3BOcVljVGhJckxXbnVmZnhHRms2VGhVNUJUWE9mdWVmdWVmdWVmdWVmdWVmdWVmdWVmdWVFLzNlVktZbTJ1S1BIQ0FZSkFjRW9KaWVmNUxrWG5lZWZoRXZXY21BMVJGNCtNbDlxTEJoU2VDeUlkY3NhR08zQW1Hd2swZVJjQ2FJRW5aU2FwTUNpaElLWEU0ODRBbFN1Q09Rd3FxMlE1UXpNZ1BzNkN1WXhWaFhoTkoyR2lEY1QxdDhxWEJlYVRiYURmcmhxUkE3QmtJUjZUOFdXY1hVY0x2OFZaWEh4WS9qUEFmQmxuMFlaRWZWaitNclBteC9HWGZSaG42VldVeDhlSDR5V1A4QUZsdkQxRFAxUDhaWEVmZEwrbWVpOGtMd09kUFVNcitySDhaK3Qza0x3OVF5aE52aGxNZkZqK00wZjVjdStuSEtJK0xIOFlHcHVNVzgrbWVYRmU1ZDU1WExkdnBubjlXZWZGc3krbWJiY3IzNnM4dUM2TkxIeGM4dUs5K3JMZHZwbmxjNzNxeEg5eXg1MlhmaUxMdm1zZVZyajBrQ0pZaVR3dG1EemszVUk2L25nRm9tTjZsd0Y3dG1ZV2w1QmgxZ3NycFdkaWVUZXZPUERERkFIS1VtRTEyWVhEaUt4ZjhBemlZM1dXY1ZEVUNDUXdKazFQYkdXK0pHRklXa1N3TzFPOEltUnpCeTE0SWx2cU1oK1FzSGRJa2RpQjZWbnRmbXoydnpaN1g1czlyODJSKzEvdVNvM0xGL0p1QUZISldEa0FpRzM5QnVUa3Z3NUlPcGRITmlNR1lqbC8wOEdBQUdKQ2FBbFVRS29MaFJLNHBDYVExQ0hDQ2haZXJqSldJemtpblRjZHMwK0NWN2xtaW1KWkNma3ZWVElZRGpKOWZzNmUrREt3MWJkRXFiRDNmbk9rbE9scXJRZlh1Y3BCTEdyNkhTUGxoMXVxVzJTMFdDNlRQQXRKVVVzK1FaSzFCamJlUXdtU1MwTlVSU0VIenhheGpEclVQbFRYRzQ0Z0hOTWhIMWdMOGxUbTlJa3FodDhubjU1Y0FRMGFudlRqNThZeTFZMGVKRDhML3k4SzRBYUM5dS9USWwyUUxvT1paUlhWbWJpdUpCU2JsblpyZkc4QkpjNU15anhIQjZjNUoza25lU2Q1SjNrbmVTZDVKM2tuZVNkNUoza25lU2Q1SjNrbmVTZDVKM2tuZVNkNUoza25lU2Q1SjNrbmVDZTgyNUoza25lU2Q1SjNrbmVKUk5FTEVvRnd4cDkxLzRDQmRyb20rWnZSNWo0d2NGWWlPam50ZFIxS1JjR0cwVWNoczhZN2pBdGFwTXJ6YjBsR3l1bE04QWNvbTFpRHBmUk00Y3BtSU1DTEF0TFR3eVROam5wOFhqL1hJeVA3MTliaktYTXNHSGFoR3RmR1pYK2JpVlFyN09MaDVueEEvUmNLQ1hLK1VTUmVuQzZOSmorTnNQNGthSkw4aVpkeUJGRjRpQzMvSk9CalllOEFKaTRRNk15Mk0wSGtJU0ZoQVZ1OTBSRUFJQVprb2lDajJCN1pySVUzdHNKZ2lTSnJ1TEJFRXkwYzZYelFLZkUza3poSEFWa0c3cGNKNDdodFVRNVpnaWpTQXA2TWxxa0JFZTZFYjBoV2pBVm5WVkh4QmJ5b2lESmNraHk0NTlTSmp4NnVWeHNaRVBnNlNXcUU4ekVzL0lRZmdaNWZTTlpCdGMrZzBJa2lQcVltOE5hSVdKcVFxNEwzb29VeU9nVnFVaStnMFJ4QWh3bDBDbFFaWERDb2FlSXllVHllVHllVHllVHllVHllVHllVHllVHllVHllVHllVHllVHdmdk51VHllVHllVHhtY0pwYXFKUjg4MGU2LzhRNnZ4NDU1NTBlWXlZdk9RRFpidjBuVG5KZ0xUVXBSc1NvK2pISW1USUpMTHFwbklUTVhuaVAvQUpkd0s0dlZ1aUhza2ZsL0lQVmNDTFE4NEcvSjlCVGhXRm0vSlI2TFVwelMvd0RxSFBWaTFSdGxKaUhVczgrZUJ5R1JIaUk0MzBUOXpWNUJPWW5abnF6MVo2c1FrT1FSZm5PalFCeUloRFJnaWFNWURWb3JGeXpNMkp3Ym1lcldwQ2p6SnFrYlNXRGhSM0hQTlRRVEFrQUZreUJRUjA1R1R3UHc2d283RndqYWlGQVhFSGlJeVE4clJQQXFHOXhyMVNOQ3lWTjMvd0NHUEJzSndBVWRSei96RDJuYi9aQ2VRRFdpeEc3SHd6VDdyL3dzMUdwcVQvZEhtT0JRaU5EREVnV0w5SitWNUwwQzE2RWZFM3RJaWw1TjVQVEtRZUFIaFppcC93RG9aQjNpRkEybVZwRHI1Y1A4bURGck1YM2RIUTZPSHMvWEdnSU5SUzhuWHluNjUvaVdLVmlCdEFsaUJSbUswcHI5OFZkTXBTQ2FGSjNMU1NpSGdpZE9ObnhPOGhEUUNMTVRvdGhQRzdwd01CUnpqM0kxWjljSVFnTlJ5STlNWkFnckdwbnFyZ3ZOQmN1R3VBVkMvU2ZNVlA4QTBaN1R0L3NHU0pqTks1UzlielI3ci93Rjd1b2tsTDZ4SGNNMTV3d0NsNTNlaWZsZVN3aUlOVEE3S0lTNmhoSVFRb3hLUVRKZCt3c0dKc0tOMGtpOGREd25SS21jbUU0eVd3SlowaG8xV2E5QVVrUUZ6RHZ0SlpKZVNWSGZNMTgxdm9Mdk4zYTZRUkFaK21wYmd2QnUvQ1ZHeTlQbTBrcWY3aXZhNENtMFFsOFJaWmRJSDhNcXlocWdnK3BiZ1I3UDQxaHdnTHlUNmV1M2Vmc3NMajV1UDRac2VWNnZJaFJrMVdxWUNQbUI1Q1QwUTJRRGdEOERlSlpnUEViSldrTHFIRDBxTGdTU1lSTVUwN1pMQy9jTERTK0tPbnl4bkxIWXpUaXM2aERMQW8xWXNKb0VsTVRvNjN6WExsY0tRZlhlWG5LZ1JmSUFRRDlPOG9mMkl4ZlZ1ZmtaVHZCM2RzSGhVUGRKNjZDeUthQ1NET2hMNmpONUFVd0ZJR3p5SlhmVk0wRnBQQ0RrUnNubDVqSWcwR0ZkMGpwNmJqcW5JcVZFQ2xJS1dnUzZkWWVIN09UWlpOako4enVHVVlGTklWbGJlQisyUVNCSURMN1Q4U25SaElRSUtla0xoMmQ1RTJwMW1kUExaVFJFWkxGOUlGQTVTWm9vaXR6bTFQS1NJV053eExlOEtiZVFBUzZYQ0RRbkNqTDlNc2M1SWFsaHhYUmgxYkZnaXVrQlJxNWZPUzd5WGJuNkZ6OUM1K2hjL1F1Zm9YUDBMbjZGejlDNStoYy9RdWZvWFAwTG42RncwR3ZoN2MvUXVmb1hQMExuNkZ6OUs1SEVFN3BONkU1bzkxLzRqaWpPZEVrK2ZUdUJ5WUM1c0pRSTM0Y1RyR0NjWGtnRWI5VnhrTEhEYms0SHorWDV4OEdVeXk2SmlQZ1RvcVZETjBlWlJicUVOZXM2N21rYlZhQWVVaXE3OU1TaWFCdElXNHJrUnprVWhQWUpUZ2t0TTdtb3cvSVZyRnNTR3czeGpxZ1d1Q2g5MnY2MXpxTUFFcTJXR2pCTjNmSDh6QVVqcXZIcUpmbi9BQnJNTWhXTlMxZzFjQTdkYVRub2ZQR1pDS00yaUpRNTJmTTd3TnRKUkNCTlAzRWJ3RU5aNkdlaG5vWkppeE5sVXRlSFdEN0tZTHpxYUlHZXZUQXVVcUtTN3hvL0ExazFJSlE2VS84QUJNenZFeVZYZHBHK2dLZVBPQncweFNMRWdscUI4ZThsRXl1S0YwSzAwUk1MaXU1YjVWQWx2eWJ3ZU96K1czM3Z3alhyTVk3UXFnMzNxRGI1bUVra1F1RU1EQmNXUitGd2t5WG9SS3FFT1hhV1BNYXg3cFEyaVd4Y05KNGlvWnFDSVJRTTR1VzUyM2dkTkdNTUpKWlZHekVjY1pWSnVpa0tBcGFxUEcwdTJvckZ0bTI1cmxPZkkzVXR5cEpBWlVqL0FNVmhvMVpTcWEwa3MzNUFNMCs2Tk1DWDBaZk9zQ2k0ZTZLTzNGWkFRSXlReER3Z3R1c0pSR1pKWWVZMGVNZkpHVDBWWmQzcmF0ekl4aFFjL3dEMVBhZHY5bWFEd0lqL0FETlB1djhBd3pEdEZ1TnYvbmJHRVo3ZUVrSlVOVVM3bXFISVpIQ3pkYW1LTGNmRk9MQVAyamVJYnZQaFJPREdvbUVrdEZlQTMyUVpPT1dnMk80WGhLVHhuUUtTMDVIWWcvWk51Z1RWUkRVTERIMGVjTVFXNWVralBNeHhEUEUyUnNrRVU1b0VCenVxMlRIMXhJR3JmcDh5OC8xTm5mQUp4czRLb1JZMkRlMmRzdTEvcTFFUnZpT3BEa0dPYjNXRXlFanRTRlRyaThickVJdnhrWW1FbUVFZUhDRzlwRURpVGRPKzhMcUFEb1ArcFBhZHY5b3I5eW9NSEtXdU1NR1lUZnIvQUljWGF0Z0Z0eDRJN1k5U3J1Y0RBUm1GTmJxK0VzR0hWdERLOVNhVGhqVHBnZGp5MlJuamxjc2ZFVm0zaENmQk5NQ0ttYlFMeENKTGNyU2hIZjRlY3U1b0dnbVYyQkphK0FrS2FXVWlib3l3a28rZEl4aEZnYVExbHU3YjAzallNRlJ6S29ZWEI2VFdSVW9rQ1UzQ0JUTWRwaWxvUUlqWkVzQ1JiNEcyUDZFdXpVTzh6T1J1dTlJdjlXcnBiQ1Nac2dvRUl0SHJuRWIwNnFsRXdrcmxUZGtzRlk4cFVDVFVzOE03Qnh4QmhDRmhRcW9ISWJkWE9tK1FTcVZFeFpBMHJuTzArSG1Ka2NGRzRtZFJPUjRjbFM4N2dFdXpCMjV4SGtrRkhqL3BqMm5iL2JWb05GZkgvd0FtZWVNOXg1ZngzU2lBa053OC9FNGxVVEdBUlBwR0RTb3lSRG1sNFB5eFVoQ2RSaW5ENVpIVU1QMXdDWVVCZ0JBR1NFUWowend2bGlpVUxFYXp3dmxrdkQ1ZjBRcytCRTVUSkFrTHR3OUNoNUVJZjZQWmV1T1JyMldzSjJRbnl3ajBZRUIrUVVKS2hBc2d6QTA2d3J3SGRReGxJYWh1OVhrR0RFUHJJYlFBSXJ4azRKTUJqemtleWNSbVNqY3Y4QkZJU2JKeU5Ydkl2UDhBQ0hmOEFLRDZZa2xBZWNreUhlVGtIa3hLSkJOR0FkSTVEdkdKS0U0blpEMXlQZUpOc1JnWGt5R1E3eUhlUTd5SGVRN3lIZVE3eUhlUTd5SGVRN3lIZUNiL0FOamtPOGgza084aDNrTzgyaW1jY2duV3plOFJ5bEIrYitIQVVXREFKZE9xOVdEYm5VTzhJNEFyYjhzR2dqRWNFaDRud3dkQTFLaE52b3A2MXZITkswTXBScTZBamJyQmpSQnFNd1lQVWQ0b0Qvdi9BTUhDbVM5R1MrVktYMFlhS1lKVkNFby9vOTE2NGpnQ0FpSVNOSnFlVjAxa01IQ2NTWXNBWDV4dU9SYm9qSkNQbVZ2RVd3bWtrUkw1eWt1OWpqQXk4S1prUVN0UnIvV0JuOWFEZUVJUjJhZTZJa1lmZmN1eE9jN3VlZU9yWGRVdW9nTmNHY2R5Y21LMCt6eGpWckNWYytaSk0zdUJxVzdseWdaa2tJT2xwM0dFcXNBSVdwNmRDYmR6aktEdGdRaHpZdWtSRytMSXJvTU82a0N4cGV6RDBEMUxGcUtpSmtkckJpQWhJMUw2a3ZMT0d4TnAxeFBBVHl0T3NIRElsRE1vbmdNelBBaTVodHFuMHRoRUR5VENUckJURmNEeURPWWx2UWljQzVtb3MrZTVTb25VRW1wMy9sVVNXQzZJUFE0Y2ZIVFdKZXl2SjZpSGZBQTFlMHBJeldxVHo1d2pSVUJRQVlWRUpnY2xNYjVBZFFxeFlEZnJMWlR0anIvbXNHMnpjSUkzV1VOYXdoSVJNYk1Wek54dDNGN09IYzJFRTdhZU1vMXdSQ2QrQUQ0Zi9YMnp0L3NNVXZwck5QdXYvQ0NNU0U3QXlzdzYzamd3NkpUU3RQMk1rUHBOU3hSVXk1bGZoa3NwVUJVaEVNaHA2UnptbE9NcWVZUjQxOFhyQ2dFTkdRU3FpRS9FZFJraCs1VU95L1Jtdjl4Njd3MjBMcnlERy83T3Y0T2JwYUVUNjl2RUFWQVA1d3ppeWwwSnRIeWQ2cVNUOGNEN3NkZ1VkZm5QZEg1d0JmdGZuRkQ4WDV6MlIrYzlrZm5QWkg1ejJSK2M5a2ZuUFpINXhEam1sRXdKSi9mbXdUOEpEY3hZK0xpMURzK2Rkd0NBUk5LWkJ3R01hNXhtU0RnYk1mZ2Myci9Tak9VaGFGVnhHYkV4RVRPUmF6ZGpFRm9vMXJHNzRCM3BaQmhmcGt6aUNxVEc0UzFCVVVFQVlnYzNqblJWODdtWUc0TWtPU0dUZDN0QmVqc0dOeHRoS1RsU2xDV3ZSTW9XakVpczdtYStTTU0zbHRCVXRGS0xZdHVxYmpHNk92SlRtaFVWUHZrdW1BV0xuUW1OL1BaN0kvT01TQ1E2ZWpDempGck10Y3YzazVjeHZGSStLWitGNHJEV0FIaTIvaGhFR1FXNzlCSSt1S2w0S1VyQklXTjYrT08rS0VNOGlKZk9NU25URmRXbGI1KzJJQnJ6SDU0RGw0eEcraDQ4aytHRlZPZUh3OWoyNGZxL3pudWo4NTdvL09leVB6bnNqODU3SS9PZXlQemh2c0p4MitjOWtmblBkSDV6MlIrYzkwZm5QWkg1d0dwVDAvT0hESlBIS2U4L1JuNXlvNGl4Q0VvdlcvaGdkZHp5eEQ0VXA4Y1pJWlZWa2I3NTA5NUUvUXdnRkxXY1pPL2hFQlZiRGVqRjREV0poQXQ5VjhNVUJta3puWkh6ZXVGVGhwTnRzOHR2blAwWitjL1JuNXo5R2ZuUDBaK2MvUm41ejlXZm5QMFIrY0Y4RDBaQWdXcnFtTGI3aVh0bFpWZjVOc20xeFJzeVRsQ29VeG1kUlBOblYxUVBPdFlseWdqVFhRRkNJU3ZsVjRENFdTeXh2cysyS0hqaExicTJNakdZQ3hoNnVYK1BTR0NNQmxSUG84Z0R2WTNnZ1M1UlF1RVBuNWppMU9yZE9uVkJNeGQ0NUxac0pNZytGd3Fwb01tdWhCa21aRkJpWmJybmhHa1NZdGRBeUFqeDVjWnJ4OGM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YUFg5YzlmMXoxL1hQWDljOWYxejEvWFBYOWM5ZjF6MS9YTlN5Z0JXcVBBM3FiWjgrbVlJK094OU10Vm5TUVkwbTMweVdwRzR4ekkrZjVQMUVhSUdpRnV6VDJwM2V0WWlPa2UvOEFMeUQ1Yk95UVByQU9Tci9WVzRqV0FKa3pDMTRidU1saVVSa0ZrOGt4VnA0Z0NpUmNGTUQ5WUtGTG42Q2MrVUMwZ1NJQ0RjM1JPakZsZTBVWHdiTXkvbEd4VXB5TTJ4QkJWNFdEU1loS3NVMGdFeHZGelNoNG5rU2liWVo3ZGxKUUVZVFJWQW02MW9jTGgwb3FRMkVTQzlNRm4vZER2WkZFbFR5VGdZblFra3c2YXE3R1NIK1ErSGdNNFI3ZzYxUzB6T3N6a0JzRG5PSjlJUitSV09FMUtFd1J4aXcybHp4MHl1QlVLSTR4c2hVeUNyalJtMmRWS0lXSnlXdStkR1JRNDFRMEtIWUkrSzRBQSs5bWlhYXMxY0dGaDd3c0ppbWlLaFo5SVFoVUZNa0RuRUxSM2RLaktrSXZQcmlEaWI4WUtJcnluc2VxdHRWUzV3WjVRakcxdFVuR3VjVXNJQUhZbWliUzFvb3cxTU9wQ0hZSStRZW4vYi9VY1F4T0xzQ1I4MStMK1VnRUllbnA4M0lrT0FCK0k2MHc4bFFDTDFIK2RuRlVSNHo1eVJzcDM0RE5oNFphTFdVRlVKS0F3TnpwdHdiak5lVFlSYVRGT2JMblJHdlRoV1dVZWlaa2lMd2pkWjFqeXZIcGFjckFXdGpCaDBMcGU2SjB5V0tRTlVOR1VuWnZ5ak9oSXlyS2xBZElzT3p2L3VsQ2dyU0UwV2E5Y2hVc2tFdFFuU0dZNTU0L2dMK09SeEFMRFZFNGJzTUEvcUNTUzZNa0ZlRmZoa3Mva2JRbnRjeEV1NXpBd0lXRlFTeno4U3RlWkRnK1FCbkt5YTBSdXRQa1pKSzRvUThoOVE1ckM5WjlkZEVPeDJNY3lBUjBHZzcxb2VzSnhQVzNpeEV1WGtST25UQ3Z2YUtra0c0NnRodVFBMHZXNzBiMGMzbWlDV1ZITk1ERW9ybkVTam9Rd1JVaThzbWlkUWpNRENWdkduaU5peDAwM0czUkN2QkZreFVmNkI5V04xbWdrL1NFbjJ0clUrQXNNUm11aDJCS3dURWpSZzVKUklUbW1CcjBYcWJLeVBGUUV6RUpLeU1vNzcwUnlBcmtZTWVqZkE1VGkzd21sSW84UWxiY204NExnWkFjQWVTVzZhdExBRW1iNHVxRzBSTVNUb2pVb0F5cEZhNCtvdjBYV0JLbHdsTGRzQTYya0JNWktQWmdLY1RIb1BqNXJFc0xrQWhRUURzcWxreFFrWUZWOUtFaFpmRjg1ZGpKTWIyQkRnQ1E2RUZ0WUlvWnFNelFGUVJYS0NGRGUxOEpaLzBmMTdGZXRVTlJ5Q1hseEhGL21YdThJMGdOTStWUXFqbDhJdUFtWkE2WDVZRW5UNUl2MHY2eTRPazBrcEtPcGQ4WnJKNVNmbEJMVGJ0OVJoWG1PWms5VkRYRndTVVBSZ1dvUkNkanh3UWlJTjBUSmt2Q25mZklCMm5CQk5ocUIrNkJTUGdpNUV6SXAxRkx4aDRWQTR6UkFkT1A4WWdOQllRTkZOaU9lN3N4bXNOTDJGV2lHL2dDSnp4R28xZWg5TnZrRTRKQUVUS3NzRVdnalk2ZUl3MlB4UjhhZG1nMkdsY1liZ1F4UUg0aDlXTWNSVjlaSFpDMTRhbmJFSlUwdmhDMFNja0hZVHprb2dpZm5TQkMwbFJNcldhNFFRbWhVb0pFM3Zid3F3NXBJYVNEVFRIb25VTWVYR3RpR0NTdmc5R0tEZFMyeUEyU0xYMDVUWHF4dHJSaGFNVlJPbUt4SnRzbkZHRnV1YUgwNHdZZnNwUFlBcEhyTzRjMGNRUlRDbGpmaGJpY2Vnazc2ZkhTNWsxV0VJc3JVQjZDQVhTQWx0a1pVeCtIVVFyTXU3RERSSUxpOFpwQUpRN2tTVnYvQUtUNmpqQUI2RHgvSnpKTElTUkNQaG1hOUlwVEFUYVVmRTFJeStrZU9JOFg5WFJNa0RmbExHdFlhUU5MM0YyUElWeW8xR0liWVM2UFlkSEQ2TVNGRUduRnd6RkVoMmtKd3FSMVE4ek5CUWF2eGVSWWh6R2xhUG5EalZqSU5EQ0NRQ3l1cFRTcENSU1pNMEtOSXNjT3QwMWRBYXUweEFGN2p2U0pjSWtXRkNSNUs5bDlZREROcThCM2lQb0U2YkFUbHBqbXgyRnc0a256SE04K0NCczRzZ3NhaDlNRGNzRWtFUUZ2UzhKSmtEcTZaY1pNanl0K25PZVhrMTQybE5aeE53cmNWTmtsZ3dMQWtOSjBxZDVLQ25QQ0hZYUJqcUhrZHVRQmtBb1R1cDY0VTRNaDBUVWhiY1hpUXppZmxxbG5vQ0YwN2FjaHc4S0FvSkVYWm9vOWpUY09WejVpcmVDOW9jWEhJa2hCZzJsUnJVMXlxY21wd283cUk4NDlITTR0Zkt2V00ycXhwZHppRzh2MlNqZ1NnNmVYSVdiaEFoU2Fxek4xUVBXQUN0VUJMYTNlMkpyZEdJU0FWeVZ6VFc1Q0hpOHVzSkF4cWlLVWhMaTQzZzJFSnNKQWxlMVBqeGhLeWhSMkhKQlZDVjNqcWI0SFBDSThhaDR3Q1liRVJNVzl5UVg5VE5PU3RGUUFqVm1adER1Y0hWa3Vza0wwek5mOHhTSmVHR3ZoL3dBZmpnVW14cnAwVjhSVDVmeVNha25NaEl5a2pzVWk3TW8yRkVaUjNXR0ZNRUxaL3JDTFREVHl1S2E1M2lLTFhvdFlTamE3bW9JWXBRVkZjbUMwYVUyNWtMbFREZDJJQ2dVRExWR0lJMHBGcVNXNTBwTUdBbEJvR3Q0RDNCMjFjbnJQYjdTeWxMNjdtU21UZGw0SjRJbkg0MXN6VWhoZzFkOFFPMFludnlLMk8zb0xnM29pZlFveVZNenRMT1JqY1JNSW5ZTVRkRUNZMHVFWFJQVFZ3S25oQlF3VDNpTjBUdE9INHdQQUFhRmVWbzBqQ0tYeG5XU1l2S2d4eVZPQVpRc1JJVndna01wNDJoM0hRa2FHUjVEUWRQTXcrWVFEb05tUWdEcGFXZEFxYXBqY0VsTngxekdFYVJVbWswQXFPQm1NT2NTMDNEU2ltSUtlTlRKR21xU3ZjbUpETTFEQnFtQ043U0NwT2FiRFdaVU1Tc1hLQklVcENJa0ErWVpLYmFBUnRGWFdwY1JOekF0Z0c0RXlHVFJUYVpFMG9qM2dwRU1iL05RU0xVT2tOSVdGc3VFaUV6Umx4dWxEVHNaV01nSUJlY2tncXlHeFN4RlpCOGh0azBnQ1FFQjFadGw4RE9qRFYxelM5Q0ZTVWt4UWlpWW9VazlEczhkVVd1Zk03UlV3eVQvei9yMjhRckZFVHFFN053OEF4OC95ODdiZGtBdDZSWXU5WW5RaVFPRENjazIyNWpFVVh0SEpTWjAvcTFBSVEzclRjVW81M2xoTjNRUzV6SUN1ZGlFeEtCQ1hvemVkcVNLbm1SZ1hvRkNRMXBBSVJmTzhDcHBWd0tFN1VFeHpwNGhWMWpIWEVIaHlkdGhqVzNlU3pVM1VSNFZnWGM2aklpQWVtM2ZmRW40SkxSVWdHVUV6Mi9BNTBMY04xWFJic1JscDh1aVJPSTdNQ2RKa01ZUmR5RHVKeVU2aWExOGhrUkpqRjJER3pKc3JrNHZiT2FRSTNvY0c1VU1QaDBPK2hLaTltdzE1dzJuZGV6UU5XQ0dPSDFLWXBJSUV4V0NVQk4xQVh2QmFuck1hRnV4WkkxNXpvRlgyb1VzbTQ2bThjRTlVSkFDV3Nwbno4eWd0NWxJS2RVZUx6Wm9CY3ZGbUtTUW1Lc09LWlF4V0tSVmx0cytDT21IVnlzR0xhWXRhR3d0aWNDeTZvZ0FISUJLWmFZR2JCNkVGY0o4YUdGbThTTWdXS3kyWnRSc2p6aGU1UVFJaU1sdE9sNkxNaGlGYUVxTnNqTXRGVWgyeThycmFHTTJDcDV1WVFZaURxaEtZUUhZQVdIWkNNTHloTFl0RFh3ZHZNSXVTQ0NORWFWU09GWGIxR0JqakZDR2g5dTNYcVZWSmdJc0dFQ2kxR2s0UmdRandlU1pRdmNhYS93Q2FwUFN4ck9XSXBxd1J3SzBQNU1RNG93R2ZER3NtcW5jRUcvRC9BRkt5b0kyclNUMWlHVW9sUm0xdVdydjRNV0drcG91YnlkS0FjMW9iUitBZmdrbmhidzF3Z1F6MUxUUStMY2J3b1Q2NjZvaFpkZkxVTW5yYVVzUzZYcWI0TDZNYVVFSlF5aVNMcnRKREZxUEEwMkJGc2xQUGhrZmQxbzlueFpFLzRPVFVnZ3RPWUdFYU91a1ZodEVpaEpzZkNYQkc0cVFJeWgxVTVBVk9rQ1NWWk9oV1FnSkZTeUtRN1V4aXU4UHNNWW5EQk85eC91SWpaSVpRaElPZzBFOU8rRGhVS3k1QVFMYVY2bVF6R05ZQ0JNS2haMVBFMUpzcXA5cUk5YkhUY1JuUnlWdVBWQ3JkQ204VU9aa3JOMm9HWjhkT1ExdWpTTFBnZWh4S2d4c01RS2h6azNNUExTbDRBOHlxR3kyQzlLY0o2WEN3SW1BUlpNU2tPUlY2VUY2bFJ1Y3p0OEVYaTQ2QlJBSVdoTDVHK0RCWkNBdDRrU055V3dsNUdVMXRTVkVUeUpvWXZLTDhGcWFtc1ZENVUyV2djc2EyckpHa1pGMjFpa01ub2IzbENBSFNiakpid1BnNGtxazdObXNWTU93dE5nSGJ1ZUVIRENGdWl2bDEvd0E3azZsaWs3bzdvTXFvRmxRR2t6dC9qVVlYZXhaZkNQRFdURVgxZzVNcGtqT3JDR0grazZ4cWdCTU5QQkk4T1BtSTZJRFliUUlvNzgxMUdOR2h6S1R0U1o4QmNjN2F3V082d1FGSTMxZ1lWVURTcDNTRkZSZTh2OEp6cGhBL0t6V0kxQm10Q2tySlRsSU9Ra25ITm9HZWt5eEVlSFlrc3V3R0VyOGl0dlFRRGtJellWcTQydVRkSlBURzZHWHhXSEQwVmNheGNBRVlwZTVQZ1FrcVdKTXV4UURVZ1BLVHdUcDJZMXRVT1ZHQm1KaUZUTHFSZUE2THlpRUZRQmVXWldMTWlqSVlBTmFDWWhJOSttVmhHaXhPQ2hhdjhQT0REMFdPd0hmVUtUNFFSSUpNTGhacG9rUjVxc09FKzBFb3dUMXJya0VZZjlaOVF4bFVEc1dodDNjOXhkMy9BQ1JwdVRvNEJqSzJTNnJqSE5zeWgxTG4wejYvL1VyOEFtRFNPaXhuaUh2QjJEdGc1NklpYUl6dVE0dFhLdUcwbGdRZ3h0R3lNaDJuWE0wSFVBU1JzaVFoakpybUVUWWpDcUI2c1VYR3RLZ0Z0cGZBUmxJcnBZSzA5SlRYYXRwc2tVdGlGWXBBZTdEeEtiT3VFZUhGMkQxaFJoTlEzbzdEVGFDY0tSekRaVnFRQ2lmcUtoNk8wVG5LT0FwSk1pTzZKcFU4eGdPZFNjMWlnNVVBc21wVFZNUkhDNkJLRVFKNWUwQnljbTVEMWJVN0RZeW9DTVdOZmtPWU43d0ZDTUFWQXhWRld4YlVoS2d3K0hGNFE5eWdGbUJLbVk0Z3BTaFQya0M1bE5OQ2FPOFNSb1dJMWxlRFVxZEtJK0FpdVRTcFJac09XS2pEYzU0a0t3bVkxc25lRnVHa3l3WUkxUkR2MVJoREc2NWVYUkx1UTZSdGtTRHhHSUpYS3JnVGw0UENudzJ4RTJiSmowWmxIV3Q0OXhvM3Fab1JLVkpBVlJUVFNZb29Hbkk0QlVVZ3dWRzNqL3BIMUxIVTdxSklJb0htdlh2K1hNSlhMYmpQZSttZlgvNmdsaEFVR25SRmpEWHF3YTZtN095VU1oNkdtb1pMSWFSVGQ5M1FZcVk1T2RrZ2xRU1IwaWpraldSeUIyVFlLdG9ncHk2eUpNblQ5a0czMTFxTWNjU2tTUnBUc2ErdWpKcXlXRldCNG9uM3VkRVl4WGtKOVA2QVE2bVRtRDN6L2lLck4yQWg1ZVdJTFMyT2MzRFg2S2JNQ0VLSklFbG9jSk1KRnpjRWRFcDQxeEV1QjJEMUhKcUFRRlZwVjJnajVLeUhPbDZ6UWVBMlR3K1JFODNETTBHbDRiY0hSWmJhZ3RtenpjOHRPcTNrbm93Q3BMTlRPZEdjTFh0bDB5WHFuYW1vVENXNmZnaXA0U2VSb1JmS0k0SVdDTEVpa2E1bkNBR1IzR0Y3MUVFdmhpSERRdXBiQlQ0eTB3bHJtcjN2SlBhSk5rVGNTdXRDa1lqWGZodlJIVDRuL3FQcU9QOEF0L3orUitpb3FtT3EyRUs5RUVtUGUrbWZYLzRKL2tVaWlJeVVyZUxYU1hlTHpDRFNrbXVSa1VGeUVkeGNnN3J2aVRhblV0SUhFcWp1NWNjeVZBcUdpeHd3Tm81YkI0V2lZMGpDWUJkOXNJcjBCT2FMMlpycUs2OW9OeWZoTGpMT2xKZ2gzeElwdjFKQzRKdWtyNVRkR1k4WHBKZ2NqNVhFQ0RYSUtETEpuSkdXQlJkaHdCU2hzaklwSmFYemF4Rll4S2cyazRPcXhTRnNTeXk5QVRJdkNjaGlFeGFJU2duSU1UanJDRW14aWhSS0dVUklSR1NoQlJKWVFCQ0d6clJadkdvTkUwblU1dElvN01BNFB6NE5CSlE1S1JOTTZGeEFTeE1kQXhFc0lrTkRSRWdkNElPUFBpd1NXQXE1VEprc25tRFpJTk94amN0T2FYOEU4TW1FdWcyN3NYTmo4cXZHa0VzYWdrSkpJK1cza1QwbGFWTWJzbG9VaGt2QkNoMzhuL1R1c21GbzV2Zzc4eGwzQlRKdndQSjhmSDh6dFE0YjJMODA2ejJQcGtXOUJJVTVNSlNVT2o4bU9EYzVraVVxTmE5WnlkUW56VjFiTElSM2FrN0ppdDNCV0dodmNBRkhUZEVmN2hvWkFGWjhXaHdvTWxVb1ZzYWYrdmhOalFVN3EwMlI2VEd5TWx3SVliWEw2Y1BZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZlBZMzN6Mk45ODlqZmZGL2JmWFBZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZlBZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZlBZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZlBZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZlBZMzN6Mk45ODlqZmZQWTMzejJOOTg5amZmUFkzM3oyTjk4OWpmZkZoeSs2OE5HaVdXN25iVkJha0lFZEYwZnlzKzl5Q3U1YUNpTWtsRkcwT0dDSGZuNFo0ZTZWTzR3VG9XTjJWajNwaVJ2VkJGV0FYZGVXV0F1a2p0d25mYmpCWFNxQ25SNU5PVmVsdjBUd1h4eFdDZHRvZ1k5aklRY29hWFgxWmZGWkUrSy9ERkVxSTcxWEhEWjNrbzJtdWxlbmNQMDg0cEtIRUhTS1RXN0U5czhRempzQ0d6azlWNEhST1FST2lHWTBYeHlZTGxCVUVVVE5YejF2SkdBMkJBOU5zbHNqcEZpUS9MTWUxR29oTitCVVJrZlBvakp2VURCakNTTDRSVVBaVVRYWVVCb3hHNHJ6MFpDRjZIay80L3dBY0RXUlVSMU8vU3ZUMHl5Tkc2Qm4yaGpjTlNnWWVzZTlNZThNYnUvREh4Y0VSMHJma2llZWUxTUV5R2ltRXJBS21pcEJzWVR5RVQ0WURkZEpHREJrZ2FUVGdxM1NvQUdOQlZnSTQ5Nlk5d1k5Nlk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFk5NFlaME00cW5FRHdveFpxVFRFMEVsb3N2SFg5SEhsZGx2SlFSbEFnT2dJQ1oxM2Nzci9EcHc4STRQcVVFWFhqQnpaRU9WdVZXMmdqZmNyUjNyQkVBNUtja2JSV1hGaUM0MkR4aWQrd1ZsK25mK2g5SHlBdzVLc2ljaWhpMEtmbmk4OUpKWnM4OS9ON2NXUmxZVGtoY3RpbVoyaGdJem1lNktKSVJxL3JESmNGRVJteS9TNXdTd0FHRWtmWGNWRVJVUm0xUzE2bXdRcVZEQ1FDTUlVRWJicC9DRWFwUjVRTnNjVW53WXhBREVqUittZkJpaENoa1ptNXpuTHIxU2QrY3A3OEYxUzVZSURYczh2RURQdmpTU2pzTml5N3UyVmhJRTRURDVRRnp6MjR1MFZuTitnN1h3eTdxeUxsVHVBZVIzeGhGVHdUS25aUGd2V0p0M2xCc09va29vcW5ta0lnVEpFUFN3bnFlYnlGL2E5dUljRVNPSEZBQWlTUnVJNnA3UGxqTmx4Z2pOUjROZDlyaEJENTNNYURGTmVYQkFsNHFtRlpXdDJvRjJNMEZpQnNRREx5YlM4citUWGFJRVJGUXYvUkIxa3gvcVNuU0hsWUZNcFVLUHNZWlM0QUdFTldydUNLMnhQR1d0RTdMNUY2NkRDUktJRmxJbEtySXBJNTd5M2YzeWtwZFNkUlk1Y3hydGVoSEJtenVFalBGbGVISzhPVjRjcnc1WGh5dkRsZUhLOE9WNGNydzVYaHl2RGxlSEs4T1Y0Y3J3NVhoeXZCL05Zc1VlQzBJK01VSEdWTGdseUJFYTYxL2pGQ3lLbXBIMEVtNEZ4V1ZXVWdOZUs3RUVXcEp5VzdBQXAwMTY0WjBnWmpKL2ZNL0FpWEcrM05wTlBMRCtaNGlDV092V1AzemZYRnAwL01GR2NvSXptc21FTlh1ZU5VWU5FY1ZnaVFuTTMwdmU4WFdBK2NaWlR0clJOczVGS0tNZFVIbURTbGc1QytaaVFOK0JBUUhKMWthN3dyYXhWUUViV2J3QmlaVkl3T0ladTRvdkFxWEZpd0hSMDI4OU1BdTA1RWxMUWs2akdRTWltWVFBT2J0WGtraTBaOHBCRFJTZ21pMHJaRkVFK055YTJFSGhXMkZDMVZVSUVBN2hKVEU3YXdzZ3NSSW5hbUZrT2lVTm1kN0NOaEpsSmczczhUanZpWUl2aFEra2FibHdvNFh1QVlCWGFmTUtGNUJac3dXN0pFRVROaW00eEJ4RFN3UUlnek1URDhYQUZFOHREbllqY241UFhJbkE4c1l0aGF5SmUrTVVYRUJTSjBNU3JhREx2QzQycmJncVhLZmtZM0lRN1BEZFZwK0pzR0xISkJ3bzQvNkp3ZGFsTkEraFJZMHpWemlFNkVDZGtad0RtVmx6QlF0d2pFRDBJTENWTmNNZjBpU3FxcXlZTWxwNDZ5UUx1ZnlubDhOWTJSS1pjazJTMHRabVhwaUtpcmNUSjhqeHJZM2k1Vlk5eFpkOHljY1lDVXNVaThnMGVpT2NuYUlua3kvK1l3Y0lNNUxxZnIvUC85b0FEQU1CQUFJQUF3QUFBQkFRUk9zUVNTU1NTU1NTU1NTU1NDU1NTU1NTU1NTU1NTU0FBQ1NTUVFBS0NRQUFBQUFBQUFBQUFRUVFBQUFBQUFBQUFBQUFDQ0NTQ1NTU1NTU1NSZ1RiZldBMVFBZ2dYL3lCVlVobTdlY3lTU1NTU0dTU1NTQ1FTQ0FRU0NDUVFRU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1F5WVNBU0NBU0RTeUdnQ1FDUVFCU1NTU1NTU1NTU1NTU1NTU1FTRGVDQ0NRUWJDeENTcUVCUUNJZDJTU1NTU1NTU1NTU1NTU1NTQ1FTQ0FESVB4b1FRUVNTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1NTVGtRQ05DUnRnQ0FRU1dLSUlTQ201QUNrelJTSUNTU1NTU1NDU1FTdlFRQUFhRzBsSDkzUzFVbTMwdDAzODNzOXNTQ0FlVFNTUUV5U1FrSVFBU0RTcnRoS1J5RXlSUUNTQ0FDQVFRRWM5dzJxU1NRYVNKMjNuVldCR1MyVTIwZkhWNW9BQUNTU1NTU1FjU0NTU1NTUVFTZmk5L2Y3OS9MNVRObEtuOVNnQVFDUUNDU0FCSG5BQUFRU1NDU1NDdWNLQ0RTUWNXeFRiYmJiYmJiYmJiYmJiYjdSTVNBQUNTUUFTVG1oRHEwemx0VzZwSkpKSkpKSkpKSkpMcEpZMk53Q0NTU1NBYVFZUHhCSHBPS2tTU1NTU1NTU1NTU1NTU0NTUWFTQ1NTU1NTUVp5UjJNYkx3Q0FDU1NTU1NTUUFBQUFBQUFBQUFDNkRTU1NDU1NBU1FSeVl1MWhZQnpFUWJReERKQ1NTU1NTU0NTU1hUb1R6MjVDUVFTTmNCQUFFUGVyQkxJaVhwYTdTU1NTU1NRU1NDT0JTRms1TVNDQ1NMZGFBU1NTU1NTU1NTU1NTU1NTU1NTU0NTUldTNENTQUNTUUV5UldnaXFTU1NTU1NTU1NTU1NTU1NTU1NRU1NDZVpRUVNDQVNEdVNHWVVSOGhzV2hFYXJERDIybWYvQVA4QS93RC9BUDhBL3dCc3g0QVNTU1NRQVNSZ210aEVTa3JuTWFwdVA5cnEyMjIyMjAyMjBTUWdDUUNTU0FTU1VJRVFDSXZVWCtocVNFQXIrOWNZQUFBUUFBY1J3U0FDU1NTZVNTVjBrKysvdHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dHR0dEw2UUZ4UUlRSVJKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpKSkpMUnlUMkdJQVNBUVNTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU0NTUit4UmdDU0NTUVNTU1NTU1NTU1NTU1NTU1NTU1NTU1NTU1NRUkNTU1NCaUNRUVFDU1FTUUFRUVNTU1NTU1NTU1NTU1NTU1NTU0FLU1lTUVNBUUFDU0NRQ1NDQVFRUVNTU1NTU1NTU1NTU1NTU1NRU1NUeVNEU0FTQ0FBU0NDU1NTQUNBU1FBU1NTU1NTU1NTU1NTU0RvUVNTUUlDQ0NBU0NRQUNRQ0NTQUNBQVNTU1NTU1NTU1NTU1NRQVNhdVNDUVNBUUNRQVNRU1FDUUFDQ1NDU1NTU1NTU1NTU1NTU0FDUUxTUUlRUVFTQVFBQVNDUVFRQ1FTU1NTU1NTU1NTU1NTU1NRUVNCQ1NCU0NTU0FTQUNTUVNDQUNTQVNTU1NTU1NTU1NTU1NTU0NDVG1TUkNDQ1FRQ1FTQ1FBQ0FBQ1NTU1NTU1NTU1NTU1NTU1NRQVNZU1NDUUFBU0FRQVNDQ0FDU1NTU1NTU1NTU1NTU1NTU1NTU0NTU0NJRkdpU1NDUUNRQ1NRUVNTU1NTU1NTU1NTU1NTU1NTU1NST1NZTGJQYVdtMjIyMjJtMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjdTUWR5Zjdqa3RMRUx5TkZMSlpKYkhaWmJiYmJiYmJiYmJiYkxJQ1NES0FQUVJBU0JDQUNFQ0FDQ1NTUUNBQUFBQUFBQUFDU1NTQ0FDU1NTU1NBUUNRQ1FTU0FTU1NTU1NTU1NTU1NTU1NTUVFTQ1FDQ1NmLy9FQUNzUkFBTUFBUU1DQkFZQ0F3QUFBQUFBQUFBQkVTRXhZWkZCVVJBZ2NkRXdZSUdoc2ZCQXdWRGg4Zi9hQUFnQkF3RUJQeER5dVRvdDZ0dGROMTFOSUM0bzFNMzJmMzhrdkNadWluc1oxdlJucmp0Z1MyTnBkL2xKMW9ZaUY4TFRreWtKdjBNc2hYTTNnczRSZEpaSFVwWndURzRRdmhVK3RDYlBJM0MwWHROa3lhQ2E5QnVSTVBzbGdwVHJKMkRaRStzMUZMdFpYZ3JmeFdTcjVQbUw0MzQrSHA0ZXZpM01zYStIcGgrS2pOZFA4SmZtbG5zT213TTJzSU1yUEcxZTYrSG5SRnNYWjMzWGJ3ZXdwT3N2M2RFTHJpNWFxMHN5WUJTSkxBMG5qcysybnF2SFVaSnBlcCtvTSs1UG1icWVGMm1nNGswWk83Ny9BRThJNEtWSGVvL0xySHN2SW9uL0FFSUIwVmFJMUVOOXRCN0x2WHVTZDhkUDhLc2FVMnVvc3BDYmVlQUNiZnZZc2x2aGN1VE9sRTZYYjlQQnpFV1VVMGpwci9uRUVhZW1CSEdJYWxuek5WUVkxcS9EVmM5TUdRem5mc0xETU1hU1I2RldGZHV2bDNJanpJWkpXTHExSVFoQ0VJUWhvcVZaa2hDRUlRY1F6aUdRaENFSi9JMFo0N1RyNmlldWtoQ0xRK0FoRzNVUXRKZUVJUWsxSVVydTNHSm53aENFRUtzSW93VWpLWHNVcFNsS1V1SU5tLzhBZ3BTbEdwSFN1blVvOHFGZHBxVXBTbEw0L1R4OVQxS3JEVFU5QlowODdKSTJQTUNMekpsVWE3amxzN1BIUHdLVkMxU2JGa3VoU2xLaW9wU2o4TkJQYkJhZC9xWTZmRjBibmZYNFU4VGdyUkp3ci9LR1dlRmpXemJwNmpHNCtnVzNvSlhWeVJEeTM3Q1pnZVJYY25oZEl0UGlNeVpNbVJuY0ZiVVRIcU5jbzY2aTdwSHFWOUdaWlN0b3pkTnd2ZVpaRVJnVlBQUXlTRmlpRVNRY3NDemFPSnRzZHR0TmlKeTBQdEdUSXJvbDRTTC9BSWliUnFiL0FNSjFSRks4dFNkblNMb09VYXBQOERaVjFHc21SZWxMUlZDN2tkeHVKYU5hYnE5aE9vTGRxUkVSSFlpN0VSRVJFRVJFYWVTdTVXYStFWFlpN0NTV2hXVjNLN2xkeXUvOE9kbXNxV1dsVmg5M2p3VGEwTUJOWCsvdTRsZWsvd0NIUW8xSDBRbGRDUG9OdGlGcGhvaERRQzhSVEJSYWIvYzA1RjlTdXM1RW5WYTNFMVhJMzFDMm5XMjlDclFsc3V3eTNYUlkwcjJ4cFB2M0VUZnEzbDZ5cWRNWXBxWjZwbk9tT25mYmd1REttcnVyemlaeG5iVEpNZE5NSzk4clRxcy8yT3M5cDFqT2IyMDc3TDZDZWtKTnBSWTZla1dkWDltTksrYWJoTjVlbkt4MHpyMjZpdUZOZjM2ZnpYb1BpeU5FeXhIbHRIMTZNVHdYQ1NyWXM2Yi9BSWFKN3I4aWw2eEpKUUU1TEVNeWIwQ1lrbU95eUREcExnZGVnSkw5WlVtSnVNa090Si9yVXdLMUVGWVExVXlkaDBvUHFIdHRwdnNVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVNEdTYmEwcXhXZllYZ3VlNy9mM2NTZEIvdzlqR2pNZmdXSWtuanpndjI5Um94S0hOVHBPUnBIN2lmZ2cxbFZGeGtoak9mdnFLUDhFb09vd1JvYXNyWFREZU02cXQvdjd1SWVqOGV5enlMVWV2ZzIwRGxXQ1pyVWFBRVUwdnJST1Y2VisxR0pwWHgvdURPdEYyL3hTUHU0TkdhRTU2VlByNHFsVlcvMzkzZ3A2RC9oT3o5cFhzaG1uZFl6UzhpaEZzV0RwcHdNY2lJYWo4bHpFYXoxWDAvWUtqd3l1cTB3L01GQXRlNWt6Tk9tQWJ2L0FIeHZ3dlFRYlZiYk5QNHFPL3JoUmFIcmMxMDhaZFZiL2YzY1E5RitMNkovdWZZYkxDZGY3MU15TjZ1ZjBXZUhqWDdYK2hZbGMvN1hYNmpXazFuOSsrZzlmcHA3cjRLWElzdnIvUjl1L3dBR2w0MWVWRzlIRFVyei9zYnIzZTBNMW8xYjdoSmtJeU1qSXlNak1rSjR3YVJxeFFsRXVGVzZhK2pva1NxU2JTNnZRMVNiclhxaVEvaUZaRHhuMVZ2OS9keEMwWDQxSVY5eXNyMEtlS2JnM0tONCtGOXEvd0FDZ1pzREh5aUxmOGcxUVcweHcwbUZnMTFUeGVwcFdNam9mR3RkZ3RyVTQvcnlmYnY4Q1JhWXI2RVR1Q2wwS0pNajFUeXRLSVdCdSt2K05jMHV0VDZWbi9jK2pJdkNiQ2I2ZnY3a1pYUWZ4bWs4TVNTMDhuMkwvQjlrdng0T3ZRVnlOL0JSS0lwZkNydVVwU3J6WHpVcFo0VVR1aFVVcFNsS1VwU2xMNFVwU2xFU1k1dzJ0Yko1K21ndkNaRmIwR2RYcC9oL1l2OEFCOWt2d1VxR3hKTnV6N0NockwvZnVTRkVKM0VkWDZ4bzY2TEhjTzNzOWhyT3ZVZlF1Z2J3bGRqWjYvZi9BR0tGUkhnSk5FVUZTWkJyZUNpcFZEeWhTQ0RWR3MweHBYcnJOcDY5eE9raUxUdDExNmxCbE0zOTdmdzJ1a25pbVRzK2d6cTlEMC9ob1I2dE5mWVFsUkpQaFNTUVNRUU5heG9nOFJKM1JKSFMxSjlmUVpudUdSTUdnaW00S1dVUjh3aTJIUURLSlVoeXorKzVCbUhMalFzOHJBNG1CS05SS05iQkNWUWhLMUNEQ3BKQkJCQkJKSkJCQkJJeUI0Tnp3bWRhalFpMEY4WUFDcXFTYVBLOVdmUW14TmgraFJQWStoOURhZUVaR1JrWkdSa1pHUmtaR1JrWkdSa1pHUmtaR1JrWkdSa1pHUmtaR1JrWkdSa1pHUmtaR1JrWkdSa1pHUmtaR1JrWkdSa1pHUmtaR1JsMFFTN2xwdGpydmtXelRjaGFFSktKcXo2TDVFaTZlTHlyQVNySHNmeHNsTm92a1pPVjlBVGpvOGFua2JWbExUNi9JTElyMDlTZ2t2MmZDaktjSGRHMVg5RjhpdVlpdjk3R3VmTEdCaWQwYlZmMFh5TEpaUmFlVktyYjAvZjVBZEVhMWRSc2p4cTVsOTF0TTU2K0x4cU9TSndXbmxTdTMwZmY1QjlmRnEySFErdU9mdU90SldOU3NkZkZjKzNsU3EzMC9mNUZreVRYb3ZYMTZDVmFrZmxXZTlQMytSVVhzL1NDVXIxejVjSVRQN2MvSWlSdEYyWDY2SFFJZFBLbjZrbFovQzMrUkhDcWEvZno1MWZoSXJselBaYm1PbnlWSDlGWng2emY1QXdXb1ZXNng0cjVMb1BxdHQvQjVNZXgzMm5xTlMwVVdySEVqT2hwYXlsMVhLZCtocTAxN2ppS3VmWml4cWI5ZlFXV0dXWnIwRzExeTkvOWp6RXordTNxYjNsbTk1WnZlWDdtOTVadmVXYjNsbTk1WnZlV2IzbCs1dmVYN205NVp2ZVdiM2xtOTVadmVYN205NVp2ZVdiM2wrNXZlWDdtOTVadmVXYjNsbTk1WnZlWDdtOTVmdWIzbCs1dmVYN205NWZ1YjNsbTk1WnZlV2IzbG05NWZ1YjNsKzV2ZVg3bTk1ZnViM2wrNXZlV2IzbG05NVp2ZVdiM2xtOTVmdWIzbCs1dmVYN205NWZ1YjNsbTk1WnZlV2IzbG05NVp2ZVg3bTk1ZnViM2wrNXZlWDdtOTVmdWIzbG05NVpxYnF1ck1hZVQxR1dUY0lSS2pZY0kyWENOcHdoSXNTZlpHK2o0RjErQkgvTVJtcldlaFBYaUhiVGsyUnN1RVBwSndqdDhDUDhBa0kySENFM1RpUXVzbkErbW5DUCtZai9rSS80QTBLdmlNUDhBVWg5bHdqYWNJMkhDTmh3alljSTJIQ05od2pZY0kySENOaHdqWWNJV1ZiNklUTXh3alljSTJIQ05od2pZY0kySENOaHdqWWNJMkhDTmh3alljSTJIQ05od2pZY0kySENOaHdqWWNJMkhDTmh3alljSTJIQ05od2pZY0kySENOaHdqWWNJMkhDTmh3amFjSTJIQ05od2pZY0kySENJOVBDOHpvd3ZRaXZSUEk3TkRlOW4yTGMrT3FneFZiaGE2bExJbU5mTXlrK3JiL2N2ekpvMzBIckcvaVVSRlMrY3YvL0VBQ3NSQUFJQkFnVUNCUVVCQVFBQUFBQUFBQUVSQUNFeFFWRmgwZkVRY1NDQmthSHdNRUJnc2NIaFVQL2FBQWdCQWdFQlB4RHcweUhQWkFyM3dnTDk3akFVcFN2OC9DUWlPQmlYdk1JT3RLcXVkWWRoQUp5L0VnblhvUFFVQ0NCS1JRa2ZScTZpaFFvS2FsUEFGQkVJUUJ4SW9vVUtFVUFnaUFJVUxvRE5FSW9zaWdKU2tLWDFRaUVmdy9GZFVmcmd1dlJLaCt1d2FqcXYrS3VRVXFJcXNRSytBQWFWZ0JNQWkyUi96NmZlQTZCL1duVW9TT01uVUdpaktFRUFuSEh5Z2tDd1IzdjFBQmNvSWtaaHhOZzJyRGNLTG9DQVhLaUNncm5sMU5BWTh3VEhGZ2czUkpaY3RxQW1rSTRQK0thaWJ4QXR6S0tNV1I4YldoTHhWbHZLb2wwdnFKS3VzQ2Y1RkN3aUNzOWY3MDBoUlpDUC9jSmdnS3pFT0FtekFJc2ZGamhCaVVPbE5BZTRjSkVBRnlvTUVvWTVaU2ZoSUF1UEU2bENLS3crbFI4bytVZktQbEh5ajVSOHAyeXJDRFRMVEVmS1BsSHlqNVR0blpBNVNsT1RnSFF4OG8yVWZLTmxPMlBsQVdIOXVZQUFVb2NRT0VFa2ZvQUFJTXd4ZUFBSnNJem9JNUdhSGhBQ3NNdGFpVkxyS0VaRnVsclRVbXBDYkdQbkd1VEh6Z29FM210TmFhazFwV1RqWndFUUlNQWhyVFVtdEdkNXJRa2NZU1NjQVE2R25oUlRsN2REVHhzeWdoQTl2bVBoWWhFcFF3WkZVWStpbkM3cEZScWdHbUpvelFtbkNiQ2FFT1QwZ010UXdTZWl5aCtGUnA5WjRBQXg0bDRGRWtZQVFoTllGd3dtSUNrOVlJRmpHRUs0eGFrQkxEMFp3Z3RCU0s1QTkvb1B3Q2RLc0lzSW9Pd21uQkJJUkNFQ0V5c3FYZ09lUE5IZ01iWGhPYVBOTlNQTkFoUmxLNWhkQkNSS01xVlZTaENnc0VGRUpSUkNUVmhaSkp3Ri93QVI5b0FMbDhFSXY4UjBmMmdDSU9SamYyZGNhYVVJaUU4V2F3aFF3RU5LTUNBQmdnVlM1dUtIQ1V4S2dpUUVzMlFoVVZxU3lnV0V3UVFHUlppNzFORmdHUU1ZRWh5Q3JVNUU2YWxZNVFsSEFLbzlvUEl2OFJBeENJUkRLSVdpRVRLSVJDSVJBeENJWlJBUkNXak1aNlhpRVFpWlJNb21VVEtXK3lhOUJDdm4wQklzWWRzQWh5SGg5bW5vMGYxRm9JV0g2aU1FVFN1S1Z4OHZPQWlCZ1NxQ3NCVkh1K0dQQjRCbzFVRTNKVkNCVWhGNEZtZ2s0R3RGcjNHMUtVTlVuSEcwVW9Rdm5yTTRlclNMVUd4L2ZhRTBncmdLZ1lwUTVvQW9xaXdXRW9zUEFJNDJnZDRCRTRFRFVDaGVyTllkTkRTNkJ1SGxoYUZvTjZ3VVF4N1JidlYzaHhQbnZHVUNFMEJlSkZHYS91RFN0ZTNuL2tJQ05ZakVZakVZakVZakVZakVZakVZakVZakVZakVZakVZakVZakVZakVZakVZcFdBYzFNZk9EcFdqUUNIVndmWm5IaGd1QTVwaUZBcEtPSW5MaUY3RWFaUW9FQk9OTFYwL1NsbFYwYUcraENpUVVBQTlONGdvZzhzMkJZV3REVVNFQzdKUVlqY3JsVEdCZ0RDdHNsM0JBV0VDN1ZIV0V4cXdLeFFkeFUyUkZhdzVkRHgvV1B6Q01ncldlcmY4aG5DZ2xZM3VZUW91OE5ZVEFRUXhIQUVjRENMTXBwYmFKRWlSSWtTSkVpUklrU0pFaVJJa1NKRWlSSWtTSkVpUklrU0xHRWtHZkxxWnpnSVpYQjljZ2kvZ05qSUg5UndIb1BRUSt3RnF2MjlZR0FLOEZvTGxHUkpFanVRYkZtZ0pKWkpGNm1DeUNUQW5YT0ZqVEEwS2FkWmJRR29vR2VPcFIxcTZKdFJjR29kZTRGUmMzSlJKWUVMc0lCbGdmRXYxQThZcUQwckRLRFAzancvNFFJUkI3ZlBxNExRSHo1cERtdUQ2NFNCR1hnOWlmMVBaRDlkQmpGUjk3UzJFRUl1anNYeFFYZUV3cGtyM3FnUm1DY2NLdWtRSWdNRGRWcWEwRks0aWxLaUFVS0FWenYycEh1M3dRd25rN3cxLzVRZkJBcXdPdm4vT3JsRkFmUG1rTUxnK3lKYVp0SyswZjFMWFcwSXpRV2QxZzZMR3VJZ0UyQnh1SFUySHBTRUZCaXJiOG9wUE52M1gyYW1DVWppTmFKWlV6Z2tBUUNIZ1NCbURicVRiU1Vsb0t2OEFGQUlLTmZaRWZ1QkNDR3RrWDBIOGdBeFpMRW9oRzRDOVBkVkFXaWdaSEZaVnpBdWw3ZTFSR0tUbVdzVjcrM1JSUlFpTHdMcGFDQnZoOHRBcUxZUHRCOXFTWUZLa0I1UVRQYnFlSW9ENTgwaHhjSFVwU0xqMEFZUkVBd0FiZlJBU29HSDludEQrcGE2WEtFQWtZQzdUd21pckFRTkNiSHl5aE90T2pGU0VqVVhZd1hnUWdraVdJRU9LVnlHSzVsdUdobEJGQWRTWllBRWVad3BDeGNKQVh3VHFEM2dFd1FBOUl4R0l4R0l4R0l4R0l4SEdJU0lBQkJoZ2dUTVlBYUJJQW9Fc0NRZklZMWdqVlNyVzV6L3lBd1JzSkR2a29CWDJnR3dpaERXWVg5bDY5SGhCb1BuelNHRjBlQmRWRUlBUHBld1A2alNZWUVSUkMrSmxTcVl5TGhMZ1RmOEE1NWhLSUFBSkNwNjlXUUpDQVFML0FLeXFCbFF2d2UwUDZnQUFNTHBWQVBubGZDWERCUWtVV2JOR3gzcXJHRFNXQXlMRWo1a3BZQmtER0dicGR3b01IZkJxcnloT0lmVVdnc0NnUC9OTG1GQmQvUzgxNXc5SFRJZlBuYUdBditzQ1JVZUVRUUJnWWRNSXdwM21Dd0F2aTJUK2xFVFdJd0ZRQVNzckt3RXdsY0lpQTRqbEVjcFdCbTNTc1I2S0xvajBSaU1Sd2lQUzRmUkdJeEdJeEdJeEdJeEdJeEdJeEdJeEdJeEdJd1RIQUFCbW80S3k5K3JNRWdMd1FJWC9BR2R2dkRVeG8wQ0Z3QUVISjdid1JLb1krdW1zZEpDL3d0QXJDa0FmNmJYTjRUaE5LSUFkbWUwTTZnc1hZZG43d29qSmNNTWVtYm1VUlNobWhMRU1zZG1GUWEzZ2dnTVN5bW9CUVROUlpsWndwRWdSeWExUVhBYmdFYXBvb01ra0J3eURTNFdVUkFHNE5qZWdRRHVhdU5uYVJSNERHRmdOSnFzTC9vMXdoNEFsSGxYQ0hJSFVhWTVyUEVTamVpVUNybWhYb2ZlWWpla3JIb1UxaWlWNkF3OWdYU3Vrd096Rk5SbUlXT0NCQjBvQUpPd0JCRi9PR3dxSUs4OG9nc0RncUdJMEp4QkhxRkVnb0RhQ21JRDdzemFqVk42NDJqWlRHM3d3QjBrQUxSNGtaT1AyZFBLVUxDODhFeDZQUVNFRm1QN044UmRLYUUwNXB6UW1sTkthVTBwcFFLQ1ZBVkFKUkFnZW9VeFVObGxnaklCc1F3cVhBZ0tCV1RXbzBKT0lJbGdnMEpDUklqeEVBTk1FQVhpMHlMd29NQ0RVRUJjVDNENFlFUkFPc1pNUUovUjBNUUFxUVFRQXA1ZnMxaHNxQ1A1MmhLQ2F0Y3lRTUJiSUFyY21wTEpNSUE3RVFQQUVIV21Ga3JvVk1QVUlMTlZHVk5hRVhjSXFLZ1Z1TndDNEltbFJnYVFJTk9xSVlzUURQbWgySVlVQ2dpQ0FFS1dBQWFUb0JTbzBjUnRoazJBcWFtdDE1bUFSU3I4M2lNSVJBRjM5dThJeW9ES3dRQVFDSUc0bTJXdVV2aFVNQUF2REx2VDlRT0VhakR2ODk0UWlnaFlxRkFSUU9hRTBKb1RRbWhOQ2FIUTBKb1RRbWhOQ0hybENPUmFqWHROUG9BMGFFTE83ck9oTkh4b1FhSFIwSUFaOEpNRWVnTE9VaWdnbkl3OTR4bkhrWlRPWXdtY1RPSm5FemlaeE00bWNUT0puRXppWnhNNG1jVE9KbkV6aVp4TTRtY1RPSm5FemlaeE00bWNUT0puRXppWnhNNG1jVE9KbkV6aVp4TTRtY1RPSm5FemlaeE00bWNUT0puRXppWnhNNG1jVE9KbkV6aVp4TTRtY1RPSm5NeENvZ1pTQ0tWNEJvTEg4MWlGVTRKSkFYaEtvckZrSDVYOEVaTitwSUt4Q1ZXQUhhUFF0UmM4eklPQjg3eC9nb1NJdU9NSVlVQXJROEZERmlhdEpmZ0lBRFJPTUtrMGtYVXNWOURTdFQ5SjE4alU1RDhCWGdHQWxJbGJ3dnRpcHlIbCtDdUpxR2hYaEpVQUwvYjhBRUFhU2gxaW94Sk9sZXk4QWlaRG5sNGJZQXU5dndRQkRkaUdGVjI5b0JvZElJSWJDSCsrRnJBeCszNEk0SkJCbktHUUZqNFRUV1AyL0JhVU44NGNoNGVFMHdLL2I4RWFreklDQnd1KzBXbC9DWU1DRWdBUXNTY2RQd1FLaStZK01wQW9ERmE2NmEvZzc4Qmdna2dBUWRUbnArdjhBdnVLQWcwejlSSm9sb1JZZDhoMEE2QWcwSWNKZ3g2YUZZSmlnQVNGamplSFJnam9KWEJ3QVVBQzFYTmN5ZHhid29hWTJ4TllJQW9FVVNvS0dJZEtrN1ZnOElCYnV3MFVxQmVHT29RMmhISzFjZTBCRW9nVFlHMFJiRDN1R0t5aCtTTkdXVlp4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphY2MybkhOcHh6YWNjMm5ITnB4emFjYzJuSE5weHphQUV3amd5N1MxUEJhRWdYaFpnSE1UbkU1d3prRzhxdTROdkNWRWExVTVKTUkvR3FFcUE5Y3hJaTlVUCtrM2dxdjFrR0o2ejVhWUI5WkF5cSs2S0ZYZDBPSDY2SnY2emVja2p3UDNKU0g3MGNDdzhYbkYzOVpLRmZXYnd2OEE3TjV6amVjNDNuUE41emplYzQzbk9ONXpqZWM0M25PTjRYUm5tZDRSRkgxanZPY2J6bkc4NXh2T2Niem5HODV4dk9jYnpuRzg1NXZPZWJ6bkc4NXh2T2Niem5HODV4dk9jYnpubTg1eHZPY2J6bkc4NXh2T2Niem5HODV4dk9jYnpuRzg1eHZPZWJ6bkc4NXh2T2Niem5HOEo2SDFqNGh2bFJXR0hncUU5UVhwV0FJQWRRVVhDVFlsaFhldzhRVjdBZktlSXFKTTRoTVZHZWNFVEgxR1VoeHk5eERESXNqSFA4eC8vOFFBS2hBQkFRQUNBUU1EQkFJREFRRUJBQUFBQVJFQUlURkJVV0Z4a2ZFUWdhSHdJTEZBVU1IUk1PSC8yZ0FJQVFFQUFUOFFoRndsUnFnMnhJdHJlTnlydUpvU1JSS2dOYlFydEFZc1VieGtIZEJoYzZOcSt5R0lEVFlHVlN0ak1hNlJVTGxFV29td1hVanlmMGs0SVVSd05VUUI4b2htMS9IQ05teFFUdjdobDRoZFppZE96RHBnZ3I1cVpvWENCNHdSQW5Gd3dlaUJ2dmdzbUZZNWZZR0xyVWtWU00veHFGbHBRZkNWbUFsZ0dZYXNIOHJvanJSVUtFREhUKzkrVWFBMUl0QW9TVU9hZzJES0tTZzFJQkVEbXhBTTJkQXRwVUwvQUpVd0lwcEE5QXJ2K25GTjVYclZqVFdQUU54YTJpMnVDSk9ta1JPUnlOcTNoYVhvRFY3RGhOc0RRb3VacElNMWFxTTB3NU90c2NFa1BhNllyYThyK1NFVWowSVYvaHFlcDlEVnNEZ2RDdTVZV0d6MS92YzVCa0NiSm81LzdmZHdBZ1picm1rL0hXV2YrK0FqU1F3VG1FUEthOWQ0WEpQQnJTSzNXdkxtb1ZVb0RvS2ZQT0lsWjNYS3V0WGFxTjV6dHV5ajVqaHltU1hsWGJIejJIejJIejJIejJIeTJIeStIejJIeTJIeTJIeTJIeTJIeStIeTJIeTJIeStIeStIeXVIejJIeTJIeStIeStIeStHaUd0RG85L1llMldqVkpORTNyM3Z2Z0FobkYrSjA0RFlWMHh3UUszS2h4ODloOHRoOGxoOHRoOHRoOHRoOHRoODloOHRoOHRoOHZoOHZoODloOHRoOHZoOC9oOHRoOHZoOHRoOHRoODloODloODloODloODloODlnaFFIdmNLdTVic1pXYTA5Y1Buc05yZG91Z3BRZmUrK051TzloeXhLdXpyRjBtWEt1MkdZQldOM1FDUkVSTHNUZjA5ZWV2UFhucnoxNTY4OWVldlBYbnJ6MTU2ODllZXY4NEFXQWNTQnRTYldhQWpDcnRvMXBLRUhFTkVzNWM5NFhGRkxLRVNDT0NnWjVicmdxRnJZQlJvNk5MN1FTb0VhQnJuWnhzWU1jSlFYTG05cmJBcXRFWWpacWhlNTZzTkFvaVFRZ2hCRVVackFGemZINDJHS2gwZzJTcFJDTU9UZ05DNEVEMmdqQWlvY0oxb0V3RDlqZzZsUTdFUU9TUXVFalE4aWdpSGhqZzZaYW5UTzRtc1dBalBDWWlVYW1vSVMxV0FFT0ZVZFVnSkRTTkowamlhQlJVVUV0NXNFREFuVWh3emxIb0RhQk9nUnR1K09Vd2tLRVVoRGlzdzNXTGFTY1FvWWtRTWdLU0JXaG1URkhZYk8xR2xjSGFMNXRxU3FCamswVVRDcDZqMG9MQkhhQUNyMExCYzZiMDRRSUVMVVVKTWlUUWNMVFpSMW5pSWhzMi85Q0RRY28xWExwT3dnU0ZvYmNzcXhhQVVkR0plbFVJeGFxT0JqZEJKMGNjRTRsWXEwVkppTVZOUTA0SUpHaWxrRkc0OUdEOUdFTHV3VkV1cmpiYVErMEtXRFFiV0FGZ3R4Y092WUtJRHBIQkJFeHRaTWNEQnNoRmFMb20xRmExUlFMcTFHSkE2TEJzeWJ3TVQ2Q3FPRzlLa2hvZUpKVmwzS2FoUWoyaGhIU3dPSlhRdkhGeGdTeFF3TFNKc0JzS0c0VGJuaVowWnFFZHJBYzVNaE5idERSVG9vdnZvd0lLUlpRS3d0R3BVSUVWVkJ5V09tS0JVaDBHcW94c0Y0Mkk2SzBhQU5obFBuVWdVVnRzMm9wcVo2ODllZXZQWG5yejE1Njg5ZWV2UFhucnoxNG03VHdJRytjaFdJeUNtSGs2LzAvVnJWdUVMS0NBSHBXZzRSREZSZzVHZGNXcm1oQXFRQlFlZDAzaEZXWlNvS0lJaUU3bUlGSUVFdFJBQ3NjMmdvVnhUcllvSENuWGtEMXhocFF5N0FVaVFFVVZDZ3c2Z3RlYzEyZ0ZGU0IxbS9OYkd4VDJpTyticXhqQUtsZEFzVGttOVlET2c1QXBNdW9JRVdJT0VrZXFpQXBSS284SUp3OFlNWFRaRHlvTm1WZ0FoSEZPbkRDRHlSRkdKQkVEeHNyUzFFblhaSjY1ckMxRGpnQTBzY1hTRng2ZCtBQXJieWJNcEhuVGttVWJlWXJBTjJrTlZvaHIyTS9td0lRZEJYZ0YxZzRFR3N1T3lsQVJpQkFrSXgwYUVtdUhlb3poYU9MS1MxYURRZ2h5cUNoelpIRXl4eVV5MFFsMUdyU1FsNXczc3pUYlZib2VSQVdjWEdwVUVtZ0tBN09LamVibGMxMkFaV1ExMEFCQkM0Vm1na3NlQUd2Qzd3NUJnOG9tUlZRczBBMk5YQkFxaEZOQzF2TWVkbDJ4T3FiTVNUdTZGaHRtc0piTDJBRlJRMkVWSUpBNkJyMXp4Zm5QQitjOFg1enhmblBGK2M4WDV6eGZuUEIrYzhINXp4Zm5QRitjOFg1enhmblBGK2M4SDV6eGZuUEYrYzhYNXp3Zm5QRitjOFg1enhmblBGK2M4WDV6eGZuUEYrYzhINXp3Zm5QQitjOEg1endmblBCK2M4SDV6d2ZuUEIrYzhINXp3Zm5QQitjOFg1eEVqbTlmTDZqd2l4YmhqZHk2TmpyV0htV1FrQzVzd0sxZ2IxblhuT3BraHBXSFQ1Y29SNGRrTFoxOWNWeHVxa1BOT0ZJcElvbGJxa0tBS1FKQWFBR2dMN1EwZ1dodWdLMUJnandVNXpkMnBlZDZVVUVUQXRMQllzbEFSUkZoRUtOai9OUUxWZ0FBU0JnQTdKdEFreGpXRURVVm9ORk5pZFhlTlcwcFlRQUxXUmNyWXdYUzNnU1NKcDdSQ1Q3MEluYXRRTjlhUmtDZ0FNU0VQS2sxM1k0VWpXcHRTeFdoMFFETGZBRElRQ2lRVTBSU2w5Q0ZXSzBiNmNFbDZNQUVaVWQrVEptaEtrUlFCYWNRMnhzVUVDZk1SYldCQ1BKcWxOazhVWGJTaVRkRkZGNFlpazBvM0Q4QVB0bENHTXRSQVFrMHRTZ1FCa3VsanZ0ZVlVWk1jMkZCMFhadnBhZXNlUVFGcFNtMnREZ2JpQnJSTFl1Qk5HVXhSTThSd3FYcGliU0N1TnJrM0UyeGd5alAyU2tQaEhWY3hRMExodG1sMlJ6cTNPRmp3SlJiTWdBTUd2STVBR2dQSDZjVWtHN3g5RGgvSWxsU1E1ZEhXeUlvT25KL29ieDRjT0hEaHc0Y09IRGh3NGNPSERodzRjT0hEaHc0Y09IRGh3NEdOdUFqdXJKa1NtQ2tORHBOdnEwTWNvMDQwMjlyMU9tU3VXVURxR1R5K1RlOGFzc1VkYmxBRmJWS2FUQlJNSk5DRmJYUkpCVFRHaUNCTEdHdkY0dTV6Z01QaW9BQndEKyt1REZNaEExZlpSM056U1VrQVNpQnBFWTNwekdUQWRocVVkM2NDNjdlTlRXTFR3RkRXaWFVMlhleTZ3NHVhd3VpczBjMWwxMU5ZMElvSnNvNE5oR3B0MGhQVmlRNnR3SVR4VlExbzA2eFNEdmRoVlJIYnRYV21hbDZaNDBwQTVOQ1d1bFNrSWdnY3A2cDRMMXZwemplVHVwL1RFWUlETmtwVkNBcnBwclM0cHAzRW1aU2cyUjA2TkZNcFRSV21UVUxDUmVDd1FFdE1CRUZwdEdLQWdGeEZkQ1lSQ1d0b2VEWFUycG5FZzdKMHphYmRlZFlJVGVoU1hCN0owVy90TURqQUttVUI0YUxYcnpZNEFIWlNpdzBCMGVpd01BQzhSSUZSQVVPQlFYcUdPNEpXVFNGQUlXQVVwTUdJRytsTFZhU00xSUttSUxveGdJaGd0cElOQUJyRFNnQVFETHFFRFdtdVhFbFhFRG9GNkRWaDF2ZmpEMFM1bExvRkZPZzhiNlhqTVNvdHhBMDA4SHBuR0VYT1Mzb0JwYVRaaU9rYUVsQllWTHBvT1RGMGxFQ1BJMGlobXJ4Z0hNZ2x0aGxVUUNBZE9vLzBCSGU2QUdCb3BydVpyblI5YjBWMFRvclYwMzYwVktXZ2xTbTNUV0tvWFN3WU93MEFON2RzUFdFNEdDRHRVSythT0laeU1COEcya2c1bjN4QUdBaURBakdCdWNtamtYWC9HOUt1MDB1aGtQSlZTMndjS2dObWhsWUIwbVFSdGFMZU1BUXdZZHVsNEtRdHU5VEdEQXF6a0JTYVZlS2JyQWt3RUxLdDJhSWlncEV2SUt2RTBneEFVZWcxM2ZHRU9WM0JOQ29LaGg0Y3NKOUNzQlNGVHBxd0JWZ3NrQWZoVHNLQnZwY0tnR0M1V1V0S2l3a1FTT0FpSXN5Q0x4UmdWUXpxSzFYcnBybkdsMTBUWURYOFVVU0o2d0ZUcVlWZmNzNXBKV2tHZ1VUWUxmT1F6eEdDeVJTYlpiUU9iRWdBbWc2ZERMR1RyclJLMGpIOGpueWZQa3VmSmMrVDU4M3o1TG55WFBrdWZKYytTNThsejV2bnlYUGt1Zk44K1M1OGx6NUxueVhQa3VmSmMrUzU4bHo1TG55WFBrdWZKYytTNThsejVMbnlYUGt1ZkpjK1M1OGx6NUxueVhQa3VmSmMrUzU4bHo1TG55WFBrdWZKYytTNThsejVMbnlYRVJwdWdmcUVBTm9iZHk2NXVGRW1BTEY4ODNMMGRoaE9BSXE5RGVSNFVsUlVyZU4xZkdUSWtVMnkwNDgrZGM4NE9jR1VEV0U4Mm11dWRTa09vN1FlTzcyM2dzcTV5Q0tsNllpYjZpZDhMeW8yZ0FkMWFOYTdvQ2tTODJaMmtnTzc5ODV2N0ZEaFhiMmkrZzV4NGJRVWhoaTg3OHNXQXdyYVRSQmRtK3hnNjVRVHpPQ00yYjU4TWRaYVRydm1pTlFjOEk3cExoaEEwREtRWVNnNnRPY2pFOHJpd1lOMjhHN3hnZ0djcSt0dkV4V3JVRU5uVHRuTTlXNUZvUlQyQXhSKzEzeUtsak53d1h0QWtCRkJCQjhZOEJJS0NpVkJvU0lIeGdrOUlwYUJJZE5lR1VVUlRRYUZFQmt1enlNQUNxSnd2WXVaU2dMa2ZvZmZGMXl3a1hxb0FFMnJnY2xHS0lXUFJHMU5GSm04UzBSQXhSU2hTK1RBUkNOUTVTSUoxaVU3anZuOVdDaHBOOWg5RWNkNmNVSHVQRjc0bTJWK29JT3FqeFUzanpGY2hjam9VVmhwelIzcURsVVVKMXB1ZHNCcFNxZ0hWWHB4elo1a2lFZU9ZeFBwZGw5TXRVRmd1Z1VvVUdjWlg3YklRQjI1b1IyYzFjMENCZ0JwV0NXMHlHd1JoQlFwM0VPVzhaYjk3SzhsRG1iTjQzS2VyRHNiZS9BOWYzRFNOSkx6K01aenJPQ0lMTjFnK0xhUkFDUktnRmRBdzRMVS9idlVTaVJvN0kwSm02eWx5RkYwQ0l4YnJEVEYxT3pPZHBlTHMwNEVNV1lxS1lqZG9oM21zVGJPMFJUa3BsQ2hoQ2RtOTh2UjlzdE9NT3NVVklKczhEMmNZa1kvUVEyVzNEVVRDcmplZHBtaUNnYnZYZkJXcVNvd0VwNE9mTnNmTnNmTnNmTnNmTnNmTnNmTnNmTnNmTnNmTnNmTnNEU29GTUlEUzdBcEx3TU5uWHEwUmRkWkJnVmVseEg2RnV0Z2RmQjZOeHRPN2hOTG11L2I2dmlnSWgwdXVRRTZLYjRZcVY4YXNtVUVENkw2dWJnV0lCcE9ScWdxQWhYamhlVm1DMU9oZ0RSM05RUVBaVVVleTdXNERnYkJJaXdBa1R3MEVzbGtyZGh3bEJYc05BdTJtamNFZGRqTVczdHR0S214dExuU2UzQlZhbFd2TGJ2Q3I3c0NhZFc3ZTA3WTVxNDQ5T0ZuUTdPekUyR0EwZGxXZEsyK3JOZy9DSndIK21XdVV4V0JLQ25EMXlLVGZLdE1JSVVDT0FOWUhQYnlHSkhjcUhYRHU1cnVsT1ZTQVZCUUE3QWF5QlZWUnRrcE5WVGJNazB5cEF0S3VZdjN1TTNxcHFTZ25CaVAzeGNQMklGR0RLSUY2V0lXZG1TVW1IWXJTbzdPQ29aL2JqL25BZlVlaDRIT2JPZ3NWYkoxSFFXVWJ5MEo1RVJ4T0QzcjF3WFp3SnZBVTBUbzlNZUs2RGlsLzd5OHpwN0FFZEhmWmpUU0hxTFNJZ3NnQWlZSXdpZ2FBRHFzdXdoTjJqZTBJNkpjVmVXOEFEV1UwbVExZzNKdmRPVVV3QjlsT1FTSVZYRkFFQ1lrcEVKYXdOc3BSa0dHeEpPdXRSYVFJcGNvUmpjZ3BvMDFiNzByZXlNTmRWVWprVG9sSFBURXNFT0xiQTRGcUd0NVZ3WWFqdTJuMDZkVTRxdGQ3d3p6SlZoWWhDbmNnNEFsc2pMVHRaQ01zTEFidW04QkdBcVdVUzBXcXdnNHdRakJrRm9BUkJvY2FJOHlFSVJlcWpuVkZwTGpqaDN0RlRvbEFlblFtSUhTekE2RzVxZ0Rxb2RjTnQ5Skpha05BRVNwc0FOR2dJRkRBN1h6a2lOaGZiRWxTb1NoSUcwbTJBSkNGNklnQW5UbEZkVWdqQWJ6WUpRdDlzYlZwdDd5RTFaNUIwQUFBQWNibGFxdGlZTkRuTDJwUVozQjVEQUpRNmhxQnlCbjNPZTJCNXdCWUFrWndsSENHOGZ1NVNrWUo0UUJPQmhna09nSUJBUEVKZ21QR0FaYURlNS9Qc09SbG9OTHBNaUtnT1UwL1NQaTdBcFJ2OFk4ZVBIcy9XMFZ1bHcxSUwySGJEM0FCaHBKT21ZVUFRWkszVWh5Nk4wV3F1RlVrTUJRRktqU2dST2QvVkFkd3F6cVNHNWhPRUFxT0FEems3K2pNUVIyT3hCb0J0Z1JCRnd6bGNBazFEQ0JZbTFtaUV0bGZTUXVVallsMUFra2V3U0lOMjAxdXNvbE9lTUxEMFRJcEVLN1VheHYzTGV6SW9tWUZVS0dPc0JLdzJQUFNwdWFRcXlFOGVpOTdDNVNuMTBUYkdvSkJwUW5DQksyd3E3TTg3RjVHSWpVb2t1OUM2YjN6WGZYUEorbWJEbytiQ0ZFcXBnQ0tXSTd3WUpWN0l3U0NHcWtXTDNoSjZjS0xrYXN3WHVITUNjWVlJckNuRUhWb1RZbUNJbWhPRW9FT0FCcDJSU1FZOEJYOU5TbXFXTFNnUGxKQXB1elNZaDJkMmtqYTlhYUVBYWswY0FYYWsvUVdaa3dJNXU3WFRybnBoZVZpdDBTcVZlVG5uWnA5YmlFQ0xQUWtJQTNPUUhNQ3VCRzlJUklIWWlhRHh3UXRLbzRRSXgzY0FGUmNCRzZQSU5neGc0SXBIWnBpTUZDRlNrWWpkYlFJa3hzR2tVWWk4MmxkQXdMbXVHVTIwVkpRNzdDUU1LR0ZNN1NRUkhZcFBKVEYxR0tTbWhJMmdzVUtKb2x4ZWZtQmJiNHpQeUNIQmhUYlNjZEcrK2FlRDdQL0FIUGhHSXBvVkxiQ3lVQ2NOdGhBR3o2Z3h3TnVzcmt1Qm1pSVlKaGdBK1pKVWhrd0JzQ0tISXdHdFNYa29PbUNZUldHRUJoeXE3aDhHSkFwcjdFQWtOa3VORFhIdEFhWXJHdzBkS2hVQmFrMVBQVFQ3WjhJL3dEY25iWENvVzN5NTZieFRyaVpvNTF3VURHK2RzSWh3VHhXTURTaHh1YjVjSUJJRzc3aUZ2bkFuU0xJS1JlZ0kwMG1pUndURnRHV2swZEMwM3hvV1pTT0N4V3FWR2xOZzN0RE9lSDhoVFlkSXZBWFhYTlJWUzJFaVR3UExzWG1DRFBubTlRU0l5cHRJNXlFc2dtaXZTTkQxUEdEd1l4Q3dBbmNvYVhZUUQ3bWZjejdtZmN6N21mY3h4S0UyYWZjeHR2NjRpWDgwWEk1cjZ6OGViaXBxRUI3a1RaeHIvRFBqWFhSMUJDcGFHb3BRSUREMkRvMXBEYWg4Z1FHR0VoRzdNQVVyWVFiTk40UUJDN1VRRU9oa1dSd3p5M01TOEFBaGVLZUZTdXNVQUlSQ29xT0UwQldKNEF3S0pJdDNpSytrVTFVMFVScUIyaHJOQnA3QUZvSUNsVVlBNzQrcGlTa29OUEVhTzJiTzNvckFjTEE4Q0JTWXFMcExYY1ZBa1RWZWhod2xha2l3ekJCM3ZUUnVCSEZkUm9nNFBWUEJxT0pxNmoxWVlvQXVDMmREZExkT0w2Tk5FSHBhSjBaS3ZSSGF5Q0dCVGtha0xjRVJBcUpDRmkxTFJBYmZZNk1tNDdGREk2blNzTFpyanF3czFMNWJvbTFNUThUQUpBQVY2aDBsRlVJT3hFb05NT0k2ZHhSUUhHRVdvTzRLUk43V3NYdEtnMWlpTWdnVkYxcHd3UFNWOENSc0hLUkhaUmFJd3BqTWdyazM1MkJVbStnTDRBdHpCa0JkdVNKS1dMN0JsR0ZXYkZMdklrS1JBaFFGUUVPTVFWaVNMN0ZzMVFteGJ3RWxvZXFab1Z1bkkwUU5zU002VUFtMEZwbGFhcWNVaU95KzRTUVFyMU9uQ2Yrdkt4b1Z3RVhnMHVPOEx3NEl3R0JJS0dnMGNaOGRueDJmSFo4ZG54MmZIWjRIdG5pZTJlQjdaNG50bmllMmVKN1o0bnRuaWUyZUo3WjRudG5pZTJlQjdaeDY1aUdtZmNuL3dDWXU5WW9WMGl3eFRjVHAyd3BxMlFFRmVuVkFYY040TTFSWlhFRW1BSEpndUtkcmdXWUpBWEJCVllOdlh2SldSa2tFYXBGUUZ6SnNnRnJMQmdGUXFqSlFheEFDQ0hTbEV0TS9UUCs1K21mOXo5cy93QzUrbWY5ejlNLzduNlovd0J6OU0vN242Wi8zR2dydjlONGFaUFFJZk95YnhXWDUzaEkrQUFETmEvUmpHSnFrVzBIRmQ5SFFZNWNrS0VsdHArOXhCNXpSR2JpQThSaE9GZ0RITGhFVE9JT1JiSFdHUE1BR295Z0NnTUR0ZUd3SFVqUVZrUVZobzd5ZkpFUVVFNUZTSWdqN1IyeVIwQWswVWhnT2tpZzI4YUlhQzdib0EzQi9EZXcwd1NuSFhqQXNRS3NGWG44c3RYc0wvV0E1ZDR2dHRaUG1mVS84ZmpHd2hEazF2cnJDeHVQaG40dUNCdHROazlNTVFGUEpjT3hNOUcvaTREYXAwR2ZvMW5lcm5sLzhZcWdaek4vcnJOdDlsLzZ1RWt1dkR3NGd3eDBqV3RqdzVCKzJiOUd1R2ZxMWtKdU40WTkrUE9jWFo2ay9wK01WQ0ZPWjdyV0RDRWlKbS9pNEN4QlhldjhmM21tRFR5WCttS0trSFRsL0dGaXFQVkUvRnd0VHZUWk1FODhFQU9ieEo3WnpyUGxyLzNBN1V2RlRYNC92T0Q5NEwvVXhkR001Qk4vakEyMUh1SVlBSlplS0tmakZBQWs1Uksvak5mUjh0T0J1emVCVFg0emc3dkl2OVRIWG9uTVRmcnJHUFVtc2dvTFplaDF3SVJaZUZUWDR4UUVrNkVyK01YREVPaDAvd0RjTHFxUEFwRDhmM2pRYnZNcCtKbWxtSXdDMG9MdldzNHhHRHI0WElXclFYeExIYjByNzVkYUo5Q1RTbXh5aWl3cmlzWmdKUUhZcUJYRXFBR2JxQ0VRRkVVQUVHME40L01NaHhZQzBWUURjSDVUOEJpWGRhb3Jpc1pDVHlQR2xUdzNqYlpPczFuUVV0STVJcVRZYnNCZlRSVkJXS0F4WVdLNDR2b1VocWtGYVJ5QWJIWXFTcExVMERFenk4SGw0UEx3ZVhnR1VkTUZxR3BRRnZhelRtVmNkeU1pSUFCOVk0ZXVLUXdSRFFiTGlueFA1czRhZHBJaVpWai9BTXFHU1lLcUFLTG4wMG9tRElleE1vckVoVkczUmloS2hJNENJTWdCUlVsaXRQQnB2YThSU2hYVThpNWJieVdLcUZFUUFEemJFUUltRjFCY01xZ1MxSnM4bmhWVllLZ0FGR2pSQUhGSlNaTjRXSE1xUVMyWWs1amUzMTVRWlMxTTBYYUl1Vmk3UlhFWE5DY1FURWF3eUVVb3FpNG51VFFSc0JxRWJhME9tSi9FaXBkN1pBNjdReEZlZ05LME05UTM1ZU9Bc2l1c3FCMkxvSzBoZWkwbW1FRmFtSU9nR295Rk43K2Vxd05nMHVWZHBGdzNCMGpJRGhKVUVDZ1dVemZuS3NsNEYwRHRFaUNNdmc2cUZNRUdSSUFnS3BVOFQzenhQZlBFOTg4VDN6eFBmUEU5ODhUM3p4UGZQRTk4OFQzenhQZlBFOTg4VDN6eFBmUEU5ODhUM3p4UGZQRTk4OFQzenhQZlBFOTg4VDN6eFBmT1pIcVkrSjc1NG52bmllK2VKNzR5ZFBmQklZd1o1NURzbzYyYVYzNmN6cGZjY1J3aXVnRVdPVU1KVU9TSlFYT2VqbExwZ1Z3elVIREh1d0FDbzQ0c3lXeENjUEZDb1cweERJcVgxbEJ4RGVOQlFGSmpGZ1FGcERab0RlR1JVQlFpY3NGNVIxQWR0OVRucWNDZnhlTVZNR1BFdVlaS1RrQks2RkVXeWxGOUFza0YwNHJUa0FJcU1FVEphR3o0OE1SblcwcWlwU1NVd3M0Rno4by9yR3EzQU9RZGN4eHM0Y2o5WmlKMG5BbGFwV0srRld3dHRNaEJ5V3h4TE10V21KaHpWcG80c0l1UUdxVHEwT3lFYklSV1hCYUMwdlJJVGtteWpKUkJWRHFaWTI3TUxRbGduUkdBL0dsMEhDdGxtQ2NSRXZTa2lPSytTQnlneDZXQkFJc2J1WWdDWXNvb3F3RHVWV215MUx5cVJwQUEyS3BzTzYzYXpHQVh0cDZFeE1pUmNGQXRtN3dBRFRGSU1FRXUwa1ZnQzByUVNKSy9pMmQ0Q3RXSEVrMDRsV2Z5NDhpUXZTSmlSdnRheEtjd2VDMzk3bjczUDN1ZnZjL2U1Kzl6OTduNzNQM3VmdmMvZTUrOXo5N243M1AzdWZ2Yy9lNSs5ejk3bjczUDN1ZnZjL2U1Kzl6elhCL2U1Kzl6OTduNzNHZi9BTzRTRGlhVTlNYXZMaWZXekYva1RpanBEZHA2V01SeG91Y0VxYXJJVnM0RndHb3FMUGFJbnNBZ2RwdFlVTk5YZWlWUmExcm5USGxOL3UzM3dLUXBKcnR4Z0FRMGYvQjR5TDFtd1liK2ZUSFdJYXZBN1ExcGVxT2o5VnRzTDFvaWxRQVJkT0VGcGluMnBNRmRxQTZ1RFRRaUFneHdUZ09MUGhqVllGMG9JSlJRa25IbEVJZUFNaXJyQVNzZFRTQWo5eHlmaG43VFAybU1rb2tkWXBRSlk5OEpBYVNjV1hBVUNrWGlhUHFNUmFpQURhMEF6RTRIUmM0V3NLVXZSVDBpUGhZYU1lRTZ0eElmYUhDWkp1R3BFcDhRd2hoUlVZQVJYTGxRcmJkZkc0QnFhT3FzVGZHVDFxVTFvVTZEYXE0WVFic0F0RUJnU0dPemNuK2dndkdJL2d4cGZZYVdsVmVDK040dm9uYkVDVUtPMW9vQk9kMHdHcXRCZ1VGRHNuU1ZRSXU2V3pBRTVnZXNpbENoRXJTbExCamVsSVMvL0o0eGlHTWE0Uys1WkIzRnRBT0crWnMrZ1lhMUJxRUMyR3RWR2d1YkhqQmVpeWJnMDFIQ05INksyZCsycWdHelZYRGtYMk5zNE5sTkFCQW9zS0YyQnJCemlMRVJqYVRJdFVIVTdKRGNnQXFEaEZCb0RhWkQrc3ZXZG1ITURRVWpYTUVDSXNMV1pMdElOOEN0aENtb3U5T2JWcUZYcnlSbTVyakpYbS9TNWN2L0FON2x5NWN1WExseTVjdVhMbHk1Y2VJM0xseTR5WTluVFJZODNwNEhVazYvU3lxeHROQWJsc1VFQndkY2s3Q1VrTTVOMEhDYXNDS1B6eEdTdTRpRlNKZ2tSWEZOS0RlVGdnaEJhNG5VcDZvcXFnazFWTk1Lb0Fnd0VwRkJxU1hCckF3d1M0V0tvMmxCdXg0R21wY1lyTkI3bFhSaE5lU0NZQVV0M21OcEJDVG1lb3R0Z2djTHBBUCtEeGtiTEk0NXpuTTZPWU1icXE0Q24xRllHd3ZRYUMwRld3VUV2UEh5L2JoeFNaS2o4SkFJZ3hOS0RWK0Q0dDFWSlIyTXBNcUdwamJ4V1RyT21PSTFHUlpBTXk3V1FBeFhFaDVWZXBqZzBobHUyWHhRUlJBQW1qUXh3SzE4SGlnYmFMQUJaUnhpUjRHb0FGYU9oRG93eS95dHNDQVlFYWJjZkhSWEl4dEtXQW9RRGdSdXlpaFA2Z0U3TzlNMW0rNUhyZDdxaUFBb2FxcTh1RktpU2dOVFFJQlRpdFNDa3lPWUxVZ1VWaE1HUVNLZEZJRFVSdEk4QnRrcDVNdzFJZ3dRU3RZQ005SDZVYVZOcUtPaUF2ZFZ0S0pSTlRMckVGWUhFZW5nNWhSeWxwRFcwdDJRQ0phdGRMUXM2QmxMZ2FCcm5rNFdvRWE0M0tCVFNlQTdWM284QlB3cmlJRUlsUUVBeW1zSW5ORjJOaWpJQkxsTWxIaFhvZkFRVUJRZ0poRENXUW9BanlCRUwxRFRWdTdRZ0R0SUJWTFlBQW9tRUI2Y01ZSUdvVEJubVFSUVRqa2c0QVZOWHozNzU4OS85a05OTk5OTk5OTk5OTk90NytLT21tbWxzQlVRNnRDeDhQWnFEMXp2Uk9ZNi9STTBKeFkzaHVuUkJFNWdSNTdyTkhzTzk4bFZHdHBNWHZyeElVR1VJTmQ4NGZBY3pLK2wya0tiY0t2YUdYeEN0WTdSVWc0TGZWN2JTMmlwWTdhYVF3SDNJeGJzRGRtOFF5Mk1EY2I1ZUpkN0EwZENKaVpuT3kwYUFFcDBBc25hTnRBd0dNY0JvcG9RRXQ0b1JzQ3lodGtVN3VqajZ2R0txcERzenRobFZIZGRUSlhYMUhxVGRKUGJlQVI2RHo5VDI0bG90Mm1SOHFoWUVBRzRCelFmcnVLQUo2T1M1Rks2clhVVWtZQytYc1lkQ0VFM25nOTJlRDNaNFBkazgvcWdLMUVFRTcwdXEzQTk5T2dheGFDRkRWQVVTR1Exb3Jzb09nMWhGdzNPS1NqS2JGQTAxMDBhN2JSb3JRQVU4QzFhRXBUUmdNd0t5S3dDVk54aG5XNWp4TFlydUZFUXNMZ29OWVlDQWlvZ0FZcEdrVG9zS2pvVXRKTGhLb0VDSjdnRlFscEV3Z01GNXpLaTFnQW16dUp2VlZKZFNTaVZhaVV1alV5UmRUVkN0WUdRSERvVEJCWjZXYWJHcWhZbXRvVlBnd3lxUXVpcGtJS0JNZ0FNR0ZBR2NCNXZUSk9zUTQ0QWhTWVdydUFQMHdsS21oNnNDWWZWMUxXRTloQ2dvQkhKVkZKeU9tZzlBcXRXT0pFT0VMcTZvS3RZQ2JZSlZVSUcxaGRJVnBnRFBIbGZzQWVVV1c2bS9SN0dBYm9vUUIwSVprb2xVRXJ5L3dDRkJlTWhGNE5sRzlVK3lZcmJuNlRMUXdSQlJMRnVrQnlqcnFjZG1JdGhCdEpBZEdzMmdzTWpTdUl4cVVsWUhRbUpVbkdVQ1N4U2lCYTVpMEdXQ0toNFFSREVaSXlMRUtDYU1XcHVCN0g4eWtrUkk1UzJDSmhKR0k4TWtzR2hEcEdDWWFkMVRhb0Jna2EyQnRnWE5HMHcyQVJ4S1FsRXczWW5VcVZWMUduTmdJSVBxOFlXQjNlbnhtYzdXcWpRODJnU2xVMkg2SEQxejlUMjQ4b3FxeFZ1d0FsVVFRc1FneWpkQmNJN29xMUNENGtIcGRLb2hGZzNlN2sySmpVa2s1Qm9Xd2dCUUY0QkE5akxsLzFrRjRjRFdGNDZrb1lScEhHK1lLSUFuS0s3K2tCeXpGWVlTUnNCNklIRFdiWUt0d2hMWXVTS0c4UzVBT0pOV2NBQ280cjE5eUF6V0lnVlNNd0VXcUR4Q2EyS2lnTW1sSFRZQTZEbWErSnBtSWZIVkFJZUFRQmJOWkE2WUhYWHJFZWNJbzRQYTFqMU1ITk02VnZabmxRTlBUMWlGdEt3bzNJWnpMK0p1d0MwVFozYjYvVllTN2NHMEVVdnJlTVZvam1HZEl1ajc1WkR4WElCaGtFNVphUy9VNGV1ZnFlM0M0cXVoa3FDSTdBbzdIZTJWR1RnQkNPTUlCblc5NzBjRVFBVkNMSlFKRVo3Z0taRlc0V0JsQTNoUE5ibU9Bbk5JQ1VTaHFsSUlob0pTVTBkTUFVVThPYTJqcW9BWmlpZU9wT0dhcDQvMWNGNGNUM2lMdEZQWE5rUU9jWDB6ZUcwQVNJbEJCQUdrYVlpNEppanhhS0hJUTNwT2RJRmt3QUNKaWVNYXRTS3JiT29KeFA2emtsM2RqdmY3QndlckppTGRJSGtSd0hGa1FwQU9nUTlzVkVxNUVSd0pIZU5zYzIvM2pSRW9vVk8zcGl5cTYyc2J2T0pJdVNiSEhiNnZHQXJ4UXBjVFNlSEVNQ09CMGxaMHRBNFo5RGg2L1NWZlBaQXF3QVhHS2cxZDFyL0FPV1E1OUFoZUVPNGxFTUdEc0N2NXZFM3pCU21kOEJhQndHdnhpVkl3MG1zdTZKS0VhQWRPbXNCTmNIYnBuTGlSbFI3T0lvYkhCZWNQUUhRU2NjKzU3NEJMVDN3YnJPR3g2NU5DS29GMi9zd1VnRk9RY2k4bWVCNzVIVkM4ZWNvaTZsVktjbWROTFZRenpHZUJrMlV2YkM2TWpIZkRpdXhOakt5dyt3NFBST05OendNNXdpRlpXV2Zod3IzQk1GVWhSa3VCcUFGVlpNRkVRZGtiY3RZa05aNEh2bmdlK2VCNzU0SHZuZ2UrZUI3NTRIdm5nZStlQjc1NEh2bmdlK2VCNzV4cDZtUGdlK2VCNzU0SHZuZ2UrTUhUM3dVamhRSWtGcEtrQVJZeEZPSk95Ry9RWkRFSmt5SVVvSnRMUUl1RjJ6V2tXQkVlSnFsY1BqbTQyY2lnOFk2T0JvYk5aT2xNS0hUWFJVUU9sdGVKMEduZmZsSlNKaG1aNUhwQ0tMaExDQXVzMG9sTEJIMi93RGh3d25Nb0drS2VhNk1YQVBnMWFsYU50VmJqNkhEMStncUVwb0FHQUVSTkNRS1E5VEt1UEovUmpwTzhBUVlJUXNqcXhZakhOUlk2RnJlZk5HcUNPeTRQT3FSc1RTcDJBY2dCZ0M0c3NLT0FMU082N09zQ2ZFUUhwVktORGRDT1dvWkJrRWJxVGE3Q1hHampLd2NTWUNjNUJhQU1FZ2MrRUdJS2RqV2g3c1BoQ0FaR05tOWpCR01NUUJSQmhuZ0RaekJRWmcyUzBtQ0M5dWl3VWhibXhoV1JVa1NTRzY0Uk1PZlZYY0tZQWhCSk4rZWFBakFoY2REY0pwanJxaGdCYlAzTWF1VzdGWEJCcXNJRUhTWk93WUNlNkRrSWFwVEFqSmFQYmR3RWgyd0NSdllxeUxaRkliUzBCZ3BzSm9lNHp0Z2dXc0ZmQlZHMVZPRFJwMU1sSkE5aG0wMmRrQVZjSy9OL1FrcnJNNlNSSkFERWtBRHZEMnhLclhiajY3dVkxT1pGQTcrZ1pvSVpwU29Pd2paaUVkTGpqckoxQkN0U2hFREJ1RWZUelVwQ0RaR0Ntd01Vd081bGNFa3oxd0VYaksyaUdpVU5DYU5DRFIvaEVYakF1Tm15OVhYYyt1a0pMMFFBTFFSUWNxUXhiRE5GUzY4WWxaSFhRTU5VUkRVVEVCMmdIZmdoUUtJbWFKM2kxVm9Wd05FaG9HK1RzalN1alVEYjJBR3NkY1NXbUlzYmlHQlJjcUVVaHBCQXpvQjd0cnU0cUF0aW9ZTVgrWEpuZnpmZG4zK21BRWJYRHlHaUFBQ2lGUy9TbTByR3E0UTJMVVIyVTVOMXh6VlpOZG1DM05LRjNyL0FFejRYSkJkNlRYYmlVT2oweStQeStQeStQeStOeStQeVhQK2VVUTdOSjhrR0dQSmhNNVVnaHR1MkdrVUV1TWhRZWVYVHhvVXRoU25MK3hiOUJxMGhVeDRyU1VKSXFOSHB1anBCeFZ4TmVQd0Vqb3hGTXN1a3NjajlZYVFSZ0x1d0FjdFNkd1FaczJheWd4YUpZUWdQREo0WXR2bE5EZWthaDQyTllzQVlROG5XMjJOU2lOREtYeDRnVUNvREhhWXpCMEk4RkVHZEQxUzRsQzRnRVhDMzRLSkFPbE5oZ3dpM3RBMVJCVnJ5c0IyOUhLeGdDUVFPUnEzSi84QWprbzZReDFLd0pJNjJIaW04TUlBb3VnWmRoQnNtbTdRRlFyZ3ROcUoxUTdEMFVHcVVyMEtWRUNDQnlka0UzZ1Z4K0IwSUdvRFVpR0xFS0NLbUJRU1ZBS2dJVkNvUzJKNjBxS2dIV3E4VGVHMHJtVU5FYnV6SWRWNlpQN2tqMkF4QTF6dDZZbkhBTWtMRXB3N1BWOUVWVW5RcWdVdXBLQnp5T2xSSUtUMlRYUHd1WHd1WHh1WHh1WHh1WHh1UnRiR3ptSS9tWjhibDhMbDhibDhMbGYvQU1ja3pwUkhvYVpObEpBZWdQWW4waEVhZWhLaEV6U1E1Unc3eFdTZ1J6VG1jMEpLSTRjSVZFR1dKMVRKczNHNlloYTd4NGFaQWdOdzFveW1LbjgxTVNuWXZJUnBoREF1WlJJcFFBWGNIU1pkMytHOWFvdE5weVhuZU93NUFpakFOMG5lRmQveWhRb1VLRnNtekVzWXJTRDJZMFUwdmZIc1Z4VXlVVnV3NVZ5ejZiRG9TK1p5TnBPd3huQTBaMENEcDBXcERxWW9WVjRocldIV3pvclJ1RURlSEF0UUxhQWh1Mk9NcGUrc2FvdktxaXB2VEVzeVVyVnpEYlNJd3UwMEFBR0Z3eERTV3cyYUdGWFVvbEo5S1NzQ3BZR3ExZ0t4VWpBTktDaUMxUjRqRU0rUnVpNUhSU2RzQm9ZQ0lZNjZZRVh0bXBBRG5JbjlnUUZDUkFFTXBWM0NnUUxvaldlaGowUFprZG5zejBQWm5vZXpQUTltZWg3TWpzOW1laDdNOUQyWjZIc3owUFpub2V6UFE5bWVoN005RDJaNkhzejBQWm5vZXpQUTltUjJlelBROW1laDdNOUQyWjZIc3owUFpub2V6UFE5bWVoN005RDJaNkhzejBQWm5vZXpQUTltZWg3TTlEMlo2SHN6MFBabm9lelBROW1laDdNOUQyWjZIc3owUFpub2V6STdQWmtkbnN6MFBabm9lelBROW1laDdNOUQyWjZIc3owUFpub2V6UFE5bUpzZ1lFR0l0ckVKUkpKY0hSakJjZ2lxWE00MTJ5YXhTaHAwRmd4U1RRcXNMcThXZ0VRSW5QUWdqdVBoZnBRUDI3bUZYZEpjU0xKc2l4RFROWE55UXE2TmpaWTFBbW5pQTlDQlhpWVVZYUNGaUdDT20zcmQ0QmEydGE0R3o4VWx4TzVMb1lFMWhxMDZrWUwrOGFMd3dZcUFaV0IwYnhFYkFRRW9iRWd3V2xUTUpBSWdzR3ZQZDRjYmxwcStrV0kyMlpNVmN0MUlGZDhVR2wyT3VJeFJoZWFPbnBVYmhwVU03NEtYQlp4R3dPb0EwZEVad1VRUk9SRmprY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY2prY1J3L2phb2dWSW13aWp2bzR2cmw0NVEzOTlEN0lCOUlIaHdEcm9iR2VSZGdXQ09FdmtIVmtCdE91U0J0TUpBZExVVWVGd1VZcHJreUxoUXNyTmU2RisvakdTZTJsZHRBbkFET2ZPUHJHZE1HYTBKRWlnV0luQWE0SlNBTmdObzBUVlZFY0hJZ0VBcTdPUW1JUjZheHdzQ25naUZybHFEdkFVcEFFbWkxcXdhQkM5aWFLMFE2SklnV2lJbXFDS0J6RlNXN1ZwbExhUUVLQUdpTzBNVkxvaUFpOHBNSklXRmJFSE5FTkgrQXFxV2NFbUNpRkd1TFk0QUJPb0E4Z0gvYlBIMEhpdDJQYis4SHIzZSt1eld4SWxBWFFtZzAxdk9Tb3VpZ1lwRFJ6RVlwaHA3UVFYNVZkcjExd1FBK2lZUmY2SGdjb0RicWI0TWo1Tk9kYnFibXFPbll3R1NRMnYxRzBlRThJNGFaY0xSRGNGSnlWQ2dKTnNBQkVsU0tQbUZHTnk4bEVyQnVCQ0tvblVTRDZEQ3JoSkNvNmpEaE1jVi9GbzZJOHBHaVFWR2VsSUtiY2N5UGdYOEJwci9hUEdFekkvVEJJTHROQlRtTWloZlZJRUZYV1dRUlZhbUJ2MFp1TUJYTnN6R1IzYjdkUEJtM1ZJWFg4SklPWDlxV1phd01zcWcwbXNOZUFWbndTSkprVjRDeERscUNLS0pEWTF5aEJEWllLeEVyTnBMUktWeWZwVWlJUzVqMUtoVkpIRzF5MERKMklWSUZjK2VHSjJoR3FvVm9wRk02REowZ2tuVlByRmJtdWFVQ09aUTZYbEFsaFRxazFvK0l5VU5xMHNKdTVJb2xUaDJxRUVTSU55dXNTckVya2xScENXck5tSjJJTnRxR0FCbDBRTllRS0l0V2RhZEFDR2hOS2VVcEhFaWxFV0J4NEJxRUg4UjhhN0puSnoyNkpHbnFteHNSRGxaRlJMYndRTXQyeG5YZ0xWMEJwTHlqQVVGZ01TYmwyaUFxSmtZZ2hGaEd1Skgya0MwVnRrd3hValRRRkExTGtwdU9YMnZWQnVOb2hwYVhTdFZ6K1NzOW1rMVdBcEw0TlZic1NBUlI2Q0NuZUZEdkVFSE11eWpzWGdLa0pzL1U2UHhERkF0NFhqYnBYWnlHR0E2TExSekMxUGs0TDRuTVlKOGhsR2dhQ21tbWxOY3ZQK2llTWpiSnVwVDBzRDVNMFNWcWR1ejZKditnUUtzaFlBMmd1bXdzNTR6MGxHcFBuYWpsTHJDVVFoMmx5TUI3YS9nOE9JRHFxUThPQXFzSFRwemlmV1l3YU5GRURGQnBBOVBHeENHVkUxOGoyelJlbW5SZHVtb0lGSE5YSVlLVmkrd1pBUU5tQUhBRmMybEpVSTdGQlFBWEFHUjIreWsyMVdNQVM3TXRaR29JZDFYZm83WWFVMkVHeDBIcGl6d0lxTVh1QkJGRGFnaWJYVVd1QWlnT1dHMWhWcUZSVWJZbk5zRGdnTVFvRVRjQ2hxT2JlS2RiQU1FRGdSektsaEdMYkhJOWdoakgybXRBSFFsUTVNZzdnMk5HZHNiQ0MrbVJOWHF5TTRBQVUzRWtNU0JBRnVTOGJQaFl3REJpdVZSbEd6TnNIWVdySWtWbVNVMnd4MWgwWUNBdjVRM2I1S0NKRGU4NnJWU05pem02YjA0S0dMMUdPRmxVTmtHMGlHSUowSU9CZ2dkWDFMb0JMaFFLNlFTaEJCSVVaUVd5TlVHWFdaU1ZvQVJybkdJd0l1NjRJb1JrblpMbkNrSnBZaUprTUljb2VmZ2dtbkJDWk9EL1JQSDBIaEo3Ykl1MzFDN0lrTitpRVJTa0FaeUMrNGlVVmlhRXUrTUFJcVIySW5DZW12NFBHVkFsVDFZOExKWmJvMVltNzZRYXhlS0Zvb0ozck9xZFo0NkR3VzN2YkUwZHVmY0wxVnlVRFNVK0FoWUFRWWdsVllzYTdzd0tnR1ExQ1l5Wmt0U2greVkzbDlDTUl5SnBvalR6YjZYWFJUa0ZHcTBUczBWZzNCTDRhbnQ5V0lKQUFwU01sS2JQWDdweWtCVzBPaEFlOEdCQlhabFFyZElNQXpLbFNhTEN3aVM2UnRtRGFaVURzS1NOSVVJakh1ZDdVMkU1UUFPb0JEdWxlY1JUanp6bzZWWU9na2ZRZ3NFQ1NLQ0k4eWRGOXpLQUNHMmdYTW95cG05SC9LK1FpVndreWF1S05IQnFwb0tNbTluU0lVb3RaSTdHb1paMFU5MlVlMGluRnl3ZXlPYjZKTlhFaTBXamtGU3dVSXVlQlVPY2VGdkdBa1NQZ0t0ZUowb09Zc1VlOWtpQm00RkJpZXRFNDN5OXpTRXJodUdISUExQklDV0NnY09Bb2k3Y1NMcUVpQnhnTUFkRjNXU3Zxd2N3VWNzZWdBZ0RwWUFSaFRDTGdwcDJZZDR0MldITVhFN2t4TXhTZ0sybVNiQ0tFc0Z1RmVqTG1LeEJNN2tlS0VHZ3dRSW15a2dwa2cxMFlCMGEwSlpONmNEVlhHaHZGRk5DTGY4dDR3cmJQZXRQQVhybzVrUjBRWFBzS0JSU3QyQUJhVTBmU2tRVlVDTmdGVWFreG1qRk5PbUhvZHVOanpyR0RUZ29vY2k3ZjRPeHhBQlFXNStXalNPcE9oTm03WEV0ZGtBY2paU0diajAyWnBKRHhzSm5CM2dsMjN4MkhacVlZNnRjQ2pFZUFSTTFVRGpWV1RjTHd5TkJaY1F0RHNGTGZKRUVnQUpxY3FxcWVreVZFUUZvZ09kQVFFUzNGVzJ4c0lWa0NmcjF0cUFwY2tVcEVRcnVOekhEYlVLaWh4TGFnZW81aHRTVFpUQ3dDVUUzR3ZOaURBRU90a3ZCRTBpOWpwMVFsTE41L05RcTdDcWtRZ0Fzc3V4ZDI5RFhlQkNSWXVDMm1TRUE5MU1NRGg0dDVTUHQxY1FLQTV0ZkN3MkljN1p3aG81bUV6N295QjJyR21FQzVQSTBHWUpDdGtHSk1URGdpMEFnd0RHTFl0cFd3RnJGaEpNQmhTYWlZTzBEUU1KVGtNYWRCaUZFZHprSGxBQWlVbk9BRitDQTFJRFJFOVFNZ1g1M044TkNDTTdHeHVWSFFJeFVYSXdoRlF4ZHhoY2JZbFo2QkpxbmxRczl1cXJBclpYRFMxUkVWcFNrYUNBRUIzTkZpN0ZJQVJaMDBnTTBCdjN4VHZhTnduSHlBVXF3SVNScndLTW9vNXprbitZOFpPMEU0RkI3MlVqVXRBODRvd2lRR0srandhUVZacElGcXhYaTRWdTAyQ1FnRmdDZ1MwZ2xNU2lpQktvNHRWYmEzK0R3NEhrVzZHczZBcmV3RzdaZmxmQWlKd0pVeFFNbHpaQjZnT29UUmFBMkhBUWF0N0dvcm84UUVDTENrUXhYTjZBdDZIWENkUzROazB5S2dab0dNODk4MWNzTk85QmFRSEYxWXlaSWtOSXNCdXNPbURZa2ZMY2lGbENPcXBjUjBGcGQzN0xRUnVBQzVQRlJibzRFU3dnTU40UjBqQkJPUzNoUWxoMVhBTW1VVlFZaFIxQ0J6T0l2c09sd0FzRGxzMmlZVUxpRXdwUWpTQUlWSmtyNU9jOGxZblN2WTNMYUlzWHNoTEt5ZGt6dVRVY2xnVUFxQXV0S1VzazJXYXhicXVzUWtFMEx1QndCU1FxWVNEVWhuUHkxNUJ4RFhKdTdvbURJZmpKaE9xb0xSUWQwNUFGNXdJYnlQdFlBT09XcldsMUxXaVhVSTAzVVZFS29JQUZkSVFCaFNJVXM3REtSYUFRaU00dWZRZzFCc1ZGMURKWk5LNGljVm1QU29pMXRnQ0VJRHB2bFhVaEk2TUhmbGFSTWtZQ2hZZ2dNclJ2L1M4YktXQnJKRStHQlZvdGdyb3RLR1pSYTJDTFhVZ1VKcnUzSmllYnJXdmtqa0Q0QjFyQzhvUFp4MmhLTk9FRVhIaFFCSkFaZDFCZHVvb0grVzhZNEdsQmtBeFVHc0VLNGdBYWxSK3JRUmVjbmtia2U3UVNKVlNRVGxNVUlFVDJYcHNEdzc3L3dBSGpDazNtcXRxT29WMFhRMm1CVWRPQUVDc0NYc0NCRk5GMkhNQ3FpVjBHQXMzREc4YXV5VVNQaWlRRXdMQ1FhNG1JRUExMEJGaDZmalV3QTU2Y3ZZd2JVRThvRzFLUmtORFFITHZyTTMxRUViRFYyQ3NUaFdFQmNJNEthRUF3TTNtKzdSYWFhbEFnN0ZFaWZJRVRrV3NLVDJUQWhUeFVkS2hMSnNyd1JzWUhvVVUxWHJMME1QMVg3T2xxMTFFaGczZmlNZ0ExMkthMFN6UkNHdDZnS2dDOXMybUNUQWxFZ0VkUklTUm1zSUJnUzFSU3FBa2dpTUZyWlphMGxZOFNyRWE4bGFleWdnMWE5Y0JVR21SNzA1c2NFaXQwc1dnVTZxR1hDT29wVjR4Nnlsc0ZJZFFLUXBMS0lDYkdDdDZtUmNEZG01QnNOOXpjK211bmNDYmk2VVJBNlFPZzRDSWxTcVlpK3E1MG9aWHRJUmhSK2RadmxBUWdkTEZXMnl3UkNIWVBLd2hqQnR1WmpkN0M4VUxIRVhSMSswN0VzYW5JSUJGTDJKTVJ0TnkxUVFsVHdVamhrSmcrY3FOaGpvaGkxU29GTGQ2a1V4V0Z1R0tBcTlIWDIvelhqQUlkdzNNbHZPWnZjdzBNdDVVK3NKdzFVem9nQVdySXdJRmpxQlJzSXJEUW5mcVp1anUwb1JSNGQvdzVNUm1kVEhkRVNXVG1PYW1FV1NxamlSQXRTTlFSc1ZHaHZ3aXFBTnlRSEd3a0pna2F3UTFNOUJweGkvODNGYnZrMElXZ0k0TUpEOWlJZzhtczBZTU5vRlZhaWN1c0VJZ0FhWVBPQ0dCb0JqU3Fnd29vVXhySlpGd1BPUVZCUE5GbzJJczJ3QVNHQXR2WURkNDVub2lvclplSWRMd3k1aHFReVVCTTdHbE95N0NpbmRLU3hQclVDMjNUUXhnQnp3V0VDVFZKTjdWRFFhcVB2YXFScVZLZ1o0aW12Y29waW1OSFpRMjFjUWpBNW9YTU9sUlVLM0ZKQklJV1pjUk5GVkxvWlpCUnU4QVdpNzdJeFVrcW4xbjBBT05aTjRnL3dBVXYrZThZRmpWNlV2M3RZeUc2UERPN3NvQlRhMmZSNUFSWHg2TmdheWxoeStva0ZIRmdTT2pxeDFHSi9EazhPSVJZM2kyS3Q5SkZkNWtKeStMRWVldFNFTXMzTmVudEtVc0s2QzJsZTVLNk02WmhNUnRFeUVMamExUldpQ0lDdW5GZ082YmdKRFJRbEVFa3FvdUZ3TmlvNFBBSWVGSWNVSWlrNXFvTFJIZkJFRHRNblVSUlo2TUZGcTM3ZXBDQnlJcFZYREQwdUFxMWpDdllBaEtZMk1Cb0oxRXpMd3FjOER4WndFSldjQXBDb2NxYWlPV1BXV0FGUVlYQXZvWnBBUW9TQ212Q0VocFo3RGFNWU1tUkVrZURLZWtKQ0RKN0ZkazMzMjFCV3ltY0RxYndjYk9iK0pnRUNKazFrMUpxcW9HTFJRazBvY2svcUxrWGF3UEVvS1lXaG1HRlBQWTRLcGx4UGRNTzBaMkpRNzl0c0tKT0REMGhCYWFrYk5wNjl5THg3UzhWQ0dDd1B3UkltZFFpTzZNQndaT0lvTmdPNU1DMWJyT2tEN1dtaWRQRFFORkc1VkJoRlFaaHBXVlVtdy8wVHhqQkdUcUZNdkpUekhDRUZqVmlLeUQ2QTIxcldvcE5SMy9BQ0tjbmh4Q0tJckYxZHVxRk9ycGtjbXFWajBrQVdFTTJERWwrZE0wdW1TTnFBUktnbXJIU1NodzVvWkVoTFA5YlRBMEJHR29yS2lRV0wrWkxSdXBVOFNWNFNSMG9SaFRBTklQZFhnRStSQmh0dUJhUUtpVlh5c0VQSWdqM05JTU9SZjY3YTBJTUlOeXNEZTFvcW9FY3RJUzdLcGlHcmZnRXlvRVNSb3lQcXMrUnhWaGNEWTJzQ3h1ZEZWYWpUY1VnVk9jV0x5Q0Frb0VvaWI0eU5BTURVRElLRXVrRmNGWXQvbUtKYUJxRlk1WXVrZ2FGclRzUWFrS3NFS2lRWGFuWkVGaUNzRVZvcTVvUlNGT0tFdUF4V0RwUmpaTzBiRGNLTWdKcTZKTkdKSzBDakZuRDIySWdrRE5SN0JHZWgzQjU0QkNJRVFPSGJDQVhIenB3UnF4a0JxVi9wbmo2RC8xdkw2a3ZOMWtXT2h0K21vbjZGT1Z6MFpmR0xyQk5mYUJLSjJ5Z0JCWFRDakV5U01jSDRnSzNHc1ZSYU1WVWdCQlNhRUVjOUh2VkxzcFRvenhEVGI2b0tEVUFTUUJEZU00T2RLVGFPa1ExME9CY3Z0b1d3RzVBRk5SQlplMTJIdEJESmFobFYyRzdIeGxKSEdFb3J4RllMSjNwRUVvcFpBYXJZc3hiNnJWQUxIZDBvS0hRREFsWkNIWmdIOUNxYXNyYThnaHRoWFIvd0NBVVExbGVBS3AyMm9iS2hGMGNIdk5MUUp0TGRPaURhelhXNlhBTitkendIR1NzYTFseHRrdS9VaFd6S1M4ZE5oRTFvZ2wyT2JDdjRIVmpwT29FYU5yR210N1FETXJRWUFRT0dpYW9RS0FzdXNEZHgvT1VscVVMZzJMQzVaWWFPSmRxQ0FWQ3FjQWUrclI2YVVZZWE1bXY5TnlaQUJxR1YxcUJzM3JXR001b1RPUUJOVXZqeXZxeUl0cGdnTmloQUhyWHJuUXBWVnhTWWdsRkhmQ21HYXgyQXAxamM5c2tHUTF5R3diQzUwMlRBdkNxb1pPS0ZGZnRoMHU5N1VqU2hLaXV4QjJMU0tPMjFPaVZwdHJSU0pDQWtVQkpORlRaVVlZUVpqeTRnYUthV1dvN1lYOTI1UUFvck5rZ2JHcGE2ZER3Uk8wZlJidmtGRXJJMDZIL3dCTkd6WnMyYk5telpzMlJvb2RuL1hHelpzMmJObXpaczJiTm16WnMyYk5telpzMmJObXpaczJiTm16WnMyYk5telpzMmJObXpac3ZCaEFubXlMclFPWUg5ZWNWWVYwb2VBQ0VxWVI5QXE2bHhhSkVqNFU5Rm41RG5EY1REMTIwaDJDUzh2T2FQdUhXZFJLaGVMakJVY0JSUVk5R2tmUk0rZnhId3VicWtoaXJMVTZDeEFXSWdXYU5nQ0ZBMjNWbUloVnZiaEpjRFNSWThSeUVUb2hCVkF6VjRqUnRwSFdJcUJRMFZWZTZkSFd5QnA5YkNFVTN0U0hwMmVjc0dGVG1pcTJrNGM2UmIwRldaSXNkZ2dXaWwwTzlNbHV3ZDNGWVU1K1lhSFN3YUlvR3dSNGFnV2dEQ1lIYlFKUFZBNE1RMWxOTTJmSHNPRWlSMlU3VUNrcnluRkUzb1U2b2g0QUl5cDB4VnhQRmhSemFEMk1GSk9Db2c2VFRzbkZaV0Q0R3U3T2xlVFRUMTZoaHNwQlIzaWlXb0ZTYURLK3BJa2RuWWhDUksyY0diMWVDWmVCNjVBbW1wcXdYSW14Vk5Rd2FFYWRPbEtsdlNYYSs1WEQ3NUJhVTVyank1SFNYeXBvblVlTStieG4vd0MyTXlVYmhHRkFHMFB2aHgxZ3F4VTUwM0xaaUhQdjR2QnRRUlVzZWhpWVBkdVVPY25JQTVGREVjWTdLRERPY0NEQVZISkh3WXg4T1BoQVlBVlZZQU4zRHFOVHA3aVE1ci83NGlBaUQxdU0vd0QzeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeCtXeEt1SUMySGduVTZ6YzZ3K0t3VlJzdkppcUpnaFVXSFV3WDBIQjQ4blNQTEc2MkZ2ZUt2RHF1K1BOem0rSzFKclBFQ05CK3I4c3JVaXV6VDJZTUNvSU5TOG8yVFhOdW8wNnE3ZWxMTkNnRUdzRjFPZ2lWZThPV3I1SmNIZ3djQVZHOVFDQnFBREc0cXdrS1RDQklFUUdnMk5RWUxPbEFIUmU3RzRZNGliYW1CaEVPbnVlbzVTVVpXRlZCZEFZQXpvNDJrWm9CWFZFRU1CR0ZobjErQjVadUN3UW9BRGZ1cVhablpPTmJBQzB4U1NLZG5uSXMwRWpFSnhFUXZQZU1aWmR4ZE1SRzRKMzRUNVEwU0pvYWxhdUJ3QzBCUkh5MHV2emdzZGJWQU9EZ2p1SGtRUWdMT1l3K25aZ2ZJMjFPQVFwRlJYZUJ2UnRkRHhHQjhCdVNHWU9RWVN1d0RDaklCSVdhZW1aT1JKUTNNeFE4RWRBZ2xnR3RWT0ZUYVlCcGc2SXFSMHVNQ1dSd2xFRFFRM21nUVlkT0Y4YlZpQWxLQzRKQjNIUTdDSUNkNDlGNURTd3BFVXlXYWkwYTQ5VVlmSVFRU1ZOb3lqZ004NnNEUWNKV0JvZGtFUUVoTFVoMXAzRlVkamNQZjhQTEVHYjYwOE9DelhSQXJSN0IyWXpwY0lMMlNZdFhkYUp0RXE4Z0pOdG5RS0VMYTB1c09WZ3Rxb2t2YldTMXEyUkFLUUc2ZFFHSWpKcmtlZnlaT3BkSEo2Z0RvK0ZXQnFVVWJRMExvYXdicG1WMkhHS3FBcWFJVmpzaHI2NUMyRDl3cGdHTkRTQ055bmJMUlFCd0lEL0N5N2R1M2J0MjdkdTNidDI3ZHUzYnRMTmlGMFl4SkV2SFZ5OGdQWEdKVDY2Y294UFNqaVJVd0s0U3lOT1pqV0plMlNUaHg0WDI4NUl2UEFTSEtxd095QlNuYVViSVdzRTRDQUlBTGVtaHNJUlNzRXgwWkg2bGs2Q1o1TXJ2cmg0TmNjczNBSDNmSEtmVWpvL0dLTFFmVTc4WktDRkdtN3Q1c1Vra0tBem9YU2NaRTVsYklKb0xBYWR0QWxJSlNTUE5YSUJpWFN6cUJJdENCaFNwUkR6NDZHQ1ZDc1V4bjE2cXdoMUFha0RRYWN3c0xUdTZZNWlsaFFHTGNZRmxNZkJnQXFCQWZ4SGt1UW9pcU94anJmckd0S1FXV0J5OHU2TmF1a1loWGRWekVETmFxTlZUWG9KcElnSmdEUU80TGsyb0ZwMkZnY1NVcVV4QzdTRjBpWWJWR21jTVdScXFDR0xaTjRxN1Vab2lZS3FNUUF4L29DQ0U5TFIxVmNHMnRUNG9FclhZMHJwbTdPQ0N0RkZRbmhEUWdteDI0RkF0ZHczb0J1QU1Qa3dzdEFWRFdXdW1DZnlWVFlSU3pJaU9qRVJ0Mmg4amEwVnlqZ3Y0M0NwRzJJUkdpYmN0T0w1RUhMcTg5QWQ0R2pTRTFLQnNncHJWQi8wWFI2bU1vUGxlb2doc1J5Tm1FeWdUcXF4cVFhQWpBWXRQQkkzcllaRUkwQmtNOVdZbkJnTG9rTklpa0xCanExZW5Ia2VyQU1BSlB1a0NaUlNvSW9NQlZpY1hUYUtReGE0S3h2bkVFNVZhUFFBVGt0QU43MEVzRkFqWG00V0tqa0dLbW5VYlRzL2xod09xUUZFcDZheHM3eFRRb29mWC8vMlE9PSZxdW90OyI+PC9pbWFnZXRleHR1cmU+IDwvYXBwZWFyYW5jZT48Ym94IHVzZWdlb2NhY2hlPSJ0cnVlIiBjY3c9InRydWUiIHNvbGlkPSJ0cnVlIiBzaXplPSI3MCA3MCAwIj48L2JveD48L3NoYXBlPjwvdHJhbnNmb3JtPg==\"><innerelement><number id=\"6ba19b76-efff-fae5-a449-1f4aabda6256\" type=\"Default\" node_id=\"7\"><number _id=\"1\" node_id=\"7\" id=\"6ba19b76-efff-fae5-a449-1f4aabda6256\" alerttype=\"1\" def=\"foo\" translation=\"-1.79691435395194 6.6561484057411855  0\" scale=\".10 .6  .10\" nodename=\"transform\" node=\"true\" render=\"true\" bboxcenter=\"0,0,0\" bboxsize=\"-1,-1,-1\" center=\"0,0,0\" rotation=\"0,0,0,0\" scaleorientation=\"0,0,0,0\"><shape def=\"float\" onclick=\"VTM3DMap.SelectElement(event);\" ispickable=\"true\" nodename=\"Shape\" render=\"true\" bboxcenter=\"0,0,0\" bboxsize=\"-1,-1,-1\"><appearance nodename=\"Appearance\" sorttype=\"auto\"><material specularcolor=\".5 .5 .5\" diffusecolor=\"0 1 0\" nodename=\"Material\" ambientintensity=\"0.2\" emissivecolor=\"0,0,0\" shininess=\"0.2\"></material></appearance><cone size=\"10 10 10\" nodename=\"Cone\" solid=\"true\" ccw=\"true\" usegeocache=\"true\" bottomradius=\"1\" height=\"2\" bottom=\"true\" side=\"true\" top=\"true\" subdivision=\"32\"></cone></shape></number></number></innerelement></initialize><nodes><number color=\"#fff\" alerttype=\"1\" name=\"ÐÑÇå ÑÇåÑæ\" _id=\"7\"><number type=\"Default\" nodename=\"DefaultShape\"><number def=\"foo\" rotation=\"Auto\" translation=\"Auto\" scale=\".10 .6  .10\" nodename=\"Transform\"><number def=\"float\" ispickable=\"true\" nodename=\"Shape\"><number nodename=\"Appearance\"><number specularcolor=\".5 .5 .5\" dinumberusecolor=\"0 1 0\" nodename=\"Material\"></number></number><number size=\"20 20 20\" nodename=\"Box\"></number></number></number></number></number><number color=\"#fff\" alerttype=\"1\" name=\"ÐÑÇå ÇÊÇÞ\" _id=\"2219\"><number type=\"Default\" nodename=\"DefaultShape\"><number def=\"foo\" rotation=\"Auto\" translation=\"Auto\" scale=\".10 .6  .10\" nodename=\"Transform\"><number def=\"float\" ispickable=\"true\" nodename=\"Shape\"><number nodename=\"Appearance\"><number specularcolor=\".5 .5 .5\" dinumberusecolor=\"0 1 0\" nodename=\"Material\"></number></number><number size=\"20 20 20\" nodename=\"Box\"></number></number></number></number></number></nodes></number></collections></element>";
//            return s;
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
