package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Comment extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private String message;
    @JsonProperty
    private TrafficLog trafficLog;
    @JsonProperty
    private boolean reject;
    @JsonProperty
    private boolean authorize;
    @JsonProperty
    protected String effectorUser;

    public Comment() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TrafficLog getTrafficLog() {
        return trafficLog;
    }

    public void setTrafficLog(TrafficLog trafficLog) {
        this.trafficLog = trafficLog;
    }

    public boolean isAuthorize() {
        return authorize;
    }

    public void setAuthorize(boolean authorize) {
        this.authorize = authorize;
    }

    public boolean isReject() {
        return reject;
    }

    public void setReject(boolean reject) {
        this.reject = reject;
    }

    public String getEffectorUser() {
        return effectorUser;
    }

    public void setEffectorUser(String effectorUser) {
        this.effectorUser = effectorUser;
    }
}

