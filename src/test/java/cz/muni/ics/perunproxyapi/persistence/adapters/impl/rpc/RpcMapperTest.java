package cz.muni.ics.perunproxyapi.persistence.adapters.impl.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cz.muni.ics.perunproxyapi.persistence.enums.MemberStatus;
import cz.muni.ics.perunproxyapi.persistence.models.AttributeObjectMapping;
import cz.muni.ics.perunproxyapi.persistence.models.ExtSource;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.Member;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttribute;
import cz.muni.ics.perunproxyapi.persistence.models.Resource;
import cz.muni.ics.perunproxyapi.persistence.models.User;
import cz.muni.ics.perunproxyapi.persistence.models.UserExtSource;
import cz.muni.ics.perunproxyapi.persistence.models.Vo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class RpcMapperTest {

    public static final String ID = "id";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";

    private final ObjectMapper mapper = new ObjectMapper();

    private ObjectNode sampleUser1Json;
    private User sampleUser1;
    private ObjectNode sampleUser2Json;
    private User sampleUser2;
    private ArrayNode usersArray;

    @BeforeEach
    public void setUp() {
        setUpUsers();
    }

    private void setUpUsers() {
        sampleUser1Json = JsonNodeFactory.instance.objectNode();
        Long id1 = 1L;
        String firstName1 = "John";
        String lastName1 = "Doe";

        sampleUser1Json.put(ID, id1);
        sampleUser1Json.put(FIRST_NAME, firstName1);
        sampleUser1Json.put(LAST_NAME, lastName1);

        sampleUser1 = new User(id1, firstName1, lastName1, new LinkedHashMap<>());

        sampleUser2Json = JsonNodeFactory.instance.objectNode();
        Long id2 = 2L;
        String firstName2 = "Joanne";
        String lastName2 = "Doe";

        sampleUser2Json.put(ID, id2);
        sampleUser2Json.put(FIRST_NAME, firstName2);
        sampleUser2Json.put(LAST_NAME, lastName2);

        sampleUser2 = new User(id2, firstName2, lastName2, new LinkedHashMap<>());

        usersArray = JsonNodeFactory.instance.arrayNode();
        usersArray.add(sampleUser1Json);
        usersArray.add(sampleUser2Json);
    }

    @Test
    public void testMapUserStandard() {
        User actual = RpcMapper.mapUser(sampleUser1Json);
        assertNotNull(actual);
        assertEquals(sampleUser1.getPerunId(), actual.getPerunId());
        assertEquals(sampleUser1.getFirstName(), actual.getFirstName());
        assertEquals(sampleUser1.getLastName(), actual.getLastName());
        assertEquals(sampleUser1, actual);
    }

    @Test
    public void mapNullUser() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUser(null));
    }

    @Test
    public void mapNullNodeUser() {
        assertNull(RpcMapper.mapUser(JsonNodeFactory.instance.nullNode()));
    }

    @Test
    public void mapUsersStandard() {
        List<User> actual = RpcMapper.mapUsers(usersArray);
        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertTrue(actual.containsAll(Arrays.asList(sampleUser1, sampleUser2)));
    }

    @Test
    public void mapNullUsers() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUsers(null));
    }

    @Test
    public void mapNullNodeUsers() {
        List<User> actual = RpcMapper.mapUsers(JsonNodeFactory.instance.nullNode());
        assertNotNull(actual);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void mapGroupStandard() {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("id", 1L);
        groupMap.put("parentGroupId", 2L);
        groupMap.put("name", "name");
        groupMap.put("description", "desc");
        groupMap.put("voId", 18L);
        JsonNode node = mapper.convertValue(groupMap, JsonNode.class);
        Group group = RpcMapper.mapGroup(node);
        assertEquals(group.getId(), 1L);
        assertEquals(group.getParentGroupId(), 2L);
        assertEquals(group.getName(), "name");
        assertEquals(group.getDescription(), "desc");
        assertEquals(group.getVoId(), 18L);
    }

    @Test
    public void mapGroupsStandard() {
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("id", 1L);
        groupMap.put("parentGroupId", 2L);
        groupMap.put("name", "name");
        groupMap.put("description", "desc");
        groupMap.put("voId", 18L);
        List<JsonNode> groups = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            groups.add(mapper.convertValue(groupMap, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(groups, JsonNode.class);
        List<Group> mappedGroups = RpcMapper.mapGroups(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedGroups.get(i).getId(), 1L);
            assertEquals(mappedGroups.get(i).getParentGroupId(), 2L);
            assertEquals(mappedGroups.get(i).getName(), "name");
            assertEquals(mappedGroups.get(i).getDescription(), "desc");
            assertEquals(mappedGroups.get(i).getVoId(), 18L);
        }
    }

    @Test
    public void mapNullGroup() {
        assertEquals(RpcMapper.mapGroup(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullGroups() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapGroups(null));
    }

    @Test
    public void mapFacilityStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        Facility facility = RpcMapper.mapFacility(node);
        assertEquals(facility.getId(), 1L);
        assertEquals(facility.getName(), "name");
        assertEquals(facility.getDescription(), "description");
    }

    @Test
    public void mapFacilitiesStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("description", "description");
        List<JsonNode> facilities = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            facilities.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(facilities, JsonNode.class);
        List<Facility> mappedFacilities = RpcMapper.mapFacilities(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedFacilities.get(i).getId(), 1L);
            assertEquals(mappedFacilities.get(i).getName(), "name");
            assertEquals(mappedFacilities.get(i).getDescription(), "description");
        }
    }

    @Test
    public void mapNullFacility() {
        assertEquals(RpcMapper.mapFacility(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullFacilities() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapFacilities(null));
    }

    @Test
    public void mapFacilityMissingId() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUser(node));
    }

    @Test
    public void mapMemberStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("userId", 2L);
        map.put("voId", 3L);
        map.put("status", "VALID");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        Member member = RpcMapper.mapMember(node);
        assertEquals(member.getId(), 1L);
        assertEquals(member.getUserId(), 2L);
        assertEquals(member.getVoId(), 3L);
        assertEquals(member.getStatus(), MemberStatus.VALID);
    }

    @Test
    public void mapMemberNullId() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", 2L);
        map.put("voId", 3L);
        map.put("status", "VALID");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapMember(node));
    }

    @Test
    public void mapMembersStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("userId", 2L);
        map.put("voId", 3L);
        map.put("status", "VALID");

        List<JsonNode> members = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            members.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(members, JsonNode.class);
        List<Member> mappedMembers = RpcMapper.mapMembers(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedMembers.get(i).getId(), 1L);
            assertEquals(mappedMembers.get(i).getUserId(), 2L);
            assertEquals(mappedMembers.get(i).getVoId(), 3L);
            assertEquals(mappedMembers.get(i).getStatus(), MemberStatus.VALID);
        }
    }

    @Test
    public void mapNullMember() {
        assertEquals(RpcMapper.mapMember(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullMembers() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapMembers(null));
    }

    @Test
    public void mapResourceStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("voId", 2L);
        map.put("facilityId", 3L);
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        Resource resource = RpcMapper.mapResource(node);
        assertEquals(resource.getId(), 1L);
        assertEquals(resource.getVoId(), 2L);
        assertEquals(resource.getFacilityId(), 3L);
        assertEquals(resource.getName(), "name");
        assertEquals(resource.getDescription(), "description");
    }

    @Test
    public void mapResourceNull() {
        Map<String, Object> map = new HashMap<>();
        map.put("voId", 2L);
        map.put("facilityId", 3L);
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapResource(node));
    }

    @Test
    public void mapResourcesStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("voId", 2L);
        map.put("facilityId", 3L);
        map.put("name", "name");
        map.put("description", "description");
        List<JsonNode> resources = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            resources.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(resources, JsonNode.class);
        List<Resource> mappedResources = RpcMapper.mapResources(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedResources.get(i).getId(), 1L);
            assertEquals(mappedResources.get(i).getVoId(), 2L);
            assertEquals(mappedResources.get(i).getFacilityId(), 3L);
            assertEquals(mappedResources.get(i).getName(), "name");
            assertEquals(mappedResources.get(i).getDescription(), "description");
        }
    }

    @Test
    public void mapNullResource() {
        assertEquals(RpcMapper.mapResource(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullResources() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapResources(null));
    }

    @Test
    public void mapExtSourceStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("type", "type");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        ExtSource extSource = RpcMapper.mapExtSource(node);
        assertEquals(extSource.getId(), 1L);
        assertEquals(extSource.getName(), "name");
        assertEquals(extSource.getType(), "type");
    }

    @Test
    public void mapExtSourcesStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("type", "type");

        List<JsonNode> resources = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            resources.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(resources, JsonNode.class);
        List<ExtSource> mappedExtSources = RpcMapper.mapExtSources(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedExtSources.get(i).getId(), 1L);
            assertEquals(mappedExtSources.get(i).getName(), "name");
            assertEquals(mappedExtSources.get(i).getType(), "type");
        }
    }

    @Test
    public void mapExtSourceNullId() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name");
        map.put("type", "type");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapExtSource(node));
    }

    @Test
    public void mapNullExtSource() {
        assertEquals(RpcMapper.mapExtSource(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullExtSources() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapExtSources(null));
    }

    @Test
    public void mapVoStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("shortName", "shortName");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        Vo vo = RpcMapper.mapVo(node);
        assertEquals(vo.getId(), 1L);
        assertEquals(vo.getName(), "name");
        assertEquals(vo.getShortName(), "shortName");
    }

    @Test
    public void mapVosStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("name", "name");
        map.put("shortName", "shortName");

        List<JsonNode> vos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            vos.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(vos, JsonNode.class);
        List<Vo> mappedVos = RpcMapper.mapVos(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedVos.get(i).getId(), 1L);
            assertEquals(mappedVos.get(i).getName(), "name");
            assertEquals(mappedVos.get(i).getShortName(), "shortName");
        }
    }

    @Test
    public void mapVoNullId() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "name");
        map.put("shortName", "shortName");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapVo(node));
    }

    @Test
    public void mapNullVo() {
        assertEquals(RpcMapper.mapVo(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullVos() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapVos(null));
    }

    @Test
    public void mapUserExtSourceStandard() throws ParseException {
        ExtSource extSource = new ExtSource(6L, "name", "type");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = dateFormat.parse("10/10/1999 10:10:10");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("login", "login");
        map.put("extSource", extSource);
        map.put("loa", 12);
        map.put("persistent", true);
        map.put("lastAccess", "1999-10-10 10:10:10");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        UserExtSource ues = RpcMapper.mapUserExtSource(node);
        assertEquals(ues.getId(), 1L);
        assertEquals(ues.getLogin(), "login");
        assertEquals(ues.getExtSource(), extSource);
        assertEquals(ues.getLoa(), 12);
        assertEquals(ues.getLastAccess(), timestamp);
    }

    @Test
    public void mapUserExtSourcesStandard() throws ParseException {
        ExtSource extSource = new ExtSource(6L, "name", "type");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = dateFormat.parse("10/10/1999 10:10:10");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("login", "login");
        map.put("extSource", extSource);
        map.put("loa", 12);
        map.put("persistent", true);
        map.put("lastAccess", "1999-10-10 10:10:10");

        List<JsonNode> vos = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            vos.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(vos, JsonNode.class);
        List<UserExtSource> mappedUserExtSources = RpcMapper.mapUserExtSources(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedUserExtSources.get(i).getId(), 1L);
            assertEquals(mappedUserExtSources.get(i).getLogin(), "login");
            assertEquals(mappedUserExtSources.get(i).getExtSource(), extSource);
            assertEquals(mappedUserExtSources.get(i).getLoa(), 12);
            assertEquals(mappedUserExtSources.get(i).getLastAccess(), timestamp);
        }
    }

    @Test
    public void mapNullUserExtSource() {
        assertEquals(RpcMapper.mapUserExtSource(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullUserExtSources() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUserExtSources(null));
    }

    @Test
    public void mapUserExtSourceNullId() throws ParseException {
        ExtSource extSource = new ExtSource(6L, "name", "type");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        Date date = dateFormat.parse("10/10/1999 10:10:10");
        long time = date.getTime();
        Timestamp timestamp = new Timestamp(time);
        Map<String, Object> map = new HashMap<>();
        map.put("login", "login");
        map.put("extSource", extSource);
        map.put("loa", 12);
        map.put("persistent", true);
        map.put("lastAccess", "1999-10-10 10:10:10");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUserExtSource(node));
    }

    @Test
    public void mapAttributeStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("friendlyName", "friendlyName");
        map.put("namespace", "namespace");
        map.put("description", "description");
        map.put("type", "type");
        map.put("displayName", "displayName");
        map.put("writable", true);
        map.put("unique", true);
        map.put("entity", "user");
        map.put("baseFriendlyName", "baseFriendlyName");
        map.put("friendlyNameParameter", "friendlyNameParameter");
        map.put("value", JsonNodeFactory.instance.objectNode());

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        PerunAttribute attribute = RpcMapper.mapAttribute(node);
        assertEquals(attribute.getId(), 1L);
        assertEquals(attribute.getFriendlyName(), "friendlyName");
        assertEquals(attribute.getNamespace(), "namespace");
        assertEquals(attribute.getDescription(), "description");
        assertEquals(attribute.getType(), "type");
        assertEquals(attribute.getDisplayName(), "displayName");
        assertEquals(attribute.getEntity(), "user");
        assertEquals(attribute.getBaseFriendlyName(), "baseFriendlyName");
        assertEquals(attribute.getFriendlyNameParameter(), "friendlyNameParameter");
        assertEquals(attribute.getValue(), JsonNodeFactory.instance.objectNode());
    }

    @Test
    public void mapAttributeNullId() {
        Map<String, Object> map = new HashMap<>();
        map.put("friendlyName", "friendlyName");
        map.put("namespace", "namespace");
        map.put("description", "description");
        map.put("type", "type");
        map.put("displayName", "displayName");
        map.put("writable", true);
        map.put("unique", true);
        map.put("entity", "user");
        map.put("baseFriendlyName", "baseFriendlyName");
        map.put("friendlyNameParameter", "friendlyNameParameter");
        map.put("value", JsonNodeFactory.instance.objectNode());

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapAttribute(node));
    }

    @Test
    public void mapAttributesStandard() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("friendlyName", "friendlyName");
        map.put("namespace", "namespace");
        map.put("description", "description");
        map.put("type", "type");
        map.put("displayName", "displayName");
        map.put("writable", true);
        map.put("unique", true);
        map.put("entity", "user");
        map.put("baseFriendlyName", "baseFriendlyName");
        map.put("friendlyNameParameter", "friendlyNameParameter");
        map.put("value", JsonNodeFactory.instance.objectNode());

        List<JsonNode> vos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            vos.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(vos, JsonNode.class);
        List<AttributeObjectMapping> attrMappings = List.of(new AttributeObjectMapping(), new AttributeObjectMapping());

        attrMappings.get(0).setIdentifier("first");
        attrMappings.get(1).setIdentifier("second");
        attrMappings.get(0).setRpcName("namespace:friendlyName");
        attrMappings.get(1).setRpcName("namespace:friendlyName");

        Map<String, PerunAttribute> attributes = RpcMapper.mapAttributes(node, new HashSet<>(attrMappings));
        for (Map.Entry<String, PerunAttribute> entry : attributes.entrySet()) {
            assertEquals(entry.getValue().getId(), 1L);
            assertEquals(entry.getValue().getFriendlyName(), "friendlyName");
            assertEquals(entry.getValue().getNamespace(), "namespace");
            assertEquals(entry.getValue().getDescription(), "description");
            assertEquals(entry.getValue().getType(), "type");
            assertEquals(entry.getValue().getDisplayName(), "displayName");
            assertEquals(entry.getValue().getEntity(), "user");
            assertEquals(entry.getValue().getBaseFriendlyName(), "baseFriendlyName");
            assertEquals(entry.getValue().getFriendlyNameParameter(), "friendlyNameParameter");
            assertEquals(entry.getValue().getValue(), JsonNodeFactory.instance.objectNode());
        }
    }

    @Test
    public void mapNullAttribute() {
        assertEquals(RpcMapper.mapAttribute(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapNullAttributes() {
        List<AttributeObjectMapping> attrMappings = List.of(new AttributeObjectMapping(), new AttributeObjectMapping());

        attrMappings.get(0).setIdentifier("first");
        attrMappings.get(1).setIdentifier("second");
        attrMappings.get(0).setRpcName("namespace:friendlyName");
        attrMappings.get(1).setRpcName("namespace:friendlyName");
        assertThrows(NullPointerException.class, () -> RpcMapper.mapAttributes(null, new HashSet<>(attrMappings)));
    }

    @Test
    public void mapNullMappings() {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1L);
        map.put("friendlyName", "friendlyName");
        map.put("namespace", "namespace");
        map.put("description", "description");
        map.put("type", "type");
        map.put("displayName", "displayName");
        map.put("writable", true);
        map.put("unique", true);
        map.put("entity", "user");
        map.put("baseFriendlyName", "baseFriendlyName");
        map.put("friendlyNameParameter", "friendlyNameParameter");
        map.put("value", JsonNodeFactory.instance.objectNode());

        List<JsonNode> vos = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            vos.add(mapper.convertValue(map, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(vos, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapAttributes(node, null));
    }

}
