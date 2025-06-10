package net.engineeringdigest.journalApp.controllers;

import net.engineeringdigest.journalApp.entities.BookEntry;
import net.engineeringdigest.journalApp.entities.Cart;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.services.BookEntryService;
import net.engineeringdigest.journalApp.services.CartService;
import net.engineeringdigest.journalApp.services.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BookEntryService bookEntryService;

    @Autowired
    private CartService cartService;



    @GetMapping("/all-users")
    public ResponseEntity<?> viewAllUsers(){
        List<User> all = userService.getAllUsers();
        if(all != null && !all.isEmpty()){
            return new ResponseEntity<>(all, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create-book")
    public ResponseEntity<BookEntry> createBookEntry(@RequestBody BookEntry myEntry){
        try{
            bookEntryService.saveEntry(myEntry);
            return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete-book/id/{myId}")
    public ResponseEntity<?> deleteBookEntryById(@PathVariable String myId){
        Optional<BookEntry> bookEntryOptional = bookEntryService.findById(myId);
        if(!bookEntryOptional.isPresent()){
            return new ResponseEntity<>("Book not found",HttpStatus.NOT_FOUND);
        }
        List<Cart> allCarts= cartService.findAll();
        List<String>updatedCartIds = new ArrayList<>();
        for(Cart cart: allCarts){
            boolean isUpdated = cart.getBookEntries().removeIf(book -> book.getId().equals(myId));
            if (isUpdated) {
                updatedCartIds.add(cart.getId());
                cartService.saveCart(cart);
            }
        }
        bookEntryService.deleteById(myId);
        return new ResponseEntity<>("BookEntry deleted successfully. Updated carts for users: " + updatedCartIds,HttpStatus.OK);
    }

    @PutMapping("/update-book/id/{myId}")
    public ResponseEntity<?> updateBookEntryById(@PathVariable String myId, @RequestBody BookEntry newEntry){
//        return bookEntries.put(myId, myEntry);

        BookEntry old = bookEntryService.findById(myId).orElse(null);
        if(old != null){
            old.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().equals("") ? newEntry.getTitle() : old.getTitle());
            old.setAuthor(newEntry.getAuthor() != null && !newEntry.getAuthor().equals("") ? newEntry.getAuthor() : old.getAuthor());
            bookEntryService.saveEntry(old);
            return new ResponseEntity<BookEntry>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-user/id/{myId}")
    public ResponseEntity<User> getUserById(@PathVariable String myId){
        Optional<User> user = userService.findById(myId);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/user-delete/id/{myId}")
    public ResponseEntity<?> deleteUserById(@PathVariable String myId){
        //get the cartId
        Optional<User> userOptional = userService.findById(myId);
        if(!userOptional.isPresent()){
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        String cart_id = userOptional.get().getCartId();
        userService.deleteById(myId);
        cartService.deleteById(cart_id);
        return new ResponseEntity<>("User and its cart deleted successfully",HttpStatus.OK);
    }

    @PutMapping("/user-update/id/{myId}")
    public ResponseEntity<?> updateUserById(@RequestBody User user){
        User userInDB = userService.findByUserName(user.getUsername());
        if(userInDB != null){
            userInDB.setUsername(user.getUsername());
            userInDB.setPassword(user.getPassword());
            userService.saveUser(userInDB);
            return new ResponseEntity<>(userInDB,HttpStatus.OK);
        }
        return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    }



}
