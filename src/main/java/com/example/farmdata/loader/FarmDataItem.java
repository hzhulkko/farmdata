package com.example.farmdata.loader;

import com.example.farmdata.data.SensorType;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class FarmDataItem {

    @CsvBindByName
    private String location;
    @CsvCustomBindByName(converter = ZonedDateTimeConverter.class)
    private ZonedDateTime dateTime;
    @CsvBindByName
    private SensorType sensorType;
    @CsvCustomBindByName(converter = NumericConverter.class)
    private Double value;
}
