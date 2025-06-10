package net.engineeringdigest.journalApp.repositories;

import net.engineeringdigest.journalApp.entities.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    Optional<User> findById(String user_id);
    boolean existsByUsername(String username);
}

