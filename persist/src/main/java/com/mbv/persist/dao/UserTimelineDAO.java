package com.mbv.persist.dao;

import com.mbv.persist.entity.UserTimeline;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 23/02/16.
 */
public interface UserTimelineDAO extends BaseEntityDAO<Long,UserTimeline> {

    public List<UserTimeline> getAllTimelineInfoByUser(Long userId, Map<Object, Object> paginationInfo);
}
