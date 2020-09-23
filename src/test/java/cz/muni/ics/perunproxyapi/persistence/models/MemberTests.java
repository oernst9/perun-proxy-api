package cz.muni.ics.perunproxyapi.persistence.models;

import cz.muni.ics.perunproxyapi.persistence.enums.MemberStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MemberTests {

    @Test
    public void testCreateMemberNullId() {
        assertThrows(NullPointerException.class, () -> new Member(null, 1L, 2L, "VALID"));
    }

    @Test
    public void testCreateMemberNullUserId() {
        assertThrows(NullPointerException.class, () -> new Member(1L, null, 2L, "VALID"));
    }

    @Test
    public void testCreateMemberNullVoId() {
        assertThrows(NullPointerException.class, () -> new Member(1L, 2L, null, "VALID"));
    }

    @Test
    public void testCreateMemberNullStatus() {
        assertThrows(NullPointerException.class, () -> new Member(1L, 2L, 3L, (String) null));
    }

    @Test
    public void testCreateMemberNullMemberStatus() {
        assertThrows(NullPointerException.class, () -> new Member(1L, 2L, 3L, (MemberStatus) null));
    }

    @Test
    public void testCreateMemberWrongStatus() {
        assertThrows(NullPointerException.class, () -> new Member(1L, 2L, 3L, "RANDOM"));
    }

}
