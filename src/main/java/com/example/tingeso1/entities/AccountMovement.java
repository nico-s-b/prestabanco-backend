package com.example.tingeso1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "account_movements")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="client_account_id", nullable=false)
    private ClientAccount account;

    private ZonedDateTime movementDate;
    private int amount;
    private Boolean isDeposit;
}
