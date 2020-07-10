package cz.muni.ics.perunproxyapi.adapters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import cz.muni.ics.perunproxyapi.adapters.interfaces.FullAdapter;
import cz.muni.ics.perunproxyapi.adapters.mappers.RpcMapper;
import cz.muni.ics.perunproxyapi.attributes.AttributeMappingService;
import cz.muni.ics.perunproxyapi.attributes.AttributeObjectMapping;
import cz.muni.ics.perunproxyapi.connectors.PerunConnectorRpc;
import cz.muni.ics.perunproxyapi.enums.Entity;
import cz.muni.ics.perunproxyapi.enums.MemberStatus;
import cz.muni.ics.perunproxyapi.models.Facility;
import cz.muni.ics.perunproxyapi.models.Group;
import cz.muni.ics.perunproxyapi.models.Member;
import cz.muni.ics.perunproxyapi.models.PerunAttribute;
import cz.muni.ics.perunproxyapi.models.PerunAttributeValue;
import cz.muni.ics.perunproxyapi.models.User;
import cz.muni.ics.perunproxyapi.models.UserExtSource;
import cz.muni.ics.perunproxyapi.models.Vo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cz.muni.ics.perunproxyapi.adapters.PerunAdapterRpcConstants.*;

public class RpcAdapterImpl extends PerunAdapterWithMappingServices implements FullAdapter {

    private final static Logger log = LoggerFactory.getLogger(RpcAdapterImpl.class);

    @Autowired
    private PerunConnectorRpc connectorRpc;

    @Autowired
    private AttributeMappingService attributeMappingService;

    @Override
    public Map<String, PerunAttribute> getAttributes(Entity entity, Long entityId, List<String> attrsToFetch) {
        if (!this.connectorRpc.isEnabled()) {
            return new HashMap<>();
        } else if (attrsToFetch == null || attrsToFetch.isEmpty()) {
            return new HashMap<>();
        }

        Set<AttributeObjectMapping> mappings = attributeMappingService.getMappingsByIdentifiers(attrsToFetch);

        List<String> rpcNames = mappings.stream().map(AttributeObjectMapping::getRpcName).collect(Collectors.toList());

        Map<String, Object> map = new LinkedHashMap<>();
        map.put(entity.toString().toLowerCase(), entityId);
        map.put("attrNames", rpcNames);

        JsonNode res = connectorRpc.post(ATTRIBUTES_MANAGER, "getAttributes", map);
        return RpcMapper.mapAttributes(res, mappings);
    }

    @Override
    public UserExtSource getUserExtSource(String extSourceName, String extSourceLogin) {
        if (!this.connectorRpc.isEnabled()) {
            return null;
        }

        log.trace("getUserExtSource({}, {})", extSourceName, extSourceLogin);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("extSourceLogin", extSourceLogin);
        map.put("extSourceName", extSourceName);

        JsonNode res = connectorRpc.post(USERS_MANAGER, "getUserExtSourceByExtLoginAndExtSourceName", map);
        UserExtSource ues = RpcMapper.mapUserExtSource(res);
        log.trace("getUserExtSource({}, {}) returns {}", extSourceName, extSourceLogin, ues);
        return ues;


    }

    @Override
    public MemberStatus getMemberStatusByUserAndVo(User user, Vo vo) {
        Member member = getMemberByUser(user, vo);
        return member.getStatus();
    }

    @Override
    public void setAttributes(Entity entity, Long entityId, List<PerunAttribute> attributes) {
        if (!this.connectorRpc.isEnabled()) {
            return;
        }

        log.trace("getUserExtSource({}, {}, {})", entity, entityId, attributes);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(entity.toString(), entityId);
        map.put("attributes", attributes);
        connectorRpc.post(ATTRIBUTES_MANAGER, "setAttributes", map);
    }

    @Override
    public boolean updateUserExtSourceLastAccess(UserExtSource userExtSource) {
        if (!this.connectorRpc.isEnabled()) {
            return false;
        }

        log.trace("updateUserExtSourceLastAccess({})", userExtSource);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("userExtSource", userExtSource);

        JsonNode res = connectorRpc.post(USERS_MANAGER, "updateUserExtSourceLastAccess", map);
        boolean successful = (res == null || res.isNull() || res instanceof NullNode);
        log.trace("updateUserExtSourceLastAccess({}) returns {}", userExtSource, res);
        return successful;
    }

    @Override
    public Member getMemberByUser(User user, Vo vo) {
        if (!this.connectorRpc.isEnabled()) {
            return null;
        }

        log.trace("getMemberByUser({}, {})", user, vo);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("user", user);
        map.put("vo", vo);

        JsonNode res = connectorRpc.post(MEMBERS_MANAGER, "getMemberByUser", map);
        Member member = RpcMapper.mapMember(res);
        log.trace("getMemberByUser({}, {}) returns {}", user, vo, member);
        return member;
    }

    @Override
    public User getPerunUser(Long idpEntityId, List<String> uids) {
        if (!this.connectorRpc.isEnabled()) {
            return null;
        }
        log.trace("getPerunUser({}, {})", idpEntityId, uids);
        User user = null;
        for (String uid : uids) {
            try {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("extSourceName", idpEntityId);
                map.put("extLogin", uid);
                JsonNode res = connectorRpc.post(USERS_MANAGER, "getUserByExtSourceNameAndExtLogin", map);
                user = RpcMapper.mapPerunUser(res);
                return user;
            } catch(Exception e) {
                continue; //needs to be done differently
            }
        }
        log.trace("getPerunUser({}, {}) returns {}", idpEntityId, uids, user);
        return user;

    }

    @Override
    public List<Group> getMemberGroups(Long userId, Long voId) {
        if (!this.connectorRpc.isEnabled()) {
            return new ArrayList<>();
        }
        log.trace("getMemberGroups({}, {})", userId, voId);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("user", userId);

        JsonNode res = connectorRpc.post(MEMBERS_MANAGER, "getMemberByUser", map);

        Member member = RpcMapper.mapMember(res);

        Map<String, Object> map_groups = new LinkedHashMap<>();
        map_groups.put("member", member.getId());
        JsonNode res_groups = connectorRpc.post(GROUPS_MANAGER, "getAllMemberGroups", map_groups);
        List<Group> allGroups = RpcMapper.mapGroups(res_groups);

        List<Group> voGroups = allGroups.stream().filter(group -> voId.equals(group.getVoId()))
                .collect(Collectors.toList());

        log.trace("getMemberGroups({}, {}) returns {}", userId, voId, voGroups);
        return voGroups;
    }

    @Override
    public List<Group> getSpGroups(String spEntityId) {
        return null;
    }

    @Override
    public Group getGroupByName(Long voId, String name) {
        if (!this.connectorRpc.isEnabled()) {
            return null;
        }

        log.trace("getGroupByName({}, {})", voId, name);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("vo", voId);
        map.put("name", name);

        JsonNode res = connectorRpc.post(GROUPS_MANAGER, "getGroupByName", map);
        Group group = RpcMapper.mapGroup(res);
        log.trace("getGroupByName({}, {}) returns {}", voId, name, group);
        return group;
    }

    @Override
    public Vo getVoByShortName(String voShortName) {
        if (!this.connectorRpc.isEnabled()) {
            return null;
        }

        log.trace("getVoByShortName({})", voShortName);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("shortName", voShortName);

        JsonNode res = connectorRpc.post(VOS_MANAGER, "getVoByShortName", map);
        Vo vo = RpcMapper.mapVo(res);
        log.trace("getVoByShortName({}) returns {}", voShortName, vo);
        return vo;
    }

    @Override
    public Vo getVoById(Long voId) {

        if (!this.connectorRpc.isEnabled()) {
            return null;
        }

        log.trace("getVoById({})",voId);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", voId);

        JsonNode res = connectorRpc.post(VOS_MANAGER, "getVoById", map);
        Vo vo = RpcMapper.mapVo(res);
        log.trace("getVoById({}) returns {}", voId, vo);
        return vo;
    }

    @Override
    public Map<String, PerunAttributeValue> getAttributesValues(Entity entity, Long entityId, List<String> attributes) {
        if (!this.connectorRpc.isEnabled()) {
            return new HashMap<>();
        }

        log.trace("getAttributesValues({}, {}, {})", entity, entityId, attributes);
        Map<String, PerunAttribute> userAttributes = this.getAttributes(entity, entityId, attributes);
        Map<String, PerunAttributeValue> valueMap = extractValues(userAttributes);

        log.trace("getAttributesValues({}, {}, {}) returns: {}", entity, entityId, attributes, valueMap);
        return valueMap;
    }

    @Override
    public List<Facility> getFacilitiesByAttribute(String name, String attrValue) {
        log.trace("getFacilitiesByAttribute({}, {})", name, attrValue);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("attributeName", name);
        map.put("attributeValue", attrValue);
        JsonNode res = connectorRpc.post(FACILITIES_MANAGER, "getFacilitiesByAttribute", map);
        List<Facility> facilities = RpcMapper.mapFacilities(res);
        log.trace("getFacilitiesByAttribute({}, {}) returns {}", name, attrValue, facilities);
        return facilities;
    }

    @Override
    public List<Group> getUsersGroupsOnFacility(Long facilityId, Long userId) {

        if (!this.connectorRpc.isEnabled()) {
            return new ArrayList<>();
        }
        log.trace("getUsersGroupsOnFacility({}, {})", facilityId, userId);
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("user", userId);
        map.put("facility", facilityId);

        JsonNode res = connectorRpc.post(USERS_MANAGER, "getGroupsWhereUserIsActive", map);
        List<Group> groups = RpcMapper.mapGroups(res);
        log.trace("getUsersGroupsOnFacility({}, {}) returns {}", facilityId, userId, groups);
        return groups;

    }

    @Override
    public List<Facility> searchFacilitiesByAttributeValue(PerunAttribute attribute) {

        if (!this.connectorRpc.isEnabled()) {
            return new ArrayList<>();
        }
        log.trace("searchFacilitiesByAttributeValue({})", attribute);
        Map<String, Object> map = new LinkedHashMap<>();
        Map<String, String> attributeValue = new LinkedHashMap<>();
        attributeValue.put(attribute.getType(), attribute.getValue().toString());
        map.put("attributesWithSearchingValues", attributeValue);

        JsonNode res = connectorRpc.post(SEARCHER, "getFacilities", map);
        List<Facility> facilities = RpcMapper.mapFacilities(res);
        log.trace("searchFacilitiesByAttributeValue({}) returns {}", attribute, facilities);
        return facilities;

    }

    private Map<String, PerunAttributeValue> extractValues(Map<String, PerunAttribute> attributeMap) {
        if (!this.connectorRpc.isEnabled()) {
            return new HashMap<>();
        }

        Map<String, PerunAttributeValue> resultMap = new LinkedHashMap<>();
        for (Map.Entry<String, PerunAttribute> attrPair: attributeMap.entrySet()) {
            String attrName = attrPair.getKey();
            PerunAttribute attr = attrPair.getValue();
            resultMap.put(attrName, attr.getValue());
        }

        return resultMap;
    }

}
