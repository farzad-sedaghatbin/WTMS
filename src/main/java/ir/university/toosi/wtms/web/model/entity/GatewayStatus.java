package ir.university.toosi.wtms.web.model.entity;


/**
 * @author : Hamed Hatami , Javad Sarhadi , Farzad Sedaghatbin, Atefeh Ahmadi
 * @version : 0.8
 */

public enum GatewayStatus {

    OPEN, CLOSE, NORMAL;

    public String getDescription() {

        switch (this) {

            case OPEN:
                return "";
            case CLOSE:
                return "";
            case NORMAL:
                return "";
        }
        return "NONE";
    }

    public String getValue() {
        switch (this) {

            case OPEN:
                return "1";
            case CLOSE:
                return "2";
            case NORMAL:
                return "3";
        }
        return "0";
    }
}