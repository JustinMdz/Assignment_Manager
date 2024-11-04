package org.una.programmingIII.Assignment_Manager.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.una.programmingIII.Assignment_Manager.Dto.UserDto;
import io.jsonwebtoken.ExpiredJwtException;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

@Service
public class JWTService {

    private final Key key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;
    private final UserService userService;

    @Autowired
    public JWTService(@Value("${jwt.secret}") String secretKey,
                      @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
                      @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration, UserService userService) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.userService = userService;
    }

    public String generateAccessToken(UserDto user) {
        return generateToken(user.getEmail(), accessTokenExpiration);
    }

    public String generateRefreshToken(UserDto user) {
        return generateToken(user.getEmail(), refreshTokenExpiration);
    }

    public String generateToken(String email, long expirationTime) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }


    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new BadCredentialsException("Invalid token", e);
        }
    }


    public String refreshAccessToken(String token) {
        if (isTokenExpiredButValid(token)) {
            String email = getEmailFromToken(token);
            return generateToken(email,accessTokenExpiration);
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }

    public boolean isTokenExpiredButValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            return false;
        }
    }


}
