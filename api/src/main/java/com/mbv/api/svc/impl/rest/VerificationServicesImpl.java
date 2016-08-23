package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.svc.VerficationServices;
import com.mbv.api.util.NetworkUtil;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.response.EmailResponses;
import com.mbv.framework.util.KeyGenerator;
import com.mbv.framework.util.SMSUtil;
import com.mbv.framework.util.SMTP;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.VerificationOtpDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.VerificationOTP;
import com.mbv.persist.enums.Status;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 23/02/16.
 */
@Component
@Path("/mpokket/api/verify/user")
public class VerificationServicesImpl implements VerficationServices {

    @Autowired
    SMSUtil smsUtil;

    @InjectParam
    VerificationOtpDAO verificationOtpDAO;

    @InjectParam
    UserDAO userDAO;

    @Autowired
    SMTP smtp;

    @Autowired
    EmailResponses emailResponses;

    @Autowired
    KeyGenerator keyGenerator;

    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getOTPCode(@PathParam("userId") Long userId) throws AppException {
        User user = userDAO.get(userId);
        //Check for existing otp. If present set status to archived.
        List<VerificationOTP> activeOTPs = verificationOtpDAO.getActiveOTPRequests(userId);
        if (activeOTPs.size() > 0) {
            for (VerificationOTP verificationOTP : activeOTPs) {
                verificationOTP.setStatus(Status.CANCELLED);
                verificationOTP.setLastUpdatedBy(userId);
            }
            verificationOtpDAO.bulkUpdate(activeOTPs);
        }
        //Generate new OTP
        String otp = keyGenerator.generateCode(5);
        String ip = NetworkUtil.getSystemPublicIP();
        //Send mail first
        if(ip != null) {
            smtp.sendMail(user.getEmail(),
                    "Human verify yourself - Team mPokket!", getBodyText(user, otp, ip));
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Oops something went wrong!");
        }
        String refId = smsUtil.sendSMS(user.getPhone(), "Your mPokket OTP code is : " + otp, null);
        //Save details
        VerificationOTP verificationOTP = new VerificationOTP();
        verificationOTP.setUserId(userId);
        verificationOTP.setCode(otp);
        verificationOTP.setExternalId(refId);
        verificationOTP.setIsEmailVerified(false);
        verificationOTP.setIsMobileVerified(false);
        verificationOTP.setStatus(Status.ACTIVE);
        verificationOTP.setCreatedBy(userId);
        verificationOTP.setLastUpdatedBy(userId);
        verificationOtpDAO.create(verificationOTP);
        return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS, otp);
    }

    @GET
    @Path("/{userId}/otp/{otpCode}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse verifyOTPCode(@PathParam("userId") Long userId, @PathParam("otpCode") String code) throws AppException {
        VerificationOTP verificationOTP = verificationOtpDAO.getDetialsByCode(code, userId);
        if (verificationOTP != null) {
            verificationOTP.setIsMobileVerified(true);
            verificationOtpDAO.updateAndFlush(verificationOTP);
            return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS, "Verification Success");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Invalid Code");
        }
    }

    private String getBodyText(User user, String code, String machineIP) {
        return emailResponses.getVerificationMail(StringUtil.getName(user),
                "http://staging.mpokket.com:8080/mpokket/api/authenticate/user/" +
                        String.valueOf(user.getId()) + "/otp/" + code + "/email/verify");
    }
}
