package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FacilityTests {

    @Test
    public void testCreateFacilityNullId() {
        assertThrows(NullPointerException.class, () -> new Facility(null, "name", "Description"));
    }

    @Test
    public void testCreateFacilityNullName() {
        assertThrows(NullPointerException.class, () -> new Facility(1L, null, "Description"));
    }

    @Test
    public void testCreateFacilityNullDescription() {
        assertThrows(NullPointerException.class, () -> new Facility(1L, "name", null));
    }

    @Test
    public void testCreateFacilityEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Facility(1L, "", "Description"));
        assertThrows(IllegalArgumentException.class, () -> new Facility(1L, " ", "Description"));
    }

    @Test
    public void testCreateFacilityEmptyDescription() {
        assertDoesNotThrow(() -> new Facility(1L, "name", ""));
        assertDoesNotThrow(() -> new Facility(1L, "name", " "));
    }

    @Test
    public void testSetEmptyRpIdentifier() {
        Facility facility = new Facility(1L, "name", "Desc");
        assertThrows(IllegalArgumentException.class, () -> facility.setRpIdentifier(""));
    }

    @Test
    public void testSetNullRpIdentifier() {
        Facility facility = new Facility(1L, "name", "Desc");
        assertThrows(NullPointerException.class, () -> facility.setRpIdentifier(null));
    }

}
