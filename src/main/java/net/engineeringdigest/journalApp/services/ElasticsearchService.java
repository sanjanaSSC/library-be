//package net.engineeringdigest.journalApp.services;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.elasticsearch.core.IndexRequest;
//import co.elastic.clients.elasticsearch.core.IndexResponse;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Service
//public class ElasticsearchService {
//
//    @Autowired
//    private ElasticsearchClient elasticsearchClient;
//
//    private static final String INDEX_NAME = "books"; // Elasticsearch index name
//
//    public void indexDocument(Map<String, Object> document) {
//        try {
//            IndexRequest<Map<String, Object>> request = IndexRequest.of(i -> i
//                    .index(INDEX_NAME)
//                    .document(document)
//            );
//
//            IndexResponse response = elasticsearchClient.index(request);
//            System.out.println("Indexed document with ID: " + response.id());
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Error indexing document: " + e.getMessage());
//        }
//    }
//}

