package com.appolica.assessment.models.dto;

import com.appolica.assessment.models.Container;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class HistoricalConversionContainerDTO implements Container {
    @JsonProperty("date")
    private LocalDate date;
    @JsonProperty("base")
    private String baseCurrency;
    @JsonProperty("results")
    private ResultsDTO resultsDTO;
    @JsonProperty("ms")
    private int milliseconds;

    public HistoricalConversionContainerDTO() {
    }

    public LocalDate getDate() {
        return date;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public ResultsDTO getResults() {
        return resultsDTO;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    @Override
    public String toString() {
        return "HistoricalConversionContainer{" +
                "date=" + date +
                ", baseCurrency='" + baseCurrency + '\'' +
                ", results=" + resultsDTO +
                ", milliseconds=" + milliseconds +
                '}';
    }
}
