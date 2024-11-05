package com.example.tingeso1.services;

import com.example.tingeso1.entities.Credit;
import com.example.tingeso1.entities.DocumentEntity;
import com.example.tingeso1.enums.CreditType;
import com.example.tingeso1.enums.DocumentType;
import com.example.tingeso1.repositories.DocumentRepository;
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

public class DocumentServiceTest {

    @InjectMocks
    DocumentService documentService;

    @Mock
    private DocumentRepository documentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetDocuments() {
        //Given
        DocumentEntity document1 = new DocumentEntity();
        DocumentEntity document2 = new DocumentEntity();
        DocumentEntity document3 = new DocumentEntity();
        ArrayList<DocumentEntity> mockDocuments = new ArrayList<>(Arrays.asList(document1, document2, document3));

        when(documentRepository.findAll()).thenReturn(mockDocuments);

        //When
        ArrayList<DocumentEntity> result = documentService.getDocuments();

        //Then
        assertThat(result.size()).isEqualTo(3);
        assertNotNull(result);
        assertEquals(mockDocuments, result);

        verify(documentRepository, times(1)).findAll();
    }

    @Test
    void testSaveDocument() {
        //Given
        DocumentEntity document = new DocumentEntity();

        when(documentRepository.save(document)).thenReturn(document);

        //When
        DocumentEntity result = documentService.saveDocument(document);

        //Then
        assertNotNull(result);
        assertEquals(document, result);
        verify(documentRepository, times(1)).save(document);
    }

    @Test
    void testGetDocumentById() {
        //Given
        DocumentEntity document = new DocumentEntity();
        Long id = 1L;
        document.setId(id);

        when(documentRepository.findById(id)).thenReturn(Optional.of(document));

        //When
        DocumentEntity result = documentService.getDocumentById(id);

        //Then
        assertNotNull(result);
        assertEquals(document, result);
        verify(documentRepository, times(1)).findById(id);
    }

    @Test
    void testGetDocumentByCreditId() {
        //Given
        Credit credit = new Credit();
        credit.setId(1L);

        DocumentEntity document1 = new DocumentEntity();
        DocumentEntity document2 = new DocumentEntity();
        DocumentEntity document3 = new DocumentEntity();

        document1.setCredit(credit);
        document2.setCredit(credit);
        document3.setCredit(credit);

        List<DocumentEntity> documents = Arrays.asList(document1, document2, document3);
        // Mock de la respuesta del repositorio
        when(documentRepository.findAllByCreditId(credit.getId())).thenReturn(documents);
        //When
        List<DocumentEntity> result = documentService.getDocumentsByCreditId(credit.getId());

        //Then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(3);
        assertEquals(document1, result.get(0));
        assertEquals(document2, result.get(1));
        assertEquals(document3, result.get(2));

        verify(documentRepository, times(1)).findAllByCreditId(credit.getId());
    }

    @Test
    void testDeleteDocument_Success() throws Exception {
        //Given
        Long id = 1L;

        //borrado exitoso no lanza excepción
        doNothing().when(documentRepository).deleteById(id);

        //When
        boolean result = documentService.deleteDocument(id);

        //Then
        assertTrue(result);
        verify(documentRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeleteDocument_Exception() throws Exception {
        // Given
        Long id = 1L;

        // Al fallar, deleteById lanza una excepción
        doThrow(new RuntimeException("Error al eliminar")).when(documentRepository).deleteById(id);

        // When
        Exception exception = assertThrows(Exception.class, () -> {
            documentService.deleteDocument(id);
        });

        //Then
        assertEquals("Error al eliminar", exception.getMessage());
        verify(documentRepository, times(1)).deleteById(id);
    }

    @Test
    void testWhichMissingDocuments_COMERCIAL(){
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.COMERCIAL);
        ArrayList<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity document1 = new DocumentEntity();
        document1.setDocumentType(DocumentType.INCOMECERTIFY);
        documents.add(document1);
        DocumentEntity document2 = new DocumentEntity();
        document2.setDocumentType(DocumentType.FINANCIALSTATUSREPORT);
        documents.add(document2);
        DocumentEntity document3 = new DocumentEntity();
        document3.setDocumentType(DocumentType.VALUATIONCERTIFY);
        documents.add(document3);
        DocumentEntity document4 = new DocumentEntity();
        document4.setDocumentType(DocumentType.BUSINESSPLAN);
        documents.add(document4);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing.size()).isEqualTo(0);

        //Given
        documents.remove(document1);
        documents.remove(document2);
        documents.remove(document3);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing2 = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing2.size()).isEqualTo(3);
        assertThat(missing2.stream().anyMatch(DocumentType.INCOMECERTIFY::equals)).isTrue();
        assertThat(missing2.stream().anyMatch(DocumentType.FINANCIALSTATUSREPORT::equals)).isTrue();
        assertThat(missing2.stream().anyMatch(DocumentType.VALUATIONCERTIFY::equals)).isTrue();
    }

    @Test
    void testWhichMissingDocuments_FIRSTHOME(){
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.FIRSTHOME);
        ArrayList<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity document1 = new DocumentEntity();
        document1.setDocumentType(DocumentType.INCOMECERTIFY);
        documents.add(document1);
        DocumentEntity document2 = new DocumentEntity();
        document2.setDocumentType(DocumentType.VALUATIONCERTIFY);
        documents.add(document2);
        DocumentEntity document3 = new DocumentEntity();
        document3.setDocumentType(DocumentType.CREDITREPORT);
        documents.add(document3);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing.size()).isEqualTo(0);

        //Given
        documents.remove(document1);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing2 = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing2.size()).isEqualTo(1);
        assertThat(missing2.get(0)).isEqualTo(DocumentType.INCOMECERTIFY);
    }

    @Test
    void testWhichMissingDocuments_SECONDHOME(){
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.SECONDHOME);
        ArrayList<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity document1 = new DocumentEntity();
        document1.setDocumentType(DocumentType.INCOMECERTIFY);
        documents.add(document1);
        DocumentEntity document2 = new DocumentEntity();
        document2.setDocumentType(DocumentType.VALUATIONCERTIFY);
        documents.add(document2);
        DocumentEntity document3 = new DocumentEntity();
        document3.setDocumentType(DocumentType.FIRSTHOUSEDEED);
        documents.add(document3);
        DocumentEntity document4 = new DocumentEntity();
        document4.setDocumentType(DocumentType.CREDITREPORT);
        documents.add(document4);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing.size()).isEqualTo(0);

        //Given
        documents.remove(document3);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing2 = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing2.size()).isEqualTo(1);
        assertThat(missing2.get(0)).isEqualTo(DocumentType.FIRSTHOUSEDEED);
    }

    @Test
    void testWhichMissingDocuments_REMODELING(){
        //Given
        Credit credit = new Credit();
        credit.setCreditType(CreditType.REMODELING);
        ArrayList<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity document1 = new DocumentEntity();
        document1.setDocumentType(DocumentType.INCOMECERTIFY);
        documents.add(document1);
        DocumentEntity document2 = new DocumentEntity();
        document2.setDocumentType(DocumentType.REMODELINGBUDGET);
        documents.add(document2);
        DocumentEntity document3 = new DocumentEntity();
        document3.setDocumentType(DocumentType.UPDATEDVALUATIONCERTIFY);
        documents.add(document3);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing.size()).isEqualTo(0);

        //Given
        documents.remove(document2);
        documents.remove(document3);
        credit.setDocuments(documents);

        //When
        ArrayList<DocumentType> missing2 = documentService.whichMissingDocuments(credit);

        //Then
        assertThat(missing2.size()).isEqualTo(2);
        assertThat(missing2.get(0)).isEqualTo(DocumentType.REMODELINGBUDGET);
        assertThat(missing2.get(1)).isEqualTo(DocumentType.UPDATEDVALUATIONCERTIFY);
    }
}
