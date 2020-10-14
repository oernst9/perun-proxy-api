package cz.muni.ics.perunproxyapi.persistence.adapters.impl.rpc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.muni.ics.perunproxyapi.persistence.enums.MemberStatus;

import cz.muni.ics.perunproxyapi.persistence.models.*;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class RpcMapperTests {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void mapUserStandard() {
        Map<String, Object> userMap = new <String, Object>HashMap();
        userMap.put("id", 1L);
        userMap.put("firstName", "name");
        userMap.put("lastName", "surname");
        JsonNode node = mapper.convertValue(userMap, JsonNode.class);
        User user = RpcMapper.mapUser(node);
        assertEquals(user.getPerunId(), 1L);
        assertEquals(user.getFirstName(), "name");
        assertEquals(user.getLastName(), "surname");
    }

    @Test
    public void mapUserMissingId() {
        Map<String, Object> userMap = new <String, Object>HashMap();
        userMap.put("firstName", "name");
        userMap.put("lastName", "surname");
        JsonNode node = mapper.convertValue(userMap, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUser(node));
    }

    @Test
    public void mapNullUser() {
        assertEquals(RpcMapper.mapUser(JsonNodeFactory.instance.nullNode()), null);
    }

    @Test
    public void mapUsersStandard() {
        Map<String, Object> userMap = new <String, Object>HashMap();
        userMap.put("id", 1L);
        userMap.put("firstName", "name");
        userMap.put("lastName", "surname");
        List<JsonNode> users = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            users.add(mapper.convertValue(userMap, JsonNode.class));
        }
        JsonNode node = mapper.convertValue(users, JsonNode.class);
        List<User> mappedUsers = RpcMapper.mapUsers(node);
        for (int i = 0; i < 5; i++) {
            assertEquals(mappedUsers.get(i).getPerunId(), 1L);
            assertEquals(mappedUsers.get(i).getFirstName(), "name");
            assertEquals(mappedUsers.get(i).getLastName(), "surname");
        }
    }

    @Test
    public void mapNullUsers() {
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUsers(null));
    }

    @Test
    public void mapGroupStandard() {
        Map<String, Object> groupMap = new <String, Object>HashMap();
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
        Map<String, Object> groupMap = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapUser(node));
    }

    @Test
    public void mapMemberStandard() {
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
        map.put("userId", 2L);
        map.put("voId", 3L);
        map.put("status", "VALID");

        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapMember(node));
    }

    @Test
    public void mapMembersStandard() {
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
        map.put("voId", 2L);
        map.put("facilityId", 3L);
        map.put("name", "name");
        map.put("description", "description");
        JsonNode node = mapper.convertValue(map, JsonNode.class);
        assertThrows(NullPointerException.class, () -> RpcMapper.mapResource(node));
    }

    @Test
    public void mapResourcesStandard() {
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
        Map<String, Object> map = new <String, Object>HashMap();
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
