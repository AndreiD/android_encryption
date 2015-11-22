package com.androidadvance.encryptedapi.security;

import android.util.Base64;
import android.util.Log;

import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;

import javax.crypto.Cipher;

public class RSACipher {


    public static class PublicKeyReader {

        public PublicKey get() {
            InputStream inputStream2 = getClass().getResourceAsStream("/assets/mypublickey.key");
            InputStreamReader reader = new InputStreamReader(inputStream2);
            BufferedReader br = new BufferedReader(reader);
            StringBuffer sb = new StringBuffer();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            String pk = sb.toString();
            byte[] decoded = Base64.decode(pk, Base64.DEFAULT);
            KeyFactory kf = null;
            try {
                kf = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            RSAPublicKey pubKey = null;
            try {
                pubKey = (RSAPublicKey) kf.generatePublic(new X509EncodedKeySpec(decoded));
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
            return pubKey;
        }
    }


    public static class PrivateKeyReader {

        public PrivateKey get(String filename)
                throws Exception {

            File f = new File(filename);
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            byte[] keyBytes = new byte[(int) f.length()];
            dis.readFully(keyBytes);
            dis.close();

            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
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
