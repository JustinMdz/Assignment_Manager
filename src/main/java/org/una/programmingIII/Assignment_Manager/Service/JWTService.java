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
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String refreshAccessToken(String token) {
        if (isValidToken(token)) {
            String email = getEmailFromToken(token);
            Optional<UserDto> user = userService.getUserByEmail(email);
            return user.map(this::generateAccessToken)
                    .orElseThrow(() -> new BadCredentialsException("Invalid refresh token"));
        } else {
            throw new BadCredentialsException("Invalid refresh token");
        }
    }


}
