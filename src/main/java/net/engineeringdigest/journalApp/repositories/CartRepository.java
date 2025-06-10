package net.engineeringdigest.journalApp.repositories;

import net.engineeringdigest.journalApp.entities.Cart;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {
    Cart findByUserId(String user_id);
    Cart deleteBookById(String book_id);
    Optional<Cart> findById(String id);
}
