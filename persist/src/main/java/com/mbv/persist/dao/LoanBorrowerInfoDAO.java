package com.mbv.persist.dao;

import com.mbv.persist.entity.LoanBorrowerInfo;
import com.mbv.persist.enums.Status;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface LoanBorrowerInfoDAO extends BaseEntityDAO<Long,LoanBorrowerInfo> {

    public List<LoanBorrowerInfo> getAvailableRequestsByUser(Long userId, Status status);

    public List<LoanBorrowerInfo> getOpenBorrowRequests(Long userId, Map<Object, Object> paginationInfo);
}
