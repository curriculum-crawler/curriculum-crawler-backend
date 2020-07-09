package edu.harvard.cscis71.curriculum.api;

import edu.harvard.cscis71.curriculum.io.StorageService;
import edu.harvard.cscis71.curriculum.model.Document;
import edu.harvard.cscis71.curriculum.model.DocumentModel;
import edu.harvard.cscis71.curriculum.repository.DocumentRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("${openapi.curriculumCrawler.base-path:}")
public class DocumentApiController implements DocumentApi {

    public static final String REQUEST_MAPPING = "/document";

    @Value("${upload.path:/home/}")
    private String filePath;

    private final NativeWebRequest request;
    private final DocumentRepository documentRepository;
    private final StorageService storageService;

    @Autowired
    public DocumentApiController(NativeWebRequest request, DocumentRepository documentRepository, StorageService storageService) {
        this.request = request;
        this.documentRepository = documentRepository;
        this.storageService = storageService;
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
        Optional<DocumentModel> model = documentRepository.findById(UUID.fromString(id));
        if (model.isPresent()) {
            DocumentModel m = model.get();
            try {
                storageService.delete(m.getStorageUri());
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (IOException e) {
                log.error("Error deleting file", e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Document> getByDocumentId(String id) {
        Optional<DocumentModel> model = documentRepository.findById(UUID.fromString(id));
        if (model.isPresent()) {
            DocumentModel m = model.get();
            return new ResponseEntity<>(m.toDocument(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<Document> updateDocument(String id, @Valid Document document) {
        Optional<DocumentModel> model = documentRepository.findById(UUID.fromString(id));
        if (model.isPresent()) {
            DocumentModel m = model.get();
            if (Objects.nonNull(document.getFilename())) {
                m.setFilename(document.getFilename());
            }
            if (Objects.nonNull(document.getFiletype())) {
                m.setFiletype(document.getFiletype());
            }
            m = documentRepository.save(m);
            return new ResponseEntity<>(m.toDocument(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value = "Upload Document", nickname = "uploadDocument", notes = "Uploads a document", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted", response = Document.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 501, message = "Not Implemented"),
            @ApiResponse(code = 502, message = "Bad Gateway"),
            @ApiResponse(code = 503, message = "Service Unavailable") })
    @RequestMapping(value = "/document/{id}/upload",
            method = RequestMethod.POST)
    public ResponseEntity<Document> uploadDocument(@ApiParam(value = "UUID to retreive",required=true) @PathVariable("id") String id, @ApiParam(value = "Document to upload"  )  @RequestParam("file") MultipartFile file) {
        Optional<DocumentModel> optional = documentRepository.findById(UUID.fromString(id));
        if (optional.isPresent()) {
            DocumentModel documentModel = optional.get();
            try {
                documentModel.setFiletype(file.getContentType());
                documentModel.setFilename(file.getOriginalFilename());
                documentModel = documentRepository.save(documentModel);
                documentModel.setStorageUri(UriBuilder.fromPath("/data/{id}").build("id", documentModel.getId()));
                documentModel = documentRepository.save(documentModel);
                storageService.store(file.getInputStream(), documentModel.getStorageUri());
                return new ResponseEntity<>(documentModel.toDocument(), HttpStatus.ACCEPTED);
            } catch (Exception e) {
                log.error("Error creating file", e);
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
