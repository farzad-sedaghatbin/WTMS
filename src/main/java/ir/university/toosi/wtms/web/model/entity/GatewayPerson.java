package ir.university.toosi.wtms.web.model.entity;

import ir.university.toosi.wtms.web.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;

public class GatewayPerson extends BaseEntity {
    private long id;
    private Gateway gateway;
    private Person person;


    public GatewayPerson() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}