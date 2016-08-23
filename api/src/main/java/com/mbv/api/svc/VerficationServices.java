package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;

/**
 * Created by arindamnath on 23/02/16.
 */
public interface VerficationServices {

    public BaseResponse getOTPCode(Long userId) throws Exception;

    public BaseResponse verifyOTPCode(Long userId, String code) throws Exception;
}
