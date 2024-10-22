package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.User;
import com.example.tingeso1.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public ArrayList<User> getUsers(){
        return (ArrayList<User>) userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }

    public User getUserByRut(String rut){
        return userRepository.findByRut(rut);
    }
    
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public boolean deleteUser(Long id) throws Exception {
        try{
            userRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public User authenticate(String email, String password){
        User usuario = userRepository.findByEmail(email);
        if (usuario != null){
            if (usuario.getPass().equals(password)){
                return usuario;
            }
        }
        return null;
    }

}
