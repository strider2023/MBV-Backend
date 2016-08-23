package com.mbv.api.svc.impl.rest;

import com.mbv.api.auth.UserContext;
import com.mbv.api.constant.APIConstants;
import com.mbv.api.data.AuthData;
import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.UserSignUpData;
import com.mbv.api.svc.AuthServices;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.cache.CacheClient;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.response.EmailResponses;
import com.mbv.framework.response.VerificationResponses;
import com.mbv.framework.util.DESEncryption;
import com.mbv.framework.util.JSONUtil;
import com.mbv.framework.util.SMTP;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.UserDeviceDAO;
import com.mbv.persist.dao.UserScoreDAO;
import com.mbv.persist.dao.UserSessionDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.dao.VerificationOtpDAO;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.UserDevice;
import com.mbv.persist.entity.UserScore;
import com.mbv.persist.entity.UserSession;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.entity.VerificationOTP;
import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Currency;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.UserTimelineType;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 25/02/16.
 */
@Component
@Path("/mpokket/api/authenticate")
public class AuthServicesImpl extends BaseAPIServiceImpl implements AuthServices {

    @InjectParam
    UserDAO userDAO;

    @InjectParam
    UserDeviceDAO userDeviceDAO;

    @InjectParam
    UserSessionDAO userSessionDAO;

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    VerificationOtpDAO verificationOtpDAO;

    @InjectParam
    UserScoreDAO userScoreDAO;

    @Autowired
    DESEncryption desEncryption;

    @Autowired
    EmailResponses emailResponses;

    @Autowired
    SMTP smtp;

    @Autowired
    VerificationResponses verificationResponses;

    @POST
    @Path("/user")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse authenticateUser(AuthData authData) throws AppException {
        User user = userDAO.getUserByEmailAndHash(authData.getEmail(), desEncryption.encrypt(authData.getPassword(), null));
        if (user != null) {
            if(authData.getDeviceData() != null) {
                UserDevice userDevice = userDeviceDAO.getDeviceById(user.getId(), authData.getDeviceData().getDeviceId());
                if (userDevice == null) {
                    userDevice = new UserDevice();
                    userDevice.setUserId(user.getId());
                    userDevice.setDeviceId(authData.getDeviceData().getDeviceId());
                    userDevice.setImeiId(authData.getDeviceData().getImeiId());
                    userDevice.setOsType(authData.getDeviceData().getMobileOS());
                    userDevice.setOsVersion(authData.getDeviceData().getOsVersion());
                    userDevice.setBrand(authData.getDeviceData().getBrand());
                    userDevice.setModel(authData.getDeviceData().getModel());
                    userDevice.setDeviceName(authData.getDeviceData().getDeviceName());
                    userDevice.setManufacturer(authData.getDeviceData().getManufacturer());
                    userDevice.setLanguage(authData.getDeviceData().getLanguage());
                    userDevice.setDeviceType(authData.getDeviceData().getMobileDeviceType());
                    userDevice.setOperator(authData.getDeviceData().getOperatorName());
                    userDevice.setStatus(Status.ACTIVE);
                    userDevice.setCreatedBy(user.getId());
                    userDevice.setLastUpdatedBy(user.getId());
                    userDeviceDAO.create(userDevice);
                    return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, 1, saveUserSession(user, userDevice));
                } else if (userDevice.getStatus() == Status.ACTIVE) {
                    return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, 1, saveUserSession(user, userDevice));
                } else {
                    return new BaseResponse(user.getId(), BaseResponse.ResponseCode.FAILURE, "This device has been flagged as stolen!");
                }
            } else {
                return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, 1, saveUserSession(user, null));
            }
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Invalid Username or Password!");
        }
    }

    @POST
    @Path("/recover")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse forgotPassword(AuthData authData) throws AppException {
        User user = userDAO.getUserByEmail(authData.getEmail());
        if(user != null) {
            smtp.sendMail(user.getEmail(), "Password Recovery - mPokket", getUserEmailRecoveryBody(user));
            return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, "Recovery information sent to registered email.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "User doesn't exists!");
        }
    }

    @POST
    @Path("/user/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse userSignUp(UserSignUpData userSignUpData) throws AppException {
        User user = userDAO.getUserByEmailAndPhone(userSignUpData.getEmail(), userSignUpData.getPhoneNumber());
        if (user == null) {
            user = new User();
            user.setFirstname(userSignUpData.getFirstName());
            user.setMiddlename(userSignUpData.getMiddleName());
            user.setLastname(userSignUpData.getLastName());
            user.setHash(desEncryption.encrypt(userSignUpData.getHash(), null));
            user.setEmail(userSignUpData.getEmail());
            user.setPhone(userSignUpData.getPhoneNumber());
            user.setType(userSignUpData.getAccountType());
            user.setCurrency(Currency.INR);
            if(userSignUpData.getReferralCode() != null) {
                user.setReferralCode(userSignUpData.getReferralCode());
            }
            if(user.getGcmId() != null) {
                userSignUpData.setGcmId(user.getGcmId());
            }
            user.setStatus(Status.ACTIVE);
            user.setCreatedBy(-1l);
            user.setLastUpdatedBy(-1l);
            userDAO.create(user);

            UserDevice userDevice = new UserDevice();
            if (userSignUpData.getDeviceData() != null) {
                userDevice.setUserId(user.getId());
                userDevice.setDeviceId(userSignUpData.getDeviceData().getDeviceId());
                userDevice.setImeiId(userSignUpData.getDeviceData().getImeiId());
                userDevice.setOsType(userSignUpData.getDeviceData().getMobileOS());
                userDevice.setOsVersion(userSignUpData.getDeviceData().getOsVersion());
                userDevice.setBrand(userSignUpData.getDeviceData().getBrand());
                userDevice.setModel(userSignUpData.getDeviceData().getModel());
                userDevice.setDeviceName(userSignUpData.getDeviceData().getDeviceName());
                userDevice.setManufacturer(userSignUpData.getDeviceData().getManufacturer());
                userDevice.setLanguage(userSignUpData.getDeviceData().getLanguage());
                userDevice.setDeviceType(userSignUpData.getDeviceData().getMobileDeviceType());
                userDevice.setOperator(userSignUpData.getDeviceData().getOperatorName());
                userDevice.setStatus(Status.ACTIVE);
                userDevice.setCreatedBy(user.getId());
                userDevice.setLastUpdatedBy(user.getId());
                userDeviceDAO.create(userDevice);
            }

            UserScore userScore = new UserScore();
            userScore.setUserId(user.getId());
            userScore.setCredits(0l);
            userScore.setDefaults(0l);
            userScore.setLoanQuota(5l);
            userScore.setSystemRating(0l);
            userScore.setUserRating(0l);
            userScore.setStatus(Status.ACTIVE);
            userScore.setCreatedBy(user.getId());
            userScore.setLastUpdatedBy(user.getId());
            userScoreDAO.create(userScore);

            UserTimeline userTimeline = new UserTimeline();
            userTimeline.setUserId(user.getId());
            userTimeline.setType(UserTimelineType.PROFILE_UPDATE);
            userTimeline.setDescription("Joined mPokket.");
            userTimeline.setRefUserId(user.getId());
            userTimeline.setStatus(Status.ACTIVE);
            userTimeline.setCreatedBy(user.getId());
            userTimeline.setLastUpdatedBy(user.getId());
            userTimelineDAO.create(userTimeline);

            String sessionId = saveUserSession(user, userDevice);

            smtp.sendMail(user.getEmail(), "Welcome to mPokket", emailResponses.getSignUpMail(StringUtil.getName(user)));
            return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, 1, sessionId);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "User exists!");
        }
    }

    @GET
    @Path("user/{userId}/otp/{otpCode}/email/verify")
    @Produces(MediaType.TEXT_HTML)
    @Override
    public String authenticateUserEmail(@PathParam("userId") Long userId, @PathParam("otpCode") String code) throws AppException {
        VerificationOTP verificationOTP = verificationOtpDAO.getDetialsByCode(code, userId);
        if (verificationOTP != null) {
            verificationOTP.setIsEmailVerified(true);
            verificationOtpDAO.updateAndFlush(verificationOTP);
            return verificationResponses.getSuccessResponse();
        } else {
            return verificationResponses.getErrorResponse();
        }
    }

    private String saveUserSession(User user, UserDevice userDevice) {
        // Construct user context
        Map<Long, AccountType> roleMap = new HashMap<>();
        roleMap.put(user.getId(), user.getType());
        UserContext uc = new UserContext(user.getId(), this.getIpFromRequest(), roleMap, Currency.INR, user.getType(), true);
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        // Push UserContext to cache
        this.getCacheClient().set(APIConstants.AUTH_TOKEN_KEY + uuid, JSONUtil.getJsonString(uc), CacheClient.MemcachedInfiniteTTL);
        //Remove all active sessions
        List<UserSession> userSessionList = userSessionDAO.getActiveUserSessions(user.getId());
        if(userSessionList != null) {
            if (userSessionList.size() > 0) {
                for (UserSession userSession : userSessionList) {
                    userSession.setStatus(Status.INACTIVE);
                }
                userSessionDAO.bulkUpdate(userSessionList);
            }
        }
        //Create a new session
        UserSession userSession = new UserSession();
        userSession.setUserId(user.getId());
        userSession.setDeviceId((userDevice == null) ? -1l : userDevice.getId());
        userSession.setSessionId(uuid);
        userSession.setLoginDate(new Date());
        userSession.setStatus(Status.ACTIVE);
        userSession.setLastUpdatedBy(user.getId());
        userSession.setCreatedBy(user.getId());
        userSessionDAO.create(userSession);
        this.getHttpServletResponse().addHeader(APIConstants.AUTH_TOKEN_HEADER, uuid);
        return uuid;
    }

    private String getUserEmailRecoveryBody(User user) {
        return emailResponses.getUserEmailRecoveryBody(StringUtil.getName(user),
                desEncryption.decrypt(user.getHash(), null));
    }
}
