package com.appolica.assessment.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class LocalDateParser implements Parser<LocalDate> {
    @Override
    public LocalDate parse(String line) {
       return getStringDateFromArgs(line);
    }

    private LocalDate getStringDateFromArgs(String arg) throws DateTimeParseException {
        String[] yearMonthDate = arg.split("-");
        int year = Integer.parseInt(yearMonthDate[0]);
        int month = Integer.parseInt(yearMonthDate[1]);
        int date = Integer.parseInt(yearMonthDate[2]);
        return LocalDate.of(year, month, date);
    }

}
