package com.example.demo.configuration.securties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtils {

  private String SECRET_KEY = "secret";
  private String AUTHORITIES_KEY = "authorities";

  public String generateToken(UserDetails userDetails, Authentication authentication){
    HashMap<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails.getUsername(), authentication);
  }

  private String createToken(HashMap<String, Object> claims, String userName, Authentication authentication){

    final String authorities = authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(","));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    return Jwts.builder().setClaims(claims).setSubject(userName).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)).claim(AUTHORITIES_KEY, authorities)
        .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token){
    return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
  }

  public String extractUsername(String token){
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token){
    return extractClaim(token, Claims::getExpiration);
  }

  private Boolean isTokenExpired(String token){
    return extractExpiration(token).before(new Date());
  }

  public Boolean isvalidToken(String token, UserDetails userDetails){
    final String userName = extractUsername(token);
    return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
