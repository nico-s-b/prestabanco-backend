package com.example.tingeso1.entities;

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

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private Boolean isWorking;
    private int monthlyIncome;
    private ZonedDateTime currentWorkStartDate;
    private Boolean isEmployee;
    private int lastTwoYearIncome;
}
