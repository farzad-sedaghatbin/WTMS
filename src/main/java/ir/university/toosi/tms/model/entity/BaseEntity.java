package ir.university.toosi.tms.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class BaseEntity implements Serializable {

    @Transient
    @JsonProperty
    protected String effectorUser;
    @JsonProperty
    @Column(name = "DELETED")
    protected String deleted;
    @JsonProperty
    @Column(name = "STATUS")
    protected String status;
    @JsonProperty
    @Transient
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
