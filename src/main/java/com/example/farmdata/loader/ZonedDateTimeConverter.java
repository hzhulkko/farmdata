package com.example.farmdata.loader;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ZonedDateTimeConverter extends AbstractBeanField {
    @Override
    protected Object convert(String date) throws CsvDataTypeMismatchException {
        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        try {
            return ZonedDateTime.parse(date, formatter);
        } catch (DateTimeParseException ex) {
            throw new CsvDataTypeMismatchException("Cannot parse to ISO offset datetime: " + date);
        }

    }
}
