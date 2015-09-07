package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;

public class GatewaySpecialState extends BaseEntity {
    @JsonProperty
    private long id;
    @JsonProperty
    private String date;
    @JsonProperty
    private String time;
    @JsonProperty
    private String until;
    @JsonProperty
    private GatewayStatus gateWayStatus;
    @JsonProperty
    private Gateway gateway;


    public GatewaySpecialState() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public GatewayStatus getGateWayStatus() {
        return gateWayStatus;
    }

    public void setGateWayStatus(GatewayStatus gateWayStatus) {
        this.gateWayStatus = gateWayStatus;
    }

    public String getUntil() {
        return until;
    }

    public void setUntil(String until) {
        this.until = until;
    }
}