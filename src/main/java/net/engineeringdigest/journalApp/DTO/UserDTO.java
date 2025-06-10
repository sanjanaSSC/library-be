package net.engineeringdigest.journalApp.DTO;


import lombok.Data;
import net.engineeringdigest.journalApp.entities.User;
import org.bson.types.ObjectId;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserDTO {
        public enum Role {
                USER,
                ADMIN
        }

        private String id;
        private String username;
        private String email;
        @ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        private List<Role> roles = new ArrayList<>();
        private String cartId; // Simplified Cart reference
        private List<TransactionDTO> transactions; // Simplified Transaction references
        private String jwt; // Add this field

        // Getters and setters
}
