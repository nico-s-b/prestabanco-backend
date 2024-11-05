package com.example.tingeso1.services;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.DocumentEntity;
import com.example.tingeso1.enums.CreditState;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.enums.DocumentType;
import com.example.tingeso1.utils.CreditRequest;
import org.hibernate.sql.exec.ExecutionException;
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

    @Autowired
    ClientService clientService;

    public ArrayList<Credit> getCredits(){
        return (ArrayList<Credit>) creditRepository.findAll();
    }

    public Credit saveCredit(Credit credit){
        credit.setLastUpdateDate(ZonedDateTime.now());
        return creditRepository.save(credit);
    }

    public Credit cancelCredit(Credit credit){
        credit.setState(CreditState.CANCELLED);
        return creditRepository.save(credit);
    }

    public Credit getCreditById(Long id){
        Optional<Credit> optionalRecord = creditRepository.findById(id);
        return optionalRecord.orElseThrow(() -> new ExecutionException("DocumentEntity not found for this id :: " + id));
    }

    public ArrayList<Credit>  getCreditsById(Long id){
        return (ArrayList<Credit>) creditRepository.findAllByClientId(id);
    }

    public Credit createCredit(CreditRequest request, Client client) {
        Credit credit = buildCredit(request);
        credit.setClient(client);
        client.getCredits().add(credit);
        credit.setDocuments(new ArrayList<>());

        clientService.saveClient(client);
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

    public float getMinAnnualRate(Credit credit) {
        return switch (credit.getCreditType()) {
            case FIRSTHOME -> 3.5F;
            case SECONDHOME -> 4;
            case COMERCIAL -> 5;
            case REMODELING -> 4.5F;
        };
    }

    public float getMaxAnnualRate(Credit credit) {
        return switch (credit.getCreditType()) {
            case FIRSTHOME -> 5;
            case SECONDHOME -> 6;
            case COMERCIAL -> 7;
            case REMODELING -> 6;
        };
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

    //Método para obtener la cuota mensual de un crédito dado sus parámetros
    public int getCreditInstallment(Credit credit){
        int n = credit.getLoanPeriod()*12;
        double annualRate = credit.getAnnualRate();
        double rate = annualRate / 12 / 100;
        double compoundInterest = Math.pow(1 + rate, n);
        int capital = credit.getCreditMount();
        return (int) (capital*(rate*compoundInterest)/(compoundInterest - 1));
    }

    public List<Integer> simulateCreditInstallments(CreditRequest request) {
        Credit credit = buildCredit(request);

        credit.setAnnualRate(getMinAnnualRate(credit));
        int minInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(request.getAnnualRate());
        int requestedInstallment = getCreditInstallment(credit);

        credit.setAnnualRate(getMaxAnnualRate(credit));
        int maxInstallment = getCreditInstallment(credit);

        return Arrays.asList(minInstallment, requestedInstallment , maxInstallment);
    }

    public Credit buildCredit(CreditRequest request){
        Credit credit = new Credit();

        switch (request.getCreditType()) {
            case "FIRSTHOME" -> credit.setCreditType(CreditType.FIRSTHOME);
            case "SECONDHOME" -> credit.setCreditType(CreditType.SECONDHOME);
            case "COMERCIAL" -> credit.setCreditType(CreditType.COMERCIAL);
            case "REMODELING" -> credit.setCreditType(CreditType.REMODELING);
            default -> throw new IllegalArgumentException("Tipo de crédito no válido: " + request.getCreditType());
        }

        credit.setLoanPeriod(request.getLoanPeriod());
        credit.setCreditMount(request.getCreditMount());
        credit.setPropertyValue(request.getPropertyValue());
        credit.setAnnualRate(request.getAnnualRate());
        credit.setRequestDate(ZonedDateTime.now());
        credit.setLastUpdateDate(ZonedDateTime.now());
        credit.setState(CreditState.INITIALREV);
        return credit;
    }
}
