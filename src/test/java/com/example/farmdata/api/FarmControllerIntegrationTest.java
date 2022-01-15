package com.example.farmdata.api;

import com.example.farmdata.AbstractFarmDataIntegrationTest;
import com.example.farmdata.data.SensorType;
import com.example.farmdata.loader.DataLoaderService;
import com.example.farmdata.loader.FarmDataItem;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FarmControllerIntegrationTest extends AbstractFarmDataIntegrationTest {

    @Autowired
    DataLoaderService dataLoaderService;

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.basePath = "/api/v1/farm";
    }

    @Test
    void whenNonExistentPath_thenReturnJsonNotFound() {
        given()
                .basePath("/")
                .when().get("/foo")
                .then().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetFarmsWithUnsupportedMethod_thenReturnBadRequest() {
        given()
                .when().post("/")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.METHOD_NOT_ALLOWED.value())
                .contentType(ContentType.JSON);
    }

    @Test
    void whenGetFarmWithNonNumericParameter_thenReturnBadRequest() {
        given()
                .when().get("/foo")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("error", is(equalTo("For input string: \"foo\"")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenNoFarmsExist_whenListAllFarmsCalled_thenReturnEmptyList() {
        given()
                .when().get("/")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(is(equalTo("[]")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenFarmsExist_whenListAllFarmsCalled_thenReturnAllFarm() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_2", ZonedDateTime.now(), SensorType.pH, 2.0)
        ));
        given()
                .when().get("/")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("name", hasItems("farm_1", "farm_2"));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenFarmExists_whenShowFarmDetailsCalled_thenReturnFarm() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        given()
                .when().get("/1")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("id", is(equalTo(1)))
                .and()
                .body("name", is(equalTo("farm_1")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenNoFarmExists_whenShowFarmDetailsCalled_thenReturnErrorResponse() {
        given()
                .when().get("/1")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .contentType(ContentType.JSON)
                .body("error", is(equalTo("Farm not found with id 1")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMetricDataExists_whenListAllMeasurementsCalledWithMetric_thenReturnMeasurementWithCorrectData() {
        var timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", timestamp, SensorType.pH, 1.0)
        ));
        given()
                .when().get("/1/ph")
                .then().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body("sensorType[0]", equalTo("pH"))
                .and()
                .body("timestamp[0]", equalTo(timestamp.format(DateTimeFormatter.ISO_DATE_TIME)))
                .and()
                .body("value[0]", equalTo(1.0F));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMetricDataNotExists_whenListAllMeasurementsCalledWithMetric_thenReturnEmptyList() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_2", ZonedDateTime.now(), SensorType.temperature, 1.0)
        ));
        given()
                .when().get("/1/temperature")
                .then().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.OK.value())
                .body(is(equalTo("[]")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenSensorTypeNotExists_whenListAllMeasurementsCalledWithMetric_thenReturnErrorResponse() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        given()
                .when().get("/1/humidity")
                .then().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("error", is(equalTo("Sensor type humidity does not exist")));
    }

    @Test
    @Sql({"/schema.sql"})
    void givenMetricDataExists_whenListAllMeasurementsCalledWithDateRange_thenReturnMeasurement() {
        var timestamp = ZonedDateTime.now(ZoneOffset.UTC);
        dataLoaderService.saveAll(Arrays.asList(
                new FarmDataItem("farm_1", timestamp.minusDays(2), SensorType.pH, 1.0),
                new FarmDataItem("farm_1", timestamp, SensorType.pH, 2.0)
        ));
        var start = timestamp.minusDays(2).toLocalDate().format(DateTimeFormatter.ISO_DATE);
        var end = timestamp.minusDays(1).toLocalDate().format(DateTimeFormatter.ISO_DATE);
        given()
                .param("start", start)
                .param("end", end)
                .when().get("/1/PH")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body(".", hasSize(1))
                .body("timestamp[0]", equalTo(timestamp.minusDays(2).format(DateTimeFormatter.ISO_DATE_TIME)));
    }

    @Test
    @Sql({"/schema.sql"})
    void whenListAllMeasurementsCalledWithInvalidDate_thenReturnErrorResponse() {
        dataLoaderService.saveAll(Collections.singletonList(
                new FarmDataItem("farm_1", ZonedDateTime.now(), SensorType.pH, 1.0)
        ));
        var start = "2020/01/01";
        given()
                .param("start", start)
                .when().get("/1/pH")
                .then().log().ifValidationFails()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .contentType(ContentType.JSON)
                .body("error",
                        is(equalTo("Invalid date format: 2020/01/01. Date should be of format yyyy-MM-dd")));
    }

}
