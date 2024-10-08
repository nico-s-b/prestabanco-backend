package com.example.tingeso1.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.utils.MiscUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.repositories.ClientEmploymentRecordRepository;

@Service
public class ClientEmploymentRecordService {

    @Autowired
    ClientEmploymentRecordRepository clientEmploymentRecordRepository;

    @Autowired
    CreditService creditService;

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

    public boolean hasEnoughIncomeInstallmentRate(ClientEmploymentRecord employmentRecord, Credit credit){
        int monthlyInstallment = creditService.getCreditInstallment(credit);
        float rate;
        if (employmentRecord.getIsEmployee()){
            rate = ((float) monthlyInstallment / employmentRecord.getMonthlyIncome())*100;
        }else{
            // Considerar ingreso mensual de últimos 2 años para trabajador independiente
            rate = ((float) monthlyInstallment / ((float) employmentRecord.getLastTwoYearIncome() / 24) )*100;
        }
        //Aprobar si relación no supera el 35% (o sea, es 35% o menor)
        return rate < 36;
    }

}
