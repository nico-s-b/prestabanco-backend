package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientEmploymentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientEmploymentRecordRepository extends JpaRepository<ClientEmploymentRecord, Long> {
}