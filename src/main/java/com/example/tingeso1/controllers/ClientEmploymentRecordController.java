package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientEmploymentRecordService;
import com.example.tingeso1.entities.ClientEmploymentRecord;

@RestController
@RequestMapping("api/v1/employmentrecords")
@CrossOrigin("*")
public class ClientEmploymentRecordController {
    @Autowired
    ClientEmploymentRecordService clientEmploymentRecordService;

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

    @PostMapping("/")
    public ResponseEntity<ClientEmploymentRecord> saveClientEmploymentRecord(@RequestBody ClientEmploymentRecord clientEmploymentRecord) {
        ClientEmploymentRecord clientEmploymentRecordNew = clientEmploymentRecordService.saveClientEmploymentRecord(clientEmploymentRecord);
        return ResponseEntity.ok(clientEmploymentRecordNew);
    }

    @PutMapping("/")
    public ResponseEntity<ClientEmploymentRecord> updateClientEmploymentRecord(@RequestBody ClientEmploymentRecord clientEmploymentRecord) {
        ClientEmploymentRecord clientEmploymentRecordUpdated = clientEmploymentRecordService.updateClientEmploymentRecord(clientEmploymentRecord);
        return ResponseEntity.ok(clientEmploymentRecordUpdated);
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