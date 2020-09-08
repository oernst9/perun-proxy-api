package cz.muni.ics.perunproxyapi.application.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import cz.muni.ics.perunproxyapi.application.facade.FacadeUtils;
import cz.muni.ics.perunproxyapi.application.service.ProxyUserService;
import cz.muni.ics.perunproxyapi.application.service.ServiceUtils;
import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.adapters.FullAdapter;
import cz.muni.ics.perunproxyapi.persistence.adapters.impl.rpc.RpcMapper;
import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.exceptions.InternalErrorException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttribute;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import cz.muni.ics.perunproxyapi.persistence.models.User;
import cz.muni.ics.perunproxyapi.persistence.models.UserExtSource;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class ProxyUserServiceImpl implements ProxyUserService {

    @Override
    public User findByExtLogins(@NonNull DataAdapter preferredAdapter, @NonNull String idpIdentifier,
                                @NonNull List<String> userIdentifiers)
            throws PerunUnknownException, PerunConnectionException
    {
        if (!StringUtils.hasText(idpIdentifier)) {
            throw new IllegalArgumentException("IdP identifier cannot be empty");
        } else if (userIdentifiers.isEmpty()) {
            throw new IllegalArgumentException("User identifiers cannot be empty");
        }
        return preferredAdapter.getPerunUser(idpIdentifier, userIdentifiers);
    }

    @Override
    public User findByExtLogin(@NonNull DataAdapter preferredAdapter, @NonNull String idpIdentifier,
                               @NonNull String login)
            throws PerunUnknownException, PerunConnectionException
    {
        if (!StringUtils.hasText(idpIdentifier)) {
            throw new IllegalArgumentException("IdP Identifier cannot be empty");
        } else if (!StringUtils.hasText(login)) {
            throw new IllegalArgumentException("Login cannot be empty");
        }
        return findByExtLogins(preferredAdapter, idpIdentifier, Collections.singletonList(login));
    }

    @Override
    public Map<String, PerunAttributeValue> getAttributesValues(@NonNull DataAdapter preferredAdapter,
                                                                @NonNull Entity entity, long id,
                                                                List<String> attributes)
            throws PerunUnknownException, PerunConnectionException
    {
        return preferredAdapter.getAttributesValues(entity, id, attributes);
    }

    @Override
    public User findByPerunUserId(@NonNull DataAdapter preferredAdapter, @NonNull Long userId)
            throws PerunUnknownException, PerunConnectionException {
        return preferredAdapter.findPerunUserById(userId);
    }

    @Override
    public List<String> getAllEntitlements(@NonNull DataAdapter adapter, @NonNull Long userId,
                                           @NonNull String prefix, @NonNull String authority,
                                           String forwardedEntitlementsAttrIdentifier)
            throws PerunUnknownException, PerunConnectionException
    {

        List<String> forwardedEntitlements = adapter.getForwardedEntitlements(userId,
                forwardedEntitlementsAttrIdentifier);
        List<String> entitlements = new ArrayList<>(forwardedEntitlements);

        List<Group> groups = adapter.getUserGroups(userId);
        if (groups != null && !groups.isEmpty()) {
            List<String> eduPersonEntitlement = ServiceUtils.wrapGroupEntitlements(groups, prefix, authority);
            entitlements.addAll(eduPersonEntitlement);
        }

        return entitlements;
    }

    @Override
    public User getUserWithAttributesByLogin(@NonNull DataAdapter preferredAdapter, @NonNull String login,
                                             List<String> attrIdentifiers)
            throws PerunUnknownException, PerunConnectionException
    {
        if (!StringUtils.hasText(login)) {
            throw new IllegalArgumentException("User login cannot be empty");
        }
        return preferredAdapter.getUserWithAttributesByLogin(login, attrIdentifiers);
    }

    @Override
    public User getUserByLogin(@NonNull DataAdapter preferredAdapter, @NonNull String login)
            throws PerunUnknownException, PerunConnectionException
    {
        if (!StringUtils.hasText(login)) {
            throw new IllegalArgumentException("User login cannot be empty");
        }
        return this.getUserWithAttributesByLogin(preferredAdapter, login, new ArrayList<>());
    }

    @Override
    public User findByIdentifiers(@NonNull DataAdapter adapter,
                                  @NonNull String idpIdentifier,
                                  @NonNull List<String> identifiers,
                                  @NonNull List<String> attrIdentifiers)
    {
        if (!StringUtils.hasText(idpIdentifier)) {
            throw new IllegalArgumentException("IdP Identifier cannot be empty");
        } else if (identifiers.isEmpty()) {
            throw new IllegalArgumentException("Identifiers cannot be empty");
        }
        return adapter.findByIdentifiers(idpIdentifier, identifiers, attrIdentifiers);
    }

    public User getUserByLogin(@NonNull DataAdapter preferredAdapter,
                               @NonNull String loginAttrIdentifier,
                               @NonNull String login)
            throws PerunUnknownException, PerunConnectionException {
        return this.getUserWithAttributesByLogin(preferredAdapter, login, Collections.singletonList(loginAttrIdentifier));
    }

    @Override
    public UserExtSource getUserExtSource(@NonNull FullAdapter adapter, @NonNull String extSourceName, @NonNull String extSourceLogin) throws PerunUnknownException, PerunConnectionException {
        return adapter.getUserExtSource(extSourceName, extSourceLogin);

    }

    @Override
    public List<UserExtSource> getUserExtSources(@NonNull FullAdapter adapter, @NonNull Long userId) throws PerunUnknownException, PerunConnectionException {
        return adapter.getUserExtSources(userId);
    }

    @Override
    public boolean updateUserIdentityAttributes(@NonNull String login, @NonNull String identityId,
                                                @NonNull Map<String, JsonNode> requestAttributesMap,
                                                @NonNull FullAdapter adapter, @NonNull Map<String, String> attributeMapper,
                                                @NonNull List<String> attributesToFindUes)
            throws PerunUnknownException, PerunConnectionException
    {

        Map<String, PerunAttribute> attributesToChangeMap = new HashMap<>();
        Map<String, JsonNode> requestAttributesWithPerunKeysMap = new HashMap<>();

        for (Map.Entry<String, JsonNode> entry : requestAttributesMap.entrySet()) {
            String newKey = attributeMapper.get(entry.getKey());
            attributesToChangeMap.put(newKey, RpcMapper.mapAttribute(entry.getValue()));
            requestAttributesWithPerunKeysMap.put(newKey, requestAttributesMap.get(entry.getKey()));
        }

        UserExtSource ues = getUserExtSourceUsingIdentityId(adapter, attributesToFindUes, login, attributesToChangeMap, identityId);
        Map<String, PerunAttribute> attributesFromPerunMap = adapter.getAttributes(Entity.USER_EXT_SOURCE, ues.getId(), new ArrayList<>(requestAttributesWithPerunKeysMap.keySet()));

        List<PerunAttribute> attributesToUpdateList = getAttributesToUpdate(attributesFromPerunMap, requestAttributesWithPerunKeysMap);
        boolean attributesUpdated = false;
        if (!attributesToUpdateList.isEmpty()) {
            convertListValuesToStringValues(attributesToUpdateList);
            attributesUpdated = adapter.setAttributes(Entity.USER_EXT_SOURCE, ues.getId(), attributesToUpdateList);
        }
        boolean lastAcessUpdated = adapter.updateUserExtSourceLastAccess(ues);
        return attributesUpdated && lastAcessUpdated;
    }

    private UserExtSource getUserExtSourceUsingIdentityId(FullAdapter adapter, List<String> attributesToFindUes, String login, Map<String, PerunAttribute> attributesToChangeMap, String identityId) throws PerunUnknownException, PerunConnectionException {
        User user = getUserByLogin(adapter, login);
        List<UserExtSource> uesesList = getUserExtSources(adapter, user.getPerunId());
        List<UserExtSource> uesesWithIdentityIdList = uesesList.stream()
                .filter(x -> Objects.equals(identityId, x.getExtSource().getName())).collect(Collectors.toList());

        if (uesesWithIdentityIdList.isEmpty()) {
            throw new InternalErrorException("uesesWithIdentityIdList is empty. No UserExtSource with the identityId found for the user");
        }
        UserExtSource ues;
        List<UserExtSource> uesesWithLogin = uesesWithIdentityIdList.stream().filter(x -> Objects.equals(login, x.getLogin())).collect(Collectors.toList());
        if (uesesWithLogin.size() != 1) {
            List<UserExtSource> uesesByAttributes = new ArrayList<>();
            for (UserExtSource uesWithLogin : uesesWithIdentityIdList) {
                Map<String, PerunAttributeValue> uesAttributes = getAttributesValues(adapter, Entity.USER_EXT_SOURCE, uesWithLogin.getId(), attributesToFindUes);
                List<String> equalAttributes = attributesToFindUes.stream()
                        .filter(x -> Objects.equals(attributesToChangeMap.get(x), uesAttributes.get(x)))
                        .collect(Collectors.toList());
                if (!equalAttributes.isEmpty()) {
                    uesesByAttributes.add(uesWithLogin);
                }
            }
            if (uesesByAttributes.size() != 1) {
                throw new InternalErrorException("There is no UserExtSource or more than one.");
            }
            ues = uesesByAttributes.get(0);
        } else {
            ues = uesesWithLogin.get(0);
        }
        return ues;
    }

    private List<PerunAttribute> convertListValuesToStringValues(List<PerunAttribute> attributesList) {
        JsonNodeFactory jsonNodeFactory = JsonNodeFactory.instance;
        for (PerunAttribute perunAttribute : attributesList) {

            if (PerunAttributeValue.ARRAY_TYPE.equals(perunAttribute.getType())) {
                StringBuilder arrayAsString = new StringBuilder();
                List<String> valueList = perunAttribute.valueAsList();
                for (String part : valueList) {
                    arrayAsString.append(part).append(";");
                }
                perunAttribute.setValue(PerunAttributeValue.STRING_TYPE, jsonNodeFactory.textNode(arrayAsString.toString()));
            }

        }
        return attributesList;
    }

    private List<PerunAttribute> getAttributesToUpdate(Map<String, PerunAttribute> attributesFromPerunMap, Map<String, JsonNode> requestAttributesWithPerunKeysMap) {
        List<PerunAttribute> attributesToUpdateList = new ArrayList<>();
        for (Map.Entry<String, PerunAttribute> perunAttributeEntry : attributesFromPerunMap.entrySet()) {
            PerunAttribute perunAttribute = perunAttributeEntry.getValue();
            String perunAttributeKey = perunAttributeEntry.getKey();

            JsonNode value = requestAttributesWithPerunKeysMap.get(perunAttributeKey);
            if (!Objects.equals(perunAttribute.getValue(), value)) {
                perunAttribute.setValue(perunAttribute.getType(), value);
                attributesToUpdateList.add(perunAttribute);
            }
        }
        return attributesToUpdateList;
    }

}
