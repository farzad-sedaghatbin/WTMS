package ir.university.toosi.wtms.web.model.entity.usermanagement;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

import java.io.Serializable;
import java.util.Set;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@JsonIgnoreProperties(value = "@id")
public class Operation extends BaseEntity implements Serializable {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private Operation parent;
    @JsonProperty
    private boolean selected;

    public Operation() {
    }


    public Operation(long id) {
        this.id = id;
    }

    public Operation(long id, String name, String persianDescription, String englishDescription, boolean enabled, String deleted, Set<WorkGroup> workGroups, boolean selected, Operation parent) {
        this.id = id;
        this.name = name;
        this.enabled = enabled;
        this.deleted = deleted;
        this.selected = selected;
        this.parent = parent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Operation getParent() {
        return parent;
    }

    public void setParent(Operation parent) {
        this.parent = parent;
    }
}