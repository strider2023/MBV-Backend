package com.mbv.api.data;

/**
 * Created by arindamnath on 05/03/16.
 */
public class LoanInterestData {

    private Long id;

    private Long period;

    private Long interest;

    private Long defaultRate;

    private Long defaultThreshold;

    private Double processingFee;

    private Double appUsageFee;

    private Double serviceCharge;

    private Double serviceTax;

    private Double paymentGateway;

    private Double agentFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public Long getInterest() {
        return interest;
    }

    public void setInterest(Long interest) {
        this.interest = interest;
    }

    public Long getDefaultRate() {
        return defaultRate;
    }

    public void setDefaultRate(Long defaultRate) {
        this.defaultRate = defaultRate;
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

    public Double getAppUsageFee() {
        return appUsageFee;
    }

    public void setAppUsageFee(Double appUsageFee) {
        this.appUsageFee = appUsageFee;
    }

    public Double getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
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
}
