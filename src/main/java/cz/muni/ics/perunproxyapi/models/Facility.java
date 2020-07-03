package cz.muni.ics.perunproxyapi.models;

import lombok.Getter;

public class Facility {

    @Getter
    private long id;

    @Getter
    private String name;

    @Getter
    private String description;

    @Getter
    private long identifier;

    public Facility(long id, String name, String description, long identifier) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.identifier = identifier;
    }
}
