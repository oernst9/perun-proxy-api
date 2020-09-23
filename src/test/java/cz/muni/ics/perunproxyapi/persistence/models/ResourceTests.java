package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ResourceTests {

    @Test
    public void testCreateResourceNullId() {
        assertThrows(NullPointerException.class, () -> new Resource(null, 1L, 2L, "name", "desc"));
    }

    @Test
    public void testCreateResourceNullUserId() {
        assertThrows(NullPointerException.class, () -> new Resource(1L, null, 2L, "name", "desc"));
    }

    @Test
    public void testCreateResourceNullVoId() {
        assertThrows(NullPointerException.class, () -> new Resource(1L, 2L, null, "name", "desc"));
    }

    @Test
    public void testCreateResourceNullName() {
        assertThrows(NullPointerException.class, () -> new Resource(1L, 2L, 3L, null, "desc"));
    }

    @Test
    public void testCreateResourceNullDescription() {
        assertThrows(NullPointerException.class, () -> new Resource(1L, 2L, 3L, "name", null));
    }

    @Test
    public void testCreateResourceEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Resource(1L, 2L, 3L, "", "desc"));
        assertThrows(IllegalArgumentException.class, () -> new Resource(1L, 2L, 3L, " ", "desc"));
    }

    @Test
    public void testCreateResourceEmptyDescription() {
        assertDoesNotThrow(() -> new Resource(1L, 2L, 3L, "name", ""));
        assertDoesNotThrow(() -> new Resource(1L, 2L, 3L, "name", " "));
    }

}
