package com.oraclequantapi.oraclequantapi.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ConversionResponse {

    private String fromCurrency;
    private String toCurrency;

    private BigDecimal amount;
    private BigDecimal convertedAmount;
    private BigDecimal exchangeRate;


    private LocalDateTime timestamp;

    public ConversionResponse(){

    }
    public ConversionResponse(String fromCurrency,
                              String toCurrency,
                              BigDecimal amount,
                              BigDecimal convertedAmount,
                              BigDecimal exchangeRate,
                              LocalDateTime timestamp){

        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.convertedAmount = convertedAmount;
        this.exchangeRate = exchangeRate;
        this.timestamp = timestamp;
    }

    public String getFromCurrency(){
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency){
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency(){
        return toCurrency;

    }

    public void setToCurrency(String toCurrency){
        this.toCurrency = toCurrency;
    }

     public BigDecimal getAmount() {
        return amount;

     }

     public void setAmount(BigDecimal amount){
        this.amount = amount;
     }
    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public void setConvertedAmount(BigDecimal convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
