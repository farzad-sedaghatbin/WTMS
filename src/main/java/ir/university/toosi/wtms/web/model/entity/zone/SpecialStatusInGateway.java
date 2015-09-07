package ir.university.toosi.wtms.web.model.entity.zone;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


public class SpecialStatusInGateway extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String date;
    @JsonProperty
    private String time;
    @JsonProperty
    private String ZCUAddress;
    @JsonProperty
    private BLookup specialStatusActionType;
    @JsonProperty
    private Gateway gateway;

    public SpecialStatusInGateway() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getZCUAddress() {
        return ZCUAddress;
    }

    public void setZCUAddress(String ZCUAddress) {
        this.ZCUAddress = ZCUAddress;
    }

    public BLookup getSpecialStatusActionType() {
        return specialStatusActionType;
    }

    public void setSpecialStatusActionType(BLookup specialStatusActionType) {
        this.specialStatusActionType = specialStatusActionType;
    }

    public String getSpecialStatusActionTypeCode() {
        return "SpecialStatusActionType";
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}