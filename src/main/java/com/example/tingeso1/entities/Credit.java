package com.example.tingeso1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;
import java.util.List;

import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.enums.CreditState;

import jakarta.persistence.*;

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

    @ManyToOne
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @OneToOne(mappedBy = "credit")
    private Executive executive;

    @OneToMany(mappedBy = "credit", cascade = CascadeType.ALL)
    private List<DocumentEntity> documents;

    @Enumerated(EnumType.STRING)
    private CreditType creditType;
    
    private int loanPeriod;
    private int rate;
    private int creditMount;
    private int totalCost;
    private Boolean isSimulation;
    
    @Enumerated(EnumType.STRING)
    private CreditState state;
    
    private ZonedDateTime requestDate;
    private ZonedDateTime lastUpdateDate;
}
