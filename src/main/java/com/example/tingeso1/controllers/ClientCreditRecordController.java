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
@CrossOrigin("*")
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

    @PostMapping("/{clientId}")
    public ResponseEntity<ClientCreditRecord> createCreditRecord(@PathVariable Long clientId, @RequestBody ClientCreditRecord creditRecord) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            creditRecord.setClient(client);
            ClientCreditRecord savedRecord = clientCreditRecordService.saveClientCreditRecord(creditRecord);
            return ResponseEntity.ok(savedRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{clientId}/{recordId}")
    public ResponseEntity<ClientCreditRecord> updateClientCreditRecord(@PathVariable Long clientId, @PathVariable Long recordId, @RequestBody ClientCreditRecord creditRecord) {
        Client client = clientService.getClientById(clientId);
        if (client != null) {
            ClientCreditRecord existingRecord = clientCreditRecordService.getClientCreditRecordById(recordId);
            if (existingRecord != null) {
                creditRecord.setId(recordId);
                creditRecord.setClient(client);
                ClientCreditRecord updatedRecord = clientCreditRecordService.updateClientCreditRecord(creditRecord);
                return ResponseEntity.ok(updatedRecord);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
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

