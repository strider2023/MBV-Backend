package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 23/02/16.
 */
public class UserTimelineDAOImpl extends BaseEntityDAOImpl<UserTimeline> implements UserTimelineDAO{

    public UserTimelineDAOImpl() {
        super(UserTimeline.class);
    }

    @Override
    public List<UserTimeline> getAllTimelineInfoByUser(Long userId, Map<Object, Object>paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserTimeline.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserTimeline> userTimelines = criteria.list();
        evictCollection(userTimelines);
        return userTimelines;
    }
}
