package cz.muni.ics.perunproxyapi.application.service;

import cz.muni.ics.perunproxyapi.persistence.models.Group;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for service layer.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
@Slf4j
public class ServiceUtils {

    /**
     * Convert given groupName into the AARC format.
     * @param groupName Name of the group.
     * @param prefix Prefix to be prepended.
     * @param authority Authority issuing the groupName.
     * @return GroupName in the AARC format.
     */
    public static String wrapGroupNameToAARC(@NonNull String groupName, @NonNull String prefix, @NonNull String authority) {
        return prefix + ":group:" + URLEncoder.encode(groupName, StandardCharsets.UTF_8) + '#' + authority;
    }

    /**
     * Convert given capability into the AARC format.
     * @param capability Capability value.
     * @param prefix Prefix to be prepended.
     * @param authority Authority issuing the capability.
     * @return Capability in the AARC format.
     */
    public static String wrapCapabilityToAARC(@NonNull String capability, @NonNull String prefix, @NonNull String authority) {
        return prefix + ':' + capability + '#' + authority;
    }

    /**
     * Get group enitlements for the user.
     * @param groups Groups the user is member of.
     * @param prefix Prefix to be prepended.
     * @param authority Authority issuing the entitlement.
     * @return List of entitlements in the AARC format.
     */
    public static List<String> wrapGroupEntitlements(@NonNull List<Group> groups, @NonNull String prefix,
                                                     @NonNull String authority)
    {
        List<String> entitlements = new ArrayList<>();
        for (Group group : groups) {
            String groupName = group.getUniqueGroupName();
            if (groupName == null) {
                continue;
            }
            groupName = groupName.replaceAll("^(\\w*):members$", "$1");
            groupName = ServiceUtils.wrapGroupNameToAARC(groupName, prefix, authority);
            entitlements.add(groupName);
        }
        Collections.sort(entitlements);
        return entitlements;
    }

}
