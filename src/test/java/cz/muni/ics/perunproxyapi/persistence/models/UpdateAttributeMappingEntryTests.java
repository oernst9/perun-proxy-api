package cz.muni.ics.perunproxyapi.persistence.models;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UpdateAttributeMappingEntryTests {

    @Test
    public void testCreateUAMENullExternalNames() {
        assertThrows(NullPointerException.class, () -> new UpdateAttributeMappingEntry(null, true, true));
    }

    @Test
    public void testCreateUAMEEmptyExternalNames() {
        assertThrows(IllegalArgumentException.class, () -> new UpdateAttributeMappingEntry(new ArrayList<>(), true, true));
    }

}
