package com.example.tingeso1.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.repositories.CreditRepository;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;

    public ArrayList<Credit> getCredits(){
        return (ArrayList<Credit>) creditRepository.findAll();
    }

    public Credit saveCredit(Credit credit){
        return creditRepository.save(credit);
    }

    public Credit getCreditById(Long id){
        return creditRepository.findById(id).get();
    }

    public Credit updateCredit(Credit credit) {
        return creditRepository.save(credit);
    }

    public boolean deleteCredit(Long id) throws Exception {
        try{
            creditRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    //Método para obtener la cuota mensual de un crédito dado sus parámetros
    public int getCreditInstallment(Credit credit){
        int n = credit.getLoanPeriod()*12;
        double annualRate = credit.getAnnualRate();
        double rate = annualRate / 12 / 100;
        double compoundInterest = Math.pow(1 + rate, n);
        int capital = credit.getCreditMount();
        return (int) (capital*(rate*compoundInterest)/(compoundInterest - 1));
    }
}
