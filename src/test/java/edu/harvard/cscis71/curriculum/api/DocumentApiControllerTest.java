package edu.harvard.cscis71.curriculum.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.truth.Truth;
import edu.harvard.cscis71.curriculum.io.StorageService;
import edu.harvard.cscis71.curriculum.model.Document;
import edu.harvard.cscis71.curriculum.model.DocumentModel;
import edu.harvard.cscis71.curriculum.repository.DocumentRepository;
import edu.harvard.cscis71.curriculum.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentApiControllerTest {

    private static Map<String, String> testData;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader objectReader = objectMapper.reader();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DocumentApiController controller;

    @MockBean
    private StorageService storageService;

    @MockBean
    private DocumentRepository documentRepository;

    private UUID uuid;
    private DocumentModel documentModel;

    @Mock
    private MultipartFile multipartFile;

    @BeforeAll
    static void setupAll() throws IOException {
        testData = FileUtil.load("classpath*:json/*.json");
    }

    @BeforeEach
    void setUp() throws Exception {
        String content = testData.get("create-document");
        uuid = UUID.randomUUID();
        Document document  = objectReader.readValue(content, Document.class);
        documentModel = new DocumentModel();
        documentModel.setId(uuid);
        documentModel.setFilename(document.getFilename());
        documentModel.setFiletype(document.getFiletype());
        documentModel.setStorageUri(new URI("/data"));

        Truth.assertThat(controller).isNotNull();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDocuments() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(DocumentApiController.REQUEST_MAPPING))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetDocuments() {
        Truth.assertThat(controller.getDocuments()).isNotNull();
        Truth.assertThat(controller.getDocuments().getBody()).hasSize(0);
    }

    @Test
    void createDocument() throws Exception {
        String content = testData.get("create-document");
        this.mockMvc.perform(MockMvcRequestBuilders.post(DocumentApiController.REQUEST_MAPPING)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateDocument() throws Exception {
        String content = testData.get("create-document");
        Document document  = objectReader.readValue(content, Document.class);
        // Given: Content must not contain an id
        Truth.assertThat(document.getId()).isNull();

        ResponseEntity<Document> response = controller.createDocument(document);
        Truth.assertThat(response).isNotNull();
        Truth.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Truth.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Truth.assertThat(response.getBody()).isNotNull();
        Truth.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void deleteDocument() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testDeleteDocument() {
        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));

        ResponseEntity<Void> deleteResponse = controller.deleteDocument(uuid.toString());
        Truth.assertThat(deleteResponse).isNotNull();
        Truth.assertThat(deleteResponse.getStatusCode().is2xxSuccessful()).isTrue();
        Truth.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.empty());

        deleteResponse = controller.deleteDocument(uuid.toString());
        Truth.assertThat(deleteResponse).isNotNull();
        Truth.assertThat(deleteResponse.getStatusCode().is4xxClientError()).isTrue();
        Truth.assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getByDocumentId() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void testGetByDocumentId() {
        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));

        ResponseEntity<Document> getResponse = controller.getByDocumentId(UUID.randomUUID().toString());
        Truth.assertThat(getResponse).isNotNull();
        Truth.assertThat(getResponse.getStatusCode()).isNotNull();
        Truth.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        getResponse = controller.getByDocumentId(uuid.toString());
        Truth.assertThat(getResponse).isNotNull();
        Truth.assertThat(getResponse.getStatusCode()).isNotNull();
        Truth.assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateDocument() throws Exception {
        String content = testData.get("create-document");
        Document document  = objectReader.readValue(content, Document.class)
                .id(uuid);
        Truth.assertThat(document.getId()).isNotNull();

        this.mockMvc.perform(MockMvcRequestBuilders.put(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));
        Mockito.when(documentRepository.save(Mockito.any(DocumentModel.class))).thenReturn(documentModel);

        this.mockMvc.perform(MockMvcRequestBuilders.put(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateDocument() throws Exception {
        String content = testData.get("create-document");
        Document document  = objectReader.readValue(content, Document.class)
                .id(uuid);
        Truth.assertThat(document.getId()).isNotNull();

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));
        Mockito.when(documentRepository.save(Mockito.any(DocumentModel.class))).thenReturn(documentModel);

        ResponseEntity<Document> response = controller.updateDocument(uuid.toString(), document);
        Truth.assertThat(response).isNotNull();
        Truth.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        Truth.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
        Truth.assertThat(response.getBody()).isNotNull();
        Truth.assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    void testUploadDocument() throws Exception {
        ResponseEntity<Document> response = controller.uploadDocument(uuid.toString(), multipartFile);
        Truth.assertThat(response).isNotNull();
        Truth.assertThat(response.getStatusCode()).isNotNull();
        Truth.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));
        Mockito.when(documentRepository.save(Mockito.any(DocumentModel.class))).thenReturn(documentModel);

        response = controller.uploadDocument(uuid.toString(), multipartFile);
        Truth.assertThat(response).isNotNull();
        Truth.assertThat(response.getStatusCode()).isNotNull();
        Truth.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.ACCEPTED);
    }

}