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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CreditServiceTest {
    @InjectMocks
    CreditService creditService = new CreditService();
    Credit credit = new Credit();

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private DocumentService documentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCredits() {
        //Given
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();
        Credit credit3 = new Credit();
        List<Credit> mockCredits = new ArrayList<>(Arrays.asList(credit1, credit2, credit3));

        when(creditRepository.findAll()).thenReturn(mockCredits);

        //When
        ArrayList<Credit> result = creditService.getCredits();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockCredits, result);

        verify(creditRepository, times(1)).findAll();
    }

    @Test
    void testSaveCredit() {
        //Given
        Credit credit = new Credit();

        when(creditRepository.save(credit)).thenReturn(credit);

        //When
        Credit result = creditService.saveCredit(credit);

        //Then
        assertNotNull(result);
        assertEquals(credit, result);
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void testGetCreditById() {
        //Given
        Credit credit = new Credit();
        Long id = 1L;
        credit.setId(id);

        when(creditRepository.findById(id)).thenReturn(Optional.of(credit));

        //When
        Credit result = creditService.getCreditById(id);

        //Then
        assertNotNull(result);
        assertEquals(credit, result);
        verify(creditRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateCredit() {
        //Given
        Credit credit = new Credit();
        credit.setId(1L);

        when(creditRepository.save(credit)).thenReturn(credit);

        //When
        Credit result = creditService.updateCredit(credit);

        //Then
        assertNotNull(result);
        assertEquals(credit, result);
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void testDeleteCredit_Success() throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(creditRepository).deleteById(id);

        //When
        boolean result = creditService.deleteCredit(id);

        //Then
        assertTrue(result);
        verify(creditRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteCredit_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(creditRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            creditService.deleteCredit(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(creditRepository, times(1)).deleteById(id);
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
        boolean result1 = creditService.verifyMaxFinancingMount(credit1);
        boolean result2 = creditService.verifyMaxFinancingMount(credit2);
        boolean result3 = creditService.verifyMaxFinancingMount(credit3);
        boolean result4 = creditService.verifyMaxFinancingMount(credit4);

        //Then
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    @Test
    public void testGetMaxFinancingMount() {
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
        int result1 = creditService.getMaxFinancingMount(credit1);
        int result2 = creditService.getMaxFinancingMount(credit2);
        int result3 = creditService.getMaxFinancingMount(credit3);
        int result4 = creditService.getMaxFinancingMount(credit4);

        //Then
        assertThat(result1).isEqualTo(80);
        assertThat(result2).isEqualTo(70);
        assertThat(result3).isEqualTo(60);
        assertThat(result4).isEqualTo(50);
    }

    @Test
    public void testGetMaxLoanPeriod() {
        //Given
        Credit credit1 = new Credit();
        Credit credit2 = new Credit();
        Credit credit3 = new Credit();
        Credit credit4 = new Credit();
        credit1.setCreditType(CreditType.FIRSTHOME);
        credit2.setCreditType(CreditType.SECONDHOME);
        credit3.setCreditType(CreditType.COMERCIAL);
        credit4.setCreditType(CreditType.REMODELING);

        //When
        int period1 = creditService.getMaxLoanPeriod(credit1);
        int period2 = creditService.getMaxLoanPeriod(credit2);
        int period3 = creditService.getMaxLoanPeriod(credit3);
        int period4 = creditService.getMaxLoanPeriod(credit4);

        //Then
        assertThat(period1).isEqualTo(30);
        assertThat(period2).isEqualTo(20);
        assertThat(period3).isEqualTo(25);
        assertThat(period4).isEqualTo(15);

    }

    @Test
    public void testVerify_defaultCases() {
        Credit mockCredit = mock(Credit.class);

        when(mockCredit.getCreditType()).thenReturn(null);

        CreditService creditService = new CreditService();

        assertThrows(IllegalStateException.class, () -> {
            creditService.isCreditAmountLessThanMaxAmount(mockCredit);
        });
        assertThrows(IllegalStateException.class, () -> {
            creditService.verifyMaxFinancingMount(mockCredit);
        });
        assertThrows(IllegalStateException.class, () -> {
            creditService.verifyCreditRequest(mockCredit);
        });
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
        boolean result = creditService.verifyCreditRequest(credit);

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
        boolean result = creditService.verifyCreditRequest(credit);

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
        boolean result = creditService.verifyCreditRequest(credit);

        assertTrue(result);
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
        boolean result = creditService.verifyCreditRequest(credit);

        assertFalse(result);
    }

    @Test
    void testGetCreditInstallment() {
        //Given
        Credit credit = new Credit();
        credit.setCreditMount(100000000);
        credit.setLoanPeriod(20);
        credit.setAnnualRate(4.5F);

        //When
        int montlhlyInstallment = creditService.getCreditInstallment(credit);

        //Then
        assertThat(montlhlyInstallment).isEqualTo(632649);
    }

    @Test
    void testSetMaxAnnualRate() {
        //Given
        Credit credit1 = new Credit();
        credit1.setCreditType(CreditType.FIRSTHOME);
        Credit credit2 = new Credit();
        credit2.setCreditType(CreditType.SECONDHOME);
        Credit credit3 = new Credit();
        credit3.setCreditType(CreditType.COMERCIAL);
        Credit credit4 = new Credit();
        credit4.setCreditType(CreditType.REMODELING);

        //When
        creditService.setMaxAnnualRate(credit1);
        creditService.setMaxAnnualRate(credit2);
        creditService.setMaxAnnualRate(credit3);
        creditService.setMaxAnnualRate(credit4);

        //Then
        assertThat(credit1.getAnnualRate()).isEqualTo(5);
        assertThat(credit2.getAnnualRate()).isEqualTo(6);
        assertThat(credit3.getAnnualRate()).isEqualTo(7);
        assertThat(credit4.getAnnualRate()).isEqualTo(6);
    }

    @Test
    void testSetMinAnnualRate() {
        //Given
        Credit credit1 = new Credit();
        credit1.setCreditType(CreditType.FIRSTHOME);
        Credit credit2 = new Credit();
        credit2.setCreditType(CreditType.SECONDHOME);
        Credit credit3 = new Credit();
        credit3.setCreditType(CreditType.COMERCIAL);
        Credit credit4 = new Credit();
        credit4.setCreditType(CreditType.REMODELING);

        //When
        creditService.setMinAnnualRate(credit1);
        creditService.setMinAnnualRate(credit2);
        creditService.setMinAnnualRate(credit3);
        creditService.setMinAnnualRate(credit4);

        //Then
        assertThat(credit1.getAnnualRate()).isEqualTo(3.5F);
        assertThat(credit2.getAnnualRate()).isEqualTo(4);
        assertThat(credit3.getAnnualRate()).isEqualTo(5);
        assertThat(credit4.getAnnualRate()).isEqualTo(4.5F);
    }

    @Test
    void TestIsCreditAmountLessThanMaxAmount_FIRSTHOME_OK() {
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.FIRSTHOME);
        credit.setCreditMount(800);
        credit.setPropertyValue(1000);

        //When
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isCreditAmountLessThanMaxAmount(credit);

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
        boolean result = creditService.isClientAgeAllowed(credit, client);

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
        boolean result = creditService.isClientAgeAllowed(credit, client);

        //Then
        assertFalse(result);
    }
}
