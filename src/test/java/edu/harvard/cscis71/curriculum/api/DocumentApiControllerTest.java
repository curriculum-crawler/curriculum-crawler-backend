package edu.harvard.cscis71.curriculum.api;

import com.google.common.truth.Truth;
import edu.harvard.cscis71.curriculum.util.FileUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class DocumentApiControllerTest {

    private static Map<String, String> testData;

    @Autowired
    private MockMvc mockMvc;

    private DocumentApiController controller;

    @BeforeAll
    static void setupAll() throws IOException {
        testData = FileUtil.load("classpath*:json/*.json");
    }

    @BeforeEach
    void setUp() {
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
    void deleteDocument() {
    }

    @Test
    void getByDocumentId() {
    }

    @Test
    void updateDocument() {
    }

    @Test
    void uploadDocument() {

    }

}