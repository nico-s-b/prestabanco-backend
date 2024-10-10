package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientCreditRecord;
import com.example.tingeso1.entities.ClientEmploymentRecord;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.repositories.ClientCreditRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class ClientCreditRecordServiceTest {

    @InjectMocks
    ClientCreditRecordService clientCreditRecordService = new ClientCreditRecordService();

    @Mock
    private ClientCreditRecordRepository clientCreditRecordRepository;

    @Mock
    private CreditService creditService;

    @Mock
    private ClientEmploymentRecordService clientEmploymentRecordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientCreditRecords() {
        //Given
        ClientCreditRecord record1 = new ClientCreditRecord();
        ClientCreditRecord record2 = new ClientCreditRecord();
        ClientCreditRecord record3 = new ClientCreditRecord();
        List<ClientCreditRecord> mockRecords = new ArrayList<>(Arrays.asList(record1, record2, record3));

        when(clientCreditRecordRepository.findAll()).thenReturn(mockRecords);

        //When
        ArrayList<ClientCreditRecord> result = clientCreditRecordService.getClientCreditRecords();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockRecords, result);

        verify(clientCreditRecordRepository, times(1)).findAll();
    }

    @Test
    void testSaveClientCreditRecord() {
        //Given
        ClientCreditRecord record = new ClientCreditRecord();

        when(clientCreditRecordRepository.save(record)).thenReturn(record);

        //When
        ClientCreditRecord result = clientCreditRecordService.saveClientCreditRecord(record);

        //Then
        assertNotNull(result);
        assertEquals(record, result);
        verify(clientCreditRecordRepository, times(1)).save(record);
    }

    @Test
    void testGetClientCreditRecordById() {
        //Given
        ClientCreditRecord record = new ClientCreditRecord();
        Long id = 1L;
        record.setId(id);

        when(clientCreditRecordRepository.findById(id)).thenReturn(Optional.of(record));

        //When
        ClientCreditRecord result = clientCreditRecordService.getClientCreditRecordById(id);

        //Then
        assertNotNull(result);
        assertEquals(record, result);
        verify(clientCreditRecordRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateClientCreditRecord() {
        //Given
        ClientCreditRecord record = new ClientCreditRecord();
        record.setId(1L);

        when(clientCreditRecordRepository.save(record)).thenReturn(record);

        //When
        ClientCreditRecord result = clientCreditRecordService.updateClientCreditRecord(record);

        //Then
        assertNotNull(result);
        assertEquals(record, result);
        verify(clientCreditRecordRepository, times(1)).save(record);
    }

    @Test
    void testDeleteClientCreditRecord_Success() throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(clientCreditRecordRepository).deleteById(id);

        //When
        boolean result = clientCreditRecordService.deleteClientCreditRecord(id);

        //Then
        assertTrue(result);
        verify(clientCreditRecordRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteClientCreditRecord_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(clientCreditRecordRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            clientCreditRecordService.deleteClientCreditRecord(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(clientCreditRecordRepository, times(1)).deleteById(id);
    }

    @Test
    void testHasGoodDebtIncomeRate_Success() {
        //Given
        ClientCreditRecord record = new ClientCreditRecord();
        record.setDebtAmount(100000);
        ClientEmploymentRecord employmentRecord = new ClientEmploymentRecord();
        int income = 400000;
        Credit credit = new Credit();
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);
        when(clientEmploymentRecordService.getClientMonthlyIncome(any(ClientEmploymentRecord.class))).thenAnswer(invocation -> income);

        //When
        boolean approved = clientCreditRecordService.hasGoodDebtIncomeRate(record, employmentRecord, credit);

        //Then
        assertTrue(approved);
    }

    @Test
    void testHasGoodDebtIncomeRate_Failure() {
        //Given
        ClientCreditRecord record = new ClientCreditRecord();
        record.setDebtAmount(100000);
        ClientEmploymentRecord employmentRecord = new ClientEmploymentRecord();
        int income = 390000;
        Credit credit = new Credit();
        int expectedInstallment = 100000;

        when(creditService.getCreditInstallment(any(Credit.class))).thenAnswer(invocation -> expectedInstallment);
        when(clientEmploymentRecordService.getClientMonthlyIncome(any(ClientEmploymentRecord.class))).thenAnswer(invocation -> income);

        //When
        boolean approved = clientCreditRecordService.hasGoodDebtIncomeRate(record, employmentRecord, credit);

        //Then
        assertFalse(approved);
    }
}

