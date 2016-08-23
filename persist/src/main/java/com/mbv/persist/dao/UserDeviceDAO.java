package com.mbv.persist.dao;

import com.mbv.persist.entity.UserDevice;

import java.util.List;

/**
 * Created by arindamnath on 23/02/16.
 */
public interface UserDeviceDAO extends BaseEntityDAO<Long,UserDevice> {

    public UserDevice getDeviceById(Long userId, String deviceId);

    public List<UserDevice> getDeviceByUser(Long userId);
}
