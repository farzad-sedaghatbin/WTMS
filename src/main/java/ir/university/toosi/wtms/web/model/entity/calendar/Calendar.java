package ir.university.toosi.wtms.web.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

import java.util.Hashtable;
import java.util.List;


public class Calendar extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String code;
    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private Hashtable<String, Hashtable<Integer, List<String>>> calendarDatesTable = new Hashtable<>();
    @JsonProperty
    private byte[] content;


    public Calendar() {
    }

    public Calendar(long id) {
        this.id = id;
    }

    public Calendar(String code, String name, String description, boolean defaultCalendar) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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


    public Hashtable<String, Hashtable<Integer, List<String>>> getCalendarDatesTable() {
        return calendarDatesTable;
    }

    public void setCalendarDatesTable(Hashtable<String, Hashtable<Integer, List<String>>> calendarDatesTable) {
        this.calendarDatesTable = calendarDatesTable;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
