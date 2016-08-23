package com.mbv.sched.jobs;

import com.mbv.framework.data.LoanUserData;
import com.mbv.framework.data.PDFLoanData;
import com.mbv.framework.response.EmailResponses;
import com.mbv.framework.spring.ServiceContext;
import com.mbv.framework.util.PDFUtil;
import com.mbv.framework.util.SMTP;
import com.mbv.framework.util.UploadFile;
import com.mbv.persist.dao.LoanBorrowerInfoDAO;
import com.mbv.persist.dao.LoanInstallmentDAO;
import com.mbv.persist.dao.LoanInterestDAO;
import com.mbv.persist.dao.LoanLenderInfoDAO;
import com.mbv.persist.dao.LoanReceiptDAO;
import com.mbv.persist.dao.UserDAO;
import com.mbv.persist.dao.UserLocationDAO;
import com.mbv.persist.dao.UserTransactionDAO;
import com.mbv.persist.dao.UserWalletDAO;
import com.mbv.persist.entity.LoanBorrowerInfo;
import com.mbv.persist.entity.LoanInstallment;
import com.mbv.persist.entity.LoanInterest;
import com.mbv.persist.entity.LoanLenderInfo;
import com.mbv.persist.entity.LoanReceipt;
import com.mbv.persist.entity.User;
import com.mbv.persist.entity.UserLocation;
import com.mbv.persist.entity.UserTransaction;
import com.mbv.persist.entity.UserWallet;
import com.mbv.persist.enums.Status;
import com.mbv.persist.enums.TransactionType;
import com.mbv.sched.util.StringUtil;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by arindamnath on 10/03/16.
 */
public class WalletTransactionJob extends QuartzJobBean {

    private static Logger logger = LoggerFactory
            .getLogger(WalletTransactionJob.class.getCanonicalName());

    private LoanBorrowerInfoDAO loanBorrowerInfoDAO = ServiceContext.getApplicationContext().getBean(LoanBorrowerInfoDAO.class);

    private LoanLenderInfoDAO loanLenderInfoDAO = ServiceContext.getApplicationContext().getBean(LoanLenderInfoDAO.class);

    private LoanInstallmentDAO loanInstallmentDAO = ServiceContext.getApplicationContext().getBean(LoanInstallmentDAO.class);

    private LoanInterestDAO loanInterestDAO = ServiceContext.getApplicationContext().getBean(LoanInterestDAO.class);

    private UserTransactionDAO userTransactionDAO = ServiceContext.getApplicationContext().getBean(UserTransactionDAO.class);

    private UserWalletDAO userWalletDAO = ServiceContext.getApplicationContext().getBean(UserWalletDAO.class);

    private LoanReceiptDAO loanReceiptDAO = ServiceContext.getApplicationContext().getBean(LoanReceiptDAO.class);

    private UserLocationDAO userLocationDAO = ServiceContext.getApplicationContext().getBean(UserLocationDAO.class);

    private UserDAO userDAO = ServiceContext.getApplicationContext().getBean(UserDAO.class);

    private PDFUtil pdfUtil = ServiceContext.getApplicationContext().getBean(PDFUtil.class);

    private SMTP smtp = ServiceContext.getApplicationContext().getBean(SMTP.class);

    private UploadFile uploadFile = ServiceContext.getApplicationContext().getBean(UploadFile.class);

    private EmailResponses emailResponses = ServiceContext.getApplicationContext().getBean(EmailResponses.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        incomingTransfers();
        outgoingTransfers();
    }

    private void incomingTransfers() {
        //TODO Tweak logic
        List<UserTransaction> userTransactionList = userTransactionDAO.getTransactionsByTypeAndStatus(Status.PENDING,
                TransactionType.LOAN_RECEIVE, null);
        if(userTransactionList.size() > 0) {
            logger.info(String.valueOf(userTransactionList.size()) + " number of transactions to process.");
            for(UserTransaction userTransaction : userTransactionList) {
                //Fetch Loan info
                //TODO make a prefetch and read data from map
                LoanBorrowerInfo loanBorrowerInfo = loanBorrowerInfoDAO.get(userTransaction.getLoanId());
                LoanLenderInfo loanLenderInfo = loanLenderInfoDAO.getLenderDetailsByLoanId(userTransaction.getLoanId());
                LoanInterest loanInterest = loanInterestDAO.get(loanBorrowerInfo.getInterestId());
                User lender = userDAO.get(userTransaction.getFromUserId());
                User borrower = userDAO.get(userTransaction.getToUserId());
                List<UserLocation> borrowerLocations = userLocationDAO.getUserLocationInfoById(borrower.getId(), null);
                List<UserLocation> lenderLocations = userLocationDAO.getUserLocationInfoById(lender.getId(), null);
                //Add money to wallet by cross verifying id
                UserWallet receiverWallet = userWalletDAO.get(userTransaction.getToWalletId());
                receiverWallet.setAmount(receiverWallet.getAmount() + userTransaction.getAmount());
                userWalletDAO.updateAndFlush(receiverWallet);
                //Get the date of repayment
                //TODO Tweak logic to omit alternate saturdays
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, loanInterest.getLoanPeriod().intValue());
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 2);
                } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                } else {
                    calendar.add(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 1);
                }
                //Insert interest data
                LoanInstallment loanInstallment = new LoanInstallment();
                loanInstallment.setLoanId(userTransaction.getLoanId());
                loanInstallment.setBorrowerId(loanBorrowerInfo.getBorrowerId());
                loanInstallment.setLenderId(loanLenderInfo.getLenderId());
                loanInstallment.setInstallment(loanBorrowerInfo.getPeriod());
                loanInstallment.setProjectedAmount(loanBorrowerInfo.getAmount());
                loanInstallment.setProjectedDate(calendar.getTime());
                loanInstallment.setProcessingFee(loanInterest.getProcessingFee());
                loanInstallment.setAppUsageFee(loanInterest.getAppUsageFee());
                loanInstallment.setRevenue(loanInterest.getRevenue());
                loanInstallment.setServiceTax(loanInterest.getServiceTax());
                loanInstallment.setPaymentGateway(loanInterest.getPaymentGateway());
                loanInstallment.setAgentFee(loanInterest.getAgentFee());
                loanInstallment.setStatus(Status.PENDING);
                loanInstallment.setCreatedBy(userTransaction.getToUserId());
                loanInstallment.setLastUpdatedBy(userTransaction.getToUserId());
                loanInstallmentDAO.create(loanInstallment);
                //Generate upgrade pdf
                PDFLoanData pdfLoanData = new PDFLoanData();
                pdfLoanData.setBorrowerData(new LoanUserData(StringUtil.getName(borrower),
                        borrowerLocations.get(0).getAddress(),
                        borrowerLocations.get(0).getCity(),
                        borrowerLocations.get(0).getState(),
                        String.valueOf(borrowerLocations.get(0).getPincode())));
                pdfLoanData.setLenderData(new LoanUserData(StringUtil.getName(lender),
                        lenderLocations.get(0).getAddress(),
                        lenderLocations.get(0).getCity(),
                        lenderLocations.get(0).getState(),
                        String.valueOf(lenderLocations.get(0).getPincode())));
                pdfLoanData.setAmount(loanBorrowerInfo.getAmount());
                pdfLoanData.setDisbursementAmount(500l);
                pdfLoanData.setPeriod(loanBorrowerInfo.getPeriod());
                pdfLoanData.setReferenceId(loanBorrowerInfo.getLoanId());
                pdfLoanData.setRequestedOn(new Date());
                pdfLoanData.setRepaymentOn(calendar.getTime());
                pdfLoanData.setTax(loanInterest.getServiceTax());
                pdfLoanData.setUsageFee(loanInterest.getAppUsageFee());
                pdfLoanData.setInterest(loanInterest.getLoanInterest());
                //Send the mails
                getBorrowApprovalEmailBody(borrower, lender, loanBorrowerInfo, pdfLoanData);
                getLendApprovalEmailBody(borrower, lender, loanBorrowerInfo, pdfLoanData);
                //Update the transaction
                userTransaction.setStatus(Status.COMPLETED);
                userTransactionDAO.updateAndFlush(userTransaction);
                logger.info("Transaction with id [" + String.valueOf(userTransaction.getId()) + "] processed");
            }
        } else {
            logger.info("0 number of transactions to process.");
        }
    }

    private void outgoingTransfers() {
        //TODO Tweak logic
        List<UserTransaction> userTransactionList = userTransactionDAO.getTransactionsByTypeAndStatus(Status.PENDING,
                TransactionType.LOAN_SEND, null);
        if(userTransactionList.size() > 0) {
            logger.info(String.valueOf(userTransactionList.size()) + " number of transactions to process.");
            for(UserTransaction userTransaction : userTransactionList) {
                //TODO Add data to csv and mail
                userTransaction.setStatus(Status.COMPLETED);
                userTransactionDAO.updateAndFlush(userTransaction);
                logger.info("Transaction with id [" + String.valueOf(userTransaction.getId()) + "] processed");
            }
        } else {
            logger.info("0 number of transactions to process.");
        }
    }

    private void getBorrowApprovalEmailBody(User borrower, User lender,
                                            LoanBorrowerInfo loanBorrowerInfo, PDFLoanData pdfLoanData) {
        pdfUtil.setPDFLoanData(pdfLoanData);
        byte[] pdfBytes = pdfUtil.createBorrowerPDF(false, false);
        String name = "mPokket_Borrower_Receipt_" + System.currentTimeMillis() + ".pdf";
        uploadFile.uploadAttachment(new ByteArrayInputStream(pdfBytes), name);

        LoanReceipt loanReceipt = new LoanReceipt();
        loanReceipt.setReceiptUrl("https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/receipts/" + name);
        loanReceipt.setLoanId(loanBorrowerInfo.getId());
        loanReceipt.setStatus(Status.ACTIVE);
        loanReceipt.setCreatedBy(lender.getId());
        loanReceipt.setLastUpdatedBy(lender.getId());
        loanReceiptDAO.create(loanReceipt);

        smtp.sendMail(borrower.getEmail(),
                "mPokket money request approved - " + loanBorrowerInfo.getLoanId(),
                emailResponses.getLoanApprovalEmailBody(StringUtil.getName(borrower),
                        StringUtil.getName(lender),
                        loanBorrowerInfo.getAmount(),
                        loanBorrowerInfo.getPeriod(),
                        loanBorrowerInfo.getLoanId()),
                pdfBytes,
                name,
                smtp.getEmailProps().getReplyFrom());
    }

    private void getLendApprovalEmailBody(User borrower, User lender,
                                          LoanBorrowerInfo loanBorrowerInfo, PDFLoanData pdfLoanData) {
        pdfUtil.setPDFLoanData(pdfLoanData);
        byte[] pdfBytes = pdfUtil.createBorrowerPDF(false, true);
        String name = "mPokket_Lender_Receipt_" + System.currentTimeMillis() + ".pdf";
        uploadFile.uploadAttachment(new ByteArrayInputStream(pdfBytes), name);

        LoanReceipt loanReceipt = new LoanReceipt();
        loanReceipt.setReceiptUrl("https://s3-ap-southeast-1.amazonaws.com/mbv-pokket/receipts/" + name);
        loanReceipt.setLoanId(loanBorrowerInfo.getId());
        loanReceipt.setStatus(Status.ACTIVE);
        loanReceipt.setCreatedBy(lender.getId());
        loanReceipt.setLastUpdatedBy(lender.getId());
        loanReceiptDAO.create(loanReceipt);

        smtp.sendMail(lender.getEmail(),
                "Lend request approved form mPokket - " + loanBorrowerInfo.getLoanId(),
                emailResponses.getLoanGivenEmailBody(StringUtil.getName(borrower),
                        StringUtil.getName(lender),
                        loanBorrowerInfo.getAmount(),
                        loanBorrowerInfo.getPeriod(),
                        loanBorrowerInfo.getLoanId()),
                pdfBytes,
                name,
                smtp.getEmailProps().getReplyFrom());
    }
}
