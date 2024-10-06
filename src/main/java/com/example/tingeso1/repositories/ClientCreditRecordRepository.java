package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientCreditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientCreditRecordRepository extends JpaRepository<ClientCreditRecord, Long> {
}