package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.DocumentEntity;
import com.example.tingeso1.repositories.DocumentRepository;

@Service
public class DocumentService {

    @Autowired
    DocumentRepository documentRepository;

    public ArrayList<DocumentEntity> getDocuments(){
        return (ArrayList<DocumentEntity>) documentRepository.findAll();
    }

    public DocumentEntity saveDocument(DocumentEntity document){
        return documentRepository.save(document);
    }

    public DocumentEntity getDocumentById(Long id){
        return documentRepository.findById(id).get();
    }

    public DocumentEntity updateDocument(DocumentEntity document) {
        return documentRepository.save(document);
    }

    public boolean deleteDocument(Long id) throws Exception {
        try{
            documentRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }       
}
