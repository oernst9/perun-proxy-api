package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GroupTests {

    @Test
    public void testCreateGroupNullId() {
        assertThrows(NullPointerException.class, () -> new Group(null, 5L, "name", "desc", "uniqueName", 2L));
    }

    @Test
    public void testCreateGroupNullName() {
        assertThrows(NullPointerException.class, () -> new Group(1L, 5L, null, "desc", "uniqueName", 2L));
    }

    @Test
    public void testCreateGroupNullDescription() {
        assertThrows(NullPointerException.class, () -> new Group(1L, 5L, "name", null, "uniqueName", 2L));
    }

    @Test
    public void testCreateGroupEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Group(1L, 5L, "", "desc", "uniqueName", 2L));
        assertThrows(IllegalArgumentException.class, () -> new Group(1L, 5L, " ", "desc", "uniqueName", 2L));
    }

    @Test
    public void testCreateGroupEmptyDescription() {
        assertDoesNotThrow(() -> new Group(1L, 5L, "name", "", "uniqueName", 2L));
        assertDoesNotThrow(() -> new Group(1L, 5L, "name", " ", "uniqueName", 2L));
    }

    @Test
    public void testCreateGroupEmptyUniqueName() {
        assertDoesNotThrow(() -> new Group(1L, 5L, "name", "desc", "", 2L));
        assertDoesNotThrow(() -> new Group(1L, 5L, "name", "desc", " ", 2L));
    }

    @Test
    public void testCreateGroupNullUniqueName() {
        assertDoesNotThrow(() -> new Group(1L, 5L, "name", "desc", null, 2L));
    }

    @Test
    public void testCreateGroupNullVoId() {
        assertThrows(NullPointerException.class, () -> new Group(1L, 5L, null, "desc", "uniqueName", null));
    }

}
