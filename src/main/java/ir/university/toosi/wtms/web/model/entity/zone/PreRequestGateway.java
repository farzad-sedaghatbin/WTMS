package ir.university.toosi.wtms.web.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;


/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class PreRequestGateway extends BaseEntity {

    @JsonProperty
    private long id;
    private Gateway gateway;
    private Gateway preGateway;

    public PreRequestGateway() {
    }

    public PreRequestGateway(long id, Gateway gateway, Gateway preGateway) {
        this.id = id;
        this.gateway = gateway;
        this.preGateway = preGateway;
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

    public Gateway getPreGateway() {
        return preGateway;
    }

    public void setPreGateway(Gateway preGateway) {
        this.preGateway = preGateway;
    }
}