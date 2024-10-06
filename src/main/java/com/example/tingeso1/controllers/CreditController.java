package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.services.CreditService;

@RestController
@RequestMapping("api/v1/credits")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;

    @GetMapping("/")
    public ResponseEntity<List<Credit>> listCredits() {
        List<Credit> credits = creditService.getCredits();
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credit> getCreditById(@PathVariable Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Credit> saveCredit(@RequestBody Credit credit) {
        Credit creditNew = creditService.saveCredit(credit);
        return ResponseEntity.ok(creditNew);
    }

    @PutMapping("/")
    public ResponseEntity<Credit> updateCredit(@RequestBody Credit credit) {
        Credit creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditById(@PathVariable Long id) throws Exception {
        var isDeleted = creditService.deleteCredit(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
