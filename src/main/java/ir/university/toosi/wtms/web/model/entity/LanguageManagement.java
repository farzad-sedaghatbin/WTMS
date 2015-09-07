package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public class LanguageManagement extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String title;
    @JsonProperty
    private Languages type;

    public LanguageManagement() {
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

    public Languages getType() {
        return type;
    }

    public void setType(Languages type) {
        this.type = type;
    }
}
