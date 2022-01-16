package com.example.farmdata;

import com.example.farmdata.loader.DataLoaderService;
import com.example.farmdata.loader.FarmDataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class FarmDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(FarmDataApplication.class, args);
    }

    @Bean
    @Profile("!test")
    CommandLineRunner initializeDatabase(DataLoaderService service) {
        return args -> {
            var files = Arrays.asList(
                    "data/friman_metsola.csv",
                    "data/Nooras_Farm.csv",
                    "data/ossi_farm.csv",
                    "data/PartialTech.csv");
            files.forEach(filePath -> {
                var data = FarmDataLoader.readFarmDataItems(filePath);
                service.saveAll(data);
            });

        };
    }


}
