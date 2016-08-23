package com.mbv.api.svc;

import com.mbv.api.data.AuthData;
import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserSignUpData;
import com.mbv.framework.exception.AppException;

/**
 * Created by arindamnath on 23/02/16.
 */
public interface AuthServices {

    public BaseResponse authenticateUser(AuthData authData) throws AppException;

    public BaseResponse forgotPassword(AuthData authData) throws AppException;

    public BaseResponse userSignUp(UserSignUpData userSignUpData) throws AppException;

    public String authenticateUserEmail(Long userId, String code) throws AppException;
}
