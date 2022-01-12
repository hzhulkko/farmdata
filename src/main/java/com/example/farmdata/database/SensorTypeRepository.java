package com.example.farmdata.database;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SensorTypeRepository extends JpaRepository<SensorTypeEntity, Long> {

    Optional<SensorTypeEntity> findByName(String name);
}
