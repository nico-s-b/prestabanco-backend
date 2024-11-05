package com.example.tingeso1.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.entities.User;
import com.example.tingeso1.services.UserService;

@RestController
@RequestMapping("api/v1/users")
@CrossOrigin(origins = "http://prestabanco.brazilsouth.cloudapp.azure.com:8070")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable Long id) throws Exception {
        var isDeleted = userService.deleteUser(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
