package cz.muni.ics.perunproxyapi.persistence.adapters;

import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunConnectionException;
import cz.muni.ics.perunproxyapi.persistence.exceptions.PerunUnknownException;
import cz.muni.ics.perunproxyapi.persistence.models.PerunAttributeValue;
import lombok.NonNull;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static cz.muni.ics.perunproxyapi.persistence.enums.Entity.USER;

public class AdapterUtils {

    public static List<String> getForwardedEntitlements(@NonNull DataAdapter dataAdapter,
                                                        @NonNull Long userId,
                                                        String entitlementsIdentifier)
            throws PerunUnknownException, PerunConnectionException
    {
        if (!StringUtils.hasText(entitlementsIdentifier)) {
            return new ArrayList<>();
        }

        PerunAttributeValue attributeValue = dataAdapter.getAttributeValue(USER, userId, entitlementsIdentifier);
        if (attributeValue != null && attributeValue.valueAsList() != null) {
            return attributeValue.valueAsList();
        }

        return new ArrayList<>();
    }

}
