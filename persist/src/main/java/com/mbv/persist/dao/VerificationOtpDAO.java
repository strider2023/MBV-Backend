package com.mbv.persist.dao;

import com.mbv.persist.entity.VerificationOTP;

import java.util.List;

/**
 * Created by arindamnath on 23/02/16.
 */
public interface VerificationOtpDAO extends BaseEntityDAO<Long,VerificationOTP> {

    public VerificationOTP getDetialsByCode(String otpCode, Long userId);

    public List<VerificationOTP> getActiveOTPRequests(Long userId);
}
