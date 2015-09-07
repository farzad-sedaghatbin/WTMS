package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.GatewaySpecialState;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean
public class GatewaySpecialStateDAOImpl extends BaseDAOImpl<GatewaySpecialState> {

    public List<GatewaySpecialState> findByGatewayId(String id) {
        try {
            return (List<GatewaySpecialState>) em.createNamedQuery("GatewaySpecialState.findByGatewayId")
                    .setParameter("id", Long.valueOf(id))
                    .getResultList();
        } catch (Exception e) {
            return null;
        }
    }
}
