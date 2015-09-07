package ir.university.toosi.wtms.web.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

@JsonIgnoreProperties(value = "@id")
public class Camera extends BaseEntity {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String ip;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private String description;
    @JsonProperty
    private String descText;
    @JsonProperty
    private String nameText;
    @JsonProperty
    private boolean connected;
    @JsonProperty
    private long frames;
    public Camera() {
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public long getFrames() {
        return frames;
    }

    public void setFrames(long frames) {
        this.frames = frames;
    }
}
