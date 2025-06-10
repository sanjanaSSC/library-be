package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entities.BookEntry;
import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repositories.BookRepository;
import net.engineeringdigest.journalApp.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

//    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final PasswordEncoder passwordEncoder;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User saveUser(User user){
        try{
            if (!user.getPassword().startsWith("$2a$")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            String hashedPassword = user.getPassword();
            System.out.println(passwordEncoder.matches("12345", hashedPassword) + " Matched");

            System.out.println("hashed Password: " + user.getPassword());

            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(Collections.singletonList(User.Role.USER));
            }
            return userRepository.save(user);
        }catch (Exception e){
            logger.info("User already exists");
            return null;
        }
    }




    public User saveNewUser(User user){
        return userRepository.save(user);
    }


    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void deleteById(String id){
        userRepository.deleteById(id);
    }

    public User findByUserName(String username){
        return userRepository.findByUsername(username);
    }

    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }
}