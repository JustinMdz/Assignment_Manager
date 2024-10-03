package org.una.programmingIII.Assignment_Manager.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class PasswordEncryptionService {

    private final PasswordEncoder bcryptEncoder;

    public PasswordEncryptionService() {

        this.bcryptEncoder = new BCryptPasswordEncoder();
    }

    public String encodePassword(String plainPassword) {
        String sha256Encoded = encodeWithSHA256(plainPassword);
        return bcryptEncoder.encode(sha256Encoded);
    }

    public boolean matches(String plainPassword, String encodedPassword) {
        String sha256Encoded = encodeWithSHA256(plainPassword);
        return bcryptEncoder.matches(sha256Encoded, encodedPassword);
    }

    private String encodeWithSHA256(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(plainPassword.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error en la encriptación SHA-256", e);
        }
    }
}
