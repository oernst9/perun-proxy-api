package cz.muni.ics.perunproxyapi.application.facade.impl;

import com.fasterxml.jackson.databind.JsonNode;

import cz.muni.ics.perunproxyapi.application.facade.FacadeUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.net.UrlEscapers;
import cz.muni.ics.perunproxyapi.application.service.RelyingPartyService;
import cz.muni.ics.perunproxyapi.persistence.adapters.impl.AdaptersContainer;

import cz.muni.ics.perunproxyapi.application.facade.ProxyuserFacade;
import cz.muni.ics.perunproxyapi.application.facade.configuration.FacadeConfiguration;
import cz.muni.ics.perunproxyapi.application.service.ProxyUserService;
import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;

import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;

import cz.muni.ics.perunproxyapi.persistence.models.Group;

import cz.muni.ics.perunproxyapi.persistence.models.User;
import cz.muni.ics.perunproxyapi.presentation.DTOModels.UserDTO;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.ArrayList;
import java.util.Collections;




@Component
@Slf4j
public class ProxyuserFacadeImpl implements ProxyuserFacade {

    private final Map<String, JsonNode> methodConfigurations;
    private final AdaptersContainer adaptersContainer;

    private final ProxyUserService proxyUserService;
    private final RelyingPartyService relyingPartyService;

    private final String defaultIdpIdentifier;

    public static final String FIND_BY_EXT_LOGINS = "find_by_ext_logins";
    public static final String GET_USER_BY_LOGIN = "get_user_by_login";
    public static final String FIND_BY_PERUN_USER_ID = "find_by_perun_user_id";
    public static final String GET_ALL_ENTITLEMENTS = "get_all_entitlements";

    public static final String IDP_IDENTIFIER = "idpIdentifier";

    @Autowired
    public ProxyuserFacadeImpl(@NonNull ProxyUserService proxyUserService,
                               @NonNull AdaptersContainer adaptersContainer,
                               @NonNull FacadeConfiguration facadeConfiguration,
                               @NonNull RelyingPartyService relyingPartyService,
                               @Value("${facade.default_idp}") String defaultIdp) {
        this.proxyUserService = proxyUserService;
        this.adaptersContainer = adaptersContainer;
        this.methodConfigurations = facadeConfiguration.getProxyUserAdapterMethodConfigurations();
        this.relyingPartyService = relyingPartyService;
        this.defaultIdpIdentifier = defaultIdp;
    }

    @Override
    public User findByExtLogins(String idpIdentifier, List<String> userIdentifiers) throws PerunUnknownException, PerunConnectionException {
        JsonNode options = FacadeUtils.getOptions(FIND_BY_EXT_LOGINS, methodConfigurations);
        DataAdapter adapter = FacadeUtils.getAdapter(adaptersContainer, options);

        log.debug("Calling proxyUserService.findByExtLogins on adapter {}", adapter.getClass());

        return proxyUserService.findByExtLogins(adapter, idpIdentifier, userIdentifiers);
    }

    @Override
    public UserDTO getUserByLogin(String login, List<String> fields) throws PerunUnknownException, PerunConnectionException {
        JsonNode options = FacadeUtils.getOptions(GET_USER_BY_LOGIN, methodConfigurations);
        DataAdapter adapter = FacadeUtils.getAdapter(adaptersContainer, options);
        String idpIdentifier = options.has(IDP_IDENTIFIER) ? options.get(IDP_IDENTIFIER).asText() : defaultIdpIdentifier;

        User user = proxyUserService.findByExtLogin(adapter, idpIdentifier , login);
        UserDTO userDTO = null;

        if (user != null) {
            userDTO = new UserDTO(
                    login,
                    user.getFirstName(),
                    user.getLastName(),
                    String.format("%s %s", user.getFirstName(), user.getLastName()),
                    user.getId(),
                    new HashMap<>()
            );

            if (fields != null && !fields.isEmpty()){
                Map<String, PerunAttributeValue> attributeValues =
                        proxyUserService.getAttributesValues(adapter, Entity.USER , user.getId() , fields);
                userDTO.setPerunAttributes(attributeValues);
            }
        }

        return userDTO;
    }

    @Override
    public User findByPerunUserId(Long userId) throws PerunUnknownException, PerunConnectionException {
        JsonNode options = FacadeUtils.getOptions(FIND_BY_PERUN_USER_ID, methodConfigurations);
        DataAdapter adapter = FacadeUtils.getAdapter(adaptersContainer, options);

        log.debug("Calling proxyUserService.findByPerunUserId on adapter {}", adapter.getClass());

        return proxyUserService.findByPerunUserId(adapter, userId);
    }

    @Override
    public List<String> getAllEntitlements(Long userId) throws PerunUnknownException, PerunConnectionException {
        JsonNode options = methodConfigurations.getOrDefault(GET_ALL_ENTITLEMENTS, JsonNodeFactory.instance.nullNode());
        DataAdapter adapter = FacadeUtils.getAdapter(adaptersContainer, options);
        List<String> result = new ArrayList<>();
        List<Group> groups = proxyUserService.getUserGroupsInVo(adapter, userId, null);
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> eduPersonEntitlement = getGroupEntitlements(groups);
        List<String> forwardedEduPersonEntitlement = relyingPartyService.getForwardedEntitlement(adapter, userId)
                .valueAsList();

        result.addAll(eduPersonEntitlement);
        result.addAll(forwardedEduPersonEntitlement);
        return result;
    }

    private List<String> getGroupEntitlements(List<Group> groups) {
        List<String> eduPersonEntitlement = new ArrayList<>();
        if (FacadeConfiguration.prefix.trim().isEmpty() || FacadeConfiguration.authority.trim().isEmpty()) {
            throw new RuntimeException("Missing mandatory configuration options 'prefix' or 'authority'.");
        }
        for (Group group : groups) {
            String groupName = group.getUniqueGroupName();
            groupName = groupName.replaceAll("/^(\\w*)\\:members$/", "$1");
            groupName = wrapGroupNameToAARC(groupName);
            eduPersonEntitlement.add(groupName);
        }
        Collections.sort(eduPersonEntitlement);
        return eduPersonEntitlement;
    }

    private String wrapGroupNameToAARC(String groupName) {
        return FacadeConfiguration.prefix + "group:" + UrlEscapers.urlPathSegmentEscaper().escape(groupName) + "#" + FacadeConfiguration.authority;
    }
}
