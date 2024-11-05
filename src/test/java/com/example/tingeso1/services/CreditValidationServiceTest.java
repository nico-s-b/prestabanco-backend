package com.example.tingeso1.services;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.repositories.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreditValidationServiceTest {
    @InjectMocks
    CreditValidationService validationService = new CreditValidationService();
    Credit credit = new Credit();

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private DocumentService documentService;

    @Mock
    CreditService creditService = new CreditService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyMaxFinancingMount() {
        //Given
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();
        Credit credit3 = new Credit();
        Credit credit4 = new Credit();

        credit1.setCreditMount(80);
        credit2.setCreditMount(70);
        credit3.setCreditMount(60);
        credit4.setCreditMount(50);

        credit1.setCreditType(CreditType.FIRSTHOME);
        credit2.setCreditType(CreditType.SECONDHOME);
        credit3.setCreditType(CreditType.COMERCIAL);
        credit4.setCreditType(CreditType.REMODELING);

        credit1.setPropertyValue(100);
        credit2.setPropertyValue(100);
        credit3.setPropertyValue(100);
        credit4.setPropertyValue(100);

        //When
        boolean result1 = validationService.verifyMaxFinancingMount(credit1);
        boolean result2 = validationService.verifyMaxFinancingMount(credit2);
        boolean result3 = validationService.verifyMaxFinancingMount(credit3);
        boolean result4 = validationService.verifyMaxFinancingMount(credit4);

        //Then
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    @Test
    public void testVerify_defaultCases() {
        Credit mockCredit = mock(Credit.class);

        when(mockCredit.getCreditType()).thenReturn(null);

        CreditValidationService validationService = new CreditValidationService();

        assertThrows(IllegalStateException.class, () -> {
            validationService.isCreditAmountLessThanMaxAmount(mockCredit);
        });
        assertThrows(IllegalStateException.class, () -> {
            validationService.verifyMaxFinancingMount(mockCredit);
        });
        assertThrows(IllegalStateException.class, () -> {
            validationService.verifyCreditRequest(mockCredit);
        });
    }

    @Test
    void testVerifyCreditRequest_FIRSTHOME_fail() {
        //Given
        Credit credit = new Credit();
        credit.setLoanPeriod(40);
        credit.setPropertyValue(1000);
        credit.setCreditMount(490);
        credit.setCreditType(CreditType.FIRSTHOME);

        when(documentService.whichMissingDocuments(credit)).thenReturn(new ArrayList<>());

        //When
        boolean result = validationService.verifyCreditRequest(credit);

        assertFalse(result);
    }

    @Test
    void testVerifyCreditRequest_SECONDHOME() {
        //Given
        Credit credit = new Credit();
        credit.setLoanPeriod(10);
        credit.setPropertyValue(1000);
        credit.setCreditMount(490);
        credit.setCreditType(CreditType.SECONDHOME);

        when(documentService.whichMissingDocuments(credit)).thenReturn(new ArrayList<>());

        //When
        boolean result = validationService.verifyCreditRequest(credit);

        assertTrue(result);
    }

    @Test
    void testVerifyCreditRequest_COMERCIAL() {
        //Given
        Credit credit = new Credit();
        credit.setLoanPeriod(10);
        credit.setPropertyValue(1000);
        credit.setCreditMount(490);
        credit.setCreditType(CreditType.COMERCIAL);

        when(documentService.whichMissingDocuments(credit)).thenReturn(new ArrayList<>());

        //When
        boolean result = validationService.verifyCreditRequest(credit);

        //Then
        assertTrue(result);
    }

    @Test
    void testVerifyCreditRequest_REMODELING() {
        //Given
        Credit credit = new Credit();
        credit.setLoanPeriod(10);
        credit.setPropertyValue(1000);
        credit.setCreditMount(490);
        credit.setCreditType(CreditType.REMODELING);

        when(documentService.whichMissingDocuments(credit)).thenReturn(new ArrayList<>());

        //When
        boolean result = validationService.verifyCreditRequest(credit);

        assertTrue(result);
    }




    @Test
    void TestIsCreditAmountLessThanMaxAmount_FIRSTHOME_OK() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.FIRSTHOME);
        credit.setCreditMount(800);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertTrue(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_FIRSTHOME_FAIL() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.FIRSTHOME);
        credit.setCreditMount(801);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertFalse(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_SECONDHOME_OK() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.SECONDHOME);
        credit.setCreditMount(700);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertTrue(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_SECONDHOME_FAIL() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.SECONDHOME);
        credit.setCreditMount(701);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertFalse(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_COMERCIAL_OK() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.COMERCIAL);
        credit.setCreditMount(600);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertTrue(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_COMERCIAL_FAIL() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.COMERCIAL);
        credit.setCreditMount(601);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertFalse(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_REMODELING_OK() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.REMODELING);
        credit.setCreditMount(500);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertTrue(result);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_REMODELING_FAIL() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.REMODELING);
        credit.setCreditMount(501);
        credit.setPropertyValue(1000);

        //When
        boolean result = validationService.isCreditAmountLessThanMaxAmount(credit);

        assertFalse(result);
    }

    @Test
    void testIsClientAgeAllowed_Success() {
        //Given
        ZonedDateTime mockRequestDate = ZonedDateTime.parse("2000-01-01T00:00:00.000+01:01[America/Santiago]");
        Credit credit = new Credit();
        credit.setRequestDate(mockRequestDate);
        credit.setLoanPeriod(20);
        Client client = new Client();
        client.setBirthDate(mockRequestDate.minusYears(50).plusDays(1));

        //When
        boolean result = validationService.isClientAgeAllowed(credit, client);

        //Then
        assertTrue(result);
    }

    @Test
    void testIsClientAgeAllowed_Failure() {
        //Given
        ZonedDateTime mockRequestDate = ZonedDateTime.parse("2000-01-01T00:00:00.000+01:01[America/Santiago]");
        Credit credit = new Credit();
        credit.setRequestDate(mockRequestDate);
        credit.setLoanPeriod(20);
        Client client = new Client();
        client.setBirthDate(mockRequestDate.minusYears(50));

        //When
        boolean result = validationService.isClientAgeAllowed(credit, client);

        //Then
        assertFalse(result);
    }

}
