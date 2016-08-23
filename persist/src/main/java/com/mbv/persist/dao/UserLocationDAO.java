package com.mbv.persist.dao;

import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.enums.CurrentLocationType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserLocationDAO extends BaseEntityDAO<Long,UserLocation> {

    public List<UserLocation> getUserLocationInfoById(Long userId, Map<Object, Object> paginationInfo);

    public UserLocation getUserLocationByDetails(Long userId, String city, String state, String country, Long pincode, CurrentLocationType type);
}
