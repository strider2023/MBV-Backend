package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.UserTransactionDAO;
import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by arindamnath on 25/02/16.
 */
public class UserTransactionDAOImpl extends BaseEntityDAOImpl<UserTransaction> implements UserTransactionDAO {

    public UserTransactionDAOImpl() {
        super(UserTransaction.class);
    }

    @Override
    public UserTransaction getTransactionByUserId(Long userId, Long walletId, String transactionId) {
        Criteria criteria = this.getSession().createCriteria(UserTransaction.class);
        criteria.add(Restrictions.eq("fromUserId", userId));
        criteria.add(Restrictions.eq("fromWalletId", walletId));
        criteria.add(Restrictions.eq("transactionId", transactionId));
        return (UserTransaction) criteria.uniqueResult();
    }

    @Override
    public List<UserTransaction> getTransactionInfoByStatus(Long userId, Status status, Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserTransaction.class);
        criteria.add(Restrictions.eq("fromUserId", userId));
        if(status == Status.ALL) {
            criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        } else {
            criteria.add(Restrictions.eq("status", status));
        }
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserTransaction> userTransactions = criteria.list();
        evictCollection(userTransactions);
        return userTransactions;
    }

    @Override
    public List<UserTransaction> getTransactionsByTypeAndStatus(Status status, TransactionType transactionType,
                                                                Map<Object, Object> paginationInfo) {
        Criteria criteria = this.getSession().createCriteria(UserTransaction.class);
        //Set transaction type
        if(transactionType == TransactionType.ALL) {
            criteria.add(Restrictions.in("type", Arrays.asList(TransactionType.values())));
        } else {
            criteria.add(Restrictions.eq("type", transactionType));
        }
        //Set status
        if(status == Status.ALL) {
            criteria.add(Restrictions.in("status", Arrays.asList(Status.PENDING, Status.ACTIVE, Status.COMPLETED)));
        } else {
            criteria.add(Restrictions.eq("status", status));
        }
        if(paginationInfo != null) {
            applyPagination(paginationInfo, criteria);
        }
        List<UserTransaction> userTransactions = criteria.list();
        evictCollection(userTransactions);
        return userTransactions;
    }

    @Override
    public Long getLastInsertedId() {
        Criteria criteria = this.getSession().createCriteria(UserTransaction.class);
        criteria.addOrder(Order.desc("createdDate"));
        criteria.setMaxResults(1);
        UserTransaction userTransaction = (UserTransaction) criteria.uniqueResult();
        this.getSession().clear();
        return (userTransaction != null) ? userTransaction.getId() : 1l;
    }
}
