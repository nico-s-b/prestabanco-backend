package com.example.tingeso1.services;

import com.example.tingeso1.entities.Client;
import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.enums.CreditState;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.repositories.CreditRepository;
import com.example.tingeso1.utils.CreditRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.text.Document;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.*;

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

    @Mock
    private ClientService clientService;

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
    void testSaveCredit_Docs() {
        //Given
        Credit credit = new Credit();
        credit.setDocuments(new ArrayList<>());

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
    void testGetCreditsById() {
        //Given
        Credit credit = new Credit();
        ArrayList<Credit> credits = new ArrayList<>(Collections.singletonList(credit));
        Long clientId = 1L;

        when(creditRepository.findAllByClientId(clientId)).thenReturn(credits);

        //When
        ArrayList<Credit> result = creditService.getCreditsById(clientId);

        //Then
        assertNotNull(result);
        assertEquals(credits, result);
        verify(creditRepository, times(1)).findAllByClientId(clientId);
    }

    @Test
    void testUpdateCredit() {
        //Given
        Credit credit = new Credit();
        credit.setId(1L);

        when(creditRepository.save(credit)).thenReturn(credit);

        //When
        Credit result = creditService.saveCredit(credit);

        //Then
        assertNotNull(result);
        assertEquals(credit, result);
        verify(creditRepository, times(1)).save(credit);
    }

    @Test
    void testCreateCredit() {
        //Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setCreditType("FIRSTHOME");
        creditRequest.setLoanPeriod(15);
        creditRequest.setCreditMount(50000);
        creditRequest.setPropertyValue(100000);

        Client client = new Client();
        client.setCredits(new ArrayList<>());

        CreditService spyService = spy(creditService);
        Credit mockCredit = new Credit();
        doReturn(mockCredit).when(spyService).buildCredit(creditRequest);

        //When
        Credit result = spyService.createCredit(creditRequest, client);

        //Then
        assertNotNull(result);
        assertEquals(mockCredit, result);
        assertTrue(client.getCredits().contains(result));

        verify(clientService, times(1)).saveClient(client);
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
    void testGetMaxAnnualRate() {
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
        float rate1 = creditService.getMaxAnnualRate(credit1);
        float rate2 = creditService.getMaxAnnualRate(credit2);
        float rate3 = creditService.getMaxAnnualRate(credit3);
        float rate4 = creditService.getMaxAnnualRate(credit4);

        //Then
        assertThat(rate1).isEqualTo(5);
        assertThat(rate2).isEqualTo(6);
        assertThat(rate3).isEqualTo(7);
        assertThat(rate4).isEqualTo(6);
    }

    @Test
    void testGetMinAnnualRate() {
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
        float rate1 = creditService.getMinAnnualRate(credit1);
        float rate2 = creditService.getMinAnnualRate(credit2);
        float rate3 = creditService.getMinAnnualRate(credit3);
        float rate4 = creditService.getMinAnnualRate(credit4);

        //Then
        assertThat(rate1).isEqualTo(3.5F);
        assertThat(rate2).isEqualTo(4);
        assertThat(rate3).isEqualTo(5);
        assertThat(rate4).isEqualTo(4.5F);
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
    void testSimulateCreditInstallment() {
        // Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setCreditType("FIRSTHOME");
        creditRequest.setLoanPeriod(15);
        creditRequest.setCreditMount(50000);
        creditRequest.setPropertyValue(100000);

        Credit mockCredit = new Credit();
        CreditService spyService = spy(creditService);

        doReturn(mockCredit).when(spyService).buildCredit(creditRequest);

        doReturn(3.5F).when(spyService).getMinAnnualRate(mockCredit);
        doReturn(5.0F).when(spyService).getMaxAnnualRate(mockCredit);

        doReturn(1000,1250, 1500).when(spyService).getCreditInstallment(mockCredit);

        // When
        List<Integer> result = spyService.simulateCreditInstallments(creditRequest);

        // Then
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(1000, result.get(0)); // cuota mínima
        assertEquals(1250, result.get(1)); // cuota solicitada
        assertEquals(1500, result.get(2)); // cuota máxima

        verify(spyService, times(1)).getMaxAnnualRate(mockCredit);
        verify(spyService, times(1)).getMinAnnualRate(mockCredit);
        verify(spyService, times(3)).getCreditInstallment(mockCredit); // llamado dos veces, para min y max
    }

    @ParameterizedTest
    @EnumSource(CreditType.class)
    void testBuildCreditWithDifferentTypes(CreditType creditType) {
        // Given
        CreditRequest creditRequest = new CreditRequest();
        creditRequest.setCreditType(creditType.name());
        creditRequest.setLoanPeriod(15);
        creditRequest.setCreditMount(50000);
        creditRequest.setPropertyValue(100000);

        // When
        Credit result = creditService.buildCredit(creditRequest);

        // Then
        assertNotNull(result);
        assertThat(result.getCreditType()).isEqualTo(creditType);
        assertThat(result.getLoanPeriod()).isEqualTo(15);
        assertThat(result.getCreditMount()).isEqualTo(50000);
        assertThat(result.getPropertyValue()).isEqualTo(100000);
    }

}
