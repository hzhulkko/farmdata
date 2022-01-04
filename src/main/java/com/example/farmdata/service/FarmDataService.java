package com.example.farmdata.service;

import com.example.farmdata.data.FarmDataItem;
import com.example.farmdata.repository.FarmDataRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FarmDataService {

    @Autowired
    private final FarmDataRepository repository;

    public void saveAll(List<FarmDataItem> data) {
        repository.saveAll(data);
    }

    public List<FarmDataItem> findAll() {
        return repository.findAll();
    }
}
