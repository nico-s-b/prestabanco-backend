package com.example.tingeso1.services;

import java.util.ArrayList;

import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.entities.Credit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.ClientCreditRecord;
import com.example.tingeso1.repositories.ClientCreditRecordRepository;

@Service
public class ClientCreditRecordService {

    @Autowired
    ClientCreditRecordRepository clientCreditRecordRepository;
    @Autowired
    private CreditService creditService;
    @Autowired
    private ClientEmploymentRecordService employmentRecordService;

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

    //R4: Relación entre deuda e ingreso
    public boolean hasGoodDebtIncomeRate(ClientCreditRecord clientCreditRecord, ClientEmploymentRecord clientEmploymentRecord, Credit credit) {
        int totalProjectedDebt = clientCreditRecord.getDebtAmount() + creditService.getCreditInstallment(credit);
        int income = employmentRecordService.getClientMonthlyIncome(clientEmploymentRecord);
        //Rechazar si la suma de deudas (considerando cuota del crédito) es mayor a 50%
        return  ((float) totalProjectedDebt / income)*100 < 51;
    }
}
