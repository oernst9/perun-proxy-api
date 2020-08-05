package cz.muni.ics.perunproxyapi.application.facade;


import cz.muni.ics.perunproxyapi.application.service.Service;
import cz.muni.ics.perunproxyapi.persistence.models.Group;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.common.net.UrlEscapers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Facade {

    @Autowired
    private Service service;

    @NonNull private final String prefix = config.getProperty("prefix", null);
    @NonNull private final String authority = config.getProperty("authority", null);

    public List<String> getEntitlements(Long facilityId, Long userId) {
        List<String> result = new ArrayList<>();
        List<Group> groups = service.getUsersGroupsOnFacility(facilityId, userId);
        if (groups == null || groups.isEmpty()) {
            return Collections.emptyList();
        }
        List<String> eduPersonEntitlement = getEduPersonEntitlement(groups);
        List<String> capabilities = getCapabilities(facilityId, groups);
        List<String> forwardedEduPersonEntitlement = Collections.singletonList(service.getForwardedEntitlement(userId)
                .getValue().asText());

        result.addAll(eduPersonEntitlement);
        result.addAll(capabilities);
        result.addAll(forwardedEduPersonEntitlement);
        return result;
    }

    private List<String> getCapabilities(Long facilityId, List<Group> groups) {
        List<String> capabilities = new ArrayList<>();
        capabilities.addAll(service.getResourceCapabilities(facilityId, groups));
        capabilities.addAll(service.getFacilityCapabilities(facilityId));
        for (String capability : capabilities) {
            capability = wrapCapabilityToAARC(capability);
        }
        return capabilities;
    }


    private List<String> getEduPersonEntitlement(List<Group> groups) {
        List<String> eduPersonEntitlement = new ArrayList<>();
        if (prefix.trim().isEmpty() || authority.trim().isEmpty()) {
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
        return prefix + "group:" + UrlEscapers.urlPathSegmentEscaper().escape(groupName) + "#" + authority;
    }

    private String wrapCapabilityToAARC(String capability) {
        return prefix + UrlEscapers.urlPathSegmentEscaper().escape(capability) + "#" + authority;
    }

}
