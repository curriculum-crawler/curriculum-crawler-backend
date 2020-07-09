package edu.harvard.cscis71.curriculum;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.truth.Truth;
import edu.harvard.cscis71.curriculum.api.DocumentApiController;
import edu.harvard.cscis71.curriculum.io.StorageService;
import edu.harvard.cscis71.curriculum.model.Document;
import edu.harvard.cscis71.curriculum.model.DocumentModel;
import edu.harvard.cscis71.curriculum.repository.DocumentRepository;
import edu.harvard.cscis71.curriculum.util.FileUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest
public class WebLayerTest {

    private static Map<String, String> testData;
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ObjectReader objectReader = objectMapper.reader();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentRepository documentRepository;

    @MockBean
    private StorageService storageService;

    private UUID uuid;
    private DocumentModel documentModel;

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

    }

    @Test
    public void shouldReturnOKResponse() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.redirectedUrl(HomeController.REDIRECT_STRING));
    }

    @Test
    void getDocuments() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get(DocumentApiController.REQUEST_MAPPING))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
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
    void deleteDocument() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        Mockito.when(documentRepository.findById(uuid)).thenReturn(Optional.of(documentModel));

        this.mockMvc.perform(MockMvcRequestBuilders.delete(UriBuilder.fromPath(DocumentApiController.REQUEST_MAPPING + "/{id}").build(uuid.toString())))
                .andExpect(MockMvcResultMatchers.status().isOk());
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
}
