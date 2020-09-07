package cz.muni.ics.perunproxyapi.application.service.impl;

import cz.muni.ics.perunproxyapi.application.service.RelyingPartyService;
import cz.muni.ics.perunproxyapi.application.service.ServiceUtils;
import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RelyingPartyServiceImpl implements RelyingPartyService {

    @Override
    public List<String> getEntitlements(@NonNull DataAdapter adapter, @NonNull Long facilityId,
                                        @NonNull Long userId, @NonNull String prefix, @NonNull String authority,
                                        String forwardedEntitlementsAttrIdentifier,
                                        String resourceCapabilitiesAttrIdentifier,
                                        String facilityCapabilitiesAttrIdentifier)
            throws PerunUnknownException, PerunConnectionException
    {
        List<String> entitlements = new ArrayList<>(
                adapter.getForwardedEntitlements(userId, forwardedEntitlementsAttrIdentifier)
        );

        List<Group> groups = adapter.getUsersGroupsOnFacility(facilityId, userId);
        if (groups == null || groups.isEmpty()) {
            return entitlements;
        }

        List<String> groupEntitlements = ServiceUtils.wrapGroupEntitlements(groups, prefix, authority);
        entitlements.addAll(groupEntitlements);

        List<String> capabilities = adapter.getCapabilities(facilityId, userId, groups,
                resourceCapabilitiesAttrIdentifier, facilityCapabilitiesAttrIdentifier);
        if (capabilities != null && !capabilities.isEmpty()) {
            entitlements.addAll(capabilities.stream()
                    .map(cap -> ServiceUtils.wrapCapabilityToAARC(cap, prefix, authority))
                    .collect(Collectors.toSet())
            );
        }

        return entitlements;
    }

    @Override
    public Facility getFacilityByIdentifier(@NonNull DataAdapter adapter, @NonNull String rpIdentifier)
            throws PerunUnknownException, PerunConnectionException
    {
        return adapter.getFacilityByRpIdentifier(rpIdentifier);
    }

}
