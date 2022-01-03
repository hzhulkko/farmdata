package com.example.farmdata;

import com.example.farmdata.loader.FarmDataLoader;
import com.example.farmdata.repository.FarmDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class FarmDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmDataApplication.class, args);
	}

	@Bean
	CommandLineRunner initializeDatabase(FarmDataRepository repository) {
		return args -> {
			var files = Arrays.asList(
					"src/main/resources/data/friman_metsola.csv",
					"src/main/resources/data/Nooras_Farm.csv",
					"src/main/resources/data/ossi_farm.csv",
					"src/main/resources/data/PartialTech.csv");
			files.forEach(filePath -> {
				var data = FarmDataLoader.readFarmDataItems(filePath);
				data.forEach(item -> repository.save(item));
			});

		};
	}

}
