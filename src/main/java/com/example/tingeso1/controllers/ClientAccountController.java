package com.example.tingeso1.controllers;

import java.util.List;

import com.example.tingeso1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientAccountService;
import com.example.tingeso1.entities.ClientAccount;
import com.example.tingeso1.entities.Client;

@RestController
@RequestMapping("api/v1/accounts")
@CrossOrigin(origins = "http://prestabanco.brazilsouth.cloudapp.azure.com:8070")
public class ClientAccountController {
    @Autowired
    ClientAccountService clientAccountService;

    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientAccount>> listClientAccounts() {
        List<ClientAccount> clientAccounts = clientAccountService.getClientAccounts();
        return ResponseEntity.ok(clientAccounts);
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientAccount> createOrUpdateAccount(
            @PathVariable Long clientId,
            @RequestBody ClientAccount clientAccount) {

        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        ClientAccount existingRecord = clientAccountService.getClientAccountByClient(clientId);
        if (existingRecord != null) {
            clientAccount.setId(existingRecord.getId());
        }

        clientAccount.setClient(client);
        ClientAccount savedRecord = clientAccountService.saveClientAccount(clientAccount);
        return ResponseEntity.ok(savedRecord);
    }


    @GetMapping("/{clientId}")
    public ResponseEntity<ClientAccount> getAccountByClient(@PathVariable Long clientId) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            ClientAccount account = clientAccountService.getClientAccountByClient(clientId);
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientAccountById(@PathVariable Long id) throws Exception {
        var isDeleted = clientAccountService.deleteClientAccount(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

