package cz.muni.ics.perunproxyapi.adapters;


import cz.muni.ics.perunproxyapi.attributes.AttributeMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Abstract class containing different mapping services. Adapter implementation should extend this class if
 * the implementation needs to use the mapping service.
 *
 * @author Dominik Frantisek Bucik <bucik@ics.muni.cz>
 */
public abstract class PerunAdapterWithMappingServices {

    @Autowired
    @Qualifier("userAttributesMappingService")
    private AttributeMappingService userAttributesMappingService;

    @Autowired
    @Qualifier("facilityAttributesMappingService")
    private AttributeMappingService facilityAttributesMappingService;

    @Autowired
    @Qualifier("groupAttributesMappingService")
    private AttributeMappingService groupAttributesMappingService;

    @Autowired
    @Qualifier("voAttributesMappingService")
    private AttributeMappingService voAttributesMappingService;

    @Autowired
    @Qualifier("resourceAttributesMappingService")
    private AttributeMappingService resourceAttributesMappingService;

    public AttributeMappingService getUserAttributesMappingService() {
        return userAttributesMappingService;
    }

    public void setUserAttributesMappingService(AttributeMappingService userAttributesMappingService) {
        this.userAttributesMappingService = userAttributesMappingService;
    }

    public AttributeMappingService getFacilityAttributesMappingService() {
        return facilityAttributesMappingService;
    }

    public void setFacilityAttributesMappingService(AttributeMappingService facilityAttributesMappingService) {
        this.facilityAttributesMappingService = facilityAttributesMappingService;
    }

    public AttributeMappingService getGroupAttributesMappingService() {
        return groupAttributesMappingService;
    }

    public void setGroupAttributesMappingService(AttributeMappingService groupAttributesMappingService) {
        this.groupAttributesMappingService = groupAttributesMappingService;
    }

    public AttributeMappingService getVoAttributesMappingService() {
        return voAttributesMappingService;
    }

    public void setVoAttributesMappingService(AttributeMappingService voAttributesMappingService) {
        this.voAttributesMappingService = voAttributesMappingService;
    }

    public AttributeMappingService getResourceAttributesMappingService() {
        return resourceAttributesMappingService;
    }

    public void setResourceAttributesMappingService(AttributeMappingService resourceAttributesMappingService) {
        this.resourceAttributesMappingService = resourceAttributesMappingService;
    }

}

