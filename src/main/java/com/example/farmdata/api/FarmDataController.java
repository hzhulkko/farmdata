package com.example.farmdata.api;

import com.example.farmdata.service.FarmDataService;
import com.example.farmdata.data.FarmDataItem;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/farm")
public class FarmDataController {

    @Autowired
    private final FarmDataService service;

    @GetMapping("/")
    public List<FarmDataItem> listAllItems() {
        return service.findAll();
    }
}
