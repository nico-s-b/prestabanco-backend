package com.example.tingeso1.services;

import com.example.tingeso1.entities.*;
import com.example.tingeso1.repositories.ClientRepository;
import com.example.tingeso1.repositories.CreditRepository;
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

public class ClientServiceTest {

    @InjectMocks
    ClientService clientService = new ClientService();

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private CreditRepository creditRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUsers() {
        //Given
        Client client1 = new Client();
        Client client2 = new Client();
        Client client3 = new Client();
        List<Client> mockUsers = new ArrayList<>(Arrays.asList(client1, client2, client3));

        when(clientRepository.findAll()).thenReturn(mockUsers);

        //When
        ArrayList<Client> result = (ArrayList<Client>) clientService.getAllClients();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockUsers, result);

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testAddCreditRequest(){
        //Given
        Client client = new Client();
        client.setCredits(new ArrayList<>());
        Credit credit = new Credit();

        //When
        clientService.addCreditRequest(client, credit);

        //Then
        assertThat(client.getCredits()).contains(credit);
        assertThat(credit.getClient()).isEqualTo(client);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testDeleteCreditRequest(){
        //Given
        Client client = new Client();
        ArrayList<Credit> credits = new ArrayList<>();
        Credit credit = new Credit();
        credit.setClient(client);
        credits.add(credit);
        client.setCredits(credits);

        //When
        clientService.deleteCreditRequest(client, credit);

        //Then
        assertThat(client.getCredits()).doesNotContain(credit);
        assertThat(client.getCredits()).isEmpty();
        assertThat(credit.getClient()).isNull();
        verify(creditRepository, times(1)).delete(credit);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testAddClientAccount(){
        //Given
        Client client = new Client();
        ClientAccount account = new ClientAccount();

        //When
        clientService.addClientAccount(client, account);

        //Then
        assertThat(account.getClient()).isEqualTo(client);
        assertThat(client.getAccount()).isEqualTo(account);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testAddClientCreditRecord(){
        //Given
        Client client = new Client();
        ClientCreditRecord record = new ClientCreditRecord();

        //When
        clientService.addClientCreditRecord(client, record);

        //Then
        assertThat(record.getClient()).isEqualTo(client);
        assertThat(client.getCreditRecord()).isEqualTo(record);
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testAddClientEmploymentRecord(){
        //Given
        Client client = new Client();
        ClientEmploymentRecord record = new ClientEmploymentRecord();

        //When
        clientService.addClientEmploymentRecord(client, record);

        //Then
        assertThat(record.getClient()).isEqualTo(client);
        assertThat(client.getEmploymentRecord()).isEqualTo(record);
        verify(clientRepository, times(1)).save(client);
    }

}
