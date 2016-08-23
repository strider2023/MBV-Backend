package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.LoanBorrowData;
import com.mbv.api.data.LoanLendData;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.enums.RoleType;
import com.mbv.persist.enums.Status;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanServices {

    public BaseResponse getUserEligibility(Long userId, RoleType type) throws AppException;

    public BaseResponse getOfferCodes(Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse verifyOfferCode(Long userId, String offerCode) throws AppException;

    public BaseResponse createBorrowRequest(Long userId, LoanBorrowData loanBorrowData) throws AppException;

    public BaseResponse deleteBorrowRequest(Long userId, Long loanId) throws AppException;

    public BaseResponse getOpenLoanRequest(Long userId, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getLoanDetails(Long userId, Long loanId) throws AppException;

    public BaseResponse createLendRequest(Long userId, Long loanId, LoanLendData loanLendData) throws AppException;

    public BaseResponse acceptLendRequest(Long userId, Long loanId) throws AppException;

    public BaseResponse declineLendRequest(Long userId, Long loanId) throws AppException;

    public BaseResponse getLoanHistory(Long userId, RoleType type, Status status, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getUpcomingEvents(Long userId) throws AppException;
}
