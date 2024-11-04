package com.example.tingeso1.entities;

import com.example.tingeso1.enums.SaveCapacityStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonManagedReference(value = "account-client")
    @OneToOne
    @MapsId
    @JoinColumn(name = "client_id")
    private Client client;

    private int accountBalance;
    private ZonedDateTime startDate;
    private Boolean r1MinimumBalance;                   //Has automatic method
    private Boolean r2ConsistentSaves;                  //Has to be manually verified
    private Boolean r3PeriodicDeposits;                 //Has to be manually verified
    private Boolean r4BalanceYearsOfAccountRelation;    //Has automatic method
    private Boolean r5RecentWithdrawals;                //Has to be manually verified
    private SaveCapacityStatus saveCapacityStatus;
}