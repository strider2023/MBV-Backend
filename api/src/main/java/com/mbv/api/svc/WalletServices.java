package com.mbv.api.svc;

import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.WalletAccountData;
import com.mbv.api.data.WalletData;
import com.mbv.api.data.WalletTransactionData;
import com.mbv.framework.exception.AppException;
import com.mbv.persist.enums.Status;

/**
 * Created by arindamnath on 04/03/16.
 */
public interface WalletServices {

    public BaseResponse getBankCodes(Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getWalletInfo(Long userId) throws AppException;

    public BaseResponse createUserWallet(Long userId, WalletData walletData) throws AppException;

    public BaseResponse addBankAccount(Long userId, WalletAccountData walletAccountData) throws AppException;

    public BaseResponse updateBankAccount(Long userId, Long accId, WalletAccountData walletAccountData) throws AppException;

    public BaseResponse removeBankAccount(Long userId, Long accId, WalletData walletData) throws AppException;

    public BaseResponse getBankAccounts(Long userId, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse getTransactions(Long userId, Status status, Integer offset, Integer pageSize, String searchField, String searchText, String sortField, Boolean sortInAscOrder, String filters) throws AppException;

    public BaseResponse addAmountToWallet(Long userId, WalletTransactionData walletTransactionData) throws AppException;
}
