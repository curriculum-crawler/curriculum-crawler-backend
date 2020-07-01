package edu.harvard.cscis71.curriculum.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "curriculumcrawler")
public class Curriculum {
    @Id
    private String id;

    private String title;

    public Curriculum() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
