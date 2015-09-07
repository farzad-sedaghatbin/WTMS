package ir.university.toosi.tms.model.dao;


import ir.university.toosi.tms.model.entity.Role;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Stateless
@LocalBean

public class RoleDAOImpl extends BaseDAOImpl<Role> {


    public Role findById(String id) {
        try {
            return (Role) em.createNamedQuery("Role.findById")
                    .setParameter("id", Long.valueOf(id))
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}