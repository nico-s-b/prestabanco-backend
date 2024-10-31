package com.example.tingeso1.services;

import com.example.tingeso1.entities.*;
import com.example.tingeso1.repositories.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.repositories.ClientRepository;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private CreditRepository creditRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Client saveClient(Client client){
        return clientRepository.save(client);
    }

    public Client getClientById(Long id){
        return clientRepository.findById(id).get();
    }

    public List<Credit> getClientRequest(Client client) {
        return client.getCredits();
    }

    public void addCreditRequest(Client client, Credit credit) {
        credit.setClient(client);
        client.getCredits().add(credit);
        clientRepository.save(client);
    }

    public void deleteCreditRequest(Client client, Credit credit) {
        credit.setClient(null);
        client.getCredits().remove(credit);
        clientRepository.save(client);
        creditRepository.delete(credit);
    }

    public void addClientAccount(Client client, ClientAccount account) {
        account.setClient(client);
        client.setAccount(account);
        clientRepository.save(client);
    }

    public void addClientCreditRecord(Client client, ClientCreditRecord creditRecord) {
        creditRecord.setClient(client);
        client.setCreditRecord(creditRecord);
        clientRepository.save(client);
    }

    public void addClientEmploymentRecord(Client client, ClientEmploymentRecord employmentRecord) {
        employmentRecord.setClient(client);
        client.setEmploymentRecord(employmentRecord);
        clientRepository.save(client);
    }

}
