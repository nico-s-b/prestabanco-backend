package com.example.tingeso1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.repositories.ExecutiveRepository;

@Service
public class ExecutiveService {

    @Autowired
    ExecutiveRepository executiveRepository;
}
