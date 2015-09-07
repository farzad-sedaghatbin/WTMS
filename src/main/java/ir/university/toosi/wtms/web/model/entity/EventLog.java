package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(value = "@id")
public class EventLog extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private EventLogType operation;
    @JsonProperty
    private String objectId;
    @JsonProperty
    private String tableName;
    @JsonProperty
    private String username;
    @JsonProperty
    private String date;
    @JsonProperty
    private String time;

    public EventLog() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EventLogType getOperation() {
        return operation;
    }

    public void setOperation(EventLogType operation) {
        this.operation = operation;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
