package cz.muni.ics.perunproxyapi.application.facade.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.google.common.net.UrlEscapers;
import cz.muni.ics.perunproxyapi.application.facade.FacadeUtils;
import cz.muni.ics.perunproxyapi.application.facade.RelyingPartyFacade;
import cz.muni.ics.perunproxyapi.application.facade.configuration.FacadeConfiguration;
import cz.muni.ics.perunproxyapi.application.service.ProxyUserService;
import cz.muni.ics.perunproxyapi.application.service.RelyingPartyService;
import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.adapters.impl.AdaptersContainer;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class RelyingPartyFacadeImpl implements RelyingPartyFacade {

    private final Map<String, JsonNode> methodConfigurations;
    private final AdaptersContainer adaptersContainer;
    private final ProxyUserService proxyUserService;
    private final RelyingPartyService relyingPartyService;
    private final String defaultIdpIdentifier;

    public static final String GET_ENTITLEMENTS = "get_entitlements";

    @Autowired
    public RelyingPartyFacadeImpl(@NonNull ProxyUserService proxyUserService,
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
    public List<String> getEntitlements(Long facilityId, Long userId) throws PerunUnknownException, PerunConnectionException {
        JsonNode options = methodConfigurations.getOrDefault(GET_ENTITLEMENTS, JsonNodeFactory.instance.nullNode());
        DataAdapter adapter = FacadeUtils.getAdapter(adaptersContainer, options);
        List<String> result = new ArrayList<>();
        List<Group> groups = proxyUserService.getUsersGroupsOnFacility(adapter, facilityId, userId);
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> eduPersonEntitlement = getGroupEntitlements(groups);
        List<String> capabilities = getCapabilities(adapter, facilityId, groups);
        List<String> forwardedEduPersonEntitlement = relyingPartyService.getForwardedEntitlement(adapter, userId)
                .valueAsList();

        result.addAll(eduPersonEntitlement);
        result.addAll(capabilities);
        result.addAll(forwardedEduPersonEntitlement);
        return result;
    }

    private List<String> getCapabilities(DataAdapter preferredAdapter, Long facilityId, List<Group> groups) throws PerunUnknownException, PerunConnectionException {
        List<String> capabilities = new ArrayList<>();
        capabilities.addAll(relyingPartyService.getResourceCapabilities(preferredAdapter, facilityId, groups));
        if (!groups.isEmpty()) {
            capabilities.addAll(relyingPartyService.getFacilityCapabilities(preferredAdapter, facilityId));
        }
        for (int i = 0; i < capabilities.size(); i++) {
            capabilities.set(i, wrapCapabilityToAARC(capabilities.get(i)));
        }

        return capabilities;
    }

    private List<String> getGroupEntitlements(List<Group> groups) {
        List<String> eduPersonEntitlement = new ArrayList<>();
        if (FacadeConfiguration.prefix.trim().isEmpty() || FacadeConfiguration.authority.trim().isEmpty()) {
            throw new RuntimeException("Missing mandatory configuration options 'prefix' or 'authority'."); //what exception to throw
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

    private String wrapCapabilityToAARC(String capability) {
        return FacadeConfiguration.prefix + UrlEscapers.urlPathSegmentEscaper().escape(capability) + "#" + FacadeConfiguration.authority;
    }
}
