package cz.muni.ics.perunproxyapi.models;

import lombok.Getter;

public class Vo {

    @Getter
    private long id;

    @Getter
    private String name;

    @Getter
    private String shortName;

    public Vo(long id, String name, String shortName) {
        this.id = id;
        this.name = name;
        this.shortName = shortName;
    }
}
