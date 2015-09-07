package ir.university.toosi.wtms.web.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;
import ir.university.toosi.wtms.web.model.entity.rule.RulePackage;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

@JsonIgnoreProperties(value = "@id")
public class Person extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String lastName;
    @JsonProperty
    private byte[] picture;
    @JsonProperty
    private String personnelNo;
    @JsonProperty
    private String nationalCode;
    @JsonProperty
    private String pin;
    @JsonProperty
    private String extraField1;
    @JsonProperty
    private String extraField2;
    @JsonProperty
    private String extraField3;
    @JsonProperty
    private String extraField4;
    @JsonProperty
    private BLookup personStatus;
    @JsonProperty
    private String mobile;
    @JsonProperty
    private String email;
    @JsonProperty
    private String address;
    @JsonProperty
    private String phone;
    @JsonProperty
    private String createDate;
    @JsonProperty
    private String createTime;
    @JsonProperty
    private String createBy;
    @JsonProperty
    private boolean hasCard;
    @JsonProperty
    private String workStation;
    @JsonProperty
    private String personOtherId;
    @JsonProperty
    private String password;
    @JsonProperty
    private Organ organRef;
    @JsonProperty
    private RulePackage rulePackage;
    @JsonProperty
    private byte[] finger;
    @JsonIgnore
    private boolean selected;

    public Person() {
    }

    public Person(String name, String lastName, byte[] picture, String personnelNo, String nationalCode, String pin) {
        this.name = name;
        this.lastName = lastName;
        this.picture = picture;
        this.personnelNo = personnelNo;
        this.nationalCode = nationalCode;
        this.pin = pin;
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getPersonnelNo() {
        return personnelNo;
    }

    public void setPersonnelNo(String personnelNo) {
        this.personnelNo = personnelNo;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
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

    public String getExtraField1() {
        return extraField1;
    }

    public void setExtraField1(String extraField1) {
        this.extraField1 = extraField1;
    }

    public String getExtraField2() {
        return extraField2;
    }

    public void setExtraField2(String extraField2) {
        this.extraField2 = extraField2;
    }

    public String getExtraField3() {
        return extraField3;
    }

    public void setExtraField3(String extraField3) {
        this.extraField3 = extraField3;
    }

    public String getExtraField4() {
        return extraField4;
    }

    public void setExtraField4(String extraField4) {
        this.extraField4 = extraField4;
    }

    public BLookup getPersonStatus() {
        return personStatus;
    }

    public void setPersonStatus(BLookup personStatus) {
        this.personStatus = personStatus;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Organ getOrganRef() {
        return organRef;
    }

    public void setOrganRef(Organ organRef) {
        this.organRef = organRef;
    }

    public RulePackage getRulePackage() {
        return rulePackage;
    }

    public void setRulePackage(RulePackage rulePackage) {
        this.rulePackage = rulePackage;
    }

    public String getWorkStation() {
        return workStation;
    }

    public void setWorkStation(String workStation) {
        this.workStation = workStation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getFinger() {
        return finger;
    }

    public void setFinger(byte[] finger) {
        this.finger = finger;
    }

    public boolean isHasCard() {
        return hasCard;
    }

    public void setHasCard(boolean hasCard) {
        this.hasCard = hasCard;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getPersonOtherId() {
        return personOtherId;
    }

    public void setPersonOtherId(String personOtherId) {
        this.personOtherId = personOtherId;
    }
}