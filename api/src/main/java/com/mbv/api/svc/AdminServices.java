package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.OfferCodeData;
import com.mbv.api.data.UserSignUpData;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;

/**
 * Created by arindamnath on 30/03/16.
 */
public interface AdminServices {

    public BaseResponse getListOfUsers(Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getListOfTransactions(Status status, TransactionType type, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getTransactionDetails(Long transactionId) throws AppException;

    public BaseResponse updateTransactionStatus(Long transactionId, Status status) throws AppException;

    public BaseResponse getUserDetails(Long userId) throws AppException;

    public BaseResponse verifyUserLoacation(Long locationId) throws AppException;

    public BaseResponse verifyUserEducation(Long educationId) throws AppException;

    public BaseResponse verifyUserKYC(Long educationId) throws AppException;

    public BaseResponse updateUserDetails(Long userId, UserSignUpData userSignUpData) throws AppException;

    public BaseResponse getListOfOfferCodes(Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse createOfferCode(OfferCodeData offerCodeData) throws AppException;

    public BaseResponse getListOfAgents(Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse createAgent(UserSignUpData userSignUpData) throws AppException;

    public BaseResponse removeUser(Long id) throws AppException;
}
