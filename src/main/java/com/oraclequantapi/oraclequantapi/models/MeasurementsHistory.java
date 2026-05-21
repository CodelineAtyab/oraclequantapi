package com.oraclequantapi.oraclequantapi.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MEASUREMENT_TABLE")
public class MeasurementsHistory {

    // UUID string
    @Id
    @Column(name = "ID", length = 36)
    public String id;

    // Time when the API received the conversion request.
    @Column(name = "REQUEST_TIMESTAMP")
    public LocalDateTime timestamp;

    // Client IP address. JsonProperty makes the JSON name match the task wording.
    @JsonProperty("source_ip_address")
    @Column(name = "SOURCE_IP_ADDRESS")
    public String sourceIpAddress;


    // Normalized input string after converting capital letters to lowercase.
    @Lob
    @Column(name = "MEASUREMENT_INPUT")
    public String input;

    // Output is stored as text, for example "[2,6]".
    @Lob
    @Column(name = "MEASUREMENT_OUTPUT")
    public String output;

}
