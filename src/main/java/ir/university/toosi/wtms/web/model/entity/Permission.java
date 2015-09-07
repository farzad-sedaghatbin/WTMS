package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


public class Permission extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String objectId;
    @JsonProperty
    private String objectName;
    @JsonProperty
    private PermissionType permissionType;
    @JsonProperty
    private boolean selected;

    public Permission() {
    }

    public Permission(String objectId, String objectName, PermissionType permissionType) {
        this.objectId = objectId;
        this.objectName = objectName;
        this.permissionType = permissionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public PermissionType getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(PermissionType permissionType) {
        this.permissionType = permissionType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}