package com.example.tingeso1.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.tingeso1.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.repositories.ClientEmploymentRecordRepository;

@Service
public class ClientEmploymentRecordService {

    @Autowired
    ClientEmploymentRecordRepository clientEmploymentRecordRepository;

    public ArrayList<ClientEmploymentRecord> getClientEmploymentRecords(){
        return (ArrayList<ClientEmploymentRecord>) clientEmploymentRecordRepository.findAll();
    }

    public ClientEmploymentRecord saveClientEmploymentRecord(ClientEmploymentRecord clientEmploymentRecord){
        return clientEmploymentRecordRepository.save(clientEmploymentRecord);
    }

    public ClientEmploymentRecord getClientEmploymentRecordById(Long id){
        return clientEmploymentRecordRepository.findById(id).get();
    }

    public ClientEmploymentRecord updateClientEmploymentRecord(ClientEmploymentRecord clientEmploymentRecord) {
        return clientEmploymentRecordRepository.save(clientEmploymentRecord);
    }

    public boolean deleteClientEmploymentRecord(Long id) throws Exception {
        try{
            clientEmploymentRecordRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //Calcula años de servicio de un empleado a partir de fecha de inicio de su trabajo actual
    public boolean hasEnoughYearsOfService(ClientEmploymentRecord employmentRecord){
        ZonedDateTime start = employmentRecord.getCurrentWorkStartDate();
        int yearsOfService = MiscUtils.getYearsUntilNow(start);
        if (yearsOfService == 0){
            return false;
        }else {
            return true;
        }
    }

}
