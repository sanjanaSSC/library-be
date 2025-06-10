package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entities.BookEntry;
import net.engineeringdigest.journalApp.entities.ElasticBookEntry;
import net.engineeringdigest.journalApp.repositories.BookRepository;
//import net.engineeringdigest.journalApp.repositories.ElasticSearchBookRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@Service
public class BookEntryService {

    @Autowired
    private BookRepository bookRepository;

//    @Autowired
//    private ElasticSearchBookRepository elasticSearchBookRepository;
    public List<BookEntry> getAllBooks(){
        return bookRepository.findAll();
    }

//    public ElasticBookEntry convertToElasticEntry(BookEntry bookEntry){
//        ElasticBookEntry elasticBookEntry = new ElasticBookEntry();
//        elasticBookEntry.setId(bookEntry.getId());
//        elasticBookEntry.setTitle(bookEntry.getTitle());
//        elasticBookEntry.setAuthor(bookEntry.getAuthor());
//        elasticBookEntry.setGenre(bookEntry.getGenre());
//        elasticBookEntry.setCount(bookEntry.getCount());
//        return elasticBookEntry;
//    }

    public BookEntry saveEntry(BookEntry bookEntry){
        BookEntry savedBook = bookRepository.save(bookEntry);
//        ElasticBookEntry elasticBookEntry = convertToElasticEntry(savedBook);
//
//        elasticSearchBookRepository.save(elasticBookEntry); // Index to Elasticsearch
        return savedBook;
    }

    public Optional<BookEntry> findById(String id){
        return bookRepository.findById(id);
    }

    public void deleteById(String id){
        bookRepository.deleteById(id);
//        elasticSearchBookRepository.deleteById(id);
    }

    public List<BookEntry> searchBooksByTitle(String title) {
        return bookRepository.findByTitleStartingWithIgnoreCase(title);
    }

    public List<BookEntry> searchBooksByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public List<BookEntry> searchBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }
}