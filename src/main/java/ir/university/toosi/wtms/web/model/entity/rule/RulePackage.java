package ir.university.toosi.wtms.web.model.entity.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.calendar.Calendar;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class RulePackage extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private Calendar calendar;
//    @JsonProperty
//    private Set<Rule> rules;
//    @JsonProperty
//    private Set<RuleException> ruleExceptions;
    @JsonProperty
    private boolean aniPassBack;
    @JsonProperty
    private boolean selected;
    @JsonProperty
    private boolean allowExit;
    @JsonProperty
    private boolean allowExitGadget;


    public RulePackage() {
    }

    public RulePackage(long id, String name, Calendar calendars) {
        this.id = id;
        this.name = name;
        this.calendar = calendar;
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

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
//
//    public Set<Rule> getRules() {
//        return rules;
//    }
//
//    public void setRules(Set<Rule> rules) {
//        this.rules = rules;
//    }

    public boolean isAniPassBack() {
        return aniPassBack;
    }

    public void setAniPassBack(boolean aniPassBack) {
        this.aniPassBack = aniPassBack;
    }

    public boolean isAllowExit() {
        return allowExit;
    }

    public void setAllowExit(boolean allowExit) {
        this.allowExit = allowExit;
    }

    public boolean isAllowExitGadget() {
        return allowExitGadget;
    }

    public void setAllowExitGadget(boolean allowExitGadget) {
        this.allowExitGadget = allowExitGadget;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}