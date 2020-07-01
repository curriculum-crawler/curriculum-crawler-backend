package edu.harvard.cscis71.curriculum;

import edu.harvard.cscis71.curriculum.model.Curriculum;
import edu.harvard.cscis71.curriculum.repository.CurriculumRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class CurriculumApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private CurriculumRepository curriculumRepository;

    @Test
    // Test requires a live Elastic server will need to fix
    @Disabled
    public void addNewCurriculum() {
        Curriculum curriculum = new Curriculum();
        curriculum.setId("test_id");
        curriculum.setTitle("Test Curriculum");
        curriculumRepository.save(curriculum);

        Optional<Curriculum> retrieved = curriculumRepository.findById("test_id");
        if(retrieved.isPresent()) {
            assert curriculum.getTitle().equals(retrieved.get().getTitle());
        }
    }
}
