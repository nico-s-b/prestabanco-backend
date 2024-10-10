package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.repositories.ClientEmploymentRecordRepository;
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
import static org.mockito.Mockito.when;

public class ClientEmploymentRecordServiceTest {
    @InjectMocks
    ClientEmploymentRecordService employmentRecordService = new ClientEmploymentRecordService();
    ClientEmploymentRecord employmentRecord = new ClientEmploymentRecord();

    @Mock
    private ClientEmploymentRecordRepository clientEmploymentRecordRepository;

    @Mock
    private CreditService creditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientEmploymentRecords() {
        //Given
        ClientEmploymentRecord record1 = new ClientEmploymentRecord();
        ClientEmploymentRecord record2 = new ClientEmploymentRecord();
        ClientEmploymentRecord record3 = new ClientEmploymentRecord();
        List<ClientEmploymentRecord> mockRecords = new ArrayList<>(Arrays.asList(record1, record2, record3));

        when(clientEmploymentRecordRepository.findAll()).thenReturn(mockRecords);

        //When
        ArrayList<ClientEmploymentRecord> result = employmentRecordService.getClientEmploymentRecords();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockRecords, result);

        verify(clientEmploymentRecordRepository, times(1)).findAll();
    }

    @Test
    void testSaveClientEmploymentRecord() {
        //Given
        ClientEmploymentRecord record = new ClientEmploymentRecord();

        when(clientEmploymentRecordRepository.save(record)).thenReturn(record);

        //When
        ClientEmploymentRecord result = employmentRecordService.saveClientEmploymentRecord(record);

        //Then
        assertNotNull(result);
        assertEquals(record, result);
        verify(clientEmploymentRecordRepository, times(1)).save(record);
    }

    @Test
    void testGetClientEmploymentRecordById() {
        //When
        ClientEmploymentRecord record = new ClientEmploymentRecord();
        Long id = 1L;
        record.setId(id);

        when(clientEmploymentRecordRepository.findById(id)).thenReturn(Optional.of(record));

        ClientEmploymentRecord result = employmentRecordService.getClientEmploymentRecordById(id);

        assertNotNull(result);
        assertEquals(record, result);
        verify(clientEmploymentRecordRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateClientEmploymentRecord() {
        //Given
        ClientEmploymentRecord record = new ClientEmploymentRecord();
        record.setId(1L);

        when(clientEmploymentRecordRepository.save(record)).thenReturn(record);

        //When
        ClientEmploymentRecord result = employmentRecordService.updateClientEmploymentRecord(record);

        //Then
        assertNotNull(result);
        assertEquals(record, result);
        verify(clientEmploymentRecordRepository, times(1)).save(record);
    }

    @Test
    void testDeleteClientEmploymentRecord_Success()  throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(clientEmploymentRecordRepository).deleteById(id);

        //When
        boolean result = employmentRecordService.deleteClientEmploymentRecord(id);

        //Then
        assertTrue(result);
        verify(clientEmploymentRecordRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteClientEmploymentRecord_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(clientEmploymentRecordRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            employmentRecordService.deleteClientEmploymentRecord(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(clientEmploymentRecordRepository, times(1)).deleteById(id);
    }


    @Test
    void whenCurrentWorkStartDateMoreThanOneYearAgo_thenHasEnoughYears() {
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1).minusDays(1);
        employmentRecord.setCurrentWorkStartDate(oneYearAgo);
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());

        //When
        boolean hasMoreThan1Year = employmentRecordService.hasEnoughYearsOfService(employmentRecord, credit);

        //Then
        assertThat(hasMoreThan1Year).isEqualTo(true);
    }

    @Test
    void whenCurrentWorkStartDateLessThanOneYearAgo_thenDontHasEnoughYears() {
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1).plusDays(1);
        employmentRecord.setCurrentWorkStartDate(oneYearAgo);
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());

        //When
        boolean hasMoreThan1Year = employmentRecordService.hasEnoughYearsOfService(employmentRecord, credit);

        //Then
        assertThat(hasMoreThan1Year).isEqualTo(false);
    }

    @Test
    void testGetClientMonthlyIncome_WhenClientIsEmployee () {
        //Given
        ClientEmploymentRecord record = new ClientEmploymentRecord();
        record.setIsEmployee(true);
        record.setMonthlyIncome(280000);
        record.setLastTwoYearIncome(0);

        //when
        int result = employmentRecordService.getClientMonthlyIncome(record);

        //Then
        assertThat(result).isEqualTo(280000);
    }

    @Test
    void testGetClientMonthlyIncome_WhenClientIsNotEmployee () {
        //Given
        ClientEmploymentRecord record = new ClientEmploymentRecord();
        record.setIsEmployee(false);
        record.setMonthlyIncome(0);
        record.setLastTwoYearIncome(2400000);

        //when
        int result = employmentRecordService.getClientMonthlyIncome(record);

        //Then
        assertThat(result).isEqualTo(100000);
    }

    @Test
    void testHasEnoughIncomeInstallmentRate_EmployeeSuccess() {
        //Given
        Credit credit = new Credit();
        ClientEmploymentRecord clientEmploymentRecord = new ClientEmploymentRecord();
        clientEmploymentRecord.setIsEmployee(true);
        clientEmploymentRecord.setMonthlyIncome(280000);
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);

        //When
        boolean result = employmentRecordService.hasEnoughIncomeInstallmentRate(clientEmploymentRecord, credit);

        //Then
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testHasEnoughIncomeInstallmentRate_EmployeeFailure() {
        //Given
        Credit credit = new Credit();
        ClientEmploymentRecord clientEmploymentRecord = new ClientEmploymentRecord();
        clientEmploymentRecord.setIsEmployee(true);
        clientEmploymentRecord.setMonthlyIncome(275000);
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);

        //When
        boolean result = employmentRecordService.hasEnoughIncomeInstallmentRate(clientEmploymentRecord, credit);

        //Then
        assertThat(result).isEqualTo(false);
    }

    @Test
    void testHasEnoughIncomeInstallmentRate_IndependentSuccess() {
        //Given
        Credit credit = new Credit();
        ClientEmploymentRecord clientEmploymentRecord = new ClientEmploymentRecord();
        clientEmploymentRecord.setIsEmployee(false);
        clientEmploymentRecord.setLastTwoYearIncome(280000*24);
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);

        //When
        boolean result = employmentRecordService.hasEnoughIncomeInstallmentRate(clientEmploymentRecord, credit);

        //Then
        assertThat(result).isEqualTo(true);
    }

    @Test
    void testHasEnoughIncomeInstallmentRate_IndependentFailure() {
        //Given
        Credit credit = new Credit();
        ClientEmploymentRecord clientEmploymentRecord = new ClientEmploymentRecord();
        clientEmploymentRecord.setIsEmployee(false);
        clientEmploymentRecord.setLastTwoYearIncome(275000*24);
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);

        //When
        boolean result = employmentRecordService.hasEnoughIncomeInstallmentRate(clientEmploymentRecord, credit);

        //Then
        assertThat(result).isEqualTo(false);
    }

}
