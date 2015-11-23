package com.androidadvance.encryptedapi.security;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;


//Generate a 2048-bit RSA private key
//$ openssl genrsa -out private_key.pem 2048
//Convert private Key to PKCS#8 format (so Java can read it)
//$ openssl pkcs8 -topk8 -inform PEM -outform DER -in private_key.pem \ -out private_key.der -nocrypt
//Output public key portion in DER format (so Java can read it)
//$ openssl rsa -in private_key.pem -pubout -outform DER -out public_key.der

public class RSAHelper {


    public PublicKey getPublicKey(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("public_key.der");
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();
            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(fileBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }



    public PrivateKey getPrivateKey(Context ctx) {
        try {
            InputStream is = ctx.getAssets().open("private_key.der");
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();

            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(fileBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] encrypt(Key publicKey, byte[] toBeCiphred) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return rsaCipher.doFinal(toBeCiphred);
        } catch (Exception e) {
            Log.e("RSAHelper", "Error while encrypting data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(Key privateKey, byte[] encryptedText) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return rsaCipher.doFinal(encryptedText);
        } catch (Exception e) {
            Log.e("RSAHelper", "Error while decrypting data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}