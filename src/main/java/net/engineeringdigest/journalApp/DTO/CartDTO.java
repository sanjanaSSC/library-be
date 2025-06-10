package net.engineeringdigest.journalApp.DTO;

import lombok.Data;
import org.bson.types.ObjectId;

@Data
public class CartDTO {
    private String id;
    private String userId;
}
