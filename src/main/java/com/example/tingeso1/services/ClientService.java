package com.example.tingeso1.services;

import com.example.tingeso1.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.repositories.ClientRepository;
import java.util.ArrayList;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    

}
