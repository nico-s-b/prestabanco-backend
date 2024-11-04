package com.example.tingeso1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference(value = "record-client")
    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private int debtAmount;                 //Ingresado por cliente
    private ZonedDateTime lastDebtDate;
    private ZonedDateTime oldestUnpaidInstallmentDate;
    private Boolean isSlowpayer;
    private Boolean hasSeriousDebts;
}
