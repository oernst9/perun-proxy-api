package cz.muni.ics.perunproxyapi.adapters;

import cz.muni.ics.perunproxyapi.adapters.interfaces.DataAdapter;
import cz.muni.ics.perunproxyapi.enums.Entity;
import cz.muni.ics.perunproxyapi.models.*;

import java.util.List;
import java.util.Map;

public class LdapAdapterImpl implements DataAdapter {
    @Override
    public User getPerunUser(long userId, long uids) {
        return null;
    }

    @Override
    public List<Group> getMemberGroups(long userId, long voId) {
        return null;
    }

    @Override
    public List<Group> getSpGroups(long spEntityId) {
        return null;
    }

    @Override
    public Group getGroupByName(long voId, String name) {
        return null;
    }

    @Override
    public Vo getVoByShortName(String voShortName) {
        return null;
    }

    @Override
    public Vo getVoById(long voId) {
        return null;
    }

    @Override
    public Map<String, PerunAttribute> getAttributesValues(Entity entity, long entityId, List<String> attributes) {
        return null;
    }

    @Override
    public List<Facility> getFacilitiesByAttributes(String name, String attrValue) {
        return null;
    }

    @Override
    public List<Group> getUsersGroupsOnFacility(long spEntityId, long userId) {
        return null;
    }

    @Override
    public List<Facility> searchFacilitiesByAttributeValue(PerunAttribute attribute) {
        return null;
    }
}
