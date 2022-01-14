package com.example.farmdata.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/farm")
public class FarmController {

    @Autowired
    private final FarmService farmService;

    @GetMapping("/")
    public List<FarmResponse> listAllFarms() {
        return farmService.findAllFarms();
    }

    @GetMapping("/{id}")
    public FarmResponse showFarmDetails(
            @PathVariable String id) {
        return farmService.findFarmById(Long.parseLong(id));
    }

    public List<MeasurementResponse> listAllMeasurements(Long id, String metric) {
        return farmService.findMeasurementsByFarmAndSensorType(id, metric);
    }

    @GetMapping("/{id}/{metric}")
    public List<MeasurementResponse> listAllMeasurements(
            @PathVariable Long id,
            @PathVariable String metric,
            @RequestParam(defaultValue = "", name = "start") String startDate,
            @RequestParam(defaultValue = "", name = "end") String endDate
    ) {
        if (startDate.isEmpty() && endDate.isEmpty()) {
            return listAllMeasurements(id, metric);
        }
        return farmService.findMeasurementsByDataRange(id, metric, startDate, endDate);
    }
}
