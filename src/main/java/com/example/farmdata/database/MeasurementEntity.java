package com.example.farmdata.database;

import com.example.farmdata.data.SensorType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "measurement")
@Getter
@Setter
public class MeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "measurement_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "sensor_type")
    private SensorTypeEntity sensorType;

    @Column(name = "measurement_value")
    private Double value;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private FarmEntity farm;

}
