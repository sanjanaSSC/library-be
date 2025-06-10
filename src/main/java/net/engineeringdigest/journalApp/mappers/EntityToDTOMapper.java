package net.engineeringdigest.journalApp.mappers;

import net.engineeringdigest.journalApp.DTO.*;
import net.engineeringdigest.journalApp.entities.*;

import java.util.List;
import java.util.stream.Collectors;


public class EntityToDTOMapper {
    public static UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
//        userDTO.setRole(user.getRoles());

        if (user.getCartId() != null) {
            userDTO.setCartId(user.getCartId());
        }

        if (user.getTransactions() != null) {
            List<TransactionDTO> transactionDTOs = user.getTransactions().stream()
                    .map(EntityToDTOMapper::mapToTransactionDTO)
                    .collect(Collectors.toList());
            userDTO.setTransactions(transactionDTOs);
        }

        return userDTO;
    }

    public static CartDTO mapToCartDTO(Cart cart) {
        CartDTO cartDTO = new CartDTO();
        cartDTO.setId(cart.getId());
        cartDTO.setUserId(cart.getUserId());
        return cartDTO;
    }

    public static TransactionDTO mapToTransactionDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setUserId(transaction.getUserId());
        transactionDTO.setBorrowDate(transaction.getBorrowDate());
        transactionDTO.setReturnDate(transaction.getReturnDate());
        return transactionDTO;
    }
}
