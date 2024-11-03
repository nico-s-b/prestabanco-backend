package com.example.tingeso1.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.util.List;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENT")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User{

    @JsonBackReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private ClientAccount account;

    @JsonBackReference
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ClientCreditRecord creditRecord;

    @JsonBackReference
    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private ClientEmploymentRecord employmentRecord;

    @JsonManagedReference
    @OneToMany(mappedBy="client", cascade = CascadeType.ALL)
    private List<Credit> credits;
}