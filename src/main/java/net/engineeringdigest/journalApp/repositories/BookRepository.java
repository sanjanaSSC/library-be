package net.engineeringdigest.journalApp.repositories;

import net.engineeringdigest.journalApp.entities.BookEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<BookEntry, String> {
    List<BookEntry> findByTitleContainingIgnoreCase(String title);
    List<BookEntry> findByTitleStartingWithIgnoreCase(String title);
    List<BookEntry> findByAuthorContainingIgnoreCase(String author);
    List<BookEntry> findByGenreContainingIgnoreCase(String genre);
    Optional<BookEntry> findById(String id);

    void deleteById(String id);
}