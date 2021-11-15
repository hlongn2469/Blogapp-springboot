package com.springboot.blog.security;

import com.springboot.blog.exception.BlogAPIException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwt_secret;

    @Value("${app.jwt-expiration-milliseconds}")
    private int jwt_expire;

    // generate token
    public String generateToken(Authentication auth){
        String username = auth.getName();
        Date current_date = new Date();
        Date expired_date = new Date(current_date.getTime() + jwt_expire);
        String token = Jwts.builder().setSubject(username).
                setIssuedAt(new Date()).setExpiration(expired_date).
                signWith(SignatureAlgorithm.HS512, jwt_secret).compact();
        return token;
    }

    // get username from token
    public String getUserNameFromJWT(String token){
        Claims claims = Jwts.parser().setSigningKey(jwt_secret).
                parseClaimsJws(token).getBody();
        return claims.getSubject();


    }

    // validate JWT token
    public boolean validateToken(String token){
        try{
            Jwts.parser().setSigningKey(jwt_secret).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }

    }
}
