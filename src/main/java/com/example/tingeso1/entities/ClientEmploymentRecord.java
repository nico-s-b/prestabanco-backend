package com.example.tingeso1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "employment_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientEmploymentRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @JsonManagedReference(value = "employment-client")
    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private Boolean isWorking;
    private Boolean isEmployee;
    private ZonedDateTime currentWorkStartDate;
    private int monthlyIncome;
    private int lastTwoYearIncome;
}
