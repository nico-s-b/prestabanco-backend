package com.example.tingeso1.controllers;

import java.util.List;

import com.example.tingeso1.entities.Executive;
import com.example.tingeso1.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ExecutiveService;

@RestController
@RequestMapping("api/v1/executives")
@CrossOrigin(origins = "http://prestabanco.brazilsouth.cloudapp.azure.com:8070")
public class ExecutiveController {
    @Autowired
    ExecutiveService executiveService;

    @GetMapping("/")
    public ResponseEntity<List<Executive>> listExecutives() {
        List<Executive> executives = executiveService.getAllExecutives();
        return ResponseEntity.ok(executives);
    }

    @PostMapping("/")
    public ResponseEntity<Executive> registerExecutive(@RequestBody Executive executive) {
        Executive savedExecutive = executiveService.saveExecutive(executive);
        return ResponseEntity.ok(savedExecutive);
    }

    @PutMapping("/{executiveId}")
    public ResponseEntity<Executive> updateExecutive(@PathVariable Long executiveId, @RequestBody Executive executive) {
        executive.setId(executiveId);
        Executive executiveUpdated = executiveService.saveExecutive(executive);
        return ResponseEntity.ok(executiveUpdated);
    }

    @GetMapping("/{executiveId}")
    public ResponseEntity<Executive> getExecutiveById(@PathVariable Long executiveId) {
        Executive executive = executiveService.getExecutiveById(executiveId);
        if (executive != null) {
            return ResponseEntity.ok(executive);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}