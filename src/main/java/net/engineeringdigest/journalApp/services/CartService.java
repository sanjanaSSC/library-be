package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entities.*;
import net.engineeringdigest.journalApp.repositories.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.*;

import java.util.*;

@Component
@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

//    public List<Cart>getAllCart(){
//        return cartRepository.findAll();
//    }

    public Cart createEmptyCart(String user_id){
        //find the user from userRepository by id
        //if user doesnt exist throw error
        //if found check if cart already exists
        //if yes then return existing car
        //else create new cart
        //for new cart set the user_id
        //save this cart also in user entity
        Optional<User> userOptional = userRepository.findById(user_id);
        User user = userOptional.get();

        if(user == null){
            throw new RuntimeException("User not found");
        }

//        if(user.getCartId() != null){
//            return user.getCart();
//        }

        Cart newCart = new Cart();
        newCart.setUserId(user.getId());
        newCart = cartRepository.save(newCart);

        user.setCartId(newCart.getId());
        userRepository.save(user);

        return new Cart();
    }

    public Cart saveCart(Cart cart){
         return cartRepository.save(cart);
    }

    public List<Cart> findAll(){
        return cartRepository.findAll();
    }

    public void deleteById(String id){
        cartRepository.deleteById(id);
    }

    public Cart findByUserId(String id){
        return cartRepository.findByUserId(id);
    }

    public Optional<Cart> findById(String id){
        return cartRepository.findById(id);
    }

    public Cart saveToCart(Cart cart){
        return cartRepository.save(cart);
    }

    public Cart deleteBookById(String book_id){
        return cartRepository.deleteBookById(book_id);
    }
}

