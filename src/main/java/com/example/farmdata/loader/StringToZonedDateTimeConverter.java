package com.example.farmdata.loader;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class StringToZonedDateTimeConverter extends AbstractBeanField {
    @Override
    protected Object convert(String date) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        var formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        return ZonedDateTime.parse(date);
    }
}
