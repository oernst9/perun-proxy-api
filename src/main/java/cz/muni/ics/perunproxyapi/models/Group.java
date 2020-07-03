package cz.muni.ics.perunproxyapi.models;

import lombok.Getter;

public class Group {

    @Getter
    private long id;

    @Getter
    private long voId;

    @Getter
    private String name;

    @Getter
    private String uniqueName;

    @Getter
    private String description;

    public Group(long id, long voId, String name, String uniqueName, String description) {
        this.id = id;
        this.voId = voId;
        this.name = name;
        this.uniqueName = uniqueName;
        this.description = description;
    }
}
