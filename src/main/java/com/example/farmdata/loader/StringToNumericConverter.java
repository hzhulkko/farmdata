package com.example.farmdata.loader;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringToNumericConverter extends AbstractBeanField {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException, CsvConstraintViolationException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            log.error("Cannot convert to numeric value: {}", value);
        }
        return null;
    }
}
