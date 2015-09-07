package ir.university.toosi.wtms.web.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;


@JsonIgnoreProperties(value = "@id")
public class PDP extends HardwareTree {

    @JsonProperty
    private long id;
    @JsonProperty
    private String ip;
    @JsonProperty
    private Camera camera;
    @JsonProperty
    private Gateway gateway;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean online;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private String description;
    @JsonProperty
    private String descText;
    @JsonProperty
    private String nameText;
    @JsonProperty
    private boolean entrance;
    @JsonProperty
    private String updateDate;
    @JsonProperty
    private boolean success = false;
    @JsonIgnore
    private Gateway parentGateway;


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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
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

    public boolean isEntrance() {
        return entrance;
    }

    public void setEntrance(boolean entrance) {
        this.entrance = entrance;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    @JsonIgnore

    public Gateway getParentGateway() {
        return parentGateway;
    }
    @JsonIgnore

    public void setParentGateway(Gateway parentGateway) {
        this.parentGateway = parentGateway;
    }

    @Override
    @JsonIgnore

    public TreeNode getChildAt(int childIndex) {
        return null;
    }

    @Override
    @JsonIgnore

    public int getChildCount() {
        return 0;
    }

    @Override
    @JsonIgnore

    public TreeNode getParent() {
        return parentGateway;
    }

    @Override
    @JsonIgnore

    public int getIndex(TreeNode node) {
        return 0;
    }

    @Override
    @JsonIgnore

    public boolean getAllowsChildren() {
        return false;
    }

    @Override
    @JsonIgnore

    public boolean isLeaf() {
        return true;
    }

    @Override
    @JsonIgnore

    public Enumeration children() {
        return null;
    }
    @JsonIgnore

    public String getType(){
        return "pdp";
    }
    @JsonIgnore
    public void setType(String type){
        this.type=type;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
