package com.example.tingeso1.services;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.Executive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.repositories.ExecutiveRepository;

import java.util.List;

@Service
public class ExecutiveService {

    @Autowired
    ExecutiveRepository executiveRepository;

    public List<Executive> getAllExecutives() {
        return executiveRepository.findAll();
    }

    public void assignCredit(Executive executive, Credit credit) {
        credit.setExecutive(executive);
        executive.getCredits().add(credit);
        executiveRepository.save(executive);
    }

    public void deassignCredit(Executive executive, Credit credit) {
        credit.setExecutive(null);
        executive.getCredits().remove(credit);
        executiveRepository.save(executive);
    }
}
