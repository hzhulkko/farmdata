package com.example.farmdata.api;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MeasurementResponse {

    private String sensorType;
    private Double value;
    private ZonedDateTime timestamp;
}
