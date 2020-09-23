package cz.muni.ics.perunproxyapi.persistence.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.muni.ics.perunproxyapi.persistence.enums.AttributeType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class PerunAttributeValueTests {

    @Test
    public void testCreateAttributeValueNullName() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(NullPointerException.class, () -> new PerunAttributeValue(null, AttributeType.STRING, mapper.createObjectNode()));
    }

    @Test
    public void testCreateAttributeValueNullType() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(NullPointerException.class, () -> new PerunAttributeValue("name", (String) null, mapper.createObjectNode()));
    }


    @Test
    public void testCreateAttributeValueNullAttributeType() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(NullPointerException.class, () -> new PerunAttributeValue("name", (AttributeType) null, mapper.createObjectNode()));
    }

    @Test
    public void testCreateAttributeValueNullJsonNode() {
        ObjectMapper mapper = new ObjectMapper();
        assertThrows(NullPointerException.class, () -> new PerunAttributeValue(null, AttributeType.STRING, null));
    }

}
