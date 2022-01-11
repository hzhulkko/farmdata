package com.example.farmdata.loader;

import com.example.farmdata.data.SensorType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FarmDataItemVerifierTest {

    private final FarmDataItemVerifier verifier = new FarmDataItemVerifier();

    @ParameterizedTest
    @ValueSource(doubles = {-50.0001, 100.001})
    void whenTemperatureOutOfRange_ThenShouldReturnFalse(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.temperature);
        item.setValue(value);
        assertFalse(verifier.verifyBean(item));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-50, 100})
    void whenTemperatureInRange_ThenShouldReturnTrue(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.temperature);
        item.setValue(value);
        assertTrue(verifier.verifyBean(item));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.00001, 500.0001})
    void whenRainfallOutOfRange_ThenShouldReturnFalse(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.rainfall);
        item.setValue(value);
        assertFalse(verifier.verifyBean(item));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.0, 0.00001, 500})
    void whenRainfallInRange_ThenShouldReturnTrue(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.rainfall);
        item.setValue(value);
        assertTrue(verifier.verifyBean(item));
    }

    @ParameterizedTest
    @ValueSource(doubles = {-0.00001, 14.1})
    void whenpHOutOfRange_ThenShouldReturnFalse(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.pH);
        item.setValue(value);
        assertFalse(verifier.verifyBean(item));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.00001, -0.0, 14})
    void whenpHInRange_ThenShouldReturnTrue(double value) {
        FarmDataItem item = new FarmDataItem();
        item.setSensorType(SensorType.pH);
        item.setValue(value);
        assertTrue(verifier.verifyBean(item));
    }

}