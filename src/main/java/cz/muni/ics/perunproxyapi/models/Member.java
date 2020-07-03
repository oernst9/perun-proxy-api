package cz.muni.ics.perunproxyapi.models;

import cz.muni.ics.perunproxyapi.enums.Entity;
import lombok.Getter;

public class Member {

    @Getter
    private long id;

    @Getter
    private String voId;

    @Getter
    private Entity status;

    public Member(long id, String voId, Entity status) {
        this.id = id;
        this.voId = voId;
        this.status = status;
    }
}
