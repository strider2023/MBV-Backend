package com.mbv.persist.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by arindamnath on 24/02/16.
 */
@Entity
@Table(name="loan_interest")
public class LoanInterest extends BaseEntity<Long> implements Serializable {

    @Column(name="loan_amount")
    private Long loanAmount;

    @Column(name="loan_period")
    private Long loanPeriod;

    @Column(name="loan_interest")
    private Long loanInterest;

    @Column(name="default_rate")
    private Long defaultRate;

    @Column(name="default_threshold")
    private Long defaultThreshold;

    @Column(name="processing_fee")
    private Double processingFee;

    @Column(name="app_usage_fee")
    private Double appUsageFee;

    @Column(name="revenue")
    private Double revenue;

    @Column(name="service_tax")
    private Double serviceTax;

    @Column(name="payment_gateway_fee")
    private Double paymentGateway;

    @Column(name="agent_fee")
    private Double agentFee;

    @Column(name="category")
    private Long category;

    public Long getLoanPeriod() {
        return loanPeriod;
    }

    public void setLoanPeriod(Long loanPeriod) {
        this.loanPeriod = loanPeriod;
    }

    public Long getDefaultThreshold() {
        return defaultThreshold;
    }

    public void setDefaultThreshold(Long defaultThreshold) {
        this.defaultThreshold = defaultThreshold;
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

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Long getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Long loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Long getLoanInterest() {
        return loanInterest;
    }

    public void setLoanInterest(Long loanInterest) {
        this.loanInterest = loanInterest;
    }

    public Long getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(Long defaultRate) {
        this.defaultRate = defaultRate;
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
