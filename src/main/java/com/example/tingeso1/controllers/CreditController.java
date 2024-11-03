package com.example.tingeso1.controllers;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.enums.CreditState;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.services.ClientService;
import com.example.tingeso1.utils.CreditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.services.CreditService;

@RestController
@RequestMapping("api/v1/credits")
@CrossOrigin("*")
public class CreditController {
    @Autowired
    CreditService creditService;
    @Autowired
    private ClientService clientService;

    @GetMapping("/")
    public ResponseEntity<List<Credit>> listCredits() {
        List<Credit> credits = creditService.getCredits();
        return ResponseEntity.ok(credits);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Credit> getCreditById(@PathVariable Long id) {
        Credit credit = creditService.getCreditById(id);
        if (credit != null) {
            return ResponseEntity.ok(credit);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{creditId}")
    public ResponseEntity<Credit> updateCredit(@PathVariable Long creditId, @RequestBody Credit credit) {
        credit.setId(creditId);
        Credit creditUpdated = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditUpdated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreditById(@PathVariable Long id) throws Exception {
        var isDeleted = creditService.deleteCredit(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/request")
    public ResponseEntity<Credit> requestCredit(@RequestBody CreditRequest request, @RequestParam Long clientId) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Client client = clientService.getClientById(clientId);
        if (client == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Credit credit = buildCredit(request);
        credit.setRequestDate(ZonedDateTime.now());
        credit.setLastUpdateDate(ZonedDateTime.now());
        credit.setState(CreditState.INITIALREV);

        credit.setClient(client);
        client.getCredits().add(credit);
        clientService.saveClient(client);

        return ResponseEntity.ok(credit);
    }

    @PutMapping("/initialrev")
    public ResponseEntity<Credit> initialrev(@RequestBody Credit credit, @RequestParam Long clientId) {
        Client client = credit.getClient();


        credit.setLastUpdateDate(ZonedDateTime.now());
        Credit creditChecked = creditService.updateCredit(credit);
        return ResponseEntity.ok(creditChecked);
    }

    @PostMapping("/simulate")
    public ResponseEntity<List<Integer>> simulate(@RequestBody CreditRequest request) {
        if (request.getCreditType() == null) {
            return ResponseEntity.badRequest().body(null);
        }

        Credit credit = buildCredit(request);

        creditService.setMaxAnnualRate(credit);
        int maxInstallment = creditService.getCreditInstallment(credit);
        creditService.setMinAnnualRate(credit);
        int minInstallment = creditService.getCreditInstallment(credit);

        List<Integer> installments = Arrays.asList(minInstallment, maxInstallment);
        return ResponseEntity.ok(installments);
    }

    @GetMapping("/restrictions")
    public ResponseEntity<Map<String, Integer>> getRestrictions(
            @RequestParam String creditType,
            @RequestParam int propertyValue) {

        Credit credit = new Credit();
        credit.setCreditType(CreditType.valueOf(creditType));
        credit.setPropertyValue(propertyValue);

        Map<String, Integer> restrictions = new HashMap<>();
        restrictions.put("maxLoanPeriod", creditService.getMaxLoanPeriod(credit));
        restrictions.put("maxFinancingMount", creditService.getMaxFinancingMount(credit));

        return ResponseEntity.ok(restrictions);
    }

    Credit buildCredit(CreditRequest request){
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

        return credit;
    }

}
