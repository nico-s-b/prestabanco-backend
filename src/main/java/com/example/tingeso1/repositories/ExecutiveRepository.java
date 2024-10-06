package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutiveRepository extends JpaRepository<Executive, Long> {
}