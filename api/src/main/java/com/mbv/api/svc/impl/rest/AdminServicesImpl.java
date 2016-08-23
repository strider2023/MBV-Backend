package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BankCodesData;
import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.OfferCodeData;
import com.mbv.api.data.UserData;
import com.mbv.api.data.UserEducationData;
import com.mbv.api.data.UserKYCData;
import com.mbv.api.data.UserLocationData;
import com.mbv.api.data.UserSignUpData;
import com.mbv.api.data.WalletAccountData;
import com.mbv.api.data.WalletTransactionData;
import com.mbv.api.data.WalletTransactionDetailsData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.AdminServices;
import com.mbv.api.util.DateUtil;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.response.EmailResponses;
import com.mbv.framework.util.DESEncryption;
import com.mbv.framework.util.KeyGenerator;
import com.mbv.framework.util.SMTP;
import com.mbv.persist.dao.BankAccountDAO;
import com.mbv.persist.dao.BankCodesDAO;
import com.mbv.persist.dao.EducationDegreeDAO;
import com.mbv.persist.dao.LoanOfferCodeDAO;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.UserEducationDAO;
import com.mbv.persist.dao.UserKycDAO;
import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.dao.UserTransactionDAO;
import com.mbv.persist.dao.UserWalletDAO;
import com.mbv.persist.entity.BankAccount;
import com.mbv.persist.entity.BankCodes;
import com.mbv.persist.entity.LoanOfferCode;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.UserEducation;
import com.mbv.persist.entity.UserKYC;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.entity.UserWallet;
import com.mbv.persist.enums.AccountType;
import com.mbv.persist.enums.Currency;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 30/03/16.
 */
@Component
@Path("/mpokket/api/admin")
public class AdminServicesImpl extends BaseAPIServiceImpl implements AdminServices {

    @InjectParam
    UserDAO userDAO;

    @InjectParam
    BankCodesDAO bankCodesDAO;

    @InjectParam
    UserTransactionDAO userTransactionDAO;

    @InjectParam
    UserKycDAO userKycDAO;

    @InjectParam
    UserEducationDAO userEducationDAO;

    @InjectParam
    UserLocationDAO userLocationDAO;

    @InjectParam
    EducationDegreeDAO educationDegreeDAO;

    @InjectParam
    LoanOfferCodeDAO loanOfferCodeDAO;

    @InjectParam
    BankAccountDAO bankAccountDAO;

    @InjectParam
    UserWalletDAO userWalletDAO;

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    DESEncryption desEncryption;

    @Autowired
    EmailResponses emailResponses;

    @Autowired
    SMTP smtp;

    @GET
    @Path("/users")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getListOfUsers(@QueryParam("offset") Integer offset,
                                       @QueryParam("pageSize") Integer pageSize,
                                       @QueryParam("searchField") String searchField,
                                       @QueryParam("searchText") String searchText,
                                       @QueryParam("sortField") String sortField,
                                       @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                       @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (offset != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<User> userDAOList = userDAO.getUserList(AccountType.USER, paginationInfo);
        if (userDAOList.size() > 0) {
            List<UserData> userDatas = new ArrayList<>();
            for (User user : userDAOList) {
                UserData userData = new UserData();
                userData.setId(user.getId());
                userData.setName(StringUtil.getName(user));
                userData.setEmail(user.getEmail());
                userData.setPhoneNumber(user.getPhone());
                userData.setGender(user.getGender());
                userData.setImageURL(user.getImageUrl());
                userData.setAge(DateUtil.getAge(user.getDob()));
                userDatas.add(userData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userDatas.size(), userDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/transactions/status/{status}/type/{type}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getListOfTransactions(@PathParam("status") Status status,
                                              @PathParam("type") TransactionType type,
                                              @QueryParam("offset") Integer offset,
                                              @QueryParam("pageSize") Integer pageSize,
                                              @QueryParam("searchField") String searchField,
                                              @QueryParam("searchText") String searchText,
                                              @QueryParam("sortField") String sortField,
                                              @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                              @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (offset != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<UserTransaction> userTransactionList = userTransactionDAO.getTransactionsByTypeAndStatus(status, type, paginationInfo);
        if (userTransactionList.size() > 0) {
            List<WalletTransactionDetailsData> walletTransactionDetailsDatas = new ArrayList<>();
            for (UserTransaction userTransaction : userTransactionList) {
                WalletTransactionDetailsData walletTransactionDetailsData = new WalletTransactionDetailsData();
                walletTransactionDetailsData.setId(userTransaction.getId());
                walletTransactionDetailsData.setTransactionId(userTransaction.getTransactionId());
                walletTransactionDetailsData.setAmount(userTransaction.getAmount());
                walletTransactionDetailsData.setType(userTransaction.getType());
                if (userTransaction.getLoanId() != null) {
                    walletTransactionDetailsData.setLoanId(userTransaction.getLoanId());
                }
                walletTransactionDetailsData.setExteralId(userTransaction.getExternalId());
                walletTransactionDetailsData.setStatus(userTransaction.getStatus());
                walletTransactionDetailsData.setCreatedOn(userTransaction.getCreatedDate());
                walletTransactionDetailsData.setUpdatedOn(userTransaction.getLastUpdatedDate());
                walletTransactionDetailsDatas.add(walletTransactionDetailsData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, walletTransactionDetailsDatas.size(), walletTransactionDetailsDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/transaction/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getTransactionDetails(@PathParam("transactionId") Long transactionId) throws AppException {
        UserTransaction userTransaction = userTransactionDAO.get(transactionId);
        if (userTransaction != null) {
            WalletTransactionDetailsData walletTransactionDetailsData = new WalletTransactionDetailsData();
            walletTransactionDetailsData.setId(userTransaction.getId());
            walletTransactionDetailsData.setTransactionId(userTransaction.getTransactionId());
            walletTransactionDetailsData.setAmount(userTransaction.getAmount());
            walletTransactionDetailsData.setType(userTransaction.getType());
            if (userTransaction.getLoanId() != null) {
                walletTransactionDetailsData.setLoanId(userTransaction.getLoanId());
            }
            walletTransactionDetailsData.setExteralId(userTransaction.getExternalId());
            walletTransactionDetailsData.setStatus(userTransaction.getStatus());
            walletTransactionDetailsData.setCreatedOn(userTransaction.getCreatedDate());
            walletTransactionDetailsData.setUpdatedOn(userTransaction.getLastUpdatedDate());
            //Set user infos
            if(userTransaction.getFromUserId() != -1) {
                User fromUserData = userDAO.get(userTransaction.getFromUserId());
                UserData fromUser = new UserData();
                fromUser.setId(fromUserData.getId());
                fromUser.setName(StringUtil.getName(fromUserData));
                fromUser.setEmail(fromUserData.getEmail());
                fromUser.setPhoneNumber(fromUserData.getPhone());
                fromUser.setGender(fromUserData.getGender());
                fromUser.setImageURL(fromUserData.getImageUrl());
                fromUser.setAge(DateUtil.getAge(fromUserData.getDob()));
                walletTransactionDetailsData.setFromUser(fromUser);
                //Set account info
                walletTransactionDetailsData.setFromAccount(getWalletInfo(userTransaction.getFromUserId(), userTransaction.getFromAccountId()));
            }

            if(userTransaction.getToUserId()!= -1) {
                User toUserData = userDAO.get(userTransaction.getToUserId());
                UserData toUser = new UserData();
                toUser.setId(toUserData.getId());
                toUser.setName(StringUtil.getName(toUserData));
                toUser.setEmail(toUserData.getEmail());
                toUser.setPhoneNumber(toUserData.getPhone());
                toUser.setGender(toUserData.getGender());
                toUser.setImageURL(toUserData.getImageUrl());
                toUser.setAge(DateUtil.getAge(toUserData.getDob()));
                walletTransactionDetailsData.setToUser(toUser);
                //Set account info
                walletTransactionDetailsData.setToAccount(getWalletInfo(userTransaction.getToUserId(), userTransaction.getToAccountId()));
            }

            return new BaseResponse(transactionId, BaseResponse.ResponseCode.SUCCESS, 1, walletTransactionDetailsData);
        } else {
            return new BaseResponse(transactionId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/transaction/{transactionId}/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateTransactionStatus(@PathParam("transactionId") Long transactionId,
                                                @PathParam("status") Status status) throws AppException {
        UserTransaction userTransaction = userTransactionDAO.get(transactionId);
        if (userTransaction != null) {
            userTransaction.setStatus(status);
            userTransactionDAO.updateAndFlush(userTransaction);
            return new BaseResponse(transactionId, BaseResponse.ResponseCode.SUCCESS, "Record updated successfully!");
        } else {
            return new BaseResponse(transactionId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUserDetails(@PathParam("userId") Long userId) throws AppException {
        User user = userDAO.get(userId);
        if (user != null) {
            UserSignUpData userSignUpData = new UserSignUpData();
            userSignUpData.setId(user.getId());
            userSignUpData.setAccountType(user.getType());
            userSignUpData.setName(StringUtil.getName(user));
            userSignUpData.setEmail(user.getEmail());
            userSignUpData.setPhoneNumber(user.getPhone());
            userSignUpData.setDob(user.getDob());
            userSignUpData.setWorkStatus(user.getWorkStatus());
            userSignUpData.setResidentialStatus(user.getResidentialStatus());
            userSignUpData.setMaritalStatus(user.getMaritalStatus());
            userSignUpData.setGender(user.getGender());
            userSignUpData.setUserImage(user.getImageUrl());
            userSignUpData.setRoleType(user.getRole());
            userSignUpData.setFatherName(user.getFatherName());
            //Get location information
            List<UserLocation> userLocations = userLocationDAO.getUserLocationInfoById(userId, null);
            if (userLocations.size() > 0) {
                List<UserLocationData> userLocationDatas = new ArrayList<>();
                for (UserLocation userLocation : userLocations) {
                    UserLocationData userLocationData = new UserLocationData();
                    userLocationData.setId(userLocation.getId());
                    userLocationData.setUserId(userLocation.getUserId());
                    userLocationData.setAddress(userLocation.getAddress());
                    userLocationData.setCity(userLocation.getCity());
                    userLocationData.setState(userLocation.getState());
                    userLocationData.setCountry(userLocation.getCountry());
                    userLocationData.setPincode(userLocation.getPincode());
                    userLocationData.setType(userLocation.getType());
                    userLocationData.setIsVerified(userLocation.getIsVerified());
                    userLocationDatas.add(userLocationData);
                }
                userSignUpData.setUserLocationDatas(userLocationDatas);
            }
            //Get KYC information
            List<UserKYC> userKYCs = userKycDAO.getUserKYCInfoById(userId, null);
            if (userKYCs.size() > 0) {
                List<UserKYCData> userKYCDatas = new ArrayList<>();
                for (UserKYC userKYC : userKYCs) {
                    UserKYCData userKYCData = new UserKYCData();
                    userKYCData.setId(userKYC.getId());
                    userKYCData.setUserId(userId);
                    userKYCData.setKycId(userKYC.getKycId());
                    userKYCData.setType(userKYC.getType());
                    userKYCData.setImageUrl(userKYC.getImageUrl());
                    userKYCData.setIsVerified(userKYC.getIsVerified());
                    userKYCDatas.add(userKYCData);
                }
                userSignUpData.setUserKYCDatas(userKYCDatas);
            }
            //Get Education information
            List<UserEducation> userEducations = userEducationDAO.getEducationByUserId(userId, null);
            if (userEducations.size() > 0) {
                List<UserEducationData> userEducationDatas = new ArrayList<>();
                for (UserEducation userEducation : userEducations) {
                    UserEducationData userEducationData = new UserEducationData();
                    userEducationData.setId(userEducation.getId());
                    userEducationData.setUserId(userEducation.getUserId());
                    userEducationData.setInstitutionName(userEducation.getInstitutionName());
                    userEducationData.setDegreeType(userEducation.getType());
                    userEducationData.setDegreeCategoryName(educationDegreeDAO.get(userEducation.getDegreeCategory()).getName());
                    userEducationData.setDescription(userEducation.getDescription());
                    userEducationData.setStartDate(userEducation.getStartDate());
                    userEducationData.setEndDate(userEducation.getEndDate());
                    userEducationData.setCity(userEducation.getCity());
                    userEducationData.setState(userEducation.getState());
                    userEducationData.setCountry(userEducation.getCountry());
                    userEducationData.setPincode(userEducation.getPincode());
                    userEducationData.setReportUrl(userEducation.getReportImageUrl());
                    userEducationData.setIsVerified(userEducation.getIsVerified());
                    userEducationDatas.add(userEducationData);
                }
                userSignUpData.setUserEducationDatas(userEducationDatas);
            }
            return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS, 1, userSignUpData);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/location/{locationId}/verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse verifyUserLoacation(@PathParam("locationId") Long locationId) throws AppException {
        UserLocation userLocation = userLocationDAO.get(locationId);
        if (userLocation != null) {
            userLocation.setIsVerified(true);
            userLocationDAO.updateAndFlush(userLocation);
            return new BaseResponse(locationId, BaseResponse.ResponseCode.SUCCESS, "Address verified.");
        } else {
            return new BaseResponse(locationId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/education/{educationId}/verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse verifyUserEducation(@PathParam("educationId") Long educationId) throws AppException {
        UserEducation userEducation = userEducationDAO.get(educationId);
        if (userEducation != null) {
            userEducation.setIsVerified(true);
            userEducationDAO.updateAndFlush(userEducation);
            return new BaseResponse(educationId, BaseResponse.ResponseCode.SUCCESS, "Education details verified.");
        } else {
            return new BaseResponse(educationId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/kyc/{kycId}/verify")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse verifyUserKYC(@PathParam("kycId") Long kycId) throws AppException {
        UserKYC userKYC = userKycDAO.get(kycId);
        if (userKYC != null) {
            userKYC.setIsVerified(true);
            userKycDAO.updateAndFlush(userKYC);
            return new BaseResponse(kycId, BaseResponse.ResponseCode.SUCCESS, "KYC ID verified.");
        } else {
            return new BaseResponse(kycId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @PUT
    @Path("/agent/{userId}/update/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateUserDetails(@PathParam("userId") Long userId, UserSignUpData userSignUpData) throws AppException {
        User user = userDAO.get(userId);
        if (user != null) {
            user.setFirstname(userSignUpData.getFirstName());
            user.setMiddlename(userSignUpData.getMiddleName());
            user.setLastname(userSignUpData.getLastName());
            user.setEmail(userSignUpData.getEmail());
            user.setPhone(userSignUpData.getPhoneNumber());
            user.setDob(userSignUpData.getDob());
            user.setGender(userSignUpData.getGender());
            user.setFatherName(userSignUpData.getFatherName());
            user.setWorkStatus(userSignUpData.getWorkStatus());
            user.setResidentialStatus(userSignUpData.getResidentialStatus());
            user.setMaritalStatus(userSignUpData.getMaritalStatus());
            user.setLastUpdatedBy(-1l);
            userDAO.create(user);
            if (userSignUpData.getUserLocationDatas() != null) {
                createOrUpdateLocations(userId, userSignUpData.getUserLocationDatas());
            }
            return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, "Agent updated.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/offers")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getListOfOfferCodes(@QueryParam("offset") Integer offset,
                                            @QueryParam("pageSize") Integer pageSize,
                                            @QueryParam("searchField") String searchField,
                                            @QueryParam("searchText") String searchText,
                                            @QueryParam("sortField") String sortField,
                                            @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                            @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (offset != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<LoanOfferCode> loanOfferCodes = loanOfferCodeDAO.getOffercode(Status.ALL, paginationInfo);
        if (loanOfferCodes.size() > 0) {
            List<OfferCodeData> offerCodeDatas = new ArrayList<>();
            List<Long> userIdList = new ArrayList<>();
            for (LoanOfferCode loanOfferCode : loanOfferCodes) {
                if (!userIdList.contains(loanOfferCode.getUserId())) {
                    userIdList.add(loanOfferCode.getUserId());
                }
            }
            Map<Long, User> userMap = userDAO.get(userIdList);
            //Sort the data
            for (LoanOfferCode loanOfferCode : loanOfferCodes) {
                User user = userMap.get(loanOfferCode.getUserId());
                UserData userData = new UserData();
                userData.setId(user.getId());
                userData.setName(StringUtil.getName(user));
                userData.setEmail(user.getEmail());
                userData.setPhoneNumber(user.getPhone());
                userData.setGender(user.getGender());
                userData.setAge(DateUtil.getAge(user.getDob()));

                OfferCodeData offerCodeData = new OfferCodeData();
                offerCodeData.setId(loanOfferCode.getId());
                offerCodeData.setUserId(loanOfferCode.getUserId());
                offerCodeData.setUserData(userData);
                offerCodeData.setOfferCode(loanOfferCode.getCode());
                offerCodeData.setUsageCounter(loanOfferCode.getUsageCount());
                offerCodeData.setMaxUsageCounter(loanOfferCode.getUsageMaxCount());
                offerCodeData.setType(loanOfferCode.getType());
                offerCodeData.setStatus(loanOfferCode.getStatus());
                offerCodeDatas.add(offerCodeData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, offerCodeDatas.size(), offerCodeDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/offer/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createOfferCode(OfferCodeData offerCodeData) throws AppException {
        String offerCode = keyGenerator.generateCode(10);
        LoanOfferCode loanOfferCode = loanOfferCodeDAO.getOfferCodeByDetails(offerCode);
        if (loanOfferCode == null) {
            loanOfferCode = new LoanOfferCode();
            loanOfferCode.setUserId(offerCodeData.getUserId());
            loanOfferCode.setCode(offerCode);
            loanOfferCode.setType(offerCodeData.getType());
            loanOfferCode.setUsageMaxCount(offerCodeData.getMaxUsageCounter());
            loanOfferCode.setStatus(Status.ACTIVE);
            loanOfferCode.setCreatedBy(-1l);
            loanOfferCode.setLastUpdatedBy(-1l);
            loanOfferCodeDAO.create(loanOfferCode);
            return new BaseResponse(loanOfferCode.getId(), BaseResponse.ResponseCode.SUCCESS, "Offer code created successfully.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Oops! Something went wrong.");
        }
    }

    @GET
    @Path("/agents")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getListOfAgents(@QueryParam("offset") Integer offset,
                                        @QueryParam("pageSize") Integer pageSize,
                                        @QueryParam("searchField") String searchField,
                                        @QueryParam("searchText") String searchText,
                                        @QueryParam("sortField") String sortField,
                                        @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                        @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (offset != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<User> userDAOList = userDAO.getUserList(AccountType.AGENT, paginationInfo);
        if (userDAOList.size() > 0) {
            List<UserData> userDatas = new ArrayList<>();
            for (User user : userDAOList) {
                UserData userData = new UserData();
                userData.setId(user.getId());
                userData.setName(StringUtil.getName(user));
                userData.setEmail(user.getEmail());
                userData.setPhoneNumber(user.getPhone());
                userData.setGender(user.getGender());
                userData.setAge(DateUtil.getAge(user.getDob()));
                userDatas.add(userData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, userDatas.size(), userDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/agent/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createAgent(UserSignUpData userSignUpData) throws AppException {
        User user = userDAO.getUserByEmailAndPhone(userSignUpData.getEmail(), userSignUpData.getPhoneNumber());
        if (user == null) {
            user = new User();
            user.setFirstname(userSignUpData.getFirstName());
            user.setMiddlename(userSignUpData.getMiddleName());
            user.setLastname(userSignUpData.getLastName());
            user.setHash(desEncryption.encrypt("mpokket1234", null));
            user.setEmail(userSignUpData.getEmail());
            user.setPhone(userSignUpData.getPhoneNumber());
            user.setDob(userSignUpData.getDob());
            user.setGender(userSignUpData.getGender());
            user.setFatherName(userSignUpData.getFatherName());
            user.setWorkStatus(userSignUpData.getWorkStatus());
            user.setResidentialStatus(userSignUpData.getResidentialStatus());
            user.setMaritalStatus(userSignUpData.getMaritalStatus());
            user.setType(AccountType.AGENT);
            user.setCurrency(Currency.INR);
            user.setStatus(Status.ACTIVE);
            user.setCreatedBy(-1l);
            user.setLastUpdatedBy(-1l);
            userDAO.create(user);
            if (userSignUpData.getUserLocationDatas() != null) {
                createOrUpdateLocations(user.getId(), userSignUpData.getUserLocationDatas());
            }
            smtp.sendMail(user.getEmail(), "Welcome to mPokket - Agent", emailResponses.getSignUpMail(StringUtil.getName(user)));
            return new BaseResponse(user.getId(), BaseResponse.ResponseCode.SUCCESS, "Agent created.");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "User or Agent exists!");
        }
    }

    @DELETE
    @Path("/remove/user/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse removeUser(@PathParam("userId") Long id) throws AppException {
        User user = userDAO.get(id);
        //Delete KYC INFO
        List<UserKYC> userKYCs = userKycDAO.getUserKYCInfoById(id, null);
        if (userKYCs.size() > 0) {
            for (UserKYC userKYC : userKYCs) {
                userKYC.setStatus(Status.DELETED);
            }
            userKycDAO.bulkUpdate(userKYCs);
        }
        //Delete location info
        List<UserLocation> userLocations = userLocationDAO.getUserLocationInfoById(id, null);
        if (userLocations.size() > 0) {
            for (UserLocation userLocation : userLocations) {
                userLocation.setStatus(Status.DELETED);
            }
            userLocationDAO.bulkUpdate(userLocations);
        }
        //Delete education information
        List<UserEducation> userEducations = userEducationDAO.getEducationByUserId(id, null);
        if (userEducations.size() > 0) {
            for (UserEducation userEducation : userEducations) {
                userEducation.setStatus(Status.DELETED);
            }
            userEducationDAO.bulkUpdate(userEducations);
        }
        //TODO delete additional stuff
        user.setStatus(Status.DELETED);
        user.setLastUpdatedBy(id);
        userDAO.updateAndFlush(user);
        return new BaseResponse(id, BaseResponse.ResponseCode.SUCCESS);
    }

    private void createOrUpdateLocations(Long userId, List<UserLocationData> userLocationDatas) {
        if (userLocationDatas.size() > 0) {
            for (UserLocationData userLocationData : userLocationDatas) {
                UserLocation userLocation = userLocationDAO.getUserLocationByDetails(userId,
                        userLocationData.getCity(),
                        userLocationData.getState(),
                        userLocationData.getCountry(),
                        userLocationData.getPincode(),
                        userLocationData.getType());
                //Create if not present else update
                if (userLocation == null) {
                    userLocation = new UserLocation();
                    userLocation.setUserId(userId);
                    userLocation.setAddress(userLocationData.getAddress());
                    userLocation.setCity(userLocationData.getCity());
                    userLocation.setState(userLocationData.getState());
                    userLocation.setCountry(userLocationData.getCountry());
                    userLocation.setPincode(userLocationData.getPincode());
                    userLocation.setMonthsOfOccupation(new Date());
                    userLocation.setType(userLocationData.getType());
                    userLocation.setIsVerified(false);
                    userLocation.setStatus(Status.ACTIVE);
                    userLocation.setCreatedBy(userId);
                    userLocation.setLastUpdatedBy(userId);
                    userLocationDAO.create(userLocation);
                } else {
                    userLocation.setAddress(userLocationData.getAddress());
                    userLocation.setCity(userLocationData.getCity());
                    userLocation.setState(userLocationData.getState());
                    userLocation.setCountry(userLocationData.getCountry());
                    userLocation.setPincode(userLocationData.getPincode());
                    userLocation.setMonthsOfOccupation(new Date());
                    userLocation.setType(userLocationData.getType());
                    userLocation.setLastUpdatedBy(userId);
                    userLocationDAO.updateAndFlush(userLocation);
                }
            }
        }
    }

    private WalletAccountData getWalletInfo(Long userId, Long accountId) {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        BankAccount bankAccount = bankAccountDAO.get(accountId);
        if (bankAccount != null) {
            BankCodes bankCode = bankCodesDAO.get(bankAccount.getIfscId());
            BankCodesData bankCodesData = null;
            if (bankCode != null) {
                bankCodesData = new BankCodesData();
                bankCodesData.setId(bankCode.getId());
                bankCodesData.setIfscCode(bankCode.getIfscCode());
                bankCodesData.setBankName(bankCode.getBankName());
                bankCodesData.setBankBranch(bankCode.getBranch());
                bankCodesData.setBankAddress(bankCode.getAddress());
                bankCodesData.setBankCity(bankCode.getCity());
                bankCodesData.setBankDistrict(bankCode.getDistrict());
                bankCodesData.setBankState(bankCode.getState());
                if (bankCode.getLogo() != null) {
                    bankCodesData.setBankLogoUrl(bankCode.getLogo());
                }
            }
            //Add the wallet account details
            WalletAccountData walletAccountData = new WalletAccountData();
            walletAccountData.setId(bankAccount.getId());
            walletAccountData.setUserId(userId);
            walletAccountData.setWalletId(userWallet.getId());
            //Encapsulate the actual value
            walletAccountData.setBankAccount(StringUtil.hideData(
                    desEncryption.decrypt(bankAccount.getAccountId(),
                            desEncryption.decrypt(userWallet.getTransactionHash(), null))));
            walletAccountData.setBankRefId(bankAccount.getIfscId());
            walletAccountData.setIsPrimary(bankAccount.getIsPrimary());
            walletAccountData.setIsReceiveVerified(bankAccount.getIsReceiveVerified());
            walletAccountData.setIsSendVerified(bankAccount.getIsSendVerified());
            if (bankCodesData != null) {
                walletAccountData.setBankIFSCData(bankCodesData);
            }
            return walletAccountData;
        } else {
            return null;
        }
    }
}
