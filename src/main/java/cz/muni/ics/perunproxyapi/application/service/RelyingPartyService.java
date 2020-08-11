package cz.muni.ics.perunproxyapi.application.service;

import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;

import java.util.List;
import java.util.Set;

public interface RelyingPartyService {

    /**
     *
     * @param facilityId
     * @param userGroups
     * @return
     */
    Set<String> getResourceCapabilities(DataAdapter preferredAdapter, Long facilityId, List<Group> userGroups) throws PerunUnknownException, PerunConnectionException;

    /**
     *
     * @param facilityId
     * @return
     */
    Set<String> getFacilityCapabilities(DataAdapter preferredAdapter, Long facilityId) throws PerunUnknownException, PerunConnectionException;
    /**
     *
     * @param attributeName
     * @param attrValue
     * @return
     */
    List<Facility> getFacilitiesByAttribute(DataAdapter preferredAdapter, String attributeName, String attrValue) throws PerunUnknownException, PerunConnectionException;

    /**
     *
     * @param userId
     * @return
     */
    PerunAttributeValue getForwardedEntitlement(DataAdapter preferredAdapter, Long userId) throws PerunUnknownException, PerunConnectionException;

}
