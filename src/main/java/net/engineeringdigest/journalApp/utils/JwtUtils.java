package net.engineeringdigest.journalApp.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import net.engineeringdigest.journalApp.entities.BookEntry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    private String SECRET_KEY = "Thiq783+kosd^^ndkwuytpoasnkw^sjhiwkjoo";

    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String extractUsername(String token){
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Date extractExpiration(String token){
        return extractAllClaims(token).getExpiration();
    }

    private Boolean isTokenExpired(String token){
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true; // Token is expired
        }
    }

    public String generateAccessToken(String username) {
        final long accessTokenExpiration = 60 * 1000; // 5 minutes
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username, accessTokenExpiration);
    }

    public String generateRefreshToken(String username) {
        final long refreshTokenExpiration = 60 * 60 * 1000; // 1 hour
        Map<String,Object> claims = new HashMap<>();
        return createToken(claims, username, refreshTokenExpiration);
    }

    public String createToken(Map<String,Object> claims, String subject, Long expirationTime){
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .header().empty().add("typ","JWT")
                .and()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

//    public Boolean validateToken(String token){
//        return !isTokenExpired(token);
//    }

    public boolean validateToken(String token) {
        try {
//            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

}
