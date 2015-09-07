package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(value = "@id")
public class Lookup extends BaseEntity implements Serializable {

    public static final Long ORGAN_TYPE_ID = 1l;
    public static final Long CARD_TYPE_ID = 2l;
    public static final Long CARD_STATUS_ID = 3l;
    public static final Long EMPLOYEE_TYPE_ID = 4l;
    public static final Long ASSIST_TYPE_ID = 5l;
    public static final Long POST_TYPE_ID = 6l;
    public static final Long PC_LOCATION_ID = 8l;

    @JsonProperty
    private long id;

    @JsonProperty
    private String title;

    @JsonProperty
    private boolean definable;
    @JsonProperty
    private String titleText;



    public Lookup() {
    }

    public Lookup(int id) {
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

    public boolean isDefinable() {
        return definable;
    }

    public void setDefinable(boolean definable) {
        this.definable = definable;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
