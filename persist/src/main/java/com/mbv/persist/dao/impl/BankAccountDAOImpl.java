package com.mbv.persist.dao.impl;

import com.mbv.persist.dao.BankAccountDAO;
import com.mbv.persist.entity.BankAccount;
import com.mbv.persist.enums.Status;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * Created by arindamnath on 04/03/16.
 */
public class BankAccountDAOImpl extends BaseEntityDAOImpl<BankAccount> implements BankAccountDAO {

    public BankAccountDAOImpl() { super(BankAccount.class); }

    @Override
    public List<BankAccount> getAllActiveBankAccountsByUser(Long userId) {
        Criteria criteria = this.getSession().createCriteria(BankAccount.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        List<BankAccount> bankAccounts = criteria.list();
        evictCollection(bankAccounts);
        return bankAccounts;
    }

    @Override
    public BankAccount getBankAccountNumber(Long userId, Long walletId, String accountNumber) {
        Criteria criteria = this.getSession().createCriteria(BankAccount.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("accountId", accountNumber));
        criteria.add(Restrictions.eq("walletId", walletId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        return (BankAccount) criteria.uniqueResult();
    }

    @Override
    public BankAccount getPrimaryBankAccountByUserId(Long userId) {
        Criteria criteria = this.getSession().createCriteria(BankAccount.class);
        criteria.add(Restrictions.eq("userId", userId));
        criteria.add(Restrictions.eq("status", Status.ACTIVE));
        criteria.add(Restrictions.eq("isPrimary", true));
        return (BankAccount) criteria.uniqueResult();
    }

    @Override
    public Long getLastEntryId() {
        Criteria criteria = this.getSession().createCriteria(BankAccount.class);
        criteria.addOrder(Order.desc("createdDate"));
        criteria.setMaxResults(1);
        BankAccount bankAccount = (BankAccount) criteria.uniqueResult();
        this.getSession().clear();
        return (bankAccount != null) ? bankAccount.getId() : 1l;
    }
}
