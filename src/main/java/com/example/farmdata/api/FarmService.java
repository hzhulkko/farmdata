package com.example.farmdata.api;

import com.example.farmdata.database.FarmEntity;
import com.example.farmdata.database.FarmRepository;
import com.example.farmdata.database.MeasurementEntity;
import com.example.farmdata.database.MeasurementRepository;
import com.example.farmdata.database.SensorTypeEntity;
import com.example.farmdata.database.SensorTypeRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FarmService {

    @Autowired
    private final FarmRepository farmRepository;
    @Autowired
    private final MeasurementRepository measurementRepository;
    @Autowired
    private final SensorTypeRepository sensorTypeRepository;

    @Transactional
    public List<FarmResponse> findAllFarms() {
        return farmRepository.findAll().stream()
                .map(this::mapToFarmResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public FarmResponse findFarmById(Long id) {
        var farmEntity = getFarm(id);
        return mapToFarmResponse(farmEntity);
    }

    @Transactional
    public List<MeasurementResponse> findMeasurementsByFarmAndSensorType(Long id, String sensorName) {
        var farmEntity = getFarm(id);
        var sensorType = getSensorType(sensorName);
        return mapToMeasurementResponse(measurementRepository.findAllByFarmAndSensorType(farmEntity, sensorType));
    }

    @Transactional
    public List<MeasurementResponse> findMeasurementsByDataRange(Long id, String sensorName,
                                                                                    String start, String end) {
        var farmEntity = getFarm(id);
        var sensorType = getSensorType(sensorName);
        var endDate = end.isEmpty() ?
                ZonedDateTime.now(ZoneOffset.UTC).plusDays(1) : getDateObject(end).plusDays(1);
        if (start.isEmpty()) {
            return mapToMeasurementResponse(
                    measurementRepository.findAllByFarmAndSensorTypeAndTimestampBefore(farmEntity, sensorType,
                            endDate)
            );
        }
        var startDate = getDateObject(start);
        return mapToMeasurementResponse(
                measurementRepository.findAllByFarmAndSensorTypeAndTimestampBetween(farmEntity, sensorType,
                        startDate, endDate)
        );
    }

    private FarmEntity getFarm(Long id) {
        return farmRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Farm not found"));
    }

    private SensorTypeEntity getSensorType(String sensorName) {
        return sensorTypeRepository.findByName(sensorName)
                .orElseThrow(() -> new RuntimeException("SensorType not found"));
    }

    private FarmResponse mapToFarmResponse(FarmEntity entity) {
        return new FarmResponse(entity.getId(), entity.getName());
    }

    private List<MeasurementResponse> mapToMeasurementResponse(List<MeasurementEntity> data) {
        return data.stream()
                .map(entity -> {
                    var measurement = new MeasurementResponse();
                    measurement.setSensorType(entity.getSensorType().getName());
                    measurement.setValue(entity.getValue());
                    measurement.setTimestamp(entity.getTimestamp().withZoneSameInstant(ZoneOffset.UTC));
                    return measurement;
                })
                .collect(Collectors.toList());
    }

    private ZonedDateTime getDateObject(String dateString) {
        var localDate = LocalDate.parse(dateString, DateTimeFormatter.ISO_DATE);
        return localDate.atStartOfDay(ZoneOffset.UTC);
    }

}
