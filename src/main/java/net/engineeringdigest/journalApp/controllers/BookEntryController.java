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
@RequestMapping("/book")
public class BookEntryController {

    @Autowired
    private BookEntryService bookEntryService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

//    @GetMapping
//    public ResponseEntity<?> viewAllBooks(){
//        try{
//            return new ResponseEntity<>(bookEntryService.getAllBooks(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
//        }
//    }

    @GetMapping("/myBooks")
    public ResponseEntity<?> getAllBooksOfUser(){
        System.out.println("Incoming request: " + SecurityContextHolder.getContext().getAuthentication());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return new ResponseEntity<>("User not authenticated", HttpStatus.UNAUTHORIZED);
        }
        System.out.println(SecurityContextHolder.getContext().getAuthentication() + "Authentication");

        String userName = authentication.getName();
        User user = userService.findByUserName(userName);
        Optional<Cart> cart = cartService.findById(user.getCartId());
        List<BookEntry> all = cart.get().getBookEntries();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }




    @GetMapping("id/{myId}")
    public ResponseEntity<BookEntry> getBookEntryById(@PathVariable String myId){
//       return bookEntries.get(myId);
        Optional<BookEntry> bookEntry = bookEntryService.findById(myId);
        if(bookEntry.isPresent()){
            return new ResponseEntity<>(bookEntry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }







}
