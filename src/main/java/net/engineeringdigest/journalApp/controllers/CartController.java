package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entities.*;
import net.engineeringdigest.journalApp.services.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookEntryService bookEntryService;

    @Autowired
    private UserService userService;
//    @GetMapping
//    public List<Cart> viewCart(){
//        return cartService.getAllCart();
//    }

    @PostMapping
    public ResponseEntity<?> createEmptyCart(@PathVariable String user_id){
        try{
            Cart cart = cartService.createEmptyCart(user_id);
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(e,HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<?> getCartByUserId(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        Cart cart = cartService.findByUserId(user.getId());
        if(cart != null){
            return new ResponseEntity<>(cart, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/add/{book_id}")
    public ResponseEntity<?> addBookToCart(@PathVariable String book_id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        Cart cart = cartService.findByUserId(user.getId());
        if(cart == null){
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }

        Optional<BookEntry> bookEntryOptional = bookEntryService.findById(book_id);
        if (!bookEntryOptional.isPresent()) {
            return new ResponseEntity<>("Book not found", HttpStatus.NOT_FOUND);
        }
        BookEntry bookEntry = bookEntryOptional.get();


        cart.getBookEntries().add(bookEntry);
        Cart updatedCart = cartService.saveToCart(cart);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{book_id}")
    public ResponseEntity<?> deleteBookFromCart(@PathVariable String book_id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        Cart cart = cartService.findByUserId(user.getId());

        if(cart == null){
            return new ResponseEntity<>("Cart not found", HttpStatus.NOT_FOUND);
        }

        boolean bookRemoved = cart.getBookEntries().removeIf(book -> book != null && book.getId().equals(book_id));

        if(!bookRemoved){
            return new ResponseEntity<>("Book not found in the cart", HttpStatus.NOT_FOUND);
        }
        cartService.saveCart(cart);
        return new ResponseEntity<>(cart.getBookEntries(),HttpStatus.OK);
    }
}
