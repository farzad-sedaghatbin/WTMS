package ir.university.toosi.wtms.web.model.entity.usermanagement;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class WorkGroup extends BaseEntity implements Serializable {
    @JsonProperty
    private long id;
    @JsonProperty
    private String description;
    @JsonProperty
    private String enabled;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private Set<Role> roles;
    @JsonProperty
    private String descText;
    @JsonProperty
    private String descShow;
    @JsonProperty
    private String name;



    public WorkGroup() {
    }

    public WorkGroup(int id) {
    }


    public WorkGroup(long id, String name, String persianDescription, String englishDescription, String enabled, String deleted, boolean selected, Set<User> users) {
        this.id = id;
        this.enabled = enabled;
        this.deleted = deleted;
        this.selected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public String getDescShow() {
        return descShow;
    }

    public void setDescShow(String descShow) {
        this.descShow = descShow;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}