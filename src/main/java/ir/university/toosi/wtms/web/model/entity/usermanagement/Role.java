package ir.university.toosi.wtms.web.model.entity.usermanagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.Permission;

import java.io.Serializable;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@JsonIgnoreProperties(value = "@id")
public class Role extends BaseEntity implements Serializable {
    @JsonProperty
    private long id;
    @JsonProperty
    private String description;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private Set<Operation> operations;
    @JsonProperty
    private String descText;
    @JsonProperty
    private String descShow;
    @JsonProperty
    private Set<Permission> permissions;
    @JsonProperty
    private String name;

    public Role() {
    }


    public Role(long id) {
        this.id = id;
    }

    public Role(long id, String name, String persianDescription, String englishDescription, boolean enabled, String deleted, Set<WorkGroup> workGroups, boolean selected) {
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


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
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

    public Set<Operation> getOperations() {
        return operations;
    }

    public void setOperations(Set<Operation> operations) {
        this.operations = operations;
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}