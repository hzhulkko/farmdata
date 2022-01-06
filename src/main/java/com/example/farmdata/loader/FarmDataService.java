package com.example.farmdata.loader;

import com.example.farmdata.database.FarmRepository;
import com.example.farmdata.database.MeasurementRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FarmDataService {

    @Autowired
    private final FarmRepository farmRepository;
    @Autowired
    private final MeasurementRepository measurementRepository;

    public void saveAll(List<FarmDataItem> data) {
        var farmToSave = FarmDataMapper.mapToFarm(data.get(0));
        log.info("Starting to save measurement data from {}", farmToSave.getName());
        var farm = farmRepository.save(farmToSave);
        data.forEach(item -> {
            var measurement = FarmDataMapper.mapToMeasurement(item);
            measurement.setFarm(farm);
            measurementRepository.save(measurement);
        });
        log.info("All data saved!");
    }
}
