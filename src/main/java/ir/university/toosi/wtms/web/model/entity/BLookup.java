package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(value = "@id")
public class BLookup extends BaseEntity implements Serializable {
    public static final String CARD_STATUS_STOLEN = "CARD_STATUS_STOLEN";   //„”—ÊﬁÂ
    public static final String CARD_STATUS_LOST = "CARD_STATUS_LOST";         //„›ﬁÊœ
    public static final String CARD_STATUS_CLOSED = "CARD_STATUS_CLOSED";  //„”œÊœ
    public static final String CARD_IS_INVALID = "CARD_IS_INVALID";       //»«ÿ·
    public static final String CARD_IS_OPEN = "CARD_IS_OPEN";       //»«“


    public static final String CARD_SPECIAL = "CARD_SPECIAL";       //Ê?éÂ
    public static final String CARD_NORMAL = "CARD_NORMAL";      //⁄«œ?



    @JsonProperty
    private long id;
    @JsonProperty
    private String title;
    @JsonProperty
    private String code;
    @JsonProperty
    private Lookup lookup;
    @JsonProperty
    private String titleText;


    public BLookup() {
    }

    public BLookup(int id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Lookup getLookup() {
        return lookup;
    }

    public void setLookup(Lookup lookup) {
        this.lookup = lookup;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
