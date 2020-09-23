package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ExtSourceTests {

    @Test
    public void testCreateExtSourceNullId() {
        assertThrows(NullPointerException.class, () -> new ExtSource(null, "name", "type"));
    }

    @Test
    public void testCreateExtSourceNullName() {
        assertThrows(NullPointerException.class, () -> new ExtSource(1L, null, "type"));
    }

    @Test
    public void testCreateExtSourceNullType() {
        assertThrows(NullPointerException.class, () -> new ExtSource(1L, "name", null));
    }

    @Test
    public void testCreateExtSourceEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new ExtSource(1L, "", "type"));
        assertThrows(IllegalArgumentException.class, () -> new ExtSource(1L, " ", "type"));
    }

    @Test
    public void testCreateExtSourceEmptyType() {
        assertThrows(IllegalArgumentException.class, () -> new ExtSource(1L, "name", ""));
        assertThrows(IllegalArgumentException.class, () -> new ExtSource(1L, "name", " "));
    }

}
