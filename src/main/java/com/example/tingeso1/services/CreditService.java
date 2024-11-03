package com.example.tingeso1.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.enums.DocumentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.repositories.CreditRepository;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;

    @Autowired
    DocumentService documentService;

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

    public void setMaxAnnualRate(Credit credit){
        if (credit.getCreditType() == CreditType.FIRSTHOME){
            credit.setAnnualRate(5);
        }
        else if (credit.getCreditType() == CreditType.SECONDHOME
                || credit.getCreditType() == CreditType.REMODELING){
            credit.setAnnualRate(6);
        }
        else if (credit.getCreditType() == CreditType.COMERCIAL) {
            credit.setAnnualRate(7);
        }
    }

    public void setMinAnnualRate(Credit credit){
        if (credit.getCreditType() == CreditType.FIRSTHOME){
            credit.setAnnualRate(3.5F);
        }
        else if (credit.getCreditType() == CreditType.SECONDHOME) {
            credit.setAnnualRate(4);
        }
        else if (credit.getCreditType() == CreditType.COMERCIAL) {
            credit.setAnnualRate(5);
        }
        else if (credit.getCreditType() == CreditType.REMODELING) {
            credit.setAnnualRate(4.5F);
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

    public boolean verifyMaxFinancingMount(Credit credit){
        if (credit.getCreditType() == null) {
            throw new IllegalStateException("CreditType cannot be null");
        }

        int creditMount = credit.getCreditMount();
        int propertyValue = credit.getPropertyValue();
        int financingPercentage = (creditMount / propertyValue)*100;

        switch (credit.getCreditType()){
            case FIRSTHOME -> {
                if (financingPercentage < 80) return true;
            }
            case SECONDHOME -> {
                if (financingPercentage < 70) return true;
            }
            case COMERCIAL -> {
                if (financingPercentage < 60) return true;
            }
            case REMODELING -> {
                if (financingPercentage < 50) return true;
            }
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditType());
        }
        return false;
    }

    public int getMaxFinancingMount(Credit credit) {
        double percentage = switch (credit.getCreditType()) {
            case FIRSTHOME -> 0.8;
            case SECONDHOME -> 0.7;
            case COMERCIAL -> 0.6;
            case REMODELING -> 0.5;
        };
        return (int) (credit.getPropertyValue() * percentage);
    }

    public int getMaxLoanPeriod(Credit credit) {
        return switch (credit.getCreditType()) {
            case FIRSTHOME -> 30;
            case SECONDHOME -> 20;
            case COMERCIAL -> 25;
            case REMODELING -> 15;
        };
    }

    public boolean verifyCreditRequest(Credit credit) {
        if (credit.getCreditType() == null) {
            throw new IllegalStateException("CreditType cannot be null");
        }
        int loanPeriod = credit.getLoanPeriod();
        if (loanPeriod > getMaxLoanPeriod(credit)) {
            return false;
        }
        int creditMount = credit.getCreditMount();
        if (creditMount > getMaxLoanPeriod(credit)) {
            return false;
        }
        ArrayList<DocumentType> docs = documentService.whichMissingDocuments(credit);
        if (!docs.isEmpty()) {
            return false;
        }
        return true;
    }

    //R5 Monto máximo de financiamiento
    public boolean isCreditAmountLessThanMaxAmount(Credit credit){
        if (credit.getCreditType() == null) {
            throw new IllegalStateException("CreditType cannot be null");
        }

        switch (credit.getCreditType()){
            case FIRSTHOME -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.8;
            }
            case SECONDHOME -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.7;
            }
            case COMERCIAL -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.6;
            }
            case REMODELING -> {
                return credit.getCreditMount() <= credit.getPropertyValue()*0.5;
            }
            default -> throw new IllegalStateException("Unexpected value: " + credit.getCreditType());
        }
    }

    //R6 Edad del solicitante
    public boolean isClientAgeAllowed(Credit credit, Client client){
        ZonedDateTime endOfPaymentDate = credit.getRequestDate().plusYears((long) credit.getLoanPeriod());
        int clientAgeAtEndOfPayment = (int) client.getBirthDate().until(endOfPaymentDate, ChronoUnit.YEARS);
        return clientAgeAtEndOfPayment < 70;
    }
}
