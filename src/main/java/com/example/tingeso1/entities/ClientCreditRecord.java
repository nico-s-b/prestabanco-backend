package com.example.tingeso1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "credit_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientCreditRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private Boolean isSlowpayer;
    private Boolean hasSeriousDebts;
    private int debtAmount;
    private ZonedDateTime lastDebtDate;
    private ZonedDateTime oldestDebtDate;
}
