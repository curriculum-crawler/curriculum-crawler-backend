package edu.harvard.cscis71.curriculum.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.net.URI;
import java.util.UUID;

@ToString
@EqualsAndHashCode
@Entity
public class DocumentModel {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Setter
    @Getter
    private UUID id;
    @Setter
    @Getter
    private String filename;
    @Setter
    @Getter
    private String filetype;
    @Setter
    @Getter
    private URI storageUri;

    public DocumentModel() {}

    public DocumentModel(Document document) {
        this.filename = document.getFilename();
        this.filetype = document.getFiletype();
        this.storageUri = document.getStorageUri();
    }

    public Document toDocument() {
        return new Document().filetype(this.filetype)
                .storageUri(this.storageUri)
                .filename(this.filename)
                .id(this.id);
    }
}
