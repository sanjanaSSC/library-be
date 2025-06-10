package net.engineeringdigest.journalApp.entities;

import lombok.Data;

import javax.persistence.Id;

@org.springframework.data.elasticsearch.annotations.Document(indexName = "books")
@Data
public class ElasticBookEntry {
    @Id
    private String id;
    private String title;
    private String author;
    private String genre;
    private Long count;

    // Getters, setters, etc.
}
