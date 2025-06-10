package net.engineeringdigest.journalApp.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.io.ObjectInput;


@Document(collection = "book_entries")
@Data
public class BookEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonSerialize(using = ToStringSerializer.class) // Convert ObjectId to String
    private String id;

    private String title;
    private String author;
    private String genre;
    private Long count;
}
