package com.example.farmdata.api;

import com.example.farmdata.database.FarmEntity;
import com.example.farmdata.database.MeasurementEntity;
import com.example.farmdata.database.FarmRepository;
import com.example.farmdata.database.MeasurementRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FarmControllerService {

    @Autowired
    private final FarmRepository farmRepository;
    @Autowired
    private final MeasurementRepository measurementRepository;

    public List<FarmResponse> findAllFarms() {
        return farmRepository.findAll().stream()
                .map(this::mapToFarmResponse)
                .collect(Collectors.toList());
    }

    public FarmResponse findFarmById(Long id) {
        var farmEntity = farmRepository.findById(id);
        return farmEntity.map(this::mapToFarmResponse).orElseGet(FarmResponse::new);
    }

    public List<MeasurementEntity> findMeasurementsByFarm(FarmEntity farmEntity) {
        return measurementRepository.findAllByFarm(farmEntity);
    }

    private FarmResponse mapToFarmResponse(FarmEntity entity) {
        var measurements = findMeasurementsByFarm(entity)
                .stream()
                .map(entry -> new MeasurementResponse(entry.getSensorType(), entry.getValue(), entry.getTimestamp()))
                .collect(Collectors.toList());
        return new FarmResponse(entity.getName(), measurements);
    }
}
