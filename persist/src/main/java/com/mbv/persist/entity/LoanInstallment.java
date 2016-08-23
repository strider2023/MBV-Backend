package com.mbv.persist.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="loan_installment")
public class LoanInstallment extends BaseEntity<Long> implements Serializable {

    @Column(name="loan_id")
    private Long loanId;

    @Column(name="borrower_id")
    private Long borrowerId;

    @Column(name="lender_id")
    private Long lenderId;

    @Column(name="projected_amount")
    private Long projectedAmount;

    @Column(name="actual_amount")
    private Long actualAmount;

    @Column(name="installment")
    private Long installment;

    @Column(name="projected_date")
    private Date projectedDate;

    @Column(name="actual_date")
    private Date actualDate;

    @Column(name="processing_fee")
    private Double processingFee;

    @Column(name="app_usage_fee")
    private Double appUsageFee;

    @Column(name="revenue")
    private Double revenue;

    @Column(name="service_tax")
    private Double serviceTax;

    @Column(name="payment_gateway")
    private Double paymentGateway;

    @Column(name="agent_fee")
    private Double agentFee;

    @Column(name="transaction_id")
    private Long transactionId;

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getBorrowerId() {
        return borrowerId;
    }

    public void setBorrowerId(Long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public Long getLenderId() {
        return lenderId;
    }

    public void setLenderId(Long lenderId) {
        this.lenderId = lenderId;
    }

    public Long getInstallment() {
        return installment;
    }

    public void setInstallment(Long installment) {
        this.installment = installment;
    }

    public Date getProjectedDate() {
        return projectedDate;
    }

    public void setProjectedDate(Date projectedDate) {
        this.projectedDate = projectedDate;
    }

    public Date getActualDate() {
        return actualDate;
    }

    public void setActualDate(Date actualDate) {
        this.actualDate = actualDate;
    }

    public Double getProcessingFee() {
        return processingFee;
    }

    public void setProcessingFee(Double processingFee) {
        this.processingFee = processingFee;
    }

    public Double getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(Double serviceTax) {
        this.serviceTax = serviceTax;
    }

    public Double getPaymentGateway() {
        return paymentGateway;
    }

    public void setPaymentGateway(Double paymentGateway) {
        this.paymentGateway = paymentGateway;
    }

    public Double getAgentFee() {
        return agentFee;
    }

    public void setAgentFee(Double agentFee) {
        this.agentFee = agentFee;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getProjectedAmount() {
        return projectedAmount;
    }

    public void setProjectedAmount(Long projectedAmount) {
        this.projectedAmount = projectedAmount;
    }

    public Long getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(Long actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Double getAppUsageFee() {
        return appUsageFee;
    }

    public void setAppUsageFee(Double appUsageFee) {
        this.appUsageFee = appUsageFee;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
