package com.appolica.assessment.utils;

import com.appolica.assessment.models.ConversionContainer;

import static com.appolica.assessment.config.CustomURL.*;

public class UrlBuilder {


    /**
     * <pre>
     * Endpoint returns JSON with the following structure:
     *
     * {
     *   "base":"USD",          //currency ISO 4217 three-letter code format
     *   "amount":9.99,         //decimal requested to convert from base to target currency
     *   "ms":2,                //response time in milliseconds
     *   "result":{
     *      ("EUR"):9.24,       //converted amount
     *      "rate":0.92536}     //base to target currency exchange rate
     * }
     *
     * () - variable name based on query
     * </pre>
     */
    public static String buildTodayConversion(ConversionContainer conversionContainer) {
        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(DOMAIN)
                .append(CONVERT)
                .append(QUERY_STRINGS)
                .append("from=")
                .append(conversionContainer.getBaseCurrency())
                .append(AND)
                .append("to=")
                .append(conversionContainer.getTargetCurrency())
                .append(AND)
                .append("amount=")
                .append(conversionContainer.getAmount())
                .append(AND)
                .append("api_key=")
                .append(API_KEY);
        return urlBuilder.toString();
    }

    /**
     * <pre>
     * Endpoint returns JSON with the following structure:
     *
     * {
     *   "date":"2024-06-01",    //conversion date
     *   "base":USD,             //currency ISO 4217 three-letter code format
     *   "ms":2,                 //response time in milliseconds
     *   "results":{
     *      ("EUR"):0.92173,     // base to target currency exchange rate
     * }
     *
     * () - variable name based on query
     * </pre>
     */

    public static String buildHistoricalDayConversion(ConversionContainer conversionContainer) {
        StringBuilder urlBuilder = new StringBuilder();

        urlBuilder.append(DOMAIN)
                .append(HISTORICAL)
                .append(QUERY_STRINGS)
                .append("date=")
                .append(conversionContainer.getDate())
                .append(AND)
                .append("from=")
                .append(conversionContainer.getBaseCurrency())
                .append(AND)
                .append("to=")
                .append(conversionContainer.getTargetCurrency())
                .append(AND)
                .append("amount=")
                .append(conversionContainer.getAmount())
                .append(AND)
                .append("api_key=")
                .append(API_KEY);
        return urlBuilder.toString();
    }


}
