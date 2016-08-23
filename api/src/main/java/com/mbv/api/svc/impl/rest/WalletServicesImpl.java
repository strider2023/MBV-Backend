package com.mbv.api.svc.impl.rest;

import com.mbv.api.data.BankCodesData;
import com.mbv.api.data.BaseResponse;
import com.mbv.api.data.WalletAccountData;
import com.mbv.api.data.WalletData;
import com.mbv.api.data.WalletTransactionData;
import com.mbv.api.filter.pagination.PaginationMapper;
import com.mbv.api.svc.WalletServices;
import com.mbv.api.util.StringUtil;
import com.mbv.framework.exception.AppException;
import com.mbv.framework.util.DESEncryption;
import com.mbv.framework.util.KeyGenerator;
import com.mbv.persist.dao.BankAccountDAO;
import com.mbv.persist.dao.BankCodesDAO;
import com.mbv.persist.dao.UserDeviceDAO;
import com.mbv.persist.dao.UserTransactionDAO;
import com.mbv.persist.dao.UserWalletDAO;
import com.mbv.persist.entity.BankAccount;
import com.mbv.persist.entity.BankCodes;
import com.mbv.persist.entity.UserDevice;
import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.entity.UserWallet;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;
import com.sun.jersey.api.core.InjectParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Created by arindamnath on 04/03/16.
 */
@Component
@Path("/mpokket/api/wallet/")
public class WalletServicesImpl extends BaseAPIServiceImpl implements WalletServices {

    @InjectParam
    UserDeviceDAO userDeviceDAO;

    @InjectParam
    UserWalletDAO userWalletDAO;

    @InjectParam
    BankAccountDAO bankAccountDAO;

    @InjectParam
    BankCodesDAO bankCodesDAO;

    @InjectParam
    UserTransactionDAO userTransactionDAO;

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    DESEncryption desEncryption;

    @GET
    @Path("/banks")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getBankCodes(@QueryParam("offset") Integer offset,
                                     @QueryParam("pageSize") Integer pageSize,
                                     @QueryParam("searchField") String searchField,
                                     @QueryParam("searchText") String searchText,
                                     @QueryParam("sortField") String sortField,
                                     @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                     @QueryParam("filters") String filters) throws AppException {
        Map<Object, Object> paginationInfo = null;
        if (searchText != null) {
            paginationInfo = PaginationMapper.mapPaginationInfo(offset, pageSize, searchField, searchText, sortField, sortInAscOrder);
        }
        List<BankCodes> bankCodeses = bankCodesDAO.getBankCodesByString(paginationInfo);
        if (bankCodeses.size() > 0) {
            List<BankCodesData> bankCodesDatas = new ArrayList<>();
            for (BankCodes bankCodes : bankCodeses) {
                BankCodesData bankCodesData = new BankCodesData();
                bankCodesData.setId(bankCodes.getId());
                bankCodesData.setIfscCode(bankCodes.getIfscCode());
                bankCodesData.setBankName(bankCodes.getBankName());
                bankCodesData.setBankBranch(bankCodes.getBranch());
                bankCodesData.setBankAddress(bankCodes.getAddress());
                bankCodesData.setBankCity(bankCodes.getCity());
                bankCodesData.setBankDistrict(bankCodes.getDistrict());
                bankCodesData.setBankState(bankCodes.getState());
                if (bankCodes.getLogo() != null) {
                    bankCodesData.setBankLogoUrl(bankCodes.getLogo());
                }
                bankCodesDatas.add(bankCodesData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, bankCodesDatas.size(), bankCodesDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getWalletInfo(@PathParam("userId") Long userId) throws AppException {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        if (userWallet != null) {
            WalletData walletData = new WalletData();
            walletData.setId(userWallet.getId());
            walletData.setUserId(userId);
            walletData.setWalletId(userWallet.getWalletId());
            walletData.setAmount(userWallet.getAmount());
            walletData.setType(userWallet.getProvider());
            return new BaseResponse(userWallet.getId(), BaseResponse.ResponseCode.SUCCESS, 1, walletData);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/create/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse createUserWallet(@PathParam("userId") Long userId, WalletData walletData) throws AppException {
        if (userWalletDAO.getWalletInfoByUserId(userId) == null) {
            UserWallet userWallet = new UserWallet();
            userWallet.setUserId(userId);
            userWallet.setAmount(0.0d);
            userWallet.setWalletId(keyGenerator.generateWalletCode());
            userWallet.setTransactionHash(desEncryption.encrypt(walletData.getHash(), null));
            userWallet.setProvider(walletData.getType());
            userWallet.setStatus(Status.ACTIVE);
            userWallet.setCreatedBy(userId);
            userWallet.setLastUpdatedBy(userId);
            userWalletDAO.create(userWallet);
            return new BaseResponse(userWallet.getId(), BaseResponse.ResponseCode.SUCCESS, 1, "User wallet created successfully!");
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "User wallet already exists!");
        }
    }

    @POST
    @Path("/user/{userId}/add/bank/account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse addBankAccount(@PathParam("userId") Long userId,
                                       WalletAccountData walletAccountData) throws AppException {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        if (userWallet != null) {
            String hash = desEncryption.encrypt(walletAccountData.getHash(), null);
            if (userWallet.getTransactionHash().equalsIgnoreCase(hash)) {
                String pass = walletAccountData.getHash();
                if (bankAccountDAO.getBankAccountNumber(userId, userWallet.getId(),
                        desEncryption.encrypt(walletAccountData.getBankAccount(), pass)) == null) {
                    List<BankAccount> bankAccounts = bankAccountDAO.getAllActiveBankAccountsByUser(userId);
                    Long lastId = bankAccountDAO.getLastEntryId();

                    BankAccount bankAccount = new BankAccount();
                    bankAccount.setUserId(userId);
                    bankAccount.setWalletId(walletAccountData.getWalletId());
                    bankAccount.setAccountId(desEncryption.encrypt(walletAccountData.getBankAccount(), pass));
                    bankAccount.setIfscId(walletAccountData.getBankRefId());
                    bankAccount.setTransferCode(keyGenerator.generateAccountCode(userId, lastId));
                    //Check if there are other accounts already present
                    if (bankAccounts.size() == 0) {
                        bankAccount.setIsPrimary(true);
                    } else {
                        bankAccount.setIsPrimary(walletAccountData.getIsPrimary());
                        //Set other accounts to false
                        if (walletAccountData.getIsPrimary()) {
                            for (BankAccount ba : bankAccounts) {
                                ba.setIsPrimary(false);
                            }
                            bankAccountDAO.bulkUpdate(bankAccounts);
                        }
                    }
                    bankAccount.setIsSendVerified(false);
                    bankAccount.setIsReceiveVerified(false);
                    bankAccount.setStatus(Status.ACTIVE);
                    bankAccount.setCreatedBy(userId);
                    bankAccount.setLastUpdatedBy(userId);
                    bankAccountDAO.create(bankAccount);
                    return new BaseResponse(bankAccount.getId(), BaseResponse.ResponseCode.SUCCESS, "Bank account information created!");
                } else {
                    return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Bank account information already linked.");
                }
            } else {
                return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Wrong transaction password.");
            }
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @PUT
    @Path("/user/{userId}/update/bank/account/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateBankAccount(@PathParam("userId") Long userId,
                                          @PathParam("accountId") Long accId,
                                          WalletAccountData walletAccountData) throws AppException {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        if (userWallet != null) {
            String pass = desEncryption.encrypt(walletAccountData.getHash(), null);
            if (userWallet.getTransactionHash().equalsIgnoreCase(pass)) {
                BankAccount bankAccount = bankAccountDAO.get(accId);
                if (bankAccount != null) {
                    //Set the other accounts to false
                    List<BankAccount> bankAccounts = bankAccountDAO.getAllActiveBankAccountsByUser(userId);
                    if (bankAccounts.size() > 0) {
                        if (walletAccountData.getIsPrimary()) {
                            for (BankAccount ba : bankAccounts) {
                                ba.setIsPrimary(false);
                            }
                            bankAccountDAO.bulkUpdate(bankAccounts);
                        }
                    }
                    //Update the info
                    bankAccount.setAccountId(desEncryption.encrypt(walletAccountData.getBankAccount(), pass));
                    bankAccount.setIfscId(walletAccountData.getBankRefId());
                    bankAccount.setIsPrimary(walletAccountData.getIsPrimary());
                    bankAccount.setIsReceiveVerified(false);
                    bankAccount.setIsSendVerified(false);
                    bankAccount.setLastUpdatedBy(userId);
                    bankAccountDAO.updateAndFlush(bankAccount);
                    return new BaseResponse(bankAccount.getId(), BaseResponse.ResponseCode.SUCCESS, "Bank account information updated!");
                } else {
                    return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
                }
            } else {
                return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Wrong transaction password.");
            }
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "User wallet doesn't exists!");
        }
    }

    @POST
    @Path("/user/{userId}/delete/bank/account/{accountId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse removeBankAccount(@PathParam("userId") Long userId,
                                          @PathParam("accountId") Long accId,
                                          WalletData walletData) throws AppException {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        if (userWallet != null) {
            BankAccount bankAccount = bankAccountDAO.get(accId);
            if (bankAccount != null) {
                //To delete an account the request must originate from same user, wallet and correct transaction password
                if (bankAccount.getUserId() == userId && bankAccount.getWalletId() == userWallet.getId()
                        && userWallet.getTransactionHash().equalsIgnoreCase(desEncryption.encrypt(walletData.getHash(), null))) {
                    bankAccount.setStatus(Status.DELETED);
                    bankAccount.setLastUpdatedBy(userId);
                    bankAccountDAO.updateAndFlush(bankAccount);

                    List<BankAccount> bankAccounts = bankAccountDAO.getAllActiveBankAccountsByUser(userId);
                    if (bankAccounts.size() > 0) {
                        if (bankAccount.getIsPrimary()) {
                            bankAccounts.get(0).setIsPrimary(true);
                            bankAccountDAO.updateAndFlush(bankAccounts.get(0));
                        }
                    }
                    return new BaseResponse(accId, BaseResponse.ResponseCode.SUCCESS, "Account information deleted!");
                } else {
                    return new BaseResponse(accId, BaseResponse.ResponseCode.FAILURE, "User doesn't have access to modify this account!");
                }
            } else {
                return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
            }
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "User wallet doesn't exists!");
        }
    }

    @GET
    @Path("/user/{userId}/bank/accounts/")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getBankAccounts(@PathParam("userId") Long userId,
                                        @QueryParam("offset") Integer offset,
                                        @QueryParam("pageSize") Integer pageSize,
                                        @QueryParam("searchField") String searchField,
                                        @QueryParam("searchText") String searchText,
                                        @QueryParam("sortField") String sortField,
                                        @QueryParam("sortInAscOrder") Boolean sortInAscOrder,
                                        @QueryParam("filters") String filters) throws AppException {
        UserWallet userWallet = userWalletDAO.getWalletInfoByUserId(userId);
        List<BankAccount> bankAccounts = bankAccountDAO.getAllActiveBankAccountsByUser(userId);
        if (bankAccounts.size() > 0) {
            List<WalletAccountData> walletAccountDatas = new ArrayList<>();
            //Get all bank details
            List<Long> ifscIdList = new ArrayList<>();
            for(BankAccount bankAccount : bankAccounts) {
                if(!ifscIdList.contains(bankAccount.getIfscId())) {
                    ifscIdList.add(bankAccount.getIfscId());
                }
            }
            Map<Long, BankCodes> bankCodesMap = bankCodesDAO.get(ifscIdList);
            for (BankAccount bankAccount : bankAccounts) {
                BankCodes bankCodes = bankCodesMap.get(bankAccount.getIfscId());
                BankCodesData bankCodesData = null;
                if (bankCodes != null) {
                    bankCodesData = new BankCodesData();
                    bankCodesData.setId(bankCodes.getId());
                    bankCodesData.setIfscCode(bankCodes.getIfscCode());
                    bankCodesData.setBankName(bankCodes.getBankName());
                    bankCodesData.setBankBranch(bankCodes.getBranch());
                    bankCodesData.setBankAddress(bankCodes.getAddress());
                    bankCodesData.setBankCity(bankCodes.getCity());
                    bankCodesData.setBankDistrict(bankCodes.getDistrict());
                    bankCodesData.setBankState(bankCodes.getState());
                    if (bankCodes.getLogo() != null) {
                        bankCodesData.setBankLogoUrl(bankCodes.getLogo());
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
                walletAccountDatas.add(walletAccountData);
            }
            return new BaseResponse(-1l, BaseResponse.ResponseCode.SUCCESS, walletAccountDatas.size(),
                    walletAccountDatas);
        } else {
            return new BaseResponse(-1l, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @GET
    @Path("/user/{userId}/transactions/status/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse getTransactions(@PathParam("userId") Long userId,
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
        List<UserTransaction> userTransactions = userTransactionDAO.getTransactionInfoByStatus(userId, status, paginationInfo);
        if (userTransactions.size() > 0) {
            List<WalletTransactionData> walletTransactionDatas = new ArrayList<>();
            for (UserTransaction userTransaction : userTransactions) {
                WalletTransactionData walletTransactionData = new WalletTransactionData();
                walletTransactionData.setId(userTransaction.getId());
                walletTransactionData.setTransactionId(userTransaction.getTransactionId());
                walletTransactionData.setUserId(userTransaction.getFromUserId());
                walletTransactionData.setWalletId(userTransaction.getFromWalletId());
                walletTransactionData.setAccountId(userTransaction.getFromAccountId());
                walletTransactionData.setToUserId(userTransaction.getToUserId());
                walletTransactionData.setToWalletId(userTransaction.getToWalletId());
                walletTransactionData.setToAccountId(userTransaction.getToAccountId());
                walletTransactionData.setAmount(userTransaction.getAmount());
                walletTransactionData.setType(userTransaction.getType());
                if (userTransaction.getLoanId() != null) {
                    walletTransactionData.setLoanId(userTransaction.getLoanId());
                }
                walletTransactionData.setExteralId(userTransaction.getExternalId());
                walletTransactionData.setStatus(userTransaction.getStatus());
                walletTransactionData.setCreatedOn(userTransaction.getCreatedDate());
                walletTransactionData.setUpdatedOn(userTransaction.getLastUpdatedDate());
                walletTransactionDatas.add(walletTransactionData);
            }
            return new BaseResponse(userId, BaseResponse.ResponseCode.SUCCESS,
                    walletTransactionDatas.size(), walletTransactionDatas);
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "No record(s) found.");
        }
    }

    @POST
    @Path("/user/{userId}/wallet/transaction/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse addAmountToWallet(@PathParam("userId") Long userId,
                                          WalletTransactionData walletTransactionData) throws AppException {
        UserWallet userWallet = userWalletDAO.get(walletTransactionData.getWalletId());
        if (userWallet != null) {
            UserDevice userDevice = userDeviceDAO.getDeviceById(userId, walletTransactionData.getDeviceData().getDeviceId());
            if (userDevice != null) {
                String hash = desEncryption.encrypt(walletTransactionData.getHash(), null);
                if (userWallet.getTransactionHash().equalsIgnoreCase(hash)) {
                    String transactionId = keyGenerator.generateTransactionCode(
                            walletTransactionData.getWalletId(), userTransactionDAO.getLastInsertedId());
                    if (userTransactionDAO.getTransactionByUserId(userId, userWallet.getId(), transactionId) == null) {
                        UserTransaction userTransaction = new UserTransaction();
                        userTransaction.setTransactionId(transactionId);
                        userTransaction.setFromUserId(walletTransactionData.getUserId());
                        userTransaction.setFromWalletId(walletTransactionData.getWalletId());
                        userTransaction.setFromAccountId(walletTransactionData.getAccountId());
                        userTransaction.setToUserId(-1l);
                        userTransaction.setToAccountId(-1l);
                        userTransaction.setToWalletId(-1l);
                        userTransaction.setAmount(walletTransactionData.getAmount());
                        userTransaction.setType(TransactionType.WALLET);
                        userTransaction.setNote(walletTransactionData.getNote());

                        userTransaction.setStatus(Status.PENDING);
                        userTransaction.setCreatedBy(userId);
                        userTransaction.setLastUpdatedBy(userId);
                        userTransactionDAO.create(userTransaction);
                        return new BaseResponse(userTransaction.getId(), BaseResponse.ResponseCode.SUCCESS, "Transaction recorded successfully.");
                    } else {
                        return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Transaction record exists!");
                    }
                } else {
                    return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "Wrong transaction password.");
                }
            } else {
                return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "This device has been flagged as stolen!");
            }
        } else {
            return new BaseResponse(userId, BaseResponse.ResponseCode.FAILURE, "User wallet doesn't exists!");
        }
    }
}
