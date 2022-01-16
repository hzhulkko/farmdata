package com.example.farmdata.loader;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FarmDataLoader {

    public static List<FarmDataItem> readFarmDataItems(String filePath) {
        try (var reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(filePath).getInputStream())
        )){
            return new CsvToBeanBuilder<FarmDataItem>(reader)
                    .withType(FarmDataItem.class)
                    .withVerifier(new FarmDataItemVerifier())
                    .withExceptionHandler(new FarmDataExceptionHandler())
                    .build().parse();
        } catch (IOException e) {
            log.error("Could not read file: {}", filePath);
        }
        return Collections.emptyList();
    }
}
