package com.example.farmdata.api;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/farm")
public class FarmDataController {

    @Autowired
    private final FarmControllerService service;

    @GetMapping("/")
    public List<FarmResponse> listAllFarms() {
        return service.findAllFarms();
    }

    @GetMapping("/{id}")
    public FarmResponse showFarmDetails(@PathVariable String id) {
        return service.findFarmById(Long.parseLong(id));
    }
}
