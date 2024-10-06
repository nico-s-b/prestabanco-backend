package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.AccountMovement;
import com.example.tingeso1.repositories.AccountMovementRepository;

@Service
public class AccountMovementService {

    @Autowired
    AccountMovementRepository accountMovementRepository;

    public ArrayList<AccountMovement> getAccountMovements(){
        return (ArrayList<AccountMovement>) accountMovementRepository.findAll();
    }

    public AccountMovement saveAccountMovement(AccountMovement accountMovement){
        return accountMovementRepository.save(accountMovement);
    }

    public AccountMovement getAccountMovementById(Long id){
        return accountMovementRepository.findById(id).get();
    }

    public AccountMovement updateAccountMovement(AccountMovement accountMovement) {
        return accountMovementRepository.save(accountMovement);
    }

    public boolean deleteAccountMovement(Long id) throws Exception {
        try{
            accountMovementRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
