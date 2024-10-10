package com.example.tingeso1.services;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.repositories.CreditRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.OngoingStubbing;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CreditServiceTest {
    @InjectMocks
    CreditService creditService = new CreditService();
    Credit credit = new Credit();

    @Mock
    private CreditRepository creditRepository;

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
