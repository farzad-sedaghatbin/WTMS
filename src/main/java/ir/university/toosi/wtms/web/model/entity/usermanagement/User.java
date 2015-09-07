package ir.university.toosi.wtms.web.model.entity.usermanagement;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.PC;
import ir.university.toosi.wtms.web.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.util.HashUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@JsonIgnoreProperties(value = "@id")
public class User extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String firstname;
    @JsonProperty
    private String lastname;
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String enable;
    @JsonProperty
    private String lastLoginDate;
    @JsonProperty
    private String lastLoginIP;
    @JsonProperty
    private String failedLoginCount;
    @JsonProperty
    private boolean online;
    @JsonProperty
    private Set<WorkGroup> workGroups;
    @JsonProperty
    private String skin;
    @JsonProperty
    private String extraField1;
    @JsonProperty
    private String extraField2;
    @JsonProperty
    private String extraField3;
    @JsonProperty
    private String extraField4;
    @JsonProperty
    private Person person;
    @JsonProperty
    private Set<PC> pcs;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(Set<PC> pcs) {
        this.pcs = pcs;
    }

    public User(String username, String password, String enable) {
        this.username = username;
        this.password = password;
        this.enable = enable;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.length() < 30) {
            this.password = HashUtil.getSecurePassword(password);
        } else {
            this.password = password;
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getFailedLoginCount() {
        return failedLoginCount;
    }

    public void setFailedLoginCount(String failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(String lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getLastLoginIP() {
        return lastLoginIP;
    }

    public void setLastLoginIP(String lastLoginIP) {
        this.lastLoginIP = lastLoginIP;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public Set<WorkGroup> getWorkGroups() {
        return workGroups;
    }

    public void setWorkGroups(Set<WorkGroup> workGroups) {
        this.workGroups = workGroups;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Set<PC> getPcs() {
        if (pcs == null)
            return new HashSet<>();
        return pcs;
    }

    public void setPcs(Set<PC> pcs) {
        this.pcs = pcs;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}
