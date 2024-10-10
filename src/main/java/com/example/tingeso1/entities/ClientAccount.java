package com.example.tingeso1.entities;

import com.example.tingeso1.enums.SaveCapacityStatus;
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
    private Boolean r1MinimumBalance;
    private Boolean r2ConsistentSaves;
    private Boolean r3PeriodicDeposits;
    private Boolean r4BalanceYearsOfAccountRelation;
    private Boolean r5RecentWithdrawals;
    private SaveCapacityStatus saveCapacityStatus;
}