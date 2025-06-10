package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entities.*;
import net.engineeringdigest.journalApp.services.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.*;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookEntryService bookEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Transaction> viewAllTransactions(){
        return transactionService.viewAll();
    }

//    @PostMapping("/createTransaction")
//    public void createTransaction(@RequestBody Transaction transaction) {
//        if (transaction.getUserId() == null || userService.findById(transaction.getUserId())== null) {
//            throw new IllegalArgumentException("User and Username must not be null.");
//        }
//        transactionService.saveTransaction(transaction);
//    }



    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        Optional<Cart> cartOptional = cartService.findById(user.getCartId());
        if(!cartOptional.isPresent()){
            return new ResponseEntity<>("Cart not found",HttpStatus.BAD_REQUEST);
        }
        Cart cart = cartOptional.get();
        List<BookEntry> bookEntries = cart.getBookEntries();
        List<String> outOfStockBooks = new ArrayList<>();

        // Check each book's stock and update the count
        for (BookEntry book : bookEntries) {
            if (book.getCount() > 0) {
                book.setCount(book.getCount() - 1);
                bookEntryService.saveEntry(book);
            } else {
                outOfStockBooks.add(book.getTitle());
            }
        }

        // If there are any out of stock books, return a BAD_REQUEST response
        if (!outOfStockBooks.isEmpty()) {
            return new ResponseEntity<>("The following books are out of stock: " + String.join(", ", outOfStockBooks), HttpStatus.BAD_REQUEST);
        }

        // Update the transaction's borrow date and set status
        Transaction transaction = new Transaction();
        transaction.setUserId(cart.getUserId());
        transaction.setCartId(cart.getId());
        transaction.setBorrowDate(LocalDate.now());
        transaction.setStatus(true); // Consider refining the status
        transactionService.borrowBooks(transaction);

        cart.getBookEntries().clear();
        cartService.saveCart(cart);
        // Return success message
        return new ResponseEntity<>("Books have been borrowed and will be delivered shortly", HttpStatus.OK);
    }

    @PostMapping("/return/{book_id}")
    public ResponseEntity<?> returnBook(@PathVariable String book_id){
        Transaction transaction = transactionService.findBookById(book_id);
        if(transaction == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Optional<BookEntry> bookOptional = bookEntryService.findById(book_id);

        if(bookOptional.isPresent()){
            BookEntry book = bookOptional.get();
            book.setCount(book.getCount() + 1);
            transaction.setReturnDate(LocalDate.now());
            transaction.setStatus(true);
            transactionService.returnBooks(transaction);
            return new ResponseEntity<>("Book" + book.getTitle() + "is returned", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        }
    }
}
