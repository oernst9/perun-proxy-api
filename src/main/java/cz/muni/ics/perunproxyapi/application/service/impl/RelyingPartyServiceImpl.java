package cz.muni.ics.perunproxyapi.application.service.impl;

import cz.muni.ics.perunproxyapi.application.service.RelyingPartyService;
import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.muni.ics.perunproxyapi.persistence.adapters.impl.ldap.PerunAdapterLdapConstants.CAPABILITIES;

@Component
public class RelyingPartyServiceImpl implements RelyingPartyService {

    @Override
    public Set<String> getResourceCapabilities(DataAdapter preferredAdapter, Long facilityId, List<Group> userGroups) throws PerunUnknownException, PerunConnectionException {
        return preferredAdapter.getResourceCapabilities(facilityId, userGroups);
    }

    @Override
    public Set<String> getFacilityCapabilities(DataAdapter preferredAdapter, Long facilityId) throws PerunUnknownException, PerunConnectionException {
        Set<String> result = new HashSet<>();
        PerunAttributeValue attrVal = preferredAdapter.getAttributesValues(Entity.FACILITY, facilityId,
                Collections.singletonList(CAPABILITIES)).get(CAPABILITIES);
        if (attrVal.valueAsList() != null) {
            result = new HashSet<>(attrVal.valueAsList());
        }
        return result;
    }

    @Override
    public PerunAttributeValue getForwardedEntitlement(DataAdapter preferredAdapter, Long userId) throws PerunUnknownException, PerunConnectionException {
        if (userId == null) {
            return null;
        }
        PerunAttributeValue forwardedEduPersonEntitlement = preferredAdapter
                .getAttributesValues(Entity.USER, userId, Collections.singletonList("forwardedEduPersonEntitlement"))
                .get("forwardedEduPersonEntitlement");
        return forwardedEduPersonEntitlement;
    }

    @Override
    public List<Facility> getFacilitiesByAttribute(DataAdapter preferredAdapter, @NonNull String attributeName, @NonNull String attrValue) throws PerunUnknownException, PerunConnectionException {
        return preferredAdapter.getFacilitiesByAttribute(attributeName, attrValue);
    }
}
