package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.AccountMovementService;
import com.example.tingeso1.entities.AccountMovement;

@RestController
@RequestMapping("api/v1/movements")
@CrossOrigin("*")
public class AccountMovementController {
    @Autowired
    AccountMovementService accountMovementService;

    @GetMapping("/")
	public ResponseEntity<List<AccountMovement>> listAccountMovement() {
    	List<AccountMovement> accountMovement = accountMovementService.getAccountMovements();
		return ResponseEntity.ok(accountMovement);
	}

	@GetMapping("/{id}")
	public ResponseEntity<AccountMovement> getAccountMovementById(@PathVariable Long id) {
		AccountMovement accountMovement = accountMovementService.getAccountMovementById(id);
        if (accountMovement != null) {
            return ResponseEntity.ok(accountMovement);
        } else {
            return ResponseEntity.notFound().build();
        }	
    }

	@PostMapping("/")
	public ResponseEntity<AccountMovement> saveAccountMovement(@RequestBody AccountMovement accountMovement) {
		AccountMovement accountMovementNew = accountMovementService.saveAccountMovement(accountMovement);
		return ResponseEntity.ok(accountMovementNew);
	}

	@PutMapping("/")
	public ResponseEntity<AccountMovement> updateAccountMovement(@RequestBody AccountMovement accountMovement){
		AccountMovement accountMovementUpdated = accountMovementService.updateAccountMovement(accountMovement);
		return ResponseEntity.ok(accountMovementUpdated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteAccountMovementById(@PathVariable Long id) throws Exception {
		var isDeleted = accountMovementService.deleteAccountMovement(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
	}
}
