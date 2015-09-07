package ir.university.toosi.wtms.web.model.entity.rule;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

import java.util.List;

/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class RuleException extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String fromDate;
    @JsonProperty
    private String toDate;
    @JsonProperty
    private Integer fromIndex;
    @JsonProperty
    private Integer toIndex;
    @JsonProperty
    private String name;
    @JsonProperty
    private String startTime;
    @JsonProperty
    private String endTime;
    @JsonProperty
    private String entranceCount;
    @JsonProperty
    private String exitCount;
    @JsonProperty
    private boolean deny;
    @JsonProperty
    private List<RulePackage> rulePackage;


    public RuleException() {

    }

    public RuleException(long id, String startTime, String endTime, String entranceCount, String exitCount) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.entranceCount = entranceCount;
        this.exitCount = exitCount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEntranceCount() {
        return entranceCount;
    }

    public void setEntranceCount(String entranceCount) {
        this.entranceCount = entranceCount;
    }

    public String getExitCount() {
        return exitCount;
    }

    public void setExitCount(String exitCount) {
        this.exitCount = exitCount;
    }

    public List<RulePackage> getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(List<RulePackage> rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public Integer getFromIndex() {
        return fromIndex;
    }

    public void setFromIndex(Integer fromIndex) {
        this.fromIndex = fromIndex;
    }

    public Integer getToIndex() {
        return toIndex;
    }

    public void setToIndex(Integer toIndex) {
        this.toIndex = toIndex;
    }

    public boolean isDeny() {
        return deny;
    }

    public void setDeny(boolean deny) {
        this.deny = deny;
    }
}