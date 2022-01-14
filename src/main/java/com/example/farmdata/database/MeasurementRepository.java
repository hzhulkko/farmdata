package com.example.farmdata.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.ZonedDateTime;
import java.util.List;

public interface MeasurementRepository extends JpaRepository<MeasurementEntity, Long> {

    List<MeasurementEntity> findAllByFarmAndSensorTypeAndTimestampBetween(FarmEntity farm, SensorTypeEntity sensorType,
                                                                          ZonedDateTime start, ZonedDateTime end);

    List<MeasurementEntity> findAllByFarmAndSensorTypeAndTimestampBefore(FarmEntity farm, SensorTypeEntity sensorType,
                                                                        ZonedDateTime end);

    List<MeasurementEntity> findAllByFarmAndSensorType(FarmEntity farm, SensorTypeEntity sensorType);
}
