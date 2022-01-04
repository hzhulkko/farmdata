package com.example.farmdata.data;

import com.example.farmdata.loader.NumericConverter;
import com.example.farmdata.loader.ZonedDateTimeConverter;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvIgnore;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Data
@Entity
public class FarmDataItem {

    @CsvIgnore
    private @Id @GeneratedValue Long id;
    @CsvBindByName
    private String location;
    @CsvCustomBindByName(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateTime;
    @CsvBindByName
    private SensorType sensorType;
    @CsvCustomBindByName(converter = NumericConverter.class)
    private Double value;
}
