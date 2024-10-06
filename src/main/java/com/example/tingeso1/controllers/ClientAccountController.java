package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientAccountService;
import com.example.tingeso1.entities.ClientAccount;

@RestController
@RequestMapping("api/v1/accounts")
@CrossOrigin("*")
public class ClientAccountController {
    @Autowired
    ClientAccountService clientAccountService;

    @GetMapping("/")
    public ResponseEntity<List<ClientAccount>> listClientAccounts() {
        List<ClientAccount> clientAccounts = clientAccountService.getClientAccounts();
        return ResponseEntity.ok(clientAccounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientAccount> getClientAccountById(@PathVariable Long id) {
        ClientAccount clientAccount = clientAccountService.getClientAccountById(id);
        if (clientAccount != null) {
            return ResponseEntity.ok(clientAccount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<ClientAccount> saveClientAccount(@RequestBody ClientAccount clientAccount) {
        ClientAccount clientAccountNew = clientAccountService.saveClientAccount(clientAccount);
        return ResponseEntity.ok(clientAccountNew);
    }

    @PutMapping("/")
    public ResponseEntity<ClientAccount> updateClientAccount(@RequestBody ClientAccount clientAccount) {
        ClientAccount clientAccountUpdated = clientAccountService.updateClientAccount(clientAccount);
        return ResponseEntity.ok(clientAccountUpdated);
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

