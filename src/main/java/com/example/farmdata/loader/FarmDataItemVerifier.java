package com.example.farmdata.loader;

import com.example.farmdata.data.SensorType;
import com.opencsv.bean.BeanVerifier;
import org.springframework.stereotype.Component;

@Component
public class FarmDataItemVerifier implements BeanVerifier<FarmDataItem> {
    @Override
    public boolean verifyBean(FarmDataItem bean) {
        return temperatureIsInRange(bean) && rainFallIsInRange(bean) && pHIsInRange(bean);
    }

    private boolean temperatureIsInRange(FarmDataItem item) {
        if (item.getSensorType().equals(SensorType.TEMPERATURE)) {
            return item.getValue() >= -50.0 && item.getValue() <= 100.0;
        }
        return true;
    }

    private boolean rainFallIsInRange(FarmDataItem item) {
        if (item.getSensorType().equals(SensorType.RAINFALL)) {
            return item.getValue() >= 0.0 && item.getValue() <= 500.0;
        }
        return true;
    }

    private boolean pHIsInRange(FarmDataItem item) {
        if (item.getSensorType().equals(SensorType.PH)) {
            return item.getValue() >= 0.0 && item.getValue() <= 14.0;
        }
        return true;
    }
}
