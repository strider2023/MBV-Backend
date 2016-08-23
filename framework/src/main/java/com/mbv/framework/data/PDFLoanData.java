package com.mbv.framework.data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by arindamnath on 09/03/16.
 */
public class PDFLoanData {

    private LoanUserData borrowerData;

    private LoanUserData lenderData;

    private Long amount;

    private Long disbursementAmount;

    private Long period;

    private String referenceId;

    private Date requestedOn;

    private Date repaymentOn;

    private Double tax;

    private Double usageFee;

    private Long interest;

    private SimpleDateFormat dateFormat;

    public PDFLoanData() {
        this.dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        this.borrowerData = new LoanUserData("Arindam Nath", "House 19, Chatterjee Bagan", "Chinsurah", "West Bengal", "712102");
        this.lenderData = new LoanUserData("Maitri Nath", "House 19, Meow Bagan", "Chinsurah", "West Bengal", "712102");
        this.amount = 550l;
        this.disbursementAmount = 500l;
        this.period = 1l;
        this.referenceId = "MPL-1457435892707-KYP8";
        this.requestedOn = new Date();
        this.repaymentOn = new Date();
        this.tax = 6.50d;
        this.usageFee = 43.50d;
        this.interest = 25l;
    }

    public PDFLoanData(LoanUserData borrowerData, LoanUserData lenderData, Long amount,
                       Long disbursementAmount, Long period, String referenceId, Date requestedOn,
                       Date repaymentOn, Double tax, Double usageFee) {
        this.dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        this.borrowerData = borrowerData;
        this.lenderData = lenderData;
        this.amount = amount;
        this.disbursementAmount = disbursementAmount;
        this.period = period;
        this.referenceId = referenceId;
        this.requestedOn = requestedOn;
        this.repaymentOn = repaymentOn;
        this.tax = tax;
        this.usageFee = usageFee;
    }

    public LoanUserData getBorrowerData() {
        return borrowerData;
    }

    public void setBorrowerData(LoanUserData borrowerData) {
        this.borrowerData = borrowerData;
    }

    public LoanUserData getLenderData() {
        return lenderData;
    }

    public void setLenderData(LoanUserData lenderData) {
        this.lenderData = lenderData;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getDisbursementAmount() {
        return disbursementAmount;
    }

    public void setDisbursementAmount(Long disbursementAmount) {
        this.disbursementAmount = disbursementAmount;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getRequestedOn() {
        return dateFormat.format(requestedOn);
    }

    public void setRequestedOn(Date requestedOn) {
        this.requestedOn = requestedOn;
    }

    public String getRepaymentOn() {
        return dateFormat.format(repaymentOn);
    }

    public void setRepaymentOn(Date repaymentOn) {
        this.repaymentOn = repaymentOn;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getUsageFee() {
        return usageFee;
    }

    public void setUsageFee(Double usageFee) {
        this.usageFee = usageFee;
    }

    public Long getInterest() {
        return interest;
    }

    public void setInterest(Long interest) {
        this.interest = interest;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(SimpleDateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }
}
