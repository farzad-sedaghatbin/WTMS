package ir.university.toosi.tms.model.entity.zone;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.tms.model.entity.BaseEntity;
import ir.university.toosi.tms.model.entity.rule.RulePackage;

import javax.persistence.*;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@Entity
@Table(name = "tb_Zone")
@NamedQueries({
        @NamedQuery(
                name = "Zone.list",
                query = "select z from Zone z where z.deleted='0'"
        ),
        @NamedQuery(
                name = "Zone.findById",
                query = "select z from Zone z where z.id=:id"
        ),
        @NamedQuery(
                name = "Zone.findByRulePackageId",
                query = "select z from Zone z where z.rulePackage.id=:id and z.deleted='0'"
        ),
        @NamedQuery(
                name = "Zone.exist",
                query = "select z from Zone z where z.name=:name and z.deleted='0'"
        )
})
public class Zone extends BaseEntity {

    @Id
    @GeneratedValue
    @JsonProperty
    @Column(name = "id")
    private long id;
    @JsonProperty
    @Column(name = "name")
    private String name;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @JsonProperty
    @Column(name = "passBack")
    private boolean passBackControl;
    @JsonProperty
    @Column(name = "truePassControl")
    private boolean truePass;
    @Column(name = "enabled")
    @JsonProperty
    private boolean enabled;
    @Transient
    @JsonProperty
    private boolean selected;
    @Transient
    @JsonProperty
    private String descText;
    @JsonProperty
    @ManyToOne
    private RulePackage rulePackage;


    public Zone() {
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


}