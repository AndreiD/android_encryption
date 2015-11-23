package com.androidadvance.encryptedapi.security;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.Cipher;

public class RSACipher {


    public static class PublicKeyReader {

        public PublicKey get(Context ctx) throws Exception {

            InputStream is = ctx.getAssets().open("public_key.der");
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();

            X509EncodedKeySpec spec =
                    new X509EncodedKeySpec(fileBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePublic(spec);
        }
    }


    public static class PrivateKeyReader {

        public PrivateKey get(Context ctx)
                throws Exception {


            InputStream is = ctx.getAssets().open("private_key.der");
            byte[] fileBytes = new byte[is.available()];
            is.read(fileBytes);
            is.close();

            PKCS8EncodedKeySpec spec =
                    new PKCS8EncodedKeySpec(fileBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            return kf.generatePrivate(spec);

        }
    }


    public static byte[] encrypt(Key publicKey, byte[] toBeCiphred) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return rsaCipher.doFinal(toBeCiphred);
        } catch (Exception e) {
            Log.e("RSACipher", "Error while encrypting data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static byte[] decrypt(Key privateKey, byte[] encryptedText) {
        try {
            Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding", "BC");
            rsaCipher.init(Cipher.DECRYPT_MODE, privateKey);
            return rsaCipher.doFinal(encryptedText);
        } catch (Exception e) {
            Log.e("RSACipher", "Error while decrypting data: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}