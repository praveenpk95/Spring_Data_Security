package com.example.demo.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {

	// The Secret Key which is used to generate JWT, with this secret signature
    private String SECRET_KEY = "secret";

    // Takes in token and returns username
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Takes in a token, uses claims resolver to figure out what the claims are.
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // takes token and returns if token is expired or not
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Takes the user details, return the jwt.
    // Calls createToken, and uses Map of claims, here it is empty, can be added if needed.
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    // Call JWT api and sets the values needed and sends the jwt token, takes the secret key also. (signwith method)
    // sets the subject with the userName received. sets the issed date.
    // sets the expiration date(usually current date + the time mentioned, here 10 hours)
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }
    
    
    // Validates the token received.
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}