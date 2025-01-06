package coms309.Security;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import coms309.Exceptions.JWTException;
import coms309.Users.User;
import coms309.Users.UserRepository;

import coms309.Users.UserType;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class JWTService {
    @Value("${jwt.signing}")
    private String signingKey;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EncryptionKeyRepository encryptionKeyRepo;

    public String retrieveUserToken(User user) throws JWTException {
        SecretKey encryptionKey = retrieveUserEncryptionKey(user);
        if (encryptionKey == null) {
            throw new JWTException("Encryption key failed to generate or retrieve");
        }

        //ActiveTokens tokenObject = userRepo.getActiveToken(user.getUsername());
        String unencryptedToken = null;
        //if (tokenObject == null) {
            unencryptedToken = generateNewSessionToken(user);
        //} else {
            //byte[] decodedBytes = Base64.getDecoder().decode(tokenObject.getActiveToken());
        //    unencryptedToken = tokenObject.getActiveToken();
        //}

        if (unencryptedToken == null) {
            throw new JWTException("User session token failed to generate or retrieve");
        }

        return encryptString(unencryptedToken, encryptionKey);
    }

    public SecretKey retrieveUserEncryptionKey(User user) {
        EncryptionKey encryptionKeyObject = userRepo.getEncryptionKey(user.getUsername());
        SecretKey encryptionKey = null;
        if (encryptionKeyObject != null) {
            String encodedKey = encryptionKeyObject.getEncryptionKey();
            byte[] decodedBytes = Base64.getDecoder().decode(encodedKey);
            encryptionKey = new SecretKeySpec(decodedBytes, "AES");
            System.out.println("User's encryption key retrieved");
        } else {
            encryptionKey = generateNewEncryptionKey(user);
            System.out.println("New encryption key generated for user");
        }
        return encryptionKey;
    }

    public SecretKey generateNewEncryptionKey(User user) {
        SecretKey encryptionKey = null;
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(new SecureRandom());
            encryptionKey = keyGenerator.generateKey();
            String encodedKey = Base64.getEncoder().encodeToString(encryptionKey.getEncoded());
            encryptionKeyRepo.save(user.getUsername(), encodedKey);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Encryption algorithm not found");
        }
        return encryptionKey;
    }

    public String generateNewSessionToken(User user) {
        String token = null;
        SecretKey signingSecretKey = Keys.hmacShaKeyFor(signingKey.getBytes());
        UserType userType = user.getUserType();
        token = Jwts.builder()
            .subject("SessionToken")
            .claim("type", userType)
            .claim("username", user.getUsername())
            .signWith(signingSecretKey)
            .compact();

//        activeTokensRepo.save(user.getUsername(), token);
        return token;
    }

    public String decryptUserToken(User user, String encryptedToken) {
        String encodedKey = userRepo.getEncryptionKey(user.getUsername()).getEncryptionKey();
        byte[] keyBytes = Base64.getDecoder().decode(encodedKey);
        SecretKey encryptionKey = new SecretKeySpec(keyBytes, "AES");
        return decryptString(encryptedToken, encryptionKey);
    }

    public Jwt<?, ?> parseUserToken(User user, String token) throws SignatureException {
        SecretKey signingSecretKey = Keys.hmacShaKeyFor(signingKey.getBytes());
        return Jwts.parser().verifyWith(signingSecretKey).build().parse(token);
    }

    public boolean checkUserIsType(String username, UserType userType, String encryptedToken) {
        return checkUserIsType(userRepo.find(username), userType, encryptedToken);
    }

    public boolean checkUserIsType(User user, UserType userType, String encryptedToken) {
        if (encryptedToken.contains("Bearer ")) {
            encryptedToken = encryptedToken.replace("Bearer ", "");
        }

        if (!userRepo.exists(user.getUsername())) {
            return false;
        }

        Jwt<?, ?> jwt = null;
        String unencryptedToken = null;
        try {
            unencryptedToken = decryptUserToken(user, encryptedToken);
            jwt = parseUserToken(user, unencryptedToken);
        } catch (SignatureException ex) {
            System.out.println("Signing key did not match this token's signature");
        } catch (IllegalArgumentException ex) {
            System.out.println("Wrong format of user token, likely bad token");
        }

        if (jwt == null) {
            return false;
        }

        JsonObject jwtPayload = JsonParser.parseString(jwt.getPayload().toString()).getAsJsonObject();

        boolean userTypeCorrect = false;
        boolean usernameCorrect = false;

        if (jwtPayload.has("type")) {
            UserType tokenUserType = UserType.valueOf(jwtPayload.get("type").getAsString());
            userTypeCorrect = tokenUserType.equals(userType);
        }
        if (jwtPayload.has("username")) {
            String claimedUsername = jwtPayload.get("username").getAsString();
            usernameCorrect = claimedUsername.equals(user.getUsername());
        }

        return (userTypeCorrect && usernameCorrect);
    }

    public boolean checkUserIdentity(String username, String encryptedToken) {
        return checkUserIdentity(userRepo.find(username), encryptedToken);
    }

    public boolean checkUserIdentity(User user, String encryptedToken) {
        if (encryptedToken.contains("Bearer ")) {
            encryptedToken = encryptedToken.replace("Bearer ", "");
        }

        if (!userRepo.exists(user.getUsername())) {
            return false;
        }

        Jwt<?, ?> jwt = null;
        String unencryptedToken = null;
        try {
            unencryptedToken = decryptUserToken(user, encryptedToken);
            jwt = parseUserToken(user, unencryptedToken);
        } catch (SignatureException ex) {
            System.out.println("Signing key did not match this token's signature");
        } catch (IllegalArgumentException ex) {
            System.out.println("Wrong format of user token, likely bad token");
        }

        if (jwt == null) {
            return false;
        }

        JsonObject jwtPayload = JsonParser.parseString(jwt.getPayload().toString()).getAsJsonObject();

        if (jwtPayload.has("username")) {
            String claimedUsername = jwtPayload.get("username").getAsString();
            return claimedUsername.equals(user.getUsername());
        }

        return false;
    }

    private static String encryptString(String target, SecretKey encryptionKey) {
        String encryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, encryptionKey);
            byte[] encryptedBytes = cipher.doFinal(target.getBytes());
            encryptedString = Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algorithm");
        } catch (NoSuchPaddingException ex) {
            System.out.println("No such padding");
        } catch (InvalidKeyException ex) {
            System.out.println("Invalid key");
        } catch (IllegalBlockSizeException ex) {
            System.out.println("Illegal block size");
        } catch (BadPaddingException ex) {
            System.out.println("Bad padding");
        }
        return encryptedString;
    }

    private static String decryptString(String target, SecretKey encryptionKey) {
        String decryptedString = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, encryptionKey);
            byte[] decoded = Base64.getDecoder().decode(target);
            decryptedString = new String(cipher.doFinal(decoded));
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("No such algorithm");
        } catch (NoSuchPaddingException ex) {
            System.out.println("No such padding");
        } catch (InvalidKeyException ex) {
            System.out.println("Invalid key");
        } catch (IllegalBlockSizeException ex) {
            System.out.println("Illegal block size");
        } catch (BadPaddingException ex) {
            System.out.println("Bad padding");
        }
        return decryptedString;
    }
}
