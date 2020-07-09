package edu.harvard.cscis71.curriculum.repository;

import edu.harvard.cscis71.curriculum.model.DocumentModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DocumentRepository extends CrudRepository<DocumentModel, UUID> {

}
