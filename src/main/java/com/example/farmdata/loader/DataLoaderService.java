package com.example.farmdata.loader;

import com.example.farmdata.data.SensorType;
import com.example.farmdata.database.FarmRepository;
import com.example.farmdata.database.MeasurementRepository;
import com.example.farmdata.database.SensorTypeEntity;
import com.example.farmdata.database.SensorTypeRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class DataLoaderService {

    @Autowired
    private final FarmRepository farmRepository;
    @Autowired
    private final MeasurementRepository measurementRepository;
    @Autowired
    private final SensorTypeRepository sensorTypeRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveDataForOneFarm(List<FarmDataItem> data) {
        var sensorTypes = getSavedSensorTypes();
        var farmToSave = FarmDataMapper.mapToFarm(data.get(0));
        log.info("Starting to save measurement data from {}", farmToSave.getName());
        var farm = farmRepository.save(farmToSave);
        data.forEach(item -> {
            var sensorType = sensorTypes.get(item.getSensorType());
            var measurement = FarmDataMapper.mapToMeasurement(item);
            measurement.setFarm(farm);
            measurement.setSensorType(sensorType);
            measurementRepository.save(measurement);
        });
        log.info("All data saved for {}!", farmToSave.getName());
    }

    private Map<SensorType, SensorTypeEntity> getSavedSensorTypes() {
        Map<SensorType, SensorTypeEntity> saved = new HashMap<>();
        Arrays.stream(SensorType.values()).forEach(sensorType -> {
            var entity = findSensorType(sensorType.name());
            saved.put(sensorType, entity);
        });
        return saved;
    }

    private SensorTypeEntity findSensorType(String name) {
        var sensorType = sensorTypeRepository.findByNameIgnoreCase(name);
        if (sensorType.isPresent()) {
            return sensorType.get();
        }
        return sensorTypeRepository.save(new SensorTypeEntity(name));
    }
}
