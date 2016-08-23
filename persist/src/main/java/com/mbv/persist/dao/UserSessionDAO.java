package com.mbv.persist.dao;

import com.mbv.persist.entity.UserSession;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserSessionDAO extends BaseEntityDAO<Long,UserSession> {

    public UserSession getUserSessionByAuthToken(String token);

    public List<UserSession> getActiveUserSessions(Long userId);

    public List<UserSession> getActiveUserSessionsByDevice(Long userId, Long deviceId);
}
