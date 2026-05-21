package com.oraclequantapi.oraclequantapi.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "MEASUREMENT_TABLE")
public class MeasurementsHistory {

    @Id
    @Column(name = "ID", length = 36)
    public String id;


}
