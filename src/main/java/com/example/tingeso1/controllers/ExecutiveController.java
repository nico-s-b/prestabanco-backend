package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.services.ExecutiveService;
import com.example.tingeso1.entities.Executive;

@RestController
@RequestMapping("api/v1/executives")
@CrossOrigin("*")
public class ExecutiveController {
    @Autowired
    ExecutiveService executiveServicexecutiveService;
}