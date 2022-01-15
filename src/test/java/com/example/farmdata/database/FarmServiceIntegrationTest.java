package com.example.farmdata.database;

import com.example.farmdata.AbstractFarmDataIntegrationTest;
import com.example.farmdata.api.exception.DataNotFoundException;
import com.example.farmdata.api.FarmService;
import com.example.farmdata.api.MeasurementResponse;
import com.example.farmdata.data.SensorType;
import com.example.farmdata.loader.DataLoaderService;
import com.example.farmdata.loader.FarmDataItem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;

class FarmServiceIntegrationTest extends AbstractFarmDataIntegrationTest {

    @Autowired
    DataLoaderService dataLoaderService;

    @Autowired
    FarmService farmService;

    @Test
    @Sql({"/schema.sql"})
    void givenNoFarmsInDatabase_thenFindAllFarmsReturnsEmptyList() {
        var farms = farmService.findAllFarms();
        Assertions.assertTrue(farms.isEmpty());
    }

    @Test
    @Sql({"/schema.sql"})
    void givenFarmsInDatabase_thenFindFarmAllFarmsReturnsAllFarms() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_2", ZonedDateTime.now(), SensorType.pH, 2.0)
        ));
        var farms = farmService.findAllFarms();
        Assertions.assertEquals(2, farms.size());
    }

    @Test
    @Sql({"/schema.sql"})
    void givenNoFarmsInDatabase_thenFindFarmAllFarmsReturnsEmptyList() {
        var farms = farmService.findAllFarms();
        Assertions.assertTrue(farms.isEmpty());
    }

    @Test
    @Sql({"/schema.sql"})
    void givenNoFarmsInDatabase_thenFindFarmAllFarmsThrowsDataNotFoundException() {
        Assertions.assertThrows(DataNotFoundException.class, () -> farmService.findFarmById(1L));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenFarmsInDatabase_thenFindFarmByIdReturnsCorrectFarm() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_2", ZonedDateTime.now(), SensorType.pH, 2.0)
        ));
        var farmToFind = farmService.findFarmById(1L);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(farmToFind),
                () -> Assertions.assertEquals("farm_1", farmToFind.getName())
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenFarmNotInDatabase_thenFindFarmByIdThrowsDataNotFoundException() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        Assertions.assertThrows(DataNotFoundException.class, () -> farmService.findFarmById(2L));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementsInDatabase_thenOnlyMeasurementsForRequestedSensorTypeReturned() {
        dataLoaderService.saveAll(Arrays.asList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.temperature, 1.0),
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 2.0),
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 3.0)
        ));
        var sensorType = SensorType.pH;
        var measurements =
                farmService.findMeasurementsByFarmAndSensorType(1L, sensorType.name());
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, measurements.size()),
                () -> Assertions.assertTrue(measurements.stream()
                        .allMatch(result -> result.getSensorType().equals(sensorType.name()))
                )
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementsInDatabase_whenFindMeasurementsWithNonMatchingSensorType_thenEmptyListReturned() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_2", ZonedDateTime.now(), SensorType.temperature, 1.0)
        ));
        var sensorType = SensorType.temperature;
        var measurements =
                farmService.findMeasurementsByFarmAndSensorType(1L, sensorType.name());
        Assertions.assertTrue(measurements.isEmpty());
    }

    @Test
    @Sql({"/schema.sql"})
    void givenNoSensorTypeInDatabase_whenFindMeasurements_thenThrowDataNotFoundException() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        Assertions.assertThrows(DataNotFoundException.class,
                () -> farmService.findMeasurementsByFarmAndSensorType(1L, "humidity")
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementInDatabase_thenMeasurementWithCorrectTimestampAndValueReturned() {
        var expectedDate = LocalDate.parse("2019-01-01").atStartOfDay(ZoneOffset.UTC);
        var expectedValue = 5.0;
        var sensorType = SensorType.pH;
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", expectedDate, sensorType, expectedValue)
        ));
        var measurements =
                farmService.findMeasurementsByFarmAndSensorType(1L, sensorType.name());
        var actual = measurements.get(0);
        Assertions.assertAll(
                () -> Assertions.assertNotNull(actual),
                () -> Assertions.assertEquals(expectedDate, actual.getTimestamp()),
                () -> Assertions.assertEquals(expectedValue, actual.getValue())
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementsInDatabase_whenQueryWithStartAndEndDate_thenMeasurementsFromBetweenStartAndEndDateDateReturned() {
        var someDate = ZonedDateTime.parse("2019-01-01T00:00:00.001Z");
        var sensorType = SensorType.temperature;
        dataLoaderService.saveAll(Arrays.asList(
                new FarmDataItem("farm_1", someDate.minusDays(1), sensorType, 1.0),
                new FarmDataItem("farm_1", someDate, sensorType, 2.0),
                new FarmDataItem("farm_1", someDate.plusDays(1), sensorType, 3.0)
        ));
        var start = "2018-12-31";
        var end = "2019-01-01";
        var expectedValues = Arrays.asList(1.0, 2.0);
        var measurements =
                farmService.findMeasurementsByDataRange(1L, sensorType.name(), start, end);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, measurements.size()),
                () -> Assertions.assertTrue(measurements.stream()
                        .map(MeasurementResponse::getValue)
                        .allMatch(expectedValues::contains)
                )
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementsInDatabase_whenQueryEndDateOnly_thenMeasurementsFromBeforeEndDateDateReturned() {
        var someDate = LocalDate.parse("2019-01-01").atStartOfDay(ZoneOffset.UTC);
        var sensorType = SensorType.rainfall;
        dataLoaderService.saveAll(Arrays.asList(
                new FarmDataItem("farm_1", someDate.minusDays(1), sensorType, 1.0),
                new FarmDataItem("farm_1", someDate, sensorType, 2.0),
                new FarmDataItem("farm_1", someDate.plusDays(1), sensorType, 3.0)
        ));
        var end = "2019-01-01";
        var start = "";
        var expectedValues = Arrays.asList(1.0, 2.0);
        var measurements =
                farmService.findMeasurementsByDataRange(1L, sensorType.name(), start, end);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, measurements.size()),
                () -> Assertions.assertTrue(measurements.stream()
                        .map(MeasurementResponse::getValue)
                        .allMatch(expectedValues::contains)
                )
        );
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMeasurementsInDatabase_whenQueryStartDateOnly_thenMeasurementsFBetweenStartDateAndCurrentDateReturned() {
        var someDate = LocalDate.parse("2019-01-01").atStartOfDay(ZoneOffset.UTC);
        var sensorType = SensorType.rainfall;
        dataLoaderService.saveAll(Arrays.asList(
                new FarmDataItem("farm_1", someDate.minusDays(1), sensorType, 1.0),
                new FarmDataItem("farm_1", someDate, sensorType, 2.0),
                new FarmDataItem("farm_1", someDate.plusDays(1), sensorType, 3.0)
        ));
        var end = "";
        var start = "2019-01-01";
        var expectedValues = Arrays.asList(2.0, 3.0);
        var measurements =
                farmService.findMeasurementsByDataRange(1L, sensorType.name(), start, end);
        Assertions.assertAll(
                () -> Assertions.assertEquals(2, measurements.size()),
                () -> Assertions.assertTrue(measurements.stream()
                        .map(MeasurementResponse::getValue)
                        .allMatch(expectedValues::contains)
                )
        );
    }

}
