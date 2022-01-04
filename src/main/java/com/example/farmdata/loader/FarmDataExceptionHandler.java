package com.example.farmdata.loader;

import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FarmDataExceptionHandler implements CsvExceptionHandler {
    @Override
    public CsvException handleException(CsvException e) {
        log.warn("Ignoring error: {}", e.getMessage());
        return null;
    }
}
