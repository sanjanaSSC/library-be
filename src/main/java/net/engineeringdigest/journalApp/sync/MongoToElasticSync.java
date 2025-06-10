//package net.engineeringdigest.journalApp.sync;
//
//import net.engineeringdigest.journalApp.entities.BookEntry;
//import net.engineeringdigest.journalApp.entities.ElasticBookEntry;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.mongodb.core.ChangeStreamEvent;
//import org.springframework.data.mongodb.core.ChangeStreamOptions;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
//import org.springframework.data.mongodb.core.messaging.MessageListener;
//import org.springframework.data.mongodb.core.messaging.Subscription;
//import org.springframework.data.mongodb.core.messaging.SubscriptionRequest;
//import org.springframework.stereotype.Component;
//
//import javax.swing.text.Document;
//
//@Component
//public class MongoToElasticSync {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//    @Autowired
//    private ElasticsearchRepository elasticsearchRepository;
//
//
//    @Bean
//    public Subscription syncMongoToElasticsearch() {
//        MessageListener<ChangeStreamEvent<BookEntry>, BookEntry> messageListener = (event) -> {
//            BookEntry changedBook = event.getBody();
//            if(changedBook == null){
//                return;
//            }
//            switch (event.getRaw().getOperationType()){
//                case INSERT:
//                case UPDATE:
//                    ElasticBookEntry elasticBookEntry = convertToElasticEntry(changedBook);
//                    elasticsearchRepository.save(elasticBookEntry);
//                    break;
//                case DELETE:
//                    Document documentKey = event.getRaw().getCollectionName().
//                    if(documentKey != null){
//                        String id = documentKey.getObjectId("_id").toString();
//                        elasticsearchRepository.deleteById(id);
//                    }
//            }
//        }
//    }
//
//    public ElasticBookEntry convertToElasticEntry(BookEntry bookEntry){
//        ElasticBookEntry elasticBookEntry = new ElasticBookEntry();
//        elasticBookEntry.setId(bookEntry.getId());
//        elasticBookEntry.setTitle(bookEntry.getTitle());
//        elasticBookEntry.setAuthor(bookEntry.getAuthor());
//        elasticBookEntry.setGenre(bookEntry.getGenre());
//        elasticBookEntry.setCount(bookEntry.getCount());
//        return elasticBookEntry;
//    }
//
//    private BookEntry convertToBookEntry(Document document) {
//        BookEntry bookEntry = new BookEntry();
//        bookEntry.setId(document.get("_id").toString());
//        bookEntry.setTitle(document.get("title"));
//        // Add other fields as necessary
//        return bookEntry;
//    }
//}

//import net.engineeringdigest.journalApp.services.ElasticsearchService;
//import org.bson.Document;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//public class MongoToElasticSync {
//    @Autowired
//    private ElasticsearchService elasticsearchService;
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//
//    @EventListener(ApplicationReadyEvent.class)
//    public void startListening() {
//        mongoTemplate.getCollection("book_entries")
//                .watch()
//                .forEach(event -> {
//                    Document document = event.getFullDocument();
//                    elasticsearchService.indexDocument(document);
//                });
//    }
//}
