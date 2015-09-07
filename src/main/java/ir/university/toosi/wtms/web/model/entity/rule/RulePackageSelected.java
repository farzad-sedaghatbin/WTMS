package ir.university.toosi.wtms.web.model.entity.rule;

import java.io.Serializable;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class RulePackageSelected implements Serializable {

    private RulePackage rulePackage;
    private boolean selected;

    public RulePackageSelected() {
    }

    public RulePackageSelected(RulePackage rulePackage, boolean selected) {
        this.rulePackage = rulePackage;
        this.selected = selected;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}