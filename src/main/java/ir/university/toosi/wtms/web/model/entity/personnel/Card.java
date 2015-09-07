package ir.university.toosi.wtms.web.model.entity.personnel;


import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BLookup;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

/**
 * @author : Hamed Hatami ,  Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */
public class Card extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String code;
    @JsonProperty
    private BLookup cardType;
    @JsonProperty
    private BLookup cardStatus;
    @JsonProperty
    private boolean visible;
    @JsonProperty
    private Person person;
    @JsonProperty
    private String startDate;
    @JsonProperty
    private String expirationDate;

    public Card() {
    }

    public Card(String name, String code, BLookup cardType, BLookup cardStatus, boolean visible, Person person) {
        this.name = name;
        this.code = code;
        this.cardType = cardType;
        this.cardStatus = cardStatus;
        this.visible = visible;
        this.person = person;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BLookup getCardType() {
        return cardType;
    }

    public void setCardType(BLookup cardType) {
        this.cardType = cardType;
    }

    public BLookup getCardStatus() {
        return cardStatus;
    }

    public void setCardStatus(BLookup cardStatus) {
        this.cardStatus = cardStatus;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }


    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}