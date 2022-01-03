package com.example.farmdata.repository;

import com.example.farmdata.data.FarmDataItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmDataRepository extends JpaRepository<FarmDataItem, Long> {
}
