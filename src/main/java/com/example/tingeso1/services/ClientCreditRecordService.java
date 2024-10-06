package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.ClientCreditRecord;
import com.example.tingeso1.repositories.ClientCreditRecordRepository;

@Service
public class ClientCreditRecordService {

    @Autowired
    ClientCreditRecordRepository clientCreditRecordRepository;

    public ArrayList<ClientCreditRecord> getClientCreditRecords(){
        return (ArrayList<ClientCreditRecord>) clientCreditRecordRepository.findAll();
    }

    public ClientCreditRecord saveClientCreditRecord(ClientCreditRecord clientCreditRecord){
        return clientCreditRecordRepository.save(clientCreditRecord);
    }

    public ClientCreditRecord getClientCreditRecordById(Long id){
        return clientCreditRecordRepository.findById(id).get();
    }

    public ClientCreditRecord updateClientCreditRecord(ClientCreditRecord clientCreditRecord) {
        return clientCreditRecordRepository.save(clientCreditRecord);
    }

    public boolean deleteClientCreditRecord(Long id) throws Exception {
        try{
            clientCreditRecordRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
