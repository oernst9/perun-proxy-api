package cz.muni.ics.perunproxyapi.persistence.models;

import cz.muni.ics.perunproxyapi.persistence.enums.AttributeType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class AttributeObjectMappingTests {
    @Test
    public void testCreateAOMNullExternalNames() {
        assertThrows(NullPointerException.class, () -> new AttributeObjectMapping(null, "rpcName", "ldapName",
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMNullRpcName() {
        assertThrows(NullPointerException.class, () -> new AttributeObjectMapping("identifier", null, "ldapName",
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMNullLdapName() {
        assertDoesNotThrow(() -> new AttributeObjectMapping("identifier", "rpcName", null,
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMNullAttributeType() {
        assertThrows(NullPointerException.class, () -> new AttributeObjectMapping("identifier", "rpcName", "ldapName",
                null, "separator"));
    }

    @Test
    public void testCreateAOMNullSeparator() {
        assertDoesNotThrow(() -> new AttributeObjectMapping("identifier", "rpcName", "ldapName",
                AttributeType.STRING, null));
    }

    @Test
    public void testCreateAOMEmptyExternalNames() {
        assertThrows(IllegalArgumentException.class, () -> new AttributeObjectMapping("", "rpcName", "ldapName",
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMEmptyRpcName() {
        assertThrows(IllegalArgumentException.class, () -> new AttributeObjectMapping("identifier", "", "ldapName",
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMEmptyLdapName() {
        assertDoesNotThrow(() -> new AttributeObjectMapping("identifier", "rpcName", "",
                AttributeType.STRING, "separator"));
    }

    @Test
    public void testCreateAOMEmptySeparator() {
        assertDoesNotThrow(() -> new AttributeObjectMapping("identifier", "rpcName", "ldapName",
                AttributeType.STRING, ""));
    }

}
