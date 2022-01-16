package com.example.farmdata.loader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.internal.util.collections.Sets;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FarmDataLoaderTest {

    private List<FarmDataItem> data;

    @BeforeAll
    void readTestData() {
        data = FarmDataLoader.readFarmDataItems("test_data.csv");
    }

    @Test
    void whenValidData_ThenShouldReadToFarmDataItems() {
        var locations = data.stream().map(FarmDataItem::getLocation).collect(Collectors.toSet());
        assertAll(
                () -> assertEquals(6, data.size()),
                () -> assertEquals(Sets.newSet("valid_data"), locations)
        );
    }

    @Test
    void whenNonNumericValue_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("invalid_non_numeric")));
    }

    @Test
    void whenEmptyValue_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("invalid_empty_value")));
    }

    @Test
    void whenInvalidTemperature_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("invalid_temperature")));
    }

    @Test
    void whenInvalidRainFall_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("invalid_rainFall")));
    }

    @Test
    void whenInvalidpH_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("invalid_pH")));
    }

    @Test
    void whenUnknownSensorType_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("unknown_sensor")));
    }

    @Test
    void whenWrongDateTimeFormatType_ThenShouldBeIgnored() {
        assertTrue(data.stream().noneMatch(item -> item.getLocation().equals("wrong_datetime_format")));
    }



}