package cz.muni.ics.perunproxyapi.enums;

public enum Entity {
    USER,
    VO,
    FACILITY,
    GROUP,
    RESOURCE;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
