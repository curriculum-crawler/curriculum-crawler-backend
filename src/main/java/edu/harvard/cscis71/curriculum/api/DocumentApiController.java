package edu.harvard.cscis71.curriculum.api;

import edu.harvard.cscis71.curriculum.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("${openapi.curriculumCrawler.base-path:}")
public class DocumentApiController implements DocumentApi {

    public static final String REQUEST_MAPPING = "/document";

    private final NativeWebRequest request;

    @Autowired
    public DocumentApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<List<Document>> getDocuments() {
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Document> createDocument(@Valid Document document) {
        document.setId(UUID.randomUUID());
        return new ResponseEntity<>(document, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteDocument(String id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Document> getByDocumentId(String id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Document> updateDocument(String id, @Valid Document document) {
        return new ResponseEntity<>(document, HttpStatus.OK);

    }
}
