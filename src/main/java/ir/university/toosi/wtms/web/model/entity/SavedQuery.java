package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public class SavedQuery extends BaseEntity {

    @JsonProperty
    private long id;

    @JsonProperty
    private String title;
    @JsonProperty
    private String query;
    @JsonProperty
    private String type;
    @JsonProperty
    private String schedule = "0";
    @JsonProperty
    private Long count;
    @JsonProperty
    private String exportType;


    public SavedQuery() {
    }

    public SavedQuery(long id, String title, String query, String type) {
        this.id = id;
        this.title = title;
        this.query = query;
        this.type = type;
    }

    public SavedQuery(String query, String schedule, Long count, String exportType, String type) {
        this.query = query;
        this.schedule = schedule;
        this.count = count;
        this.type = type;
        this.exportType = exportType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getExportType() {
        return exportType;
    }

    public void setExportType(String exportType) {
        this.exportType = exportType;
    }
}
