package net.engineeringdigest.journalApp.DTO;

import lombok.Data;
import org.bson.types.ObjectId;

import java.time.LocalDate;
import java.util.Date;

@Data
public class TransactionDTO {
    private String id;
    private String userId; // Simplified reference
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private boolean isReturned;
}
