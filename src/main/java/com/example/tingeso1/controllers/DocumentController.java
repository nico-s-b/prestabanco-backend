package com.example.tingeso1.controllers;

import java.util.List;

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

    @PostMapping("/")
    public ResponseEntity<DocumentEntity> saveDocument(@RequestBody DocumentEntity document) {
        DocumentEntity documentNew = documentService.saveDocument(document);
        return ResponseEntity.ok(documentNew);
    }

    @PutMapping("/")
    public ResponseEntity<DocumentEntity> updateDocument(@RequestBody DocumentEntity document) {
        DocumentEntity documentUpdated = documentService.updateDocument(document);
        return ResponseEntity.ok(documentUpdated);
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

