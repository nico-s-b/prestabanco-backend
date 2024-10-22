package com.example.tingeso1.controllers;

import com.example.tingeso1.utils.LoginRequest;
import com.example.tingeso1.entities.User;
import com.example.tingeso1.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author nic_s
 */
@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        //System.out.println("Email: " + loginRequest.getEmail());
        //System.out.println("Password: " + loginRequest.getPassword());        
        User usuario = userService.authenticate(loginRequest.getEmail(), loginRequest.getPassword());

        if (usuario != null) {
            session.setAttribute("usuario", usuario); 
            return new ResponseEntity<>("Login exitoso", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Credenciales inválidas", HttpStatus.UNAUTHORIZED);
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();  
        return new ResponseEntity<>("Logout exitoso", HttpStatus.OK);
    }    
}
