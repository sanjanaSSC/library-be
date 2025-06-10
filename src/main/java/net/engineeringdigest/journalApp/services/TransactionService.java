package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entities.Cart;
import net.engineeringdigest.journalApp.entities.Transaction;
import net.engineeringdigest.journalApp.repositories.TransactionRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<Transaction> viewAll(){
       return transactionRepository.findAll();
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }

    public Transaction findCartByID(String cart_id){
        return transactionRepository.findCartById(cart_id);
    }

    public Transaction findBookById(String cart_id){
        return transactionRepository.findBookById(cart_id);
    }

    public void borrowBooks(Transaction transaction){
        transactionRepository.save(transaction);
    }

    public void returnBooks(Transaction transaction){
        transactionRepository.save(transaction);
    }
}
