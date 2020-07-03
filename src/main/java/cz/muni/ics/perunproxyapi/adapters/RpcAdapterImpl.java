package cz.muni.ics.perunproxyapi.adapters;

import com.sun.istack.NotNull;
import cz.muni.ics.perunproxyapi.adapters.interfaces.FullAdapter;
import cz.muni.ics.perunproxyapi.enums.Entity;
import cz.muni.ics.perunproxyapi.enums.MemberStatus;
import cz.muni.ics.perunproxyapi.models.*;

import java.util.List;
import java.util.Map;

public class RpcAdapterImpl implements FullAdapter {
    @Override
    public Map<String, PerunAttribute> getAttributes(Entity entity, long entityId, List<String> attributes) {
        return null;
    }

    @Override
    public UserExtSource getUserExtSource(String extSourceName, String extSourceLogin) {
        return null;
    }

    @Override
    public MemberStatus getMemberStatusByUserAndVo(User user, Vo vo) {
        return null;
    }

    @Override
    public void setAttributes(Entity entity, long entityId, List<PerunAttribute> attributes) {

    }

    @Override
    public void updateUserExtSourceLastAccess(UserExtSource userExtSource) {

    }

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
