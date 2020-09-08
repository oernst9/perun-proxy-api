package cz.muni.ics.perunproxyapi.persistence.enums;

public enum Entity {
    USER,
    VO,
    FACILITY,
    GROUP,
    USER_EXT_SOURCE,
    RESOURCE;

    @Override
    public String toString() {
        if (this.equals(Entity.USER_EXT_SOURCE)) {
            return "userExtSource";
        }
        return name().toLowerCase();
    }
}
