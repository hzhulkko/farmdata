package com.example.farmdata;

import com.example.farmdata.loader.FarmDataLoader;
import com.example.farmdata.loader.FarmDataService;
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
	CommandLineRunner initializeDatabase(FarmDataService service) {
		return args -> {
			var files = Arrays.asList(
					"src/main/resources/data/friman_metsola.csv",
					"src/main/resources/data/Nooras_Farm.csv",
					"src/main/resources/data/ossi_farm.csv",
					"src/main/resources/data/PartialTech.csv");
			files.forEach(filePath -> {
				var data = FarmDataLoader.readFarmDataItems(filePath);
				service.saveAll(data);
			});

		};
	}

}
