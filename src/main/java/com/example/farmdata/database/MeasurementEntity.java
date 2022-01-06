package com.example.farmdata.database;

import com.example.farmdata.data.SensorType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @Column(name = "measurement_id")
    private Long id;
    @Column(name = "timestamp")
    private ZonedDateTime timestamp;
    @Column(name = "sensor_type")
    private SensorType sensorType;
    @Column(name = "value")
    private Double value;
    @ManyToOne
    @JoinColumn(name = "farm_id")
    private FarmEntity farm;

}
