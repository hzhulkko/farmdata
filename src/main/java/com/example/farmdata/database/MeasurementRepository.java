package com.example.farmdata.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<MeasurementEntity, Long> {

    List<MeasurementEntity> findAllByFarm(FarmEntity farm);

    List<MeasurementEntity> findAllByFarmAndSensorType(FarmEntity farm, SensorTypeEntity sensorType);
}
