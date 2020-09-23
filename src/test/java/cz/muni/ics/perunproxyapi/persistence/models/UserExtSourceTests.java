package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserExtSourceTests {

    @Test
    public void testCreateUserExtSourceNullId() {
        ExtSource extSource = new ExtSource(1L, "name", "type");
        Timestamp timestamp = new Timestamp(1);
        assertThrows(NullPointerException.class, () -> new UserExtSource(null, extSource, "login", 1, true, timestamp));
    }

    @Test
    public void testCreateUserExtSourceNullExtSource() {
        Timestamp timestamp = new Timestamp(1);
        assertThrows(NullPointerException.class, () -> new UserExtSource(1L, null, "login", 1, true, timestamp));
    }

    @Test
    public void testCreateUserExtSourceNullLogin() {
        ExtSource extSource = new ExtSource(1L, "name", "type");
        Timestamp timestamp = new Timestamp(1);
        assertThrows(NullPointerException.class, () -> new UserExtSource(1L, extSource, null, 1, true, timestamp));
    }

    @Test
    public void testCreateUserExtSourceNegativeLoa() {
        ExtSource extSource = new ExtSource(1L, "name", "type");
        Timestamp timestamp = new Timestamp(1);
        assertThrows(IllegalArgumentException.class, () -> new UserExtSource(1L, extSource, "login", -1, true, timestamp));
    }

    @Test
    public void testCreateUserExtSourceNullLastAccess() {
        ExtSource extSource = new ExtSource(1L, "name", "type");
        assertDoesNotThrow(() -> new UserExtSource(1L, extSource, "login", 1, true, null));
    }

    @Test
    public void testCreateUserExtSourceEmptyLogin() {
        ExtSource extSource = new ExtSource(1L, "name", "type");
        Timestamp timestamp = new Timestamp(1);
        assertThrows(IllegalArgumentException.class, () -> new UserExtSource(1L, extSource, "", 1, true, timestamp));
        assertThrows(IllegalArgumentException.class, () -> new UserExtSource(1L, extSource, " ", 1, true, timestamp));
    }

}
