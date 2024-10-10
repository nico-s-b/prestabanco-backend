package com.example.tingeso1.services;

import com.example.tingeso1.entities.ClientAccount;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.enums.SaveCapacityStatus;
import com.example.tingeso1.repositories.ClientAccountRepository;
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

public class ClientAccountServiceTest {

    @InjectMocks
    ClientAccountService clientAccountService = new ClientAccountService();

    @Mock
    private ClientAccountRepository clientAccountRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetClientAccounts() {
        //Given
        ClientAccount account1 = new ClientAccount();
        ClientAccount account2 = new ClientAccount();
        ClientAccount account3 = new ClientAccount();
        List<ClientAccount> mockAccounts = new ArrayList<>(Arrays.asList(account1, account2, account3));

        when(clientAccountRepository.findAll()).thenReturn(mockAccounts);

        //When
        ArrayList<ClientAccount> result = clientAccountService.getClientAccounts();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockAccounts, result);

        verify(clientAccountRepository, times(1)).findAll();
    }

    @Test
    void testSaveClientAccount() {
        //Given
        ClientAccount account = new ClientAccount();

        when(clientAccountRepository.save(account)).thenReturn(account);

        //When
        ClientAccount result = clientAccountService.saveClientAccount(account);

        //Then
        assertNotNull(result);
        assertEquals(account, result);
        verify(clientAccountRepository, times(1)).save(account);
    }

    @Test
    void testGetClientAccountById() {
        //Given
        ClientAccount account = new ClientAccount();
        Long id = 1L;
        account.setId(id);

        when(clientAccountRepository.findById(id)).thenReturn(Optional.of(account));

        //When
        ClientAccount result = clientAccountService.getClientAccountById(id);

        //Then
        assertNotNull(result);
        assertEquals(account, result);
        verify(clientAccountRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateClientAccount() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setId(1L);

        when(clientAccountRepository.save(account)).thenReturn(account);

        //When
        ClientAccount result = clientAccountService.updateClientAccount(account);

        //Then
        assertNotNull(result);
        assertEquals(account, result);
        verify(clientAccountRepository, times(1)).save(account);
    }

    @Test
    void testDeleteClientAccount_Success() throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(clientAccountRepository).deleteById(id);

        //When
        boolean result = clientAccountService.deleteClientAccount(id);

        //Then
        assertTrue(result);
        verify(clientAccountRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteClientAccount_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(clientAccountRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            clientAccountService.deleteClientAccount(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(clientAccountRepository, times(1)).deleteById(id);
    }

    @Test
    void testHasMinimumBalance_Success() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(100);
        Credit credit = new Credit();
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasMinimumBalance(account, credit);

        assertTrue(account.getR1MinimumBalance());
    }

    @Test
    void testHasMinimumBalance_Failure() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(99);
        Credit credit = new Credit();
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasMinimumBalance(account, credit);

        assertFalse(account.getR1MinimumBalance());
    }

    @Test
    void testHasGoodBalanceYearsRelation_WhenLessThan2Years_Success() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(200);
        account.setStartDate(ZonedDateTime.now().minusYears(2).plusDays(1));
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasGoodBalanceYearsRelation(credit, account);

        //Then
        assertTrue(account.getR4BalanceYearsOfAccountRelation());
    }

    @Test
    void testHasGoodBalanceYearsRelation_WhenLessThan2Years_Failure() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(199);
        account.setStartDate(ZonedDateTime.now().minusYears(2).plusDays(1));
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasGoodBalanceYearsRelation(credit, account);

        //Then
        assertFalse(account.getR4BalanceYearsOfAccountRelation());
    }

    @Test
    void testHasGoodBalanceYearsRelation_WhenMoreThan2Years_Success() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(100);
        account.setStartDate(ZonedDateTime.now().minusYears(2));
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasGoodBalanceYearsRelation(credit, account);

        //Then
        assertTrue(account.getR4BalanceYearsOfAccountRelation());
    }

    @Test
    void testHasGoodBalanceYearsRelation_WhenMoreThan2Years_Failure() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setAccountBalance(99);
        account.setStartDate(ZonedDateTime.now().minusYears(2));
        Credit credit = new Credit();
        credit.setRequestDate(ZonedDateTime.now());
        credit.setCreditMount(1000);

        //When
        clientAccountService.hasGoodBalanceYearsRelation(credit, account);

        //Then
        assertFalse(account.getR1MinimumBalance());
    }

    @Test
    void testRulesApprovedFromSaveCapacity(){
        //Given
        ClientAccount account = new ClientAccount();
        account.setR1MinimumBalance(true);
        account.setR2ConsistentSaves(true);
        account.setR3PeriodicDeposits(true);
        account.setR4BalanceYearsOfAccountRelation(true);
        account.setR5RecentWithdrawals(false);

        //When
        int rules = clientAccountService.rulesApprovedFromSaveCapacity(account);

        //Then
        assertEquals(4, rules);
    }

    @Test
    void testHasAllRulesEvaluated_Success() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setR1MinimumBalance(true);
        account.setR2ConsistentSaves(true);
        account.setR3PeriodicDeposits(true);
        account.setR4BalanceYearsOfAccountRelation(true);
        account.setR5RecentWithdrawals(false);

        //When
        boolean result = clientAccountService.hasAllRulesEvaluated(account);

        //Then
        assertTrue(result);
    }

    @Test
    void testHasAllRulesEvaluated_Failure() {
        //Given
        ClientAccount account = new ClientAccount();
        account.setR1MinimumBalance(true);
        account.setR2ConsistentSaves(true);
        account.setR3PeriodicDeposits(true);
        account.setR4BalanceYearsOfAccountRelation(true);
        //account.setR5RecentWithdrawals(false);

        //When
        boolean result = clientAccountService.hasAllRulesEvaluated(account);

        //Then
        assertFalse(result);
    }

    @Test
    void testEvaluateSaveCapacity_5rules(){
        //Given
        ClientAccount account = new ClientAccount();
        account.setR1MinimumBalance(true);
        account.setR2ConsistentSaves(true);
        account.setR3PeriodicDeposits(true);
        account.setR4BalanceYearsOfAccountRelation(true);
        account.setR5RecentWithdrawals(true);

        //When
        clientAccountService.evaluateSaveCapacity(account);

        //Then
        assertEquals(account.getSaveCapacityStatus(), SaveCapacityStatus.SOLID);
    }

    @Test
    void testEvaluateSaveCapacity_3or4rules(){
        //Given
        //3 rules approved
        ClientAccount account1 = new ClientAccount();
        account1.setR1MinimumBalance(true);
        account1.setR2ConsistentSaves(true);
        account1.setR3PeriodicDeposits(true);
        account1.setR4BalanceYearsOfAccountRelation(false);
        account1.setR5RecentWithdrawals(false);
        //4 rules approved
        ClientAccount account2 = new ClientAccount();
        account2.setR1MinimumBalance(true);
        account2.setR2ConsistentSaves(true);
        account2.setR3PeriodicDeposits(true);
        account2.setR4BalanceYearsOfAccountRelation(true);
        account2.setR5RecentWithdrawals(false);

        //When
        clientAccountService.evaluateSaveCapacity(account1);
        clientAccountService.evaluateSaveCapacity(account2);

        //Then
        assertEquals(account1.getSaveCapacityStatus(), SaveCapacityStatus.MODERATE);
        assertEquals(account2.getSaveCapacityStatus(), SaveCapacityStatus.MODERATE);
    }

    @Test
    void testEvaluateSaveCapacity_2orLessRules(){
        //Given
        //No rules approved
        ClientAccount account0 = new ClientAccount();
        account0.setR1MinimumBalance(true);
        account0.setR2ConsistentSaves(true);
        account0.setR3PeriodicDeposits(false);
        account0.setR4BalanceYearsOfAccountRelation(false);
        account0.setR5RecentWithdrawals(false);
        //1 rule approved
        ClientAccount account1 = new ClientAccount();
        account1.setR1MinimumBalance(true);
        account1.setR2ConsistentSaves(true);
        account1.setR3PeriodicDeposits(false);
        account1.setR4BalanceYearsOfAccountRelation(false);
        account1.setR5RecentWithdrawals(false);
        //2 rules approved
        ClientAccount account2 = new ClientAccount();
        account2.setR1MinimumBalance(true);
        account2.setR2ConsistentSaves(false);
        account2.setR3PeriodicDeposits(false);
        account2.setR4BalanceYearsOfAccountRelation(false);
        account2.setR5RecentWithdrawals(false);

        //When
        clientAccountService.evaluateSaveCapacity(account0);
        clientAccountService.evaluateSaveCapacity(account1);
        clientAccountService.evaluateSaveCapacity(account2);

        //Then
        assertEquals(account0.getSaveCapacityStatus(), SaveCapacityStatus.INSUFFICIENT);
        assertEquals(account1.getSaveCapacityStatus(), SaveCapacityStatus.INSUFFICIENT);
        assertEquals(account2.getSaveCapacityStatus(), SaveCapacityStatus.INSUFFICIENT);
    }
}

