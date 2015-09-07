package ir.university.toosi.wtms.web.model.entity.zone;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterators;
import ir.university.toosi.wtms.web.action.UserManagementAction;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;
import ir.university.toosi.wtms.web.util.RESTfulClientUtil;

import javax.swing.tree.TreeNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */


public class Zone extends HardwareTree {

    @JsonProperty
    private long id;
    @JsonProperty
    private String description;
    @JsonProperty
    private boolean passBackControl;
    @JsonProperty
    private boolean truePass;
    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private String descText;
    @JsonProperty
    private RulePackage rulePackage;
    @JsonIgnore
    private List<Gateway> children = new ArrayList<>();


    public Zone() {
    }
    public String getType(){
        return "zone";
    }
    public void setType(String type){
        this.type=type;
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

    public boolean isTruePass() {
        return truePass;
    }

    public void setTruePass(boolean truePass) {
        this.truePass = truePass;
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

    public String getDescText() {
        return descText;
    }

    public void setDescText(String descText) {
        this.descText = descText;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    @JsonIgnore
    public void addChild(Gateway zone) {
        children.add(zone);
    }

    @JsonIgnore
    public static List<HardwareTree> prepareHierarchy(List<Zone> Zones, UserManagementAction me) throws IOException {
        List<HardwareTree> finalZones = new ArrayList<>();
        List<Gateway> gateways = new ArrayList<>();
        List<PDP> pdps = new ArrayList<>();
        for (Zone parentZone : Zones) {
            me.getGeneralHelper().getWebServiceInfo().setServiceName("/findGatewayByZone");
            gateways = new ObjectMapper().readValue(new RESTfulClientUtil().restFullService(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), new ObjectMapper().writeValueAsString(parentZone)), new TypeReference<List<Gateway>>() {
            });
            for (Gateway childGate : gateways) {
                parentZone.addChild(childGate);
                me.getGeneralHelper().getWebServiceInfo().setServiceName("/findPdpByGatewayId");
                pdps = new ObjectMapper().readValue(new RESTfulClientUtil().restFullServiceString(me.getGeneralHelper().getWebServiceInfo().getServerUrl(), me.getGeneralHelper().getWebServiceInfo().getServiceName(), String.valueOf(childGate.getId())), new TypeReference<List<PDP>>() {
                });
                for (PDP pdp : pdps) {
                    childGate.addChild(pdp);
                }
            }
            finalZones.add(parentZone);
        }
        return finalZones;
    }

    @JsonIgnore
    public TreeNode getChildAt(int childIndex) {
        return children.get(childIndex);
    }

    @JsonIgnore
    public int getChildCount() {
        return children.size();
    }

    @JsonIgnore

    public TreeNode getParent() {
        return null;
    }

    @JsonIgnore

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @JsonIgnore
    public boolean getAllowsChildren() {
        return true;
    }

    @JsonIgnore
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @JsonIgnore
    @Override
    public Enumeration children() {
        return Iterators.asEnumeration(children.iterator());
    }
}