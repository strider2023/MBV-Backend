package com.mbv.persist.dao;

import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;

import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public interface UserTransactionDAO extends BaseEntityDAO<Long,UserTransaction> {

    public UserTransaction getTransactionByUserId(Long userId, Long walletId, String transactionId);

    public List<UserTransaction> getTransactionInfoByStatus(Long userId, Status status, Map<Object, Object> paginationInfo);

    public List<UserTransaction> getTransactionsByTypeAndStatus(Status status, TransactionType transactionType, Map<Object, Object> paginationInfo);

    public Long getLastInsertedId();
}
