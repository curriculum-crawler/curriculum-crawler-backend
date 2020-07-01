package edu.harvard.cscis71.curriculum.repository;

import edu.harvard.cscis71.curriculum.model.Curriculum;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CurriculumRepository extends ElasticsearchRepository<Curriculum, String> {
}
