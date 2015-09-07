package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


public class SystemConfiguration extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private SystemParameterType parameter;
    @JsonProperty
    private String value;
    @JsonProperty
    private String type;

    public SystemConfiguration() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public SystemParameterType getParameter() {
        return parameter;
    }

    public void setParameter(SystemParameterType parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}