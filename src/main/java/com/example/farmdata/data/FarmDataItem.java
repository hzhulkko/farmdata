package com.example.farmdata.data;

import com.example.farmdata.loader.StringToNumericConverter;
import com.example.farmdata.loader.StringToZonedDateTimeConverter;
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
    @CsvCustomBindByName(converter = StringToZonedDateTimeConverter.class)
    private ZonedDateTime dateTime;
    @CsvBindByName
    private String sensorType;
    @CsvCustomBindByName(converter = StringToNumericConverter.class)
    private Double value;
}
