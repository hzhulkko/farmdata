package com.example.farmdata.loader;

import com.example.farmdata.database.FarmEntity;
import com.example.farmdata.database.MeasurementEntity;

public class FarmDataMapper {

    private FarmDataMapper() {}

    public static FarmEntity mapToFarm(FarmDataItem item) {
        var farm = new FarmEntity();
        farm.setName(item.getLocation());
        return farm;
    }

    public static MeasurementEntity mapToMeasurement(FarmDataItem item) {
        var measurement = new MeasurementEntity();
        measurement.setSensorType(item.getSensorType());
        measurement.setTimestamp(item.getDateTime());
        measurement.setValue(item.getValue());
        return measurement;
    }
}
