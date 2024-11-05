package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findAllByCreditId(Long id);
}