package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.LoanBorrowData;
import com.mbv.api.data.LoanData;
import com.mbv.api.data.LoanEventsData;
import com.mbv.api.data.LoanInterestData;
import com.mbv.api.data.LoanLendData;
import com.mbv.api.data.UserData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.LoanServices;
import com.mbv.api.util.DateUtil;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.response.EmailResponses;
import com.mbv.framework.util.GCMUtil;
import com.mbv.framework.util.KeyGenerator;
import com.mbv.framework.util.SMTP;
import com.mbv.persist.dao.BankAccountDAO;
import com.mbv.persist.dao.LoanBorrowerInfoDAO;
import com.mbv.persist.dao.LoanInstallmentDAO;
import com.mbv.persist.dao.LoanInterestDAO;
import com.mbv.persist.dao.LoanLenderInfoDAO;
import com.mbv.persist.dao.LoanOfferCodeDAO;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.UserDeviceDAO;
import com.mbv.persist.dao.UserEducationDAO;
import com.mbv.persist.dao.UserKycDAO;
import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.dao.UserScoreDAO;
import com.mbv.persist.dao.UserTimelineDAO;
import com.mbv.persist.dao.UserTransactionDAO;
import com.mbv.persist.dao.UserWalletDAO;
import com.mbv.persist.entity.BankAccount;
import com.mbv.persist.entity.LoanBorrowerInfo;
import com.mbv.persist.entity.LoanInstallment;
import com.mbv.persist.entity.LoanInterest;
import com.mbv.persist.entity.LoanLenderInfo;
import com.mbv.persist.entity.LoanOfferCode;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.UserDevice;
import com.mbv.persist.entity.UserEducation;
import com.mbv.persist.entity.UserKYC;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.entity.UserScore;
import com.mbv.persist.entity.UserTimeline;
import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.entity.UserWallet;
import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;
import com.mbv.persist.enums.UserTimelineType;
import com.sun.jersey.api.core.InjectParam;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 25/02/16.
 */
@Component
@Path("/mpokket/api/loans/")
public class LoanServicesImpl implements LoanServices {

    @InjectParam
    UserDAO userDAO;

    @InjectParam
    UserDeviceDAO userDeviceDAO;

    @InjectParam
    UserEducationDAO userEducationDAO;

    @InjectParam
    UserKycDAO userKycDAO;

    @InjectParam
    UserLocationDAO userLocationDAO;

    @InjectParam
    LoanBorrowerInfoDAO loanBorrowerInfoDAO;

    @InjectParam
    LoanLenderInfoDAO loanLenderInfoDAO;

    @InjectParam
    LoanInterestDAO loanInterestDAO;

    @InjectParam
    LoanInstallmentDAO loanInstallmentDAO;

    @InjectParam
    LoanOfferCodeDAO loanOfferCodeDAO;

    @InjectParam
    UserTimelineDAO userTimelineDAO;

    @InjectParam
    UserTransactionDAO userTransactionDAO;

    @InjectParam
    UserWalletDAO userWalletDAO;

    @InjectParam
    BankAccountDAO bankAccountDAO;

    @InjectParam
    UserScoreDAO userScoreDAO;

    @Autowired
    SMTP smtp;

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    EmailResponses emailResponses;

    @Autowired
    GCMUtil gcmUtil;

    @GET
    @Path("/user/{userId}/type/{type}/eligibility")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUserEligibility(@PathParam("userId") Long userId,
                                           @PathParam("type") RoleType type) throws AppException {
        //Check bank account
        BankAccount bankAccount = bankAccountDAO.getPrimaryBankAccountByUserId(userId);
        if(bankAccount == null) {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Primary bank account information missing.");
        } else {
            if(!bankAccount.getIsReceiveVerified() || !bankAccount.getIsSendVerified()) {
                return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Primary bank account not verified");
            }
        }
        //Check addresses
        List<UserLocation> userLocations = userLocationDAO.getUserLocationInfoById(userId, null);
        if (userLocations.size() > 0) {
            for (UserLocation userLocation : userLocations) {
                if (!userLocation.getIsVerified()) {
                    return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Your " +
                            userLocation.getType().toString() + " address could not be verified by the system.");
                }
            }
            //Check KYCs
            List<UserKYC> userKYCs = userKycDAO.getUserKYCInfoById(userId, null);
            if (userKYCs.size() > 0) {
                boolean identity = false, pan = false, address = false, student = false;
                for (UserKYC userKYC : userKYCs) {
                    switch (userKYC.getType()) {
                        case PAN:
                            identity = true;
                            pan = true;
                            break;
                        case ADHAAR:
                            identity = true;
                            address = true;
                            break;
                        case VOTER_ID:
                            identity = true;
                            address = true;
                            break;
                        case PASSPORT:
                            identity = true;
                            address = true;
                            break;
                        case BANK:
                            identity = true;
                            address = true;
                            break;
                        case STUDENT_ID:
                            student = true;
                            break;
                    }
                }
                //KYC check for borrower
                if (type == RoleType.BORROW) {
                    if (!identity && !pan && !address && !student) {
                        return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "KYC could not be verified.");
                    } else {
                        //Borrower education information
                        List<UserEducation> userEducations = userEducationDAO.getEducationByUserId(userId, null);
                        if(userEducations.size() > 0) {
                            for (UserEducation userEducation : userEducations) {
                                if (!userEducation.getIsVerified()) {
                                    return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Education information could not be verified.");
                                }
                            }
                        } else {
                            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Education information not sufficient.");
                        }
                    }
                } else { //KYC check for lender
                    if (!identity && !pan && !address) {
                        return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "KYC could not be verified.");
                    }
                }
                return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, "You are eligible.");
            } else {
                return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "KYC information not sufficient.");
            }
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Location information not sufficient.");
        }
    }


    @GET
    @Path("/offers")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getOfferCodes(@QueryParam("offset") Integer offset,
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
        List<LoanOfferCode> offerCodes = loanOfferCodeDAO.getOffercode(Status.ACTIVE, paginationInfo);
        if (offerCodes.size() > 0) {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, offerCodes.size(), offerCodes);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No offers available.");
        }
    }

    @GET
    @Path("/user/{userId}/offer/{offerId}/eligibility")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse verifyOfferCode(@PathParam("userId") Long userId,
                                        @PathParam("offerId") String offerCode) throws AppException {
        LoanOfferCode loanOfferCode = loanOfferCodeDAO.getOfferCodeByDetails(offerCode);
        if (loanOfferCode != null) {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, "Code verified!");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Invalid code.");
        }
    }

    @POST
    @Path("/user/{userId}/submit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createBorrowRequest(@PathParam("userId") Long userId, LoanBorrowData loanBorrowData) throws AppException {
        User user = userDAO.get(userId);
        UserDevice userDevice = userDeviceDAO.getDeviceById(userId, loanBorrowData.getDeviceData().getDeviceId());
        if (userDevice == null) {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "This device is not linked to our system.");
        } else if (userDevice.getStatus() == Status.ACTIVE) {
            UserScore userScore = userScoreDAO.getUserScoreInfoById(userId);
            if (loanBorrowerInfoDAO.getAvailableRequestsByUser(userId, Status.PENDING).size() > userScore.getLoanQuota()) {
                return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "You have exceeded your request quota!");
            }
            LoanInterest loanInterest = loanInterestDAO.getInterestByAmountAndPeriod(loanBorrowData.getAmount(),
                    loanBorrowData.getPeriod());
            if (loanInterest == null) {
                return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "Product information not found.");
            }
            String loanId = keyGenerator.generateLoanCode();
            LoanOfferCode loanOfferCode = null;
            if (loanBorrowData.getOfferCode() != null) {
                loanOfferCode = loanOfferCodeDAO.getOfferCodeByDetails(loanBorrowData.getOfferCode());
            }

            LoanBorrowerInfo loanBorrowerInfo = new LoanBorrowerInfo();
            loanBorrowerInfo.setBorrowerId(userId);
            loanBorrowerInfo.setLoanId(loanId);
            loanBorrowerInfo.setAmount(loanBorrowData.getAmount());
            loanBorrowerInfo.setDeviceId(userDevice.getId());
            loanBorrowerInfo.setInterestId(loanInterest.getId());
            loanBorrowerInfo.setPeriod(loanBorrowData.getPeriod());
            if (!StringUtil.isNullOrEmpty(loanBorrowData.getNote())) {
                loanBorrowerInfo.setNote(loanBorrowData.getNote());
            }
            if (!StringUtil.isNullOrEmpty(loanOfferCode)) {
                loanBorrowerInfo.setOfferCode(loanOfferCode.getId());
            }
            loanBorrowerInfo.setStatus(Status.DRAFT);
            loanBorrowerInfo.setCreatedBy(userId);
            loanBorrowerInfo.setLastUpdatedBy(userId);
            loanBorrowerInfoDAO.create(loanBorrowerInfo);

            if (!StringUtil.isNullOrEmpty(loanOfferCode)) {
                if (loanOfferCode.getUsageMaxCount() <= (loanOfferCode.getUsageCount() + 1l)) {
                    loanOfferCode.setStatus(Status.COMPLETED);
                } else {
                    loanOfferCode.setUsageCount(loanOfferCode.getUsageCount() + 1l);
                }
                loanOfferCodeDAO.updateAndFlush(loanOfferCode);
            }

            UserTimeline userTimeline = new UserTimeline();
            userTimeline.setUserId(userId);
            userTimeline.setType(UserTimelineType.LOAN_REQUESTED);
            userTimeline.setDescription(getBorrowDescription(user,
                    loanBorrowData.getPeriod(), loanId, loanBorrowData.getAmount()));
            userTimeline.setRefUserId(userId);
            userTimeline.setRefLoanId(loanBorrowerInfo.getId());
            userTimeline.setStatus(Status.ACTIVE);
            userTimeline.setCreatedBy(userId);
            userTimeline.setLastUpdatedBy(userId);
            userTimelineDAO.create(userTimeline);

            smtp.sendMail(user.getEmail(), "mPokket Money Request - " + loanId,
                    getBorrowEmailBody(user, loanBorrowerInfo));

            return new BaseResponse(loanBorrowerInfo.getId(), BaseResponse.ResponseCode.SUCCESS);
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "This device has been flagged as stolen!");
        }
    }

    @DELETE
    @Path("/user/{userId}/loan/{loanId}/remove")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse deleteBorrowRequest(@PathParam("userId") Long userId, @PathParam("loanId") Long loanId) throws AppException {
        LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(loanId);
        if (loanBorrowerInfo != null) {
            if (loanBorrowerInfo.getStatus() == Status.DRAFT) {
                loanBorrowerInfo.setStatus(Status.DELETED);
                loanBorrowerInfo.setLastUpdatedBy(userId);
                loanBorrowerInfoDAO.updateAndFlush(loanBorrowerInfo);
                return new BaseResponse(loanId, BaseResponse.ResponseCode.SUCCESS, "Borrow request removed from the system.");
            } else {
                return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "Sorry only requests with Draft status can be processed.");
            }
        } else {
            return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/open/requests")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getOpenLoanRequest(@PathParam("userId") Long userId,
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
        Map<String, Object> filterMap = PaginationMapper.getFilters(filters);
        //TODO filter logic here
        List<LoanBorrowerInfo> loanBorrowerInfos = loanBorrowerInfoDAO.getOpenBorrowRequests(userId, paginationInfo);
        if (loanBorrowerInfos.size() > 0) {
            List<LoanBorrowData> loanBorrowDatas = new ArrayList<>();
            List<Long> userIdList = new ArrayList<>();
            for(LoanBorrowerInfo loanBorrowerInfo : loanBorrowerInfos) {
                if(!userIdList.contains(loanBorrowerInfo.getBorrowerId())) {
                    userIdList.add(loanBorrowerInfo.getBorrowerId());
                }
            }
            Map<Long, User> userMap = userDAO.get(userIdList);
            for (LoanBorrowerInfo loanBorrowerInfo : loanBorrowerInfos) {
                User user = userMap.get(loanBorrowerInfo.getBorrowerId());
                UserData borrowerData = new UserData();
                borrowerData.setId(user.getId());
                borrowerData.setName(StringUtil.getName(user));
                borrowerData.setEmail(user.getEmail());
                borrowerData.setPhoneNumber(user.getPhone());
                borrowerData.setGender(user.getGender());
                borrowerData.setAge(DateUtil.getAge(user.getDob()));
                borrowerData.setImageURL(user.getImageUrl());

                LoanBorrowData loanBorrowData = new LoanBorrowData();
                loanBorrowData.setId(loanBorrowerInfo.getId());
                loanBorrowData.setLoanId(loanBorrowerInfo.getLoanId());
                loanBorrowData.setAmount(loanBorrowerInfo.getAmount());
                loanBorrowData.setPeriod(loanBorrowerInfo.getPeriod());
                loanBorrowData.setCreatedOn(loanBorrowerInfo.getCreatedDate());
                loanBorrowData.setUserData(borrowerData);
                loanBorrowDatas.add(loanBorrowData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, loanBorrowDatas.size(), loanBorrowDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No open request available.");
        }
    }

    @GET
    @Path("user/{userId}/details/loan/{loanId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getLoanDetails(@PathParam("userId") Long userId, @PathParam("loanId") Long loanId) throws AppException {
        LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(loanId);
        if (loanBorrowerInfo != null) {
            LoanData loanData = new LoanData();
            loanData.setId(loanId);
            loanData.setLoanId(loanBorrowerInfo.getLoanId());
            loanData.setAmount(loanBorrowerInfo.getAmount());
            loanData.setNote(loanBorrowerInfo.getNote());
            loanData.setPeriod(loanBorrowerInfo.getPeriod());
            loanData.setStatus(loanBorrowerInfo.getStatus());
            loanData.setRequestDate(loanBorrowerInfo.getLastUpdatedDate());

            User borrowUser = userDAO.get(loanBorrowerInfo.getBorrowerId());
            UserData borrowerData = new UserData();
            borrowerData.setId(borrowUser.getId());
            borrowerData.setName(StringUtil.getName(borrowUser));
            borrowerData.setEmail(borrowUser.getEmail());
            borrowerData.setPhoneNumber(borrowUser.getPhone());
            borrowerData.setGender(borrowUser.getGender());
            borrowerData.setImageURL(borrowerData.getImageURL());
            borrowerData.setAge(DateUtil.getAge(borrowUser.getDob()));
            loanData.setLoanBorrowerInfo(borrowerData);
            if (borrowUser.getId() == userId) {
                loanData.setRoleType(RoleType.BORROW);
            }

            LoanLenderInfo loanLenderInfo = loanLenderInfoDAO.getLenderDetailsByLoanId(loanId);
            if (loanLenderInfo != null) {
                User lendUser = userDAO.get(loanLenderInfo.getLenderId());
                UserData lenderData = new UserData();
                lenderData.setId(lendUser.getId());
                lenderData.setName(StringUtil.getName(lendUser));
                lenderData.setEmail(lendUser.getEmail());
                lenderData.setPhoneNumber(lendUser.getPhone());
                lenderData.setGender(lendUser.getGender());
                lenderData.setImageURL(lendUser.getImageUrl());
                lenderData.setAge(DateUtil.getAge(lendUser.getDob()));
                loanData.setLoanLenderInfo(lenderData);
                if (lendUser.getId() == userId) {
                    loanData.setRoleType(RoleType.LEND);
                }
            }

            List<LoanInstallment> loanInstallments = loanInstallmentDAO.getInstallmentDetailsByLoanId(loanId);
            if (loanInstallments.size() > 0) {
                List<LoanEventsData> loanEventsDatas = new ArrayList<>();
                for(LoanInstallment loanInstallment : loanInstallments) {
                    LoanEventsData loanEventsData = new LoanEventsData();
                    loanEventsData.setId(loanInstallment.getId());
                    loanEventsData.setLoanId(loanInstallment.getLoanId());
                    loanEventsData.setLoanRefId(loanBorrowerInfo.getLoanId());
                    loanEventsData.setAmount(loanInstallment.getProjectedAmount());
                    loanEventsData.setDate(loanInstallment.getProjectedDate());
                    loanEventsData.setStatus(loanInstallment.getStatus());
                    loanEventsDatas.add(loanEventsData);
                }
                loanData.setLoanInstallmentData(loanEventsDatas);
            }

            LoanInterest loanInterest = loanInterestDAO.get(loanBorrowerInfo.getInterestId());
            if(loanInterest != null) {
                LoanInterestData loanInterestData = new LoanInterestData();
                loanInterestData.setAgentFee(loanInterest.getAgentFee());
                loanInterestData.setServiceTax(loanInterest.getServiceTax());
                loanInterestData.setAppUsageFee(loanInterest.getAppUsageFee());
                loanInterestData.setInterest(loanInterest.getLoanInterest());
                loanInterestData.setDefaultRate(loanInterest.getDefaultRate());
                loanData.setLoanInterestData(loanInterestData);
            }

            return new BaseResponse(loanId, BaseResponse.ResponseCode.SUCCESS, 1, loanData);
        } else {
            return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("user/{userId}/loan/{loanId}/accept")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createLendRequest(@PathParam("userId") Long userId,
                                          @PathParam("loanId") Long loanId,
                                          LoanLendData loanLendData) throws AppException {
        User lender = userDAO.get(userId);
        LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(loanId);
        if (loanBorrowerInfo != null) {
            if (loanBorrowerInfo.getStatus() == Status.DRAFT) {
                UserDevice userDevice = userDeviceDAO.getDeviceById(userId, loanLendData.getDeviceData().getDeviceId());
                if (userDevice != null) {
                    //Check wallet amount
                    UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
                    if (userWallet != null) {
                        if (userWallet.getAmount() < loanBorrowerInfo.getAmount()) {
                            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Insufficient funds!");
                        }
                    } else {
                        return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Wallet information not available");
                    }
                    //Cannot be the same user
                    User borrower = userDAO.get(loanBorrowerInfo.getBorrowerId());
                    if (userId == borrower.getId()) {
                        return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "You cannot approve this request!");
                    }
                    //Check for offer code
                    LoanOfferCode loanOfferCode = null;
                    if (loanLendData.getOfferCode() != null) {
                        loanOfferCode = loanOfferCodeDAO.getOfferCodeByDetails(loanLendData.getOfferCode());
                    }
                    //Check for primary account
                    BankAccount bankAccount = bankAccountDAO.getPrimaryBankAccountByUserId(userId);
                    if(bankAccount == null) {
                        return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Primary bank account not found.");
                    }
                    //Create the lender request
                    LoanLenderInfo loanLenderInfo = new LoanLenderInfo();
                    loanLenderInfo.setLoanId(loanId);
                    loanLenderInfo.setLenderId(userId);
                    loanLenderInfo.setDeviceId(userDevice.getId());
                    loanLenderInfo.setAmount(loanLendData.getAmount());
                    if (!StringUtil.isNullOrEmpty(loanOfferCode)) {
                        loanLenderInfo.setOfferCode(loanOfferCode.getId());
                    }
                    if (!StringUtil.isNullOrEmpty(loanLendData.getNote())) {
                        loanLenderInfo.setNote(loanLendData.getNote());
                    }
                    loanLenderInfo.setStatus(Status.PENDING);
                    loanLenderInfo.setCreatedBy(userId);
                    loanLenderInfo.setLastUpdatedBy(userId);
                    loanLenderInfoDAO.create(loanLenderInfo);
                    //Update the coupon code usage counter
                    if (!StringUtil.isNullOrEmpty(loanOfferCode)) {
                        if (loanOfferCode.getUsageMaxCount() <= (loanOfferCode.getUsageCount() + 1l)) {
                            loanOfferCode.setStatus(Status.COMPLETED);
                        } else {
                            loanOfferCode.setUsageCount(loanOfferCode.getUsageCount() + 1l);
                        }
                        loanOfferCodeDAO.updateAndFlush(loanOfferCode);
                    }
                    //Set borrow request to pending
                    loanBorrowerInfo.setStatus(Status.PENDING);
                    loanBorrowerInfoDAO.updateAndFlush(loanBorrowerInfo);
                    //Update the timeline information for the lender and borrower
                    UserTimeline lenderTimeline = new UserTimeline();
                    lenderTimeline.setUserId(lender.getId());
                    lenderTimeline.setType(UserTimelineType.APPROVED_LOAN);
                    lenderTimeline.setDescription(getLendDescription(lender,
                            loanBorrowerInfo.getPeriod(),
                            loanBorrowerInfo.getLoanId(),
                            loanBorrowerInfo.getAmount()));
                    lenderTimeline.setRefUserId(lender.getId());
                    lenderTimeline.setRefLoanId(loanId);
                    lenderTimeline.setStatus(Status.ACTIVE);
                    lenderTimeline.setCreatedBy(lender.getId());
                    lenderTimeline.setLastUpdatedBy(lender.getId());
                    userTimelineDAO.create(lenderTimeline);
                    //Initiate a transaction
                    String transactionId = keyGenerator.generateTransactionCode(
                            userWallet.getId(), userTransactionDAO.getLastInsertedId());
                    UserTransaction userTransaction = new UserTransaction();
                    userTransaction.setTransactionId(transactionId);
                    userTransaction.setFromUserId(userId);
                    userTransaction.setFromWalletId(userWallet.getId());
                    userTransaction.setFromAccountId(bankAccount.getId());
                    userTransaction.setToUserId(loanBorrowerInfo.getBorrowerId());
                    userTransaction.setToAccountId(bankAccountDAO.getPrimaryBankAccountByUserId(loanBorrowerInfo.getBorrowerId()).getId());
                    userTransaction.setToWalletId(userWalletDAO.getWalletInfoByUserId(loanBorrowerInfo.getBorrowerId()).getId());
                    userTransaction.setAmount(loanBorrowerInfo.getAmount());
                    userTransaction.setType(TransactionType.LOAN_SEND);
                    userTransaction.setLoanId(loanId);
                    userTransaction.setStatus(Status.PENDING);
                    userTransaction.setCreatedBy(userId);
                    userTransaction.setLastUpdatedBy(userId);
                    userTransactionDAO.create(userTransaction);
                    //Update wallet amount
                    userWallet.setAmount(userWallet.getAmount() - loanBorrowerInfo.getAmount());
                    userWalletDAO.updateAndFlush(userWallet);
                    //Push Notification
                    pushApproveLenderRequest(userDAO.get(loanBorrowerInfo.getBorrowerId()), loanBorrowerInfo);
                    return new BaseResponse(lender.getId(), BaseResponse.ResponseCode.SUCCESS, "Loan application sent successfully.");
                } else {
                    return new BaseResponse(lender.getId(), BaseResponse.ResponseCode.FAILURE, "This device is not linked to our system.");
                }
            } else {
                return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "This request is no longer available!");
            }
        } else {
            return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }

    }

    @GET
    @Path("/user/{userId}/loan/{loanId}/approve")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse acceptLendRequest(@PathParam("userId") Long userId, @PathParam("loanId") Long loanId) throws AppException {
        LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(loanId);
        if(loanBorrowerInfo != null) {
            if(loanBorrowerInfo.getStatus() == Status.PENDING) {
                //Get the wallet and account details
                UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
                BankAccount bankAccount = bankAccountDAO.getPrimaryBankAccountByUserId(userId);
                if(bankAccount == null) {
                    return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Primary bank account not found.");
                }
                //Set the loan to active
                loanBorrowerInfo.setStatus(Status.ACTIVE);
                loanBorrowerInfoDAO.updateAndFlush(loanBorrowerInfo);
                //Set the lender request to active
                LoanLenderInfo loanLenderInfo = loanLenderInfoDAO.getLenderDetailsByLoanId(loanId);
                loanLenderInfo.setStatus(Status.ACTIVE);
                loanLenderInfoDAO.updateAndFlush(loanLenderInfo);
                //Insert Borrower transaction
                String transactionId = keyGenerator.generateTransactionCode(
                        userWallet.getId(), userTransactionDAO.getLastInsertedId());
                UserTransaction userTransaction = new UserTransaction();
                userTransaction.setTransactionId(transactionId);
                userTransaction.setFromUserId(loanLenderInfo.getLenderId());
                userTransaction.setFromAccountId(bankAccountDAO.getPrimaryBankAccountByUserId(loanLenderInfo.getLenderId()).getId());
                userTransaction.setFromWalletId(userWalletDAO.getWalletInfoByUserId(loanLenderInfo.getLenderId()).getId());
                userTransaction.setToUserId(userId);
                userTransaction.setToWalletId(userWallet.getId());
                userTransaction.setToAccountId(bankAccount.getId());
                userTransaction.setAmount(loanBorrowerInfo.getAmount());
                userTransaction.setType(TransactionType.LOAN_RECEIVE);
                userTransaction.setLoanId(loanId);
                userTransaction.setStatus(Status.PENDING);
                userTransaction.setCreatedBy(userId);
                userTransaction.setLastUpdatedBy(userId);
                userTransactionDAO.create(userTransaction);
                //Insert timeline event
                UserTimeline borrowerTimeline = new UserTimeline();
                borrowerTimeline.setUserId(loanBorrowerInfo.getBorrowerId());
                borrowerTimeline.setType(UserTimelineType.LOAN_APPROVED);
                borrowerTimeline.setDescription(getBorrowAcceptedDescription(
                        loanBorrowerInfo.getPeriod(),
                        loanBorrowerInfo.getLoanId(),
                        loanBorrowerInfo.getAmount()));
                borrowerTimeline.setRefUserId(userId);
                borrowerTimeline.setRefLoanId(loanId);
                borrowerTimeline.setStatus(Status.ACTIVE);
                borrowerTimeline.setCreatedBy(userId);
                borrowerTimeline.setLastUpdatedBy(userId);
                userTimelineDAO.create(borrowerTimeline);
                pushBorrowerApprovedLenderRequest(userDAO.get(loanLenderInfo.getLenderId()), loanBorrowerInfo);
                return new BaseResponse(loanId, BaseResponse.ResponseCode.SUCCESS, "Request updated successfully.");
            } else {
                return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "This request is no longer available!");
            }
        } else {
            return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/loan/{loanId}/decline")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse declineLendRequest(@PathParam("userId") Long userId, @PathParam("loanId") Long loanId) throws AppException {
        LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(loanId);
        if(loanBorrowerInfo != null) {
            if(loanBorrowerInfo.getStatus() == Status.PENDING) {
                //Cancel the request
                LoanLenderInfo loanLenderInfo = loanLenderInfoDAO.getLenderDetailsByLoanId(loanId);
                loanLenderInfo.setStatus(Status.CANCELLED);
                loanLenderInfoDAO.updateAndFlush(loanLenderInfo);
                //Revert the cash back to the lender
                UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(loanLenderInfo.getId());
                userWallet.setAmount(userWallet.getAmount() + loanLenderInfo.getAmount());
                userWalletDAO.updateAndFlush(userWallet);
                //Insert reverted amount data to timeline
                UserTimeline borrowerTimeline = new UserTimeline();
                borrowerTimeline.setUserId(loanBorrowerInfo.getBorrowerId());
                borrowerTimeline.setType(UserTimelineType.WALLET);
                borrowerTimeline.setDescription("The amount of " + loanLenderInfo.getAmount() + "INR was " +
                        "reverted back to your wallet as your lend approve declined by the borrower. " +
                        "Current wallet balance is " + userWallet.getAmount() + "INR.");
                borrowerTimeline.setRefUserId(userId);
                borrowerTimeline.setRefLoanId(loanId);
                borrowerTimeline.setStatus(Status.ACTIVE);
                borrowerTimeline.setCreatedBy(userId);
                borrowerTimeline.setLastUpdatedBy(userId);
                userTimelineDAO.create(borrowerTimeline);
                return new BaseResponse(loanId, BaseResponse.ResponseCode.SUCCESS, "Request updated successfully.");
            } else {
                return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "This request is no longer available!");
            }
        } else {
            return new BaseResponse(loanId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("user/{userId}/history/type/{type}/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getLoanHistory(@PathParam("userId") Long userId,
                                       @PathParam("type") RoleType type,
                                       @PathParam("status") Status status,
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
        List<LoanBorrowerInfo> loanBorrowerInfos = new ArrayList<>();
        List<LoanLenderInfo> loanLenderInfos = new ArrayList<>();
        if(type == RoleType.ALL) {
            loanBorrowerInfos = loanBorrowerInfoDAO.getAvailableRequestsByUser(userId, status);
            loanLenderInfos = loanLenderInfoDAO.getLenderDetailsByUserId(userId, status);
        } else if(type == RoleType.LEND) {
            loanLenderInfos = loanLenderInfoDAO.getLenderDetailsByUserId(userId, status);
        } else if(type == RoleType.BORROW) {
            loanBorrowerInfos = loanBorrowerInfoDAO.getAvailableRequestsByUser(userId, status);
        }

        Map<Long, LoanBorrowerInfo> loanBorrowerInfoMap = new HashMap<>();
        if(loanLenderInfos.size() > 0) {
            List<Long> loanIdList = new ArrayList<>();
            for(LoanLenderInfo loanLenderInfo : loanLenderInfos) {
                if(!loanIdList.contains(loanLenderInfo.getLoanId())) {
                    loanIdList.add(loanLenderInfo.getLoanId());
                }
            }
            loanBorrowerInfoMap = loanBorrowerInfoDAO.get(loanIdList);
        }

        if(loanBorrowerInfos.size() > 0 || loanLenderInfos.size() > 0) {
            List<LoanEventsData> loanEventsDatas = new ArrayList<>();
            if(loanBorrowerInfos.size() > 0) {
                for (LoanBorrowerInfo loanBorrowerInfo : loanBorrowerInfos) {
                    LoanEventsData loanEventsData = new LoanEventsData();
                    loanEventsData.setId(loanBorrowerInfo.getId());
                    loanEventsData.setLoanId(loanBorrowerInfo.getId());
                    loanEventsData.setLoanRefId(loanBorrowerInfo.getLoanId());
                    loanEventsData.setAmount(loanBorrowerInfo.getAmount());
                    loanEventsData.setDate(loanBorrowerInfo.getCreatedDate());
                    loanEventsData.setStatus(loanBorrowerInfo.getStatus());
                    loanEventsDatas.add(loanEventsData);
                }
            }
            if(loanLenderInfos.size() > 0) {
                for(LoanLenderInfo loanLenderInfo : loanLenderInfos) {
                    LoanEventsData loanEventsData = new LoanEventsData();
                    loanEventsData.setId(loanLenderInfo.getId());
                    loanEventsData.setLoanId(loanLenderInfo.getLoanId());
                    loanEventsData.setLoanRefId(loanBorrowerInfoMap.get(loanLenderInfo.getLoanId()).getLoanId());
                    loanEventsData.setAmount(loanLenderInfo.getAmount());
                    loanEventsData.setDate(loanLenderInfo.getCreatedDate());
                    loanEventsData.setStatus(loanLenderInfo.getStatus());
                    loanEventsDatas.add(loanEventsData);
                }
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, loanEventsDatas.size(), loanEventsDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("user/{userId}/events")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getUpcomingEvents(@PathParam("userId") Long userId) throws AppException {
        Map<RoleType, List<LoanEventsData>> loanInstallments = new HashMap<>();
        List<LoanInstallment> lenderInstallment = loanInstallmentDAO.getUpcomingInstallmentsByUserId(userId, true);
        List<LoanInstallment> borrowerInstallment = loanInstallmentDAO.getUpcomingInstallmentsByUserId(userId, false);
        //Compile the list of loan information
        List<Long> loanIdList = new ArrayList<>();
        if(lenderInstallment.size() > 0) {
            for(LoanInstallment loanInstallment : lenderInstallment) {
                if(!loanIdList.contains(loanInstallment.getLoanId())) {
                    loanIdList.add(loanInstallment.getLoanId());
                }
            }
        }
        if(borrowerInstallment.size() > 0) {
            for(LoanInstallment loanInstallment : borrowerInstallment) {
                if(!loanIdList.contains(loanInstallment.getLoanId())) {
                    loanIdList.add(loanInstallment.getLoanId());
                }
            }
        }
        Map<Long, LoanBorrowerInfo> loanBorrowerInfoMap = new HashMap<>();
        if(loanIdList.size() > 0) {
            loanBorrowerInfoMap = loanBorrowerInfoDAO.get(loanIdList);
        }
        //Form the loan events dataset based on lend and borrow
        if (lenderInstallment.size() > 0) {
            List<LoanEventsData> loanEventsDatas = new ArrayList<>();
            for(LoanInstallment loanInstallment : lenderInstallment) {
                LoanEventsData loanEventsData = new LoanEventsData();
                loanEventsData.setId(loanInstallment.getId());
                loanEventsData.setLoanId(loanInstallment.getLoanId());
                loanEventsData.setLoanRefId(loanBorrowerInfoMap.get(loanInstallment.getLoanId()).getLoanId());
                loanEventsData.setAmount(loanInstallment.getProjectedAmount());
                loanEventsData.setDate(loanInstallment.getProjectedDate());
                loanEventsData.setStatus(loanInstallment.getStatus());
                loanEventsDatas.add(loanEventsData);
            }
            loanInstallments.put(RoleType.LEND, loanEventsDatas);
        }
        if (borrowerInstallment.size() > 0) {
            List<LoanEventsData> loanEventsDatas = new ArrayList<>();
            for(LoanInstallment loanInstallment : borrowerInstallment) {
                LoanEventsData loanEventsData = new LoanEventsData();
                loanEventsData.setId(loanInstallment.getId());
                loanEventsData.setLoanId(loanInstallment.getLoanId());
                loanEventsData.setLoanRefId(loanBorrowerInfoMap.get(loanInstallment.getLoanId()).getLoanId());
                loanEventsData.setAmount(loanInstallment.getProjectedAmount());
                loanEventsData.setDate(loanInstallment.getProjectedDate());
                loanEventsData.setStatus(loanInstallment.getStatus());
                loanEventsDatas.add(loanEventsData);
            }
            loanInstallments.put(RoleType.BORROW, loanEventsDatas);
        }
        if (loanInstallments.size() > 0) {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, loanInstallments.size(), loanInstallments);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    private String getBorrowDescription(User user, Long period, String loanId, Long amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.getName(user));
        sb.append(" ");
        sb.append("has raised a borrow request of " + String.valueOf(amount) + " INR for a period of ");
        sb.append(String.valueOf(period) + " month.");
        sb.append("Ref Id: " + loanId);
        return sb.toString();
    }

    private String getBorrowAcceptedDescription(Long period, String loanId, Long amount) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your mPokket request of " + String.valueOf(amount) + " INR for a period of ");
        sb.append(String.valueOf(period) + " month, has been approved.");
        sb.append("Ref Id: " + loanId);
        return sb.toString();
    }

    private String getLendDescription(User user, Long period, String loanId, Long amount) {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.getName(user));
        sb.append(" ");
        sb.append("has approved a borrow request of " + String.valueOf(amount) + " INR for a period of ");
        sb.append(String.valueOf(period) + " month.");
        sb.append("Ref Id: " + loanId);
        return sb.toString();
    }

    private String getBorrowEmailBody(User user, LoanBorrowerInfo loanBorrowerInfo) {
        return emailResponses.getLoanApplicationEmailBody(StringUtil.getName(user),
                loanBorrowerInfo.getAmount(),
                loanBorrowerInfo.getPeriod(),
                loanBorrowerInfo.getLoanId());
    }

    private void pushApproveLenderRequest(User user, LoanBorrowerInfo loanBorrowerInfo) {
        if(user.getGcmId() != null) {
            JSONObject data = new JSONObject();
            data.put("to", user.getGcmId());
            JSONObject loan = new JSONObject();
            loan.put("loanId", loanBorrowerInfo.getId());
            JSONObject notification = new JSONObject();
            notification.put("title", "mPokket Approval");
            notification.put("text", "Your borrow request " + loanBorrowerInfo.getLoanId() + ", has been approved by a lender. Please accept the request to process transaction.");
            data.put("data", loan);
            data.put("notification", notification);
            gcmUtil.pushNotification(data);
        }
    }

    private void pushBorrowerApprovedLenderRequest(User user, LoanBorrowerInfo loanBorrowerInfo) {
        if(user.getGcmId() != null) {
            JSONObject data = new JSONObject();
            data.put("to", user.getGcmId());
            JSONObject loan = new JSONObject();
            loan.put("loanId", loanBorrowerInfo.getId());
            JSONObject notification = new JSONObject();
            notification.put("title", "mPokket Approval");
            notification.put("text", "Your lend request " + loanBorrowerInfo.getLoanId() + ", has been accepted by the borrower. The transaction will be processed shortly.");
            data.put("data", loan);
            data.put("notification", notification);
            gcmUtil.pushNotification(data);
        }
    }
}
