package com.example.tingeso1.controllers;

import java.util.List;

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

    @PostMapping("/")
    public ResponseEntity<ClientCreditRecord> saveClientCreditRecord(@RequestBody ClientCreditRecord clientCreditRecord) {
        ClientCreditRecord clientCreditRecordNew = clientCreditRecordService.saveClientCreditRecord(clientCreditRecord);
        return ResponseEntity.ok(clientCreditRecordNew);
    }

    @PutMapping("/")
    public ResponseEntity<ClientCreditRecord> updateClientCreditRecord(@RequestBody ClientCreditRecord clientCreditRecord) {
        ClientCreditRecord clientCreditRecordUpdated = clientCreditRecordService.updateClientCreditRecord(clientCreditRecord);
        return ResponseEntity.ok(clientCreditRecordUpdated);
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

