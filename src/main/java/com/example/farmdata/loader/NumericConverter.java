package com.example.farmdata.loader;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumericConverter extends AbstractBeanField {
    @Override
    protected Object convert(String value) throws CsvDataTypeMismatchException {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            throw new CsvDataTypeMismatchException("Cannot convert to numeric value: " + value);
        }
    }
}
