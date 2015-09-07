package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;


public class BaseEntity implements Serializable {

    @JsonProperty
    protected String effectorUser;
    @JsonProperty
    protected String deleted;
    @JsonProperty
    protected String status;
    @JsonProperty
    protected Languages currentLang;

    public String getEffectorUser() {
        return effectorUser;
    }

    public void setEffectorUser(String effectorUser) {
        this.effectorUser = effectorUser;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Languages getCurrentLang() {
        return currentLang;
    }

    public void setCurrentLang(Languages currentLang) {
        this.currentLang = currentLang;
    }
}
