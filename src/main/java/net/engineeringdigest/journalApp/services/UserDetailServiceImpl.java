package net.engineeringdigest.journalApp.services;

import net.engineeringdigest.journalApp.entities.User;
import net.engineeringdigest.journalApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if(user != null){
            System.out.println("Loaded User: " + user.getUsername());
            System.out.println("Encoded Password: " + user.getPassword());
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
//                    .roles(user.getRoles().toArray(new String[0]))
                    .roles(user.getRoles().stream().map(Enum::name).toArray(String[]::new))
                    .build();


            return userDetails;
        }
        throw new UsernameNotFoundException("User not found with username" + username);
    }
}
