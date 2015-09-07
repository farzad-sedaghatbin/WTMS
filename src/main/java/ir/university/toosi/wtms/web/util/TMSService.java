package ir.university.toosi.wtms.web.util;

import ir.university.toosi.wtms.web.action.monitoring.HandleMonitoringAction;
import ir.university.toosi.wtms.web.model.entity.TrafficLog;

import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;


/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@ApplicationPath("restful")
@Path("/TMSService")
public class TMSService extends Application {
    HandleMonitoringAction handleMonitoringAction;

    /**
     * Push Service Implementation
     */
    @POST
    @Path("/Cross")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String cross(TrafficLog trafficLog) {
        handleMonitoringAction = ManagedBeanManager.lookup(HandleMonitoringAction.class);
        if (handleMonitoringAction != null)
            handleMonitoringAction.sendMessage(trafficLog);
        return "true";

    }

}
