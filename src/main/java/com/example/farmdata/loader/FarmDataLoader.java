package com.example.farmdata.loader;

import com.example.farmdata.data.FarmDataItem;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.MappingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class FarmDataLoader {

    public static List<FarmDataItem> readFarmDataItems(String filePath) {
        try {
            return new CsvToBeanBuilder<FarmDataItem>(new FileReader(filePath))
                    .withType(FarmDataItem.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            log.error("File not found: {}", filePath);
        }
        return Collections.emptyList();
    }
}
