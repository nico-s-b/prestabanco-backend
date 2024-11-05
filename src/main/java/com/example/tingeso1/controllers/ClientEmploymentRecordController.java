package com.example.tingeso1.controllers;

import java.util.List;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientEmploymentRecordService;
import com.example.tingeso1.entities.ClientEmploymentRecord;

@RestController
@RequestMapping("api/v1/employmentrecords")
@CrossOrigin(origins = "*")
public class ClientEmploymentRecordController {
    @Autowired
    ClientEmploymentRecordService clientEmploymentRecordService;

    @Autowired
    ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<ClientEmploymentRecord>> listClientEmploymentRecords() {
        List<ClientEmploymentRecord> clientEmploymentRecords = clientEmploymentRecordService.getClientEmploymentRecords();
        return ResponseEntity.ok(clientEmploymentRecords);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEmploymentRecord> getClientEmploymentRecordById(@PathVariable Long id) {
        ClientEmploymentRecord clientEmploymentRecord = clientEmploymentRecordService.getClientEmploymentRecordById(id);
        if (clientEmploymentRecord != null) {
            return ResponseEntity.ok(clientEmploymentRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{clientId}")
    public ResponseEntity<ClientEmploymentRecord> createOrUpdateEmploymentRecord(
            @PathVariable Long clientId,
            @RequestBody ClientEmploymentRecord employmentRecord) {

        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }

        ClientEmploymentRecord existingRecord = clientEmploymentRecordService.getClientEmploymentRecordById(clientId);
        if (existingRecord != null) {
            employmentRecord.setId(existingRecord.getId());
        }

        employmentRecord.setClient(client);
        ClientEmploymentRecord savedRecord = clientEmploymentRecordService.saveClientEmploymentRecord(employmentRecord);
        return ResponseEntity.ok(savedRecord);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClientEmploymentRecordById(@PathVariable Long id) throws Exception {
        var isDeleted = clientEmploymentRecordService.deleteClientEmploymentRecord(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}