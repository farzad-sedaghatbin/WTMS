package ir.university.toosi.wtms.web.model.entity.zone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Iterators;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
@JsonIgnoreProperties(value = "@id")
public class Gateway extends  HardwareTree {

    @JsonProperty
    private long id;
    @JsonProperty
    private String description;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean passBackControl;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private List<Camera> cameras;
    @JsonProperty
    private Zone zone;
    @JsonProperty
    private RulePackage rulePackage;
    @JsonIgnore
    private String cameraName;
    @JsonProperty
    private List<Long> preRequestGateways;
    @JsonIgnore
    private List<PDP> children = new ArrayList<>();
    @JsonIgnore
    private Zone parentZone;


    public Gateway() {
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

    public boolean isPassBackControl() {
        return passBackControl;
    }

    public void setPassBackControl(boolean passBackControl) {
        this.passBackControl = passBackControl;
    }


//    public String getCrossingTypeCode() {
//        return "CrossingType";
//    }

    public List<Camera> getCameras() {
        return cameras;
    }

    public void setCameras(List<Camera> cameras) {
        this.cameras = cameras;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
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

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getCameraName() {
        if (cameraName == null || cameras.size() == 0)
            return "";
        else
            return cameras.get(0).getName();
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public List<Long> getPreRequestGateways() {
        return preRequestGateways;
    }

    public void setPreRequestGateways(List<Long> preRequestGateways) {
        this.preRequestGateways = preRequestGateways;
    }

    @JsonIgnore
    public void addChild(PDP pdp) {
        children.add(pdp);
    }

    @JsonIgnore
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }
    @JsonIgnore
    @Override
    public int getChildCount() {
        return children.size();
    }
    @JsonIgnore
    @Override
    public TreeNode getParent() {
        return parentZone;
    }
    @JsonIgnore
    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }
    @JsonIgnore
    @Override
    public boolean getAllowsChildren() {
        return false;
    }
    @JsonIgnore
    @Override
    public boolean isLeaf() {
        return false;
    }
    @JsonIgnore

    public Enumeration children() {
        return Iterators.asEnumeration(children.iterator());
    }
    @JsonIgnore

    public List<PDP> getChildren() {
        return children;
    }

    public void setChildren(List<PDP> children) {
        this.children = children;
    }
    @JsonIgnore

    public Zone getParentZone() {
        return parentZone;
    }

    @JsonIgnore
    public void setParentZone(Zone parentZone) {
        this.parentZone = parentZone;
    }
    @JsonIgnore

    public String getType(){
        return "gateway";
    }
    @JsonIgnore

    public void setType(String type){
        this.type=type;
    }
}