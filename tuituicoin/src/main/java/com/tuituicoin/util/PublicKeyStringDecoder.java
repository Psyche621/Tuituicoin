package com.tuituicoin.util;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyStringDecoder {
    /* Parses a public key in string form for reconstruction from database */
    public static PublicKey stringToPublicKey(String publicKeyString) {
        // Remove PEM headers and footers if necessary
        String keyPEM = publicKeyString
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replaceAll("\\s", "");
        
        try {
            byte[] keyBytes = Base64.getDecoder().decode(keyPEM);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("No such algorithm");
            e.printStackTrace();
            return null;
        } catch (InvalidKeySpecException e) {
            System.out.println("Invalid key spec in string parser");
            e.printStackTrace();
            return null;
        }
    }
}
