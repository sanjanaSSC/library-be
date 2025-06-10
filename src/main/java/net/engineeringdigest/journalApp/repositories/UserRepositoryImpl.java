package net.engineeringdigest.journalApp.repositories;

import net.engineeringdigest.journalApp.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.*;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;
    public List<User> getUserForSA(){
        Query query = new Query();
        Criteria criteria = new Criteria();
        query.addCriteria(Criteria.where("email").regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"));
        query.addCriteria(criteria.orOperator(Criteria.where("email").exists(true),
        Criteria.where("sentimentAnalysis").is(true)));
        List<User> users = mongoTemplate.find(query, User.class);
        return users;
    }
}
