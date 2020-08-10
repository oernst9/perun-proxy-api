package cz.muni.ics.perunproxyapi.persistence.adapters;


import cz.muni.ics.perunproxyapi.persistence.enums.Entity;
import cz.muni.ics.perunproxyapi.persistence.models.Facility;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttribute;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import cz.muni.ics.perunproxyapi.persistence.models.User;
import cz.muni.ics.perunproxyapi.persistence.models.Vo;
import lombok.NonNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Methods for fetching data.
 */
public interface DataAdapter {

    /**
     * Get user from Perun.
     * @param idpEntityId EntityID of the ExtSource.
     * @param uids List of user identifiers received from remote idp used as userExtSourceLogin.
     * @return User or null.
     */
    User getPerunUser(@NonNull String idpEntityId, @NonNull List<String> uids);

    /**
     * Get groups the user is member of in particular VO.
     *
     * @param userId ID of the USER.
     * @param voId ID of the VO.
     * @return Groups from VO that the user is a member of. Including VO members group.
     */
    List<Group> getUserGroupsInVo(@NonNull Long userId, @NonNull Long voId);

    /**
     * Get groups with access to the SP.
     * @param spIdentifier Identifier of the SP..
     * @return List of groups assigned to all facilities with given identifier.
     */
    List<Group> getSpGroups(@NonNull String spIdentifier);

    /**
     * Get group with given name from specified VO.
     * @param voId ID of the VO.
     * @param name group name. Note that name of group is without VO name prefix.
     * @return Group or null.
     */
    Group getGroupByName(@NonNull Long voId, @NonNull String name);

    /**
     * Get VO by shortName.
     * @param shortName Short name of the VO to be found.
     * @return VO or null
     */
    Vo getVoByShortName(@NonNull String shortName);

    /**
     * Get VO by ID from Perun.
     * @param id ID of the VO.
     * @return VO or null.
     */
    Vo getVoById(@NonNull Long id);

    /**
     * Get values of attributes for given entity.
     * @param entity Entity enumeration value. Specifies Perun entity.
     * @param entityId ID of the entity in Perun.
     * @param attributes List of attribute names. Specifies what attributes we want to fetch.
     * @return Map<String, PerunAttributeValue>, key is identifier of the attribute, value is the value of the attribute.
     */
    Map<String, PerunAttributeValue> getAttributesValues(@NonNull Entity entity,
                                                         @NonNull Long entityId,
                                                         @NonNull List<String> attributes);

    /**
     * Get Facilities that have specified attribute with given value.
     * @param attributeName Identifier of the attribute.
     * @param attrValue Value of the attribute as String.
     * @return List of facilities.
     */
    List<Facility> getFacilitiesByAttribute(@NonNull String attributeName, @NonNull String attrValue);

    /**
     * Get groups the user is member of and are assigned (have access) to the SP.
     * @param facilityId ID of facility representing the SP.
     * @param userId ID of the user.
     * @return List of Groups.
     */
    List<Group> getUsersGroupsOnFacility(@NonNull Long facilityId, @NonNull Long userId);

    /**
     * Search for facilities by value of the attribute.
     * @param attribute The whole attribute object with value.
     * @return List of Facilities.
     */
    List<Facility> searchFacilitiesByAttributeValue(@NonNull PerunAttribute attribute);

    /**
     * @param entityId int entityId
     * @param userGroups array of groups where user belongs to
     * @return array of resource capabilities
     */
    Set<String> getResourceCapabilities(@NonNull Long entityId, @NonNull List<Group> userGroups);

}
