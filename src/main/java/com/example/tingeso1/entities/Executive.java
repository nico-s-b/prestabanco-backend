package com.example.tingeso1.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("EXECUTIVE")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Executive extends User{

    @JsonManagedReference
    @OneToMany(mappedBy = "executive", cascade = CascadeType.ALL)
    private List<Credit> credits = new ArrayList<>();

}