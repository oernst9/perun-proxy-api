package cz.muni.ics.perunproxyapi.application.facade;

import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;

import java.util.List;

public interface RelyingPartyFacade {

    /**
     *
     * @param facilityId
     * @param userId
     * @return
     */
    List<String> getEntitlements(Long facilityId, Long userId) throws PerunUnknownException, PerunConnectionException;
}
