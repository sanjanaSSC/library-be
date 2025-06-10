package net.engineeringdigest.journalApp.repositories;

import net.engineeringdigest.journalApp.entities.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, Long> {
    Transaction findCartById(String cart_id);
    Transaction findBookById(String book_id);

}