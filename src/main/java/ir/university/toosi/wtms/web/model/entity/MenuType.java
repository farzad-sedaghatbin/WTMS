package ir.university.toosi.wtms.web.model.entity;

/**
 * Created by farzad on 4/23/14.
 */
public enum MenuType {
    HARDWARE, CARD, ZONE, SEND_RECEIVE, SENTRY, MANAGEMENT, MAP, REPORT, CALENDAR, USER, SETTING;

    public MenuType getValue(int casse){
        switch (casse){
            case 0:
                return HARDWARE;
            case 1:
                return SENTRY;
            case 2:
                return CARD;
            case 3:
                return USER;
            case 4:
                return MAP;
            case 5:
                return ZONE;
            case 6:
                return REPORT;
            case 7:
                return CALENDAR;
            case 8:
                return SEND_RECEIVE;
            default:
                return MANAGEMENT;
        }

    }
}
