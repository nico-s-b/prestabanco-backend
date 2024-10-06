package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.ClientAccount;
import com.example.tingeso1.repositories.ClientAccountRepository;

@Service
public class ClientAccountService {

    @Autowired
    ClientAccountRepository clientAccountRepository;

    public ArrayList<ClientAccount> getClientAccounts(){
        return (ArrayList<ClientAccount>) clientAccountRepository.findAll();
    }

    public ClientAccount saveClientAccount(ClientAccount clientAccount){
        return clientAccountRepository.save(clientAccount);
    }

    public ClientAccount getClientAccountById(Long id){
        return clientAccountRepository.findById(id).get();
    }

    public ClientAccount updateClientAccount(ClientAccount clientAccount) {
        return clientAccountRepository.save(clientAccount);
    }

    public boolean deleteClientAccount(Long id) throws Exception {
        try{
            clientAccountRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

}
