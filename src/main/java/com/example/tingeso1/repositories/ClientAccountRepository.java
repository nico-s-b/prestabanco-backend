package com.example.tingeso1.repositories;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.ClientAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientAccountRepository extends JpaRepository<ClientAccount, Long> {
    ClientAccount findByClient(Client client);
}