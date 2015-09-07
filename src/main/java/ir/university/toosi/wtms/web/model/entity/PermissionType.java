package ir.university.toosi.wtms.web.model.entity;

public enum PermissionType {
    ORGAN, ZONE, GATEWAY, PDP;

    public String getDescription() {

        switch (this) {

            case ORGAN:
                return "Organ";
            case ZONE:
                return "Zone";
            case GATEWAY:
                return "Gateway";
            case PDP:
                return "PDP";
        }
        return "NONE";
    }

    public String getValue() {
        switch (this) {

            case ORGAN:
                return "1";
            case ZONE:
                return "2";
            case GATEWAY:
                return "3";
            case PDP:
                return "4";
        }
        return "0";
    }
}