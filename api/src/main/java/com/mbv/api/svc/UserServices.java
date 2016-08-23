package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserSignUpData;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.enums.RoleType;
import com.sun.jersey.core.header.FormDataContentDisposition;

import java.io.InputStream;

/**
 * Created by arindamnath on 20/01/16.
 */
public interface UserServices {

    public BaseResponse getUser(Long id) throws AppException;

    public BaseResponse updateUserType(Long userId, RoleType roleType) throws AppException;

    public BaseResponse updateUser(Long id, UserSignUpData userSignUpData) throws AppException;

    public BaseResponse updateUserGMCId(Long userId, UserSignUpData userSignUpData) throws AppException;

    public BaseResponse rateUser(Long id, Long rating) throws AppException;

    public BaseResponse uploadUserImage(Long id, InputStream inputStream, FormDataContentDisposition fileDetail) throws AppException;

    public BaseResponse getUserTimeline(Long userId, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;
}
