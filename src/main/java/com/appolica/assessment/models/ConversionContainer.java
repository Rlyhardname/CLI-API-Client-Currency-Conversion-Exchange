package com.appolica.assessment.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class ConversionContainer implements Container {
    @JsonProperty("date")
    private LocalDate localDate;
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("base_currency")
    private String baseCurrency;
    @JsonProperty("target_currency")
    private String targetCurrency;
    @JsonProperty("converted_amount")
    private double convertedAmount;

    public ConversionContainer() {
    }

    public ConversionContainer(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(String targetCurrency) {
        this.targetCurrency = targetCurrency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = bankersRoundingToSecondFractional(amount);
    }

    public double getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(double convertedAmount) {
        this.convertedAmount = bankersRoundingToSecondFractional(convertedAmount);
    }

    private double bankersRoundingToSecondFractional(double amount) {
        BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    @Override
    public String toString() {
        return "ConversionContainer{" +
                "localDate=" + localDate +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", targetCurrency='" + targetCurrency + '\'' +
                ", amount=" + amount +
                ", convertedAmount=" + convertedAmount +
                '}';
    }
}
