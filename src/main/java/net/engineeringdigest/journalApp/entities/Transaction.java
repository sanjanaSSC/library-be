package net.engineeringdigest.journalApp.entities;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.persistence.*;

import java.time.LocalDate;
import java.util.Date;

@Document(collection = "transactions")
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Field("user_id") // Field name in the database
    private String userId;

//    @Field("book_id") // Field name in the database
//    private ObjectId bookId;

    @Field("cart_id") // Field name in the database
    private String cartId;

    private LocalDate borrowDate;

    private LocalDate returnDate;

    private boolean status;

}
