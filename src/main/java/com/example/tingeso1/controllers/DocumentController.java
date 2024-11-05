package com.example.tingeso1.controllers;

import java.util.List;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.services.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.DocumentService;
import com.example.tingeso1.entities.DocumentEntity;

@RestController
@RequestMapping("api/v1/documents")
@CrossOrigin("*")
public class DocumentController {
    @Autowired
    DocumentService documentService;
    @Autowired
    private CreditService creditService;

    @GetMapping("/")
    public ResponseEntity<List<DocumentEntity>> listDocuments() {
        List<DocumentEntity> documents = documentService.getDocuments();
        return ResponseEntity.ok(documents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentEntity> getDocumentById(@PathVariable Long id) {
        DocumentEntity document = documentService.getDocumentById(id);
        if (document != null) {
            return ResponseEntity.ok(document);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/credit/{id}")
    public ResponseEntity<List<DocumentEntity>> getDocumentByCreditId(@PathVariable Long id) {
        List<DocumentEntity> documents = documentService.getDocumentsByCreditId(id);
        if (!documents.isEmpty()) {
            return ResponseEntity.ok(documents);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/")
    public ResponseEntity<DocumentEntity> saveDocument(@RequestBody DocumentEntity document, @RequestParam Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit == null) {
            return ResponseEntity.notFound().build();
        }
        document.setCredit(credit);
        credit.getDocuments().add(document);

        DocumentEntity savedDocument = documentService.saveDocument(document);
        creditService.saveCredit(credit);

        return ResponseEntity.ok(savedDocument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentById(@PathVariable Long id) throws Exception {
        var isDeleted = documentService.deleteDocument(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

