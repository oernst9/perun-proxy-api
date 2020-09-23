package cz.muni.ics.perunproxyapi.persistence.models;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.StringUtils;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UpdateAttributeMappingEntry {
    
    @NonNull List<String> externalNames;
    boolean appendOnly = false;
    boolean useForSearch = false;

    public UpdateAttributeMappingEntry(@NonNull List<String> externalNames, boolean appendOnly, boolean useForSearch) {
        this.setExternalNames(externalNames);
        this.setAppendOnly(appendOnly);
        this.setUseForSearch(useForSearch);
    }

    public void setExternalNames(@NonNull List<String> externalNames) {
        if (externalNames.isEmpty()) {
            throw new IllegalArgumentException("External names cannot be null nor empty");
        }
        
        this.externalNames = externalNames;
    }

}
