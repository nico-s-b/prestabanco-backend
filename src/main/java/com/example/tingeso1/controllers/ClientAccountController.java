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
@CrossOrigin("*")
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

    @PostMapping("/{clientId}/account")
    public ResponseEntity<ClientAccount> createOrUpdateAccount(@PathVariable Long clientId, @RequestBody ClientAccount account) {
        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        ClientAccount existingAccount = client.getAccount();
        // Update si existe cuenta, actualizar solo info del cliente
        if (existingAccount != null) {
            existingAccount.setAccountBalance(account.getAccountBalance());
            existingAccount.setStartDate(account.getStartDate());

            existingAccount.setR1MinimumBalance(existingAccount.getR1MinimumBalance());
            existingAccount.setR2ConsistentSaves(existingAccount.getR2ConsistentSaves());
            existingAccount.setR3PeriodicDeposits(existingAccount.getR3PeriodicDeposits());
            existingAccount.setR4BalanceYearsOfAccountRelation(existingAccount.getR4BalanceYearsOfAccountRelation());
            existingAccount.setR5RecentWithdrawals(existingAccount.getR5RecentWithdrawals());
            existingAccount.setSaveCapacityStatus(existingAccount.getSaveCapacityStatus());

            ClientAccount updatedAccount = clientAccountService.updateClientAccount(existingAccount);
            return ResponseEntity.ok(updatedAccount);
        } else {
            //Create si no existe cuenta
            account.setClient(client);
            ClientAccount savedAccount = clientAccountService.saveClientAccount(account);
            return ResponseEntity.ok(savedAccount);
        }
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

