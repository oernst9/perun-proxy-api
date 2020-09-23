package cz.muni.ics.perunproxyapi.persistence.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class PerunAttributeTests {

    @Test
    public void testCreatePerunAttributeNullId() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(null, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeNullFriendlyName() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, null,
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeNullNamespace() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, "friendlyName",
                null, "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeNullDescription() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", null,
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeNullDisplayName() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", null, true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeNullEntity() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, null, "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }


    @Test
    public void testCreatePerunAttributeNullBaseFriendlyName() {
        assertThrows(NullPointerException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", null,
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeEmptyFriendlyName() {
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, " ",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeEmptyNamespace() {
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                " ", "description",
                "string", "displayName", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeEmptyDisplayName() {
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", " ", true,
                true, "USER", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

    @Test
    public void testCreatePerunAttributeEmptyEntity() {
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, "", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, " ", "baseFriendlyName",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }


    @Test
    public void testCreatePerunAttributeEmptyBaseFriendlyName() {
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", "",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
        assertThrows(IllegalArgumentException.class, () -> new PerunAttribute(1L, "friendlyName",
                "namespace", "description",
                "string", "displayName", true,
                true, "USER", " ",
                "friendlyNameParameter", new ObjectMapper().createObjectNode()));
    }

}
