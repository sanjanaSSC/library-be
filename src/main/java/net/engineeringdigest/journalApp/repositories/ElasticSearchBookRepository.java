//package net.engineeringdigest.journalApp.repositories;
//
//import net.engineeringdigest.journalApp.entities.BookEntry;
//import net.engineeringdigest.journalApp.entities.ElasticBookEntry;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.mongodb.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface ElasticSearchBookRepository extends ElasticsearchRepository<ElasticBookEntry, String>{
//    @Query("{\"match\": {\"title\": \"?0\"}}")
//    List<ElasticBookEntry> getByTitle(String title);
//}
