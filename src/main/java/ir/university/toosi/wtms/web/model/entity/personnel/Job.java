package ir.university.toosi.wtms.web.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

import javax.persistence.Column;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public class Job extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String employNo;
    @JsonProperty
    private BLookup employType;
    @JsonProperty
    private String folderNo;
    @JsonProperty
    private String internalTel;
    @JsonProperty
    private BLookup assistType;
    @JsonProperty
    private BLookup postType;
    @JsonProperty
    @Column(name = "description")
    private String description;
    @JsonProperty
    private Person person;

    public Job() {
    }

    public Job(String employNo, BLookup employType, String folderNo, String internalTel, BLookup assistType, BLookup postType, String description, Person person, Organ organ) {
        this.employNo = employNo;
        this.employType = employType;
        this.folderNo = folderNo;
        this.internalTel = internalTel;
        this.assistType = assistType;
        this.postType = postType;
        this.description = description;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmployNo() {
        return employNo;
    }

    public void setEmployNo(String employNo) {
        this.employNo = employNo;
    }

    public BLookup getEmployType() {
        return employType;
    }

    public void setEmployType(BLookup employType) {
        this.employType = employType;
    }

    public String getFolderNo() {
        return folderNo;
    }

    public void setFolderNo(String folderNo) {
        this.folderNo = folderNo;
    }

    public String getInternalTel() {
        return internalTel;
    }

    public void setInternalTel(String internalTel) {
        this.internalTel = internalTel;
    }

    public BLookup getAssistType() {
        return assistType;
    }

    public void setAssistType(BLookup assistType) {
        this.assistType = assistType;
    }

    public BLookup getPostType() {
        return postType;
    }

    public void setPostType(BLookup postType) {
        this.postType = postType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}