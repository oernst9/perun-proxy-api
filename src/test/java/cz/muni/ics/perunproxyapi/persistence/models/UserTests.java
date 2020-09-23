package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTests {

    @Test
    public void testCreateUserNullId() {
        assertThrows(NullPointerException.class, () -> new User(null, "name", "name2", new HashMap<>()));
    }

    @Test
    public void testCreateUserNullFirstName() {
        assertThrows(NullPointerException.class, () -> new User(1L, null, "s", new HashMap<>()));
    }

    @Test
    public void testCreateUserNullLastName() {
        assertThrows(NullPointerException.class, () -> new User(1L, "firstName", null, new HashMap<>()));
    }

    @Test
    public void testCreateUserNullLogin() {
        assertThrows(NullPointerException.class, () -> new User(1L, "firstName", "lastName", null, new HashMap<>()));
    }

    @Test
    public void testCreateUserEmptyFirstName() {
        assertDoesNotThrow(() -> new User(1L, "", "lastName", new HashMap<>()));
        assertDoesNotThrow(() -> new User(1L, " ", "lastName", new HashMap<>()));
    }

    @Test
    public void testCreateUserEmptyLastName() {
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "firstName", "", new HashMap<>()));
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "firstName", " ", new HashMap<>()));
    }

    @Test
    public void testCreateUserEmptyLogin() {
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "firstName", "lastName", "", new HashMap<>()));
        assertThrows(IllegalArgumentException.class, () -> new User(1L, "firstName", "lastName", " ", new HashMap<>()));
    }

    @Test
    public void testCreateUserNullAttributesMap() {
        assertThrows(NullPointerException.class, () -> new User(1L, "firstName", "lastName", null));
    }

}
