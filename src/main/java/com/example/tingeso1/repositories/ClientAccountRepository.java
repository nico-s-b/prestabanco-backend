package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {
    ClientAccount findByClientId(Long id);
}