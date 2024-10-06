/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.tingeso1.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.ZonedDateTime;

import com.example.tingeso1.enums.DocumentType;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="credit_id", nullable=true)
    private Credit credit;

    @ManyToOne
    @JoinColumn(name="client_id", nullable=true)
    private Client client;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;
    
    private String url;
    private ZonedDateTime uploadDate;
}