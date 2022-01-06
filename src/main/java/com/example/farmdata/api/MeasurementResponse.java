package com.example.farmdata.api;

import com.example.farmdata.data.SensorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MeasurementResponse {

    private SensorType sensorType;
    private Double value;
    private ZonedDateTime timestamp;
}
