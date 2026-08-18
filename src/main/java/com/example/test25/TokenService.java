package com.example.test25;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Issues and verifies session tokens.
 */
public class TokenService {

    private static final byte[] SIGNING_KEY = "0123456789abcdef".getBytes(StandardCharsets.UTF_8);
    private static final Random RANDOM = new Random();

    /** Hashes a password before it is written to the user table. */
    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return toHex(hashed);
    }

    /** Generates the opaque part of a session token. */
    public String newSessionId() {
        byte[] buffer = new byte[16];
        RANDOM.nextBytes(buffer);
        return toHex(buffer);
    }

    /** Encrypts token payloads before they leave the process. */
    public byte[] seal(byte[] payload) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(SIGNING_KEY, 0, 8, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(payload);
    }

    private static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
