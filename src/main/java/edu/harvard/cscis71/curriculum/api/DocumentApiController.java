package edu.harvard.cscis71.curriculum.api;

import edu.harvard.cscis71.curriculum.model.Document;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("${openapi.curriculumCrawler.base-path:}")
public class DocumentApiController implements DocumentApi {

    public static final String REQUEST_MAPPING = "/document";

    @Value("${upload.path:/home/}")
    private String filePath;

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

    @ApiOperation(value = "Upload Document", nickname = "uploadDocument", notes = "Uploads a document", tags={  })
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", response = Document.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden"),
            @ApiResponse(code = 500, message = "Internal Server Error"),
            @ApiResponse(code = 501, message = "Not Implemented"),
            @ApiResponse(code = 502, message = "Bad Gateway"),
            @ApiResponse(code = 503, message = "Service Unavailable") })
    @RequestMapping(value = "/upload",
            method = RequestMethod.POST)
    public ResponseEntity<String> uploadDocument(@ApiParam(value = "Document to upload"  )  @RequestParam("file") MultipartFile file) {
        Path root = Paths.get(filePath);
        try {
            Files.copy(file.getInputStream(), root.resolve(file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return new ResponseEntity<>("Error uploading file :" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("File Uploaded Successfully", HttpStatus.OK);

    }

}
