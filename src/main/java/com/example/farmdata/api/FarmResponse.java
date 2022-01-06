package com.example.farmdata.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class FarmResponse {

    private String name;
    private List<MeasurementResponse> measurements;
}
