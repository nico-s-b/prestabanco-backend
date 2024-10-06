package com.example.tingeso1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "client_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @OneToOne(mappedBy = "account")
    private Client client;
    
    @OneToMany(mappedBy="account")
    private List<AccountMovement> movements;

    private int accountBalance;
    private ZonedDateTime startDate;
}