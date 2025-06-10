package net.engineeringdigest.journalApp.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "cart")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field("user_id") // Field name in the database
    private String userId;

    @DBRef
    private List<BookEntry> bookEntries = new ArrayList<>();
}
