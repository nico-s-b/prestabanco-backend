package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.AccountMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountMovementRepository extends JpaRepository<AccountMovement, Long> {
}