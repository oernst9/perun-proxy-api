package cz.muni.ics.perunproxyapi.models;

import lombok.Getter;

public class User {
    @Getter
    private long id;

    @Getter
    private String name;

    public User(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
