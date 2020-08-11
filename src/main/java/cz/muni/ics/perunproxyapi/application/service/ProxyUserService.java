package cz.muni.ics.perunproxyapi.application.service;

import cz.muni.ics.perunproxyapi.persistence.adapters.DataAdapter;
import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import cz.muni.ics.perunproxyapi.persistence.models.User;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Service layer for user related things. Purpose of this class is to execute correct methods on the given adapter.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 * @author Pavol Pluta <pavol.pluta1@gmail.com>
 */
public interface ProxyUserService {

    /**
     * Find user by identifiers via given adapter.
     *
     * @param preferredAdapter Adapter to be used.
     * @param idpEntityId Identifier of source Identity Provider.
     * @param userIdentifiers List of users identifiers.
     * @return User or null.
     * @throws PerunUnknownException Thrown as wrapper of unknown exception thrown by Perun interface.
     * @throws PerunConnectionException Thrown when problem with connection to Perun interface occurs.
     */
    User findByExtLogins(DataAdapter preferredAdapter, String idpEntityId, List<String> userIdentifiers)
            throws PerunUnknownException, PerunConnectionException;

    /**
<<<<<<< HEAD
     * Get user by given IdP identifier and attribute.
     *
     * @param preferredAdapter Adapter to be used.
     * @param idpIdentifier Identifier of source Identity Provider.
     * @param login User login attribute.
     * @return User or null.
     * @throws PerunUnknownException Thrown as wrapper of unknown exception thrown by Perun interface.
     * @throws PerunConnectionException Thrown when problem with connection to Perun interface occurs.
     */
    User findByExtLogin(DataAdapter preferredAdapter, String idpIdentifier, String login)
            throws PerunUnknownException, PerunConnectionException;

    /**
     * Get attribute values for a given entity.
     *
     * @param preferredAdapter Adapter to be used.
     * @param entity Entity.
     * @param id Entity id.
     * @param attributes Attributes.
     * @return Map of attribute values for a given entity.
     * @throws PerunUnknownException Thrown as wrapper of unknown exception thrown by Perun interface.
     * @throws PerunConnectionException Thrown when problem with connection to Perun interface occurs.
     */
    Map<String, PerunAttributeValue> getAttributesValues(DataAdapter preferredAdapter, Entity entity,
                                                         long id, List<String> attributes)
            throws PerunUnknownException, PerunConnectionException;

    /**
     * Get user by id.
     * @param preferredAdapter Adapter for connection to be used.
     * @param userId Id of a Perun user.
     * @return User or null
     * @throws PerunUnknownException Thrown as wrapper of unknown exception thrown by Perun interface.
     * @throws PerunConnectionException Thrown when problem with connection to Perun interface occurs.
     */
    User findByPerunUserId(DataAdapter preferredAdapter, Long userId) throws PerunUnknownException, PerunConnectionException;
    
    /**
     * Get groups the user is member of in particular VO.
     *
     * @param userId ID of the USER.
     * @param voId ID of the VO.
     * @return Groups from VO that the user is a member of. Including VO members group.
     */
    List<Group> getUserGroupsInVo(DataAdapter preferredAdapter, @NonNull Long userId, @NonNull Long voId) throws PerunUnknownException, PerunConnectionException;

    /**
     *
     * @param facilityId
     * @param userId
     * @return
     */
    List<Group> getUsersGroupsOnFacility(DataAdapter preferredAdapter, Long facilityId, Long userId) throws PerunUnknownException, PerunConnectionException;



}
