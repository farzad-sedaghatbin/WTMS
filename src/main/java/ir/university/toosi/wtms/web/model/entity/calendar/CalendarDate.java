package ir.university.toosi.wtms.web.model.entity.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import ir.university.toosi.wtms.web.model.entity.BaseEntity;

public class CalendarDate extends BaseEntity {

    @JsonProperty
    private long id;

    @JsonProperty
    private int startDate;
    @JsonProperty
    private int endDate;

    @JsonProperty
    private DayType dayType;
    @JsonProperty
    private Calendar calendar;

    public CalendarDate() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public DayType getDayType() {
        return dayType;
    }

    public void setDayType(DayType dayType) {
        this.dayType = dayType;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public int getEndDate() {
        return endDate;
    }

    public void setEndDate(int endDate) {
        this.endDate = endDate;
    }
}
