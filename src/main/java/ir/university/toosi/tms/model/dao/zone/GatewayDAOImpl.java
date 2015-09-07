package ir.university.toosi.tms.model.dao.zone;


import ir.university.toosi.tms.model.dao.BaseDAOImpl;
import ir.university.toosi.tms.model.entity.zone.Gateway;
import ir.university.toosi.tms.model.entity.zone.Zone;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class GatewayDAOImpl extends BaseDAOImpl<Gateway> {


    public Gateway findById(String id) {
        try {
            return (Gateway) em.createNamedQuery("Gateway.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Gateway> findByZone(Zone zone) {
        try {
            return (List<Gateway>) em.createNamedQuery("Gateway.findByZone")
                    .setParameter("zone", zone)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Gateway> findByZoneAndRulePackage(String zoneId, String rulePackageId) {
        try {
            return (List<Gateway>) em.createNamedQuery("Gateway.findByZoneAndRulePackage")
                    .setParameter("zoneId", zoneId)
                    .setParameter("rulepackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Gateway> findByRulePackage(Long rulePackageId) {
        try {
            return (List<Gateway>) em.createNamedQuery("Gateway.findByZoneAndRulePackage")
                    .setParameter("rulepackageId", rulePackageId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public List<Gateway> findByCamera(Long cameraId) {
        try {
            return (List<Gateway>) em.createNamedQuery("Gateway.findByCamera")
                    .setParameter("cameraId", cameraId)
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}