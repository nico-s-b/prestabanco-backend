package com.example.tingeso1.controllers;

import com.example.tingeso1.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientService;

import java.util.List;

@RestController
@RequestMapping("api/v1/clients")
@CrossOrigin(origins = "*")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<Client>> lisClients() {
        List<Client> clients = clientService.getAllClients();
        return ResponseEntity.ok(clients);
    }

    @PostMapping("/")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        ClientAccount newAccount = new ClientAccount();
        newAccount.setClient(client);
        client.setAccount(newAccount);

        ClientEmploymentRecord newEmploymentRecord = new ClientEmploymentRecord();
        newEmploymentRecord.setClient(client);
        client.setEmploymentRecord(newEmploymentRecord);

        ClientCreditRecord newCreditRecord = new ClientCreditRecord();
        newCreditRecord.setClient(client);
        client.setCreditRecord(newCreditRecord);

        Client savedClient = clientService.saveClient(client);
        return ResponseEntity.ok(savedClient);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<Client> updateClient(@PathVariable Long clientId, @RequestBody Client client) {
        client.setId(clientId);
        Client clientUpdated = clientService.saveClient(client);
        return ResponseEntity.ok(clientUpdated);
    }

    @GetMapping("/{clientId}")
    public ResponseEntity<Client> getClientById(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

