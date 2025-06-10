package net.engineeringdigest.journalApp.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.DTO.UserDTO;
import net.engineeringdigest.journalApp.entities.*;
import net.engineeringdigest.journalApp.mappers.EntityToDTOMapper;
import net.engineeringdigest.journalApp.services.*;
import net.engineeringdigest.journalApp.utils.JwtUtils;
import org.bson.types.ObjectId;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/public")
@Slf4j
public class PublicController {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @Autowired
    private BookEntryService bookEntryService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RedisService redisService;

//    @Autowired
//    private ElasticsearchRepository elasticsearchRepository;

//    @Autowired
//    private ElasticsearchService elasticsearchService;

    private static final Logger logger = LoggerFactory.getLogger(PublicController.class);


    @GetMapping("/health-check")
    public String healthCheck(){
        return "ok";
    }

    @PostMapping("/sign-up")
    @Transactional
    public ResponseEntity<?> signUp(@Valid @RequestBody User myUser){
        try {
            if (userService.existsByUsername(myUser.getUsername())) {
                log.error("User {} already exists", myUser.getUsername());
                return new ResponseEntity<>("Username already exists", HttpStatus.BAD_REQUEST);
            }
            User savedUser = userService.saveUser(myUser);

            Cart cart = new Cart();
            cart.setUserId(savedUser.getId());
            Cart savedCart = cartService.saveCart(cart);
//        // Step 3: Create and save an initial transaction (if needed)
//        Transaction transaction = new Transaction();
//        transaction.setUser(savedUser);
//        Transaction savedTransaction = transactionService.saveTransaction(transaction);
//
//        // Step 4: Update the user's relationships
            savedUser.setCartId(savedCart.getId()); // Attach the cart to the user
//        savedUser.setTransactions(List.of(savedTransaction)); // Attach transactions to the user
            userService.saveUser(savedUser); // Save the user again with updated relationships

            UserDTO userDTO = EntityToDTOMapper.mapToUserDTO(savedUser);

            return new ResponseEntity<>(userDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error during user sign-up: {}", e.getMessage(), e);
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view-books")
    public ResponseEntity<?> viewAllBooks(){
        try{
            return new ResponseEntity<>(bookEntryService.getAllBooks(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody User user){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            UserDetails userDetails = userDetailService.loadUserByUsername(user.getUsername());
            String accessToken = jwtUtils.generateAccessToken(userDetails.getUsername());
            String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);

            return ResponseEntity.ok(tokens);

//            User userInDB = userService.findByUserName(userDetails.getUsername());
//
//            UserDTO userDTO = EntityToDTOMapper.mapToUserDTO(userInDB);
//            userDTO.setJwt(jwt);
//
//            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Incorrect username or password", HttpStatus.BAD_REQUEST);        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestParam String refreshToken){
        if (!jwtUtils.validateToken(refreshToken)) {
            return new ResponseEntity<>("Invalid refresh token", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtUtils.extractUsername(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(username);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", newAccessToken);

        return ResponseEntity.ok(tokens);
    }

    @GetMapping("/genres")
    public List<String> getDistinctGenres() {
        List<String> genres = mongoTemplate.query(BookEntry.class)
                .distinct("genre")
                .as(String.class)
                .all();
        genres.add(0, "All");
        return genres;
    }

    @GetMapping("/search")
    public List<BookEntry> searchBooks(@RequestParam(value = "title", required = false) String title,
                                       @RequestParam(value = "author", required = false) String author,
                                       @RequestParam(value = "genre", required = false) String genre){


        if(title!= null){
            List<BookEntry> allBooksCache = redisService.get(title, new TypeReference<List<BookEntry>>() {});
            if(allBooksCache != null){
                return allBooksCache;
            }else {
                List<BookEntry> allBooks = bookEntryService.searchBooksByTitle(title);
                if(allBooks != null){
                    redisService.set(title, allBooks, 300l);
                }
                return allBooks;
            }
        }else if(author!= null){
            return bookEntryService.searchBooksByAuthor(author);
        }else if(genre!= null) {
            if("All".equalsIgnoreCase(genre)) {
                return mongoTemplate.findAll(BookEntry.class);
            } else{
                return bookEntryService.searchBooksByGenre(genre);
            }
        }else{
            return null;
        }
    }

//    @GetMapping("/search")
//    public List<ElasticBookEntry> searchBooksByTitle(String title) {
//        List<ElasticBookEntry> books =  elasticsearchService.findByTitle(title);
//        return books;
//    }

}


