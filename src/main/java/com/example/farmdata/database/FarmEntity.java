package com.example.farmdata.database;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "farm")
@Getter
@Setter
public class FarmEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "farm_id")
    private Long id;
    @Column(name = "farm_name", unique = true)
    private String name;
    @OneToMany(mappedBy = "farm", fetch = FetchType.LAZY)
    private Set<MeasurementEntity> measurements;
}
