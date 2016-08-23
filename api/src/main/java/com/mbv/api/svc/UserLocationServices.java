package com.mbv.api.svc;


import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserLocationData;
import com.mbv.framework.exception.AppException;

import java.util.List;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserLocationServices {

    public BaseResponse getAllUserLocations(Long id, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getUserLocationInfo(Long id, Long locationId) throws AppException;

    public BaseResponse saveUserLocation(Long id, UserLocationData userLocationData) throws AppException;

    public BaseResponse updateUserLocation(Long id, Long locationId, UserLocationData userLocationData) throws AppException;

    public BaseResponse deleteUserLocation(Long id, Long locationId) throws AppException;
}
