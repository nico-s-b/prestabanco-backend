package com.example.tingeso1.controllers;

import java.util.List;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientCreditRecordService;
import com.example.tingeso1.entities.ClientCreditRecord;

@RestController
@RequestMapping("api/v1/creditrecords")
@CrossOrigin(origins = "*")
public class ClientCreditRecordController {
    @Autowired
    ClientCreditRecordService clientCreditRecordService;

    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientCreditRecord>> listClientCreditRecords() {
        List<ClientCreditRecord> clientCreditRecords = clientCreditRecordService.getClientCreditRecords();
        return ResponseEntity.ok(clientCreditRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientCreditRecord> getClientCreditRecordById(@PathVariable Long id) {
        ClientCreditRecord clientCreditRecord = clientCreditRecordService.getClientCreditRecordById(id);
        if (clientCreditRecord != null) {
            return ResponseEntity.ok(clientCreditRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientCreditRecord> createOrUpdateClientCreditRecord(
            @PathVariable Long clientId,
            @RequestBody ClientCreditRecord creditRecord) {

        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        ClientCreditRecord existingRecord = clientCreditRecordService.getClientCreditRecordById(clientId);
        if (existingRecord != null) {
            creditRecord.setId(existingRecord.getId());
        }

        creditRecord.setClient(client);
        ClientCreditRecord savedRecord = clientCreditRecordService.saveClientCreditRecord(creditRecord);
        return ResponseEntity.ok(savedRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientCreditRecordById(@PathVariable Long id) throws Exception {
        var isDeleted = clientCreditRecordService.deleteClientCreditRecord(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

