package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ClientService;
import com.example.tingeso1.entities.Client;

@RestController
@RequestMapping("api/v1/clients")
@CrossOrigin("*")
public class ClientController {
    @Autowired
    ClientService clientService;

}

