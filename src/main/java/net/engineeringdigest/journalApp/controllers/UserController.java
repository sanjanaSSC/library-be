package net.engineeringdigest.journalApp.controllers;


import net.engineeringdigest.journalApp.DTO.*;
import net.engineeringdigest.journalApp.entities.*;
import net.engineeringdigest.journalApp.mappers.EntityToDTOMapper;
import net.engineeringdigest.journalApp.services.*;
import net.engineeringdigest.journalApp.utils.JwtUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtils jwtUtils;

//    @GetMapping
//    public List<User> viewAllUsers(){
//        return userService.getAllUsers();
//    }


//    @GetMapping("id/{myId}")
//    public ResponseEntity<User> getUserById(@PathVariable ObjectId myId){
//        Optional<User> user = userService.findById(myId);
//        if(user.isPresent()){
//            return new ResponseEntity<>(user.get(), HttpStatus.OK);
//        }
//        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//    }




}
