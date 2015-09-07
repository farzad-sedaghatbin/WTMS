package ir.university.toosi.wtms.web.model.entity;

public enum EventLogSearchItems {

    TITLE, TYPE, DATE, TIME, USER, TABLE;

    public String getDescription() {

        switch (this) {
            case TITLE:
                return "Title";
            case TYPE:
                return "Type";
            case DATE:
                return "Date";
            case TABLE:
                return "Table";
            case TIME:
                return "Time";
            case USER:
                return "User";
        }
        return "";
    }

    public String getValue() {

        switch (this) {
            case TITLE:
                return "1";
            case TYPE:
                return "2";
            case DATE:
                return "3";
            case TABLE:
                return "4";
            case TIME:
                return "5";
            case USER:
                return "6";
        }

        return "0";
    }
}
