package com.example.farmdata.loader;

import com.example.farmdata.data.SensorType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FarmDataMapperTest {

    @Test
    void givenFarmDataItem_thenFarmEntityCreated() {
        var testItem = getRandomItem("test_farm");
        var farmEntity = FarmDataMapper.mapToFarm(testItem);
        Assertions.assertAll(
                () -> assertNull(farmEntity.getId()),
                () -> assertNull(farmEntity.getMeasurements()),
                () -> assertEquals(testItem.getLocation(), farmEntity.getName())
        );
    }

    @Test
    void givenFarmDataItem_thenMeasurementEntityCreated() {
        var testItem = getRandomItem("test_farm");
        var measurementEntity = FarmDataMapper.mapToMeasurement(testItem);
        Assertions.assertAll(
                () -> assertNull(measurementEntity.getId()),
                () -> assertNull(measurementEntity.getFarm()),
                () -> assertNull(measurementEntity.getSensorType()),
                () -> assertEquals(testItem.getDateTime(), measurementEntity.getTimestamp()),
                () -> assertEquals(testItem.getValue(), measurementEntity.getValue())
        );
    }

    private FarmDataItem getRandomItem(String name) {
        var item = new FarmDataItem();
        item.setLocation(name);
        item.setValue(new Random().nextDouble() * 10.0);
        item.setSensorType(getRandomSensoryType());
        item.setDateTime(ZonedDateTime.now());
        return item;
    }

    private SensorType getRandomSensoryType() {
        var options = SensorType.values();
        var index = new Random().nextInt(options.length);
        return SensorType.values()[index];
    }

}