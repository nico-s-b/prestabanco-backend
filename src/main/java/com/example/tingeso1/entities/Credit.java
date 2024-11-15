package com.example.tingeso1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;

import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.enums.CreditState;

import jakarta.persistence.*;
import lombok.ToString;

@Entity
@Table(name = "credits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @JsonBackReference(value = "client-credit")
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @JsonBackReference(value = "exec-credit")
    @ManyToOne
    @JoinColumn(name = "executive_id")
    private Executive executive;

    @JsonManagedReference(value = "credit-docs")
    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<DocumentEntity> documents;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;

    @Enumerated(EnumType.STRING)
    private CreditState state;

    private int loanPeriod;
    private float annualRate;
    private int creditMount;
    private int propertyValue;
    private int totalCost;
    private ZonedDateTime requestDate;
    private ZonedDateTime lastUpdateDate;
}
