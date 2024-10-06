package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientEmploymentRecord;
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

    @InjectMocks
    private ClientEmploymentRecordService clientEmploymentRecordService;

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
        List<ClientEmploymentRecord> mockRecords = new ArrayList<>(Arrays.asList(record1, record2, record3)); // Convierte a ArrayList

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
    void whenCurrentWorkStartDateMoreThanOneYearAgo_thenHasEnoughYears() {
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1).minusDays(1);
        employmentRecord.setCurrentWorkStartDate(oneYearAgo);

        //When
        boolean hasMoreThan1Year = employmentRecordService.hasEnoughYearsOfService(employmentRecord);

        //Then
        assertThat(hasMoreThan1Year).isEqualTo(true);
    }

    @Test
    void whenCurrentWorkStartDateLessThanOneYearAgo_thenDontHasEnoughYears() {
        //Given
        ZonedDateTime oneYearAgo = ZonedDateTime.now().minusYears(1).plusDays(1);
        employmentRecord.setCurrentWorkStartDate(oneYearAgo);

        //When
        boolean hasMoreThan1Year = employmentRecordService.hasEnoughYearsOfService(employmentRecord);

        //Then
        assertThat(hasMoreThan1Year).isEqualTo(false);
    }


}
