package ir.university.toosi.wtms.web.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.personnel.Card;
import ir.university.toosi.wtms.web.model.entity.personnel.Organ;
import ir.university.toosi.wtms.web.model.entity.personnel.Person;
import ir.university.toosi.wtms.web.model.entity.zone.Gateway;
import ir.university.toosi.wtms.web.model.entity.zone.PDP;
import ir.university.toosi.wtms.web.model.entity.zone.Zone;


public class TrafficLog extends BaseEntity {

    @JsonProperty
    private long id;
    @JsonProperty
    private Person person;
    @JsonProperty
    private Card card;
    @JsonProperty
    private PDP pdp;
    @JsonProperty
    private Organ organ;
    @JsonProperty
    private Zone zone;
    @JsonProperty
    private Gateway gateway;
    @JsonProperty
    private String traffic_time;
    @JsonProperty
    private String traffic_date;
    @JsonProperty
    private String pictures;
    @JsonProperty
    private String video;
    @JsonProperty
    private boolean valid;
    @JsonProperty
    private boolean last;
    @JsonProperty
    private boolean exit;
    @JsonProperty
    private boolean finger;
    @JsonProperty
    private boolean offline;


    public TrafficLog() {
    }


    public TrafficLog(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Gateway getGateway() {
        return gateway;
    }

    public void setGateway(Gateway gateway) {
        this.gateway = gateway;
    }
    @JsonIgnore
    public String getTime() {
        return traffic_time;
    }

    @JsonIgnore
    public void setTime(String time) {
        this.traffic_time = time;
    }
    @JsonIgnore
    public String getDate() {
        return traffic_date.replace("/", "");
    }
    @JsonIgnore
    public void setDate(String date) {
        this.traffic_date = date;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getTraffic_time() {
        return traffic_time;
    }

    public void setTraffic_time(String traffic_time) {
        this.traffic_time = traffic_time;
    }

    public String getTraffic_date() {
        return traffic_date;
    }

    public void setTraffic_date(String traffic_date) {
        this.traffic_date = traffic_date;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public PDP getPdp() {
        return pdp;
    }

    public void setPdp(PDP pdp) {
        this.pdp = pdp;
    }

    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isFinger() {
        return finger;
    }

    public void setFinger(boolean finger) {
        this.finger = finger;
    }

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }
}

