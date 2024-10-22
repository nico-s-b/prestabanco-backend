package com.example.tingeso1.services;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.Executive;
import com.example.tingeso1.repositories.CreditRepository;
import com.example.tingeso1.repositories.ExecutiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ExecutiveServiceTest {

    @InjectMocks
    ExecutiveService executiveService = new ExecutiveService();

    @Mock
    private ExecutiveRepository executiveRepository;

    @Mock
    private CreditRepository creditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllExecutives() {
        //Given
        Executive exec1 = new Executive();
        Executive exec2 = new Executive();
        Executive exec3 = new Executive();
        List<Executive> mockUsers = new ArrayList<>(Arrays.asList(exec1, exec2, exec3));

        when(executiveRepository.findAll()).thenReturn(mockUsers);

        //When
        ArrayList<Executive> result = (ArrayList<Executive>) executiveService.getAllExecutives();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockUsers, result);

        verify(executiveRepository, times(1)).findAll();
    }

    @Test
    void testAssignCredit(){
        //Given
        Executive executive = new Executive();
        Credit credit = new Credit();

        //When
        executiveService.assignCredit(executive, credit);

        //Then
        assertThat(executive.getCredits().contains(credit));
        assertThat(credit.getExecutive().equals(executive));
        verify(executiveRepository, times(1)).save(executive);
    }

    @Test
    void testDeleteCreditRequest(){
        //Given
        Executive executive = new Executive();
        ArrayList<Credit> credits = new ArrayList<>();
        Credit credit = new Credit();
        credit.setExecutive(executive);
        credits.add(credit);
        executive.setCredits(credits);

        //When
        executiveService.deassignCredit(executive, credit);

        //Then
        assertThat(executive.getCredits()).doesNotContain(credit);
        assertThat(executive.getCredits()).isEmpty();
        assertThat(credit.getClient()).isNull();
        verify(executiveRepository, times(1)).save(executive);
    }

}
