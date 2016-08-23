package com.mbv.persist.dao;

import com.mbv.persist.entity.BankAccount;

import java.util.List;

/**
 * Created by arindamnath on 04/03/16.
 */
public interface BankAccountDAO extends BaseEntityDAO<Long,BankAccount> {

    public List<BankAccount> getAllActiveBankAccountsByUser(Long userId);

    public BankAccount getBankAccountNumber(Long userId, Long walletId, String accountNumber);

    public BankAccount getPrimaryBankAccountByUserId(Long userId);

    public Long getLastEntryId();
}
