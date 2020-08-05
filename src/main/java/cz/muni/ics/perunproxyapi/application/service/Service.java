package cz.muni.ics.perunproxyapi.application.service;

import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.adapters.FullAdapter;
import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import cz.muni.ics.perunproxyapi.persistence.models.User;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.muni.ics.perunproxyapi.persistence.adapters.impl.ldap.PerunAdapterLdapConstants.CAPABILITIES;

@Slf4j
public class Service {

    @Autowired
    DataAdapter dataAdapter;

    @Autowired
    FullAdapter fullAdapter;


    public List<Group> getUsersGroupsOnFacility(Long facilityId, Long userId) {
        return dataAdapter.getUsersGroupsOnFacility(facilityId, userId);
    }

    public Set<String> getResourceCapabilities(Long facilityId, List<Group> userGroups) {
        return dataAdapter.getResourceCapabilities(facilityId, userGroups);
    }

    public Set<String> getFacilityCapabilities(Long facilityId) {
        log.trace("getFacilityCapabilities({})", facilityId);

        Set<String> result = new HashSet<>();
        PerunAttributeValue attrVal = dataAdapter.getAttributesValues(Entity.FACILITY, facilityId,
                Collections.singletonList(CAPABILITIES)).getOrDefault(CAPABILITIES, PerunAttributeValue.NULL);
        if (PerunAttributeValue.NULL.equals(attrVal) && attrVal.valueAsList() != null) {
            result = new HashSet<>(attrVal.valueAsList());
        }

        log.trace("getFacilityCapabilities({}) returns: {}", facilityId, result);
        return result;
    }

    public PerunAttributeValue getForwardedEntitlement(Long userId) {
        if (userId == null) {
            return null;
        }
        PerunAttributeValue forwardedEduPersonEntitlement = dataAdapter
                .getAttributesValues(Entity.USER, userId, Arrays.asList("forwardedEduPersonEntitlement"))
                .getOrDefault("forwardedEduPersonEntitlement", PerunAttributeValue.NULL);
        return forwardedEduPersonEntitlement;
    }

    public List<Facility> getFacilitiesByAttribute(@NonNull String attributeName, @NonNull String attrValue) {
        return dataAdapter.getFacilitiesByAttribute(attributeName, attrValue);
    }

}
